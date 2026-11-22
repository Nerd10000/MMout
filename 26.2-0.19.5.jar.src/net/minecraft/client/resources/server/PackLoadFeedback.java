/*    */ package net.minecraft.client.resources.server;
/*    */ 
/*    */ import java.util.UUID;
/*    */ 
/*    */ public interface PackLoadFeedback {
/*    */   void reportUpdate(UUID paramUUID, Update paramUpdate);
/*    */   
/*    */   void reportFinalResult(UUID paramUUID, FinalResult paramFinalResult);
/*    */   
/*    */   public enum Update {
/*  7 */     ACCEPTED, DOWNLOADED;
/*    */   }
/*    */   
/*    */   public enum FinalResult {
/* 13 */     DECLINED, APPLIED, DISCARDED, DOWNLOAD_FAILED, ACTIVATION_FAILED;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\PackLoadFeedback.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */