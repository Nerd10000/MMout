/*   */ package net.minecraft.network.chat;
/*   */ 
/*   */ import net.minecraft.server.level.ServerPlayer;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface ChatDecorator {
/*   */   public static final ChatDecorator PLAIN;
/*   */   
/*   */   Component decorate(ServerPlayer paramServerPlayer, Component paramComponent);
/*   */   
/*   */   static {
/* 8 */     PLAIN = ((player, plain) -> plain);
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ChatDecorator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */