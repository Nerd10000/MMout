/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ 
/*    */ public class TriggerGate {
/*    */   public static <E extends LivingEntity> OneShot<E> triggerOneShuffled(List<Pair<? extends Trigger<? super E>, Integer>> weightedTriggers) {
/* 13 */     return triggerGate(weightedTriggers, GateBehavior.OrderPolicy.SHUFFLED, GateBehavior.RunningPolicy.RUN_ONE);
/*    */   }
/*    */   
/*    */   public static <E extends LivingEntity> OneShot<E> triggerGate(List<Pair<? extends Trigger<? super E>, Integer>> weightedBehaviors, GateBehavior.OrderPolicy orderPolicy, GateBehavior.RunningPolicy runningPolicy) {
/* 19 */     ShufflingList<Trigger<? super E>> behaviors = new ShufflingList<>();
/* 20 */     weightedBehaviors.forEach(entry -> behaviors.add((Trigger)entry.getFirst(), ((Integer)entry.getSecond()).intValue()));
/* 22 */     return BehaviorBuilder.create(i -> i.point(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\TriggerGate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */