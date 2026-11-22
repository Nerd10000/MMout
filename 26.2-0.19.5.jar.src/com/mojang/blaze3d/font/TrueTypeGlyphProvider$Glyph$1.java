/*     */ package com.mojang.blaze3d.font;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import org.lwjgl.util.freetype.FT_Face;
/*     */ 
/*     */ class null implements GlyphBitmap {
/*     */   public int getPixelWidth() {
/* 180 */     return TrueTypeGlyphProvider.Glyph.this.width;
/*     */   }
/*     */   
/*     */   public int getPixelHeight() {
/* 185 */     return TrueTypeGlyphProvider.Glyph.this.height;
/*     */   }
/*     */   
/*     */   public float getOversample() {
/* 190 */     return this.this$1.this$0.oversample;
/*     */   }
/*     */   
/*     */   public float getBearingLeft() {
/* 195 */     return TrueTypeGlyphProvider.Glyph.this.bearingX;
/*     */   }
/*     */   
/*     */   public float getBearingTop() {
/* 200 */     return TrueTypeGlyphProvider.Glyph.this.bearingY;
/*     */   }
/*     */   
/*     */   public void upload(int x, int y, GpuTexture texture) {
/* 205 */     FT_Face face = this.this$1.this$0.validateFontOpen();
/* 207 */     NativeImage image = new NativeImage(NativeImage.Format.LUMINANCE, TrueTypeGlyphProvider.Glyph.this.width, TrueTypeGlyphProvider.Glyph.this.height, false);
/*     */     try {
/* 208 */       if (image.copyFromFont(face, TrueTypeGlyphProvider.Glyph.this.index))
/* 209 */         RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, image, 0, 0, x, y); 
/* 211 */       image.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         image.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isColored() {
/* 216 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\font\TrueTypeGlyphProvider$Glyph$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */