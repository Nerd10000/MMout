/*     */ package net.minecraft.client.main;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.DisplayData;
/*     */ import java.io.File;
/*     */ import java.net.Proxy;
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.client.PreferredGraphicsApi;
/*     */ import net.minecraft.client.User;
/*     */ import net.minecraft.client.resources.IndexedAssetSource;
/*     */ import net.minecraft.util.StringUtil;
/*     */ 
/*     */ public class GameConfig {
/*     */   public final UserData user;
/*     */   
/*     */   public final DisplayData display;
/*     */   
/*     */   public final FolderData location;
/*     */   
/*     */   public final GameData game;
/*     */   
/*     */   public final QuickPlayData quickPlay;
/*     */   
/*     */   public GameConfig(UserData userData, DisplayData displayData, FolderData folderData, GameData gameData, QuickPlayData quickPlayData) {
/*  22 */     this.user = userData;
/*  23 */     this.display = displayData;
/*  24 */     this.location = folderData;
/*  25 */     this.game = gameData;
/*  26 */     this.quickPlay = quickPlayData;
/*     */   }
/*     */   
/*     */   public static class GameData {
/*     */     public final boolean demo;
/*     */     
/*     */     public final String launchVersion;
/*     */     
/*     */     public final String versionType;
/*     */     
/*     */     public final boolean disableMultiplayer;
/*     */     
/*     */     public final boolean disableChat;
/*     */     
/*     */     public final boolean captureTracyImages;
/*     */     
/*     */     public final boolean renderDebugLabels;
/*     */     
/*     */     public final boolean vulkanValidation;
/*     */     
/*     */     public final PreferredGraphicsApi forcedGraphicsApi;
/*     */     
/*     */     public final boolean offlineDeveloperMode;
/*     */     
/*     */     public GameData(boolean demo, String launchVersion, String versionType, boolean disableMultiplayer, boolean disableChat, boolean captureTracyImages, boolean vulkanValidation, boolean renderDebugLabels, PreferredGraphicsApi forcedGraphicsApi, boolean offlineDeveloperMode) {
/*  42 */       this.demo = demo;
/*  43 */       this.launchVersion = launchVersion;
/*  44 */       this.versionType = versionType;
/*  45 */       this.disableMultiplayer = disableMultiplayer;
/*  46 */       this.disableChat = disableChat;
/*  47 */       this.captureTracyImages = captureTracyImages;
/*  48 */       this.vulkanValidation = vulkanValidation;
/*  49 */       this.renderDebugLabels = renderDebugLabels;
/*  50 */       this.forcedGraphicsApi = forcedGraphicsApi;
/*  51 */       this.offlineDeveloperMode = offlineDeveloperMode;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class UserData {
/*     */     public final User user;
/*     */     
/*     */     public final Proxy proxy;
/*     */     
/*     */     public UserData(User user, Proxy proxy) {
/*  60 */       this.user = user;
/*  61 */       this.proxy = proxy;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FolderData {
/*     */     public final File gameDirectory;
/*     */     
/*     */     public final File resourcePackDirectory;
/*     */     
/*     */     public final File assetDirectory;
/*     */     
/*     */     public final String assetIndex;
/*     */     
/*     */     public FolderData(File gameDirectory, File resourcePackDirectory, File assetDirectory, String assetIndex) {
/*  72 */       this.gameDirectory = gameDirectory;
/*  73 */       this.resourcePackDirectory = resourcePackDirectory;
/*  74 */       this.assetDirectory = assetDirectory;
/*  75 */       this.assetIndex = assetIndex;
/*     */     }
/*     */     
/*     */     public Path getExternalAssetSource() {
/*  79 */       return (this.assetIndex == null) ? this.assetDirectory.toPath() : IndexedAssetSource.createIndexFs(this.assetDirectory.toPath(), this.assetIndex);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface QuickPlayVariant {
/*  84 */     public static final QuickPlayVariant DISABLED = new GameConfig.QuickPlayDisabled();
/*     */     
/*     */     boolean isEnabled();
/*     */   }
/*     */   
/*     */   public static final class QuickPlaySinglePlayerData extends Record implements QuickPlayVariant {
/*     */     private final String worldId;
/*     */     
/*     */     public QuickPlaySinglePlayerData(String worldId) {
/*  89 */       this.worldId = worldId;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #89	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #89	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #89	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String worldId() {
/*  89 */       return this.worldId;
/*     */     }
/*     */     
/*     */     public boolean isEnabled() {
/*  92 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class QuickPlayMultiplayerData extends Record implements QuickPlayVariant {
/*     */     private final String serverAddress;
/*     */     
/*     */     public QuickPlayMultiplayerData(String serverAddress) {
/*  96 */       this.serverAddress = serverAddress;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #96	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #96	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #96	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String serverAddress() {
/*  96 */       return this.serverAddress;
/*     */     }
/*     */     
/*     */     public boolean isEnabled() {
/*  99 */       return !StringUtil.isBlank(this.serverAddress);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class QuickPlayRealmsData extends Record implements QuickPlayVariant {
/*     */     private final String realmId;
/*     */     
/*     */     public QuickPlayRealmsData(String realmId) {
/* 103 */       this.realmId = realmId;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #103	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #103	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #103	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String realmId() {
/* 103 */       return this.realmId;
/*     */     }
/*     */     
/*     */     public boolean isEnabled() {
/* 106 */       return !StringUtil.isBlank(this.realmId);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class QuickPlayDisabled extends Record implements QuickPlayVariant {
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/main/GameConfig$QuickPlayDisabled;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #110	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayDisabled;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/main/GameConfig$QuickPlayDisabled;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #110	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayDisabled;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/main/GameConfig$QuickPlayDisabled;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #110	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayDisabled;
/*     */     }
/*     */     
/*     */     public boolean isEnabled() {
/* 113 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class QuickPlayData extends Record {
/*     */     private final String logPath;
/*     */     
/*     */     private final GameConfig.QuickPlayVariant variant;
/*     */     
/*     */     public QuickPlayData(String logPath, GameConfig.QuickPlayVariant variant) {
/* 117 */       this.logPath = logPath;
/* 117 */       this.variant = variant;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/main/GameConfig$QuickPlayData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayData;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/main/GameConfig$QuickPlayData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayData;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/main/GameConfig$QuickPlayData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayData;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String logPath() {
/* 117 */       return this.logPath;
/*     */     }
/*     */     
/*     */     public GameConfig.QuickPlayVariant variant() {
/* 117 */       return this.variant;
/*     */     }
/*     */     
/*     */     public boolean isEnabled() {
/* 119 */       return this.variant.isEnabled();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\main\GameConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */