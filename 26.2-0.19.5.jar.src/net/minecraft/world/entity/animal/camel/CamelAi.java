/*     */ package net.minecraft.world.entity.animal.camel;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomLookAround;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ public class CamelAi {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 4.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 2.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 2.5F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT = 2.5F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 1.0F;
/*     */   
/*  41 */   private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
/*     */   
/*     */   protected static void initMemories(Camel body, RandomSource random) {}
/*     */   
/*     */   protected static List<ActivityData<Camel>> getActivities() {
/*  47 */     return List.of(
/*  48 */         initCoreActivity(), 
/*  49 */         initIdleActivity());
/*     */   }
/*     */   
/*     */   private static ActivityData<Camel> initCoreActivity() {
/*  54 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new CamelPanic(4.0F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.GAZE_COOLDOWN_TICKS)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Camel> initIdleActivity() {
/*  65 */     return ActivityData.create(Activity.IDLE, ImmutableList.of(
/*  66 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityTypes.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/*  67 */           Pair.of(Integer.valueOf(1), new AnimalMakeLove(EntityTypes.CAMEL)), 
/*  68 */           Pair.of(Integer.valueOf(2), new RunOne((List)ImmutableList.of(
/*  69 */                 Pair.of(new FollowTemptation(camel -> Float.valueOf(2.5F), camel -> Double.valueOf(camel.isBaby() ? 2.5D : 3.5D)), Integer.valueOf(1)), 
/*  70 */                 Pair.of(BehaviorBuilder.triggerIf(Predicate.not(Camel::refuseToMove), BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 2.5F)), Integer.valueOf(1))))), 
/*     */           
/*  72 */           Pair.of(Integer.valueOf(3), new RandomLookAround((IntProvider)UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F)), 
/*  73 */           Pair.of(Integer.valueOf(4), new RunOne(
/*  74 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */               
/*  77 */               (List)ImmutableList.of(
/*  78 */                 Pair.of(BehaviorBuilder.triggerIf(Predicate.not(Camel::refuseToMove), RandomStroll.stroll(2.0F)), Integer.valueOf(1)), 
/*  79 */                 Pair.of(BehaviorBuilder.triggerIf(Predicate.not(Camel::refuseToMove), SetWalkTargetFromLookTarget.create(2.0F, 3)), Integer.valueOf(1)), 
/*  80 */                 Pair.of(new RandomSitting(20), Integer.valueOf(1)), 
/*  81 */                 Pair.of(new DoNothing(30, 60), Integer.valueOf(1)))))));
/*     */   }
/*     */   
/*     */   public static void updateActivity(Camel body) {
/*  88 */     body.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.IDLE));
/*     */   }
/*     */   
/*     */   public static class CamelPanic extends AnimalPanic<Camel> {
/*     */     public CamelPanic(float speedMultiplier) {
/*  95 */       super(speedMultiplier);
/*     */     }
/*     */     
/*     */     protected boolean checkExtraStartConditions(ServerLevel level, Camel body) {
/* 100 */       return (super.checkExtraStartConditions(level, (PathfinderMob)body) && !body.isMobControlled());
/*     */     }
/*     */     
/*     */     protected void start(ServerLevel level, Camel camel, long timestamp) {
/* 105 */       camel.standUpInstantly();
/* 106 */       super.start(level, (PathfinderMob)camel, timestamp);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RandomSitting extends Behavior<Camel> {
/*     */     private final int minimalPoseTicks;
/*     */     
/*     */     public RandomSitting(int minimalPoseTimeSec) {
/* 114 */       super((Map)ImmutableMap.of());
/* 115 */       this.minimalPoseTicks = minimalPoseTimeSec * 20;
/*     */     }
/*     */     
/*     */     protected boolean checkExtraStartConditions(ServerLevel level, Camel body) {
/* 120 */       return (!body.isInWater() && body
/* 121 */         .getPoseTime() >= this.minimalPoseTicks && 
/* 122 */         !body.isLeashed() && body
/* 123 */         .onGround() && 
/* 124 */         !body.hasControllingPassenger() && body
/* 125 */         .canCamelChangePose());
/*     */     }
/*     */     
/*     */     protected void start(ServerLevel level, Camel body, long timestamp) {
/* 130 */       if (body.isCamelSitting()) {
/* 131 */         body.standUp();
/* 132 */       } else if (!body.isPanicking()) {
/* 133 */         body.sitDown();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\camel\CamelAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */