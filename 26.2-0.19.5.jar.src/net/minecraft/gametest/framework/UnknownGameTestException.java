/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class UnknownGameTestException extends GameTestException {
/*    */   private final Throwable reason;
/*    */   
/*    */   public UnknownGameTestException(Throwable reason) {
/*  9 */     super(reason.getMessage());
/* 10 */     this.reason = reason;
/*    */   }
/*    */   
/*    */   public Component getDescription() {
/* 15 */     return (Component)Component.translatable("test.error.unknown", new Object[] { this.reason.getMessage() });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\UnknownGameTestException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */