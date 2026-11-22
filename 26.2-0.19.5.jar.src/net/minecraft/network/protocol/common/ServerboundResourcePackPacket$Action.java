/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ public enum Action {
/* 39 */   SUCCESSFULLY_LOADED, DECLINED, FAILED_DOWNLOAD, ACCEPTED, DOWNLOADED, INVALID_URL, FAILED_RELOAD, DISCARDED;
/*    */   
/*    */   public boolean isTerminal() {
/* 50 */     return (this != ACCEPTED && this != DOWNLOADED);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\ServerboundResourcePackPacket$Action.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */