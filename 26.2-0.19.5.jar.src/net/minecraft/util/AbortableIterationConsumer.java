/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface AbortableIterationConsumer<T> {
/*    */   Continuation accept(T paramT);
/*    */   
/*    */   public enum Continuation {
/*  8 */     CONTINUE, ABORT;
/*    */     
/*    */     public boolean shouldAbort() {
/* 12 */       return (this == ABORT);
/*    */     }
/*    */   }
/*    */   
/*    */   static <T> AbortableIterationConsumer<T> forConsumer(Consumer<T> consumer) {
/* 22 */     return e -> {
/*    */         consumer.accept(e);
/*    */         return Continuation.CONTINUE;
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\AbortableIterationConsumer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */