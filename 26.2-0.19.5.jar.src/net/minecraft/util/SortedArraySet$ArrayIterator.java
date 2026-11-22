/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ class ArrayIterator implements Iterator<T> {
/*     */   private int index;
/*     */   
/* 158 */   private int last = -1;
/*     */   
/*     */   public boolean hasNext() {
/* 162 */     return (this.index < SortedArraySet.this.size);
/*     */   }
/*     */   
/*     */   public T next() {
/* 167 */     if (this.index >= SortedArraySet.this.size)
/* 168 */       throw new NoSuchElementException(); 
/* 170 */     this.last = this.index++;
/* 171 */     return SortedArraySet.this.contents[this.last];
/*     */   }
/*     */   
/*     */   public void remove() {
/* 176 */     if (this.last == -1)
/* 177 */       throw new IllegalStateException(); 
/* 179 */     SortedArraySet.this.removeInternal(this.last);
/* 180 */     this.index--;
/* 181 */     this.last = -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\SortedArraySet$ArrayIterator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */