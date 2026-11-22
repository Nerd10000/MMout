/*     */ package net.minecraft.world.entity.ai.sensing;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
/*     */ import net.minecraft.world.entity.monster.piglin.Piglin;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinBrute;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class PiglinSpecificSensor extends Sensor<LivingEntity> {
/*     */   public Set<MemoryModuleType<?>> requires() {
/*  33 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, (Object[])new MemoryModuleType[] { MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_REPELLENT });
/*     */   }
/*     */   
/*     */   protected void doTick(ServerLevel level, LivingEntity body) {
/*  54 */     Brain<?> brain = body.getBrain();
/*  56 */     brain.setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(level, body));
/*  58 */     Optional<Mob> nemesis = Optional.empty();
/*  59 */     Optional<Hoglin> huntableHoglin = Optional.empty();
/*  60 */     Optional<Hoglin> babyHoglin = Optional.empty();
/*  61 */     Optional<Piglin> babyPiglin = Optional.empty();
/*  62 */     Optional<LivingEntity> zombified = Optional.empty();
/*  63 */     Optional<Player> playerNotWearingGold = Optional.empty();
/*  64 */     Optional<Player> playerHoldingWantedItem = Optional.empty();
/*  65 */     int visibleAdultHoglinCount = 0;
/*  67 */     List<AbstractPiglin> visibleAdultPiglins = new ArrayList<>();
/*  70 */     NearestVisibleLivingEntities visibleLivingEntities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
/*  71 */     for (LivingEntity entity : visibleLivingEntities.findAll(ignored -> true)) {
/*  72 */       if (entity instanceof Hoglin) {
/*  72 */         Hoglin hoglin = (Hoglin)entity;
/*  73 */         if (hoglin.isBaby() && babyHoglin.isEmpty()) {
/*  74 */           babyHoglin = Optional.of(hoglin);
/*     */           continue;
/*     */         } 
/*  75 */         if (hoglin.isAdult()) {
/*  76 */           visibleAdultHoglinCount++;
/*  77 */           if (huntableHoglin.isEmpty() && hoglin.canBeHunted())
/*  78 */             huntableHoglin = Optional.of(hoglin); 
/*     */         } 
/*     */         continue;
/*     */       } 
/*  81 */       if (entity instanceof PiglinBrute) {
/*  81 */         PiglinBrute piglinBrute = (PiglinBrute)entity;
/*  82 */         visibleAdultPiglins.add(piglinBrute);
/*     */         continue;
/*     */       } 
/*  83 */       if (entity instanceof Piglin) {
/*  83 */         Piglin piglin = (Piglin)entity;
/*  84 */         if (piglin.isBaby() && babyPiglin.isEmpty()) {
/*  85 */           babyPiglin = Optional.of(piglin);
/*     */           continue;
/*     */         } 
/*  86 */         if (piglin.isAdult())
/*  87 */           visibleAdultPiglins.add(piglin); 
/*     */         continue;
/*     */       } 
/*  89 */       if (entity instanceof Player) {
/*  89 */         Player player = (Player)entity;
/*  90 */         if (playerNotWearingGold.isEmpty() && !PiglinAi.isWearingSafeArmor((LivingEntity)player) && body.canAttack(entity))
/*  91 */           playerNotWearingGold = Optional.of(player); 
/*  93 */         if (playerHoldingWantedItem.isEmpty() && !player.isSpectator() && PiglinAi.isPlayerHoldingLovedItem((LivingEntity)player))
/*  94 */           playerHoldingWantedItem = Optional.of(player); 
/*     */         continue;
/*     */       } 
/*  96 */       if (nemesis.isEmpty() && (entity instanceof net.minecraft.world.entity.monster.skeleton.WitherSkeleton || entity instanceof net.minecraft.world.entity.boss.wither.WitherBoss)) {
/*  97 */         nemesis = Optional.of((Mob)entity);
/*     */         continue;
/*     */       } 
/*  98 */       if (zombified.isEmpty() && PiglinAi.isZombified((Entity)entity))
/*  99 */         zombified = Optional.of(entity); 
/*     */     } 
/* 103 */     List<AbstractPiglin> adultPiglins = PiglinAi.findNearbyAdultPiglins(brain);
/* 105 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, nemesis);
/* 106 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, huntableHoglin);
/* 107 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, babyHoglin);
/* 108 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, zombified);
/* 109 */     brain.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, playerNotWearingGold);
/* 110 */     brain.setMemory(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, playerHoldingWantedItem);
/* 111 */     brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, adultPiglins);
/* 112 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, visibleAdultPiglins);
/* 113 */     brain.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, Integer.valueOf(visibleAdultPiglins.size()));
/* 114 */     brain.setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, Integer.valueOf(visibleAdultHoglinCount));
/*     */   }
/*     */   
/*     */   private static Optional<BlockPos> findNearestRepellent(ServerLevel level, LivingEntity body) {
/* 118 */     return BlockPos.findClosestMatch(body
/* 119 */         .blockPosition(), 8, 4, pos -> isValidRepellent(level, pos));
/*     */   }
/*     */   
/*     */   private static boolean isValidRepellent(ServerLevel level, BlockPos pos) {
/* 127 */     BlockState blockState = level.getBlockState(pos);
/* 128 */     boolean isRepellent = blockState.is(BlockTags.PIGLIN_REPELLENTS);
/* 129 */     if (isRepellent && blockState.is(Blocks.SOUL_CAMPFIRE))
/* 130 */       return CampfireBlock.isLitCampfire(blockState); 
/* 132 */     return isRepellent;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\PiglinSpecificSensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */