/*    */ package com.mojang.realmsclient.gui;
/*    */ 
/*    */ import com.mojang.realmsclient.dto.RealmsServer;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Comparator;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public class RealmsServerList implements Iterable<RealmsServer> {
/*    */   private final Minecraft minecraft;
/*    */   
/* 16 */   private final Set<RealmsServer> removedServers = new HashSet<>();
/*    */   
/* 17 */   private List<RealmsServer> servers = List.of();
/*    */   
/*    */   public RealmsServerList(Minecraft minecraft) {
/* 20 */     this.minecraft = minecraft;
/*    */   }
/*    */   
/*    */   public void updateServersList(List<RealmsServer> fetchedServers) {
/* 24 */     List<RealmsServer> sortedServers = new ArrayList<>(fetchedServers);
/* 25 */     sortedServers.sort((Comparator<? super RealmsServer>)new RealmsServer.McoServerComparator(this.minecraft.getUser().getName()));
/* 27 */     boolean removedAnyServers = sortedServers.removeAll(this.removedServers);
/* 28 */     if (!removedAnyServers)
/* 30 */       this.removedServers.clear(); 
/* 33 */     this.servers = sortedServers;
/*    */   }
/*    */   
/*    */   public void removeItem(RealmsServer server) {
/* 37 */     this.servers.remove(server);
/* 38 */     this.removedServers.add(server);
/*    */   }
/*    */   
/*    */   public Iterator<RealmsServer> iterator() {
/* 43 */     return this.servers.iterator();
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 47 */     return this.servers.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\RealmsServerList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */