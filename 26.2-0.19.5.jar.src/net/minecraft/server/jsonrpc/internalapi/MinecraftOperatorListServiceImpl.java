/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.notifications.NotificationManager;
/*    */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*    */ import net.minecraft.server.permissions.PermissionLevel;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.ServerOpListEntry;
/*    */ 
/*    */ public class MinecraftOperatorListServiceImpl implements MinecraftOperatorListService {
/*    */   private final NotificationManager notificationManager;
/*    */   
/*    */   private final JsonRpcLogger jsonrpcLogger;
/*    */   
/*    */   public MinecraftOperatorListServiceImpl(NotificationManager notificationManager, JsonRpcLogger jsonrpcLogger) {
/* 22 */     this.notificationManager = notificationManager;
/* 23 */     this.jsonrpcLogger = jsonrpcLogger;
/*    */   }
/*    */   
/*    */   private DedicatedServer server() {
/* 27 */     return Objects.<DedicatedServer>requireNonNull(this.notificationManager.server());
/*    */   }
/*    */   
/*    */   public Collection<ServerOpListEntry> getEntries() {
/* 32 */     return server().getPlayerList().getOps().getEntries();
/*    */   }
/*    */   
/*    */   public void op(NameAndId nameAndId, Optional<PermissionLevel> permissionLevel, Optional<Boolean> canBypassPlayerLimit, ClientInfo clientInfo) {
/* 37 */     this.jsonrpcLogger.log(clientInfo, "Op '{}'", new Object[] { nameAndId });
/* 38 */     server().getPlayerList().op(nameAndId, permissionLevel.map(LevelBasedPermissionSet::forLevel), canBypassPlayerLimit);
/*    */   }
/*    */   
/*    */   public void op(NameAndId nameAndId, ClientInfo clientInfo) {
/* 43 */     this.jsonrpcLogger.log(clientInfo, "Op '{}'", new Object[] { nameAndId });
/* 44 */     server().getPlayerList().op(nameAndId);
/*    */   }
/*    */   
/*    */   public void deop(NameAndId nameAndId, ClientInfo clientInfo) {
/* 49 */     this.jsonrpcLogger.log(clientInfo, "Deop '{}'", new Object[] { nameAndId });
/* 50 */     server().getPlayerList().deop(nameAndId);
/*    */   }
/*    */   
/*    */   public void clear(ClientInfo clientInfo) {
/* 55 */     this.jsonrpcLogger.log(clientInfo, "Clear operator list", new Object[0]);
/* 56 */     server().getPlayerList().getOps().clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftOperatorListServiceImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */