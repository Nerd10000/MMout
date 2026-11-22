/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import com.mojang.jtracy.Zone;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.locks.LockSupport;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*     */ import net.minecraft.util.profiling.metrics.MetricsRegistry;
/*     */ import net.minecraft.util.profiling.metrics.ProfilerMeasured;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class BlockableEventLoop<R extends Runnable> implements Executor, TaskScheduler<R>, ProfilerMeasured {
/*     */   public static final long BLOCK_TIME_NANOS = 100000L;
/*     */   
/*     */   private static volatile Supplier<CrashReport> delayedCrash;
/*     */   
/*     */   private final boolean propagatesCrashes;
/*     */   
/*     */   private final String name;
/*     */   
/*  33 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  35 */   private final Queue<R> pendingRunnables = Queues.newConcurrentLinkedQueue();
/*     */   
/*     */   private int blockingCount;
/*     */   
/*     */   protected BlockableEventLoop(String name, boolean propagatesCrashes) {
/*  39 */     this.propagatesCrashes = propagatesCrashes;
/*  40 */     this.name = name;
/*  41 */     MetricsRegistry.INSTANCE.add(this);
/*     */   }
/*     */   
/*     */   public boolean isSameThread() {
/*  47 */     return (Thread.currentThread() == getRunningThread());
/*     */   }
/*     */   
/*     */   protected boolean scheduleExecutables() {
/*  53 */     return !isSameThread();
/*     */   }
/*     */   
/*     */   public int getPendingTasksCount() {
/*  57 */     return this.pendingRunnables.size();
/*     */   }
/*     */   
/*     */   public String name() {
/*  62 */     return this.name;
/*     */   }
/*     */   
/*     */   public <V> CompletableFuture<V> submit(Supplier<V> supplier) {
/*  66 */     if (scheduleExecutables())
/*  67 */       return CompletableFuture.supplyAsync(supplier, this); 
/*  69 */     return CompletableFuture.completedFuture(supplier.get());
/*     */   }
/*     */   
/*     */   private CompletableFuture<Void> submitAsync(Runnable runnable) {
/*  74 */     return CompletableFuture.supplyAsync(() -> {
/*     */           runnable.run();
/*     */           return null;
/*  74 */         }this);
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public CompletableFuture<Void> submit(Runnable runnable) {
/*  86 */     if (scheduleExecutables())
/*  87 */       return submitAsync(runnable); 
/*  89 */     runnable.run();
/*  90 */     return CompletableFuture.completedFuture(null);
/*     */   }
/*     */   
/*     */   public void executeBlocking(Runnable runnable) {
/*  95 */     if (!isSameThread()) {
/*  96 */       submitAsync(runnable).join();
/*     */     } else {
/*  98 */       runnable.run();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void schedule(R r) {
/* 104 */     this.pendingRunnables.add(r);
/* 105 */     LockSupport.unpark(getRunningThread());
/*     */   }
/*     */   
/*     */   public void execute(Runnable command) {
/* 110 */     R task = wrapRunnable(command);
/* 111 */     if (scheduleExecutables()) {
/* 112 */       schedule(task);
/*     */     } else {
/* 114 */       doRunTask(task);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void executeIfPossible(Runnable command) {
/* 119 */     execute(command);
/*     */   }
/*     */   
/*     */   protected void dropAllTasks() {
/* 123 */     this.pendingRunnables.clear();
/*     */   }
/*     */   
/*     */   protected void runAllTasks() {
/* 127 */     while (pollTask());
/*     */   }
/*     */   
/*     */   protected boolean shouldRunAllTasks() {
/* 133 */     return (this.blockingCount > 0);
/*     */   }
/*     */   
/*     */   protected boolean pollTask() {
/* 137 */     throwDelayedException();
/* 138 */     Runnable runnable = (Runnable)this.pendingRunnables.peek();
/* 139 */     if (runnable == null)
/* 140 */       return false; 
/* 143 */     if (!shouldRunAllTasks() && !shouldRun((R)runnable))
/* 144 */       return false; 
/* 147 */     doRunTask(this.pendingRunnables.remove());
/* 149 */     return true;
/*     */   }
/*     */   
/*     */   public void managedBlock(BooleanSupplier condition) {
/* 153 */     this.blockingCount++;
/*     */     try {
/* 155 */       while (!condition.getAsBoolean()) {
/* 156 */         if (!pollTask())
/* 158 */           waitForTasks(); 
/*     */       } 
/*     */     } finally {
/* 162 */       this.blockingCount--;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void waitForTasks() {
/* 167 */     Thread.yield();
/* 168 */     LockSupport.parkNanos("waiting for tasks", 100000L);
/*     */   }
/*     */   
/*     */   protected void doRunTask(R task) {
/*     */     try {
/* 172 */       Zone ignored = TracyClient.beginZone("Task", SharedConstants.IS_RUNNING_IN_IDE);
/*     */       try {
/* 173 */         task.run();
/* 174 */         if (ignored != null)
/* 174 */           ignored.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (ignored != null)
/*     */           try {
/*     */             ignored.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 174 */     } catch (Exception e) {
/* 175 */       LOGGER.error(LogUtils.FATAL_MARKER, "Error executing task on {}", name(), e);
/* 176 */       if (isNonRecoverable(e))
/* 177 */         throw e; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<MetricSampler> profiledMetrics() {
/* 184 */     return (List<MetricSampler>)ImmutableList.of(
/* 185 */         MetricSampler.create(this.name + "-pending-tasks", MetricCategory.EVENT_LOOPS, this::getPendingTasksCount));
/*     */   }
/*     */   
/*     */   public static boolean isNonRecoverable(Throwable t) {
/* 190 */     if (t instanceof ReportedException) {
/* 190 */       ReportedException r = (ReportedException)t;
/* 191 */       return isNonRecoverable(r.getCause());
/*     */     } 
/* 196 */     return (t instanceof OutOfMemoryError || t instanceof StackOverflowError);
/*     */   }
/*     */   
/*     */   private void throwDelayedException() {
/* 200 */     if (this.propagatesCrashes) {
/* 201 */       Supplier<CrashReport> delayedCrash = BlockableEventLoop.delayedCrash;
/* 202 */       if (delayedCrash != null)
/* 203 */         throw new ReportedException((CrashReport)delayedCrash.get()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void delayCrash(CrashReport crashReport) {
/* 209 */     delayedCrash = (() -> crashReport);
/*     */   }
/*     */   
/*     */   public static synchronized void relayDelayCrash(CrashReport crashReport) {
/* 213 */     Supplier<CrashReport> delayedCrash = BlockableEventLoop.delayedCrash;
/* 214 */     if (delayedCrash == null) {
/* 215 */       BlockableEventLoop.delayedCrash = (() -> crashReport);
/*     */     } else {
/* 217 */       ((CrashReport)delayedCrash.get()).getException().addSuppressed(crashReport.getException());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract boolean shouldRun(R paramR);
/*     */   
/*     */   protected abstract Thread getRunningThread();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\thread\BlockableEventLoop.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */