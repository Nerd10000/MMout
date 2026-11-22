/*    */ package net.minecraft.client.multiplayer.resolver;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ 
/*    */ class null implements ResolvedServerAddress {
/*    */   public String getHostName() {
/* 18 */     return address.getAddress().getHostName();
/*    */   }
/*    */   
/*    */   public String getHostIp() {
/* 23 */     return address.getAddress().getHostAddress();
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 28 */     return address.getPort();
/*    */   }
/*    */   
/*    */   public InetSocketAddress asInetSocketAddress() {
/* 33 */     return address;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\resolver\ResolvedServerAddress$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */