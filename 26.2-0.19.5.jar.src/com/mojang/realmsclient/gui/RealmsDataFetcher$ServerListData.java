/*    */ package com.mojang.realmsclient.gui;
/*    */ 
/*    */ import com.mojang.realmsclient.dto.RealmsServer;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class ServerListData extends Record {
/*    */   private final List<RealmsServer> serverList;
/*    */   
/*    */   private final List<RealmsServer> availableSnapshotServers;
/*    */   
/*    */   public ServerListData(List<RealmsServer> serverList, List<RealmsServer> availableSnapshotServers) {
/* 54 */     this.serverList = serverList;
/* 54 */     this.availableSnapshotServers = availableSnapshotServers;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/gui/RealmsDataFetcher$ServerListData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #54	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/RealmsDataFetcher$ServerListData;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/gui/RealmsDataFetcher$ServerListData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #54	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/RealmsDataFetcher$ServerListData;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/gui/RealmsDataFetcher$ServerListData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #54	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/gui/RealmsDataFetcher$ServerListData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<RealmsServer> serverList() {
/* 54 */     return this.serverList;
/*    */   }
/*    */   
/*    */   public List<RealmsServer> availableSnapshotServers() {
/* 54 */     return this.availableSnapshotServers;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\RealmsDataFetcher$ServerListData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */