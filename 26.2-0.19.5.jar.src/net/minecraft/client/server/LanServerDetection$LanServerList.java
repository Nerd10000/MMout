/*    */ package net.minecraft.client.server;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.net.InetAddress;
/*    */ import java.util.List;
/*    */ 
/*    */ public class LanServerList {
/* 23 */   private final List<LanServer> servers = Lists.newArrayList();
/*    */   
/*    */   private boolean isDirty;
/*    */   
/*    */   public synchronized List<LanServer> takeDirtyServers() {
/* 27 */     if (this.isDirty) {
/* 28 */       List<LanServer> newServers = List.copyOf(this.servers);
/* 29 */       this.isDirty = false;
/* 30 */       return newServers;
/*    */     } 
/* 32 */     return null;
/*    */   }
/*    */   
/*    */   public synchronized void addServer(String pingData, InetAddress socketAddress) {
/* 36 */     String motd = LanServerPinger.parseMotd(pingData);
/* 37 */     String address = LanServerPinger.parseAddress(pingData);
/* 38 */     if (address == null)
/*    */       return; 
/* 42 */     address = socketAddress.getHostAddress() + ":" + socketAddress.getHostAddress();
/* 44 */     boolean found = false;
/* 45 */     for (LanServer server : this.servers) {
/* 46 */       if (server.getAddress().equals(address)) {
/* 47 */         server.updatePingTime();
/* 48 */         found = true;
/*    */         break;
/*    */       } 
/*    */     } 
/* 53 */     if (!found) {
/* 54 */       this.servers.add(new LanServer(motd, address));
/* 55 */       this.isDirty = true;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\server\LanServerDetection$LanServerList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */