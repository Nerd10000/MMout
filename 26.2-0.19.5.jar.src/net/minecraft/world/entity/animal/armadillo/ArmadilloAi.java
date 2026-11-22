/*     */ package net.minecraft.world.entity.animal.armadillo;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
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
/*     */ import net.minecraft.world.entity.ai.behavior.OneShot;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomLookAround;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ public class ArmadilloAi {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT = 1.25F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 1.0F;
/*     */   
/*     */   private static final double DEFAULT_CLOSE_ENOUGH_DIST = 2.0D;
/*     */   
/*     */   private static final double BABY_CLOSE_ENOUGH_DIST = 1.0D;
/*     */   
/*  49 */   private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
/*     */   
/*     */   private static final OneShot<Armadillo> ARMADILLO_ROLLING_OUT;
/*     */   
/*     */   protected static List<ActivityData<Armadillo>> getActivities() {
/*  52 */     return List.of(initCoreActivity(), initIdleActivity(), initScaredActivity());
/*     */   }
/*     */   
/*     */   static {
/*  55 */     ARMADILLO_ROLLING_OUT = BehaviorBuilder.create(i -> i.group((App)i.absent(MemoryModuleType.DANGER_DETECTED_RECENTLY)).apply((Applicative)i, ()));
/*     */   }
/*     */   
/*     */   private static ActivityData<Armadillo> initCoreActivity() {
/*  66 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new ArmadilloPanic(2.0F), new LookAtTargetSink(45, 90), new MoveToTargetSink() {
/*     */             protected boolean checkExtraStartConditions(ServerLevel level, Mob body) {
/*  73 */               if (body instanceof Armadillo) {
/*  73 */                 Armadillo armadillo = (Armadillo)body;
/*  73 */                 if (armadillo.isScared())
/*  74 */                   return false; 
/*     */               } 
/*  76 */               return super.checkExtraStartConditions(level, body);
/*     */             }
/*     */           }new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.GAZE_COOLDOWN_TICKS), ARMADILLO_ROLLING_OUT));
/*     */   }
/*     */   
/*     */   private static ActivityData<Armadillo> initIdleActivity() {
/*  86 */     return ActivityData.create(Activity.IDLE, ImmutableList.of(
/*  87 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityTypes.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/*  88 */           Pair.of(Integer.valueOf(1), new AnimalMakeLove(EntityTypes.ARMADILLO, 1.0F, 1)), 
/*  89 */           Pair.of(Integer.valueOf(2), new RunOne((List)ImmutableList.of(
/*  90 */                 Pair.of(new FollowTemptation(armadillo -> Float.valueOf(1.25F), armadillo -> Double.valueOf(armadillo.isBaby() ? 1.0D : 2.0D)), Integer.valueOf(1)), 
/*  91 */                 Pair.of(BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1.25F), Integer.valueOf(1))))), 
/*     */           
/*  93 */           Pair.of(Integer.valueOf(3), new RandomLookAround((IntProvider)UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F)), 
/*  94 */           Pair.of(Integer.valueOf(4), new RunOne(
/*  95 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */               
/*  98 */               (List)ImmutableList.of(
/*  99 */                 Pair.of(RandomStroll.stroll(1.0F), Integer.valueOf(1)), 
/* 100 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(1)), 
/* 101 */                 Pair.of(new DoNothing(30, 60), Integer.valueOf(1)))))));
/*     */   }
/*     */   
/*     */   private static ActivityData<Armadillo> initScaredActivity() {
/* 108 */     return ActivityData.create(Activity.PANIC, ImmutableList.of(
/* 109 */           Pair.of(Integer.valueOf(0), new ArmadilloBallUp())), 
/* 110 */         Set.of(Pair.of(MemoryModuleType.DANGER_DETECTED_RECENTLY, MemoryStatus.VALUE_PRESENT), 
/* 111 */           Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */   
/*     */   public static class ArmadilloBallUp extends Behavior<Armadillo> {
/* 116 */     private static final int BALL_UP_STAY_IN_STATE = 5 * TimeUtil.SECONDS_PER_MINUTE * 20;
/*     */     
/*     */     private static final int TICKS_DELAY_TO_DETERMINE_IF_DANGER_IS_STILL_AROUND = 5;
/*     */     
/*     */     private static final int DANGER_DETECTED_RECENTLY_DANGER_THRESHOLD = 75;
/*     */     
/* 120 */     private int nextPeekTimer = 0;
/*     */     
/*     */     private boolean dangerWasAround;
/*     */     
/*     */     public ArmadilloBallUp() {
/* 124 */       super(Map.of(), BALL_UP_STAY_IN_STATE);
/*     */     }
/*     */     
/*     */     protected void tick(ServerLevel level, Armadillo body, long timestamp) {
/* 129 */       super.tick(level, (LivingEntity)body, timestamp);
/* 130 */       if (this.nextPeekTimer > 0)
/* 131 */         this.nextPeekTimer--; 
/* 133 */       if (body.shouldSwitchToScaredState()) {
/* 134 */         body.switchToState(Armadillo.ArmadilloState.SCARED);
/* 135 */         if (body.onGround())
/* 136 */           body.playSound(SoundEvents.ARMADILLO_LAND); 
/*     */         return;
/*     */       } 
/* 140 */       Armadillo.ArmadilloState state = body.getState();
/* 141 */       long dangerTickCounter = body.getBrain().getTimeUntilExpiry(MemoryModuleType.DANGER_DETECTED_RECENTLY);
/* 142 */       boolean dangerIsAround = (dangerTickCounter > 75L);
/* 143 */       if (dangerIsAround != this.dangerWasAround)
/* 144 */         this.nextPeekTimer = pickNextPeekTimer(body); 
/* 146 */       this.dangerWasAround = dangerIsAround;
/* 147 */       if (state == Armadillo.ArmadilloState.SCARED) {
/* 148 */         if (this.nextPeekTimer == 0 && body.onGround() && dangerIsAround) {
/* 149 */           level.broadcastEntityEvent((Entity)body, (byte)64);
/* 150 */           this.nextPeekTimer = pickNextPeekTimer(body);
/*     */         } 
/* 154 */         if (dangerTickCounter < Armadillo.ArmadilloState.UNROLLING.animationDuration()) {
/* 155 */           body.playSound(SoundEvents.ARMADILLO_UNROLL_START);
/* 156 */           body.switchToState(Armadillo.ArmadilloState.UNROLLING);
/*     */         } 
/* 158 */       } else if (state == Armadillo.ArmadilloState.UNROLLING && dangerTickCounter > Armadillo.ArmadilloState.UNROLLING.animationDuration()) {
/* 159 */         body.switchToState(Armadillo.ArmadilloState.SCARED);
/*     */       } 
/*     */     }
/*     */     
/*     */     private int pickNextPeekTimer(Armadillo body) {
/* 164 */       return Armadillo.ArmadilloState.SCARED.animationDuration() + body.getRandom().nextIntBetweenInclusive(100, 400);
/*     */     }
/*     */     
/*     */     protected boolean checkExtraStartConditions(ServerLevel level, Armadillo body) {
/* 168 */       return body.onGround();
/*     */     }
/*     */     
/*     */     protected boolean canStillUse(ServerLevel level, Armadillo body, long timestamp) {
/* 173 */       return body.getState().isThreatened();
/*     */     }
/*     */     
/*     */     protected void start(ServerLevel level, Armadillo body, long timestamp) {
/* 177 */       body.rollUp();
/*     */     }
/*     */     
/*     */     protected void stop(ServerLevel level, Armadillo body, long timestamp) {
/* 182 */       if (!body.canStayRolledUp())
/* 183 */         body.rollOut(); 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ArmadilloPanic extends AnimalPanic<Armadillo> {
/*     */     public ArmadilloPanic(float speedMultiplier) {
/* 190 */       super(speedMultiplier, mob -> DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES);
/*     */     }
/*     */     
/*     */     protected void start(ServerLevel level, Armadillo armadillo, long timestamp) {
/* 195 */       armadillo.rollOut();
/* 196 */       super.start(level, (PathfinderMob)armadillo, timestamp);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updateActivity(Armadillo body) {
/* 201 */     body.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.PANIC, Activity.IDLE));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\armadillo\ArmadilloAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */