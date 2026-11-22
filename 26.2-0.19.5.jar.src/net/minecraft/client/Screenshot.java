/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.File;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Screenshot {
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final String SCREENSHOT_DIR = "screenshots";
/*     */   
/*     */   public static void grab(File workDir, RenderTarget target, Consumer<Component> callback) {
/*  27 */     grab(workDir, null, target, 1, callback);
/*     */   }
/*     */   
/*     */   public static void grab(Minecraft minecraft, boolean debugPanoramaRequested) {
/*  31 */     if (debugPanoramaRequested && SharedConstants.DEBUG_PANORAMA_SCREENSHOT) {
/*  32 */       minecraft.showDebugChat(minecraft.grabPanoramixScreenshot(minecraft.gameDirectory));
/*     */     } else {
/*  34 */       grab(minecraft.gameDirectory, minecraft.gameRenderer.mainRenderTarget(), message -> minecraft.execute(()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void grab(File workDir, String forceName, RenderTarget target, int downscaleFactor, Consumer<Component> callback) {
/*  39 */     takeScreenshot(target, downscaleFactor, image -> {
/*     */           File file;
/*     */           File picDir = new File(workDir, "screenshots");
/*     */           picDir.mkdir();
/*     */           if (forceName == null) {
/*     */             file = getFile(picDir);
/*     */           } else {
/*     */             file = new File(picDir, forceName);
/*     */           } 
/*     */           Util.ioPool().execute(());
/*     */         });
/*     */   }
/*     */   
/*     */   public static void takeScreenshot(RenderTarget target, Consumer<NativeImage> callback) {
/*  65 */     takeScreenshot(target, 1, callback);
/*     */   }
/*     */   
/*     */   public static void takeScreenshot(RenderTarget target, int downscaleFactor, Consumer<NativeImage> callback) {
/*  69 */     int width = target.width;
/*  70 */     int height = target.height;
/*  72 */     GpuTexture sourceTexture = target.getColorTexture();
/*  73 */     if (sourceTexture == null)
/*  74 */       throw new IllegalStateException("Tried to capture screenshot of an incomplete framebuffer"); 
/*  76 */     if (width % downscaleFactor != 0 || height % downscaleFactor != 0)
/*  77 */       throw new IllegalArgumentException("Image size is not divisible by downscale factor"); 
/*  79 */     GpuBuffer buffer = RenderSystem.getDevice().createBuffer(() -> "Screenshot buffer", 9, width * height * sourceTexture.getFormat().blockSize());
/*  80 */     RenderSystem.getDevice().createCommandEncoder().copyTextureToBuffer(sourceTexture, buffer, 0L, () -> {
/*     */           GpuBufferSlice.MappedView read = buffer.map(true, false);
/*     */           try {
/*     */             int outputHeight = height / downscaleFactor;
/*     */             int outputWidth = width / downscaleFactor;
/*     */             NativeImage image = new NativeImage(outputWidth, outputHeight, false);
/*     */             for (int y = 0; y < outputHeight; y++) {
/*     */               for (int x = 0; x < outputWidth; x++) {
/*     */                 if (downscaleFactor == 1) {
/*     */                   int argb = read.data().getInt((x + y * width) * sourceTexture.getFormat().blockSize());
/*     */                   image.setPixelABGR(x, height - y - 1, argb | 0xFF000000);
/*     */                 } else {
/*     */                   int red = 0;
/*     */                   int green = 0;
/*     */                   int blue = 0;
/*     */                   for (int i = 0; i < downscaleFactor; i++) {
/*     */                     for (int j = 0; j < downscaleFactor; j++) {
/*     */                       int argb = read.data().getInt((x * downscaleFactor + i + (y * downscaleFactor + j) * width) * sourceTexture.getFormat().blockSize());
/*     */                       red += ARGB.red(argb);
/*     */                       green += ARGB.green(argb);
/*     */                       blue += ARGB.blue(argb);
/*     */                     } 
/*     */                   } 
/*     */                   int sampleCount = downscaleFactor * downscaleFactor;
/*     */                   image.setPixelABGR(x, outputHeight - y - 1, ARGB.color(255, red / sampleCount, green / sampleCount, blue / sampleCount));
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */             callback.accept(image);
/*     */             if (read != null)
/*     */               read.close(); 
/*  81 */           } catch (Throwable t$) {
/*     */             if (read != null)
/*     */               try {
/*     */                 read.close();
/*  81 */               } catch (Throwable x2) {
/*     */                 t$.addSuppressed(x2);
/*     */               }  
/*     */             throw t$;
/*     */           } 
/*     */           buffer.close();
/*     */         }0);
/*     */   }
/*     */   
/*     */   private static File getFile(File picDir) {
/* 118 */     String name = Util.getFilenameFormattedDateTime();
/* 120 */     for (int count = 1;; count++) {
/* 121 */       File file = new File(picDir, name + name + ".png");
/* 122 */       if (!file.exists())
/* 123 */         return file; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\Screenshot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */