/*     */ package net.minecraft.world.entity.animal.axolotl;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.EraseMemoryIf;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.GateBehavior;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.TryFindWater;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class AxolotlAi {
/*  45 */   private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 0.2F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_ON_LAND = 0.15F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 0.5F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_CHASING_IN_WATER = 0.6F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT_IN_WATER = 0.6F;
/*     */   
/*     */   protected static List<ActivityData<Axolotl>> getActivities() {
/*  53 */     return List.of(initCoreActivity(), 
/*  54 */         initIdleActivity(), 
/*  55 */         initFightActivity(), 
/*  56 */         initPlayDeadActivity());
/*     */   }
/*     */   
/*     */   protected static ActivityData<Axolotl> initPlayDeadActivity() {
/*  61 */     return ActivityData.create(Activity.PLAY_DEAD, 
/*  62 */         ImmutableList.of(
/*  63 */           Pair.of(Integer.valueOf(0), new PlayDead()), 
/*  64 */           Pair.of(Integer.valueOf(1), EraseMemoryIf.create(BehaviorUtils::isBreeding, MemoryModuleType.PLAY_DEAD_TICKS))), 
/*     */         
/*  66 */         (Set)ImmutableSet.of(Pair.of(MemoryModuleType.PLAY_DEAD_TICKS, MemoryStatus.VALUE_PRESENT)), 
/*  67 */         (Set)ImmutableSet.of(MemoryModuleType.PLAY_DEAD_TICKS));
/*     */   }
/*     */   
/*     */   protected static ActivityData<Axolotl> initFightActivity() {
/*  72 */     return ActivityData.create(Activity.FIGHT, 0, ImmutableList.of(
/*  73 */           StopAttackingIfTargetInvalid.create(Axolotl::onStopAttacking), 
/*  74 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(AxolotlAi::getSpeedModifierChasing), 
/*  75 */           MeleeAttack.create(20), 
/*  76 */           EraseMemoryIf.create(BehaviorUtils::isBreeding, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */   
/*     */   protected static ActivityData<Axolotl> initCoreActivity() {
/*  81 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), 
/*     */           
/*  84 */           ValidatePlayDead.create(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)));
/*     */   }
/*     */   
/*     */   protected static ActivityData<Axolotl> initIdleActivity() {
/*  90 */     return ActivityData.create(Activity.IDLE, ImmutableList.of(
/*  91 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityTypes.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/*  92 */           Pair.of(Integer.valueOf(1), new AnimalMakeLove(EntityTypes.AXOLOTL, 0.2F, 2)), 
/*  93 */           Pair.of(Integer.valueOf(2), new RunOne((List)ImmutableList.of(
/*  94 */                 Pair.of(new FollowTemptation(AxolotlAi::getSpeedModifier), Integer.valueOf(1)), 
/*  95 */                 Pair.of(BabyFollowAdult.create(ADULT_FOLLOW_RANGE, AxolotlAi::getSpeedModifierFollowingAdult, MemoryModuleType.NEAREST_VISIBLE_ADULT, false), Integer.valueOf(1))))), 
/*     */           
/*  97 */           Pair.of(Integer.valueOf(3), StartAttacking.create(AxolotlAi::findNearestValidAttackTarget)), 
/*  98 */           Pair.of(Integer.valueOf(3), TryFindWater.create(6, 0.15F)), 
/*  99 */           Pair.of(Integer.valueOf(4), new GateBehavior(
/* 100 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */               
/* 103 */               (Set)ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, 
/*     */               
/* 106 */               (List)ImmutableList.of(
/* 107 */                 Pair.of(RandomStroll.swim(0.5F), Integer.valueOf(2)), 
/* 108 */                 Pair.of(RandomStroll.stroll(0.15F, false), Integer.valueOf(2)), 
/* 109 */                 Pair.of(SetWalkTargetFromLookTarget.create(AxolotlAi::canSetWalkTargetFromLookTarget, AxolotlAi::getSpeedModifier, 3), Integer.valueOf(3)), 
/* 110 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::isInWater), Integer.valueOf(5)), 
/* 111 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), Integer.valueOf(5)))))));
/*     */   }
/*     */   
/*     */   private static boolean canSetWalkTargetFromLookTarget(LivingEntity body) {
/* 118 */     Level level = body.level();
/* 119 */     Optional<PositionTracker> lookTarget = body.getBrain().getMemory(MemoryModuleType.LOOK_TARGET);
/* 121 */     if (lookTarget.isPresent()) {
/* 122 */       BlockPos pos = ((PositionTracker)lookTarget.get()).currentBlockPosition();
/* 123 */       return (level.isWaterAt(pos) == body.isInWater());
/*     */     } 
/* 126 */     return false;
/*     */   }
/*     */   
/*     */   public static void updateActivity(Axolotl body) {
/* 130 */     Brain<Axolotl> brain = body.getBrain();
/* 132 */     Activity oldActivity = brain.getActiveNonCoreActivity().orElse(null);
/* 137 */     if (oldActivity != Activity.PLAY_DEAD) {
/* 138 */       brain.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.PLAY_DEAD, Activity.FIGHT, Activity.IDLE));
/* 145 */       if (oldActivity == Activity.FIGHT && brain.getActiveNonCoreActivity().orElse(null) != Activity.FIGHT)
/* 146 */         brain.setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, Boolean.valueOf(true), 2400L); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static float getSpeedModifierChasing(LivingEntity mob) {
/* 152 */     return mob.isInWater() ? 0.6F : 0.15F;
/*     */   }
/*     */   
/*     */   private static float getSpeedModifierFollowingAdult(LivingEntity mob) {
/* 156 */     return mob.isInWater() ? 0.6F : 0.15F;
/*     */   }
/*     */   
/*     */   private static float getSpeedModifier(LivingEntity mob) {
/* 160 */     return mob.isInWater() ? 0.5F : 0.15F;
/*     */   }
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel level, Axolotl body) {
/* 164 */     if (BehaviorUtils.isBreeding((LivingEntity)body))
/* 165 */       return Optional.empty(); 
/* 168 */     return body.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\axolotl\AxolotlAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */