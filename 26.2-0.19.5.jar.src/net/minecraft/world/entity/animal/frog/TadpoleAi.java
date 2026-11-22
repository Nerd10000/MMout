/*    */ package net.minecraft.world.entity.animal.frog;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.ActivityData;
/*    */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*    */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*    */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*    */ import net.minecraft.world.entity.ai.behavior.GateBehavior;
/*    */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*    */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*    */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*    */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*    */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ public class TadpoleAi {
/*    */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
/*    */   
/*    */   private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 0.5F;
/*    */   
/*    */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
/*    */   
/*    */   public static List<ActivityData<Tadpole>> getActivities() {
/* 33 */     return List.of(initCoreActivity(), initIdleActivity());
/*    */   }
/*    */   
/*    */   private static ActivityData<Tadpole> initCoreActivity() {
/* 37 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new AnimalPanic(2.0F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)));
/*    */   }
/*    */   
/*    */   private static ActivityData<Tadpole> initIdleActivity() {
/* 46 */     return ActivityData.create(Activity.IDLE, ImmutableList.of(
/* 47 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityTypes.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/* 48 */           Pair.of(Integer.valueOf(1), new FollowTemptation(s -> Float.valueOf(1.25F))), 
/* 49 */           Pair.of(Integer.valueOf(2), new GateBehavior(
/* 50 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*    */               
/* 53 */               (Set)ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, 
/*    */               
/* 56 */               (List)ImmutableList.of(
/* 57 */                 Pair.of(RandomStroll.swim(0.5F), Integer.valueOf(2)), 
/* 58 */                 Pair.of(SetWalkTargetFromLookTarget.create(0.5F, 3), Integer.valueOf(3)), 
/* 59 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::isInWater), Integer.valueOf(5)))))));
/*    */   }
/*    */   
/*    */   public static void updateActivity(Tadpole body) {
/* 66 */     body.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.IDLE));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\frog\TadpoleAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */