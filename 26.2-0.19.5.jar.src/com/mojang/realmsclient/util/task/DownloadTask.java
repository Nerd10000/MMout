/*    */ package com.mojang.realmsclient.util.task;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.client.RealmsClient;
/*    */ import com.mojang.realmsclient.dto.WorldDownload;
/*    */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*    */ import com.mojang.realmsclient.exception.RetryCallException;
/*    */ import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
/*    */ import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DownloadTask extends LongRunningTask {
/* 15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 17 */   private static final Component TITLE = (Component)Component.translatable("mco.download.preparing");
/*    */   
/*    */   private final long realmId;
/*    */   
/*    */   private final int slot;
/*    */   
/*    */   private final Screen lastScreen;
/*    */   
/*    */   private final String downloadName;
/*    */   
/*    */   public DownloadTask(long realmId, int slot, String downloadName, Screen lastScreen) {
/* 25 */     this.realmId = realmId;
/* 26 */     this.slot = slot;
/* 27 */     this.lastScreen = lastScreen;
/* 28 */     this.downloadName = downloadName;
/*    */   }
/*    */   
/*    */   public void run() {
/* 33 */     RealmsClient client = RealmsClient.getOrCreate();
/* 35 */     for (int i = 0; i < 25; i++) {
/*    */       try {
/* 37 */         if (aborted())
/*    */           return; 
/* 40 */         WorldDownload worldDownload = client.requestDownloadInfo(this.realmId, this.slot);
/* 41 */         pause(1L);
/* 42 */         if (aborted())
/*    */           return; 
/* 45 */         setScreen((Screen)new RealmsDownloadLatestWorldScreen(this.lastScreen, worldDownload, this.downloadName, result -> {
/*    */               
/*    */               }));
/*    */         return;
/* 47 */       } catch (RetryCallException e) {
/* 48 */         if (aborted())
/*    */           return; 
/* 51 */         pause(e.delaySeconds);
/* 52 */       } catch (RealmsServiceException e) {
/* 53 */         if (aborted())
/*    */           return; 
/* 56 */         LOGGER.error("Couldn't download world data", (Throwable)e);
/* 57 */         setScreen((Screen)new RealmsGenericErrorScreen(e, this.lastScreen));
/*    */         return;
/* 59 */       } catch (Exception e) {
/* 60 */         if (aborted())
/*    */           return; 
/* 63 */         LOGGER.error("Couldn't download world data", e);
/* 64 */         error(e);
/*    */         return;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public Component getTitle() {
/* 72 */     return TITLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclien\\util\task\DownloadTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */