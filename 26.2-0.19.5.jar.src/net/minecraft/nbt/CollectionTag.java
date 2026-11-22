/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ import java.util.stream.Stream;
/*    */ import java.util.stream.StreamSupport;
/*    */ 
/*    */ public interface CollectionTag extends Tag, Iterable<Tag> {
/*    */   void clear();
/*    */   
/*    */   boolean setTag(int paramInt, Tag paramTag);
/*    */   
/*    */   boolean addTag(int paramInt, Tag paramTag);
/*    */   
/*    */   Tag remove(int paramInt);
/*    */   
/*    */   Tag get(int paramInt);
/*    */   
/*    */   int size();
/*    */   
/*    */   default boolean isEmpty() {
/* 22 */     return (size() == 0);
/*    */   }
/*    */   
/*    */   default Iterator<Tag> iterator() {
/* 27 */     return new Iterator<Tag>() {
/*    */         private int index;
/*    */         
/*    */         public boolean hasNext() {
/* 32 */           return (this.index < CollectionTag.this.size());
/*    */         }
/*    */         
/*    */         public Tag next() {
/* 37 */           if (!hasNext())
/* 38 */             throw new NoSuchElementException(); 
/* 40 */           return CollectionTag.this.get(this.index++);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   default Stream<Tag> stream() {
/* 46 */     return StreamSupport.stream(spliterator(), false);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\CollectionTag.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */