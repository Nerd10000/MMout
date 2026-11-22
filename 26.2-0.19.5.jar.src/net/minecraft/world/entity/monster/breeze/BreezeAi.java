/*     */ package net.minecraft.world.entity.monster.breeze;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ public class BreezeAi {
/*     */   public static final float SPEED_MULTIPLIER_WHEN_SLIDING = 0.6F;
/*     */   
/*     */   public static final float JUMP_CIRCLE_INNER_RADIUS = 4.0F;
/*     */   
/*     */   public static final float JUMP_CIRCLE_MIDDLE_RADIUS = 8.0F;
/*     */   
/*     */   public static final float JUMP_CIRCLE_OUTER_RADIUS = 24.0F;
/*     */   
/*     */   private static final int TICKS_TO_REMEMBER_SEEN_TARGET = 100;
/*     */   
/*     */   protected static List<ActivityData<Breeze>> getActivities(Breeze breeze) {
/*  42 */     return List.of(
/*  43 */         initCoreActivity(), 
/*  44 */         initIdleActivity(), 
/*  45 */         initFightActivity(breeze));
/*     */   }
/*     */   
/*     */   private static ActivityData<Breeze> initCoreActivity() {
/*  50 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new LookAtTargetSink(45, 90)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Breeze> initIdleActivity() {
/*  57 */     return ActivityData.create(Activity.IDLE, ImmutableList.of(
/*  58 */           Pair.of(Integer.valueOf(0), StartAttacking.create((paramServerLevel, breeze) -> breeze.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE))), 
/*  59 */           Pair.of(Integer.valueOf(1), StartAttacking.create((paramServerLevel, breeze) -> breeze.getBrain().getMemory(MemoryModuleType.HURT_BY).map(DamageSource::getEntity).filter(()).map(()))), 
/*     */           
/*  63 */           Pair.of(Integer.valueOf(2), new SlideToTargetSink(20, 40)), 
/*  64 */           Pair.of(Integer.valueOf(3), new RunOne((List)ImmutableList.of(
/*  65 */                 Pair.of(new DoNothing(20, 100), Integer.valueOf(1)), 
/*  66 */                 Pair.of(RandomStroll.stroll(0.6F), Integer.valueOf(2)))))));
/*     */   }
/*     */   
/*     */   private static ActivityData<Breeze> initFightActivity(Breeze body) {
/*  74 */     Objects.requireNonNull(Sensor.wasEntityAttackableLastNTicks((LivingEntity)body, 100).negate());
/*  74 */     return ActivityData.create(Activity.FIGHT, ImmutableList.of(Pair.of(Integer.valueOf(0), StopAttackingIfTargetInvalid.create(Sensor.wasEntityAttackableLastNTicks((LivingEntity)body, 100).negate()::test)), 
/*  75 */           Pair.of(Integer.valueOf(1), new Shoot()), 
/*  76 */           Pair.of(Integer.valueOf(2), new LongJump()), 
/*  77 */           Pair.of(Integer.valueOf(3), new ShootWhenStuck()), 
/*  78 */           Pair.of(Integer.valueOf(4), new Slide())), 
/*  79 */         (Set)ImmutableSet.of(
/*  80 */           Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 
/*  81 */           Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */   
/*     */   public static void updateActivity(Breeze body) {
/*  87 */     body.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/*     */   }
/*     */   
/*     */   public static class SlideToTargetSink extends MoveToTargetSink {
/*     */     @VisibleForTesting
/*     */     public SlideToTargetSink(int minTimeout, int maxTimeout) {
/*  97 */       super(minTimeout, maxTimeout);
/*     */     }
/*     */     
/*     */     protected void start(ServerLevel level, Mob body, long timestamp) {
/* 102 */       super.start(level, body, timestamp);
/* 103 */       body.playSound(SoundEvents.BREEZE_SLIDE);
/* 104 */       body.setPose(Pose.SLIDING);
/*     */     }
/*     */     
/*     */     protected void stop(ServerLevel level, Mob body, long timestamp) {
/* 109 */       super.stop(level, body, timestamp);
/* 110 */       body.setPose(Pose.STANDING);
/* 113 */       if (body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET))
/* 114 */         body.getBrain().setMemoryWithExpiry(MemoryModuleType.BREEZE_SHOOT, Unit.INSTANCE, 60L); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\breeze\BreezeAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */