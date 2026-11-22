/*    */ package net.minecraft.world.entity.animal.nautilus;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.ActivityData;
/*    */ import net.minecraft.world.entity.ai.behavior.ChargeAttack;
/*    */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*    */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*    */ import net.minecraft.world.entity.ai.behavior.GateBehavior;
/*    */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*    */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*    */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*    */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*    */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ public class ZombieNautilusAi {
/*    */   private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 1.0F;
/*    */   
/*    */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 0.9F;
/*    */   
/*    */   private static final float SPEED_WHEN_ATTACKING = 0.5F;
/*    */   
/*    */   private static final float ATTACK_KNOCKBACK_FORCE = 2.0F;
/*    */   
/*    */   private static final int TIME_BETWEEN_ATTACKS = 80;
/*    */   
/*    */   private static final double MAX_CHARGE_DISTANCE = 12.0D;
/*    */   
/*    */   private static final double MAX_TARGET_DETECTION_DISTANCE = 11.0D;
/*    */   
/*    */   public static List<ActivityData<ZombieNautilus>> getActivities() {
/* 35 */     return List.of(
/* 36 */         initCoreActivity(), 
/* 37 */         initIdleActivity(), 
/* 38 */         initFightActivity());
/*    */   }
/*    */   
/*    */   private static ActivityData<ZombieNautilus> initCoreActivity() {
/* 43 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.CHARGE_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.ATTACK_TARGET_COOLDOWN)));
/*    */   }
/*    */   
/*    */   private static ActivityData<ZombieNautilus> initIdleActivity() {
/* 53 */     return ActivityData.create(Activity.IDLE, ImmutableList.of(
/* 54 */           Pair.of(Integer.valueOf(1), new FollowTemptation(mob -> Float.valueOf(0.9F), mob -> Double.valueOf(mob.isBaby() ? 2.5D : 3.5D))), 
/* 55 */           Pair.of(Integer.valueOf(2), StartAttacking.create(NautilusAi::findNearestValidAttackTarget)), 
/* 56 */           Pair.of(Integer.valueOf(3), new GateBehavior(
/* 57 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*    */               
/* 60 */               (Set)ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, 
/*    */               
/* 63 */               (List)ImmutableList.of(
/* 64 */                 Pair.of(RandomStroll.swim(1.0F), Integer.valueOf(2)), 
/* 65 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(3)))))));
/*    */   }
/*    */   
/*    */   private static ActivityData<ZombieNautilus> initFightActivity() {
/* 73 */     return ActivityData.create(Activity.FIGHT, ImmutableList.of(
/* 74 */           Pair.of(Integer.valueOf(0), new ChargeAttack(80, NautilusAi.ATTACK_TARGET_CONDITIONS, 0.5F, 2.0F, 12.0D, 11.0D, SoundEvents.ZOMBIE_NAUTILUS_DASH))), 
/*    */         
/* 82 */         (Set)ImmutableSet.of(
/* 83 */           Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 
/* 84 */           Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT), 
/* 85 */           Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 86 */           Pair.of(MemoryModuleType.CHARGE_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT)));
/*    */   }
/*    */   
/*    */   public static void updateActivity(ZombieNautilus body) {
/* 91 */     body.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\nautilus\ZombieNautilusAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */