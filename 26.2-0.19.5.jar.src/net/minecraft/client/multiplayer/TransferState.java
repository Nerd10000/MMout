/*   */ package net.minecraft.client.multiplayer;
/*   */ 
/*   */ import java.util.Map;
/*   */ import java.util.UUID;
/*   */ import net.minecraft.resources.Identifier;
/*   */ 
/*   */ public final class TransferState extends Record {
/*   */   private final Map<Identifier, byte[]> cookies;
/*   */   
/*   */   private final Map<UUID, PlayerInfo> seenPlayers;
/*   */   
/*   */   private final boolean seenInsecureChatWarning;
/*   */   
/*   */   public TransferState(Map<Identifier, byte[]> cookies, Map<UUID, PlayerInfo> seenPlayers, boolean seenInsecureChatWarning) {
/* 8 */     this.cookies = cookies;
/* 8 */     this.seenPlayers = seenPlayers;
/* 8 */     this.seenInsecureChatWarning = seenInsecureChatWarning;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/TransferState;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/TransferState;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/TransferState;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/TransferState;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/TransferState;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/TransferState;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public Map<Identifier, byte[]> cookies() {
/* 8 */     return this.cookies;
/*   */   }
/*   */   
/*   */   public Map<UUID, PlayerInfo> seenPlayers() {
/* 8 */     return this.seenPlayers;
/*   */   }
/*   */   
/*   */   public boolean seenInsecureChatWarning() {
/* 8 */     return this.seenInsecureChatWarning;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\TransferState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */