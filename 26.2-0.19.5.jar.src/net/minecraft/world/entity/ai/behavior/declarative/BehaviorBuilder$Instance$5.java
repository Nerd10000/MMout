/*     */ package net.minecraft.world.entity.ai.behavior.declarative;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ class null implements BehaviorBuilder.TriggerWithResult<E, R> {
/*     */   null(BehaviorBuilder.Instance this$0) {}
/*     */   
/*     */   public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 454 */     T1 r1 = (T1)t1Trigger.tryTrigger(level, body, timestamp);
/* 455 */     if (r1 == null)
/* 456 */       return null; 
/* 458 */     T2 r2 = (T2)t2Trigger.tryTrigger(level, body, timestamp);
/* 459 */     if (r2 == null)
/* 460 */       return null; 
/* 462 */     T3 r3 = (T3)t3Trigger.tryTrigger(level, body, timestamp);
/* 463 */     if (r3 == null)
/* 464 */       return null; 
/* 466 */     T4 r4 = (T4)t4Trigger.tryTrigger(level, body, timestamp);
/* 467 */     if (r4 == null)
/* 468 */       return null; 
/* 470 */     Function4<T1, T2, T3, T4, R> rf = (Function4<T1, T2, T3, T4, R>)fTrigger.tryTrigger(level, body, timestamp);
/* 471 */     if (rf == null)
/* 472 */       return null; 
/* 474 */     return (R)rf.apply(r1, r2, r3, r4);
/*     */   }
/*     */   
/*     */   public Set<MemoryModuleType<?>> memories() {
/* 479 */     Set<MemoryModuleType<?>> memories = new HashSet<>();
/* 480 */     memories.addAll(t1Trigger.memories());
/* 481 */     memories.addAll(t2Trigger.memories());
/* 482 */     memories.addAll(t3Trigger.memories());
/* 483 */     memories.addAll(t4Trigger.memories());
/* 484 */     memories.addAll(fTrigger.memories());
/* 485 */     return memories;
/*     */   }
/*     */   
/*     */   public String debugString() {
/* 490 */     return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + t1Trigger.debugString() + " * " + t2Trigger.debugString() + " * " + t3Trigger.debugString();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 495 */     return debugString();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$Instance$5.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */