/*    */ package com.mojang.realmsclient.client.worldupload;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class RealmsUploadCanceledException extends RealmsUploadException {
/*  6 */   private static final Component UPLOAD_CANCELED = (Component)Component.translatable("mco.upload.cancelled");
/*    */   
/*    */   public Component getStatusMessage() {
/* 10 */     return UPLOAD_CANCELED;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\worldupload\RealmsUploadCanceledException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */