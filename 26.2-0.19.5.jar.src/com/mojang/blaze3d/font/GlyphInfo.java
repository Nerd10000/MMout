/*    */ package com.mojang.blaze3d.font;
/*    */ 
/*    */ public interface GlyphInfo {
/*    */   float getAdvance();
/*    */   
/*    */   default float getAdvance(boolean bold) {
/*  7 */     return getAdvance() + (bold ? getBoldOffset() : 0.0F);
/*    */   }
/*    */   
/*    */   default float getBoldOffset() {
/* 11 */     return 1.0F;
/*    */   }
/*    */   
/*    */   default float getShadowOffset() {
/* 15 */     return 1.0F;
/*    */   }
/*    */   
/*    */   static GlyphInfo simple(float advance) {
/* 19 */     return () -> advance;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\font\GlyphInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */