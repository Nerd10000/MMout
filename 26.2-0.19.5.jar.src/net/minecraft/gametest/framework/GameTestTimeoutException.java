/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class GameTestTimeoutException extends GameTestException {
/*    */   protected final Component message;
/*    */   
/*    */   public GameTestTimeoutException(Component message) {
/*  9 */     super(message.getString());
/* 10 */     this.message = message;
/*    */   }
/*    */   
/*    */   public Component getDescription() {
/* 15 */     return this.message;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestTimeoutException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */