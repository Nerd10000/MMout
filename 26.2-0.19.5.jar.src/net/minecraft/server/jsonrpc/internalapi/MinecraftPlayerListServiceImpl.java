/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.notifications.NotificationManager;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ 
/*    */ public class MinecraftPlayerListServiceImpl implements MinecraftPlayerListService {
/*    */   private final NotificationManager notificationManager;
/*    */   
/*    */   private final JsonRpcLogger jsonRpcLogger;
/*    */   
/*    */   public MinecraftPlayerListServiceImpl(NotificationManager notificationManager, JsonRpcLogger jsonRpcLogger) {
/* 22 */     this.notificationManager = notificationManager;
/* 23 */     this.jsonRpcLogger = jsonRpcLogger;
/*    */   }
/*    */   
/*    */   private DedicatedServer server() {
/* 27 */     return Objects.<DedicatedServer>requireNonNull(this.notificationManager.server());
/*    */   }
/*    */   
/*    */   public List<ServerPlayer> getPlayers() {
/* 32 */     return server().getPlayerList().getPlayers();
/*    */   }
/*    */   
/*    */   public ServerPlayer getPlayer(UUID uuid) {
/* 37 */     return server().getPlayerList().getPlayer(uuid);
/*    */   }
/*    */   
/*    */   public Optional<NameAndId> fetchUserByName(String name) {
/* 42 */     return server().services().nameToIdCache().get(name);
/*    */   }
/*    */   
/*    */   public Optional<NameAndId> fetchUserById(UUID id) {
/* 47 */     return Optional.<ProfileResult>ofNullable(server().services().sessionService().fetchProfile(id, true))
/* 48 */       .map(profile -> new NameAndId(profile.profile()));
/*    */   }
/*    */   
/*    */   public Optional<NameAndId> getCachedUserById(UUID id) {
/* 53 */     return server().services().nameToIdCache().get(id);
/*    */   }
/*    */   
/*    */   public Optional<ServerPlayer> getPlayer(Optional<UUID> id, Optional<String> name) {
/* 58 */     if (id.isPresent())
/* 59 */       return Optional.ofNullable(server().getPlayerList().getPlayer(id.get())); 
/* 60 */     if (name.isPresent())
/* 61 */       return Optional.ofNullable(server().getPlayerList().getPlayerByName(name.get())); 
/* 63 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   public List<ServerPlayer> getPlayersWithAddress(String ip) {
/* 68 */     return server().getPlayerList().getPlayersWithAddress(ip);
/*    */   }
/*    */   
/*    */   public void remove(ServerPlayer serverPlayer, ClientInfo clientInfo) {
/* 73 */     server().getPlayerList().remove(serverPlayer);
/* 74 */     this.jsonRpcLogger.log(clientInfo, "Remove player '{}'", new Object[] { serverPlayer.getPlainTextName() });
/*    */   }
/*    */   
/*    */   public ServerPlayer getPlayerByName(String name) {
/* 79 */     return server().getPlayerList().getPlayerByName(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftPlayerListServiceImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */