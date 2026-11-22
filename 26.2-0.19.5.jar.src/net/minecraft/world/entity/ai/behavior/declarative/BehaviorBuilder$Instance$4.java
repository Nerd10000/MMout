/*     */ package net.minecraft.world.entity.ai.behavior.declarative;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ class null implements BehaviorBuilder.TriggerWithResult<E, R> {
/*     */   null(BehaviorBuilder.Instance this$0) {}
/*     */   
/*     */   public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 402 */     T1 r1 = (T1)t1Trigger.tryTrigger(level, body, timestamp);
/* 403 */     if (r1 == null)
/* 404 */       return null; 
/* 406 */     T2 r2 = (T2)t2Trigger.tryTrigger(level, body, timestamp);
/* 407 */     if (r2 == null)
/* 408 */       return null; 
/* 410 */     T3 r3 = (T3)t3Trigger.tryTrigger(level, body, timestamp);
/* 411 */     if (r3 == null)
/* 412 */       return null; 
/* 414 */     Function3<T1, T2, T3, R> rf = (Function3<T1, T2, T3, R>)fTrigger.tryTrigger(level, body, timestamp);
/* 415 */     if (rf == null)
/* 416 */       return null; 
/* 418 */     return (R)rf.apply(r1, r2, r3);
/*     */   }
/*     */   
/*     */   public Set<MemoryModuleType<?>> memories() {
/* 423 */     Set<MemoryModuleType<?>> memories = new HashSet<>();
/* 424 */     memories.addAll(t1Trigger.memories());
/* 425 */     memories.addAll(t2Trigger.memories());
/* 426 */     memories.addAll(t3Trigger.memories());
/* 427 */     memories.addAll(fTrigger.memories());
/* 428 */     return memories;
/*     */   }
/*     */   
/*     */   public String debugString() {
/* 433 */     return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + t1Trigger.debugString() + " * " + t2Trigger.debugString();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 438 */     return debugString();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$Instance$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */