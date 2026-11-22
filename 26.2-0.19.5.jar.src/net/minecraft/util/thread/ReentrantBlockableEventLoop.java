/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ public abstract class ReentrantBlockableEventLoop<R extends Runnable> extends BlockableEventLoop<R> {
/*    */   private int reentrantCount;
/*    */   
/*    */   public ReentrantBlockableEventLoop(String name, boolean propagatesCrashes) {
/*  7 */     super(name, propagatesCrashes);
/*    */   }
/*    */   
/*    */   protected boolean scheduleExecutables() {
/* 12 */     return (runningTask() || super.scheduleExecutables());
/*    */   }
/*    */   
/*    */   protected boolean runningTask() {
/* 16 */     return (this.reentrantCount != 0);
/*    */   }
/*    */   
/*    */   protected void doRunTask(R task) {
/* 21 */     this.reentrantCount++;
/*    */     try {
/* 23 */       super.doRunTask(task);
/*    */     } finally {
/* 25 */       this.reentrantCount--;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\thread\ReentrantBlockableEventLoop.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */