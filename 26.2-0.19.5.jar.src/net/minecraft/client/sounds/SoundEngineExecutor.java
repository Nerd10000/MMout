/*    */ package net.minecraft.client.sounds;
/*    */ 
/*    */ import java.util.concurrent.locks.LockSupport;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.thread.BlockableEventLoop;
/*    */ 
/*    */ public class SoundEngineExecutor extends BlockableEventLoop<Runnable> {
/*    */   private Thread thread;
/*    */   
/*    */   private volatile boolean shutdown;
/*    */   
/*    */   public SoundEngineExecutor() {
/* 15 */     super("Sound executor", false);
/* 16 */     this.thread = createThread();
/*    */   }
/*    */   
/*    */   private Thread createThread() {
/* 20 */     Thread thread = new Thread(this::run, "Sound engine");
/* 21 */     thread.setDaemon(true);
/* 22 */     thread.setUncaughtExceptionHandler((t, e) -> Minecraft.getInstance().delayCrash(CrashReport.forThrowable(e, "Uncaught exception on thread: " + t.getName())));
/* 23 */     thread.start();
/* 24 */     return thread;
/*    */   }
/*    */   
/*    */   public Runnable wrapRunnable(Runnable runnable) {
/* 29 */     return runnable;
/*    */   }
/*    */   
/*    */   public void schedule(Runnable runnable) {
/* 34 */     if (!this.shutdown)
/* 35 */       super.schedule(runnable); 
/*    */   }
/*    */   
/*    */   protected boolean shouldRun(Runnable task) {
/* 41 */     return !this.shutdown;
/*    */   }
/*    */   
/*    */   protected Thread getRunningThread() {
/* 46 */     return this.thread;
/*    */   }
/*    */   
/*    */   private void run() {
/* 50 */     while (!this.shutdown)
/* 51 */       managedBlock(() -> this.shutdown); 
/*    */   }
/*    */   
/*    */   protected void waitForTasks() {
/* 58 */     LockSupport.park("waiting for tasks");
/*    */   }
/*    */   
/*    */   public void shutDown() {
/* 62 */     this.shutdown = true;
/* 63 */     dropAllTasks();
/* 64 */     this.thread.interrupt();
/*    */     try {
/* 66 */       this.thread.join();
/* 67 */     } catch (InterruptedException e) {
/* 68 */       Thread.currentThread().interrupt();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void startUp() {
/* 73 */     this.shutdown = false;
/* 74 */     this.thread = createThread();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\SoundEngineExecutor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */