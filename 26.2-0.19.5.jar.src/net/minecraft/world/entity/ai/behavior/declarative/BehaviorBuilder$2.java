/*    */ package net.minecraft.world.entity.ai.behavior.declarative;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.OneShot;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ class null extends OneShot<E> {
/*    */   public boolean trigger(ServerLevel level, E body, long timestamp) {
/* 81 */     return wrapped.trigger(level, (LivingEntity)body, timestamp);
/*    */   }
/*    */   
/*    */   public Set<MemoryModuleType<?>> getRequiredMemories() {
/* 86 */     Set<MemoryModuleType<?>> memories = new HashSet<>();
/* 87 */     memories.addAll(wrapped.getRequiredMemories());
/* 88 */     memories.addAll(second.getRequiredMemories());
/* 89 */     return memories;
/*    */   }
/*    */   
/*    */   public String debugString() {
/* 94 */     return "OneShot[stuff]";
/*    */   }
/*    */   
/*    */   public String toString() {
/* 99 */     return debugString();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */