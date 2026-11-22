/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import com.mojang.blaze3d.GLFWErrorCapture;
/*     */ import com.mojang.blaze3d.GLFWErrorScope;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.HexFormat;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.glfw.GLFWErrorCallbackI;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class Monitor extends Record {
/*     */   private final String monitorName;
/*     */   
/*     */   private final long monitor;
/*     */   
/*     */   private final List<VideoMode> videoModes;
/*     */   
/*     */   private final VideoMode currentMode;
/*     */   
/*     */   private final int x;
/*     */   
/*     */   private final int y;
/*     */   
/*     */   public Monitor(String monitorName, long monitor, List<VideoMode> videoModes, VideoMode currentMode, int x, int y) {
/*  18 */     this.monitorName = monitorName;
/*  18 */     this.monitor = monitor;
/*  18 */     this.videoModes = videoModes;
/*  18 */     this.currentMode = currentMode;
/*  18 */     this.x = x;
/*  18 */     this.y = y;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/platform/Monitor;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #18	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/platform/Monitor;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/platform/Monitor;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #18	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/platform/Monitor;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String monitorName() {
/*  18 */     return this.monitorName;
/*     */   }
/*     */   
/*     */   public long monitor() {
/*  18 */     return this.monitor;
/*     */   }
/*     */   
/*     */   public List<VideoMode> videoModes() {
/*  18 */     return this.videoModes;
/*     */   }
/*     */   
/*     */   public VideoMode currentMode() {
/*  18 */     return this.currentMode;
/*     */   }
/*     */   
/*     */   public int x() {
/*  18 */     return this.x;
/*     */   }
/*     */   
/*     */   public int y() {
/*  18 */     return this.y;
/*     */   }
/*     */   
/*  26 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  27 */   private static final HexFormat HEX_FORMAT = HexFormat.of().withUpperCase();
/*     */   
/*     */   public static Monitor tryCreate(long monitor) {
/*  30 */     GLFWErrorCapture glfwErrors = new GLFWErrorCapture();
/*     */     try {
/*  31 */       GLFWErrorScope gLFWErrorScope = new GLFWErrorScope((GLFWErrorCallbackI)glfwErrors);
/*     */     } finally {
/*  65 */       for (GLFWErrorCapture.Error error : glfwErrors)
/*  66 */         LOGGER.error("GLFW error collected during monitor 0x{} query: {}", HEX_FORMAT.toHexDigits(monitor), error); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String queryMonitorName(long monitor) {
/*  72 */     String monitorName = Objects.<String>requireNonNull(GLFW.glfwGetMonitorName(monitor), "unknown");
/*  73 */     return monitorName + "[0x" + monitorName + "]";
/*     */   }
/*     */   
/*     */   public VideoMode getPreferredVidMode(Optional<VideoMode> expectedMode) {
/*  77 */     if (expectedMode.isPresent()) {
/*  78 */       VideoMode videoMode = expectedMode.get();
/*  80 */       for (VideoMode mode : this.videoModes) {
/*  81 */         if (mode.equals(videoMode))
/*  82 */           return mode; 
/*     */       } 
/*     */     } 
/*  86 */     return this.currentMode;
/*     */   }
/*     */   
/*     */   public int indexOfMode(VideoMode videoMode) {
/*  90 */     return this.videoModes.indexOf(videoMode);
/*     */   }
/*     */   
/*     */   public VideoMode mode(int mode) {
/*  94 */     return this.videoModes.get(mode);
/*     */   }
/*     */   
/*     */   public int modeCount() {
/*  98 */     return this.videoModes.size();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 103 */     return String.format(Locale.ROOT, "%s(%s at (%d,%d))", new Object[] { this.monitorName, this.currentMode, Integer.valueOf(this.x), Integer.valueOf(this.y) });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\Monitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */