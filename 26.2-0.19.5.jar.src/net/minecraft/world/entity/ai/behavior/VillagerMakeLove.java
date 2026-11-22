/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ 
/*     */ public class VillagerMakeLove extends Behavior<Villager> {
/*     */   private long birthTimestamp;
/*     */   
/*     */   public VillagerMakeLove() {
/*  28 */     super(
/*  29 */         (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT), 350, 350);
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/*  40 */     return isBreedingPossible(body);
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
/*  45 */     return (timestamp <= this.birthTimestamp && isBreedingPossible(body));
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Villager body, long timestamp) {
/*  50 */     AgeableMob breedTarget = body.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get();
/*  52 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)body, (LivingEntity)breedTarget, 0.5F, 2);
/*  54 */     level.broadcastEntityEvent((Entity)breedTarget, (byte)18);
/*  55 */     level.broadcastEntityEvent((Entity)body, (byte)18);
/*  57 */     int duration = 275 + body.getRandom().nextInt(50);
/*  58 */     this.birthTimestamp = timestamp + duration;
/*     */   }
/*     */   
/*     */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/*  63 */     Villager target = body.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get();
/*  65 */     if (body.distanceToSqr((Entity)target) > 5.0D)
/*     */       return; 
/*  69 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)body, (LivingEntity)target, 0.5F, 2);
/*  71 */     if (timestamp >= this.birthTimestamp) {
/*  73 */       body.eatAndDigestFood();
/*  74 */       target.eatAndDigestFood();
/*  76 */       tryToGiveBirth(level, body, target);
/*  77 */     } else if (body.getRandom().nextInt(35) == 0) {
/*  78 */       level.broadcastEntityEvent((Entity)target, (byte)12);
/*  79 */       level.broadcastEntityEvent((Entity)body, (byte)12);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tryToGiveBirth(ServerLevel level, Villager body, Villager target) {
/*  85 */     Optional<BlockPos> childsBed = takeVacantBed(level, body);
/*  86 */     if (childsBed.isEmpty()) {
/*  88 */       level.broadcastEntityEvent((Entity)target, (byte)13);
/*  89 */       level.broadcastEntityEvent((Entity)body, (byte)13);
/*     */     } else {
/*  91 */       Optional<Villager> child = breed(level, body, target);
/*  93 */       if (child.isPresent()) {
/*  94 */         giveBedToChild(level, child.get(), childsBed.get());
/*     */       } else {
/*  96 */         level.getPoiManager().release(childsBed.get());
/*  97 */         level.debugSynchronizers().updatePoi(childsBed.get());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Villager body, long timestamp) {
/* 104 */     body.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
/*     */   }
/*     */   
/*     */   private boolean isBreedingPossible(Villager myBody) {
/* 108 */     Brain<Villager> brain = myBody.getBrain();
/* 110 */     Optional<AgeableMob> breedTarget = brain.getMemory(MemoryModuleType.BREED_TARGET).filter(entity -> entity.is(EntityTypes.VILLAGER));
/* 111 */     if (breedTarget.isEmpty())
/* 112 */       return false; 
/* 114 */     return (BehaviorUtils.targetIsValid(brain, MemoryModuleType.BREED_TARGET, EntityTypes.VILLAGER) && myBody
/* 115 */       .canBreed() && ((AgeableMob)breedTarget
/* 116 */       .get()).canBreed());
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> takeVacantBed(ServerLevel level, Villager body) {
/* 120 */     return level.getPoiManager().take(p -> p.is(PoiTypes.HOME), (poiType, poiPos) -> canReach(body, poiPos, poiType), body
/*     */         
/* 123 */         .blockPosition(), 48);
/*     */   }
/*     */   
/*     */   private boolean canReach(Villager body, BlockPos poiPos, Holder<PoiType> poiType) {
/* 129 */     Path path = body.getNavigation().createPath(poiPos, ((PoiType)poiType.value()).validRange());
/* 130 */     return (path != null && path.canReach());
/*     */   }
/*     */   
/*     */   private Optional<Villager> breed(ServerLevel level, Villager source, Villager target) {
/* 134 */     Villager child = source.getBreedOffspring(level, (AgeableMob)target);
/* 135 */     if (child == null)
/* 136 */       return Optional.empty(); 
/* 138 */     source.setAge(6000);
/* 139 */     target.setAge(6000);
/* 140 */     child.setAge(-24000);
/* 141 */     child.snapTo(source.getX(), source.getY(), source.getZ(), 0.0F, 0.0F);
/* 143 */     level.addFreshEntityWithPassengers((Entity)child);
/* 144 */     level.broadcastEntityEvent((Entity)child, (byte)12);
/* 146 */     return Optional.of(child);
/*     */   }
/*     */   
/*     */   private void giveBedToChild(ServerLevel level, Villager child, BlockPos bedPos) {
/* 150 */     GlobalPos globalBedPos = GlobalPos.of(level.dimension(), bedPos);
/* 151 */     child.getBrain().setMemory(MemoryModuleType.HOME, globalBedPos);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\VillagerMakeLove.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */