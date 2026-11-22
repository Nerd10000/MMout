/*    */ package net.minecraft.util;
/*    */ 
/*    */ public enum Continuation {
/*  8 */   CONTINUE, ABORT;
/*    */   
/*    */   public boolean shouldAbort() {
/* 12 */     return (this == ABORT);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\AbortableIterationConsumer$Continuation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */