/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.client.KeyMapping;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntry;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntryList;
/*     */ import net.minecraft.client.gui.components.debugchart.BandwidthDebugChart;
/*     */ import net.minecraft.client.gui.components.debugchart.FpsDebugChart;
/*     */ import net.minecraft.client.gui.components.debugchart.PingDebugChart;
/*     */ import net.minecraft.client.gui.components.debugchart.ProfilerPieChart;
/*     */ import net.minecraft.client.gui.components.debugchart.TpsDebugChart;
/*     */ import net.minecraft.client.gui.screens.LevelLoadingScreen;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ChunkLevel;
/*     */ import net.minecraft.server.level.ChunkResult;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.progress.ChunkLoadStatusView;
/*     */ import net.minecraft.util.debugchart.LocalSampleLogger;
/*     */ import net.minecraft.util.debugchart.RemoteDebugSampleType;
/*     */ import net.minecraft.util.debugchart.SampleStorage;
/*     */ import net.minecraft.util.debugchart.TpsDebugDimensions;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ 
/*     */ public class DebugScreenOverlay {
/*     */   private static final int MARGIN_RIGHT = 2;
/*     */   
/*     */   private static final int MARGIN_LEFT = 2;
/*     */   
/*     */   private static final int MARGIN_TOP = 2;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final Font font;
/*     */   
/*     */   private ChunkPos lastPos;
/*     */   
/*     */   private LevelChunk clientChunk;
/*     */   
/*     */   private CompletableFuture<LevelChunk> serverChunk;
/*     */   
/*     */   private boolean renderProfilerChart;
/*     */   
/*     */   private boolean renderFpsCharts;
/*     */   
/*     */   private boolean renderNetworkCharts;
/*     */   
/*     */   private boolean renderLightmapTexture;
/*     */   
/*  67 */   private final LocalSampleLogger frameTimeLogger = new LocalSampleLogger(1);
/*     */   
/*  68 */   private final LocalSampleLogger tickTimeLogger = new LocalSampleLogger((TpsDebugDimensions.values()).length);
/*     */   
/*  69 */   private final LocalSampleLogger pingLogger = new LocalSampleLogger(1);
/*     */   
/*  70 */   private final LocalSampleLogger bandwidthLogger = new LocalSampleLogger(1);
/*     */   
/*  71 */   private final Map<RemoteDebugSampleType, LocalSampleLogger> remoteSupportingLoggers = Map.of(RemoteDebugSampleType.TICK_TIME, this.tickTimeLogger);
/*     */   
/*     */   private final FpsDebugChart fpsChart;
/*     */   
/*     */   private final TpsDebugChart tpsChart;
/*     */   
/*     */   private final PingDebugChart pingChart;
/*     */   
/*     */   private final BandwidthDebugChart bandwidthChart;
/*     */   
/*     */   private final ProfilerPieChart profilerPieChart;
/*     */   
/*     */   public DebugScreenOverlay(Minecraft minecraft) {
/*  79 */     this.minecraft = minecraft;
/*  80 */     this.font = minecraft.font;
/*  81 */     this.fpsChart = new FpsDebugChart(this.font, (SampleStorage)this.frameTimeLogger);
/*  82 */     this.tpsChart = new TpsDebugChart(this.font, (SampleStorage)this.tickTimeLogger, () -> Float.valueOf((minecraft.level == null) ? 0.0F : minecraft.level.tickRateManager().millisecondsPerTick()));
/*  83 */     this.pingChart = new PingDebugChart(this.font, (SampleStorage)this.pingLogger);
/*  84 */     this.bandwidthChart = new BandwidthDebugChart(this.font, (SampleStorage)this.bandwidthLogger);
/*  85 */     this.profilerPieChart = new ProfilerPieChart(this.font);
/*     */   }
/*     */   
/*     */   public void clearChunkCache() {
/*  89 */     this.serverChunk = null;
/*  90 */     this.clientChunk = null;
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics) {
/*     */     ChunkPos chunkPos;
/*  94 */     Options options = this.minecraft.options;
/*  95 */     if (!this.minecraft.isGameLoadFinished() || (this.minecraft.gui.hud.isHidden() && this.minecraft.gui.screen() == null))
/*     */       return; 
/*  98 */     Collection<Identifier> visibleEntries = this.minecraft.debugEntries.getCurrentlyEnabled();
/*  99 */     if (visibleEntries.isEmpty())
/*     */       return; 
/* 103 */     graphics.nextStratum();
/* 104 */     ProfilerFiller profiler = Profiler.get();
/* 105 */     profiler.push("debug");
/* 108 */     if (this.minecraft.getCameraEntity() != null && this.minecraft.level != null) {
/* 109 */       BlockPos feetPos = this.minecraft.getCameraEntity().blockPosition();
/* 110 */       chunkPos = ChunkPos.containing(feetPos);
/*     */     } else {
/* 112 */       chunkPos = null;
/*     */     } 
/* 114 */     if (!Objects.equals(this.lastPos, chunkPos)) {
/* 115 */       this.lastPos = chunkPos;
/* 116 */       clearChunkCache();
/*     */     } 
/* 119 */     final List<String> leftLines = new ArrayList<>();
/* 120 */     final List<String> rightLines = new ArrayList<>();
/* 121 */     final Map<Identifier, Collection<String>> groups = new LinkedHashMap<>();
/* 122 */     final List<String> regularLines = new ArrayList<>();
/* 123 */     DebugScreenDisplayer displayer = new DebugScreenDisplayer(this) {
/*     */         public void addPriorityLine(String line) {
/* 126 */           if (leftLines.size() > rightLines.size()) {
/* 127 */             rightLines.add(line);
/*     */           } else {
/* 129 */             leftLines.add(line);
/*     */           } 
/*     */         }
/*     */         
/*     */         public void addLine(String line) {
/* 135 */           regularLines.add(line);
/*     */         }
/*     */         
/*     */         public void addToGroup(Identifier group, Collection<String> lines) {
/* 140 */           ((Collection<String>)groups.computeIfAbsent(group, k -> new ArrayList())).addAll(lines);
/*     */         }
/*     */         
/*     */         public void addToGroup(Identifier group, String lines) {
/* 145 */           ((Collection<String>)groups.computeIfAbsent(group, k -> new ArrayList())).add(lines);
/*     */         }
/*     */       };
/* 149 */     Level level = getLevel();
/* 150 */     for (Identifier id : visibleEntries) {
/* 151 */       DebugScreenEntry entry = DebugScreenEntries.getEntry(id);
/* 152 */       if (entry != null)
/* 153 */         entry.display(displayer, level, getClientChunk(), getServerChunk()); 
/*     */     } 
/* 157 */     if (!leftLines.isEmpty())
/* 158 */       leftLines.add(""); 
/* 160 */     if (!rightLines.isEmpty())
/* 161 */       rightLines.add(""); 
/* 164 */     if (!regularLines.isEmpty()) {
/* 165 */       int mid = (regularLines.size() + 1) / 2;
/* 166 */       leftLines.addAll(regularLines.subList(0, mid));
/* 167 */       rightLines.addAll(regularLines.subList(mid, regularLines.size()));
/* 169 */       leftLines.add("");
/* 170 */       if (mid < regularLines.size())
/* 171 */         rightLines.add(""); 
/*     */     } 
/* 175 */     List<Collection<String>> finalGroups = new ArrayList<>(groups.values());
/* 176 */     if (!finalGroups.isEmpty()) {
/* 177 */       int mid = (finalGroups.size() + 1) / 2;
/* 178 */       for (int i = 0; i < finalGroups.size(); i++) {
/* 179 */         Collection<String> lines = finalGroups.get(i);
/* 180 */         if (!lines.isEmpty())
/* 181 */           if (i < mid) {
/* 182 */             leftLines.addAll(lines);
/* 183 */             leftLines.add("");
/*     */           } else {
/* 185 */             rightLines.addAll(lines);
/* 186 */             rightLines.add("");
/*     */           }  
/*     */       } 
/*     */     } 
/* 192 */     if (this.minecraft.debugEntries.isOverlayVisible()) {
/* 193 */       leftLines.add("");
/* 194 */       boolean hasServer = (this.minecraft.getSingleplayerServer() != null);
/* 195 */       KeyMapping keyDebugModifier = options.keyDebugModifier;
/* 196 */       leftLines.add("Debug charts: " + 
/* 197 */           formatChart(keyDebugModifier, options.keyDebugPofilingChart, "Profiler", this.renderProfilerChart) + "; " + 
/* 198 */           formatChart(keyDebugModifier, options.keyDebugFpsCharts, hasServer ? "FPS + TPS" : "FPS", this.renderFpsCharts) + ";");
/* 200 */       leftLines.add(formatChart(keyDebugModifier, options.keyDebugNetworkCharts, !this.minecraft.isLocalServer() ? "Bandwidth + Ping" : "Ping", this.renderNetworkCharts) + "; " + formatChart(keyDebugModifier, options.keyDebugNetworkCharts, !this.minecraft.isLocalServer() ? "Bandwidth + Ping" : "Ping", this.renderNetworkCharts));
/* 203 */       leftLines.add("To edit: press " + formatKeybind(keyDebugModifier, options.keyDebugDebugOptions));
/*     */     } 
/* 206 */     extractLines(graphics, leftLines, true);
/* 207 */     extractLines(graphics, rightLines, false);
/* 209 */     graphics.nextStratum();
/* 210 */     this.profilerPieChart.setBottomOffset(10);
/* 211 */     if (showFpsCharts()) {
/* 212 */       int scaledWidth = graphics.guiWidth();
/* 213 */       int maxWidth = scaledWidth / 2;
/* 214 */       this.fpsChart.extractRenderState(graphics, 0, this.fpsChart.getWidth(maxWidth));
/* 215 */       if (this.tickTimeLogger.size() > 0) {
/* 216 */         int width = this.tpsChart.getWidth(maxWidth);
/* 217 */         this.tpsChart.extractRenderState(graphics, scaledWidth - width, width);
/*     */       } 
/* 219 */       this.profilerPieChart.setBottomOffset(this.tpsChart.getFullHeight());
/*     */     } 
/* 221 */     if (showNetworkCharts() && this.minecraft.getConnection() != null) {
/* 222 */       int scaledWidth = graphics.guiWidth();
/* 223 */       int maxWidth = scaledWidth / 2;
/* 224 */       if (!this.minecraft.isLocalServer())
/* 225 */         this.bandwidthChart.extractRenderState(graphics, 0, this.bandwidthChart.getWidth(maxWidth)); 
/* 227 */       int width = this.pingChart.getWidth(maxWidth);
/* 228 */       this.pingChart.extractRenderState(graphics, scaledWidth - width, width);
/* 229 */       this.profilerPieChart.setBottomOffset(this.pingChart.getFullHeight());
/*     */     } 
/* 231 */     if (showLightmapTexture()) {
/* 232 */       GpuTextureView lightmapTextureView = this.minecraft.gameRenderer.levelLightmap();
/* 233 */       int displaySize = 64;
/* 234 */       int x = graphics.guiWidth() - 64 - 2;
/* 235 */       int y = graphics.guiHeight() - 64 - 2;
/* 236 */       graphics.fill(x - 1, y - 1, x + 64 + 1, y + 64 + 1, -16777216);
/* 237 */       graphics.blit(lightmapTextureView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST), x, y, x + 64, y + 64, 0.0F, 1.0F, 1.0F, 0.0F);
/*     */     } 
/* 240 */     if (this.minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_CHUNKS_ON_SERVER)) {
/* 241 */       IntegratedServer singleplayerServer = this.minecraft.getSingleplayerServer();
/* 242 */       if (singleplayerServer != null && this.minecraft.player != null) {
/* 243 */         ChunkLoadStatusView statusView = singleplayerServer.createChunkLoadStatusView(16 + ChunkLevel.RADIUS_AROUND_FULL_CHUNK);
/* 244 */         statusView.moveTo(this.minecraft.player.level().dimension(), this.minecraft.player.chunkPosition());
/* 245 */         LevelLoadingScreen.extractChunksForRendering(graphics, graphics.guiWidth() / 2, graphics.guiHeight() / 2, 4, 1, statusView);
/*     */       } 
/*     */     } 
/* 249 */     Zone ignored = profiler.zone("profilerPie");
/*     */     try {
/* 250 */       this.profilerPieChart.extractRenderState(graphics);
/* 251 */       if (ignored != null)
/* 251 */         ignored.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (ignored != null)
/*     */         try {
/*     */           ignored.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 253 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private static String formatChart(KeyMapping keyDebugModifier, KeyMapping keybind, String name, boolean status) {
/* 257 */     return formatKeybind(keyDebugModifier, keybind) + " " + formatKeybind(keyDebugModifier, keybind) + " " + name;
/*     */   }
/*     */   
/*     */   private static String formatKeybind(KeyMapping keyDebugModifier, KeyMapping keybind) {
/* 261 */     return "[" + (keyDebugModifier.isUnbound() ? "" : (keyDebugModifier.getTranslatedKeyMessage().getString() + "+")) + keybind.getTranslatedKeyMessage().getString() + "]";
/*     */   }
/*     */   
/*     */   private void extractLines(GuiGraphicsExtractor graphics, List<String> lines, boolean alignLeft) {
/* 265 */     Objects.requireNonNull(this.font);
/* 265 */     int height = 9;
/*     */     int i;
/* 266 */     for (i = 0; i < lines.size(); i++) {
/* 267 */       String line = lines.get(i);
/* 268 */       if (!Strings.isNullOrEmpty(line)) {
/* 269 */         int width = this.font.width(line);
/* 270 */         int left = alignLeft ? 2 : (graphics.guiWidth() - 2 - width);
/* 271 */         int top = 2 + height * i;
/* 272 */         graphics.fill(left - 1, top - 1, left + width + 1, top + height - 1, -1873784752);
/*     */       } 
/*     */     } 
/* 275 */     for (i = 0; i < lines.size(); i++) {
/* 276 */       String line = lines.get(i);
/* 277 */       if (!Strings.isNullOrEmpty(line)) {
/* 278 */         int width = this.font.width(line);
/* 279 */         int left = alignLeft ? 2 : (graphics.guiWidth() - 2 - width);
/* 280 */         int top = 2 + height * i;
/* 281 */         graphics.text(this.font, line, left, top, -2039584, false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private ServerLevel getServerLevel() {
/* 287 */     if (this.minecraft.level == null)
/* 288 */       return null; 
/* 290 */     IntegratedServer server = this.minecraft.getSingleplayerServer();
/* 291 */     if (server != null)
/* 292 */       return server.getLevel(this.minecraft.level.dimension()); 
/* 295 */     return null;
/*     */   }
/*     */   
/*     */   private Level getLevel() {
/* 299 */     if (this.minecraft.level == null)
/* 300 */       return null; 
/* 302 */     return (Level)DataFixUtils.orElse(
/* 303 */         Optional.<IntegratedServer>ofNullable(this.minecraft.getSingleplayerServer()).flatMap(s -> Optional.ofNullable(s.getLevel(this.minecraft.level.dimension()))), this.minecraft.level);
/*     */   }
/*     */   
/*     */   private LevelChunk getServerChunk() {
/* 309 */     if (this.minecraft.level == null || this.lastPos == null)
/* 310 */       return null; 
/* 312 */     if (this.serverChunk == null) {
/* 313 */       ServerLevel level = getServerLevel();
/* 314 */       if (level == null)
/* 315 */         return null; 
/* 317 */       this.serverChunk = level.getChunkSource().getChunkFuture(this.lastPos.x(), this.lastPos.z(), ChunkStatus.FULL, false).thenApply(chunkResult -> (LevelChunk)chunkResult.orElse(null));
/*     */     } 
/* 319 */     return this.serverChunk.getNow(null);
/*     */   }
/*     */   
/*     */   private LevelChunk getClientChunk() {
/* 323 */     if (this.minecraft.level == null || this.lastPos == null)
/* 324 */       return null; 
/* 326 */     if (this.clientChunk == null)
/* 327 */       this.clientChunk = this.minecraft.level.getChunk(this.lastPos.x(), this.lastPos.z()); 
/* 329 */     return this.clientChunk;
/*     */   }
/*     */   
/*     */   public boolean showDebugScreen() {
/* 333 */     DebugScreenEntryList entries = this.minecraft.debugEntries;
/* 334 */     return ((entries.isOverlayVisible() || !entries.getCurrentlyEnabled().isEmpty()) && (!this.minecraft.gui.hud.isHidden() || this.minecraft.gui.screen() != null));
/*     */   }
/*     */   
/*     */   public boolean showProfilerChart() {
/* 338 */     return (this.minecraft.debugEntries.isOverlayVisible() && this.renderProfilerChart);
/*     */   }
/*     */   
/*     */   public boolean showNetworkCharts() {
/* 342 */     return (this.minecraft.debugEntries.isOverlayVisible() && this.renderNetworkCharts);
/*     */   }
/*     */   
/*     */   public boolean showFpsCharts() {
/* 346 */     return (this.minecraft.debugEntries.isOverlayVisible() && this.renderFpsCharts);
/*     */   }
/*     */   
/*     */   public boolean showLightmapTexture() {
/* 350 */     return (this.minecraft.debugEntries.isOverlayVisible() && this.renderLightmapTexture);
/*     */   }
/*     */   
/*     */   public void toggleNetworkCharts() {
/* 354 */     this.renderNetworkCharts = (!this.minecraft.debugEntries.isOverlayVisible() || !this.renderNetworkCharts);
/* 355 */     if (this.renderNetworkCharts) {
/* 356 */       this.minecraft.debugEntries.setOverlayVisible(true);
/* 357 */       this.renderFpsCharts = false;
/* 358 */       this.renderLightmapTexture = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void toggleFpsCharts() {
/* 363 */     this.renderFpsCharts = (!this.minecraft.debugEntries.isOverlayVisible() || !this.renderFpsCharts);
/* 364 */     if (this.renderFpsCharts) {
/* 365 */       this.minecraft.debugEntries.setOverlayVisible(true);
/* 366 */       this.renderNetworkCharts = false;
/* 367 */       this.renderLightmapTexture = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void toggleLightmapTexture() {
/* 372 */     this.renderLightmapTexture = (!this.minecraft.debugEntries.isOverlayVisible() || !this.renderLightmapTexture);
/* 373 */     if (this.renderLightmapTexture) {
/* 374 */       this.minecraft.debugEntries.setOverlayVisible(true);
/* 375 */       this.renderFpsCharts = false;
/* 376 */       this.renderNetworkCharts = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void toggleProfilerChart() {
/* 381 */     this.renderProfilerChart = (!this.minecraft.debugEntries.isOverlayVisible() || !this.renderProfilerChart);
/* 382 */     if (this.renderProfilerChart)
/* 383 */       this.minecraft.debugEntries.setOverlayVisible(true); 
/*     */   }
/*     */   
/*     */   public void logFrameDuration(long frameDuration) {
/* 388 */     this.frameTimeLogger.logSample(frameDuration);
/*     */   }
/*     */   
/*     */   public LocalSampleLogger getTickTimeLogger() {
/* 392 */     return this.tickTimeLogger;
/*     */   }
/*     */   
/*     */   public LocalSampleLogger getPingLogger() {
/* 396 */     return this.pingLogger;
/*     */   }
/*     */   
/*     */   public LocalSampleLogger getBandwidthLogger() {
/* 400 */     return this.bandwidthLogger;
/*     */   }
/*     */   
/*     */   public ProfilerPieChart getProfilerPieChart() {
/* 404 */     return this.profilerPieChart;
/*     */   }
/*     */   
/*     */   public void logRemoteSample(long[] sample, RemoteDebugSampleType type) {
/* 408 */     LocalSampleLogger logger = this.remoteSupportingLoggers.get(type);
/* 409 */     if (logger != null)
/* 410 */       logger.logFullSample(sample); 
/*     */   }
/*     */   
/*     */   public void reset() {
/* 415 */     this.tickTimeLogger.reset();
/* 416 */     this.pingLogger.reset();
/* 417 */     this.bandwidthLogger.reset();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\DebugScreenOverlay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */