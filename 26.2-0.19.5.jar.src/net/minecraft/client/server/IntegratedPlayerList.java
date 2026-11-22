/*    */ package net.minecraft.client.server;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.RegistryLayer;
/*    */ import net.minecraft.server.notifications.NotificationService;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ import net.minecraft.world.level.storage.PlayerDataStorage;
/*    */ 
/*    */ public class IntegratedPlayerList extends PlayerList {
/*    */   public IntegratedPlayerList(IntegratedServer server, LayeredRegistryAccess<RegistryLayer> registryHolder, PlayerDataStorage playerDataStorage) {
/* 14 */     super(server, registryHolder, playerDataStorage, (NotificationService)server.notificationManager());
/* 16 */     setViewDistance(10);
/*    */   }
/*    */   
/*    */   public Component canPlayerLogin(SocketAddress address, NameAndId nameAndId) {
/* 21 */     if (getServer().isSingleplayerOwner(nameAndId) && getPlayerByName(nameAndId.name()) != null)
/* 22 */       return (Component)Component.translatable("multiplayer.disconnect.name_taken"); 
/* 25 */     return super.canPlayerLogin(address, nameAndId);
/*    */   }
/*    */   
/*    */   public IntegratedServer getServer() {
/* 30 */     return (IntegratedServer)super.getServer();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\server\IntegratedPlayerList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */