/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ class null implements ValueInput.ValueInputList {
/*    */   null(ValueInputContextHelper this$0) {}
/*    */   
/*    */   public boolean isEmpty() {
/* 21 */     return true;
/*    */   }
/*    */   
/*    */   public Stream<ValueInput> stream() {
/* 26 */     return Stream.empty();
/*    */   }
/*    */   
/*    */   public Iterator<ValueInput> iterator() {
/* 31 */     return Collections.emptyIterator();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\ValueInputContextHelper$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */