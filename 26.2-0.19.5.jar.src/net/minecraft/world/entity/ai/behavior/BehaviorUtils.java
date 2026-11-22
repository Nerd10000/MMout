/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ProjectileWeaponItem;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class BehaviorUtils {
/*     */   public static void lockGazeAndWalkToEachOther(LivingEntity entity1, LivingEntity entity2, float speedModifier, int closeEnoughDistance) {
/*  35 */     lookAtEachOther(entity1, entity2);
/*  36 */     setWalkAndLookTargetMemoriesToEachOther(entity1, entity2, speedModifier, closeEnoughDistance);
/*     */   }
/*     */   
/*     */   public static boolean entityIsVisible(Brain<?> brain, LivingEntity targetEntity) {
/*  40 */     Optional<NearestVisibleLivingEntities> visibleEntities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/*  41 */     return (visibleEntities.isPresent() && ((NearestVisibleLivingEntities)visibleEntities.get()).contains(targetEntity));
/*     */   }
/*     */   
/*     */   public static boolean targetIsValid(Brain<?> brain, MemoryModuleType<? extends LivingEntity> memory, EntityType<?> targetType) {
/*  45 */     return targetIsValid(brain, memory, entity -> entity.is(targetType));
/*     */   }
/*     */   
/*     */   private static boolean targetIsValid(Brain<?> brain, MemoryModuleType<? extends LivingEntity> memory, Predicate<LivingEntity> targetPredicate) {
/*  49 */     return brain.getMemory(memory)
/*  50 */       .filter(targetPredicate)
/*  51 */       .filter(LivingEntity::isAlive)
/*  52 */       .filter(entity -> entityIsVisible(brain, entity))
/*  53 */       .isPresent();
/*     */   }
/*     */   
/*     */   private static void lookAtEachOther(LivingEntity entity1, LivingEntity entity2) {
/*  57 */     lookAtEntity(entity1, entity2);
/*  58 */     lookAtEntity(entity2, entity1);
/*     */   }
/*     */   
/*     */   public static void lookAtEntity(LivingEntity looker, LivingEntity targetEntity) {
/*  62 */     looker.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)targetEntity, true));
/*     */   }
/*     */   
/*     */   private static void setWalkAndLookTargetMemoriesToEachOther(LivingEntity entity1, LivingEntity entity2, float speedModifier, int closeEnoughDistance) {
/*  66 */     setWalkAndLookTargetMemories(entity1, (Entity)entity2, speedModifier, closeEnoughDistance);
/*  67 */     setWalkAndLookTargetMemories(entity2, (Entity)entity1, speedModifier, closeEnoughDistance);
/*     */   }
/*     */   
/*     */   public static void setWalkAndLookTargetMemories(LivingEntity walker, Entity targetEntity, float speedModifier, int closeEnoughDistance) {
/*  71 */     setWalkAndLookTargetMemories(walker, new EntityTracker(targetEntity, true), speedModifier, closeEnoughDistance);
/*     */   }
/*     */   
/*     */   public static void setWalkAndLookTargetMemories(LivingEntity walker, BlockPos targetPos, float speedModifier, int closeEnoughDistance) {
/*  75 */     setWalkAndLookTargetMemories(walker, new BlockPosTracker(targetPos), speedModifier, closeEnoughDistance);
/*     */   }
/*     */   
/*     */   public static void setWalkAndLookTargetMemories(LivingEntity walker, PositionTracker target, float speedModifier, int closeEnoughDistance) {
/*  79 */     WalkTarget walkTarget = new WalkTarget(target, speedModifier, closeEnoughDistance);
/*  80 */     walker.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, target);
/*  81 */     walker.getBrain().setMemory(MemoryModuleType.WALK_TARGET, walkTarget);
/*     */   }
/*     */   
/*     */   public static void throwItem(LivingEntity thrower, ItemStack item, Vec3 targetPos) {
/*  85 */     Vec3 throwVelocity = new Vec3(0.30000001192092896D, 0.30000001192092896D, 0.30000001192092896D);
/*  86 */     throwItem(thrower, item, targetPos, throwVelocity, 0.3F);
/*     */   }
/*     */   
/*     */   public static void throwItem(LivingEntity thrower, ItemStack item, Vec3 targetPos, Vec3 throwVelocity, float handYDistanceFromEye) {
/*  90 */     double yHandPos = thrower.getEyeY() - handYDistanceFromEye;
/*  91 */     ItemEntity itemEntity = new ItemEntity(thrower.level(), thrower.getX(), yHandPos, thrower.getZ(), item);
/*  92 */     itemEntity.setThrower((Entity)thrower);
/*  94 */     Vec3 throwVector = targetPos.subtract(thrower.position());
/*  95 */     throwVector = throwVector.normalize().multiply(throwVelocity.x, throwVelocity.y, throwVelocity.z);
/*  97 */     itemEntity.setDeltaMovement(throwVector);
/*  98 */     itemEntity.setDefaultPickUpDelay();
/*  99 */     thrower.level().addFreshEntity((Entity)itemEntity);
/*     */   }
/*     */   
/*     */   public static SectionPos findSectionClosestToVillage(ServerLevel level, SectionPos center, int radius) {
/* 103 */     int distToVillage = level.sectionsToVillage(center);
/* 107 */     Objects.requireNonNull(level);
/* 107 */     return SectionPos.cube(center, radius).filter(s -> (level.sectionsToVillage(s) < distToVillage)).min(Comparator.comparingInt(level::sectionsToVillage))
/* 108 */       .orElse(center);
/*     */   }
/*     */   
/*     */   public static boolean isWithinAttackRange(Mob body, LivingEntity target, int projectileAttackRangeMargin) {
/* 113 */     Item item = body.getMainHandItem().getItem();
/* 113 */     if (item instanceof ProjectileWeaponItem) {
/* 113 */       ProjectileWeaponItem weapon = (ProjectileWeaponItem)item;
/* 113 */       if (body.canUseNonMeleeWeapon(body.getMainHandItem())) {
/* 114 */         int maxAllowedDistance = weapon.getDefaultProjectileRange() - projectileAttackRangeMargin;
/* 115 */         return body.closerThan((Entity)target, maxAllowedDistance);
/*     */       } 
/*     */     } 
/* 117 */     return body.isWithinMeleeAttackRange(target);
/*     */   }
/*     */   
/*     */   public static boolean isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(LivingEntity body, LivingEntity otherTarget, double howMuchFurtherAway) {
/* 123 */     Optional<LivingEntity> currentTarget = body.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
/* 124 */     if (currentTarget.isEmpty())
/* 125 */       return false; 
/* 127 */     double distSqrToCurrentTarget = body.distanceToSqr(((LivingEntity)currentTarget.get()).position());
/* 128 */     double distSqrToOtherTarget = body.distanceToSqr(otherTarget.position());
/* 129 */     return (distSqrToOtherTarget > distSqrToCurrentTarget + howMuchFurtherAway * howMuchFurtherAway);
/*     */   }
/*     */   
/*     */   public static boolean canSee(LivingEntity body, LivingEntity target) {
/* 133 */     Brain<?> brain = body.getBrain();
/* 134 */     if (!brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES))
/* 135 */       return false; 
/* 137 */     return ((NearestVisibleLivingEntities)brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get()).contains(target);
/*     */   }
/*     */   
/*     */   public static LivingEntity getNearestTarget(LivingEntity body, Optional<LivingEntity> target1, LivingEntity target2) {
/* 141 */     if (target1.isEmpty())
/* 142 */       return target2; 
/* 144 */     return getTargetNearestMe(body, target1.get(), target2);
/*     */   }
/*     */   
/*     */   public static LivingEntity getTargetNearestMe(LivingEntity body, LivingEntity target1, LivingEntity target2) {
/* 148 */     Vec3 pos1 = target1.position();
/* 149 */     Vec3 pos2 = target2.position();
/* 150 */     return (body.distanceToSqr(pos1) < body.distanceToSqr(pos2)) ? target1 : target2;
/*     */   }
/*     */   
/*     */   public static Optional<LivingEntity> getLivingEntityFromUUIDMemory(LivingEntity body, MemoryModuleType<UUID> memoryType) {
/* 154 */     Optional<UUID> uuidMemory = body.getBrain().getMemory(memoryType);
/* 156 */     return uuidMemory.map(uuid -> body.level().getEntity(uuid)).map(entity -> {
/*     */           LivingEntity livingEntity = (LivingEntity)entity;
/*     */           return (entity instanceof LivingEntity) ? livingEntity : null;
/*     */         });
/*     */   }
/*     */   
/*     */   public static Vec3 getRandomSwimmablePos(PathfinderMob body, int maxHorizontalDistance, int maxVerticalDistance) {
/* 160 */     Vec3 targetPos = DefaultRandomPos.getPos(body, maxHorizontalDistance, maxVerticalDistance);
/* 161 */     int count = 0;
/* 162 */     while (targetPos != null && !body.level().getBlockState(BlockPos.containing((Position)targetPos)).isPathfindable(PathComputationType.WATER) && count++ < 10)
/* 163 */       targetPos = DefaultRandomPos.getPos(body, maxHorizontalDistance, maxVerticalDistance); 
/* 165 */     return targetPos;
/*     */   }
/*     */   
/*     */   public static boolean isBreeding(LivingEntity body) {
/* 169 */     return body.getBrain().hasMemoryValue(MemoryModuleType.BREED_TARGET);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\BehaviorUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */