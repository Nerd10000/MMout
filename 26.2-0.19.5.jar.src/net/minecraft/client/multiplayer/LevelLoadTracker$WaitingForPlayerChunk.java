/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ final class WaitingForPlayerChunk extends Record implements LevelLoadTracker.ClientState {
/*     */   private final LocalPlayer player;
/*     */   
/*     */   private final ClientLevel level;
/*     */   
/*     */   private final AtomicBoolean playerSectionReady;
/*     */   
/*     */   private final long timeoutAfter;
/*     */   
/*     */   private WaitingForPlayerChunk(LocalPlayer player, ClientLevel level, AtomicBoolean playerSectionReady, long timeoutAfter) {
/* 132 */     this.player = player;
/* 132 */     this.level = level;
/* 132 */     this.playerSectionReady = playerSectionReady;
/* 132 */     this.timeoutAfter = timeoutAfter;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #132	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #132	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #132	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public LocalPlayer player() {
/* 132 */     return this.player;
/*     */   }
/*     */   
/*     */   public ClientLevel level() {
/* 132 */     return this.level;
/*     */   }
/*     */   
/*     */   public AtomicBoolean playerSectionReady() {
/* 132 */     return this.playerSectionReady;
/*     */   }
/*     */   
/*     */   public long timeoutAfter() {
/* 132 */     return this.timeoutAfter;
/*     */   }
/*     */   
/*     */   public LevelLoadTracker.ClientState tick() {
/* 140 */     return isReady() ? new LevelLoadTracker.ClientLevelReady(Util.getMillis()) : this;
/*     */   }
/*     */   
/*     */   private boolean isReady() {
/* 145 */     if (Util.getMillis() > this.timeoutAfter) {
/* 146 */       LevelLoadTracker.LOGGER.warn("Timed out while waiting for the client to load chunks, letting the player into the world anyway");
/* 147 */       return true;
/*     */     } 
/* 150 */     BlockPos playerPos = this.player.blockPosition();
/* 151 */     BlockPos cameraPos = (Minecraft.getInstance()).gameRenderer.mainCamera().blockPosition();
/* 152 */     if (this.level.isOutsideBuildHeight(playerPos.getY()) || this.level.isOutsideBuildHeight(cameraPos.getY()) || this.player.isSpectator() || !this.player.isAlive())
/* 153 */       return true; 
/* 155 */     return this.playerSectionReady.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\LevelLoadTracker$WaitingForPlayerChunk.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */