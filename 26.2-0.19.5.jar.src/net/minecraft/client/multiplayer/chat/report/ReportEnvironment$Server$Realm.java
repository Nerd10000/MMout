/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ 
/*    */ import com.mojang.realmsclient.dto.RealmsServer;
/*    */ 
/*    */ public final class Realm extends Record implements ReportEnvironment.Server {
/*    */   private final long realmId;
/*    */   
/*    */   private final int slotId;
/*    */   
/*    */   public int slotId() {
/* 61 */     return this.slotId;
/*    */   }
/*    */   
/*    */   public long realmId() {
/* 61 */     return this.realmId;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;
/*    */   }
/*    */   
/*    */   public Realm(long realmId, int slotId) {
/* 61 */     this.realmId = realmId;
/* 61 */     this.slotId = slotId;
/*    */   }
/*    */   
/*    */   public Realm(RealmsServer realm) {
/* 63 */     this(realm.id, realm.activeSlot);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\report\ReportEnvironment$Server$Realm.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */