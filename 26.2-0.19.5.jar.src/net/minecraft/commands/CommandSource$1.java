/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ class null implements CommandSource {
/*    */   public void sendSystemMessage(Component message) {}
/*    */   
/*    */   public boolean acceptsSuccess() {
/* 13 */     return false;
/*    */   }
/*    */   
/*    */   public boolean acceptsFailure() {
/* 18 */     return false;
/*    */   }
/*    */   
/*    */   public boolean shouldInformAdmins() {
/* 23 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\CommandSource$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */