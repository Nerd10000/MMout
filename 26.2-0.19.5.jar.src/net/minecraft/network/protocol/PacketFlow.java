/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ public enum PacketFlow {
/*  4 */   SERVERBOUND("serverbound"),
/*  5 */   CLIENTBOUND("clientbound");
/*    */   
/*    */   private final String id;
/*    */   
/*    */   PacketFlow(String id) {
/*  9 */     this.id = id;
/*    */   }
/*    */   
/*    */   public PacketFlow getOpposite() {
/* 13 */     return (this == CLIENTBOUND) ? SERVERBOUND : CLIENTBOUND;
/*    */   }
/*    */   
/*    */   public String id() {
/* 19 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\PacketFlow.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */