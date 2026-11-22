/*     */ package net.minecraft.world.entity.monster.hoglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
/*     */ import net.minecraft.world.entity.ai.behavior.BecomePassiveIfMemoryPresent;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.EraseMemoryIf;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ public class HoglinAi {
/*     */   public static final int REPELLENT_DETECTION_RANGE_HORIZONTAL = 8;
/*     */   
/*     */   public static final int REPELLENT_DETECTION_RANGE_VERTICAL = 4;
/*     */   
/*  49 */   private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
/*     */   
/*     */   private static final int ATTACK_DURATION = 200;
/*     */   
/*     */   private static final int DESIRED_DISTANCE_FROM_PIGLIN_WHEN_IDLING = 8;
/*     */   
/*     */   private static final int DESIRED_DISTANCE_FROM_PIGLIN_WHEN_RETREATING = 15;
/*     */   
/*     */   private static final int ATTACK_INTERVAL = 40;
/*     */   
/*     */   private static final int BABY_ATTACK_INTERVAL = 15;
/*     */   
/*     */   private static final int REPELLENT_PACIFY_TIME = 200;
/*     */   
/*  56 */   private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_AVOIDING_REPELLENT = 1.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_RETREATING = 1.3F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 0.6F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.4F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT = 0.6F;
/*     */   
/*     */   protected static List<ActivityData<Hoglin>> getActivities() {
/*  66 */     return List.of(
/*  67 */         initCoreActivity(), 
/*  68 */         initIdleActivity(), 
/*  69 */         initFightActivity(), 
/*  70 */         initRetreatActivity());
/*     */   }
/*     */   
/*     */   private static ActivityData<Hoglin> initCoreActivity() {
/*  75 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink()));
/*     */   }
/*     */   
/*     */   private static ActivityData<Hoglin> initIdleActivity() {
/*  82 */     return ActivityData.create(Activity.IDLE, 10, ImmutableList.of(
/*  83 */           BecomePassiveIfMemoryPresent.create(MemoryModuleType.NEAREST_REPELLENT, 200), new AnimalMakeLove(EntityTypes.HOGLIN, 0.6F, 2), 
/*     */           
/*  85 */           SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, true), 
/*  86 */           StartAttacking.create(HoglinAi::findNearestValidAttackTarget), 
/*  87 */           BehaviorBuilder.triggerIf(Hoglin::isAdult, SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, 0.4F, 8, false)), 
/*  88 */           SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), 
/*  89 */           BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 0.6F), 
/*  90 */           createIdleMovementBehaviors()));
/*     */   }
/*     */   
/*     */   private static ActivityData<Hoglin> initFightActivity() {
/*  95 */     return ActivityData.create(Activity.FIGHT, 10, ImmutableList.of(
/*  96 */           BecomePassiveIfMemoryPresent.create(MemoryModuleType.NEAREST_REPELLENT, 200), new AnimalMakeLove(EntityTypes.HOGLIN, 0.6F, 2), 
/*     */           
/*  98 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), 
/*  99 */           BehaviorBuilder.triggerIf(Hoglin::isAdult, MeleeAttack.create(40)), 
/* 100 */           BehaviorBuilder.triggerIf(AgeableMob::isBaby, MeleeAttack.create(15)), 
/* 101 */           StopAttackingIfTargetInvalid.create(), 
/* 102 */           EraseMemoryIf.create(HoglinAi::isBreeding, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */   
/*     */   private static ActivityData<Hoglin> initRetreatActivity() {
/* 107 */     return ActivityData.create(Activity.AVOID, 10, ImmutableList.of(
/* 108 */           SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.3F, 15, false), 
/* 109 */           createIdleMovementBehaviors(), 
/* 110 */           SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), 
/* 111 */           EraseMemoryIf.create(HoglinAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET);
/*     */   }
/*     */   
/*     */   private static RunOne<Hoglin> createIdleMovementBehaviors() {
/* 116 */     return new RunOne((List)ImmutableList.of(
/* 117 */           Pair.of(RandomStroll.stroll(0.4F), Integer.valueOf(2)), 
/* 118 */           Pair.of(SetWalkTargetFromLookTarget.create(0.4F, 3), Integer.valueOf(2)), 
/* 119 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */   
/*     */   protected static void updateActivity(Hoglin body) {
/* 124 */     Brain<Hoglin> brain = body.getBrain();
/* 126 */     Activity oldActivity = brain.getActiveNonCoreActivity().orElse(null);
/* 129 */     brain.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.AVOID, Activity.IDLE));
/* 135 */     Activity newActivity = brain.getActiveNonCoreActivity().orElse(null);
/* 136 */     if (oldActivity != newActivity) {
/* 138 */       Objects.requireNonNull(body);
/* 138 */       getSoundForCurrentActivity(body).ifPresent(body::makeSound);
/*     */     } 
/* 142 */     body.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */   }
/*     */   
/*     */   protected static void onHitTarget(Hoglin attackerBody, LivingEntity target) {
/* 146 */     if (attackerBody.isBaby())
/*     */       return; 
/* 150 */     if (target.is(EntityTypes.PIGLIN) && piglinsOutnumberHoglins(attackerBody)) {
/* 152 */       setAvoidTarget(attackerBody, target);
/* 153 */       broadcastRetreat(attackerBody, target);
/*     */       return;
/*     */     } 
/* 156 */     broadcastAttackTarget(attackerBody, target);
/*     */   }
/*     */   
/*     */   private static void broadcastRetreat(Hoglin body, LivingEntity target) {
/* 160 */     getVisibleAdultHoglins(body).forEach(hoglin -> retreatFromNearestTarget(hoglin, target));
/*     */   }
/*     */   
/*     */   private static void retreatFromNearestTarget(Hoglin body, LivingEntity newAvoidTarget) {
/* 164 */     LivingEntity nearest = newAvoidTarget;
/* 166 */     Brain<Hoglin> brain = body.getBrain();
/* 167 */     nearest = BehaviorUtils.getNearestTarget((LivingEntity)body, brain.getMemory(MemoryModuleType.AVOID_TARGET), nearest);
/* 168 */     nearest = BehaviorUtils.getNearestTarget((LivingEntity)body, brain.getMemory(MemoryModuleType.ATTACK_TARGET), nearest);
/* 170 */     setAvoidTarget(body, nearest);
/*     */   }
/*     */   
/*     */   private static void setAvoidTarget(Hoglin body, LivingEntity avoidTarget) {
/* 174 */     body.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/* 175 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 176 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, avoidTarget, RETREAT_DURATION.sample(body.level().getRandom()));
/*     */   }
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel level, Hoglin body) {
/* 180 */     if (isPacified(body) || isBreeding(body))
/* 182 */       return Optional.empty(); 
/* 186 */     return body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
/*     */   }
/*     */   
/*     */   static boolean isPosNearNearestRepellent(Hoglin body, BlockPos pos) {
/* 191 */     Optional<BlockPos> repellentPos = body.getBrain().getMemory(MemoryModuleType.NEAREST_REPELLENT);
/* 192 */     return (repellentPos.isPresent() && ((BlockPos)repellentPos.get()).closerThan((Vec3i)pos, 8.0D));
/*     */   }
/*     */   
/*     */   private static boolean wantsToStopFleeing(Hoglin body) {
/* 196 */     return (body.isAdult() && !piglinsOutnumberHoglins(body));
/*     */   }
/*     */   
/*     */   private static boolean piglinsOutnumberHoglins(Hoglin body) {
/* 200 */     if (body.isBaby())
/* 201 */       return false; 
/* 204 */     int piglinCount = ((Integer)body.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(Integer.valueOf(0))).intValue();
/* 205 */     int hoglinCount = ((Integer)body.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(Integer.valueOf(0))).intValue() + 1;
/* 206 */     return (piglinCount > hoglinCount);
/*     */   }
/*     */   
/*     */   protected static void wasHurtBy(ServerLevel level, Hoglin body, LivingEntity attacker) {
/* 211 */     Brain<Hoglin> brain = body.getBrain();
/* 212 */     brain.eraseMemory(MemoryModuleType.PACIFIED);
/* 213 */     brain.eraseMemory(MemoryModuleType.BREED_TARGET);
/* 215 */     if (body.isBaby()) {
/* 217 */       retreatFromNearestTarget(body, attacker);
/*     */       return;
/*     */     } 
/* 221 */     maybeRetaliate(level, body, attacker);
/*     */   }
/*     */   
/*     */   private static void maybeRetaliate(ServerLevel level, Hoglin body, LivingEntity attacker) {
/* 225 */     if (body.getBrain().isActive(Activity.AVOID) && attacker.is(EntityTypes.PIGLIN))
/*     */       return; 
/* 228 */     if (attacker.is(EntityTypes.HOGLIN))
/*     */       return; 
/* 231 */     if (BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget((LivingEntity)body, attacker, 4.0D))
/*     */       return; 
/* 235 */     if (!Sensor.isEntityAttackable(level, (LivingEntity)body, attacker))
/*     */       return; 
/* 239 */     setAttackTarget(body, attacker);
/* 240 */     broadcastAttackTarget(body, attacker);
/*     */   }
/*     */   
/*     */   private static void setAttackTarget(Hoglin body, LivingEntity target) {
/* 244 */     Brain<Hoglin> brain = body.getBrain();
/* 245 */     brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 246 */     brain.eraseMemory(MemoryModuleType.BREED_TARGET);
/* 247 */     brain.setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, target, 200L);
/*     */   }
/*     */   
/*     */   private static void broadcastAttackTarget(Hoglin body, LivingEntity target) {
/* 252 */     getVisibleAdultHoglins(body).forEach(hoglin -> setAttackTargetIfCloserThanCurrent(hoglin, target));
/*     */   }
/*     */   
/*     */   private static void setAttackTargetIfCloserThanCurrent(Hoglin body, LivingEntity newTarget) {
/* 256 */     if (isPacified(body))
/*     */       return; 
/* 260 */     Optional<LivingEntity> currentTarget = body.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
/* 261 */     LivingEntity nearest = BehaviorUtils.getNearestTarget((LivingEntity)body, currentTarget, newTarget);
/* 262 */     setAttackTarget(body, nearest);
/*     */   }
/*     */   
/*     */   public static Optional<SoundEvent> getSoundForCurrentActivity(Hoglin body) {
/* 266 */     return body.getBrain().getActiveNonCoreActivity().map(activity -> getSoundForActivity(body, activity));
/*     */   }
/*     */   
/*     */   private static SoundEvent getSoundForActivity(Hoglin body, Activity activity) {
/* 270 */     if (activity == Activity.AVOID || body.isConverting())
/* 271 */       return SoundEvents.HOGLIN_RETREAT; 
/* 272 */     if (activity == Activity.FIGHT)
/* 273 */       return SoundEvents.HOGLIN_ANGRY; 
/* 274 */     if (isNearRepellent(body))
/* 275 */       return SoundEvents.HOGLIN_RETREAT; 
/* 277 */     return SoundEvents.HOGLIN_AMBIENT;
/*     */   }
/*     */   
/*     */   private static List<Hoglin> getVisibleAdultHoglins(Hoglin body) {
/* 282 */     return (List<Hoglin>)body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS).orElse(ImmutableList.of());
/*     */   }
/*     */   
/*     */   private static boolean isNearRepellent(Hoglin body) {
/* 286 */     return body.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
/*     */   }
/*     */   
/*     */   private static boolean isBreeding(Hoglin body) {
/* 290 */     return body.getBrain().hasMemoryValue(MemoryModuleType.BREED_TARGET);
/*     */   }
/*     */   
/*     */   protected static boolean isPacified(Hoglin body) {
/* 294 */     return body.getBrain().hasMemoryValue(MemoryModuleType.PACIFIED);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\hoglin\HoglinAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */