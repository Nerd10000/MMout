/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.notifications.NotificationManager;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.UserWhiteListEntry;
/*    */ 
/*    */ public class MinecraftAllowListServiceImpl implements MinecraftAllowListService {
/*    */   private final NotificationManager notificationManager;
/*    */   
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftAllowListServiceImpl(NotificationManager notificationManager, JsonRpcLogger jsonrpcLogger) {
/* 19 */     this.notificationManager = notificationManager;
/* 20 */     this.jsonrpcLogger = jsonrpcLogger;
/*    */   }
/*    */   
/*    */   private DedicatedServer server() {
/* 24 */     return Objects.<DedicatedServer>requireNonNull(this.notificationManager.server());
/*    */   }
/*    */   
/*    */   public Collection<UserWhiteListEntry> getEntries() {
/* 29 */     return server().getPlayerList().getWhiteList().getEntries();
/*    */   }
/*    */   
/*    */   public boolean add(UserWhiteListEntry infos, ClientInfo clientInfo) {
/* 34 */     this.jsonrpcLogger.log(clientInfo, "Add player '{}' to allowlist", new Object[] { infos.getUser() });
/* 35 */     return server().getPlayerList().getWhiteList().add(infos);
/*    */   }
/*    */   
/*    */   public void clear(ClientInfo clientInfo) {
/* 40 */     this.jsonrpcLogger.log(clientInfo, "Clear allowlist", new Object[0]);
/* 41 */     server().getPlayerList().getWhiteList().clear();
/*    */   }
/*    */   
/*    */   public void remove(NameAndId nameAndId, ClientInfo clientInfo) {
/* 46 */     this.jsonrpcLogger.log(clientInfo, "Remove player '{}' from allowlist", new Object[] { nameAndId });
/* 47 */     server().getPlayerList().getWhiteList().remove(nameAndId);
/*    */   }
/*    */   
/*    */   public void kickUnlistedPlayers(ClientInfo clientInfo) {
/* 52 */     this.jsonrpcLogger.log(clientInfo, "Kick unlisted players", new Object[0]);
/* 53 */     server().kickUnlistedPlayers();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftAllowListServiceImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */