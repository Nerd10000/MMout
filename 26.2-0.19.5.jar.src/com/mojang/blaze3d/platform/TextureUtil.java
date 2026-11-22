/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.function.IntUnaryOperator;
/*     */ import net.minecraft.util.ARGB;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TextureUtil {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int MIN_MIPMAP_LEVEL = 0;
/*     */   
/*     */   private static final int DEFAULT_IMAGE_BUFFER_SIZE = 8192;
/*     */   
/*  35 */   private static final int[][] DIRECTIONS = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
/*     */   
/*     */   public static ByteBuffer readResource(InputStream inputStream) throws IOException {
/*  39 */     ReadableByteChannel channel = Channels.newChannel(inputStream);
/*  40 */     if (channel instanceof SeekableByteChannel) {
/*  40 */       SeekableByteChannel seekableChannel = (SeekableByteChannel)channel;
/*  41 */       return readResource(channel, (int)seekableChannel.size() + 1);
/*     */     } 
/*  43 */     return readResource(channel, 8192);
/*     */   }
/*     */   
/*     */   private static ByteBuffer readResource(ReadableByteChannel channel, int expectedSize) throws IOException {
/*  48 */     ByteBuffer buffer = MemoryUtil.memAlloc(expectedSize);
/*     */     try {
/*  50 */       while (channel.read(buffer) != -1) {
/*  51 */         if (!buffer.hasRemaining())
/*  52 */           buffer = MemoryUtil.memRealloc(buffer, buffer.capacity() * 2); 
/*     */       } 
/*  55 */       buffer.flip();
/*  56 */       return buffer;
/*  57 */     } catch (IOException e) {
/*  58 */       MemoryUtil.memFree(buffer);
/*  59 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void writeAsPNG(Path dir, String prefix, GpuTexture texture, int maxMipLevel, IntUnaryOperator pixelModifier) {
/*  64 */     RenderSystem.assertOnRenderThread();
/*  65 */     long bufferLength = 0L;
/*  66 */     for (int i = 0; i <= maxMipLevel; i++)
/*  67 */       bufferLength += texture.getFormat().blockSize() * texture.getWidth(i) * texture.getHeight(i); 
/*  69 */     if (bufferLength > 2147483647L)
/*  70 */       throw new IllegalArgumentException("Exporting textures larger than 2GB is not supported"); 
/*  74 */     if (texture.getFormat() != GpuFormat.RGBA8_UNORM)
/*  75 */       throw new IllegalArgumentException("Exporting textures other than RGBA8_UNORM is not supported"); 
/*  77 */     GpuBuffer buffer = RenderSystem.getDevice().createBuffer(() -> "Texture output buffer", 9, bufferLength);
/*  78 */     CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
/*  79 */     Runnable onCopyComplete = () -> {
/*     */         GpuBufferSlice.MappedView read = buffer.map(true, false);
/*     */         try {
/*     */           ByteBuffer data = read.data();
/*     */           Objects.requireNonNull(data);
/*     */           IntUnaryOperator decodeTexel = data::getInt;
/*     */           int offset = 0;
/*     */           for (int i = 0; i <= maxMipLevel; i++) {
/*     */             int mipWidth = texture.getWidth(i);
/*     */             int mipHeight = texture.getHeight(i);
/*     */             try {
/*     */               NativeImage image = new NativeImage(mipWidth, mipHeight, false);
/*     */               try {
/*     */                 for (int y = 0; y < mipHeight; y++) {
/*     */                   for (int x = 0; x < mipWidth; x++) {
/*     */                     int argb = decodeTexel.applyAsInt(offset + (x + y * mipWidth) * texture.getFormat().blockSize());
/*     */                     image.setPixelABGR(x, y, pixelModifier.applyAsInt(argb));
/*     */                   } 
/*     */                 } 
/*     */                 Path target = dir.resolve(prefix + "_" + prefix + ".png");
/*     */                 image.writeToFile(target);
/*     */                 LOGGER.debug("Exported png to: {}", target.toAbsolutePath());
/*     */                 image.close();
/*  87 */               } catch (Throwable t$) {
/*     */                 try {
/*     */                   image.close();
/*  87 */                 } catch (Throwable x2) {
/*     */                   t$.addSuppressed(x2);
/*     */                 } 
/*     */                 throw t$;
/*     */               } 
/*  97 */             } catch (IOException e) {
/*     */               LOGGER.debug("Unable to write: ", e);
/*     */             } 
/*     */             offset += texture.getFormat().blockSize() * mipWidth * mipHeight;
/*     */           } 
/*     */           if (read != null)
/*     */             read.close(); 
/*     */         } catch (Throwable t$) {
/*     */           if (read != null)
/*     */             try {
/*     */               read.close();
/*     */             } catch (Throwable x2) {
/*     */               t$.addSuppressed(x2);
/*     */             }  
/*     */           throw t$;
/*     */         } 
/*     */         buffer.close();
/*     */       };
/* 106 */     AtomicInteger completedCopies = new AtomicInteger();
/* 107 */     int offset = 0;
/* 108 */     for (int j = 0; j <= maxMipLevel; j++) {
/* 109 */       commandEncoder.copyTextureToBuffer(texture, buffer, offset, () -> {
/*     */             if (completedCopies.getAndIncrement() == maxMipLevel)
/*     */               onCopyComplete.run(); 
/* 109 */           }j);
/* 114 */       offset += texture.getFormat().blockSize() * texture.getWidth(j) * texture.getHeight(j);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Path getDebugTexturePath(Path root) {
/* 120 */     return root.resolve("screenshots").resolve("debug");
/*     */   }
/*     */   
/*     */   public static Path getDebugTexturePath() {
/* 124 */     return getDebugTexturePath(Path.of(".", new String[0]));
/*     */   }
/*     */   
/*     */   public static void solidify(NativeImage image) {
/* 128 */     int width = image.getWidth();
/* 129 */     int height = image.getHeight();
/* 132 */     int[] nearestColor = new int[width * height];
/* 133 */     int[] distances = new int[width * height];
/* 134 */     Arrays.fill(distances, 2147483647);
/* 137 */     IntArrayFIFOQueue queue = new IntArrayFIFOQueue();
/*     */     int x;
/* 140 */     for (x = 0; x < width; x++) {
/* 141 */       for (int y = 0; y < height; y++) {
/* 142 */         int color = image.getPixel(x, y);
/* 143 */         if (ARGB.alpha(color) != 0) {
/* 144 */           int packedCoordinates = pack(x, y, width);
/* 145 */           distances[packedCoordinates] = 0;
/* 146 */           nearestColor[packedCoordinates] = color;
/* 147 */           queue.enqueue(packedCoordinates);
/*     */         } 
/*     */       } 
/*     */     } 
/* 153 */     while (!queue.isEmpty()) {
/* 154 */       int packedCoordinates = queue.dequeueInt();
/* 155 */       int i = x(packedCoordinates, width);
/* 156 */       int y = y(packedCoordinates, width);
/* 157 */       for (int[] direction : DIRECTIONS) {
/* 158 */         int neighborX = i + direction[0];
/* 159 */         int neighborY = y + direction[1];
/* 160 */         int packedNeighborCoordinates = pack(neighborX, neighborY, width);
/* 161 */         if (neighborX >= 0 && neighborY >= 0 && neighborX < width && neighborY < height && 
/* 162 */           distances[packedNeighborCoordinates] > distances[packedCoordinates] + 1) {
/* 163 */           distances[packedNeighborCoordinates] = distances[packedCoordinates] + 1;
/* 164 */           nearestColor[packedNeighborCoordinates] = nearestColor[packedCoordinates];
/* 165 */           queue.enqueue(packedNeighborCoordinates);
/*     */         } 
/*     */       } 
/*     */     } 
/* 172 */     for (x = 0; x < width; x++) {
/* 173 */       for (int y = 0; y < height; y++) {
/* 174 */         int color = image.getPixel(x, y);
/* 175 */         if (ARGB.alpha(color) == 0) {
/* 177 */           image.setPixel(x, y, ARGB.color(0, nearestColor[pack(x, y, width)]));
/*     */         } else {
/* 179 */           image.setPixel(x, y, color);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void fillEmptyAreasWithDarkColor(NativeImage image) {
/* 186 */     int width = image.getWidth();
/* 187 */     int height = image.getHeight();
/* 189 */     int darkestColor = -1;
/* 190 */     int minBrightness = Integer.MAX_VALUE;
/* 192 */     for (int x = 0; x < width; x++) {
/* 193 */       for (int y = 0; y < height; y++) {
/* 194 */         int color = image.getPixel(x, y);
/* 195 */         int alpha = ARGB.alpha(color);
/* 196 */         if (alpha != 0) {
/* 197 */           int red = ARGB.red(color);
/* 198 */           int green = ARGB.green(color);
/* 199 */           int blue = ARGB.blue(color);
/* 200 */           int brightness = red + green + blue;
/* 201 */           if (brightness < minBrightness) {
/* 202 */             minBrightness = brightness;
/* 203 */             darkestColor = color;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 209 */     int darkRed = 3 * ARGB.red(darkestColor) / 4;
/* 210 */     int darkGreen = 3 * ARGB.green(darkestColor) / 4;
/* 211 */     int darkBlue = 3 * ARGB.blue(darkestColor) / 4;
/* 212 */     int darkenedColor = ARGB.color(0, darkRed, darkGreen, darkBlue);
/* 214 */     for (int i = 0; i < width; i++) {
/* 215 */       for (int y = 0; y < height; y++) {
/* 216 */         int color = image.getPixel(i, y);
/* 217 */         if (ARGB.alpha(color) == 0)
/* 218 */           image.setPixel(i, y, darkenedColor); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int pack(int x, int y, int width) {
/* 225 */     return x + y * width;
/*     */   }
/*     */   
/*     */   private static int x(int packed, int width) {
/* 229 */     return packed % width;
/*     */   }
/*     */   
/*     */   private static int y(int packed, int width) {
/* 233 */     return packed / width;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\TextureUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */