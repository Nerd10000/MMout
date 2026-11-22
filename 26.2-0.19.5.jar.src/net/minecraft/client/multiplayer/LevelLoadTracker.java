/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.progress.ChunkLoadStatusView;
/*     */ import net.minecraft.server.level.progress.LevelLoadListener;
/*     */ import net.minecraft.server.level.progress.LevelLoadProgressTracker;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LevelLoadTracker implements LevelLoadListener {
/*  21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  22 */   private static final long CLIENT_WAIT_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(30L);
/*     */   
/*     */   public static final long LEVEL_LOAD_CLOSE_DELAY_MS = 500L;
/*     */   
/*  27 */   private final LevelLoadProgressTracker serverProgressTracker = new LevelLoadProgressTracker(true);
/*     */   
/*     */   private ChunkLoadStatusView serverChunkStatusView;
/*     */   
/*     */   private volatile LevelLoadListener.Stage serverStage;
/*     */   
/*     */   private ClientState clientState;
/*     */   
/*     */   private final long closeDelayMs;
/*     */   
/*     */   public LevelLoadTracker() {
/*  35 */     this(0L);
/*     */   }
/*     */   
/*     */   public LevelLoadTracker(long closeDelayMs) {
/*  39 */     this.closeDelayMs = closeDelayMs;
/*     */   }
/*     */   
/*     */   public void setServerChunkStatusView(ChunkLoadStatusView serverChunkStatusView) {
/*  43 */     this.serverChunkStatusView = serverChunkStatusView;
/*     */   }
/*     */   
/*     */   public void startClientLoad(LocalPlayer player, ClientLevel level) {
/*  47 */     this.clientState = new WaitingForServer(player, level, Util.getMillis() + CLIENT_WAIT_TIMEOUT_MS);
/*     */   }
/*     */   
/*     */   public void tickClientLoad() {
/*  51 */     if (this.clientState != null)
/*  52 */       this.clientState = this.clientState.tick(); 
/*     */   }
/*     */   
/*     */   public boolean isLevelReady() {
/*  57 */     ClientState clientState = this.clientState;
/*  57 */     if (clientState instanceof ClientLevelReady) {
/*  57 */       ClientLevelReady clientLevelReady = (ClientLevelReady)clientState;
/*     */       try {
/*  57 */         long l1 = clientLevelReady.readyAt(), l2 = l1;
/*     */         if (true) {
/*  57 */           long readyAt = l1;
/*  58 */           if (Util.getMillis() >= readyAt + this.closeDelayMs);
/*     */         } 
/*     */         return false;
/*     */       } catch (Throwable throwable) {
/*  58 */         throw new MatchException(throwable.toString(), throwable);
/*     */       } 
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   public void loadingPacketsReceived() {
/*  62 */     if (this.clientState != null)
/*  63 */       this.clientState = this.clientState.loadingPacketsReceived(); 
/*     */   }
/*     */   
/*     */   public void start(LevelLoadListener.Stage stage, int totalChunks) {
/*  69 */     this.serverProgressTracker.start(stage, totalChunks);
/*  70 */     this.serverStage = stage;
/*     */   }
/*     */   
/*     */   public void update(LevelLoadListener.Stage stage, int currentChunks, int totalChunks) {
/*  75 */     this.serverProgressTracker.update(stage, currentChunks, totalChunks);
/*     */   }
/*     */   
/*     */   public void finish(LevelLoadListener.Stage stage) {
/*  80 */     this.serverProgressTracker.finish(stage);
/*     */   }
/*     */   
/*     */   public void updateFocus(ResourceKey<Level> dimension, ChunkPos chunkPos) {
/*  85 */     if (this.serverChunkStatusView != null)
/*  86 */       this.serverChunkStatusView.moveTo(dimension, chunkPos); 
/*     */   }
/*     */   
/*     */   public ChunkLoadStatusView statusView() {
/*  91 */     return this.serverChunkStatusView;
/*     */   }
/*     */   
/*     */   public float serverProgress() {
/*  95 */     return this.serverProgressTracker.get();
/*     */   }
/*     */   
/*     */   public boolean hasProgress() {
/*  99 */     return (this.serverStage != null);
/*     */   }
/*     */   
/*     */   public Runnable getPlayerCompiledSectionCallback() {
/* 103 */     ClientState clientState = this.clientState;
/* 103 */     if (clientState instanceof WaitingForPlayerChunk) {
/* 103 */       WaitingForPlayerChunk waitingForPlayerChunk = (WaitingForPlayerChunk)clientState;
/* 104 */       return () -> waitingForPlayerChunk.playerSectionReady().set(true);
/*     */     } 
/* 106 */     return null;
/*     */   }
/*     */   
/*     */   private static interface ClientState {
/*     */     default ClientState tick() {
/* 111 */       return this;
/*     */     }
/*     */     
/*     */     default ClientState loadingPacketsReceived() {
/* 115 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class WaitingForServer extends Record implements ClientState {
/*     */     private final LocalPlayer player;
/*     */     
/*     */     private final ClientLevel level;
/*     */     
/*     */     private final long timeoutAfter;
/*     */     
/*     */     private WaitingForServer(LocalPlayer player, ClientLevel level, long timeoutAfter) {
/* 120 */       this.player = player;
/* 120 */       this.level = level;
/* 120 */       this.timeoutAfter = timeoutAfter;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #120	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #120	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #120	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForServer;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public LocalPlayer player() {
/* 120 */       return this.player;
/*     */     }
/*     */     
/*     */     public ClientLevel level() {
/* 120 */       return this.level;
/*     */     }
/*     */     
/*     */     public long timeoutAfter() {
/* 120 */       return this.timeoutAfter;
/*     */     }
/*     */     
/*     */     public LevelLoadTracker.ClientState loadingPacketsReceived() {
/* 127 */       return new LevelLoadTracker.WaitingForPlayerChunk(this.player, this.level, new AtomicBoolean(), this.timeoutAfter);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class WaitingForPlayerChunk extends Record implements ClientState {
/*     */     private final LocalPlayer player;
/*     */     
/*     */     private final ClientLevel level;
/*     */     
/*     */     private final AtomicBoolean playerSectionReady;
/*     */     
/*     */     private final long timeoutAfter;
/*     */     
/*     */     private WaitingForPlayerChunk(LocalPlayer player, ClientLevel level, AtomicBoolean playerSectionReady, long timeoutAfter) {
/* 132 */       this.player = player;
/* 132 */       this.level = level;
/* 132 */       this.playerSectionReady = playerSectionReady;
/* 132 */       this.timeoutAfter = timeoutAfter;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #132	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #132	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #132	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$WaitingForPlayerChunk;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public LocalPlayer player() {
/* 132 */       return this.player;
/*     */     }
/*     */     
/*     */     public ClientLevel level() {
/* 132 */       return this.level;
/*     */     }
/*     */     
/*     */     public AtomicBoolean playerSectionReady() {
/* 132 */       return this.playerSectionReady;
/*     */     }
/*     */     
/*     */     public long timeoutAfter() {
/* 132 */       return this.timeoutAfter;
/*     */     }
/*     */     
/*     */     public LevelLoadTracker.ClientState tick() {
/* 140 */       return isReady() ? new LevelLoadTracker.ClientLevelReady(Util.getMillis()) : this;
/*     */     }
/*     */     
/*     */     private boolean isReady() {
/* 145 */       if (Util.getMillis() > this.timeoutAfter) {
/* 146 */         LevelLoadTracker.LOGGER.warn("Timed out while waiting for the client to load chunks, letting the player into the world anyway");
/* 147 */         return true;
/*     */       } 
/* 150 */       BlockPos playerPos = this.player.blockPosition();
/* 151 */       BlockPos cameraPos = (Minecraft.getInstance()).gameRenderer.mainCamera().blockPosition();
/* 152 */       if (this.level.isOutsideBuildHeight(playerPos.getY()) || this.level.isOutsideBuildHeight(cameraPos.getY()) || this.player.isSpectator() || !this.player.isAlive())
/* 153 */         return true; 
/* 155 */       return this.playerSectionReady.get();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ClientLevelReady extends Record implements ClientState {
/*     */     private final long readyAt;
/*     */     
/*     */     private ClientLevelReady(long readyAt) {
/* 160 */       this.readyAt = readyAt;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/LevelLoadTracker$ClientLevelReady;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public long readyAt() {
/* 160 */       return this.readyAt;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\LevelLoadTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */