/*     */ package net.minecraft.client.gui.font.providers;
/*     */ 
/*     */ import com.mojang.blaze3d.font.GlyphBitmap;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import java.nio.IntBuffer;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ class null implements GlyphBitmap {
/*     */   public float getOversample() {
/* 365 */     return 2.0F;
/*     */   }
/*     */   
/*     */   public int getPixelWidth() {
/* 370 */     return UnihexProvider.Glyph.this.width();
/*     */   }
/*     */   
/*     */   public int getPixelHeight() {
/* 375 */     return 16;
/*     */   }
/*     */   
/*     */   public void upload(int x, int y, GpuTexture texture) {
/* 380 */     IntBuffer targetBuffer = MemoryUtil.memAllocInt(UnihexProvider.Glyph.this.width() * 16);
/* 381 */     UnihexProvider.unpackBitsToBytes(targetBuffer, UnihexProvider.Glyph.this.contents, UnihexProvider.Glyph.this.left, UnihexProvider.Glyph.this.right);
/* 382 */     targetBuffer.rewind();
/* 383 */     RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, MemoryUtil.memByteBuffer(targetBuffer), 0, 0, x, y, UnihexProvider.Glyph.this.width(), 16);
/* 384 */     MemoryUtil.memFree(targetBuffer);
/*     */   }
/*     */   
/*     */   public boolean isColored() {
/* 390 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\UnihexProvider$Glyph$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */