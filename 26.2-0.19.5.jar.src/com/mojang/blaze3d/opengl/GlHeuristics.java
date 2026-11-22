/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.DeviceFeatures;
/*     */ import com.mojang.blaze3d.systems.DeviceInfo;
/*     */ import com.mojang.blaze3d.systems.DeviceLimits;
/*     */ import com.mojang.blaze3d.systems.DeviceType;
/*     */ import com.mojang.blaze3d.systems.HintsAndWorkarounds;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import net.minecraft.util.Util;
/*     */ import org.lwjgl.opengl.GL33C;
/*     */ import org.lwjgl.opengl.GLCapabilities;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GlHeuristics {
/*  22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  24 */   private static final List<String> DEVICE_NAMES_THAT_IMPLY_CPU = List.of("mesa offscreen", "llvmpipe");
/*     */   
/*  28 */   private static final List<String> DEVICE_NAMES_THAT_IMPLY_VIRTUAL = List.of("virtgl");
/*     */   
/*     */   private final boolean isGlOnDx12;
/*     */   
/*     */   private final boolean isAmd;
/*     */   
/*     */   GlHeuristics(String deviceName) {
/*  37 */     this.isGlOnDx12 = isGlOnDx12(deviceName);
/*  38 */     this.isAmd = isAmd(deviceName);
/*     */   }
/*     */   
/*     */   public boolean isGlOnDx12() {
/*  42 */     return this.isGlOnDx12;
/*     */   }
/*     */   
/*     */   public boolean isAmd() {
/*  46 */     return this.isAmd;
/*     */   }
/*     */   
/*     */   private static boolean isGlOnDx12(String deviceName) {
/*  50 */     boolean isWindowsArm64 = (Util.getPlatform() == Util.OS.WINDOWS && Util.isAarch64());
/*  51 */     return (isWindowsArm64 || deviceName.startsWith("D3D12"));
/*     */   }
/*     */   
/*     */   private static boolean isAmd(String deviceName) {
/*  55 */     return deviceName.contains("AMD");
/*     */   }
/*     */   
/*     */   private static int getMaxSupportedTextureSize() {
/*  59 */     int maxReported = GlStateManager._getInteger(3379);
/*  60 */     for (int texSize = Math.max(32768, maxReported); texSize >= 1024; texSize >>= 1) {
/*  61 */       GlStateManager._texImage2D(32868, 0, 6408, texSize, texSize, 0, 6408, 5121, null);
/*  62 */       int width = GlStateManager._getTexLevelParameter(32868, 0, 4096);
/*  63 */       if (width != 0)
/*  64 */         return texSize; 
/*     */     } 
/*  67 */     int maxSupportedTextureSize = Math.max(maxReported, 1024);
/*  68 */     LOGGER.info("Failed to determine maximum texture size by probing, trying GL_MAX_TEXTURE_SIZE = {}", Integer.valueOf(maxSupportedTextureSize));
/*  69 */     return maxSupportedTextureSize;
/*     */   }
/*     */   
/*     */   public DeviceInfo createDeviceInfo(GLCapabilities capabilities, int maxSupportedAnisotropy, Set<String> enabledExtensions) {
/*  73 */     String renderer = GlStateManager._getString(7937);
/*  74 */     String vendor = GlStateManager._getString(7936);
/*  75 */     return new DeviceInfo(renderer, vendor, 
/*     */         
/*  78 */         GlStateManager._getString(7938), capabilities.GL_ARB_clip_control, "OpenGL", 1.0F, new DeviceLimits(maxSupportedAnisotropy, 
/*     */           
/*  84 */           GL33C.glGetInteger(35380), 
/*  85 */           getMaxSupportedTextureSize(), Long.MAX_VALUE, 0, 
/*     */           
/*  88 */           GL33C.glGetInteger(34852)), new DeviceFeatures(enabledExtensions
/*     */           
/*  91 */           .contains("GL_ARB_shader_draw_parameters"), false, true, enabledExtensions
/*     */           
/*  94 */           .contains("GL_ARB_multi_draw_indirect"), enabledExtensions
/*  95 */           .contains("GL_ARB_draw_indirect"), enabledExtensions
/*  96 */           .contains("GL_ARB_base_instance"), enabledExtensions
/*  97 */           .contains("GL_ARB_buffer_storage")), 
/*     */         
/*  99 */         Collections.unmodifiableSet(enabledExtensions), new HintsAndWorkarounds(
/*     */           
/* 101 */           isGlOnDx12(), 
/* 102 */           isAmd()), 
/*     */         
/* 104 */         guessDeviceType(renderer.toLowerCase(Locale.ROOT), vendor.toLowerCase(Locale.ROOT)));
/*     */   }
/*     */   
/*     */   private DeviceType guessDeviceType(String renderer, String vendor) {
/* 109 */     if (vendor.contains("intel")) {
/* 110 */       if (renderer.contains("arc"))
/* 112 */         return DeviceType.DISCRETE; 
/* 115 */       return DeviceType.INTEGRATED;
/*     */     } 
/* 117 */     for (String string : DEVICE_NAMES_THAT_IMPLY_CPU) {
/* 118 */       if (renderer.contains(string))
/* 119 */         return DeviceType.CPU; 
/*     */     } 
/* 122 */     for (String string : DEVICE_NAMES_THAT_IMPLY_VIRTUAL) {
/* 123 */       if (renderer.contains(string))
/* 124 */         return DeviceType.VIRTUAL; 
/*     */     } 
/* 127 */     return DeviceType.OTHER;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlHeuristics.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */