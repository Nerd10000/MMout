/*     */ package net.minecraft.client.gui.font.providers;
/*     */ 
/*     */ import com.mojang.blaze3d.font.GlyphBitmap;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ 
/*     */ class null implements GlyphBitmap {
/*     */   public float getOversample() {
/* 238 */     return 1.0F / BitmapProvider.Glyph.this.scale;
/*     */   }
/*     */   
/*     */   public int getPixelWidth() {
/* 243 */     return BitmapProvider.Glyph.this.width;
/*     */   }
/*     */   
/*     */   public int getPixelHeight() {
/* 248 */     return BitmapProvider.Glyph.this.height;
/*     */   }
/*     */   
/*     */   public float getBearingTop() {
/* 253 */     return BitmapProvider.Glyph.this.ascent;
/*     */   }
/*     */   
/*     */   public void upload(int x, int y, GpuTexture texture) {
/* 258 */     RenderSystem.getDevice().createCommandEncoder().copyBufferToTexture(BitmapProvider.Glyph.this.imageData.gpuData().slice(), BitmapProvider.Glyph.this.offsetX, BitmapProvider.Glyph.this.offsetY, BitmapProvider.Glyph.this.imageData.image.getWidth(), BitmapProvider.Glyph.this.imageData.image.getHeight(), texture, x, y, BitmapProvider.Glyph.this.width, BitmapProvider.Glyph.this.height, 0, 0);
/*     */   }
/*     */   
/*     */   public boolean isColored() {
/* 263 */     return (BitmapProvider.Glyph.this.imageData.image.format().components() > 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\BitmapProvider$Glyph$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */