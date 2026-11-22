/*     */ package net.minecraft.world.entity.animal.allay;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.EntityTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.GoAndGiveItemsToTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.GoToWantedItem;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StayCloseToTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public class AllayAi {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_DEPOSIT_TARGET = 2.25F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_RETRIEVING_ITEM = 1.75F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.5F;
/*     */   
/*     */   private static final int CLOSE_ENOUGH_TO_TARGET = 4;
/*     */   
/*     */   private static final int TOO_FAR_FROM_TARGET = 16;
/*     */   
/*     */   private static final int MAX_LOOK_DISTANCE = 6;
/*     */   
/*     */   private static final int MIN_WAIT_DURATION = 30;
/*     */   
/*     */   private static final int MAX_WAIT_DURATION = 60;
/*     */   
/*     */   private static final int TIME_TO_FORGET_NOTEBLOCK = 600;
/*     */   
/*     */   private static final int DISTANCE_TO_WANTED_ITEM = 32;
/*     */   
/*     */   private static final int GIVE_ITEM_TIMEOUT_DURATION = 20;
/*     */   
/*     */   protected static List<ActivityData<Allay>> getActivities() {
/*  61 */     return List.of(initCoreActivity(), initIdleActivity());
/*     */   }
/*     */   
/*     */   private static ActivityData<Allay> initCoreActivity() {
/*  65 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new AnimalPanic(2.5F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Allay> initIdleActivity() {
/*  76 */     return ActivityData.create(Activity.IDLE, 0, ImmutableList.of(
/*  77 */           GoToWantedItem.create(mob -> true, 1.75F, true, 32), new GoAndGiveItemsToTarget(AllayAi::getItemDepositPosition, 2.25F, 20, AllayAi::onItemThrown), 
/*     */           
/*  79 */           StayCloseToTarget.create(AllayAi::getItemDepositPosition, Predicate.not(AllayAi::hasWantedItem), 4, 16, 2.25F), 
/*  80 */           SetEntityLookTargetSometimes.create(6.0F, UniformInt.of(30, 60)), new RunOne(
/*  81 */             (List)ImmutableList.of(
/*  82 */               Pair.of(RandomStroll.fly(1.0F), Integer.valueOf(2)), 
/*  83 */               Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(2)), 
/*  84 */               Pair.of(new DoNothing(30, 60), Integer.valueOf(1))))));
/*     */   }
/*     */   
/*     */   public static void updateActivity(Allay body) {
/*  90 */     body.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.IDLE));
/*     */   }
/*     */   
/*     */   public static void hearNoteblock(LivingEntity allay, BlockPos pos) {
/*  96 */     Brain<?> brain = allay.getBrain();
/*  97 */     GlobalPos globalPos = GlobalPos.of(allay.level().dimension(), pos);
/*  98 */     Optional<GlobalPos> likedNoteblockPos = brain.getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
/*  99 */     if (likedNoteblockPos.isEmpty()) {
/* 100 */       brain.setMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION, globalPos);
/* 101 */       brain.setMemory(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, Integer.valueOf(600));
/* 102 */     } else if (((GlobalPos)likedNoteblockPos.get()).equals(globalPos)) {
/* 103 */       brain.setMemory(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, Integer.valueOf(600));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Optional<PositionTracker> getItemDepositPosition(LivingEntity allay) {
/* 108 */     Brain<?> brain = allay.getBrain();
/* 110 */     Optional<GlobalPos> likedNoteblockPos = brain.getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
/* 111 */     if (likedNoteblockPos.isPresent()) {
/* 112 */       GlobalPos position = likedNoteblockPos.get();
/* 113 */       if (shouldDepositItemsAtLikedNoteblock(allay, brain, position))
/* 114 */         return (Optional)Optional.of(new BlockPosTracker(position.pos().above())); 
/* 116 */       brain.eraseMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
/*     */     } 
/* 119 */     return getLikedPlayerPositionTracker(allay);
/*     */   }
/*     */   
/*     */   private static boolean hasWantedItem(LivingEntity allay) {
/* 123 */     Brain<?> brain = allay.getBrain();
/* 124 */     return brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
/*     */   }
/*     */   
/*     */   private static boolean shouldDepositItemsAtLikedNoteblock(LivingEntity allay, Brain<?> brain, GlobalPos likedNoteblockPos) {
/* 128 */     Optional<Integer> likedNoteblockCooldown = brain.getMemory(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS);
/* 129 */     Level level = allay.level();
/* 130 */     return (likedNoteblockPos.isCloseEnough(level.dimension(), allay.blockPosition(), 1024) && level
/* 131 */       .getBlockState(likedNoteblockPos.pos()).is(Blocks.NOTE_BLOCK) && likedNoteblockCooldown
/* 132 */       .isPresent());
/*     */   }
/*     */   
/*     */   private static Optional<PositionTracker> getLikedPlayerPositionTracker(LivingEntity allay) {
/* 136 */     return getLikedPlayer(allay).map(serverPlayer -> new EntityTracker((Entity)serverPlayer, true));
/*     */   }
/*     */   
/*     */   public static Optional<ServerPlayer> getLikedPlayer(LivingEntity allay) {
/* 140 */     Level level = allay.level();
/* 141 */     if (!level.isClientSide() && level instanceof ServerLevel) {
/* 141 */       ServerLevel serverLevel = (ServerLevel)level;
/* 142 */       Optional<UUID> likedPlayer = allay.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
/* 143 */       if (likedPlayer.isPresent()) {
/* 144 */         Entity entity = serverLevel.getEntity(likedPlayer.get());
/* 146 */         if (entity instanceof ServerPlayer) {
/* 146 */           ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 146 */           if ((serverPlayer.gameMode
/* 147 */             .isSurvival() || serverPlayer.gameMode.isCreative()) && serverPlayer
/* 148 */             .closerThan((Entity)allay, 64.0D))
/* 149 */             return Optional.of(serverPlayer); 
/*     */         } 
/* 151 */         return Optional.empty();
/*     */       } 
/*     */     } 
/* 155 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private static void onItemThrown(ServerLevel level, Allay thrower, ItemStack item, BlockPos targetPos) {
/* 159 */     getLikedPlayer((LivingEntity)thrower).ifPresent(player -> CriteriaTriggers.ALLAY_DROP_ITEM_ON_BLOCK.trigger(player, targetPos.below(), (ItemInstance)item));
/* 160 */     if (level.getGameTime() % 7L == 0L && level.getRandom().nextDouble() < 0.9D) {
/* 161 */       float pitch = ((Float)Util.getRandom((List)Allay.THROW_SOUND_PITCHES, level.getRandom())).floatValue();
/* 162 */       level.playSound(null, (Entity)thrower, SoundEvents.ALLAY_THROW, SoundSource.NEUTRAL, 1.0F, pitch);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\allay\AllayAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */