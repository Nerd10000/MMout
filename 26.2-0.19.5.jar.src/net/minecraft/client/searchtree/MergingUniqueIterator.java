/*    */ package net.minecraft.client.searchtree;
/*    */ 
/*    */ import com.google.common.collect.AbstractIterator;
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.PeekingIterator;
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class MergingUniqueIterator<T> extends AbstractIterator<T> {
/*    */   private final PeekingIterator<T> firstIterator;
/*    */   
/*    */   private final PeekingIterator<T> secondIterator;
/*    */   
/*    */   private final Comparator<T> comparator;
/*    */   
/*    */   public MergingUniqueIterator(Iterator<T> firstIterator, Iterator<T> secondIterator, Comparator<T> comparator) {
/* 18 */     this.firstIterator = Iterators.peekingIterator(firstIterator);
/* 19 */     this.secondIterator = Iterators.peekingIterator(secondIterator);
/* 21 */     this.comparator = comparator;
/*    */   }
/*    */   
/*    */   protected T computeNext() {
/* 26 */     boolean firstEmpty = !this.firstIterator.hasNext();
/* 27 */     boolean secondEmpty = !this.secondIterator.hasNext();
/* 28 */     if (firstEmpty && secondEmpty)
/* 29 */       return (T)endOfData(); 
/* 32 */     if (firstEmpty)
/* 33 */       return (T)this.secondIterator.next(); 
/* 35 */     if (secondEmpty)
/* 36 */       return (T)this.firstIterator.next(); 
/* 39 */     int compare = this.comparator.compare((T)this.firstIterator.peek(), (T)this.secondIterator.peek());
/* 40 */     if (compare == 0)
/* 41 */       this.secondIterator.next(); 
/* 44 */     return (compare <= 0) ? (T)this.firstIterator.next() : (T)this.secondIterator.next();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\searchtree\MergingUniqueIterator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */