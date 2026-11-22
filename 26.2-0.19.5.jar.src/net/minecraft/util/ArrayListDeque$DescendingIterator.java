/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ 
/*     */ class DescendingIterator implements Iterator<T> {
/*     */   private int index;
/*     */   
/*     */   public DescendingIterator() {
/* 294 */     this.index = paramArrayListDeque.size() - 1;
/*     */   }
/*     */   
/*     */   public boolean hasNext() {
/* 299 */     return (this.index >= 0);
/*     */   }
/*     */   
/*     */   public T next() {
/* 304 */     return ArrayListDeque.this.get(this.index--);
/*     */   }
/*     */   
/*     */   public void remove() {
/* 309 */     ArrayListDeque.this.remove(this.index + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ArrayListDeque$DescendingIterator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */