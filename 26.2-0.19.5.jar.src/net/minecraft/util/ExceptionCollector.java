/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class ExceptionCollector<T extends Throwable> {
/*    */   private T result;
/*    */   
/*    */   public void add(T throwable) {
/*  9 */     if (this.result == null) {
/* 10 */       this.result = throwable;
/*    */     } else {
/* 12 */       this.result.addSuppressed((Throwable)throwable);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void throwIfPresent() throws T {
/* 17 */     if (this.result != null)
/* 18 */       throw this.result; 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ExceptionCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */