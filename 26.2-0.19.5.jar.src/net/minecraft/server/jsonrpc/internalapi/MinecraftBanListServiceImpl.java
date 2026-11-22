/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.notifications.NotificationManager;
/*    */ import net.minecraft.server.players.IpBanListEntry;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.UserBanListEntry;
/*    */ 
/*    */ public class MinecraftBanListServiceImpl implements MinecraftBanListService {
/*    */   private final NotificationManager notificationManager;
/*    */   
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftBanListServiceImpl(NotificationManager notificationManager, JsonRpcLogger jsonrpcLogger) {
/* 20 */     this.notificationManager = notificationManager;
/* 21 */     this.jsonrpcLogger = jsonrpcLogger;
/*    */   }
/*    */   
/*    */   private DedicatedServer server() {
/* 25 */     return Objects.<DedicatedServer>requireNonNull(this.notificationManager.server());
/*    */   }
/*    */   
/*    */   public void addUserBan(UserBanListEntry ban, ClientInfo clientInfo) {
/* 30 */     this.jsonrpcLogger.log(clientInfo, "Add player '{}' to banlist. Reason: '{}'", new Object[] { ban.getDisplayName(), ban.getReasonMessage().getString() });
/* 31 */     server().getPlayerList().getBans().add(ban);
/*    */   }
/*    */   
/*    */   public void removeUserBan(NameAndId nameAndId, ClientInfo clientInfo) {
/* 36 */     this.jsonrpcLogger.log(clientInfo, "Remove player '{}' from banlist", new Object[] { nameAndId });
/* 37 */     server().getPlayerList().getBans().remove(nameAndId);
/*    */   }
/*    */   
/*    */   public void clearUserBans(ClientInfo clientInfo) {
/* 42 */     server().getPlayerList().getBans().clear();
/*    */   }
/*    */   
/*    */   public Collection<UserBanListEntry> getUserBanEntries() {
/* 47 */     return server().getPlayerList().getBans().getEntries();
/*    */   }
/*    */   
/*    */   public Collection<IpBanListEntry> getIpBanEntries() {
/* 52 */     return server().getPlayerList().getIpBans().getEntries();
/*    */   }
/*    */   
/*    */   public void addIpBan(IpBanListEntry ipBanEntry, ClientInfo clientInfo) {
/* 57 */     this.jsonrpcLogger.log(clientInfo, "Add ip '{}' to ban list", new Object[] { ipBanEntry.getUser() });
/* 58 */     server().getPlayerList().getIpBans().add(ipBanEntry);
/*    */   }
/*    */   
/*    */   public void clearIpBans(ClientInfo clientInfo) {
/* 63 */     this.jsonrpcLogger.log(clientInfo, "Clear ip ban list", new Object[0]);
/* 64 */     server().getPlayerList().getIpBans().clear();
/*    */   }
/*    */   
/*    */   public void removeIpBan(String ip, ClientInfo clientInfo) {
/* 69 */     this.jsonrpcLogger.log(clientInfo, "Remove ip '{}' from ban list", new Object[] { ip });
/* 70 */     server().getPlayerList().getIpBans().remove(ip);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftBanListServiceImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */