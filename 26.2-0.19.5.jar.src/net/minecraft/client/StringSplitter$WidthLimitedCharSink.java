/*    */ package net.minecraft.client;
/*    */ 
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.FormattedCharSink;
/*    */ 
/*    */ class WidthLimitedCharSink implements FormattedCharSink {
/*    */   private float maxWidth;
/*    */   
/*    */   private int position;
/*    */   
/*    */   public WidthLimitedCharSink(float maxWidth) {
/* 67 */     this.maxWidth = maxWidth;
/*    */   }
/*    */   
/*    */   public boolean accept(int position, Style style, int codepoint) {
/* 72 */     this.maxWidth -= StringSplitter.this.widthProvider.getWidth(codepoint, style);
/* 73 */     if (this.maxWidth >= 0.0F) {
/* 74 */       this.position = position + Character.charCount(codepoint);
/* 75 */       return true;
/*    */     } 
/* 77 */     return false;
/*    */   }
/*    */   
/*    */   public int getPosition() {
/* 81 */     return this.position;
/*    */   }
/*    */   
/*    */   public void resetPosition() {
/* 85 */     this.position = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\StringSplitter$WidthLimitedCharSink.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */