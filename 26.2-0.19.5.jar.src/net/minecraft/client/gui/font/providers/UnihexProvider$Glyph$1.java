/*     */ package net.minecraft.client.gui.font.providers;
/*     */ 
/*     */ import com.mojang.blaze3d.font.GlyphInfo;
/*     */ 
/*     */ class null implements GlyphInfo {
/*     */   public float getAdvance() {
/* 345 */     return UnihexProvider.Glyph.this.width() / 2.0F + 1.0F;
/*     */   }
/*     */   
/*     */   public float getShadowOffset() {
/* 350 */     return 0.5F;
/*     */   }
/*     */   
/*     */   public float getBoldOffset() {
/* 355 */     return 0.5F;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\UnihexProvider$Glyph$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */