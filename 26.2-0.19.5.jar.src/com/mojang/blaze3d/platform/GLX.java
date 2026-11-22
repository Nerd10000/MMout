/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.mojang.blaze3d.GLFWErrorCapture;
/*     */ import com.mojang.blaze3d.GLFWErrorScope;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Locale;
/*     */ import java.util.function.LongSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import org.lwjgl.Version;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.glfw.GLFWErrorCallback;
/*     */ import org.lwjgl.glfw.GLFWErrorCallbackI;
/*     */ import org.lwjgl.glfw.GLFWVidMode;
/*     */ import org.slf4j.Logger;
/*     */ import oshi.SystemInfo;
/*     */ import oshi.hardware.CentralProcessor;
/*     */ 
/*     */ public class GLX {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static int _getRefreshRate(Window window) {
/*  28 */     RenderSystem.assertOnRenderThread();
/*  29 */     long monitor = GLFW.glfwGetWindowMonitor(window.handle());
/*  30 */     if (monitor == 0L)
/*  31 */       monitor = GLFW.glfwGetPrimaryMonitor(); 
/*  33 */     GLFWVidMode videoMode = (monitor == 0L) ? null : GLFW.glfwGetVideoMode(monitor);
/*  34 */     return (videoMode == null) ? 0 : videoMode.refreshRate();
/*     */   }
/*     */   
/*     */   public static String _getLWJGLVersion() {
/*  38 */     return Version.getVersion();
/*     */   }
/*     */   
/*  41 */   private static int glfwPlatformType = 393221;
/*     */   
/*     */   private static String cpuInfo;
/*     */   
/*     */   public static LongSupplier _initGlfw() {
/*     */     LongSupplier timeSource;
/*  44 */     Window.checkGlfwError((error, description) -> {
/*     */           throw new IllegalStateException(String.format(Locale.ROOT, "GLFW error before init: [0x%X]%s", new Object[] { error, description }));
/*     */         });
/*  48 */     GLFWErrorCapture collectedErrors = new GLFWErrorCapture();
/*  50 */     GLFWErrorScope gLFWErrorScope = new GLFWErrorScope((GLFWErrorCallbackI)collectedErrors);
/*     */     try {
/*  51 */       if (GLFW.glfwPlatformSupported(393219) && GLFW.glfwPlatformSupported(393220))
/*  53 */         if (!SharedConstants.DEBUG_PREFER_WAYLAND)
/*  54 */           GLFW.glfwInitHint(327683, 393220);  
/*  58 */       if (GLFW.glfwInit()) {
/*  59 */         timeSource = (() -> (long)(GLFW.glfwGetTime() * 1.0E9D));
/*     */       } else {
/*  61 */         throw new IllegalStateException("Failed to initialize GLFW, errors: " + Joiner.on(",").join(collectedErrors));
/*     */       } 
/*  64 */       glfwPlatformType = GLFW.glfwGetPlatform();
/*  65 */       gLFWErrorScope.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         gLFWErrorScope.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*  67 */     for (GLFWErrorCapture.Error error : collectedErrors)
/*  68 */       LOGGER.error("GLFW error collected during initialization: {}", error); 
/*  71 */     return timeSource;
/*     */   }
/*     */   
/*     */   public static int getGlfwPlatform() {
/*  75 */     return glfwPlatformType;
/*     */   }
/*     */   
/*     */   public static void _setGlfwErrorCallback(GLFWErrorCallbackI onFullscreenError) {
/*  79 */     GLFWErrorCallback previousCallback = GLFW.glfwSetErrorCallback(onFullscreenError);
/*  80 */     if (previousCallback != null)
/*  81 */       previousCallback.free(); 
/*     */   }
/*     */   
/*     */   public static boolean _shouldClose(Window window) {
/*  87 */     return GLFW.glfwWindowShouldClose(window.handle());
/*     */   }
/*     */   
/*     */   public static String _getCpuInfo() {
/*  93 */     if (cpuInfo == null) {
/*  94 */       cpuInfo = "<unknown>";
/*     */       try {
/*  96 */         CentralProcessor processor = (new SystemInfo()).getHardware().getProcessor();
/*  97 */         cpuInfo = String.format(Locale.ROOT, "%dx %s", new Object[] { Integer.valueOf(processor.getLogicalProcessorCount()), processor.getProcessorIdentifier().getName() }).replaceAll("\\s+", " ");
/*  98 */       } catch (Throwable throwable) {}
/*     */     } 
/* 101 */     return cpuInfo;
/*     */   }
/*     */   
/*     */   public static <T> T make(Supplier<T> factory) {
/* 105 */     return factory.get();
/*     */   }
/*     */   
/*     */   public static int glfwBool(boolean value) {
/* 109 */     return value ? 1 : 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\GLX.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */