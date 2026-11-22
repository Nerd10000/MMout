/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.RegistryLayer;
/*     */ import net.minecraft.server.notifications.NotificationService;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.world.level.storage.PlayerDataStorage;
/*     */ 
/*     */ class null extends PlayerList {
/*     */   null(GameTestServer this$0, MinecraftServer server, LayeredRegistryAccess<RegistryLayer> registries, PlayerDataStorage playerIo, NotificationService notificationService) {
/* 165 */     super(server, registries, playerIo, notificationService);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestServer$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */