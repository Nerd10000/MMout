/*    */ package net.minecraft.world.entity.monster.creaking;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.ActivityData;
/*    */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*    */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*    */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*    */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*    */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*    */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*    */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*    */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*    */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*    */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*    */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*    */ import net.minecraft.world.entity.ai.behavior.Swim;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ public class CreakingAi {
/*    */   private static ActivityData<Creaking> initCoreActivity() {
/* 34 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new Swim<Creaking>(0.8F) {
/*    */             protected boolean checkExtraStartConditions(ServerLevel level, Creaking body) {
/* 38 */               return (body.canMove() && super.checkExtraStartConditions(level, (LivingEntity)body));
/*    */             }
/*    */           }new LookAtTargetSink(45, 90), new MoveToTargetSink()));
/*    */   }
/*    */   
/*    */   private static ActivityData<Creaking> initIdleActivity() {
/* 47 */     return ActivityData.create(Activity.IDLE, 10, ImmutableList.of(
/* 48 */           StartAttacking.create((level, mob) -> mob.isActive(), (level, mob) -> mob.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)), 
/* 49 */           SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), new RunOne(
/* 50 */             (List)ImmutableList.of(
/* 51 */               Pair.of(RandomStroll.stroll(0.3F), Integer.valueOf(2)), 
/* 52 */               Pair.of(SetWalkTargetFromLookTarget.create(0.3F, 3), Integer.valueOf(2)), 
/* 53 */               Pair.of(new DoNothing(30, 60), Integer.valueOf(1))))));
/*    */   }
/*    */   
/*    */   private static ActivityData<Creaking> initFightActivity(Creaking body) {
/* 59 */     return ActivityData.create(Activity.FIGHT, 10, ImmutableList.of(
/* 60 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), 
/* 61 */           MeleeAttack.create(Creaking::canMove, 40), 
/* 62 */           StopAttackingIfTargetInvalid.create((level, target) -> !isAttackTargetStillReachable(body, target))), 
/* 63 */         (Set)ImmutableSet.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)));
/*    */   }
/*    */   
/*    */   private static boolean isAttackTargetStillReachable(Creaking creaking, LivingEntity target) {
/* 67 */     Optional<List<Player>> visibleAttackablePlayers = creaking.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS);
/* 68 */     return ((Boolean)visibleAttackablePlayers.<Boolean>map(players -> {
/*    */           if (target instanceof Player) {
/*    */             Player player = (Player)target;
/*    */             if (players.contains(player));
/*    */           } 
/*    */           return Boolean.valueOf(false);
/* 68 */         }).orElse(Boolean.valueOf(false))).booleanValue();
/*    */   }
/*    */   
/*    */   protected static List<ActivityData<Creaking>> getActivities(Creaking creaking) {
/* 72 */     return List.of(
/* 73 */         initCoreActivity(), 
/* 74 */         initIdleActivity(), 
/* 75 */         initFightActivity(creaking));
/*    */   }
/*    */   
/*    */   public static void updateActivity(Creaking creaking) {
/* 80 */     if (!creaking.canMove()) {
/* 81 */       creaking.getBrain().useDefaultActivity();
/*    */     } else {
/* 83 */       creaking.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\creaking\CreakingAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */