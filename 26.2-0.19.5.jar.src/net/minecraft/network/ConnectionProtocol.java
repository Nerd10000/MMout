/*    */ package net.minecraft.network;
/*    */ 
/*    */ public enum ConnectionProtocol {
/*  4 */   HANDSHAKING("handshake"),
/*  5 */   PLAY("play"),
/*  6 */   STATUS("status"),
/*  7 */   LOGIN("login"),
/*  8 */   CONFIGURATION("configuration");
/*    */   
/*    */   private final String id;
/*    */   
/*    */   ConnectionProtocol(String id) {
/* 14 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String id() {
/* 18 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\ConnectionProtocol.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */