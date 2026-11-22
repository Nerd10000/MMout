/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import com.google.common.collect.Queues;
/*    */ import java.util.Locale;
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ 
/*    */ public final class FixedPriorityQueue implements StrictQueue<StrictQueue.RunnableWithPriority> {
/*    */   private final Queue<Runnable>[] queues;
/*    */   
/* 56 */   private final AtomicInteger size = new AtomicInteger();
/*    */   
/*    */   public FixedPriorityQueue(int size) {
/* 60 */     this.queues = (Queue<Runnable>[])new Queue[size];
/* 61 */     for (int i = 0; i < size; i++)
/* 62 */       this.queues[i] = Queues.newConcurrentLinkedQueue(); 
/*    */   }
/*    */   
/*    */   public Runnable pop() {
/* 69 */     for (Queue<Runnable> queue : this.queues) {
/* 70 */       Runnable task = queue.poll();
/* 71 */       if (task != null) {
/* 72 */         this.size.decrementAndGet();
/* 73 */         return task;
/*    */       } 
/*    */     } 
/* 76 */     return null;
/*    */   }
/*    */   
/*    */   public boolean push(StrictQueue.RunnableWithPriority task) {
/* 81 */     int priority = task.priority;
/* 83 */     if (priority >= this.queues.length || priority < 0)
/* 84 */       throw new IndexOutOfBoundsException(String.format(Locale.ROOT, "Priority %d not supported. Expected range [0-%d]", new Object[] { Integer.valueOf(priority), Integer.valueOf(this.queues.length - 1) })); 
/* 87 */     this.queues[priority].add(task);
/* 88 */     this.size.incrementAndGet();
/* 89 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 94 */     return (this.size.get() == 0);
/*    */   }
/*    */   
/*    */   public int size() {
/* 99 */     return this.size.get();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\thread\StrictQueue$FixedPriorityQueue.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */