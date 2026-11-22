/*    */ package net.minecraft.world.entity.ai.behavior.declarative;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.behavior.OneShot;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ class null extends OneShot<E> {
/*    */   public boolean trigger(ServerLevel level, E body, long timestamp) {
/* 48 */     Trigger<E> trigger = (Trigger<E>)resolvedBuilder.tryTrigger(level, body, timestamp);
/* 49 */     if (trigger == null)
/* 50 */       return false; 
/* 53 */     return trigger.trigger(level, body, timestamp);
/*    */   }
/*    */   
/*    */   public Set<MemoryModuleType<?>> getRequiredMemories() {
/* 58 */     return resolvedBuilder.memories();
/*    */   }
/*    */   
/*    */   public String debugString() {
/* 63 */     return "OneShot[" + resolvedBuilder.debugString() + "]";
/*    */   }
/*    */   
/*    */   public String toString() {
/* 68 */     return debugString();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */