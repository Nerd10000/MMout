/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWith;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.StopBeingAngryIfTargetDead;
/*     */ import net.minecraft.world.entity.ai.behavior.StrollAroundPoi;
/*     */ import net.minecraft.world.entity.ai.behavior.StrollToPoi;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ public class PiglinBruteAi {
/*     */   private static final int ANGER_DURATION = 600;
/*     */   
/*     */   private static final int MELEE_ATTACK_COOLDOWN = 20;
/*     */   
/*     */   private static final double ACTIVITY_SOUND_LIKELIHOOD_PER_TICK = 0.0125D;
/*     */   
/*     */   private static final int MAX_LOOK_DIST = 8;
/*     */   
/*     */   private static final int INTERACTION_RANGE = 8;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.6F;
/*     */   
/*     */   private static final int HOME_CLOSE_ENOUGH_DISTANCE = 2;
/*     */   
/*     */   private static final int HOME_TOO_FAR_DISTANCE = 100;
/*     */   
/*     */   private static final int HOME_STROLL_AROUND_DISTANCE = 5;
/*     */   
/*     */   public static List<ActivityData<PiglinBrute>> getActivities(PiglinBrute piglin) {
/*  53 */     return List.of(
/*  54 */         initCoreActivity(), 
/*  55 */         initIdleActivity(), 
/*  56 */         initFightActivity(piglin));
/*     */   }
/*     */   
/*     */   protected static void initMemories(PiglinBrute body) {
/*  61 */     GlobalPos currentGlobalPos = GlobalPos.of(body.level().dimension(), body.blockPosition());
/*  62 */     body.getBrain().setMemory(MemoryModuleType.HOME, currentGlobalPos);
/*     */   }
/*     */   
/*     */   private static ActivityData<PiglinBrute> initCoreActivity() {
/*  66 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), 
/*     */           
/*  69 */           InteractWithDoor.create(), 
/*  70 */           StopBeingAngryIfTargetDead.create()));
/*     */   }
/*     */   
/*     */   private static ActivityData<PiglinBrute> initIdleActivity() {
/*  75 */     return ActivityData.create(Activity.IDLE, 10, ImmutableList.of(
/*  76 */           StartAttacking.create(PiglinBruteAi::findNearestValidAttackTarget), 
/*  77 */           createIdleLookBehaviors(), 
/*  78 */           createIdleMovementBehaviors(), 
/*  79 */           SetLookAndInteract.create(EntityTypes.PLAYER, 4)));
/*     */   }
/*     */   
/*     */   private static ActivityData<PiglinBrute> initFightActivity(PiglinBrute body) {
/*  84 */     return ActivityData.create(Activity.FIGHT, 10, ImmutableList.of(
/*  85 */           StopAttackingIfTargetInvalid.create((level, target) -> !isNearestValidAttackTarget(level, body, target)), 
/*  86 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), 
/*  87 */           MeleeAttack.create(20)), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */   
/*     */   private static RunOne<PiglinBrute> createIdleLookBehaviors() {
/*  92 */     return new RunOne((List)ImmutableList.of(
/*  93 */           Pair.of(SetEntityLookTarget.create(EntityTypes.PLAYER, 8.0F), Integer.valueOf(1)), 
/*  94 */           Pair.of(SetEntityLookTarget.create(EntityTypes.PIGLIN, 8.0F), Integer.valueOf(1)), 
/*  95 */           Pair.of(SetEntityLookTarget.create(EntityTypes.PIGLIN_BRUTE, 8.0F), Integer.valueOf(1)), 
/*  96 */           Pair.of(SetEntityLookTarget.create(8.0F), Integer.valueOf(1)), 
/*  97 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */   
/*     */   private static RunOne<PiglinBrute> createIdleMovementBehaviors() {
/* 102 */     return new RunOne((List)ImmutableList.of(
/* 103 */           Pair.of(RandomStroll.stroll(0.6F), Integer.valueOf(2)), 
/* 104 */           Pair.of(InteractWith.of(EntityTypes.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), Integer.valueOf(2)), 
/* 105 */           Pair.of(InteractWith.of(EntityTypes.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), Integer.valueOf(2)), 
/* 106 */           Pair.of(StrollToPoi.create(MemoryModuleType.HOME, 0.6F, 2, 100), Integer.valueOf(2)), 
/* 107 */           Pair.of(StrollAroundPoi.create(MemoryModuleType.HOME, 0.6F, 5), Integer.valueOf(2)), 
/* 108 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */   
/*     */   protected static void updateActivity(PiglinBrute body) {
/* 113 */     Brain<PiglinBrute> brain = body.getBrain();
/* 117 */     Activity oldActivity = brain.getActiveNonCoreActivity().orElse(null);
/* 121 */     brain.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/* 126 */     Activity newActivity = brain.getActiveNonCoreActivity().orElse(null);
/* 127 */     if (oldActivity != newActivity)
/* 129 */       playActivitySound(body); 
/* 133 */     body.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */   }
/*     */   
/*     */   private static boolean isNearestValidAttackTarget(ServerLevel level, AbstractPiglin body, LivingEntity target) {
/* 137 */     return findNearestValidAttackTarget(level, body)
/* 138 */       .filter(nearestValidTarget -> (nearestValidTarget == target))
/* 139 */       .isPresent();
/*     */   }
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel level, AbstractPiglin body) {
/* 143 */     Optional<LivingEntity> angryAt = BehaviorUtils.getLivingEntityFromUUIDMemory((LivingEntity)body, MemoryModuleType.ANGRY_AT);
/* 144 */     if (angryAt.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(level, (LivingEntity)body, angryAt.get()))
/* 145 */       return angryAt; 
/* 148 */     Optional<? extends LivingEntity> player = body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
/* 149 */     if (player.isPresent())
/* 150 */       return player; 
/* 153 */     return body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
/*     */   }
/*     */   
/*     */   protected static void wasHurtBy(ServerLevel level, PiglinBrute body, LivingEntity attacker) {
/* 158 */     if (attacker instanceof AbstractPiglin)
/*     */       return; 
/* 162 */     PiglinAi.maybeRetaliate(level, body, attacker);
/*     */   }
/*     */   
/*     */   protected static void setAngerTarget(PiglinBrute body, LivingEntity target) {
/* 166 */     body.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 167 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, target.getUUID(), 600L);
/*     */   }
/*     */   
/*     */   protected static void maybePlayActivitySound(PiglinBrute body) {
/* 171 */     if (body.level().getRandom().nextFloat() < 0.0125D)
/* 172 */       playActivitySound(body); 
/*     */   }
/*     */   
/*     */   private static void playActivitySound(PiglinBrute body) {
/* 178 */     body.getBrain().getActiveNonCoreActivity().ifPresent(activity -> {
/*     */           if (activity == Activity.FIGHT)
/*     */             body.playAngrySound(); 
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\piglin\PiglinBruteAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */