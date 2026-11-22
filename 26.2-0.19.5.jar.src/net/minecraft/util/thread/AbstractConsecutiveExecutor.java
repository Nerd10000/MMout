/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*     */ import net.minecraft.util.profiling.metrics.MetricsRegistry;
/*     */ import net.minecraft.util.profiling.metrics.ProfilerMeasured;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class AbstractConsecutiveExecutor<T extends Runnable> implements Runnable, TaskScheduler<T>, ProfilerMeasured {
/*  18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  19 */   private final AtomicReference<Status> status = new AtomicReference<>(Status.SLEEPING);
/*     */   
/*     */   private final StrictQueue<T> queue;
/*     */   
/*     */   private final Executor executor;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   public AbstractConsecutiveExecutor(StrictQueue<T> queue, Executor executor, String name) {
/*  25 */     this.executor = executor;
/*  26 */     this.queue = queue;
/*  27 */     this.name = name;
/*  28 */     MetricsRegistry.INSTANCE.add(this);
/*     */   }
/*     */   
/*     */   private boolean canBeScheduled() {
/*  32 */     return (!isClosed() && !this.queue.isEmpty());
/*     */   }
/*     */   
/*     */   public void close() {
/*  37 */     this.status.set(Status.CLOSED);
/*     */   }
/*     */   
/*     */   private boolean pollTask() {
/*  41 */     if (!isRunning())
/*  42 */       return false; 
/*  45 */     Runnable runnable = this.queue.pop();
/*  46 */     if (runnable == null)
/*  47 */       return false; 
/*  50 */     Util.runNamed(runnable, this.name);
/*  52 */     return true;
/*     */   }
/*     */   
/*     */   public void run() {
/*     */     try {
/*  60 */       pollTask();
/*     */     } finally {
/*  62 */       setSleeping();
/*  63 */       registerForExecution();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void runAll() {
/*     */     try {
/*  72 */       while (pollTask());
/*     */     } finally {
/*  75 */       setSleeping();
/*  76 */       registerForExecution();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void schedule(T task) {
/*  82 */     this.queue.push(task);
/*  83 */     registerForExecution();
/*     */   }
/*     */   
/*     */   private void registerForExecution() {
/*  87 */     if (canBeScheduled() && 
/*  88 */       setRunning())
/*     */       try {
/*  90 */         this.executor.execute(this);
/*  91 */       } catch (RejectedExecutionException e) {
/*     */         try {
/*  94 */           this.executor.execute(this);
/*  95 */         } catch (RejectedExecutionException e2) {
/*  96 */           LOGGER.error("Could not schedule ConsecutiveExecutor", e2);
/*     */         } 
/*     */       }  
/*     */   }
/*     */   
/*     */   public int size() {
/* 104 */     return this.queue.size();
/*     */   }
/*     */   
/*     */   public boolean hasWork() {
/* 108 */     return (isRunning() && !this.queue.isEmpty());
/*     */   }
/*     */   
/*     */   public String toString() {
/* 113 */     return this.name + " " + this.name + " " + String.valueOf(this.status.get());
/*     */   }
/*     */   
/*     */   public String name() {
/* 118 */     return this.name;
/*     */   }
/*     */   
/*     */   public List<MetricSampler> profiledMetrics() {
/* 123 */     return (List<MetricSampler>)ImmutableList.of(
/* 124 */         MetricSampler.create(this.name + "-queue-size", MetricCategory.CONSECUTIVE_EXECUTORS, this::size));
/*     */   }
/*     */   
/*     */   private enum Status {
/* 129 */     SLEEPING, RUNNING, CLOSED;
/*     */   }
/*     */   
/*     */   private boolean setRunning() {
/* 135 */     return this.status.compareAndSet(Status.SLEEPING, Status.RUNNING);
/*     */   }
/*     */   
/*     */   private void setSleeping() {
/* 139 */     this.status.compareAndSet(Status.RUNNING, Status.SLEEPING);
/*     */   }
/*     */   
/*     */   private boolean isRunning() {
/* 143 */     return (this.status.get() == Status.RUNNING);
/*     */   }
/*     */   
/*     */   private boolean isClosed() {
/* 147 */     return (this.status.get() == Status.CLOSED);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\thread\AbstractConsecutiveExecutor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */