/*    */ package net.minecraft.client.main;
/*    */ 
/*    */ import net.minecraft.client.PreferredGraphicsApi;
/*    */ 
/*    */ public class GameData {
/*    */   public final boolean demo;
/*    */   
/*    */   public final String launchVersion;
/*    */   
/*    */   public final String versionType;
/*    */   
/*    */   public final boolean disableMultiplayer;
/*    */   
/*    */   public final boolean disableChat;
/*    */   
/*    */   public final boolean captureTracyImages;
/*    */   
/*    */   public final boolean renderDebugLabels;
/*    */   
/*    */   public final boolean vulkanValidation;
/*    */   
/*    */   public final PreferredGraphicsApi forcedGraphicsApi;
/*    */   
/*    */   public final boolean offlineDeveloperMode;
/*    */   
/*    */   public GameData(boolean demo, String launchVersion, String versionType, boolean disableMultiplayer, boolean disableChat, boolean captureTracyImages, boolean vulkanValidation, boolean renderDebugLabels, PreferredGraphicsApi forcedGraphicsApi, boolean offlineDeveloperMode) {
/* 42 */     this.demo = demo;
/* 43 */     this.launchVersion = launchVersion;
/* 44 */     this.versionType = versionType;
/* 45 */     this.disableMultiplayer = disableMultiplayer;
/* 46 */     this.disableChat = disableChat;
/* 47 */     this.captureTracyImages = captureTracyImages;
/* 48 */     this.vulkanValidation = vulkanValidation;
/* 49 */     this.renderDebugLabels = renderDebugLabels;
/* 50 */     this.forcedGraphicsApi = forcedGraphicsApi;
/* 51 */     this.offlineDeveloperMode = offlineDeveloperMode;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\main\GameConfig$GameData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */