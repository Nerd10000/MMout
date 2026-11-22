/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.blaze3d.systems.DeviceInfo;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GpuWarnlistManager extends SimplePreparableReloadListener<GpuWarnlistManager.Preparations> {
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  29 */   private static final Identifier GPU_WARNLIST_LOCATION = Identifier.withDefaultNamespace("gpu_warnlist.json");
/*     */   
/*  31 */   private ImmutableMap<String, String> warnings = ImmutableMap.of();
/*     */   
/*     */   private boolean showWarning;
/*     */   
/*     */   private boolean warningDismissed;
/*     */   
/*     */   public boolean hasWarnings() {
/*  36 */     return !this.warnings.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean willShowWarning() {
/*  40 */     return (hasWarnings() && !this.warningDismissed);
/*     */   }
/*     */   
/*     */   public void showWarning() {
/*  44 */     this.showWarning = true;
/*     */   }
/*     */   
/*     */   public void dismissWarning() {
/*  48 */     this.warningDismissed = true;
/*     */   }
/*     */   
/*     */   public boolean isShowingWarning() {
/*  52 */     return (this.showWarning && !this.warningDismissed);
/*     */   }
/*     */   
/*     */   public void resetWarnings() {
/*  56 */     this.showWarning = false;
/*  57 */     this.warningDismissed = false;
/*     */   }
/*     */   
/*     */   public String getRendererWarnings() {
/*  61 */     return (String)this.warnings.get("renderer");
/*     */   }
/*     */   
/*     */   public String getVersionWarnings() {
/*  65 */     return (String)this.warnings.get("version");
/*     */   }
/*     */   
/*     */   public String getVendorWarnings() {
/*  69 */     return (String)this.warnings.get("vendor");
/*     */   }
/*     */   
/*     */   public String getAllWarnings() {
/*  73 */     StringBuilder sb = new StringBuilder();
/*  74 */     this.warnings.forEach((k, v) -> sb.append(k).append(": ").append(v));
/*  75 */     return sb.isEmpty() ? null : sb.toString();
/*     */   }
/*     */   
/*     */   protected Preparations prepare(ResourceManager manager, ProfilerFiller profiler) {
/*  80 */     List<Pattern> rendererPatterns = Lists.newArrayList();
/*  81 */     List<Pattern> versionPatterns = Lists.newArrayList();
/*  82 */     List<Pattern> vendorPatterns = Lists.newArrayList();
/*  84 */     JsonObject root = parseJson(manager, profiler);
/*  85 */     if (root != null) {
/*  86 */       Zone ignored = profiler.zone("compile_regex");
/*     */       try {
/*  87 */         compilePatterns(root.getAsJsonArray("renderer"), rendererPatterns);
/*  88 */         compilePatterns(root.getAsJsonArray("version"), versionPatterns);
/*  89 */         compilePatterns(root.getAsJsonArray("vendor"), vendorPatterns);
/*  90 */         if (ignored != null)
/*  90 */           ignored.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (ignored != null)
/*     */           try {
/*     */             ignored.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } 
/*  93 */     return new Preparations(rendererPatterns, versionPatterns, vendorPatterns);
/*     */   }
/*     */   
/*     */   protected void apply(Preparations preparations, ResourceManager manager, ProfilerFiller profiler) {
/*  98 */     this.warnings = preparations.apply();
/*     */   }
/*     */   
/*     */   private static void compilePatterns(JsonArray jsonArray, List<Pattern> patternList) {
/* 102 */     jsonArray.forEach(e -> patternList.add(Pattern.compile(e.getAsString(), 2)));
/*     */   }
/*     */   
/*     */   private static JsonObject parseJson(ResourceManager manager, ProfilerFiller profiler) {
/*     */     try {
/* 107 */       Zone ignored = profiler.zone("parse_json");
/*     */       try {
/* 108 */         Reader resource = manager.openAsReader(GPU_WARNLIST_LOCATION);
/*     */         try {
/* 110 */           JsonObject jsonObject = StrictJsonParser.parse(resource).getAsJsonObject();
/* 111 */           if (resource != null)
/* 111 */             resource.close(); 
/* 111 */           if (ignored != null)
/* 111 */             ignored.close(); 
/*     */           return jsonObject;
/*     */         } catch (Throwable throwable) {
/*     */           if (resource != null)
/*     */             try {
/*     */               resource.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/*     */       } catch (Throwable throwable) {
/*     */         if (ignored != null)
/*     */           try {
/*     */             ignored.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 111 */     } catch (IOException|com.google.gson.JsonSyntaxException e) {
/* 112 */       LOGGER.warn("Failed to load GPU warnlist", e);
/* 114 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static final class Preparations {
/*     */     private final List<Pattern> rendererPatterns;
/*     */     
/*     */     private final List<Pattern> versionPatterns;
/*     */     
/*     */     private final List<Pattern> vendorPatterns;
/*     */     
/*     */     private Preparations(List<Pattern> rendererPatterns, List<Pattern> versionPatterns, List<Pattern> vendorPatterns) {
/* 123 */       this.rendererPatterns = rendererPatterns;
/* 124 */       this.versionPatterns = versionPatterns;
/* 125 */       this.vendorPatterns = vendorPatterns;
/*     */     }
/*     */     
/*     */     private static String matchAny(List<Pattern> patterns, String input) {
/* 129 */       List<String> allMatches = Lists.newArrayList();
/* 130 */       for (Pattern pattern : patterns) {
/* 131 */         Matcher matcher = pattern.matcher(input);
/* 132 */         while (matcher.find())
/* 133 */           allMatches.add(matcher.group()); 
/*     */       } 
/* 136 */       return String.join(", ", (Iterable)allMatches);
/*     */     }
/*     */     
/*     */     private ImmutableMap<String, String> apply() {
/* 140 */       ImmutableMap.Builder<String, String> map = new ImmutableMap.Builder();
/* 141 */       GpuDevice device = RenderSystem.getDevice();
/* 142 */       DeviceInfo deviceInfo = device.getDeviceInfo();
/* 143 */       if (deviceInfo.backendName().equals("OpenGL")) {
/* 144 */         String rendererFails = matchAny(this.rendererPatterns, deviceInfo.name());
/* 145 */         if (!rendererFails.isEmpty())
/* 146 */           map.put("renderer", rendererFails); 
/* 149 */         String versionFails = matchAny(this.versionPatterns, deviceInfo.driverInfo());
/* 150 */         if (!versionFails.isEmpty())
/* 151 */           map.put("version", versionFails); 
/* 154 */         String vendorFails = matchAny(this.vendorPatterns, deviceInfo.vendorName());
/* 155 */         if (!vendorFails.isEmpty())
/* 156 */           map.put("vendor", vendorFails); 
/*     */       } 
/* 160 */       return map.build();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\GpuWarnlistManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */