/*     */ package net.minecraft.client;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ class null implements Iterator<T> {
/*     */   private int i;
/*     */   
/*     */   public boolean hasNext() {
/* 137 */     return (this.i < RotatingSectionStorage.this.nodes.length - 1);
/*     */   }
/*     */   
/*     */   public T next() {
/* 142 */     if (this.i >= RotatingSectionStorage.this.nodes.length)
/* 143 */       throw new NoSuchElementException(); 
/* 145 */     return (RotatingSectionStorage.this.nodes[this.i++]).value;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\RotatingSectionStorage$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */