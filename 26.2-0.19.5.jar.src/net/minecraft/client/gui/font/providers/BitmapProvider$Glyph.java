/*     */ package net.minecraft.client.gui.font.providers;
/*     */ 
/*     */ import com.mojang.blaze3d.font.GlyphBitmap;
/*     */ import com.mojang.blaze3d.font.GlyphInfo;
/*     */ import com.mojang.blaze3d.font.UnbakedGlyph;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*     */ 
/*     */ final class Glyph extends Record implements UnbakedGlyph {
/*     */   private final float scale;
/*     */   
/*     */   private final BitmapProvider.ImageDataHolder imageData;
/*     */   
/*     */   private final int offsetX;
/*     */   
/*     */   private final int offsetY;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final int advance;
/*     */   
/*     */   private final int ascent;
/*     */   
/*     */   private Glyph(float scale, BitmapProvider.ImageDataHolder imageData, int offsetX, int offsetY, int width, int height, int advance, int ascent) {
/* 217 */     this.scale = scale;
/* 217 */     this.imageData = imageData;
/* 217 */     this.offsetX = offsetX;
/* 217 */     this.offsetY = offsetY;
/* 217 */     this.width = width;
/* 217 */     this.height = height;
/* 217 */     this.advance = advance;
/* 217 */     this.ascent = ascent;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/BitmapProvider$Glyph;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #217	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/BitmapProvider$Glyph;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/BitmapProvider$Glyph;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #217	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/BitmapProvider$Glyph;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/BitmapProvider$Glyph;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #217	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/BitmapProvider$Glyph;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float scale() {
/* 217 */     return this.scale;
/*     */   }
/*     */   
/*     */   public BitmapProvider.ImageDataHolder imageData() {
/* 217 */     return this.imageData;
/*     */   }
/*     */   
/*     */   public int offsetX() {
/* 217 */     return this.offsetX;
/*     */   }
/*     */   
/*     */   public int offsetY() {
/* 217 */     return this.offsetY;
/*     */   }
/*     */   
/*     */   public int width() {
/* 217 */     return this.width;
/*     */   }
/*     */   
/*     */   public int height() {
/* 217 */     return this.height;
/*     */   }
/*     */   
/*     */   public int advance() {
/* 217 */     return this.advance;
/*     */   }
/*     */   
/*     */   public int ascent() {
/* 217 */     return this.ascent;
/*     */   }
/*     */   
/*     */   public GlyphInfo info() {
/* 230 */     return GlyphInfo.simple(this.advance);
/*     */   }
/*     */   
/*     */   public BakedGlyph bake(UnbakedGlyph.Stitcher stitcher) {
/* 235 */     return stitcher.stitch(info(), new GlyphBitmap() {
/*     */           public float getOversample() {
/* 238 */             return 1.0F / BitmapProvider.Glyph.this.scale;
/*     */           }
/*     */           
/*     */           public int getPixelWidth() {
/* 243 */             return BitmapProvider.Glyph.this.width;
/*     */           }
/*     */           
/*     */           public int getPixelHeight() {
/* 248 */             return BitmapProvider.Glyph.this.height;
/*     */           }
/*     */           
/*     */           public float getBearingTop() {
/* 253 */             return BitmapProvider.Glyph.this.ascent;
/*     */           }
/*     */           
/*     */           public void upload(int x, int y, GpuTexture texture) {
/* 258 */             RenderSystem.getDevice().createCommandEncoder().copyBufferToTexture(BitmapProvider.Glyph.this.imageData.gpuData().slice(), BitmapProvider.Glyph.this.offsetX, BitmapProvider.Glyph.this.offsetY, BitmapProvider.Glyph.this.imageData.image.getWidth(), BitmapProvider.Glyph.this.imageData.image.getHeight(), texture, x, y, BitmapProvider.Glyph.this.width, BitmapProvider.Glyph.this.height, 0, 0);
/*     */           }
/*     */           
/*     */           public boolean isColored() {
/* 263 */             return (BitmapProvider.Glyph.this.imageData.image.format().components() > 1);
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\BitmapProvider$Glyph.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */