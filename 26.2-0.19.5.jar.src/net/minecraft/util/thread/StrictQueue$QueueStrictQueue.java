/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import java.util.Queue;
/*    */ 
/*    */ public final class QueueStrictQueue implements StrictQueue<Runnable> {
/*    */   private final Queue<Runnable> queue;
/*    */   
/*    */   public QueueStrictQueue(Queue<Runnable> queue) {
/* 23 */     this.queue = queue;
/*    */   }
/*    */   
/*    */   public Runnable pop() {
/* 28 */     return this.queue.poll();
/*    */   }
/*    */   
/*    */   public boolean push(Runnable t) {
/* 33 */     return this.queue.add(t);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 38 */     return this.queue.isEmpty();
/*    */   }
/*    */   
/*    */   public int size() {
/* 43 */     return this.queue.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\thread\StrictQueue$QueueStrictQueue.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */