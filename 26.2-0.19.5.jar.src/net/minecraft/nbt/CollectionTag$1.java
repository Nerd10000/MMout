/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ 
/*    */ class null implements Iterator<Tag> {
/*    */   private int index;
/*    */   
/*    */   public boolean hasNext() {
/* 32 */     return (this.index < CollectionTag.this.size());
/*    */   }
/*    */   
/*    */   public Tag next() {
/* 37 */     if (!hasNext())
/* 38 */       throw new NoSuchElementException(); 
/* 40 */     return CollectionTag.this.get(this.index++);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\CollectionTag$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */