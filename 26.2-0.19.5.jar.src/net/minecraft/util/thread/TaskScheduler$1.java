/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import java.util.concurrent.Executor;
/*    */ 
/*    */ class null implements TaskScheduler<Runnable> {
/*    */   public String name() {
/* 29 */     return name;
/*    */   }
/*    */   
/*    */   public void schedule(Runnable runnable) {
/* 34 */     executor.execute(runnable);
/*    */   }
/*    */   
/*    */   public Runnable wrapRunnable(Runnable runnable) {
/* 39 */     return runnable;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 44 */     return name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\thread\TaskScheduler$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */