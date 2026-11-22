/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ final class ClientLevelReady extends Record implements LevelLoadTracker.ClientState {
/*     */   private final long readyAt;
/*     */   
/*     */   private ClientLevelReady(long readyAt) {
/* 160 */     this.readyAt = readyAt;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #160	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #160	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #160	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public long readyAt() {
/* 160 */     return this.readyAt;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\LevelLoadTracker$ClientLevelReady.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */