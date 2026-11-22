/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class GameTestAssertException extends GameTestException {
/*    */   protected final Component message;
/*    */   
/*    */   protected final int tick;
/*    */   
/*    */   public GameTestAssertException(Component message, int tick) {
/* 10 */     super(message.getString());
/* 11 */     this.message = message;
/* 12 */     this.tick = tick;
/*    */   }
/*    */   
/*    */   public Component getDescription() {
/* 17 */     return (Component)Component.translatable("test.error.tick", new Object[] { this.message, Integer.valueOf(this.tick) });
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 22 */     return getDescription().getString();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestAssertException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */