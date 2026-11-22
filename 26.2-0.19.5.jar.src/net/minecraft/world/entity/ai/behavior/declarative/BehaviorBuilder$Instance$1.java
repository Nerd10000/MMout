/*     */ package net.minecraft.world.entity.ai.behavior.declarative;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ class null implements BehaviorBuilder.TriggerWithResult<E, R> {
/*     */   null(BehaviorBuilder.Instance this$0) {}
/*     */   
/*     */   public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 283 */     A ra = (A)aTrigger.tryTrigger(level, body, timestamp);
/* 284 */     if (ra == null)
/* 285 */       return null; 
/* 287 */     Function<A, R> rf = (Function<A, R>)fTrigger.tryTrigger(level, body, timestamp);
/* 288 */     if (rf == null)
/* 289 */       return null; 
/* 291 */     return rf.apply(ra);
/*     */   }
/*     */   
/*     */   public Set<MemoryModuleType<?>> memories() {
/* 296 */     Set<MemoryModuleType<?>> memories = new HashSet<>();
/* 297 */     memories.addAll(aTrigger.memories());
/* 298 */     memories.addAll(fTrigger.memories());
/* 299 */     return memories;
/*     */   }
/*     */   
/*     */   public String debugString() {
/* 304 */     return fTrigger.debugString() + " * " + fTrigger.debugString();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 309 */     return debugString();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$Instance$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */