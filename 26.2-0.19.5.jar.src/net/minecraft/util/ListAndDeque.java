/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Deque;
/*    */ import java.util.List;
/*    */ import java.util.RandomAccess;
/*    */ import java.util.SequencedCollection;
/*    */ 
/*    */ public interface ListAndDeque<T> extends List<T>, RandomAccess, Cloneable, Serializable, Deque<T> {
/*    */   default boolean offer(T value) {
/* 34 */     return offerLast(value);
/*    */   }
/*    */   
/*    */   default T remove() {
/* 39 */     return removeFirst();
/*    */   }
/*    */   
/*    */   default T poll() {
/* 44 */     return pollFirst();
/*    */   }
/*    */   
/*    */   default T element() {
/* 49 */     return getFirst();
/*    */   }
/*    */   
/*    */   default T peek() {
/* 54 */     return peekFirst();
/*    */   }
/*    */   
/*    */   default void push(T value) {
/* 59 */     addFirst(value);
/*    */   }
/*    */   
/*    */   default T pop() {
/* 64 */     return removeFirst();
/*    */   }
/*    */   
/*    */   ListAndDeque<T> reversed();
/*    */   
/*    */   T getFirst();
/*    */   
/*    */   T getLast();
/*    */   
/*    */   void addFirst(T paramT);
/*    */   
/*    */   void addLast(T paramT);
/*    */   
/*    */   T removeFirst();
/*    */   
/*    */   T removeLast();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ListAndDeque.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */