/*     */ package net.minecraft.util.worldupdate;
/*     */ 
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.SavedDataStorage;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class WorldUpgrader implements AutoCloseable {
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  36 */   private static final ThreadFactory THREAD_FACTORY = (new ThreadFactoryBuilder()).setNameFormat("World Upgrader #%d").setDaemon(true).build();
/*     */   
/*  37 */   private final UpgradeStatusTranslator statusTranslator = new UpgradeStatusTranslator();
/*     */   
/*     */   private final Registry<LevelStem> dimensions;
/*     */   
/*     */   private final Set<ResourceKey<Level>> levels;
/*     */   
/*     */   private final boolean eraseCache;
/*     */   
/*     */   private final boolean recreateRegionFiles;
/*     */   
/*     */   private final LevelStorageSource.LevelStorageAccess levelStorage;
/*     */   
/*     */   private final Thread thread;
/*     */   
/*     */   private final DataFixer dataFixer;
/*     */   
/*  47 */   private final UpgradeProgress upgradeProgress = new UpgradeProgress();
/*     */   
/*     */   private final SavedDataStorage overworldSavedDataStorage;
/*     */   
/*     */   public WorldUpgrader(LevelStorageSource.LevelStorageAccess levelSource, DataFixer dataFixer, RegistryAccess registryAccess, boolean eraseCache, boolean recreateRegionFiles) {
/*  52 */     this.dimensions = registryAccess.lookupOrThrow(Registries.LEVEL_STEM);
/*  53 */     this.levels = (Set<ResourceKey<Level>>)this.dimensions.registryKeySet().stream().map(Registries::levelStemToLevel).collect(Collectors.toUnmodifiableSet());
/*  54 */     this.eraseCache = eraseCache;
/*  55 */     this.dataFixer = dataFixer;
/*  56 */     this.levelStorage = levelSource;
/*  57 */     this.overworldSavedDataStorage = new SavedDataStorage(this.levelStorage.getDimensionPath(Level.OVERWORLD).resolve("data"), dataFixer, (HolderLookup.Provider)registryAccess);
/*  58 */     this.recreateRegionFiles = recreateRegionFiles;
/*  60 */     this.thread = THREAD_FACTORY.newThread(this::work);
/*  61 */     this.thread.setUncaughtExceptionHandler((t, e) -> {
/*     */           LOGGER.error("Error upgrading world", e);
/*     */           this.upgradeProgress.setStatus(UpgradeProgress.Status.FAILED);
/*     */           this.upgradeProgress.setFinished(true);
/*     */         });
/*  66 */     this.thread.start();
/*     */   }
/*     */   
/*     */   public static CompoundTag getDataFixContextTag(Registry<LevelStem> dimensions, ResourceKey<Level> dimension) {
/*  70 */     ChunkGenerator generator = ((LevelStem)dimensions.getValueOrThrow(Registries.levelToLevelStem(dimension))).generator();
/*  71 */     return ChunkMap.getChunkDataFixContextTag(dimension, generator.getTypeNameForDataFixer());
/*     */   }
/*     */   
/*     */   public static boolean verifyChunkPosAndEraseCache(ChunkPos pos, CompoundTag upgradedTag) {
/*  75 */     verifyChunkPos(pos, upgradedTag);
/*  78 */     boolean changed = upgradedTag.contains("Heightmaps");
/*  79 */     upgradedTag.remove("Heightmaps");
/*  80 */     changed = (changed || upgradedTag.contains("isLightOn"));
/*  81 */     upgradedTag.remove("isLightOn");
/*  83 */     ListTag sections = upgradedTag.getListOrEmpty("sections");
/*  84 */     for (int i = 0; i < sections.size(); i++) {
/*  85 */       Optional<CompoundTag> maybeSection = sections.getCompound(i);
/*  86 */       if (!maybeSection.isEmpty()) {
/*  89 */         CompoundTag section = maybeSection.get();
/*  90 */         changed = (changed || section.contains("BlockLight"));
/*  91 */         section.remove("BlockLight");
/*  92 */         changed = (changed || section.contains("SkyLight"));
/*  93 */         section.remove("SkyLight");
/*     */       } 
/*     */     } 
/*  95 */     return changed;
/*     */   }
/*     */   
/*     */   public static boolean verifyChunkPos(ChunkPos pos, CompoundTag upgradedTag) {
/*  99 */     ChunkPos storedPos = new ChunkPos(upgradedTag.getIntOr("xPos", 0), upgradedTag.getIntOr("zPos", 0));
/* 100 */     if (!storedPos.equals(pos))
/* 101 */       LOGGER.warn("Chunk {} has invalid position {}", pos, storedPos); 
/* 103 */     return false;
/*     */   }
/*     */   
/*     */   public void cancel() {
/* 107 */     this.upgradeProgress.setCanceled();
/*     */     try {
/* 109 */       this.thread.join();
/* 110 */     } catch (InterruptedException interruptedException) {}
/*     */   }
/*     */   
/*     */   private void work() {
/* 115 */     long conversionTime = Util.getMillis();
/* 116 */     int currentVersion = SharedConstants.getCurrentVersion().dataVersion().version();
/* 117 */     LOGGER.info("Upgrading entities");
/* 118 */     upgradeLevels(DataFixTypes.ENTITY_CHUNK, (new RegionStorageUpgrader.Builder(this.dataFixer))
/*     */         
/* 121 */         .setTypeAndFolderName("entities")
/* 122 */         .setRecreateRegionFiles(this.recreateRegionFiles)
/* 123 */         .trackProgress(this.upgradeProgress));
/* 125 */     LOGGER.info("Upgrading POIs");
/* 126 */     upgradeLevels(DataFixTypes.POI_CHUNK, (new RegionStorageUpgrader.Builder(this.dataFixer))
/*     */         
/* 129 */         .setTypeAndFolderName("poi")
/* 130 */         .setDefaultVersion(1945)
/* 131 */         .setRecreateRegionFiles(this.recreateRegionFiles)
/* 132 */         .trackProgress(this.upgradeProgress));
/* 134 */     LOGGER.info("Upgrading blocks");
/* 135 */     upgradeLevels(DataFixTypes.CHUNK, (new RegionStorageUpgrader.Builder(this.dataFixer))
/*     */         
/* 138 */         .setType("chunk")
/* 139 */         .setFolderName("region")
/* 140 */         .setRecreateRegionFiles(this.recreateRegionFiles)
/* 141 */         .trackProgress(this.upgradeProgress), (levelSpecificBuilder, level) -> levelSpecificBuilder.setDataFixContextTag(getDataFixContextTag(this.dimensions, level)).addTagModifier(currentVersion, this.eraseCache ? WorldUpgrader::verifyChunkPosAndEraseCache : WorldUpgrader::verifyChunkPos));
/* 146 */     this.overworldSavedDataStorage.saveAndJoin();
/* 147 */     conversionTime = Util.getMillis() - conversionTime;
/* 148 */     LOGGER.info("World optimization finished after {} seconds", Long.valueOf(conversionTime / 1000L));
/* 149 */     this.upgradeProgress.setFinished(true);
/*     */   }
/*     */   
/*     */   private void upgradeLevels(DataFixTypes dataFixType, RegionStorageUpgrader.Builder builder) {
/* 153 */     upgradeLevels(dataFixType, builder, (levelSpecificBuilder, level) -> levelSpecificBuilder);
/*     */   }
/*     */   
/*     */   private void upgradeLevels(DataFixTypes dataFixType, RegionStorageUpgrader.Builder builder, BiFunction<RegionStorageUpgrader.Builder, ResourceKey<Level>, RegionStorageUpgrader.Builder> levelSpecificBuilder) {
/* 157 */     List<RegionStorageUpgrader> upgraders = new ArrayList<>();
/* 158 */     this.upgradeProgress.reset(dataFixType);
/* 159 */     this.upgradeProgress.setType(UpgradeProgress.Type.REGIONS);
/* 160 */     builder.setDataFixType(dataFixType);
/* 161 */     int previousCopiesFileAmounts = 0;
/* 162 */     for (ResourceKey<Level> level : this.levels) {
/* 163 */       RegionStorageUpgrader upgrader = ((RegionStorageUpgrader.Builder)levelSpecificBuilder.apply(builder.copy(), level)).build(previousCopiesFileAmounts);
/* 164 */       upgrader.init(level, this.levelStorage);
/* 165 */       previousCopiesFileAmounts += upgrader.fileAmount();
/* 166 */       upgraders.add(upgrader);
/*     */     } 
/* 168 */     upgraders.forEach(RegionStorageUpgrader::upgrade);
/*     */   }
/*     */   
/*     */   public boolean isFinished() {
/* 172 */     return this.upgradeProgress.isFinished();
/*     */   }
/*     */   
/*     */   public Set<ResourceKey<Level>> levels() {
/* 176 */     return this.levels;
/*     */   }
/*     */   
/*     */   public float dimensionProgress(ResourceKey<Level> dimension) {
/* 180 */     return this.upgradeProgress.getDimensionProgress(dimension);
/*     */   }
/*     */   
/*     */   public float getTotalProgress() {
/* 184 */     return this.upgradeProgress.getTotalProgress();
/*     */   }
/*     */   
/*     */   public int getTotalChunks() {
/* 188 */     return this.upgradeProgress.getTotalChunks();
/*     */   }
/*     */   
/*     */   public int getConverted() {
/* 192 */     return this.upgradeProgress.getConverted();
/*     */   }
/*     */   
/*     */   public int getSkipped() {
/* 196 */     return this.upgradeProgress.getSkipped();
/*     */   }
/*     */   
/*     */   public Component getStatus() {
/* 200 */     return this.statusTranslator.translate(this.upgradeProgress);
/*     */   }
/*     */   
/*     */   public void close() {
/* 205 */     this.overworldSavedDataStorage.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\worldupdate\WorldUpgrader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */