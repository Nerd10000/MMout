/*     */ package net.minecraft.world.entity.ai.behavior.declarative;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ class null implements BehaviorBuilder.TriggerWithResult<E, A> {
/*     */   public A tryTrigger(ServerLevel level, E body, long timestamp) {
/* 186 */     return (A)a;
/*     */   }
/*     */   
/*     */   public Set<MemoryModuleType<?>> memories() {
/* 191 */     return Set.of();
/*     */   }
/*     */   
/*     */   public String debugString() {
/* 196 */     return debugString.get();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 201 */     return debugString();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$Constant$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */