/*     */ package net.minecraft.world.entity.ai.behavior.declarative;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ class null implements BehaviorBuilder.TriggerWithResult<E, R> {
/*     */   null(BehaviorBuilder.Instance this$0) {}
/*     */   
/*     */   public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 321 */     T t = (T)tTrigger.tryTrigger(level, body, timestamp);
/* 322 */     if (t == null)
/* 323 */       return null; 
/* 325 */     return func.apply(t);
/*     */   }
/*     */   
/*     */   public Set<MemoryModuleType<?>> memories() {
/* 330 */     return tTrigger.memories();
/*     */   }
/*     */   
/*     */   public String debugString() {
/* 335 */     return tTrigger.debugString() + ".map[" + tTrigger.debugString() + "]";
/*     */   }
/*     */   
/*     */   public String toString() {
/* 340 */     return debugString();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$Instance$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */