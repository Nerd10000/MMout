/*    */ package net.minecraft.client.resources.server;
/*    */ 
/*    */ enum RemovalReason {
/* 76 */   DOWNLOAD_FAILED(PackLoadFeedback.FinalResult.DOWNLOAD_FAILED),
/* 77 */   ACTIVATION_FAILED(PackLoadFeedback.FinalResult.ACTIVATION_FAILED),
/* 78 */   DECLINED(PackLoadFeedback.FinalResult.DECLINED),
/* 79 */   DISCARDED(PackLoadFeedback.FinalResult.DISCARDED),
/* 80 */   SERVER_REMOVED(null),
/* 81 */   SERVER_REPLACED(null);
/*    */   
/*    */   private final PackLoadFeedback.FinalResult serverResponse;
/*    */   
/*    */   RemovalReason(PackLoadFeedback.FinalResult serverResponse) {
/* 87 */     this.serverResponse = serverResponse;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\ServerPackManager$RemovalReason.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */