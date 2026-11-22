/*    */ package com.mojang.realmsclient.client.worldupload;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class RealmsUploadFailedException extends RealmsUploadException {
/*    */   private final Component errorMessage;
/*    */   
/*    */   public RealmsUploadFailedException(Component errorMessage) {
/*  9 */     this.errorMessage = errorMessage;
/*    */   }
/*    */   
/*    */   public RealmsUploadFailedException(String errorMessage) {
/* 13 */     this((Component)Component.literal(errorMessage));
/*    */   }
/*    */   
/*    */   public Component getStatusMessage() {
/* 18 */     return (Component)Component.translatable("mco.upload.failed", new Object[] { this.errorMessage });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\worldupload\RealmsUploadFailedException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */