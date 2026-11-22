/*     */ package net.minecraft.world.entity.animal.golem;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.TransportItemsBetweenContainers;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class CopperGolemAi {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 1.5F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
/*     */   
/*     */   private static final int TRANSPORT_ITEM_HORIZONTAL_SEARCH_RADIUS = 32;
/*     */   
/*     */   private static final int TRANSPORT_ITEM_VERTICAL_SEARCH_RADIUS = 8;
/*     */   
/*     */   private static final int TICK_TO_START_ON_REACHED_INTERACTION = 1;
/*     */   
/*     */   private static final int TICK_TO_PLAY_ON_REACHED_SOUND = 9;
/*     */   
/*     */   private static final Predicate<BlockState> TRANSPORT_ITEM_SOURCE_BLOCK;
/*     */   
/*     */   private static final Predicate<BlockState> TRANSPORT_ITEM_DESTINATION_BLOCK;
/*     */   
/*     */   static {
/*  45 */     TRANSPORT_ITEM_SOURCE_BLOCK = (block -> block.is(BlockTags.COPPER_CHESTS));
/*  46 */     TRANSPORT_ITEM_DESTINATION_BLOCK = (block -> (block.is(Blocks.CHEST) || block.is(Blocks.TRAPPED_CHEST)));
/*     */   }
/*     */   
/*     */   protected static List<ActivityData<CopperGolem>> getActivities() {
/*  49 */     return List.of(
/*  50 */         initCoreActivity(), 
/*  51 */         initIdleActivity());
/*     */   }
/*     */   
/*     */   public static void updateActivity(CopperGolem body) {
/*  56 */     body.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.IDLE));
/*     */   }
/*     */   
/*     */   private static ActivityData<CopperGolem> initCoreActivity() {
/*  60 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new AnimalPanic(1.5F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), 
/*     */           
/*  64 */           InteractWithDoor.create(), new CountDownCooldownTicks(MemoryModuleType.GAZE_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.TRANSPORT_ITEMS_COOLDOWN_TICKS)));
/*     */   }
/*     */   
/*     */   private static ActivityData<CopperGolem> initIdleActivity() {
/*  71 */     return ActivityData.create(Activity.IDLE, ImmutableList.of(
/*  72 */           Pair.of(Integer.valueOf(0), new TransportItemsBetweenContainers(1.0F, TRANSPORT_ITEM_SOURCE_BLOCK, TRANSPORT_ITEM_DESTINATION_BLOCK, 32, 8, 
/*  73 */               getTargetReachedInteractions(), onTravelling(), shouldQueueForTarget())), 
/*  74 */           Pair.of(Integer.valueOf(1), SetEntityLookTargetSometimes.create(EntityTypes.PLAYER, 6.0F, UniformInt.of(40, 80))), 
/*  75 */           Pair.of(Integer.valueOf(2), new RunOne(
/*  76 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.TRANSPORT_ITEMS_COOLDOWN_TICKS, MemoryStatus.VALUE_PRESENT), 
/*     */               
/*  80 */               (List)ImmutableList.of(
/*  81 */                 Pair.of(RandomStroll.stroll(1.0F, 2, 2), Integer.valueOf(1)), 
/*  82 */                 Pair.of(new DoNothing(30, 60), Integer.valueOf(1)))))));
/*     */   }
/*     */   
/*     */   private static Map<TransportItemsBetweenContainers.ContainerInteractionState, TransportItemsBetweenContainers.OnTargetReachedInteraction> getTargetReachedInteractions() {
/*  89 */     return Map.of(TransportItemsBetweenContainers.ContainerInteractionState.PICKUP_ITEM, 
/*  90 */         onReachedTargetInteraction(CopperGolemState.GETTING_ITEM, SoundEvents.COPPER_GOLEM_ITEM_GET), TransportItemsBetweenContainers.ContainerInteractionState.PICKUP_NO_ITEM, 
/*  91 */         onReachedTargetInteraction(CopperGolemState.GETTING_NO_ITEM, SoundEvents.COPPER_GOLEM_ITEM_NO_GET), TransportItemsBetweenContainers.ContainerInteractionState.PLACE_ITEM, 
/*  92 */         onReachedTargetInteraction(CopperGolemState.DROPPING_ITEM, SoundEvents.COPPER_GOLEM_ITEM_DROP), TransportItemsBetweenContainers.ContainerInteractionState.PLACE_NO_ITEM, 
/*  93 */         onReachedTargetInteraction(CopperGolemState.DROPPING_NO_ITEM, SoundEvents.COPPER_GOLEM_ITEM_NO_DROP));
/*     */   }
/*     */   
/*     */   private static TransportItemsBetweenContainers.OnTargetReachedInteraction onReachedTargetInteraction(CopperGolemState state, SoundEvent sound) {
/*  97 */     return (body, target, ticksSinceReachingTarget) -> {
/*     */         if (body instanceof CopperGolem) {
/*     */           CopperGolem copperGolem = (CopperGolem)body;
/*     */           Container container = target.container();
/*     */           if (ticksSinceReachingTarget.intValue() == 1) {
/*     */             container.startOpen(copperGolem);
/*     */             copperGolem.setOpenedChestPos(target.pos());
/*     */             copperGolem.setState(state);
/*     */           } 
/*     */           if (ticksSinceReachingTarget.intValue() == 9 && sound != null)
/*     */             copperGolem.playSound(sound); 
/*     */           if (ticksSinceReachingTarget.intValue() == 60) {
/*     */             if (container.getEntitiesWithContainerOpen().contains(body))
/*     */               container.stopOpen(copperGolem); 
/*     */             copperGolem.clearOpenedChestPos();
/*     */           } 
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   private static Consumer<PathfinderMob> onTravelling() {
/* 121 */     return body -> {
/*     */         if (body instanceof CopperGolem) {
/*     */           CopperGolem copperGolem = (CopperGolem)body;
/*     */           copperGolem.clearOpenedChestPos();
/*     */           copperGolem.setState(CopperGolemState.IDLE);
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   private static Predicate<TransportItemsBetweenContainers.TransportItemTarget> shouldQueueForTarget() {
/* 130 */     return transportTarget -> {
/*     */         BlockEntity patt0$temp = transportTarget.blockEntity();
/*     */         if (patt0$temp instanceof ChestBlockEntity) {
/*     */           ChestBlockEntity chestBlockEntity = (ChestBlockEntity)patt0$temp;
/*     */           return !chestBlockEntity.getEntitiesWithContainerOpen().isEmpty();
/*     */         } 
/*     */         return false;
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\golem\CopperGolemAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */