/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.AbstractIterator;
/*    */ import com.google.common.collect.Queues;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.util.Deque;
/*    */ 
/*    */ public final class SequencedPriorityIterator<T> extends AbstractIterator<T> {
/*    */   private static final int MIN_PRIO = -2147483648;
/*    */   
/* 19 */   private Deque<T> highestPrioQueue = null;
/*    */   
/* 20 */   private int highestPrio = Integer.MIN_VALUE;
/*    */   
/* 22 */   private final Int2ObjectMap<Deque<T>> queuesByPriority = (Int2ObjectMap<Deque<T>>)new Int2ObjectOpenHashMap();
/*    */   
/*    */   public void add(T data, int priority) {
/* 25 */     if (priority == this.highestPrio && this.highestPrioQueue != null) {
/* 26 */       this.highestPrioQueue.addLast(data);
/*    */       return;
/*    */     } 
/* 30 */     Deque<T> queue = (Deque<T>)this.queuesByPriority.computeIfAbsent(priority, order -> Queues.newArrayDeque());
/* 31 */     queue.addLast(data);
/* 33 */     if (priority >= this.highestPrio) {
/* 34 */       this.highestPrioQueue = queue;
/* 35 */       this.highestPrio = priority;
/*    */     } 
/*    */   }
/*    */   
/*    */   protected T computeNext() {
/* 41 */     if (this.highestPrioQueue == null)
/* 42 */       return (T)endOfData(); 
/* 45 */     T result = this.highestPrioQueue.removeFirst();
/* 47 */     if (result == null)
/* 48 */       return (T)endOfData(); 
/* 51 */     if (this.highestPrioQueue.isEmpty())
/* 52 */       switchCacheToNextHighestPrioQueue(); 
/* 55 */     return result;
/*    */   }
/*    */   
/*    */   private void switchCacheToNextHighestPrioQueue() {
/* 59 */     int foundHighestPrio = Integer.MIN_VALUE;
/* 60 */     Deque<T> foundHighestPrioQueue = null;
/* 62 */     for (ObjectIterator<Int2ObjectMap.Entry<Deque<T>>> objectIterator = Int2ObjectMaps.fastIterable(this.queuesByPriority).iterator(); objectIterator.hasNext(); ) {
/* 62 */       Int2ObjectMap.Entry<Deque<T>> entry = objectIterator.next();
/* 63 */       Deque<T> queue = (Deque<T>)entry.getValue();
/* 64 */       int prio = entry.getIntKey();
/* 66 */       if (prio > foundHighestPrio && !queue.isEmpty()) {
/* 67 */         foundHighestPrio = prio;
/* 68 */         foundHighestPrioQueue = queue;
/* 70 */         if (prio == this.highestPrio - 1)
/*    */           break; 
/*    */       } 
/*    */     } 
/* 76 */     this.highestPrio = foundHighestPrio;
/* 77 */     this.highestPrioQueue = foundHighestPrioQueue;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\SequencedPriorityIterator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */