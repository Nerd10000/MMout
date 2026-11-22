/*     */ package net.minecraft.server.notifications;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.players.IpBanListEntry;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.ServerOpListEntry;
/*     */ import net.minecraft.server.players.UserBanListEntry;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ 
/*     */ public class NotificationManager implements NotificationService {
/*  16 */   private final List<NotificationService> notificationServices = Lists.newArrayList();
/*     */   
/*     */   private DedicatedServer server;
/*     */   
/*     */   public void registerService(NotificationService notificationService) {
/*  20 */     this.notificationServices.add(notificationService);
/*     */   }
/*     */   
/*     */   public void setServer(DedicatedServer server) {
/*  24 */     if (this.server != null)
/*  25 */       throw new IllegalStateException("Server already set"); 
/*  27 */     this.server = server;
/*     */   }
/*     */   
/*     */   public DedicatedServer server() {
/*  31 */     return this.server;
/*     */   }
/*     */   
/*     */   public void playerJoined(ServerPlayer player) {
/*  36 */     this.notificationServices.forEach(notificationService -> notificationService.playerJoined(player));
/*     */   }
/*     */   
/*     */   public void playerLeft(ServerPlayer player) {
/*  41 */     this.notificationServices.forEach(notificationService -> notificationService.playerLeft(player));
/*     */   }
/*     */   
/*     */   public void serverStarted() {
/*  46 */     this.notificationServices.forEach(NotificationService::serverStarted);
/*     */   }
/*     */   
/*     */   public void serverShuttingDown() {
/*  51 */     this.notificationServices.forEach(NotificationService::serverShuttingDown);
/*     */   }
/*     */   
/*     */   public void serverSaveStarted() {
/*  56 */     this.notificationServices.forEach(NotificationService::serverSaveStarted);
/*     */   }
/*     */   
/*     */   public void serverSaveCompleted() {
/*  61 */     this.notificationServices.forEach(NotificationService::serverSaveCompleted);
/*     */   }
/*     */   
/*     */   public void serverActivityOccured() {
/*  66 */     this.notificationServices.forEach(NotificationService::serverActivityOccured);
/*     */   }
/*     */   
/*     */   public void playerOped(ServerOpListEntry operator) {
/*  71 */     this.notificationServices.forEach(notificationService -> notificationService.playerOped(operator));
/*     */   }
/*     */   
/*     */   public void playerDeoped(ServerOpListEntry operator) {
/*  76 */     this.notificationServices.forEach(notificationService -> notificationService.playerDeoped(operator));
/*     */   }
/*     */   
/*     */   public void playerAddedToAllowlist(NameAndId player) {
/*  81 */     this.notificationServices.forEach(notificationService -> notificationService.playerAddedToAllowlist(player));
/*     */   }
/*     */   
/*     */   public void playerRemovedFromAllowlist(NameAndId player) {
/*  86 */     this.notificationServices.forEach(notificationService -> notificationService.playerRemovedFromAllowlist(player));
/*     */   }
/*     */   
/*     */   public void ipBanned(IpBanListEntry ban) {
/*  91 */     this.notificationServices.forEach(notificationService -> notificationService.ipBanned(ban));
/*     */   }
/*     */   
/*     */   public void ipUnbanned(String ip) {
/*  96 */     this.notificationServices.forEach(notificationService -> notificationService.ipUnbanned(ip));
/*     */   }
/*     */   
/*     */   public void playerBanned(UserBanListEntry ban) {
/* 101 */     this.notificationServices.forEach(notificationService -> notificationService.playerBanned(ban));
/*     */   }
/*     */   
/*     */   public void playerUnbanned(NameAndId player) {
/* 106 */     this.notificationServices.forEach(notificationService -> notificationService.playerUnbanned(player));
/*     */   }
/*     */   
/*     */   public <T> void onGameRuleChanged(GameRule<T> gameRule, T value) {
/* 111 */     this.notificationServices.forEach(notificationService -> notificationService.onGameRuleChanged(gameRule, value));
/*     */   }
/*     */   
/*     */   public void statusHeartbeat() {
/* 116 */     this.notificationServices.forEach(NotificationService::statusHeartbeat);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\notifications\NotificationManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */