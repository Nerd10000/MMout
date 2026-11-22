/*     */ package com.mojang.blaze3d.font;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*     */ import org.lwjgl.util.freetype.FT_Face;
/*     */ 
/*     */ class Glyph implements UnbakedGlyph {
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final float bearingX;
/*     */   
/*     */   private final float bearingY;
/*     */   
/*     */   private final GlyphInfo info;
/*     */   
/*     */   private final int index;
/*     */   
/*     */   private Glyph(float left, float top, int width, int height, float advance, int index) {
/* 159 */     this.width = width;
/* 160 */     this.height = height;
/* 162 */     this.info = GlyphInfo.simple(advance / paramTrueTypeGlyphProvider.oversample);
/* 164 */     this.bearingX = left / paramTrueTypeGlyphProvider.oversample;
/* 165 */     this.bearingY = top / paramTrueTypeGlyphProvider.oversample;
/* 167 */     this.index = index;
/*     */   }
/*     */   
/*     */   public GlyphInfo info() {
/* 172 */     return this.info;
/*     */   }
/*     */   
/*     */   public BakedGlyph bake(UnbakedGlyph.Stitcher stitcher) {
/* 177 */     return stitcher.stitch(this.info, new GlyphBitmap() {
/*     */           public int getPixelWidth() {
/* 180 */             return TrueTypeGlyphProvider.Glyph.this.width;
/*     */           }
/*     */           
/*     */           public int getPixelHeight() {
/* 185 */             return TrueTypeGlyphProvider.Glyph.this.height;
/*     */           }
/*     */           
/*     */           public float getOversample() {
/* 190 */             return TrueTypeGlyphProvider.this.oversample;
/*     */           }
/*     */           
/*     */           public float getBearingLeft() {
/* 195 */             return TrueTypeGlyphProvider.Glyph.this.bearingX;
/*     */           }
/*     */           
/*     */           public float getBearingTop() {
/* 200 */             return TrueTypeGlyphProvider.Glyph.this.bearingY;
/*     */           }
/*     */           
/*     */           public void upload(int x, int y, GpuTexture texture) {
/* 205 */             FT_Face face = TrueTypeGlyphProvider.this.validateFontOpen();
/* 207 */             NativeImage image = new NativeImage(NativeImage.Format.LUMINANCE, TrueTypeGlyphProvider.Glyph.this.width, TrueTypeGlyphProvider.Glyph.this.height, false);
/*     */             try {
/* 208 */               if (image.copyFromFont(face, TrueTypeGlyphProvider.Glyph.this.index))
/* 209 */                 RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, image, 0, 0, x, y); 
/* 211 */               image.close();
/*     */             } catch (Throwable throwable) {
/*     */               try {
/*     */                 image.close();
/*     */               } catch (Throwable throwable1) {
/*     */                 throwable.addSuppressed(throwable1);
/*     */               } 
/*     */               throw throwable;
/*     */             } 
/*     */           }
/*     */           
/*     */           public boolean isColored() {
/* 216 */             return false;
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\font\TrueTypeGlyphProvider$Glyph.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */