/*     */ package com.mojang.realmsclient.gui.task;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.TimeSource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DataFetcher {
/*  21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Executor executor;
/*     */   
/*     */   private final TimeUnit resolution;
/*     */   
/*     */   private final TimeSource timeSource;
/*     */   
/*     */   public DataFetcher(Executor executor, TimeUnit resolution, TimeSource timeSource) {
/*  28 */     this.executor = executor;
/*  29 */     this.resolution = resolution;
/*  30 */     this.timeSource = timeSource;
/*     */   }
/*     */   
/*     */   public <T> Task<T> createTask(String id, Callable<T> updater, Duration period, RepeatedDelayStrategy repeatStrategy) {
/*  34 */     long periodInUnit = this.resolution.convert(period);
/*  35 */     if (periodInUnit == 0L)
/*  36 */       throw new IllegalArgumentException("Period of " + String.valueOf(period) + " too short for selected resolution of " + String.valueOf(this.resolution)); 
/*  38 */     return new Task<>(id, updater, periodInUnit, repeatStrategy);
/*     */   }
/*     */   
/*     */   public Subscription createSubscription() {
/*  42 */     return new Subscription();
/*     */   }
/*     */   
/*     */   private static final class ComputationResult<T> extends Record {
/*     */     private final Either<T, Exception> value;
/*     */     
/*     */     private final long time;
/*     */     
/*     */     private ComputationResult(Either<T, Exception> value, long time) {
/*  45 */       this.value = value;
/*  45 */       this.time = time;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult<TT;>;
/*     */     }
/*     */     
/*     */     public Either<T, Exception> value() {
/*  45 */       return this.value;
/*     */     }
/*     */     
/*     */     public long time() {
/*  45 */       return this.time;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SuccessfulComputationResult<T> extends Record {
/*     */     private final T value;
/*     */     
/*     */     private final long time;
/*     */     
/*     */     private SuccessfulComputationResult(T value, long time) {
/*  47 */       this.value = value;
/*  47 */       this.time = time;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult<TT;>;
/*     */     }
/*     */     
/*     */     public T value() {
/*  47 */       return this.value;
/*     */     }
/*     */     
/*     */     public long time() {
/*  47 */       return this.time;
/*     */     }
/*     */   }
/*     */   
/*     */   public class Task<T> {
/*     */     private final String id;
/*     */     
/*     */     private final Callable<T> updater;
/*     */     
/*     */     private final long period;
/*     */     
/*     */     private final RepeatedDelayStrategy repeatStrategy;
/*     */     
/*     */     private CompletableFuture<DataFetcher.ComputationResult<T>> pendingTask;
/*     */     
/*     */     private DataFetcher.SuccessfulComputationResult<T> lastResult;
/*     */     
/*  57 */     private long nextUpdate = -1L;
/*     */     
/*     */     private Task(String id, Callable<T> updater, long period, RepeatedDelayStrategy repeatStrategy) {
/*  60 */       this.id = id;
/*  61 */       this.updater = updater;
/*  62 */       this.period = period;
/*  63 */       this.repeatStrategy = repeatStrategy;
/*     */     }
/*     */     
/*     */     private void updateIfNeeded(long currentTime) {
/*  67 */       if (this.pendingTask != null) {
/*  68 */         DataFetcher.ComputationResult<T> result = this.pendingTask.getNow(null);
/*  69 */         if (result == null)
/*     */           return; 
/*  73 */         this.pendingTask = null;
/*  74 */         long completionTime = result.time;
/*  75 */         result.value()
/*  76 */           .ifLeft(value -> {
/*     */               this.lastResult = new DataFetcher.SuccessfulComputationResult<>((T)value, completionTime);
/*     */               this.nextUpdate = completionTime + this.period * this.repeatStrategy.delayCyclesAfterSuccess();
/*  80 */             }).ifRight(e -> {
/*     */               long cycles = this.repeatStrategy.delayCyclesAfterFailure();
/*     */               DataFetcher.LOGGER.warn("Failed to process task {}, will repeat after {} cycles", new Object[] { this.id, Long.valueOf(cycles), e });
/*     */               this.nextUpdate = completionTime + this.period * cycles;
/*     */             });
/*     */       } 
/*  87 */       if (this.nextUpdate <= currentTime)
/*  88 */         this.pendingTask = CompletableFuture.supplyAsync(() -> {
/*     */               try {
/*     */                 T result = this.updater.call();
/*     */                 long completionTime = DataFetcher.this.timeSource.get(DataFetcher.this.resolution);
/*     */                 return new DataFetcher.ComputationResult(Either.left(result), completionTime);
/*  93 */               } catch (Exception e) {
/*     */                 long completionTime = DataFetcher.this.timeSource.get(DataFetcher.this.resolution);
/*     */                 return new DataFetcher.ComputationResult(Either.right(e), completionTime);
/*     */               } 
/*     */             }DataFetcher.this.executor); 
/*     */     }
/*     */     
/*     */     public void reset() {
/* 102 */       this.pendingTask = null;
/* 103 */       this.lastResult = null;
/* 104 */       this.nextUpdate = -1L;
/*     */     }
/*     */   }
/*     */   
/*     */   private class SubscribedTask<T> {
/*     */     private final DataFetcher.Task<T> task;
/*     */     
/*     */     private final Consumer<T> output;
/*     */     
/* 111 */     private long lastCheckTime = -1L;
/*     */     
/*     */     private SubscribedTask(DataFetcher this$0, DataFetcher.Task<T> task, Consumer<T> output) {
/* 114 */       this.task = task;
/* 115 */       this.output = output;
/*     */     }
/*     */     
/*     */     private void update(long currentTime) {
/* 119 */       this.task.updateIfNeeded(currentTime);
/* 120 */       runCallbackIfNeeded();
/*     */     }
/*     */     
/*     */     private void runCallbackIfNeeded() {
/* 124 */       DataFetcher.SuccessfulComputationResult<T> lastResult = this.task.lastResult;
/* 125 */       if (lastResult != null && this.lastCheckTime < lastResult.time) {
/* 126 */         this.output.accept(lastResult.value);
/* 127 */         this.lastCheckTime = lastResult.time;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void runCallback() {
/* 132 */       DataFetcher.SuccessfulComputationResult<T> lastResult = this.task.lastResult;
/* 133 */       if (lastResult != null) {
/* 134 */         this.output.accept(lastResult.value);
/* 135 */         this.lastCheckTime = lastResult.time;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void reset() {
/* 140 */       this.task.reset();
/* 141 */       this.lastCheckTime = -1L;
/*     */     }
/*     */   }
/*     */   
/*     */   public class Subscription {
/*     */     private final List<DataFetcher.SubscribedTask<?>> subscriptions;
/*     */     
/*     */     public Subscription() {
/* 146 */       this.subscriptions = new ArrayList<>();
/*     */     }
/*     */     
/*     */     public <T> void subscribe(DataFetcher.Task<T> task, Consumer<T> output) {
/* 149 */       DataFetcher.SubscribedTask<T> subscription = new DataFetcher.SubscribedTask<>(DataFetcher.this, task, output);
/* 150 */       this.subscriptions.add(subscription);
/* 151 */       subscription.runCallbackIfNeeded();
/*     */     }
/*     */     
/*     */     public void forceUpdate() {
/* 155 */       for (DataFetcher.SubscribedTask<?> subscription : this.subscriptions)
/* 156 */         subscription.runCallback(); 
/*     */     }
/*     */     
/*     */     public void tick() {
/* 161 */       for (DataFetcher.SubscribedTask<?> subscription : this.subscriptions)
/* 162 */         subscription.update(DataFetcher.this.timeSource.get(DataFetcher.this.resolution)); 
/*     */     }
/*     */     
/*     */     public void reset() {
/* 167 */       for (DataFetcher.SubscribedTask<?> subscription : this.subscriptions)
/* 168 */         subscription.reset(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\task\DataFetcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */