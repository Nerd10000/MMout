/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
/*    */ import java.util.concurrent.Executor;
/*    */ 
/*    */ public class ConsecutiveExecutor extends AbstractConsecutiveExecutor<Runnable> {
/*    */   public ConsecutiveExecutor(Executor dispatcher, String name) {
/*  9 */     super(new StrictQueue.QueueStrictQueue(new ConcurrentLinkedQueue<>()), dispatcher, name);
/*    */   }
/*    */   
/*    */   public Runnable wrapRunnable(Runnable runnable) {
/* 14 */     return runnable;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\thread\ConsecutiveExecutor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */