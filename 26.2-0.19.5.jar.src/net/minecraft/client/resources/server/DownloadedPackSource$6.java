/*     */ package net.minecraft.client.resources.server;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
/*     */ 
/*     */ class null implements PackLoadFeedback {
/*     */   public void reportUpdate(UUID id, PackLoadFeedback.Update result) {
/* 366 */     DownloadedPackSource.LOGGER.debug("Pack {} changed status to {}", id, result);
/* 367 */     switch (DownloadedPackSource.null.$SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$Update[result.ordinal()]) {
/*     */       default:
/* 367 */         throw new MatchException(null, null);
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */         break;
/*     */     } 
/* 367 */     ServerboundResourcePackPacket.Action response = 
/*     */       
/* 369 */       ServerboundResourcePackPacket.Action.DOWNLOADED;
/* 371 */     connection.send((Packet)new ServerboundResourcePackPacket(id, response));
/*     */   }
/*     */   
/*     */   public void reportFinalResult(UUID id, PackLoadFeedback.FinalResult result) {
/* 376 */     DownloadedPackSource.LOGGER.debug("Pack {} changed status to {}", id, result);
/* 377 */     switch (DownloadedPackSource.null.$SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$FinalResult[result.ordinal()]) {
/*     */       default:
/* 377 */         throw new MatchException(null, null);
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */       
/*     */       case 4:
/*     */       
/*     */       case 5:
/*     */         break;
/*     */     } 
/* 377 */     ServerboundResourcePackPacket.Action response = 
/*     */       
/* 382 */       ServerboundResourcePackPacket.Action.FAILED_RELOAD;
/* 384 */     connection.send((Packet)new ServerboundResourcePackPacket(id, response));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\DownloadedPackSource$6.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */