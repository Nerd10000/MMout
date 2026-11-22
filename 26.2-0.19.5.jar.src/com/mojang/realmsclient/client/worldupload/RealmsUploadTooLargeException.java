/*    */ package com.mojang.realmsclient.client.worldupload;
/*    */ 
/*    */ import com.mojang.realmsclient.Unit;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class RealmsUploadTooLargeException extends RealmsUploadException {
/*    */   private final long sizeLimit;
/*    */   
/*    */   public RealmsUploadTooLargeException(long sizeLimit) {
/* 10 */     this.sizeLimit = sizeLimit;
/*    */   }
/*    */   
/*    */   public Component[] getErrorMessages() {
/* 15 */     return new Component[] { (Component)Component.translatable("mco.upload.failed.too_big.title"), 
/* 17 */         (Component)Component.translatable("mco.upload.failed.too_big.description", new Object[] { Unit.humanReadable(this.sizeLimit, Unit.getLargest(this.sizeLimit)) }) };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\worldupload\RealmsUploadTooLargeException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */