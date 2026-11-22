/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public final class Disguised extends Record implements OutgoingChatMessage {
/*    */   private final Component content;
/*    */   
/*    */   public Disguised(Component content) {
/* 32 */     this.content = content;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/OutgoingChatMessage$Disguised;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/OutgoingChatMessage$Disguised;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/OutgoingChatMessage$Disguised;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/OutgoingChatMessage$Disguised;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/OutgoingChatMessage$Disguised;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/OutgoingChatMessage$Disguised;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component content() {
/* 35 */     return this.content;
/*    */   }
/*    */   
/*    */   public void sendToPlayer(ServerPlayer player, boolean filtered, ChatType.Bound chatType) {
/* 40 */     player.connection.sendDisguisedChatMessage(this.content, chatType);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\OutgoingChatMessage$Disguised.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */