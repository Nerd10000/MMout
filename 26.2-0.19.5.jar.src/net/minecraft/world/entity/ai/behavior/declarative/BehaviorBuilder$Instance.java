/*     */ package net.minecraft.world.entity.ai.behavior.declarative;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.Const;
/*     */ import com.mojang.datafixers.kinds.IdF;
/*     */ import com.mojang.datafixers.kinds.OptionalBox;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import java.util.HashSet;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ public final class Instance<E extends LivingEntity> implements Applicative<BehaviorBuilder.Mu<E>, BehaviorBuilder.Instance.Mu<E>> {
/*     */   private static final class Mu<E extends LivingEntity> implements Applicative.Mu {}
/*     */   
/*     */   public <Value> Optional<Value> tryGet(MemoryAccessor<OptionalBox.Mu, Value> box) {
/* 234 */     return OptionalBox.unbox(box.value());
/*     */   }
/*     */   
/*     */   public <Value> Value get(MemoryAccessor<IdF.Mu, Value> box) {
/* 239 */     return (Value)IdF.get(box.value());
/*     */   }
/*     */   
/*     */   public <Value> BehaviorBuilder<E, MemoryAccessor<OptionalBox.Mu, Value>> registered(MemoryModuleType<Value> memory) {
/* 244 */     return new BehaviorBuilder.PureMemory<>(new MemoryCondition.Registered<>(memory));
/*     */   }
/*     */   
/*     */   public <Value> BehaviorBuilder<E, MemoryAccessor<IdF.Mu, Value>> present(MemoryModuleType<Value> memory) {
/* 249 */     return new BehaviorBuilder.PureMemory<>(new MemoryCondition.Present<>(memory));
/*     */   }
/*     */   
/*     */   public <Value> BehaviorBuilder<E, MemoryAccessor<Const.Mu<Unit>, Value>> absent(MemoryModuleType<Value> memory) {
/* 254 */     return new BehaviorBuilder.PureMemory<>(new MemoryCondition.Absent<>(memory));
/*     */   }
/*     */   
/*     */   public BehaviorBuilder<E, Unit> ifTriggered(Trigger<? super E> dependentTrigger) {
/* 259 */     return new BehaviorBuilder.TriggerWrapper<>(dependentTrigger);
/*     */   }
/*     */   
/*     */   public <A> BehaviorBuilder<E, A> point(A a) {
/* 266 */     return new BehaviorBuilder.Constant<>(a);
/*     */   }
/*     */   
/*     */   public <A> BehaviorBuilder<E, A> point(Supplier<String> debugString, A a) {
/* 270 */     return new BehaviorBuilder.Constant<>(a, debugString);
/*     */   }
/*     */   
/*     */   public <A, R> Function<App<BehaviorBuilder.Mu<E>, A>, App<BehaviorBuilder.Mu<E>, R>> lift1(App<BehaviorBuilder.Mu<E>, Function<A, R>> function) {
/* 275 */     return a -> {
/*     */         final BehaviorBuilder.TriggerWithResult<E, A> aTrigger = BehaviorBuilder.get(a);
/*     */         final BehaviorBuilder.TriggerWithResult<E, Function<A, R>> fTrigger = BehaviorBuilder.get(function);
/*     */         return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this) {
/*     */               public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 283 */                 A ra = aTrigger.tryTrigger(level, body, timestamp);
/* 284 */                 if (ra == null)
/* 285 */                   return null; 
/* 287 */                 Function<A, R> rf = fTrigger.tryTrigger(level, body, timestamp);
/* 288 */                 if (rf == null)
/* 289 */                   return null; 
/* 291 */                 return rf.apply(ra);
/*     */               }
/*     */               
/*     */               public Set<MemoryModuleType<?>> memories() {
/* 296 */                 Set<MemoryModuleType<?>> memories = new HashSet<>();
/* 297 */                 memories.addAll(aTrigger.memories());
/* 298 */                 memories.addAll(fTrigger.memories());
/* 299 */                 return memories;
/*     */               }
/*     */               
/*     */               public String debugString() {
/* 304 */                 return fTrigger.debugString() + " * " + fTrigger.debugString();
/*     */               }
/*     */               
/*     */               public String toString() {
/* 309 */                 return debugString();
/*     */               }
/*     */             });
/*     */       };
/*     */   }
/*     */   
/*     */   public <T, R> BehaviorBuilder<E, R> map(final Function<? super T, ? extends R> func, App<BehaviorBuilder.Mu<E>, T> ts) {
/* 317 */     final BehaviorBuilder.TriggerWithResult<E, T> tTrigger = BehaviorBuilder.get(ts);
/* 318 */     return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this) {
/*     */           public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 321 */             T t = tTrigger.tryTrigger(level, body, timestamp);
/* 322 */             if (t == null)
/* 323 */               return null; 
/* 325 */             return func.apply(t);
/*     */           }
/*     */           
/*     */           public Set<MemoryModuleType<?>> memories() {
/* 330 */             return tTrigger.memories();
/*     */           }
/*     */           
/*     */           public String debugString() {
/* 335 */             return tTrigger.debugString() + ".map[" + tTrigger.debugString() + "]";
/*     */           }
/*     */           
/*     */           public String toString() {
/* 340 */             return debugString();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public <A, B, R> BehaviorBuilder<E, R> ap2(App<BehaviorBuilder.Mu<E>, BiFunction<A, B, R>> func, App<BehaviorBuilder.Mu<E>, A> a, App<BehaviorBuilder.Mu<E>, B> b) {
/* 349 */     final BehaviorBuilder.TriggerWithResult<E, A> aTrigger = BehaviorBuilder.get(a);
/* 350 */     final BehaviorBuilder.TriggerWithResult<E, B> bTrigger = BehaviorBuilder.get(b);
/* 351 */     final BehaviorBuilder.TriggerWithResult<E, BiFunction<A, B, R>> fTrigger = BehaviorBuilder.get(func);
/* 353 */     return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this) {
/*     */           public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 356 */             A ra = aTrigger.tryTrigger(level, body, timestamp);
/* 357 */             if (ra == null)
/* 358 */               return null; 
/* 360 */             B rb = bTrigger.tryTrigger(level, body, timestamp);
/* 361 */             if (rb == null)
/* 362 */               return null; 
/* 364 */             BiFunction<A, B, R> fr = fTrigger.tryTrigger(level, body, timestamp);
/* 365 */             if (fr == null)
/* 366 */               return null; 
/* 368 */             return fr.apply(ra, rb);
/*     */           }
/*     */           
/*     */           public Set<MemoryModuleType<?>> memories() {
/* 373 */             Set<MemoryModuleType<?>> memories = new HashSet<>();
/* 374 */             memories.addAll(aTrigger.memories());
/* 375 */             memories.addAll(bTrigger.memories());
/* 376 */             memories.addAll(fTrigger.memories());
/* 377 */             return memories;
/*     */           }
/*     */           
/*     */           public String debugString() {
/* 382 */             return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + aTrigger.debugString();
/*     */           }
/*     */           
/*     */           public String toString() {
/* 387 */             return debugString();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public <T1, T2, T3, R> BehaviorBuilder<E, R> ap3(App<BehaviorBuilder.Mu<E>, Function3<T1, T2, T3, R>> func, App<BehaviorBuilder.Mu<E>, T1> t1, App<BehaviorBuilder.Mu<E>, T2> t2, App<BehaviorBuilder.Mu<E>, T3> t3) {
/* 394 */     final BehaviorBuilder.TriggerWithResult<E, T1> t1Trigger = BehaviorBuilder.get(t1);
/* 395 */     final BehaviorBuilder.TriggerWithResult<E, T2> t2Trigger = BehaviorBuilder.get(t2);
/* 396 */     final BehaviorBuilder.TriggerWithResult<E, T3> t3Trigger = BehaviorBuilder.get(t3);
/* 397 */     final BehaviorBuilder.TriggerWithResult<E, Function3<T1, T2, T3, R>> fTrigger = BehaviorBuilder.get(func);
/* 399 */     return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this) {
/*     */           public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 402 */             T1 r1 = t1Trigger.tryTrigger(level, body, timestamp);
/* 403 */             if (r1 == null)
/* 404 */               return null; 
/* 406 */             T2 r2 = t2Trigger.tryTrigger(level, body, timestamp);
/* 407 */             if (r2 == null)
/* 408 */               return null; 
/* 410 */             T3 r3 = t3Trigger.tryTrigger(level, body, timestamp);
/* 411 */             if (r3 == null)
/* 412 */               return null; 
/* 414 */             Function3<T1, T2, T3, R> rf = fTrigger.tryTrigger(level, body, timestamp);
/* 415 */             if (rf == null)
/* 416 */               return null; 
/* 418 */             return (R)rf.apply(r1, r2, r3);
/*     */           }
/*     */           
/*     */           public Set<MemoryModuleType<?>> memories() {
/* 423 */             Set<MemoryModuleType<?>> memories = new HashSet<>();
/* 424 */             memories.addAll(t1Trigger.memories());
/* 425 */             memories.addAll(t2Trigger.memories());
/* 426 */             memories.addAll(t3Trigger.memories());
/* 427 */             memories.addAll(fTrigger.memories());
/* 428 */             return memories;
/*     */           }
/*     */           
/*     */           public String debugString() {
/* 433 */             return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + t1Trigger.debugString() + " * " + t2Trigger.debugString();
/*     */           }
/*     */           
/*     */           public String toString() {
/* 438 */             return debugString();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public <T1, T2, T3, T4, R> BehaviorBuilder<E, R> ap4(App<BehaviorBuilder.Mu<E>, Function4<T1, T2, T3, T4, R>> func, App<BehaviorBuilder.Mu<E>, T1> t1, App<BehaviorBuilder.Mu<E>, T2> t2, App<BehaviorBuilder.Mu<E>, T3> t3, App<BehaviorBuilder.Mu<E>, T4> t4) {
/* 445 */     final BehaviorBuilder.TriggerWithResult<E, T1> t1Trigger = BehaviorBuilder.get(t1);
/* 446 */     final BehaviorBuilder.TriggerWithResult<E, T2> t2Trigger = BehaviorBuilder.get(t2);
/* 447 */     final BehaviorBuilder.TriggerWithResult<E, T3> t3Trigger = BehaviorBuilder.get(t3);
/* 448 */     final BehaviorBuilder.TriggerWithResult<E, T4> t4Trigger = BehaviorBuilder.get(t4);
/* 449 */     final BehaviorBuilder.TriggerWithResult<E, Function4<T1, T2, T3, T4, R>> fTrigger = BehaviorBuilder.get(func);
/* 451 */     return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this) {
/*     */           public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 454 */             T1 r1 = t1Trigger.tryTrigger(level, body, timestamp);
/* 455 */             if (r1 == null)
/* 456 */               return null; 
/* 458 */             T2 r2 = t2Trigger.tryTrigger(level, body, timestamp);
/* 459 */             if (r2 == null)
/* 460 */               return null; 
/* 462 */             T3 r3 = t3Trigger.tryTrigger(level, body, timestamp);
/* 463 */             if (r3 == null)
/* 464 */               return null; 
/* 466 */             T4 r4 = t4Trigger.tryTrigger(level, body, timestamp);
/* 467 */             if (r4 == null)
/* 468 */               return null; 
/* 470 */             Function4<T1, T2, T3, T4, R> rf = fTrigger.tryTrigger(level, body, timestamp);
/* 471 */             if (rf == null)
/* 472 */               return null; 
/* 474 */             return (R)rf.apply(r1, r2, r3, r4);
/*     */           }
/*     */           
/*     */           public Set<MemoryModuleType<?>> memories() {
/* 479 */             Set<MemoryModuleType<?>> memories = new HashSet<>();
/* 480 */             memories.addAll(t1Trigger.memories());
/* 481 */             memories.addAll(t2Trigger.memories());
/* 482 */             memories.addAll(t3Trigger.memories());
/* 483 */             memories.addAll(t4Trigger.memories());
/* 484 */             memories.addAll(fTrigger.memories());
/* 485 */             return memories;
/*     */           }
/*     */           
/*     */           public String debugString() {
/* 490 */             return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + t1Trigger.debugString() + " * " + t2Trigger.debugString() + " * " + t3Trigger.debugString();
/*     */           }
/*     */           
/*     */           public String toString() {
/* 495 */             return debugString();
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$Instance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */