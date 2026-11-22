/*    */ package net.minecraft.network.protocol.handshake;
/*    */ 
/*    */ public enum ClientIntent {
/*  5 */   STATUS, LOGIN, TRANSFER;
/*    */   
/*    */   private static final int STATUS_ID = 1;
/*    */   
/*    */   private static final int LOGIN_ID = 2;
/*    */   
/*    */   private static final int TRANSFER_ID = 3;
/*    */   
/*    */   public static ClientIntent byId(int id) {
/* 16 */     switch (id) {
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */       
/*    */     } 
/* 20 */     throw new IllegalArgumentException("Unknown connection intent: " + id);
/*    */   }
/*    */   
/*    */   public int id() {
/* 25 */     switch (ordinal()) {
/*    */       default:
/* 25 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */         break;
/*    */     } 
/* 25 */     return 
/*    */       
/* 28 */       3;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\handshake\ClientIntent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */