/*    */ package com.mojang.realmsclient.client.worldupload;
/*    */ 
/*    */ import com.mojang.realmsclient.client.UploadStatus;
/*    */ 
/*    */ public interface RealmsWorldUploadStatusTracker {
/*    */   UploadStatus getUploadStatus();
/*    */   
/*    */   void setUploading();
/*    */   
/*    */   static RealmsWorldUploadStatusTracker noOp() {
/* 11 */     return new RealmsWorldUploadStatusTracker() {
/* 12 */         private final UploadStatus uploadStatus = new UploadStatus();
/*    */         
/*    */         public UploadStatus getUploadStatus() {
/* 16 */           return this.uploadStatus;
/*    */         }
/*    */         
/*    */         public void setUploading() {}
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\worldupload\RealmsWorldUploadStatusTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */