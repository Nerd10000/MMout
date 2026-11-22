/*    */ package com.mojang.realmsclient.client.worldupload;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public abstract class RealmsUploadException extends RuntimeException {
/*    */   public Component getStatusMessage() {
/*  8 */     return null;
/*    */   }
/*    */   
/*    */   public Component[] getErrorMessages() {
/* 12 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\worldupload\RealmsUploadException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */