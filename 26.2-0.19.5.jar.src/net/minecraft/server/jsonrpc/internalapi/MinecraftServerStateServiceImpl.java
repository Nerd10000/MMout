/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.notifications.NotificationManager;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class MinecraftServerStateServiceImpl implements MinecraftServerStateService {
/*    */   private final NotificationManager notificationManager;
/*    */   
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftServerStateServiceImpl(NotificationManager notificationManager, JsonRpcLogger jsonrpcLogger) {
/* 22 */     this.notificationManager = notificationManager;
/* 23 */     this.jsonrpcLogger = jsonrpcLogger;
/*    */   }
/*    */   
/*    */   private DedicatedServer server() {
/* 27 */     return Objects.<DedicatedServer>requireNonNull(this.notificationManager.server());
/*    */   }
/*    */   
/*    */   public boolean isReady() {
/* 32 */     DedicatedServer dedicatedServer = this.notificationManager.server();
/* 33 */     return (dedicatedServer != null && dedicatedServer.isReady());
/*    */   }
/*    */   
/*    */   public boolean saveEverything(boolean suppressLogs, boolean flush, boolean force, ClientInfo clientInfo) {
/* 38 */     this.jsonrpcLogger.log(clientInfo, "Save everything. SuppressLogs: {}, flush: {}, force: {}", new Object[] { Boolean.valueOf(suppressLogs), Boolean.valueOf(flush), Boolean.valueOf(force) });
/* 39 */     return server().saveEverything(suppressLogs, flush, force);
/*    */   }
/*    */   
/*    */   public void halt(boolean waitForShutdown, ClientInfo clientInfo) {
/* 44 */     this.jsonrpcLogger.log(clientInfo, "Halt server. WaitForShutdown: {}", new Object[] { Boolean.valueOf(waitForShutdown) });
/* 45 */     server().halt(waitForShutdown);
/*    */   }
/*    */   
/*    */   public void sendSystemMessage(Component message, ClientInfo clientInfo) {
/* 50 */     this.jsonrpcLogger.log(clientInfo, "Send system message: '{}'", new Object[] { message.getString() });
/* 51 */     server().sendSystemMessage(message);
/*    */   }
/*    */   
/*    */   public void sendSystemMessage(Component message, boolean overlay, Collection<ServerPlayer> players, ClientInfo clientInfo) {
/* 56 */     List<String> playerNames = players.stream().map(Player::getPlainTextName).toList();
/* 57 */     this.jsonrpcLogger.log(clientInfo, "Send system message to '{}' players (overlay: {}): '{}'", new Object[] { Integer.valueOf(playerNames.size()), Boolean.valueOf(overlay), message.getString() });
/* 59 */     for (ServerPlayer player : players) {
/* 60 */       if (overlay) {
/* 61 */         player.sendOverlayMessage(message);
/*    */         continue;
/*    */       } 
/* 63 */       player.sendSystemMessage(message);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void broadcastSystemMessage(Component message, boolean overlay, ClientInfo clientInfo) {
/* 70 */     this.jsonrpcLogger.log(clientInfo, "Broadcast system message (overlay: {}): '{}'", new Object[] { Boolean.valueOf(overlay), message.getString() });
/* 72 */     for (ServerPlayer player : server().getPlayerList().getPlayers()) {
/* 73 */       if (overlay) {
/* 74 */         player.sendOverlayMessage(message);
/*    */         continue;
/*    */       } 
/* 76 */       player.sendSystemMessage(message);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftServerStateServiceImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */