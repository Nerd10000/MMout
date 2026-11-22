/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public interface MinecraftPlayerListService {
/*    */   default CompletableFuture<Optional<NameAndId>> getUser(Optional<UUID> id, Optional<String> name) {
/* 21 */     if (id.isPresent()) {
/* 22 */       Optional<NameAndId> nameAndId = getCachedUserById(id.get());
/* 23 */       if (nameAndId.isPresent())
/* 24 */         return CompletableFuture.completedFuture(nameAndId); 
/* 26 */       return CompletableFuture.supplyAsync(() -> fetchUserById(id.get()), (Executor)Util.nonCriticalIoPool());
/*    */     } 
/* 27 */     if (name.isPresent())
/* 28 */       return CompletableFuture.supplyAsync(() -> fetchUserByName(name.get()), (Executor)Util.nonCriticalIoPool()); 
/* 30 */     return CompletableFuture.completedFuture(Optional.empty());
/*    */   }
/*    */   
/*    */   List<ServerPlayer> getPlayers();
/*    */   
/*    */   ServerPlayer getPlayer(UUID paramUUID);
/*    */   
/*    */   Optional<NameAndId> fetchUserByName(String paramString);
/*    */   
/*    */   Optional<NameAndId> fetchUserById(UUID paramUUID);
/*    */   
/*    */   Optional<NameAndId> getCachedUserById(UUID paramUUID);
/*    */   
/*    */   Optional<ServerPlayer> getPlayer(Optional<UUID> paramOptional, Optional<String> paramOptional1);
/*    */   
/*    */   List<ServerPlayer> getPlayersWithAddress(String paramString);
/*    */   
/*    */   ServerPlayer getPlayerByName(String paramString);
/*    */   
/*    */   void remove(ServerPlayer paramServerPlayer, ClientInfo paramClientInfo);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftPlayerListService.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */