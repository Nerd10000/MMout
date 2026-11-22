/*    */ package net.minecraft.client.searchtree;
/*    */ 
/*    */ import com.google.common.collect.AbstractIterator;
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.PeekingIterator;
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class IntersectionIterator<T> extends AbstractIterator<T> {
/*    */   private final PeekingIterator<T> firstIterator;
/*    */   
/*    */   private final PeekingIterator<T> secondIterator;
/*    */   
/*    */   private final Comparator<T> comparator;
/*    */   
/*    */   public IntersectionIterator(Iterator<T> firstIterator, Iterator<T> secondIterator, Comparator<T> comparator) {
/* 17 */     this.firstIterator = Iterators.peekingIterator(firstIterator);
/* 18 */     this.secondIterator = Iterators.peekingIterator(secondIterator);
/* 20 */     this.comparator = comparator;
/*    */   }
/*    */   
/*    */   protected T computeNext() {
/*    */     while (true) {
/* 26 */       if (!this.firstIterator.hasNext() || !this.secondIterator.hasNext())
/* 27 */         return (T)endOfData(); 
/* 30 */       int compare = this.comparator.compare((T)this.firstIterator.peek(), (T)this.secondIterator.peek());
/* 31 */       if (compare == 0) {
/* 32 */         this.secondIterator.next();
/* 33 */         return (T)this.firstIterator.next();
/*    */       } 
/* 36 */       if (compare < 0) {
/* 37 */         this.firstIterator.next();
/*    */         continue;
/*    */       } 
/* 39 */       this.secondIterator.next();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\searchtree\IntersectionIterator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */