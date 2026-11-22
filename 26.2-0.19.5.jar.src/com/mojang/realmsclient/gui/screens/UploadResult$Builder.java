/*    */ package com.mojang.realmsclient.gui.screens;
/*    */ 
/*    */ public class Builder {
/* 21 */   private int statusCode = -1;
/*    */   
/*    */   private String errorMessage;
/*    */   
/*    */   public Builder withStatusCode(int statusCode) {
/* 25 */     this.statusCode = statusCode;
/* 26 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withErrorMessage(String errorMessage) {
/* 30 */     this.errorMessage = errorMessage;
/* 31 */     return this;
/*    */   }
/*    */   
/*    */   public UploadResult build() {
/* 35 */     return new UploadResult(this.statusCode, this.errorMessage);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\UploadResult$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */