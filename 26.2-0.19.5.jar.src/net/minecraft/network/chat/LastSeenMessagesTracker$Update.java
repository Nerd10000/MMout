/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ public final class Update extends Record {
/*    */   private final LastSeenMessages lastSeen;
/*    */   
/*    */   private final LastSeenMessages.Update update;
/*    */   
/*    */   public Update(LastSeenMessages lastSeen, LastSeenMessages.Update update) {
/* 78 */     this.lastSeen = lastSeen;
/* 78 */     this.update = update;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/LastSeenMessagesTracker$Update;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenMessagesTracker$Update;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/LastSeenMessagesTracker$Update;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenMessagesTracker$Update;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/LastSeenMessagesTracker$Update;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/LastSeenMessagesTracker$Update;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public LastSeenMessages lastSeen() {
/* 78 */     return this.lastSeen;
/*    */   }
/*    */   
/*    */   public LastSeenMessages.Update update() {
/* 78 */     return this.update;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\LastSeenMessagesTracker$Update.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */