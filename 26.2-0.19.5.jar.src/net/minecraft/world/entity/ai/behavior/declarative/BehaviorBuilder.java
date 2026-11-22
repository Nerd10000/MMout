/*     */ package net.minecraft.world.entity.ai.behavior.declarative;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.Const;
/*     */ import com.mojang.datafixers.kinds.IdF;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.kinds.OptionalBox;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.OneShot;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ public class BehaviorBuilder<E extends LivingEntity, M> implements App<BehaviorBuilder.Mu<E>, M> {
/*     */   private final TriggerWithResult<E, M> trigger;
/*     */   
/*     */   public static final class Mu<E extends LivingEntity> implements K1 {}
/*     */   
/*     */   public static <E extends LivingEntity, M> BehaviorBuilder<E, M> unbox(App<Mu<E>, M> box) {
/*  34 */     return (BehaviorBuilder)box;
/*     */   }
/*     */   
/*     */   public static <E extends LivingEntity> Instance<E> instance() {
/*  38 */     return new Instance<>();
/*     */   }
/*     */   
/*     */   public static <E extends LivingEntity> OneShot<E> create(Function<Instance<E>, ? extends App<Mu<E>, Trigger<E>>> builder) {
/*  43 */     final TriggerWithResult<E, Trigger<E>> resolvedBuilder = get(builder.apply(instance()));
/*  44 */     return new OneShot<E>() {
/*     */         public boolean trigger(ServerLevel level, E body, long timestamp) {
/*  48 */           Trigger<E> trigger = resolvedBuilder.tryTrigger(level, body, timestamp);
/*  49 */           if (trigger == null)
/*  50 */             return false; 
/*  53 */           return trigger.trigger(level, body, timestamp);
/*     */         }
/*     */         
/*     */         public Set<MemoryModuleType<?>> getRequiredMemories() {
/*  58 */           return resolvedBuilder.memories();
/*     */         }
/*     */         
/*     */         public String debugString() {
/*  63 */           return "OneShot[" + resolvedBuilder.debugString() + "]";
/*     */         }
/*     */         
/*     */         public String toString() {
/*  68 */           return debugString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <E extends LivingEntity> OneShot<E> sequence(Trigger<? super E> first, final OneShot<? super E> second) {
/*  77 */     final OneShot<E> wrapped = create(i -> i.group(i.ifTriggered(first)).apply(i, ()));
/*  78 */     return new OneShot<E>() {
/*     */         public boolean trigger(ServerLevel level, E body, long timestamp) {
/*  81 */           return wrapped.trigger(level, (LivingEntity)body, timestamp);
/*     */         }
/*     */         
/*     */         public Set<MemoryModuleType<?>> getRequiredMemories() {
/*  86 */           Set<MemoryModuleType<?>> memories = new HashSet<>();
/*  87 */           memories.addAll(wrapped.getRequiredMemories());
/*  88 */           memories.addAll(second.getRequiredMemories());
/*  89 */           return memories;
/*     */         }
/*     */         
/*     */         public String debugString() {
/*  94 */           return "OneShot[stuff]";
/*     */         }
/*     */         
/*     */         public String toString() {
/*  99 */           return debugString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <E extends LivingEntity> OneShot<E> triggerIf(Predicate<E> predicate, OneShot<? super E> behavior) {
/* 106 */     return sequence((Trigger<? super E>)triggerIf(predicate), behavior);
/*     */   }
/*     */   
/*     */   public static <E extends LivingEntity> OneShot<E> triggerIf(Predicate<E> predicate) {
/* 111 */     return create(i -> i.point(()));
/*     */   }
/*     */   
/*     */   public static <E extends LivingEntity> OneShot<E> triggerIf(BiPredicate<ServerLevel, E> predicate) {
/* 116 */     return create(i -> i.point(()));
/*     */   }
/*     */   
/*     */   private static <E extends LivingEntity, M> TriggerWithResult<E, M> get(App<Mu<E>, M> box) {
/* 121 */     return (unbox(box)).trigger;
/*     */   }
/*     */   
/*     */   private BehaviorBuilder(TriggerWithResult<E, M> trigger) {
/* 135 */     this.trigger = trigger;
/*     */   }
/*     */   
/*     */   private static <E extends LivingEntity, M> BehaviorBuilder<E, M> create(TriggerWithResult<E, M> instanceFactory) {
/* 139 */     return new BehaviorBuilder<>(instanceFactory);
/*     */   }
/*     */   
/*     */   private static final class PureMemory<E extends LivingEntity, F extends K1, Value> extends BehaviorBuilder<E, MemoryAccessor<F, Value>> {
/*     */     private PureMemory(MemoryCondition<F, Value> condition) {
/* 147 */       super(new BehaviorBuilder.TriggerWithResult<E, MemoryAccessor<F, Value>>(condition) {
/*     */             public MemoryAccessor<F, Value> tryTrigger(ServerLevel level, E body, long timestamp) {
/* 151 */               Brain<?> brain = body.getBrain();
/* 152 */               Optional<Value> value = brain.getMemoryInternal(condition.memory());
/* 153 */               if (value == null)
/* 154 */                 return null; 
/* 156 */               return condition.createAccessor(brain, value);
/*     */             }
/*     */             
/*     */             public Set<MemoryModuleType<?>> memories() {
/* 161 */               return Set.of(condition.memory());
/*     */             }
/*     */             
/*     */             public String debugString() {
/* 166 */               return "M[" + String.valueOf(condition) + "]";
/*     */             }
/*     */             
/*     */             public String toString() {
/* 171 */               return debugString();
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements TriggerWithResult<E, MemoryAccessor<F, Value>> {
/*     */     public MemoryAccessor<F, Value> tryTrigger(ServerLevel level, E body, long timestamp) {
/*     */       Brain<?> brain = body.getBrain();
/*     */       Optional<Value> value = brain.getMemoryInternal(condition.memory());
/*     */       if (value == null)
/*     */         return null; 
/*     */       return condition.createAccessor(brain, value);
/*     */     }
/*     */     
/*     */     public Set<MemoryModuleType<?>> memories() {
/*     */       return Set.of(condition.memory());
/*     */     }
/*     */     
/*     */     public String debugString() {
/*     */       return "M[" + String.valueOf(condition) + "]";
/*     */     }
/*     */     
/*     */     public String toString() {
/* 171 */       return debugString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Constant<E extends LivingEntity, A> extends BehaviorBuilder<E, A> {
/*     */     private Constant(A a) {
/* 179 */       this(a, () -> "C[" + String.valueOf(a) + "]");
/*     */     }
/*     */     
/*     */     private Constant(A a, Supplier<String> debugString) {
/* 183 */       super(new BehaviorBuilder.TriggerWithResult<E, A>(a, debugString) {
/*     */             public A tryTrigger(ServerLevel level, E body, long timestamp) {
/* 186 */               return (A)a;
/*     */             }
/*     */             
/*     */             public Set<MemoryModuleType<?>> memories() {
/* 191 */               return Set.of();
/*     */             }
/*     */             
/*     */             public String debugString() {
/* 196 */               return debugString.get();
/*     */             }
/*     */             
/*     */             public String toString() {
/* 201 */               return debugString();
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements TriggerWithResult<E, A> {
/*     */     public A tryTrigger(ServerLevel level, E body, long timestamp) {
/*     */       return (A)a;
/*     */     }
/*     */     
/*     */     public Set<MemoryModuleType<?>> memories() {
/*     */       return Set.of();
/*     */     }
/*     */     
/*     */     public String debugString() {
/*     */       return debugString.get();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 201 */       return debugString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TriggerWrapper<E extends LivingEntity> extends BehaviorBuilder<E, Unit> {
/*     */     private TriggerWrapper(Trigger<? super E> dependentTrigger) {
/* 209 */       super(new BehaviorBuilder.TriggerWithResult<E, Unit>(dependentTrigger) {
/*     */             public Unit tryTrigger(ServerLevel level, E body, long timestamp) {
/* 212 */               return dependentTrigger.trigger(level, body, timestamp) ? Unit.INSTANCE : null;
/*     */             }
/*     */             
/*     */             public Set<MemoryModuleType<?>> memories() {
/* 217 */               return Set.of();
/*     */             }
/*     */             
/*     */             public String debugString() {
/* 222 */               return "T[" + String.valueOf(dependentTrigger) + "]";
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements TriggerWithResult<E, Unit> {
/*     */     public Unit tryTrigger(ServerLevel level, E body, long timestamp) {
/*     */       return dependentTrigger.trigger(level, body, timestamp) ? Unit.INSTANCE : null;
/*     */     }
/*     */     
/*     */     public Set<MemoryModuleType<?>> memories() {
/*     */       return Set.of();
/*     */     }
/*     */     
/*     */     public String debugString() {
/* 222 */       return "T[" + String.valueOf(dependentTrigger) + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Instance<E extends LivingEntity> implements Applicative<Mu<E>, Instance.Mu<E>> {
/*     */     private static final class Mu<E extends LivingEntity> implements Applicative.Mu {}
/*     */     
/*     */     public <Value> Optional<Value> tryGet(MemoryAccessor<OptionalBox.Mu, Value> box) {
/* 234 */       return OptionalBox.unbox(box.value());
/*     */     }
/*     */     
/*     */     public <Value> Value get(MemoryAccessor<IdF.Mu, Value> box) {
/* 239 */       return (Value)IdF.get(box.value());
/*     */     }
/*     */     
/*     */     public <Value> BehaviorBuilder<E, MemoryAccessor<OptionalBox.Mu, Value>> registered(MemoryModuleType<Value> memory) {
/* 244 */       return new BehaviorBuilder.PureMemory<>(new MemoryCondition.Registered<>(memory));
/*     */     }
/*     */     
/*     */     public <Value> BehaviorBuilder<E, MemoryAccessor<IdF.Mu, Value>> present(MemoryModuleType<Value> memory) {
/* 249 */       return new BehaviorBuilder.PureMemory<>(new MemoryCondition.Present<>(memory));
/*     */     }
/*     */     
/*     */     public <Value> BehaviorBuilder<E, MemoryAccessor<Const.Mu<Unit>, Value>> absent(MemoryModuleType<Value> memory) {
/* 254 */       return new BehaviorBuilder.PureMemory<>(new MemoryCondition.Absent<>(memory));
/*     */     }
/*     */     
/*     */     public BehaviorBuilder<E, Unit> ifTriggered(Trigger<? super E> dependentTrigger) {
/* 259 */       return new BehaviorBuilder.TriggerWrapper<>(dependentTrigger);
/*     */     }
/*     */     
/*     */     public <A> BehaviorBuilder<E, A> point(A a) {
/* 266 */       return new BehaviorBuilder.Constant<>(a);
/*     */     }
/*     */     
/*     */     public <A> BehaviorBuilder<E, A> point(Supplier<String> debugString, A a) {
/* 270 */       return new BehaviorBuilder.Constant<>(a, debugString);
/*     */     }
/*     */     
/*     */     public <A, R> Function<App<BehaviorBuilder.Mu<E>, A>, App<BehaviorBuilder.Mu<E>, R>> lift1(App<BehaviorBuilder.Mu<E>, Function<A, R>> function) {
/* 275 */       return a -> {
/*     */           final BehaviorBuilder.TriggerWithResult<E, A> aTrigger = BehaviorBuilder.get(a);
/*     */           final BehaviorBuilder.TriggerWithResult<E, Function<A, R>> fTrigger = BehaviorBuilder.get(function);
/*     */           return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this) {
/*     */                 public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 283 */                   A ra = aTrigger.tryTrigger(level, body, timestamp);
/* 284 */                   if (ra == null)
/* 285 */                     return null; 
/* 287 */                   Function<A, R> rf = fTrigger.tryTrigger(level, body, timestamp);
/* 288 */                   if (rf == null)
/* 289 */                     return null; 
/* 291 */                   return rf.apply(ra);
/*     */                 }
/*     */                 
/*     */                 public Set<MemoryModuleType<?>> memories() {
/* 296 */                   Set<MemoryModuleType<?>> memories = new HashSet<>();
/* 297 */                   memories.addAll(aTrigger.memories());
/* 298 */                   memories.addAll(fTrigger.memories());
/* 299 */                   return memories;
/*     */                 }
/*     */                 
/*     */                 public String debugString() {
/* 304 */                   return fTrigger.debugString() + " * " + fTrigger.debugString();
/*     */                 }
/*     */                 
/*     */                 public String toString() {
/* 309 */                   return debugString();
/*     */                 }
/*     */               });
/*     */         };
/*     */     }
/*     */     
/*     */     public <T, R> BehaviorBuilder<E, R> map(final Function<? super T, ? extends R> func, App<BehaviorBuilder.Mu<E>, T> ts) {
/* 317 */       final BehaviorBuilder.TriggerWithResult<E, T> tTrigger = BehaviorBuilder.get(ts);
/* 318 */       return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this) {
/*     */             public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 321 */               T t = tTrigger.tryTrigger(level, body, timestamp);
/* 322 */               if (t == null)
/* 323 */                 return null; 
/* 325 */               return func.apply(t);
/*     */             }
/*     */             
/*     */             public Set<MemoryModuleType<?>> memories() {
/* 330 */               return tTrigger.memories();
/*     */             }
/*     */             
/*     */             public String debugString() {
/* 335 */               return tTrigger.debugString() + ".map[" + tTrigger.debugString() + "]";
/*     */             }
/*     */             
/*     */             public String toString() {
/* 340 */               return debugString();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public <A, B, R> BehaviorBuilder<E, R> ap2(App<BehaviorBuilder.Mu<E>, BiFunction<A, B, R>> func, App<BehaviorBuilder.Mu<E>, A> a, App<BehaviorBuilder.Mu<E>, B> b) {
/* 349 */       final BehaviorBuilder.TriggerWithResult<E, A> aTrigger = BehaviorBuilder.get(a);
/* 350 */       final BehaviorBuilder.TriggerWithResult<E, B> bTrigger = BehaviorBuilder.get(b);
/* 351 */       final BehaviorBuilder.TriggerWithResult<E, BiFunction<A, B, R>> fTrigger = BehaviorBuilder.get(func);
/* 353 */       return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this) {
/*     */             public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 356 */               A ra = aTrigger.tryTrigger(level, body, timestamp);
/* 357 */               if (ra == null)
/* 358 */                 return null; 
/* 360 */               B rb = bTrigger.tryTrigger(level, body, timestamp);
/* 361 */               if (rb == null)
/* 362 */                 return null; 
/* 364 */               BiFunction<A, B, R> fr = fTrigger.tryTrigger(level, body, timestamp);
/* 365 */               if (fr == null)
/* 366 */                 return null; 
/* 368 */               return fr.apply(ra, rb);
/*     */             }
/*     */             
/*     */             public Set<MemoryModuleType<?>> memories() {
/* 373 */               Set<MemoryModuleType<?>> memories = new HashSet<>();
/* 374 */               memories.addAll(aTrigger.memories());
/* 375 */               memories.addAll(bTrigger.memories());
/* 376 */               memories.addAll(fTrigger.memories());
/* 377 */               return memories;
/*     */             }
/*     */             
/*     */             public String debugString() {
/* 382 */               return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + aTrigger.debugString();
/*     */             }
/*     */             
/*     */             public String toString() {
/* 387 */               return debugString();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public <T1, T2, T3, R> BehaviorBuilder<E, R> ap3(App<BehaviorBuilder.Mu<E>, Function3<T1, T2, T3, R>> func, App<BehaviorBuilder.Mu<E>, T1> t1, App<BehaviorBuilder.Mu<E>, T2> t2, App<BehaviorBuilder.Mu<E>, T3> t3) {
/* 394 */       final BehaviorBuilder.TriggerWithResult<E, T1> t1Trigger = BehaviorBuilder.get(t1);
/* 395 */       final BehaviorBuilder.TriggerWithResult<E, T2> t2Trigger = BehaviorBuilder.get(t2);
/* 396 */       final BehaviorBuilder.TriggerWithResult<E, T3> t3Trigger = BehaviorBuilder.get(t3);
/* 397 */       final BehaviorBuilder.TriggerWithResult<E, Function3<T1, T2, T3, R>> fTrigger = BehaviorBuilder.get(func);
/* 399 */       return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this) {
/*     */             public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 402 */               T1 r1 = t1Trigger.tryTrigger(level, body, timestamp);
/* 403 */               if (r1 == null)
/* 404 */                 return null; 
/* 406 */               T2 r2 = t2Trigger.tryTrigger(level, body, timestamp);
/* 407 */               if (r2 == null)
/* 408 */                 return null; 
/* 410 */               T3 r3 = t3Trigger.tryTrigger(level, body, timestamp);
/* 411 */               if (r3 == null)
/* 412 */                 return null; 
/* 414 */               Function3<T1, T2, T3, R> rf = fTrigger.tryTrigger(level, body, timestamp);
/* 415 */               if (rf == null)
/* 416 */                 return null; 
/* 418 */               return (R)rf.apply(r1, r2, r3);
/*     */             }
/*     */             
/*     */             public Set<MemoryModuleType<?>> memories() {
/* 423 */               Set<MemoryModuleType<?>> memories = new HashSet<>();
/* 424 */               memories.addAll(t1Trigger.memories());
/* 425 */               memories.addAll(t2Trigger.memories());
/* 426 */               memories.addAll(t3Trigger.memories());
/* 427 */               memories.addAll(fTrigger.memories());
/* 428 */               return memories;
/*     */             }
/*     */             
/*     */             public String debugString() {
/* 433 */               return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + t1Trigger.debugString() + " * " + t2Trigger.debugString();
/*     */             }
/*     */             
/*     */             public String toString() {
/* 438 */               return debugString();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public <T1, T2, T3, T4, R> BehaviorBuilder<E, R> ap4(App<BehaviorBuilder.Mu<E>, Function4<T1, T2, T3, T4, R>> func, App<BehaviorBuilder.Mu<E>, T1> t1, App<BehaviorBuilder.Mu<E>, T2> t2, App<BehaviorBuilder.Mu<E>, T3> t3, App<BehaviorBuilder.Mu<E>, T4> t4) {
/* 445 */       final BehaviorBuilder.TriggerWithResult<E, T1> t1Trigger = BehaviorBuilder.get(t1);
/* 446 */       final BehaviorBuilder.TriggerWithResult<E, T2> t2Trigger = BehaviorBuilder.get(t2);
/* 447 */       final BehaviorBuilder.TriggerWithResult<E, T3> t3Trigger = BehaviorBuilder.get(t3);
/* 448 */       final BehaviorBuilder.TriggerWithResult<E, T4> t4Trigger = BehaviorBuilder.get(t4);
/* 449 */       final BehaviorBuilder.TriggerWithResult<E, Function4<T1, T2, T3, T4, R>> fTrigger = BehaviorBuilder.get(func);
/* 451 */       return BehaviorBuilder.create(new BehaviorBuilder.TriggerWithResult<E, R>(this) {
/*     */             public R tryTrigger(ServerLevel level, E body, long timestamp) {
/* 454 */               T1 r1 = t1Trigger.tryTrigger(level, body, timestamp);
/* 455 */               if (r1 == null)
/* 456 */                 return null; 
/* 458 */               T2 r2 = t2Trigger.tryTrigger(level, body, timestamp);
/* 459 */               if (r2 == null)
/* 460 */                 return null; 
/* 462 */               T3 r3 = t3Trigger.tryTrigger(level, body, timestamp);
/* 463 */               if (r3 == null)
/* 464 */                 return null; 
/* 466 */               T4 r4 = t4Trigger.tryTrigger(level, body, timestamp);
/* 467 */               if (r4 == null)
/* 468 */                 return null; 
/* 470 */               Function4<T1, T2, T3, T4, R> rf = fTrigger.tryTrigger(level, body, timestamp);
/* 471 */               if (rf == null)
/* 472 */                 return null; 
/* 474 */               return (R)rf.apply(r1, r2, r3, r4);
/*     */             }
/*     */             
/*     */             public Set<MemoryModuleType<?>> memories() {
/* 479 */               Set<MemoryModuleType<?>> memories = new HashSet<>();
/* 480 */               memories.addAll(t1Trigger.memories());
/* 481 */               memories.addAll(t2Trigger.memories());
/* 482 */               memories.addAll(t3Trigger.memories());
/* 483 */               memories.addAll(t4Trigger.memories());
/* 484 */               memories.addAll(fTrigger.memories());
/* 485 */               return memories;
/*     */             }
/*     */             
/*     */             public String debugString() {
/* 490 */               return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + t1Trigger.debugString() + " * " + t2Trigger.debugString() + " * " + t3Trigger.debugString();
/*     */             }
/*     */             
/*     */             public String toString() {
/* 495 */               return debugString();
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Mu<E extends LivingEntity> implements Applicative.Mu {}
/*     */   
/*     */   class null implements TriggerWithResult<E, R> {
/*     */     null(BehaviorBuilder.Instance this$0) {}
/*     */     
/*     */     public R tryTrigger(ServerLevel level, E body, long timestamp) {
/*     */       A ra = aTrigger.tryTrigger(level, body, timestamp);
/*     */       if (ra == null)
/*     */         return null; 
/*     */       Function<A, R> rf = fTrigger.tryTrigger(level, body, timestamp);
/*     */       if (rf == null)
/*     */         return null; 
/*     */       return rf.apply(ra);
/*     */     }
/*     */     
/*     */     public Set<MemoryModuleType<?>> memories() {
/*     */       Set<MemoryModuleType<?>> memories = new HashSet<>();
/*     */       memories.addAll(aTrigger.memories());
/*     */       memories.addAll(fTrigger.memories());
/*     */       return memories;
/*     */     }
/*     */     
/*     */     public String debugString() {
/*     */       return fTrigger.debugString() + " * " + fTrigger.debugString();
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return debugString();
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements TriggerWithResult<E, R> {
/*     */     null(BehaviorBuilder.Instance this$0) {}
/*     */     
/*     */     public R tryTrigger(ServerLevel level, E body, long timestamp) {
/*     */       T t = tTrigger.tryTrigger(level, body, timestamp);
/*     */       if (t == null)
/*     */         return null; 
/*     */       return func.apply(t);
/*     */     }
/*     */     
/*     */     public Set<MemoryModuleType<?>> memories() {
/*     */       return tTrigger.memories();
/*     */     }
/*     */     
/*     */     public String debugString() {
/*     */       return tTrigger.debugString() + ".map[" + tTrigger.debugString() + "]";
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return debugString();
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements TriggerWithResult<E, R> {
/*     */     null(BehaviorBuilder.Instance this$0) {}
/*     */     
/*     */     public R tryTrigger(ServerLevel level, E body, long timestamp) {
/*     */       A ra = aTrigger.tryTrigger(level, body, timestamp);
/*     */       if (ra == null)
/*     */         return null; 
/*     */       B rb = bTrigger.tryTrigger(level, body, timestamp);
/*     */       if (rb == null)
/*     */         return null; 
/*     */       BiFunction<A, B, R> fr = fTrigger.tryTrigger(level, body, timestamp);
/*     */       if (fr == null)
/*     */         return null; 
/*     */       return fr.apply(ra, rb);
/*     */     }
/*     */     
/*     */     public Set<MemoryModuleType<?>> memories() {
/*     */       Set<MemoryModuleType<?>> memories = new HashSet<>();
/*     */       memories.addAll(aTrigger.memories());
/*     */       memories.addAll(bTrigger.memories());
/*     */       memories.addAll(fTrigger.memories());
/*     */       return memories;
/*     */     }
/*     */     
/*     */     public String debugString() {
/*     */       return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + aTrigger.debugString();
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return debugString();
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements TriggerWithResult<E, R> {
/*     */     null(BehaviorBuilder.Instance this$0) {}
/*     */     
/*     */     public R tryTrigger(ServerLevel level, E body, long timestamp) {
/*     */       T1 r1 = t1Trigger.tryTrigger(level, body, timestamp);
/*     */       if (r1 == null)
/*     */         return null; 
/*     */       T2 r2 = t2Trigger.tryTrigger(level, body, timestamp);
/*     */       if (r2 == null)
/*     */         return null; 
/*     */       T3 r3 = t3Trigger.tryTrigger(level, body, timestamp);
/*     */       if (r3 == null)
/*     */         return null; 
/*     */       Function3<T1, T2, T3, R> rf = fTrigger.tryTrigger(level, body, timestamp);
/*     */       if (rf == null)
/*     */         return null; 
/*     */       return (R)rf.apply(r1, r2, r3);
/*     */     }
/*     */     
/*     */     public Set<MemoryModuleType<?>> memories() {
/*     */       Set<MemoryModuleType<?>> memories = new HashSet<>();
/*     */       memories.addAll(t1Trigger.memories());
/*     */       memories.addAll(t2Trigger.memories());
/*     */       memories.addAll(t3Trigger.memories());
/*     */       memories.addAll(fTrigger.memories());
/*     */       return memories;
/*     */     }
/*     */     
/*     */     public String debugString() {
/*     */       return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + t1Trigger.debugString() + " * " + t2Trigger.debugString();
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return debugString();
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements TriggerWithResult<E, R> {
/*     */     null(BehaviorBuilder.Instance this$0) {}
/*     */     
/*     */     public R tryTrigger(ServerLevel level, E body, long timestamp) {
/*     */       T1 r1 = t1Trigger.tryTrigger(level, body, timestamp);
/*     */       if (r1 == null)
/*     */         return null; 
/*     */       T2 r2 = t2Trigger.tryTrigger(level, body, timestamp);
/*     */       if (r2 == null)
/*     */         return null; 
/*     */       T3 r3 = t3Trigger.tryTrigger(level, body, timestamp);
/*     */       if (r3 == null)
/*     */         return null; 
/*     */       T4 r4 = t4Trigger.tryTrigger(level, body, timestamp);
/*     */       if (r4 == null)
/*     */         return null; 
/*     */       Function4<T1, T2, T3, T4, R> rf = fTrigger.tryTrigger(level, body, timestamp);
/*     */       if (rf == null)
/*     */         return null; 
/*     */       return (R)rf.apply(r1, r2, r3, r4);
/*     */     }
/*     */     
/*     */     public Set<MemoryModuleType<?>> memories() {
/*     */       Set<MemoryModuleType<?>> memories = new HashSet<>();
/*     */       memories.addAll(t1Trigger.memories());
/*     */       memories.addAll(t2Trigger.memories());
/*     */       memories.addAll(t3Trigger.memories());
/*     */       memories.addAll(t4Trigger.memories());
/*     */       memories.addAll(fTrigger.memories());
/*     */       return memories;
/*     */     }
/*     */     
/*     */     public String debugString() {
/*     */       return fTrigger.debugString() + " * " + fTrigger.debugString() + " * " + t1Trigger.debugString() + " * " + t2Trigger.debugString() + " * " + t3Trigger.debugString();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 495 */       return debugString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface TriggerWithResult<E extends LivingEntity, R> {
/*     */     R tryTrigger(ServerLevel param1ServerLevel, E param1E, long param1Long);
/*     */     
/*     */     Set<MemoryModuleType<?>> memories();
/*     */     
/*     */     String debugString();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */