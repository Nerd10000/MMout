/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.client.gui.components.ChatComponent;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.telemetry.WorldSessionTelemetryManager;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.ServerLinks;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ 
/*    */ public final class CommonListenerCookie extends Record {
/*    */   private final LevelLoadTracker levelLoadTracker;
/*    */   
/*    */   private final GameProfile localGameProfile;
/*    */   
/*    */   private final WorldSessionTelemetryManager telemetryManager;
/*    */   
/*    */   private final RegistryAccess.Frozen receivedRegistries;
/*    */   
/*    */   private final FeatureFlagSet enabledFeatures;
/*    */   
/*    */   private final String serverBrand;
/*    */   
/*    */   private final ServerData serverData;
/*    */   
/*    */   private final Screen postDisconnectScreen;
/*    */   
/*    */   private final Map<Identifier, byte[]> serverCookies;
/*    */   
/*    */   private final ChatComponent.State chatState;
/*    */   
/*    */   private final Map<String, String> customReportDetails;
/*    */   
/*    */   private final ServerLinks serverLinks;
/*    */   
/*    */   private final Map<UUID, PlayerInfo> seenPlayers;
/*    */   
/*    */   private final boolean seenInsecureChatWarning;
/*    */   
/*    */   public CommonListenerCookie(LevelLoadTracker levelLoadTracker, GameProfile localGameProfile, WorldSessionTelemetryManager telemetryManager, RegistryAccess.Frozen receivedRegistries, FeatureFlagSet enabledFeatures, String serverBrand, ServerData serverData, Screen postDisconnectScreen, Map<Identifier, byte[]> serverCookies, ChatComponent.State chatState, Map<String, String> customReportDetails, ServerLinks serverLinks, Map<UUID, PlayerInfo> seenPlayers, boolean seenInsecureChatWarning) {
/* 16 */     this.levelLoadTracker = levelLoadTracker;
/* 16 */     this.localGameProfile = localGameProfile;
/* 16 */     this.telemetryManager = telemetryManager;
/* 16 */     this.receivedRegistries = receivedRegistries;
/* 16 */     this.enabledFeatures = enabledFeatures;
/* 16 */     this.serverBrand = serverBrand;
/* 16 */     this.serverData = serverData;
/* 16 */     this.postDisconnectScreen = postDisconnectScreen;
/* 16 */     this.serverCookies = serverCookies;
/* 16 */     this.chatState = chatState;
/* 16 */     this.customReportDetails = customReportDetails;
/* 16 */     this.serverLinks = serverLinks;
/* 16 */     this.seenPlayers = seenPlayers;
/* 16 */     this.seenInsecureChatWarning = seenInsecureChatWarning;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/CommonListenerCookie;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/CommonListenerCookie;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/CommonListenerCookie;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/CommonListenerCookie;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/CommonListenerCookie;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/CommonListenerCookie;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public LevelLoadTracker levelLoadTracker() {
/* 16 */     return this.levelLoadTracker;
/*    */   }
/*    */   
/*    */   public GameProfile localGameProfile() {
/* 16 */     return this.localGameProfile;
/*    */   }
/*    */   
/*    */   public WorldSessionTelemetryManager telemetryManager() {
/* 16 */     return this.telemetryManager;
/*    */   }
/*    */   
/*    */   public RegistryAccess.Frozen receivedRegistries() {
/* 16 */     return this.receivedRegistries;
/*    */   }
/*    */   
/*    */   public FeatureFlagSet enabledFeatures() {
/* 16 */     return this.enabledFeatures;
/*    */   }
/*    */   
/*    */   public String serverBrand() {
/* 16 */     return this.serverBrand;
/*    */   }
/*    */   
/*    */   public ServerData serverData() {
/* 16 */     return this.serverData;
/*    */   }
/*    */   
/*    */   public Screen postDisconnectScreen() {
/* 16 */     return this.postDisconnectScreen;
/*    */   }
/*    */   
/*    */   public Map<Identifier, byte[]> serverCookies() {
/* 16 */     return this.serverCookies;
/*    */   }
/*    */   
/*    */   public ChatComponent.State chatState() {
/* 16 */     return this.chatState;
/*    */   }
/*    */   
/*    */   public Map<String, String> customReportDetails() {
/* 16 */     return this.customReportDetails;
/*    */   }
/*    */   
/*    */   public ServerLinks serverLinks() {
/* 16 */     return this.serverLinks;
/*    */   }
/*    */   
/*    */   public Map<UUID, PlayerInfo> seenPlayers() {
/* 16 */     return this.seenPlayers;
/*    */   }
/*    */   
/*    */   public boolean seenInsecureChatWarning() {
/* 16 */     return this.seenInsecureChatWarning;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\CommonListenerCookie.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */