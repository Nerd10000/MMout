/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ 
/*     */ final class WaitingForServer extends Record implements LevelLoadTracker.ClientState {
/*     */   private final LocalPlayer player;
/*     */   
/*     */   private final ClientLevel level;
/*     */   
/*     */   private final long timeoutAfter;
/*     */   
/*     */   private WaitingForServer(LocalPlayer player, ClientLevel level, long timeoutAfter) {
/* 120 */     this.player = player;
/* 120 */     this.level = level;
/* 120 */     this.timeoutAfter = timeoutAfter;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #120	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #120	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #120	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public LocalPlayer player() {
/* 120 */     return this.player;
/*     */   }
/*     */   
/*     */   public ClientLevel level() {
/* 120 */     return this.level;
/*     */   }
/*     */   
/*     */   public long timeoutAfter() {
/* 120 */     return this.timeoutAfter;
/*     */   }
/*     */   
/*     */   public LevelLoadTracker.ClientState loadingPacketsReceived() {
/* 127 */     return new LevelLoadTracker.WaitingForPlayerChunk(this.player, this.level, new AtomicBoolean(), this.timeoutAfter);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\LevelLoadTracker$WaitingForServer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */