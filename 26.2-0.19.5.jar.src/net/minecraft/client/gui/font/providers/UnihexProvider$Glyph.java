/*     */ package net.minecraft.client.gui.font.providers;
/*     */ 
/*     */ import com.mojang.blaze3d.font.GlyphBitmap;
/*     */ import com.mojang.blaze3d.font.GlyphInfo;
/*     */ import com.mojang.blaze3d.font.UnbakedGlyph;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import java.nio.IntBuffer;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ final class Glyph extends Record implements UnbakedGlyph {
/*     */   private final UnihexProvider.LineData contents;
/*     */   
/*     */   private final int left;
/*     */   
/*     */   private final int right;
/*     */   
/*     */   private Glyph(UnihexProvider.LineData contents, int left, int right) {
/* 335 */     this.contents = contents;
/* 335 */     this.left = left;
/* 335 */     this.right = right;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$Glyph;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #335	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$Glyph;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$Glyph;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #335	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$Glyph;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$Glyph;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #335	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$Glyph;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public UnihexProvider.LineData contents() {
/* 335 */     return this.contents;
/*     */   }
/*     */   
/*     */   public int left() {
/* 335 */     return this.left;
/*     */   }
/*     */   
/*     */   public int right() {
/* 335 */     return this.right;
/*     */   }
/*     */   
/*     */   public int width() {
/* 337 */     return this.right - this.left + 1;
/*     */   }
/*     */   
/*     */   public GlyphInfo info() {
/* 342 */     return new GlyphInfo() {
/*     */         public float getAdvance() {
/* 345 */           return UnihexProvider.Glyph.this.width() / 2.0F + 1.0F;
/*     */         }
/*     */         
/*     */         public float getShadowOffset() {
/* 350 */           return 0.5F;
/*     */         }
/*     */         
/*     */         public float getBoldOffset() {
/* 355 */           return 0.5F;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public BakedGlyph bake(UnbakedGlyph.Stitcher stitcher) {
/* 362 */     return stitcher.stitch(info(), new GlyphBitmap() {
/*     */           public float getOversample() {
/* 365 */             return 2.0F;
/*     */           }
/*     */           
/*     */           public int getPixelWidth() {
/* 370 */             return UnihexProvider.Glyph.this.width();
/*     */           }
/*     */           
/*     */           public int getPixelHeight() {
/* 375 */             return 16;
/*     */           }
/*     */           
/*     */           public void upload(int x, int y, GpuTexture texture) {
/* 380 */             IntBuffer targetBuffer = MemoryUtil.memAllocInt(UnihexProvider.Glyph.this.width() * 16);
/* 381 */             UnihexProvider.unpackBitsToBytes(targetBuffer, UnihexProvider.Glyph.this.contents, UnihexProvider.Glyph.this.left, UnihexProvider.Glyph.this.right);
/* 382 */             targetBuffer.rewind();
/* 383 */             RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, MemoryUtil.memByteBuffer(targetBuffer), 0, 0, x, y, UnihexProvider.Glyph.this.width(), 16);
/* 384 */             MemoryUtil.memFree(targetBuffer);
/*     */           }
/*     */           
/*     */           public boolean isColored() {
/* 390 */             return true;
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\UnihexProvider$Glyph.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */