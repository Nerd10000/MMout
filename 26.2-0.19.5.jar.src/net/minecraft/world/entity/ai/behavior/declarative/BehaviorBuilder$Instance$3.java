/*     */ package net.minecraft.world.entity.ai.behavior.declarative;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ class null implements BehaviorBuilder.TriggerWithResult<E, R> {
/*     */   null(BehaviorBuilder.Instance this$0) {}
/*     */   
/*     */   public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 356 */     A ra = (A)aTrigger.tryTrigger(level, body, timestamp);
/* 357 */     if (ra == null)
/* 358 */       return null; 
/* 360 */     B rb = (B)bTrigger.tryTrigger(level, body, timestamp);
/* 361 */     if (rb == null)
/* 362 */       return null; 
/* 364 */     BiFunction<A, B, R> fr = (BiFunction<A, B, R>)fTrigger.tryTrigger(level, body, timestamp);
/* 365 */     if (fr == null)
/* 366 */       return null; 
/* 368 */     return fr.apply(ra, rb);
/*     */   }
/*     */   
/*     */   public Set<MemoryModuleType<?>> memories() {
/* 373 */     Set<MemoryModuleType<?>> memories = new HashSet<>();
/* 374 */     memories.addAll(aTrigger.memories());
/* 375 */     memories.addAll(bTrigger.memories());
/* 376 */     memories.addAll(fTrigger.memories());
/* 377 */     return memories;
/*     */   }
/*     */   
/*     */   public String debugString() {
/* 382 */     return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + aTrigger.debugString();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 387 */     return debugString();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$Instance$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */