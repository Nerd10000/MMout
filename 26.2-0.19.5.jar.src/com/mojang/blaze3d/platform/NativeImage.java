/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import com.mojang.jtracy.MemoryPool;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntUnaryOperator;
/*     */ import net.minecraft.client.gui.font.providers.FreeTypeUtil;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.PngInfo;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.lwjgl.stb.STBIWriteCallback;
/*     */ import org.lwjgl.stb.STBImage;
/*     */ import org.lwjgl.stb.STBImageResize;
/*     */ import org.lwjgl.stb.STBImageWrite;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.lwjgl.util.freetype.FT_Bitmap;
/*     */ import org.lwjgl.util.freetype.FT_Face;
/*     */ import org.lwjgl.util.freetype.FT_GlyphSlot;
/*     */ import org.lwjgl.util.freetype.FreeType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class NativeImage implements AutoCloseable {
/*  42 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  43 */   private static final MemoryPool MEMORY_POOL = TracyClient.createMemoryPool("NativeImage");
/*     */   
/*  45 */   private static final Set<StandardOpenOption> OPEN_OPTIONS = EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
/*     */   
/*     */   private final Format format;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final boolean useStbFree;
/*     */   
/*     */   private long pixels;
/*     */   
/*     */   private final long size;
/*     */   
/*     */   public NativeImage(int width, int height, boolean zero) {
/*  56 */     this(Format.RGBA, width, height, zero);
/*     */   }
/*     */   
/*     */   public NativeImage(Format format, int width, int height, boolean zero) {
/*  60 */     if (width <= 0 || height <= 0)
/*  61 */       throw new IllegalArgumentException("Invalid texture size: " + width + "x" + height); 
/*  63 */     this.format = format;
/*  64 */     this.width = width;
/*  65 */     this.height = height;
/*  66 */     this.size = width * height * format.components();
/*  67 */     this.useStbFree = false;
/*  68 */     if (zero) {
/*  69 */       this.pixels = MemoryUtil.nmemCalloc(1L, this.size);
/*     */     } else {
/*  71 */       this.pixels = MemoryUtil.nmemAlloc(this.size);
/*     */     } 
/*  73 */     MEMORY_POOL.malloc(this.pixels, (int)this.size);
/*  74 */     if (this.pixels == 0L)
/*  75 */       throw new IllegalStateException("Unable to allocate texture of size " + width + "x" + height + " (" + format.components() + " channels)"); 
/*     */   }
/*     */   
/*     */   public NativeImage(Format format, int width, int height, boolean useStbFree, long pixels) {
/*  80 */     if (width <= 0 || height <= 0)
/*  81 */       throw new IllegalArgumentException("Invalid texture size: " + width + "x" + height); 
/*  83 */     this.format = format;
/*  84 */     this.width = width;
/*  85 */     this.height = height;
/*  86 */     this.useStbFree = useStbFree;
/*  87 */     this.pixels = pixels;
/*  88 */     this.size = width * height * format.components();
/*     */   }
/*     */   
/*     */   public String toString() {
/*  93 */     return "NativeImage[" + String.valueOf(this.format) + " " + this.width + "x" + this.height + "@" + this.pixels + (this.useStbFree ? "S" : "N") + "]";
/*     */   }
/*     */   
/*     */   private boolean isOutsideBounds(int x, int y) {
/*  97 */     return (x < 0 || x >= this.width || y < 0 || y >= this.height);
/*     */   }
/*     */   
/*     */   public static NativeImage read(InputStream inputStream) throws IOException {
/* 101 */     return read(Format.RGBA, inputStream);
/*     */   }
/*     */   
/*     */   public static NativeImage read(Format format, InputStream inputStream) throws IOException {
/* 106 */     ByteBuffer file = null;
/*     */     try {
/* 108 */       file = TextureUtil.readResource(inputStream);
/* 109 */       return read(format, file);
/*     */     } finally {
/* 111 */       MemoryUtil.memFree(file);
/* 112 */       IOUtils.closeQuietly(inputStream);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static NativeImage read(ByteBuffer bytes) throws IOException {
/* 117 */     return read(Format.RGBA, bytes);
/*     */   }
/*     */   
/*     */   public static NativeImage read(byte[] bytes) throws IOException {
/* 121 */     MemoryStack memoryStack = MemoryStack.stackGet();
/* 123 */     int bytesAvailable = memoryStack.getPointer();
/* 125 */     if (bytesAvailable < bytes.length) {
/* 126 */       ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length);
/*     */       try {
/* 128 */         return putAndRead(buffer, bytes);
/*     */       } finally {
/* 130 */         MemoryUtil.memFree(buffer);
/*     */       } 
/*     */     } 
/* 133 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 134 */       ByteBuffer buffer = stack.malloc(bytes.length);
/* 135 */       NativeImage nativeImage = putAndRead(buffer, bytes);
/* 136 */       if (stack != null)
/* 136 */         stack.close(); 
/*     */       return nativeImage;
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
/*     */   private static NativeImage putAndRead(ByteBuffer nativeBuffer, byte[] bytes) throws IOException {
/* 141 */     nativeBuffer.put(bytes);
/* 142 */     nativeBuffer.rewind();
/* 143 */     return read(nativeBuffer);
/*     */   }
/*     */   
/*     */   public static NativeImage read(Format format, ByteBuffer bytes) throws IOException {
/* 148 */     if (format != null && !format.supportedByStb())
/* 149 */       throw new UnsupportedOperationException("Don't know how to read format " + String.valueOf(format)); 
/* 151 */     if (MemoryUtil.memAddress(bytes) == 0L)
/* 152 */       throw new IllegalArgumentException("Invalid buffer"); 
/* 154 */     PngInfo.validateHeader(bytes);
/* 155 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 156 */       IntBuffer w = stack.mallocInt(1);
/* 157 */       IntBuffer h = stack.mallocInt(1);
/* 158 */       IntBuffer comp = stack.mallocInt(1);
/* 160 */       ByteBuffer pixels = STBImage.stbi_load_from_memory(bytes, w, h, comp, (format == null) ? 0 : format.components);
/* 161 */       if (pixels == null)
/* 162 */         throw new IOException("Could not load image: " + STBImage.stbi_failure_reason()); 
/* 164 */       long address = MemoryUtil.memAddress(pixels);
/* 165 */       MEMORY_POOL.malloc(address, pixels.limit());
/* 166 */       NativeImage nativeImage = new NativeImage((format == null) ? Format.getStbFormat(comp.get(0)) : format, w.get(0), h.get(0), true, address);
/* 167 */       if (stack != null)
/* 167 */         stack.close(); 
/*     */       return nativeImage;
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
/*     */   private void checkAllocated() {
/* 171 */     if (this.pixels == 0L)
/* 172 */       throw new IllegalStateException("Image is not allocated."); 
/*     */   }
/*     */   
/*     */   public void close() {
/* 180 */     if (this.pixels != 0L) {
/* 181 */       if (this.useStbFree) {
/* 182 */         STBImage.nstbi_image_free(this.pixels);
/*     */       } else {
/* 184 */         MemoryUtil.nmemFree(this.pixels);
/*     */       } 
/* 186 */       MEMORY_POOL.free(this.pixels);
/*     */     } 
/* 188 */     this.pixels = 0L;
/*     */   }
/*     */   
/*     */   public boolean isClosed() {
/* 192 */     return (this.pixels == 0L);
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 196 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 200 */     return this.height;
/*     */   }
/*     */   
/*     */   public Format format() {
/* 204 */     return this.format;
/*     */   }
/*     */   
/*     */   private int getPixelABGR(int x, int y) {
/* 211 */     if (this.format != Format.RGBA)
/* 212 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "getPixelRGBA only works on RGBA images; have %s", new Object[] { this.format })); 
/* 213 */     if (isOutsideBounds(x, y))
/* 214 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", new Object[] { Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(this.width), Integer.valueOf(this.height) })); 
/* 216 */     checkAllocated();
/* 217 */     long offset = (x + y * this.width) * 4L;
/* 218 */     return MemoryUtil.memGetInt(this.pixels + offset);
/*     */   }
/*     */   
/*     */   public int getPixel(int x, int y) {
/* 222 */     return ARGB.fromABGR(getPixelABGR(x, y));
/*     */   }
/*     */   
/*     */   public void setPixelABGR(int x, int y, int pixel) {
/* 229 */     if (this.format != Format.RGBA)
/* 230 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "setPixelRGBA only works on RGBA images; have %s", new Object[] { this.format })); 
/* 231 */     if (isOutsideBounds(x, y))
/* 232 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", new Object[] { Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(this.width), Integer.valueOf(this.height) })); 
/* 234 */     checkAllocated();
/* 235 */     long offset = (x + y * this.width) * 4L;
/* 236 */     MemoryUtil.memPutInt(this.pixels + offset, pixel);
/*     */   }
/*     */   
/*     */   public void setPixel(int x, int y, int pixel) {
/* 240 */     setPixelABGR(x, y, ARGB.toABGR(pixel));
/*     */   }
/*     */   
/*     */   public NativeImage mappedCopy(IntUnaryOperator function) {
/* 244 */     if (this.format != Format.RGBA)
/* 245 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "function application only works on RGBA images; have %s", new Object[] { this.format })); 
/* 247 */     checkAllocated();
/* 249 */     NativeImage result = new NativeImage(this.width, this.height, false);
/* 250 */     int pixelCount = this.width * this.height;
/* 251 */     IntBuffer sourceBuffer = MemoryUtil.memIntBuffer(this.pixels, pixelCount);
/* 252 */     IntBuffer targetBuffer = MemoryUtil.memIntBuffer(result.pixels, pixelCount);
/* 253 */     for (int i = 0; i < pixelCount; i++) {
/* 254 */       int pixel = ARGB.fromABGR(sourceBuffer.get(i));
/* 255 */       int modified = function.applyAsInt(pixel);
/* 256 */       targetBuffer.put(i, ARGB.toABGR(modified));
/*     */     } 
/* 258 */     return result;
/*     */   }
/*     */   
/*     */   public int[] getPixelsABGR() {
/* 264 */     if (this.format != Format.RGBA)
/* 265 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "getPixels only works on RGBA images; have %s", new Object[] { this.format })); 
/* 267 */     checkAllocated();
/* 268 */     int[] result = new int[this.width * this.height];
/* 269 */     MemoryUtil.memIntBuffer(this.pixels, this.width * this.height).get(result);
/* 270 */     return result;
/*     */   }
/*     */   
/*     */   public int[] getPixels() {
/* 274 */     int[] result = getPixelsABGR();
/* 275 */     for (int i = 0; i < result.length; i++)
/* 276 */       result[i] = ARGB.fromABGR(result[i]); 
/* 278 */     return result;
/*     */   }
/*     */   
/*     */   public byte getLuminanceOrAlpha(int x, int y) {
/* 283 */     if (!this.format.hasLuminanceOrAlpha())
/* 284 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "no luminance or alpha in %s", new Object[] { this.format })); 
/* 285 */     if (isOutsideBounds(x, y))
/* 286 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", new Object[] { Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(this.width), Integer.valueOf(this.height) })); 
/* 289 */     int offset = (x + y * this.width) * this.format.components() + this.format.luminanceOrAlphaOffset() / 8;
/* 290 */     return MemoryUtil.memGetByte(this.pixels + offset);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public int[] makePixelArray() {
/* 296 */     if (this.format != Format.RGBA)
/* 297 */       throw new UnsupportedOperationException("can only call makePixelArray for RGBA images."); 
/* 299 */     checkAllocated();
/* 300 */     int[] pixels = new int[getWidth() * getHeight()];
/* 301 */     for (int y = 0; y < getHeight(); y++) {
/* 302 */       for (int x = 0; x < getWidth(); x++)
/* 303 */         pixels[x + y * getWidth()] = getPixel(x, y); 
/*     */     } 
/* 306 */     return pixels;
/*     */   }
/*     */   
/*     */   public void writeToFile(File file) throws IOException {
/* 310 */     writeToFile(file.toPath());
/*     */   }
/*     */   
/*     */   public boolean copyFromFont(FT_Face face, int index) {
/* 314 */     if (this.format.components() != 1)
/* 315 */       throw new IllegalArgumentException("Can only write fonts into 1-component images."); 
/* 318 */     if (FreeTypeUtil.checkError(FreeType.FT_Load_Glyph(face, index, 4), "Loading glyph"))
/* 319 */       return false; 
/* 322 */     FT_GlyphSlot glyph = Objects.<FT_GlyphSlot>requireNonNull(face.glyph(), "Glyph not initialized");
/* 323 */     FT_Bitmap bitmap = glyph.bitmap();
/* 324 */     if (bitmap.pixel_mode() != 2)
/* 325 */       throw new IllegalStateException("Rendered glyph was not 8-bit grayscale"); 
/* 328 */     if (bitmap.width() != getWidth() || bitmap.rows() != getHeight())
/* 329 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "Glyph bitmap of size %sx%s does not match image of size: %sx%s", new Object[] { Integer.valueOf(bitmap.width()), Integer.valueOf(bitmap.rows()), Integer.valueOf(getWidth()), Integer.valueOf(getHeight()) })); 
/* 332 */     int bufferSize = bitmap.width() * bitmap.rows();
/* 333 */     ByteBuffer buffer = Objects.<ByteBuffer>requireNonNull(bitmap.buffer(bufferSize), "Glyph has no bitmap");
/* 334 */     MemoryUtil.memCopy(MemoryUtil.memAddress(buffer), this.pixels, bufferSize);
/* 336 */     return true;
/*     */   }
/*     */   
/*     */   private static class WriteCallback extends STBIWriteCallback {
/*     */     private final WritableByteChannel output;
/*     */     
/*     */     private IOException exception;
/*     */     
/*     */     private WriteCallback(WritableByteChannel output) {
/* 344 */       this.output = output;
/*     */     }
/*     */     
/*     */     public void invoke(long context, long data, int size) {
/* 349 */       ByteBuffer dataBuf = getData(data, size);
/*     */       try {
/* 351 */         this.output.write(dataBuf);
/* 352 */       } catch (IOException e) {
/* 353 */         this.exception = e;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void throwIfException() throws IOException {
/* 358 */       if (this.exception != null)
/* 359 */         throw this.exception; 
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeToFile(Path file) throws IOException {
/* 366 */     if (!this.format.supportedByStb())
/* 367 */       throw new UnsupportedOperationException("Don't know how to write format " + String.valueOf(this.format)); 
/* 369 */     checkAllocated();
/* 370 */     WritableByteChannel out = Files.newByteChannel(file, (Set)OPEN_OPTIONS, (FileAttribute<?>[])new FileAttribute[0]);
/*     */     try {
/* 371 */       if (!writeToChannel(out))
/* 372 */         throw new IOException("Could not write image to the PNG file \"" + String.valueOf(file.toAbsolutePath()) + "\": " + STBImage.stbi_failure_reason()); 
/* 374 */       if (out != null)
/* 374 */         out.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (out != null)
/*     */         try {
/*     */           out.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean writeToChannel(WritableByteChannel output) throws IOException {
/* 378 */     WriteCallback writer = new WriteCallback(output);
/*     */     try {
/* 380 */       int height = Math.min(getHeight(), Integer.MAX_VALUE / getWidth() / this.format.components());
/* 381 */       if (height < getHeight())
/* 382 */         LOGGER.warn("Dropping image height from {} to {} to fit the size into 32-bit signed int", Integer.valueOf(getHeight()), Integer.valueOf(height)); 
/* 384 */       if (STBImageWrite.nstbi_write_png_to_func(writer.address(), 0L, getWidth(), height, this.format.components(), this.pixels, 0) == 0)
/* 385 */         return false; 
/* 388 */       writer.throwIfException();
/* 389 */       return true;
/*     */     } finally {
/* 391 */       writer.free();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void copyFrom(NativeImage from) {
/* 398 */     if (from.format() != this.format)
/* 399 */       throw new UnsupportedOperationException("Image formats don't match."); 
/* 401 */     int components = this.format.components();
/* 402 */     checkAllocated();
/* 403 */     from.checkAllocated();
/* 404 */     if (this.width == from.width) {
/* 405 */       MemoryUtil.memCopy(from.pixels, this.pixels, Math.min(this.size, from.size));
/*     */     } else {
/* 407 */       int minWidth = Math.min(getWidth(), from.getWidth());
/* 408 */       int minHeight = Math.min(getHeight(), from.getHeight());
/* 409 */       for (int y = 0; y < minHeight; y++) {
/* 410 */         int fromOffset = y * from.getWidth() * components;
/* 411 */         int toOffset = y * getWidth() * components;
/* 412 */         MemoryUtil.memCopy(from.pixels + fromOffset, this.pixels + toOffset, minWidth);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fillRect(int xs, int ys, int width, int height, int pixel) {
/* 419 */     for (int y = ys; y < ys + height; y++) {
/* 420 */       for (int x = xs; x < xs + width; x++)
/* 421 */         setPixel(x, y, pixel); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void copyRect(int startX, int startY, int offsetX, int offsetY, int sizeX, int sizeY, boolean swapX, boolean swapY) {
/* 428 */     copyRect(this, startX, startY, startX + offsetX, startY + offsetY, sizeX, sizeY, swapX, swapY);
/*     */   }
/*     */   
/*     */   public void copyRect(NativeImage target, int sourceX, int sourceY, int targetX, int targetY, int sizeX, int sizeY, boolean swapX, boolean swapY) {
/* 434 */     for (int y = 0; y < sizeY; y++) {
/* 435 */       for (int x = 0; x < sizeX; x++) {
/* 436 */         int dx = swapX ? (sizeX - 1 - x) : x;
/* 437 */         int dy = swapY ? (sizeY - 1 - y) : y;
/* 438 */         int source = getPixelABGR(sourceX + x, sourceY + y);
/* 439 */         target.setPixelABGR(targetX + dx, targetY + dy, source);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resizeSubRectTo(int sourceX, int sourceY, int sizeX, int sizeY, NativeImage to) {
/* 447 */     checkAllocated();
/* 448 */     if (to.format() != this.format)
/* 449 */       throw new UnsupportedOperationException("resizeSubRectTo only works for images of the same format."); 
/* 451 */     int components = this.format.components();
/* 452 */     STBImageResize.nstbir_resize_uint8_linear(this.pixels + ((sourceX + sourceY * 
/* 453 */         getWidth()) * components), sizeX, sizeY, getWidth() * components, to.pixels, to
/* 454 */         .getWidth(), to.getHeight(), 0, components);
/*     */   }
/*     */   
/*     */   public void untrack() {
/* 460 */     DebugMemoryUntracker.untrack(this.pixels);
/*     */   }
/*     */   
/*     */   public long getPointer() {
/* 464 */     return this.pixels;
/*     */   }
/*     */   
/*     */   public ByteBuffer getPixelBytes() {
/* 468 */     return MemoryUtil.memByteBuffer(this.pixels, Math.toIntExact(this.size));
/*     */   }
/*     */   
/*     */   public Transparency computeTransparency(int x0, int y0, int x1, int y1) {
/* 472 */     checkAllocated();
/* 473 */     if (this.format != Format.RGBA)
/* 474 */       return Transparency.NONE; 
/* 476 */     if (x0 < 0 || y0 < 0 || x1 > this.width || y1 > this.height)
/* 477 */       throw new IllegalArgumentException("Cannot compute translucency out of bounds: [" + x0 + ", " + y0 + ", " + x1 + ", " + y1 + "] in " + this.width + "x" + this.height + " image"); 
/* 479 */     if (this.width * this.height * 4L > 2147483647L)
/* 480 */       throw new IllegalArgumentException("Image of size " + this.width + "x" + this.height + " is too large to compute translucency"); 
/* 482 */     boolean hasTransparentPixel = false;
/* 483 */     boolean hasTranslucentPixel = false;
/* 484 */     IntBuffer buffer = MemoryUtil.memIntBuffer(this.pixels, this.width * this.height);
/* 485 */     for (int y = y0; y < y1; y++) {
/* 486 */       for (int x = x0; x < x1; x++) {
/* 488 */         int alpha = ARGB.alpha(buffer.get(x + y * this.width));
/* 489 */         if (alpha == 0) {
/* 490 */           hasTransparentPixel = true;
/* 491 */         } else if (alpha != 255) {
/* 492 */           hasTranslucentPixel = true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 496 */     return Transparency.of(hasTransparentPixel, hasTranslucentPixel);
/*     */   }
/*     */   
/*     */   public Transparency computeTransparency() {
/* 500 */     return computeTransparency(0, 0, this.width, this.height);
/*     */   }
/*     */   
/*     */   public enum Format {
/* 505 */     RGBA(4, true, true, true, false, true, 0, 8, 16, 255, 24, true),
/* 511 */     RGB(3, true, true, true, false, false, 0, 8, 16, 255, 255, true),
/* 517 */     LUMINANCE_ALPHA(2, false, false, false, true, true, 255, 255, 255, 0, 8, true),
/* 523 */     LUMINANCE(1, false, false, false, true, false, 0, 0, 0, 0, 255, true);
/*     */     
/*     */     private final int components;
/*     */     
/*     */     private final boolean hasRed;
/*     */     
/*     */     private final boolean hasGreen;
/*     */     
/*     */     private final boolean hasBlue;
/*     */     
/*     */     private final boolean hasLuminance;
/*     */     
/*     */     private final boolean hasAlpha;
/*     */     
/*     */     private final int redOffset;
/*     */     
/*     */     private final int greenOffset;
/*     */     
/*     */     private final int blueOffset;
/*     */     
/*     */     private final int luminanceOffset;
/*     */     
/*     */     private final int alphaOffset;
/*     */     
/*     */     private final boolean supportedByStb;
/*     */     
/*     */     Format(int components, boolean hasRed, boolean hasGreen, boolean hasBlue, boolean hasLuminance, boolean hasAlpha, int redOffset, int greenOffset, int blueOffset, int luminanceOffset, int alphaOffset, boolean supportedByStb) {
/* 548 */       this.components = components;
/* 549 */       this.hasRed = hasRed;
/* 550 */       this.hasGreen = hasGreen;
/* 551 */       this.hasBlue = hasBlue;
/* 552 */       this.hasLuminance = hasLuminance;
/* 553 */       this.hasAlpha = hasAlpha;
/* 554 */       this.redOffset = redOffset;
/* 555 */       this.greenOffset = greenOffset;
/* 556 */       this.blueOffset = blueOffset;
/* 557 */       this.luminanceOffset = luminanceOffset;
/* 558 */       this.alphaOffset = alphaOffset;
/* 559 */       this.supportedByStb = supportedByStb;
/*     */     }
/*     */     
/*     */     public int components() {
/* 563 */       return this.components;
/*     */     }
/*     */     
/*     */     public boolean hasRed() {
/* 567 */       return this.hasRed;
/*     */     }
/*     */     
/*     */     public boolean hasGreen() {
/* 571 */       return this.hasGreen;
/*     */     }
/*     */     
/*     */     public boolean hasBlue() {
/* 575 */       return this.hasBlue;
/*     */     }
/*     */     
/*     */     public boolean hasLuminance() {
/* 579 */       return this.hasLuminance;
/*     */     }
/*     */     
/*     */     public boolean hasAlpha() {
/* 583 */       return this.hasAlpha;
/*     */     }
/*     */     
/*     */     public int redOffset() {
/* 587 */       return this.redOffset;
/*     */     }
/*     */     
/*     */     public int greenOffset() {
/* 591 */       return this.greenOffset;
/*     */     }
/*     */     
/*     */     public int blueOffset() {
/* 595 */       return this.blueOffset;
/*     */     }
/*     */     
/*     */     public int luminanceOffset() {
/* 599 */       return this.luminanceOffset;
/*     */     }
/*     */     
/*     */     public int alphaOffset() {
/* 603 */       return this.alphaOffset;
/*     */     }
/*     */     
/*     */     public boolean hasLuminanceOrRed() {
/* 607 */       return (this.hasLuminance || this.hasRed);
/*     */     }
/*     */     
/*     */     public boolean hasLuminanceOrGreen() {
/* 611 */       return (this.hasLuminance || this.hasGreen);
/*     */     }
/*     */     
/*     */     public boolean hasLuminanceOrBlue() {
/* 615 */       return (this.hasLuminance || this.hasBlue);
/*     */     }
/*     */     
/*     */     public boolean hasLuminanceOrAlpha() {
/* 619 */       return (this.hasLuminance || this.hasAlpha);
/*     */     }
/*     */     
/*     */     public int luminanceOrRedOffset() {
/* 623 */       return this.hasLuminance ? this.luminanceOffset : this.redOffset;
/*     */     }
/*     */     
/*     */     public int luminanceOrGreenOffset() {
/* 627 */       return this.hasLuminance ? this.luminanceOffset : this.greenOffset;
/*     */     }
/*     */     
/*     */     public int luminanceOrBlueOffset() {
/* 631 */       return this.hasLuminance ? this.luminanceOffset : this.blueOffset;
/*     */     }
/*     */     
/*     */     public int luminanceOrAlphaOffset() {
/* 635 */       return this.hasLuminance ? this.luminanceOffset : this.alphaOffset;
/*     */     }
/*     */     
/*     */     public boolean supportedByStb() {
/* 639 */       return this.supportedByStb;
/*     */     }
/*     */     
/*     */     private static Format getStbFormat(int i) {
/* 643 */       switch (i) {
/*     */         case 1:
/* 645 */           return LUMINANCE;
/*     */         case 2:
/* 647 */           return LUMINANCE_ALPHA;
/*     */         case 3:
/* 649 */           return RGB;
/*     */       } 
/* 652 */       return RGBA;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\NativeImage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */