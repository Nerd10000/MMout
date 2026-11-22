/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class RunOne<E extends LivingEntity> extends GateBehavior<E> {
/*    */   public RunOne(List<Pair<? extends BehaviorControl<? super E>, Integer>> weightedBehaviors) {
/* 17 */     this(
/* 18 */         (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), weightedBehaviors);
/*    */   }
/*    */   
/*    */   public RunOne(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, List<Pair<? extends BehaviorControl<? super E>, Integer>> weightedBehaviors) {
/* 24 */     super(entryCondition, 
/*    */         
/* 26 */         (Set<MemoryModuleType<?>>)ImmutableSet.of(), GateBehavior.OrderPolicy.SHUFFLED, GateBehavior.RunningPolicy.RUN_ONE, weightedBehaviors);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\RunOne.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */