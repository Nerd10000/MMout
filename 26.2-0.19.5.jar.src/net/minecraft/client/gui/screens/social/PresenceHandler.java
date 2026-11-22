/*     */ package net.minecraft.client.gui.screens.social;
/*     */ 
/*     */ import com.mojang.authlib.yggdrasil.FriendsService;
/*     */ import com.mojang.authlib.yggdrasil.response.PresenceResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.PresenceStatus;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.PresenceSharing;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.friends.FriendsOverlayScreen;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class PresenceHandler {
/*  22 */   private static final Duration PRESENCE_UPDATE_INTERVAL = Duration.ofMinutes(1L);
/*     */   
/*     */   private static final long MAX_PRESENCE_INTERVAL_MULTIPLIER = 5L;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final FriendsService friendsService;
/*     */   
/*  28 */   private PresenceResponse latestPresence = new PresenceResponse(new ArrayList());
/*     */   
/*  29 */   private Instant lastPresencePost = Instant.now();
/*     */   
/*     */   private boolean updatePresence;
/*     */   
/*     */   public PresenceHandler(Minecraft minecraft, FriendsService friendsService) {
/*  34 */     this.minecraft = minecraft;
/*  35 */     this.friendsService = friendsService;
/*  36 */     this.updatePresence = true;
/*     */   }
/*     */   
/*     */   private void updatePresence() {
/*  40 */     this.updatePresence = false;
/*  41 */     this.lastPresencePost = Instant.now();
/*  42 */     PresenceStatus publicPresenceStatus = getPublicPresenceStatus();
/*  43 */     CompletableFuture.runAsync(() -> {
/*     */           PresenceResponse newPresence = this.friendsService.presence(publicPresenceStatus.name());
/*     */           this.minecraft.execute(());
/*  52 */         }(Executor)Util.nonCriticalIoPool());
/*     */   }
/*     */   
/*     */   private boolean shouldRefreshPresence() {
/*  56 */     PlayerSocialManager socialManager = this.minecraft.getPlayerSocialManager();
/*  57 */     if (!socialManager.isFriendListEnabled() || socialManager.getFriends().isEmpty())
/*  58 */       return false; 
/*  60 */     Duration sinceLastPresence = Duration.between(this.lastPresencePost, Instant.now());
/*  61 */     Duration interval = this.friendsService.getPresencePollInterval().orElse(PRESENCE_UPDATE_INTERVAL);
/*  62 */     Duration maxInterval = interval.multipliedBy(5L);
/*  63 */     return ((this.updatePresence && sinceLastPresence.compareTo(interval) >= 0) || sinceLastPresence
/*  64 */       .compareTo(maxInterval) >= 0);
/*     */   }
/*     */   
/*     */   public void tick() {
/*  68 */     if (shouldRefreshPresence())
/*  69 */       updatePresence(); 
/*     */   }
/*     */   
/*     */   public void tryUpdatePresence() {
/*  74 */     this.updatePresence = true;
/*     */   }
/*     */   
/*     */   public PresenceResponse getLatestPresence() {
/*  78 */     return this.latestPresence;
/*     */   }
/*     */   
/*     */   private PresenceStatus getPublicPresenceStatus() {
/*  82 */     switch ((PresenceSharing)this.minecraft.options.sharePresence().get()) {
/*     */       default:
/*  82 */         throw new MatchException(null, null);
/*     */       case NONE:
/*     */       
/*     */       case LIMITED:
/*     */       
/*     */       case ALL:
/*     */         break;
/*     */     } 
/*  82 */     return 
/*     */       
/*  85 */       getPresenceStatus();
/*     */   }
/*     */   
/*     */   private PresenceStatus getPresenceStatus() {
/*  90 */     IntegratedServer singleplayerServer = this.minecraft.getSingleplayerServer();
/*  91 */     if (singleplayerServer != null)
/*  92 */       return (singleplayerServer.getMultiplayerScope() == MinecraftServer.MultiplayerScope.LAN) ? 
/*  93 */         PresenceStatus.PLAYING_HOSTED_SERVER : 
/*  94 */         PresenceStatus.PLAYING_OFFLINE; 
/*  96 */     ServerData server = this.minecraft.getCurrentServer();
/*  97 */     if (server != null)
/*  98 */       return server.isRealm() ? PresenceStatus.PLAYING_REALMS : PresenceStatus.PLAYING_SERVER; 
/* 100 */     return PresenceStatus.ONLINE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\social\PresenceHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */