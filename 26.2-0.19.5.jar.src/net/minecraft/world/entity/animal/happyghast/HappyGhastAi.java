/*    */ package net.minecraft.world.entity.animal.happyghast;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.ActivityData;
/*    */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*    */ import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
/*    */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*    */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*    */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*    */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*    */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*    */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*    */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*    */ import net.minecraft.world.entity.ai.behavior.Swim;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ public class HappyGhastAi {
/*    */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
/*    */   
/*    */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
/*    */   
/*    */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT = 1.1F;
/*    */   
/*    */   private static final double BABY_GHAST_CLOSE_ENOUGH_DIST = 3.0D;
/*    */   
/* 29 */   private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(3, 16);
/*    */   
/*    */   protected static List<ActivityData<HappyGhast>> getActivities() {
/* 32 */     return List.of(
/* 33 */         initCoreActivity(), 
/* 34 */         initIdleActivity(), 
/* 35 */         initPanicActivity());
/*    */   }
/*    */   
/*    */   private static ActivityData<HappyGhast> initCoreActivity() {
/* 40 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new AnimalPanic(2.0F, 0), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)));
/*    */   }
/*    */   
/*    */   private static ActivityData<HappyGhast> initIdleActivity() {
/* 50 */     return ActivityData.create(Activity.IDLE, ImmutableList.of(
/* 51 */           Pair.of(Integer.valueOf(1), new FollowTemptation(mob -> Float.valueOf(1.25F), mob -> Double.valueOf(3.0D), true)), 
/* 52 */           Pair.of(Integer.valueOf(2), BabyFollowAdult.create(ADULT_FOLLOW_RANGE, mob -> Float.valueOf(1.1F), MemoryModuleType.NEAREST_VISIBLE_PLAYER, true)), 
/* 53 */           Pair.of(Integer.valueOf(3), BabyFollowAdult.create(ADULT_FOLLOW_RANGE, mob -> Float.valueOf(1.1F), MemoryModuleType.NEAREST_VISIBLE_ADULT, true)), 
/* 54 */           Pair.of(Integer.valueOf(4), new RunOne(
/* 55 */               (List)ImmutableList.of(
/* 56 */                 Pair.of(RandomStroll.fly(1.0F), Integer.valueOf(1)), 
/* 57 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(1)))))));
/*    */   }
/*    */   
/*    */   private static ActivityData<HappyGhast> initPanicActivity() {
/* 65 */     return ActivityData.create(Activity.PANIC, ImmutableList.of(), 
/* 66 */         Set.of(
/* 67 */           Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_PRESENT)));
/*    */   }
/*    */   
/*    */   public static void updateActivity(HappyGhast body) {
/* 72 */     body.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.PANIC, Activity.IDLE));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\happyghast\HappyGhastAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */