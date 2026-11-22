/*     */ package com.mojang.blaze3d.font;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import it.unimi.dsi.fastutil.ints.IntArraySet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.font.CodepointMap;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*     */ import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
/*     */ import net.minecraft.client.gui.font.providers.FreeTypeUtil;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.lwjgl.util.freetype.FT_Bitmap;
/*     */ import org.lwjgl.util.freetype.FT_Face;
/*     */ import org.lwjgl.util.freetype.FT_GlyphSlot;
/*     */ import org.lwjgl.util.freetype.FT_Vector;
/*     */ import org.lwjgl.util.freetype.FreeType;
/*     */ 
/*     */ public class TrueTypeGlyphProvider implements GlyphProvider {
/*     */   private ByteBuffer fontMemory;
/*     */   
/*     */   private FT_Face face;
/*     */   
/*     */   private final float oversample;
/*     */   
/*     */   private final CodepointMap<GlyphEntry> glyphs;
/*     */   
/*     */   public TrueTypeGlyphProvider(ByteBuffer fontMemory, FT_Face face, float size, float oversample, float shiftX, float shiftY, String skip) {
/*  30 */     this.glyphs = new CodepointMap(x$0 -> new GlyphEntry[x$0], x$0 -> new GlyphEntry[x$0][]);
/*  33 */     this.fontMemory = fontMemory;
/*  34 */     this.face = face;
/*  36 */     this.oversample = oversample;
/*  38 */     IntArraySet intArraySet = new IntArraySet();
/*  39 */     Objects.requireNonNull(intArraySet);
/*  39 */     skip.codePoints().forEach(intArraySet::add);
/*  41 */     int pixelsPerEm = Math.round(size * oversample);
/*  42 */     FreeType.FT_Set_Pixel_Sizes(face, pixelsPerEm, pixelsPerEm);
/*  44 */     float transformX = shiftX * oversample;
/*  45 */     float transformY = -shiftY * oversample;
/*  46 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/*  47 */       FT_Vector vector = FreeTypeUtil.setVector(FT_Vector.malloc(stack), transformX, transformY);
/*  48 */       FreeType.FT_Set_Transform(face, null, vector);
/*  50 */       IntBuffer indexPtr = stack.mallocInt(1);
/*  51 */       int codepoint = (int)FreeType.FT_Get_First_Char(face, indexPtr);
/*     */       while (true) {
/*  53 */         int index = indexPtr.get(0);
/*  54 */         if (index == 0)
/*     */           break; 
/*  58 */         if (!intArraySet.contains(codepoint))
/*  59 */           this.glyphs.put(codepoint, new GlyphEntry(index)); 
/*  62 */         codepoint = (int)FreeType.FT_Get_Next_Char(face, codepoint, indexPtr);
/*     */       } 
/*  64 */       if (stack != null)
/*  64 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public UnbakedGlyph getGlyph(int codepoint) {
/*  69 */     GlyphEntry entry = (GlyphEntry)this.glyphs.get(codepoint);
/*  70 */     return (entry != null) ? getOrLoadGlyphInfo(codepoint, entry) : null;
/*     */   }
/*     */   
/*     */   private UnbakedGlyph getOrLoadGlyphInfo(int codepoint, GlyphEntry entry) {
/*  74 */     UnbakedGlyph result = entry.glyph;
/*  75 */     if (result == null) {
/*  76 */       FT_Face face = validateFontOpen();
/*  77 */       synchronized (face) {
/*  79 */         result = entry.glyph;
/*  80 */         if (result == null) {
/*  81 */           result = loadGlyph(codepoint, face, entry.index);
/*  82 */           entry.glyph = result;
/*     */         } 
/*     */       } 
/*     */     } 
/*  87 */     return result;
/*     */   }
/*     */   
/*     */   private UnbakedGlyph loadGlyph(int codepoint, FT_Face face, int index) {
/*  93 */     int errorCode = FreeType.FT_Load_Glyph(face, index, 4194312);
/*  94 */     if (errorCode != 0)
/*  95 */       FreeTypeUtil.assertError(errorCode, String.format(Locale.ROOT, "Loading glyph U+%06X", new Object[] { Integer.valueOf(codepoint) })); 
/*  97 */     FT_GlyphSlot glyph = face.glyph();
/*  98 */     if (glyph == null)
/*  99 */       throw new NullPointerException(String.format(Locale.ROOT, "Glyph U+%06X not initialized", new Object[] { Integer.valueOf(codepoint) })); 
/* 102 */     float scaledAdvance = FreeTypeUtil.x(glyph.advance());
/* 104 */     FT_Bitmap bitmap = glyph.bitmap();
/* 105 */     int left = glyph.bitmap_left();
/* 106 */     int top = glyph.bitmap_top();
/* 107 */     int width = bitmap.width();
/* 108 */     int height = bitmap.rows();
/* 110 */     if (width <= 0 || height <= 0)
/* 111 */       return (UnbakedGlyph)new EmptyGlyph(scaledAdvance / this.oversample); 
/* 114 */     return new Glyph(left, top, width, height, scaledAdvance, index);
/*     */   }
/*     */   
/*     */   private FT_Face validateFontOpen() {
/* 118 */     if (this.fontMemory == null || this.face == null)
/* 119 */       throw new IllegalStateException("Provider already closed"); 
/* 121 */     return this.face;
/*     */   }
/*     */   
/*     */   public void close() {
/* 126 */     if (this.face != null) {
/* 127 */       synchronized (FreeTypeUtil.LIBRARY_LOCK) {
/* 128 */         FreeTypeUtil.checkError(FreeType.FT_Done_Face(this.face), "Deleting face");
/*     */       } 
/* 130 */       this.face = null;
/*     */     } 
/* 132 */     MemoryUtil.memFree(this.fontMemory);
/* 133 */     this.fontMemory = null;
/*     */   }
/*     */   
/*     */   public IntSet getSupportedGlyphs() {
/* 138 */     return this.glyphs.keySet();
/*     */   }
/*     */   
/*     */   private static class GlyphEntry {
/*     */     private final int index;
/*     */     
/*     */     private volatile UnbakedGlyph glyph;
/*     */     
/*     */     private GlyphEntry(int index) {
/* 146 */       this.index = index;
/*     */     }
/*     */   }
/*     */   
/*     */   private class Glyph implements UnbakedGlyph {
/*     */     private final int width;
/*     */     
/*     */     private final int height;
/*     */     
/*     */     private final float bearingX;
/*     */     
/*     */     private final float bearingY;
/*     */     
/*     */     private final GlyphInfo info;
/*     */     
/*     */     private final int index;
/*     */     
/*     */     private Glyph(float left, float top, int width, int height, float advance, int index) {
/* 159 */       this.width = width;
/* 160 */       this.height = height;
/* 162 */       this.info = GlyphInfo.simple(advance / TrueTypeGlyphProvider.this.oversample);
/* 164 */       this.bearingX = left / TrueTypeGlyphProvider.this.oversample;
/* 165 */       this.bearingY = top / TrueTypeGlyphProvider.this.oversample;
/* 167 */       this.index = index;
/*     */     }
/*     */     
/*     */     public GlyphInfo info() {
/* 172 */       return this.info;
/*     */     }
/*     */     
/*     */     public BakedGlyph bake(UnbakedGlyph.Stitcher stitcher) {
/* 177 */       return stitcher.stitch(this.info, new GlyphBitmap() {
/*     */             public int getPixelWidth() {
/* 180 */               return TrueTypeGlyphProvider.Glyph.this.width;
/*     */             }
/*     */             
/*     */             public int getPixelHeight() {
/* 185 */               return TrueTypeGlyphProvider.Glyph.this.height;
/*     */             }
/*     */             
/*     */             public float getOversample() {
/* 190 */               return TrueTypeGlyphProvider.this.oversample;
/*     */             }
/*     */             
/*     */             public float getBearingLeft() {
/* 195 */               return TrueTypeGlyphProvider.Glyph.this.bearingX;
/*     */             }
/*     */             
/*     */             public float getBearingTop() {
/* 200 */               return TrueTypeGlyphProvider.Glyph.this.bearingY;
/*     */             }
/*     */             
/*     */             public void upload(int x, int y, GpuTexture texture) {
/* 205 */               FT_Face face = TrueTypeGlyphProvider.this.validateFontOpen();
/* 207 */               NativeImage image = new NativeImage(NativeImage.Format.LUMINANCE, TrueTypeGlyphProvider.Glyph.this.width, TrueTypeGlyphProvider.Glyph.this.height, false);
/*     */               try {
/* 208 */                 if (image.copyFromFont(face, TrueTypeGlyphProvider.Glyph.this.index))
/* 209 */                   RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, image, 0, 0, x, y); 
/* 211 */                 image.close();
/*     */               } catch (Throwable throwable) {
/*     */                 try {
/*     */                   image.close();
/*     */                 } catch (Throwable throwable1) {
/*     */                   throwable.addSuppressed(throwable1);
/*     */                 } 
/*     */                 throw throwable;
/*     */               } 
/*     */             }
/*     */             
/*     */             public boolean isColored() {
/* 216 */               return false;
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements GlyphBitmap {
/*     */     public int getPixelWidth() {
/*     */       return TrueTypeGlyphProvider.Glyph.this.width;
/*     */     }
/*     */     
/*     */     public int getPixelHeight() {
/*     */       return TrueTypeGlyphProvider.Glyph.this.height;
/*     */     }
/*     */     
/*     */     public float getOversample() {
/*     */       return TrueTypeGlyphProvider.this.oversample;
/*     */     }
/*     */     
/*     */     public float getBearingLeft() {
/*     */       return TrueTypeGlyphProvider.Glyph.this.bearingX;
/*     */     }
/*     */     
/*     */     public float getBearingTop() {
/*     */       return TrueTypeGlyphProvider.Glyph.this.bearingY;
/*     */     }
/*     */     
/*     */     public void upload(int x, int y, GpuTexture texture) {
/*     */       FT_Face face = TrueTypeGlyphProvider.this.validateFontOpen();
/*     */       NativeImage image = new NativeImage(NativeImage.Format.LUMINANCE, TrueTypeGlyphProvider.Glyph.this.width, TrueTypeGlyphProvider.Glyph.this.height, false);
/*     */       try {
/*     */         if (image.copyFromFont(face, TrueTypeGlyphProvider.Glyph.this.index))
/*     */           RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, image, 0, 0, x, y); 
/*     */         image.close();
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           image.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean isColored() {
/* 216 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\font\TrueTypeGlyphProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */