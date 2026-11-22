/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*     */ import java.util.Objects;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.WorldStem;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.ServerPacksSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.util.worldupdate.WorldUpgrader;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.WorldData;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class OptimizeWorldScreen extends Screen {
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final ToIntFunction<ResourceKey<Level>> DIMENSION_COLORS;
/*     */   
/*     */   private final BooleanConsumer callback;
/*     */   
/*     */   private final WorldUpgrader upgrader;
/*     */   
/*     */   static {
/*  37 */     DIMENSION_COLORS = (ToIntFunction<ResourceKey<Level>>)Util.make(new Reference2IntOpenHashMap(), map -> {
/*     */           map.put(Level.OVERWORLD, -13408734);
/*     */           map.put(Level.NETHER, -10075085);
/*     */           map.put(Level.END, -8943531);
/*     */           map.defaultReturnValue(-2236963);
/*     */         });
/*     */   }
/*     */   
/*     */   public static OptimizeWorldScreen create(Minecraft minecraft, BooleanConsumer callback, DataFixer dataFixer, LevelStorageSource.LevelStorageAccess levelSourceAccess, boolean eraseCache) {
/*     */     try {
/*  49 */       WorldOpenFlows worldOpenFlows = minecraft.createWorldOpenFlows();
/*  50 */       PackRepository packRepository = ServerPacksSource.createPackRepository(levelSourceAccess);
/*  52 */       Dynamic<?> unfixedDataTag = levelSourceAccess.getUnfixedDataTagWithFallback();
/*  53 */       int dataVersion = NbtUtils.getDataVersion(unfixedDataTag);
/*  54 */       if (DataFixers.getFileFixer().requiresFileFixing(dataVersion))
/*  55 */         throw new IllegalStateException("Can't optimize world before file fixing; shouldn't be able to get here"); 
/*  57 */       Dynamic<?> dataTag = DataFixTypes.LEVEL.updateToCurrentVersion(DataFixers.getDataFixer(), unfixedDataTag, dataVersion);
/*  58 */       WorldStem worldStem = worldOpenFlows.loadWorldStem(levelSourceAccess, dataTag, false, packRepository);
/*     */       try {
/*  59 */         WorldData worldData = worldStem.worldDataAndGenSettings().data();
/*  60 */         RegistryAccess.Frozen registryAccess = worldStem.registries().compositeAccess();
/*  61 */         levelSourceAccess.saveDataTag(worldData);
/*  62 */         OptimizeWorldScreen optimizeWorldScreen = new OptimizeWorldScreen(callback, dataFixer, levelSourceAccess, worldData, eraseCache, (RegistryAccess)registryAccess);
/*  63 */         if (worldStem != null)
/*  63 */           worldStem.close(); 
/*     */         return optimizeWorldScreen;
/*     */       } catch (Throwable throwable) {
/*     */         if (worldStem != null)
/*     */           try {
/*     */             worldStem.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*  64 */     } catch (Exception e) {
/*  65 */       LOGGER.warn("Failed to load datapacks, can't optimize world", e);
/*  66 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private OptimizeWorldScreen(BooleanConsumer callback, DataFixer dataFixer, LevelStorageSource.LevelStorageAccess levelSource, WorldData worldData, boolean eraseCache, RegistryAccess registryAccess) {
/*  71 */     super((Component)Component.translatable("optimizeWorld.title", new Object[] { worldData.getLevelSettings().levelName() }));
/*  72 */     this.callback = callback;
/*  73 */     this.upgrader = new WorldUpgrader(levelSource, dataFixer, registryAccess, eraseCache, false);
/*     */   }
/*     */   
/*     */   protected void init() {
/*  78 */     super.init();
/*  80 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_CANCEL, button -> {
/*     */             this.upgrader.cancel();
/*     */             this.callback.accept(false);
/*  83 */           }).bounds(this.width / 2 - 100, this.height / 4 + 150, 200, 20).build());
/*     */   }
/*     */   
/*     */   public void tick() {
/*  88 */     if (this.upgrader.isFinished())
/*  89 */       this.callback.accept(true); 
/*     */   }
/*     */   
/*     */   public boolean shouldCloseOnEsc() {
/*  95 */     return false;
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 100 */     this.callback.accept(false);
/*     */   }
/*     */   
/*     */   public void removed() {
/* 105 */     this.upgrader.cancel();
/* 106 */     this.upgrader.close();
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 111 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/* 113 */     graphics.centeredText(this.font, this.title, this.width / 2, 20, -1);
/* 115 */     int x0 = this.width / 2 - 150;
/* 116 */     int x1 = this.width / 2 + 150;
/* 117 */     int y0 = this.height / 4 + 100;
/* 118 */     int y1 = y0 + 10;
/* 120 */     Objects.requireNonNull(this.font);
/* 120 */     graphics.centeredText(this.font, this.upgrader.getStatus(), this.width / 2, y0 - 9 - 2, -6250336);
/* 122 */     if (this.upgrader.getTotalChunks() > 0) {
/* 123 */       graphics.fill(x0 - 1, y0 - 1, x1 + 1, y1 + 1, -16777216);
/* 125 */       graphics.text(this.font, (Component)Component.translatable("optimizeWorld.info.converted", new Object[] { Integer.valueOf(this.upgrader.getConverted()) }), x0, 40, -6250336);
/* 126 */       Objects.requireNonNull(this.font);
/* 126 */       graphics.text(this.font, (Component)Component.translatable("optimizeWorld.info.skipped", new Object[] { Integer.valueOf(this.upgrader.getSkipped()) }), x0, 40 + 9 + 3, -6250336);
/* 127 */       Objects.requireNonNull(this.font);
/* 127 */       graphics.text(this.font, (Component)Component.translatable("optimizeWorld.info.total", new Object[] { Integer.valueOf(this.upgrader.getTotalChunks()) }), x0, 40 + (9 + 3) * 2, -6250336);
/* 129 */       int progress = 0;
/* 130 */       for (ResourceKey<Level> dimension : (Iterable<ResourceKey<Level>>)this.upgrader.levels()) {
/* 131 */         int length = Mth.floor(this.upgrader.dimensionProgress(dimension) * (x1 - x0));
/* 132 */         graphics.fill(x0 + progress, y0, x0 + progress + length, y1, DIMENSION_COLORS.applyAsInt(dimension));
/* 133 */         progress += length;
/*     */       } 
/* 136 */       int totalProgress = this.upgrader.getConverted() + this.upgrader.getSkipped();
/* 138 */       MutableComponent mutableComponent1 = Component.translatable("optimizeWorld.progress.counter", new Object[] { Integer.valueOf(totalProgress), Integer.valueOf(this.upgrader.getTotalChunks()) });
/* 139 */       MutableComponent mutableComponent2 = Component.translatable("optimizeWorld.progress.percentage", new Object[] { Integer.valueOf(Mth.floor(this.upgrader.getTotalProgress() * 100.0F)) });
/* 140 */       Objects.requireNonNull(this.font);
/* 140 */       graphics.centeredText(this.font, (Component)mutableComponent1, this.width / 2, y0 + 2 * 9 + 2, -6250336);
/* 141 */       Objects.requireNonNull(this.font);
/* 141 */       graphics.centeredText(this.font, (Component)mutableComponent2, this.width / 2, y0 + (y1 - y0) / 2 - 9 / 2, -6250336);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\OptimizeWorldScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */