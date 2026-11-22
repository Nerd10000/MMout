/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.systems.DeviceInfo;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public final class Preparations {
/*     */   private final List<Pattern> rendererPatterns;
/*     */   
/*     */   private final List<Pattern> versionPatterns;
/*     */   
/*     */   private final List<Pattern> vendorPatterns;
/*     */   
/*     */   private Preparations(List<Pattern> rendererPatterns, List<Pattern> versionPatterns, List<Pattern> vendorPatterns) {
/* 123 */     this.rendererPatterns = rendererPatterns;
/* 124 */     this.versionPatterns = versionPatterns;
/* 125 */     this.vendorPatterns = vendorPatterns;
/*     */   }
/*     */   
/*     */   private static String matchAny(List<Pattern> patterns, String input) {
/* 129 */     List<String> allMatches = Lists.newArrayList();
/* 130 */     for (Pattern pattern : patterns) {
/* 131 */       Matcher matcher = pattern.matcher(input);
/* 132 */       while (matcher.find())
/* 133 */         allMatches.add(matcher.group()); 
/*     */     } 
/* 136 */     return String.join(", ", (Iterable)allMatches);
/*     */   }
/*     */   
/*     */   private ImmutableMap<String, String> apply() {
/* 140 */     ImmutableMap.Builder<String, String> map = new ImmutableMap.Builder();
/* 141 */     GpuDevice device = RenderSystem.getDevice();
/* 142 */     DeviceInfo deviceInfo = device.getDeviceInfo();
/* 143 */     if (deviceInfo.backendName().equals("OpenGL")) {
/* 144 */       String rendererFails = matchAny(this.rendererPatterns, deviceInfo.name());
/* 145 */       if (!rendererFails.isEmpty())
/* 146 */         map.put("renderer", rendererFails); 
/* 149 */       String versionFails = matchAny(this.versionPatterns, deviceInfo.driverInfo());
/* 150 */       if (!versionFails.isEmpty())
/* 151 */         map.put("version", versionFails); 
/* 154 */       String vendorFails = matchAny(this.vendorPatterns, deviceInfo.vendorName());
/* 155 */       if (!vendorFails.isEmpty())
/* 156 */         map.put("vendor", vendorFails); 
/*     */     } 
/* 160 */     return map.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\GpuWarnlistManager$Preparations.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */