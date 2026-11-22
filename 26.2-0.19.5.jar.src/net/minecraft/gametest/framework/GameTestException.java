/*   */ package net.minecraft.gametest.framework;
/*   */ 
/*   */ import net.minecraft.network.chat.Component;
/*   */ 
/*   */ public abstract class GameTestException extends RuntimeException {
/*   */   public GameTestException(String message) {
/* 7 */     super(message);
/*   */   }
/*   */   
/*   */   public abstract Component getDescription();
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */