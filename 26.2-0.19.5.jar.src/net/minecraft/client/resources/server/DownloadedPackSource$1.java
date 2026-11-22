/*    */ package net.minecraft.client.resources.server;
/*    */ 
/*    */ import java.util.UUID;
/*    */ 
/*    */ class null implements PackLoadFeedback {
/*    */   public void reportUpdate(UUID id, PackLoadFeedback.Update update) {
/* 66 */     DownloadedPackSource.LOGGER.debug("Downloaded pack {} changed state to {}", id, update);
/*    */   }
/*    */   
/*    */   public void reportFinalResult(UUID id, PackLoadFeedback.FinalResult result) {
/* 71 */     DownloadedPackSource.LOGGER.debug("Downloaded pack {} finished with state {}", id, result);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\DownloadedPackSource$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */