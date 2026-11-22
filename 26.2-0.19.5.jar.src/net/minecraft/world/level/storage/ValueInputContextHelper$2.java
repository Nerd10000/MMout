/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ class null implements ValueInput.TypedInputList<Object> {
/*    */   null(ValueInputContextHelper this$0) {}
/*    */   
/*    */   public boolean isEmpty() {
/* 38 */     return true;
/*    */   }
/*    */   
/*    */   public Stream<Object> stream() {
/* 43 */     return Stream.empty();
/*    */   }
/*    */   
/*    */   public Iterator<Object> iterator() {
/* 48 */     return Collections.emptyIterator();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\ValueInputContextHelper$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */