/*   */ package net.minecraft.network.chat;
/*   */ 
/*   */ public final class LastSeenTrackedEntry extends Record {
/*   */   private final MessageSignature signature;
/*   */   
/*   */   private final boolean pending;
/*   */   
/*   */   public LastSeenTrackedEntry(MessageSignature signature, boolean pending) {
/* 3 */     this.signature = signature;
/* 3 */     this.pending = pending;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/LastSeenTrackedEntry;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenTrackedEntry;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/LastSeenTrackedEntry;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/network/chat/LastSeenTrackedEntry;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/LastSeenTrackedEntry;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/network/chat/LastSeenTrackedEntry;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public MessageSignature signature() {
/* 3 */     return this.signature;
/*   */   }
/*   */   
/*   */   public boolean pending() {
/* 3 */     return this.pending;
/*   */   }
/*   */   
/*   */   public LastSeenTrackedEntry acknowledge() {
/* 5 */     return this.pending ? new LastSeenTrackedEntry(this.signature, false) : this;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\LastSeenTrackedEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */