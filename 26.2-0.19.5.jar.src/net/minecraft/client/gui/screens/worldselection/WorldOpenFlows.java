/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.screens.AlertScreen;
/*     */ import net.minecraft.client.gui.screens.BackupConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
/*     */ import net.minecraft.client.gui.screens.FileFixerAbortedScreen;
/*     */ import net.minecraft.client.gui.screens.GenericMessageScreen;
/*     */ import net.minecraft.client.gui.screens.NoticeWithLinkScreen;
/*     */ import net.minecraft.client.gui.screens.RecoverWorldDataScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.resources.server.DownloadedPackSource;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.server.RegistryLayer;
/*     */ import net.minecraft.server.ReloadableServerResources;
/*     */ import net.minecraft.server.WorldLoader;
/*     */ import net.minecraft.server.WorldStem;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.ServerPacksSource;
/*     */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.MemoryReserve;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.util.filefix.AbortedFileFixException;
/*     */ import net.minecraft.util.filefix.CanceledFileFixException;
/*     */ import net.minecraft.util.filefix.FailedCleanupFileFixException;
/*     */ import net.minecraft.util.filefix.FileFixException;
/*     */ import net.minecraft.util.worldupdate.UpgradeProgress;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.gamerules.GameRuleMap;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.storage.LevelDataAndDimensions;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ import net.minecraft.world.level.storage.PrimaryLevelData;
/*     */ import net.minecraft.world.level.storage.WorldData;
/*     */ import net.minecraft.world.level.validation.ContentValidationException;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class WorldOpenFlows {
/*  84 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  85 */   private static final UUID WORLD_PACK_ID = UUID.fromString("640a6a92-b6cb-48a0-b391-831586500359");
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final LevelStorageSource levelSource;
/*     */   
/*     */   public WorldOpenFlows(Minecraft minecraft, LevelStorageSource levelSource) {
/*  91 */     this.minecraft = minecraft;
/*  92 */     this.levelSource = levelSource;
/*     */   }
/*     */   
/*     */   public void createFreshLevel(String levelId, LevelSettings levelSettings, WorldOptions options, Function<HolderLookup.Provider, WorldDimensions> dimensionsProvider, Screen parentScreen) {
/*  97 */     this.minecraft.setScreenAndShow((Screen)new GenericMessageScreen((Component)Component.translatable("selectWorld.data_read")));
/*  98 */     LevelStorageSource.LevelStorageAccess levelSourceAccess = createWorldAccess(levelId);
/*  99 */     if (levelSourceAccess == null)
/*     */       return; 
/* 103 */     PackRepository packRepository = ServerPacksSource.createPackRepository(levelSourceAccess);
/* 105 */     WorldDataConfiguration dataConfiguration = levelSettings.dataConfiguration();
/*     */     try {
/* 107 */       WorldLoader.PackConfig packConfig = new WorldLoader.PackConfig(packRepository, dataConfiguration, false, false);
/* 108 */       WorldStem worldStem = loadWorldDataBlocking(packConfig, context -> {
/*     */             WorldDimensions dimensions = dimensionsProvider.apply(context.datapackWorldgen());
/*     */             WorldDimensions.Complete completeDimensions = dimensions.bake(context.datapackDimensions().lookupOrThrow(Registries.LEVEL_STEM));
/*     */             return new WorldLoader.DataLoadOutput(new LevelDataAndDimensions.WorldDataAndGenSettings((WorldData)new PrimaryLevelData(levelSettings, completeDimensions.specialWorldProperty(), completeDimensions.lifecycle()), new WorldGenSettings(options, dimensions)), completeDimensions.dimensionsRegistryAccess());
/* 108 */           }WorldStem::new);
/* 122 */       this.minecraft.doWorldLoad(levelSourceAccess, packRepository, worldStem, Optional.empty(), true);
/* 123 */     } catch (Exception e) {
/* 124 */       LOGGER.warn("Failed to load datapacks, can't proceed with server load", e);
/* 125 */       levelSourceAccess.safeClose();
/* 126 */       this.minecraft.gui.setScreen(parentScreen);
/*     */     } 
/*     */   }
/*     */   
/*     */   private LevelStorageSource.LevelStorageAccess createWorldAccess(String levelId) {
/*     */     try {
/* 132 */       return this.levelSource.validateAndCreateAccess(levelId);
/* 133 */     } catch (IOException e) {
/* 134 */       LOGGER.warn("Failed to read level {} data", levelId, e);
/* 135 */       SystemToast.onWorldAccessFailure(this.minecraft, levelId);
/* 136 */       this.minecraft.gui.setScreen(null);
/* 137 */       return null;
/* 138 */     } catch (ContentValidationException e) {
/* 139 */       LOGGER.warn("{}", e.getMessage());
/* 140 */       this.minecraft.gui.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.minecraft.gui.setScreen(null)));
/* 141 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void createLevelFromExistingSettings(LevelStorageSource.LevelStorageAccess levelSourceAccess, ReloadableServerResources serverResources, LayeredRegistryAccess<RegistryLayer> registryAccess, LevelDataAndDimensions.WorldDataAndGenSettings worldDataAndGenSettings, Optional<GameRules> gameRules) {
/* 147 */     PackRepository packRepository = ServerPacksSource.createPackRepository(levelSourceAccess);
/* 148 */     CloseableResourceManager resourceManager = (CloseableResourceManager)(new WorldLoader.PackConfig(packRepository, worldDataAndGenSettings.data().getDataConfiguration(), false, false)).createResourceManager().getSecond();
/* 149 */     this.minecraft.doWorldLoad(levelSourceAccess, packRepository, new WorldStem(resourceManager, serverResources, registryAccess, worldDataAndGenSettings), gameRules, true);
/*     */   }
/*     */   
/*     */   public WorldStem loadWorldStem(LevelStorageSource.LevelStorageAccess worldAccess, Dynamic<?> levelDataTag, boolean safeMode, PackRepository packRepository) throws Exception {
/* 153 */     WorldLoader.PackConfig packConfig = LevelStorageSource.getPackConfig(levelDataTag, packRepository, safeMode);
/* 154 */     return loadWorldDataBlocking(packConfig, context -> {
/*     */           Registry<LevelStem> datapackDimensions = context.datapackDimensions().lookupOrThrow(Registries.LEVEL_STEM);
/*     */           LevelDataAndDimensions data = LevelStorageSource.getLevelDataAndDimensions(worldAccess, levelDataTag, context.dataConfiguration(), datapackDimensions, context.datapackWorldgen());
/*     */           return new WorldLoader.DataLoadOutput(data.worldDataAndGenSettings(), data.dimensions().dimensionsRegistryAccess());
/* 154 */         }WorldStem::new);
/*     */   }
/*     */   
/*     */   public Pair<LevelSettings, WorldCreationContext> recreateWorldData(LevelStorageSource.LevelStorageAccess levelSourceAccess) throws Exception {
/* 169 */     PackRepository packRepository = ServerPacksSource.createPackRepository(levelSourceAccess);
/* 171 */     Dynamic<?> unfixedDataTag = levelSourceAccess.getUnfixedDataTag(false);
/* 172 */     int dataVersion = NbtUtils.getDataVersion(unfixedDataTag);
/* 173 */     if (DataFixers.getFileFixer().requiresFileFixing(dataVersion))
/* 174 */       throw new IllegalStateException("Can't recreate world before file fixing; shouldn't be able to get here"); 
/* 176 */     Dynamic<?> levelDataTag = DataFixTypes.LEVEL.updateToCurrentVersion(DataFixers.getDataFixer(), unfixedDataTag, dataVersion);
/* 178 */     WorldLoader.PackConfig packConfig = LevelStorageSource.getPackConfig(levelDataTag, packRepository, false);
/* 179 */     return loadWorldDataBlocking(packConfig, context -> {
/*     */           Registry<LevelStem> noDatapackDimensions = (new MappedRegistry(Registries.LEVEL_STEM, Lifecycle.stable())).freeze();
/*     */           LevelDataAndDimensions existingData = LevelStorageSource.getLevelDataAndDimensions(levelSourceAccess, levelDataTag, context.dataConfiguration(), noDatapackDimensions, context.datapackWorldgen());
/*     */           static final class Data extends Record {
/*     */             private final LevelSettings levelSettings;
/*     */             
/*     */             private final WorldOptions options;
/*     */             
/*     */             private final Registry<LevelStem> existingDimensions;
/*     */             
/*     */             Data(LevelSettings levelSettings, WorldOptions options, Registry<LevelStem> existingDimensions) {
/*     */               this.levelSettings = levelSettings;
/*     */               this.options = options;
/*     */               this.existingDimensions = existingDimensions;
/*     */             }
/*     */             
/*     */             public final String toString() {
/*     */               // Byte code:
/*     */               //   0: aload_0
/*     */               //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;)Ljava/lang/String;
/*     */               //   6: areturn
/*     */               // Line number table:
/*     */               //   Java source line number -> byte code offset
/*     */               //   #168	-> 0
/*     */               // Local variable table:
/*     */               //   start	length	slot	name	descriptor
/*     */               //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;
/*     */             }
/*     */             
/*     */             public final int hashCode() {
/*     */               // Byte code:
/*     */               //   0: aload_0
/*     */               //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;)I
/*     */               //   6: ireturn
/*     */               // Line number table:
/*     */               //   Java source line number -> byte code offset
/*     */               //   #168	-> 0
/*     */               // Local variable table:
/*     */               //   start	length	slot	name	descriptor
/*     */               //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;
/*     */             }
/*     */             
/*     */             public final boolean equals(Object o) {
/*     */               // Byte code:
/*     */               //   0: aload_0
/*     */               //   1: aload_1
/*     */               //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;Ljava/lang/Object;)Z
/*     */               //   7: ireturn
/*     */               // Line number table:
/*     */               //   Java source line number -> byte code offset
/*     */               //   #168	-> 0
/*     */               // Local variable table:
/*     */               //   start	length	slot	name	descriptor
/*     */               //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;
/*     */               //   0	8	1	o	Ljava/lang/Object;
/*     */             }
/*     */             
/*     */             public LevelSettings levelSettings() {
/*     */               return this.levelSettings;
/*     */             }
/*     */             
/*     */             public WorldOptions options() {
/*     */               return this.options;
/*     */             }
/*     */             
/*     */             public Registry<LevelStem> existingDimensions() {
/*     */               return this.existingDimensions;
/*     */             }
/*     */           };
/*     */           return new WorldLoader.DataLoadOutput(new Data(existingData.worldDataAndGenSettings().data().getLevelSettings(), existingData.worldDataAndGenSettings().genSettings().options(), existingData.dimensions().dimensions()), context.datapackDimensions());
/*     */         }(resources, managers, registries, loadedData) -> {
/*     */           resources.close();
/*     */           DataResult<GameRuleMap> existingGameRules = LevelStorageSource.readExistingSavedData(levelSourceAccess, (HolderLookup.Provider)registries.compositeAccess(), GameRuleMap.TYPE);
/*     */           existingGameRules.ifError(());
/*     */           InitialWorldCreationOptions initialWorldCreationOptions = new InitialWorldCreationOptions(WorldCreationUiState.SelectedGameMode.SURVIVAL, existingGameRules.result().orElse(GameRuleMap.of()), null);
/*     */           return Pair.of(loadedData.levelSettings, new WorldCreationContext(loadedData.options, new WorldDimensions(loadedData.existingDimensions), registries, managers, loadedData.levelSettings.dataConfiguration(), initialWorldCreationOptions));
/*     */         });
/*     */   }
/*     */   
/*     */   private <D, R> R loadWorldDataBlocking(WorldLoader.PackConfig packConfig, WorldLoader.WorldDataSupplier<D> worldDataGetter, WorldLoader.ResultFactory<D, R> worldDataSupplier) throws Exception {
/* 206 */     long start = Util.getMillis();
/* 207 */     WorldLoader.InitConfig config = new WorldLoader.InitConfig(packConfig, Commands.CommandSelection.INTEGRATED, (PermissionSet)LevelBasedPermissionSet.GAMEMASTER);
/* 208 */     CompletableFuture<R> resourceLoad = WorldLoader.load(config, worldDataGetter, worldDataSupplier, (Executor)Util.backgroundExecutor(), (Executor)this.minecraft);
/* 209 */     Objects.requireNonNull(resourceLoad);
/* 209 */     this.minecraft.managedBlock(resourceLoad::isDone);
/* 210 */     long end = Util.getMillis();
/* 211 */     LOGGER.debug("World resource load blocked for {} ms", Long.valueOf(end - start));
/* 212 */     return resourceLoad.get();
/*     */   }
/*     */   
/*     */   private void askForBackup(LevelStorageSource.LevelStorageAccess levelAccess, boolean oldCustomized, Runnable proceedCallback, Runnable cancelCallback) {
/*     */     MutableComponent mutableComponent1, mutableComponent2;
/* 218 */     if (oldCustomized) {
/* 219 */       mutableComponent1 = Component.translatable("selectWorld.backupQuestion.customized");
/* 220 */       mutableComponent2 = Component.translatable("selectWorld.backupWarning.customized");
/*     */     } else {
/* 222 */       mutableComponent1 = Component.translatable("selectWorld.backupQuestion.experimental");
/* 223 */       mutableComponent2 = Component.translatable("selectWorld.backupWarning.experimental");
/*     */     } 
/* 226 */     this.minecraft.gui.setScreen((Screen)new BackupConfirmScreen(cancelCallback, (backup, eraseCache) -> EditWorldScreen.conditionallyMakeBackupAndShowToast(backup, levelAccess).thenAcceptAsync((), (Executor)this.minecraft), (Component)mutableComponent1, (Component)mutableComponent2, false));
/*     */   }
/*     */   
/*     */   public static void confirmWorldCreation(Minecraft minecraft, CreateWorldScreen parent, Lifecycle lifecycle, Runnable task, boolean skipWarning) {
/* 240 */     BooleanConsumer callback = confirmed -> {
/*     */         if (confirmed) {
/*     */           task.run();
/*     */         } else {
/*     */           minecraft.gui.setScreen(parent);
/*     */         } 
/*     */       };
/* 247 */     if (skipWarning || lifecycle == Lifecycle.stable()) {
/* 248 */       task.run();
/* 249 */     } else if (lifecycle == Lifecycle.experimental()) {
/* 250 */       minecraft.gui.setScreen((Screen)new ConfirmScreen(callback, 
/*     */             
/* 252 */             (Component)Component.translatable("selectWorld.warning.experimental.title"), 
/* 253 */             (Component)Component.translatable("selectWorld.warning.experimental.question")));
/*     */     } else {
/* 257 */       minecraft.gui.setScreen((Screen)new ConfirmScreen(callback, 
/*     */             
/* 259 */             (Component)Component.translatable("selectWorld.warning.deprecated.title"), 
/* 260 */             (Component)Component.translatable("selectWorld.warning.deprecated.question")));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void openWorld(String levelId, Runnable onCancel) {
/* 266 */     this.minecraft.setScreenAndShow((Screen)new GenericMessageScreen((Component)Component.translatable("selectWorld.data_read")));
/* 267 */     LevelStorageSource.LevelStorageAccess worldAccess = createWorldAccess(levelId);
/* 268 */     if (worldAccess == null)
/*     */       return; 
/* 271 */     openWorldLoadLevelData(worldAccess, onCancel);
/*     */   }
/*     */   
/*     */   private void openWorldLoadLevelData(LevelStorageSource.LevelStorageAccess worldAccess, Runnable onCancel) {
/*     */     Dynamic<?> levelDataTag;
/*     */     LevelSummary summary;
/* 275 */     this.minecraft.setScreenAndShow((Screen)new GenericMessageScreen((Component)Component.translatable("selectWorld.data_read")));
/*     */     try {
/* 279 */       levelDataTag = worldAccess.getUnfixedDataTag(false);
/* 280 */       summary = worldAccess.fixAndGetSummaryFromTag(levelDataTag);
/* 281 */     } catch (IOException|net.minecraft.nbt.NbtException|net.minecraft.nbt.ReportedNbtException e) {
/* 282 */       this.minecraft.gui.setScreen((Screen)new RecoverWorldDataScreen(this.minecraft, success -> {
/*     */               if (success) {
/*     */                 openWorldLoadLevelData(worldAccess, onCancel);
/*     */               } else {
/*     */                 worldAccess.safeClose();
/*     */                 onCancel.run();
/*     */               } 
/* 282 */             }worldAccess));
/*     */       return;
/* 291 */     } catch (OutOfMemoryError e) {
/* 292 */       MemoryReserve.release();
/* 294 */       String detailedMessage = "Ran out of memory trying to read level data of world folder \"" + worldAccess.getLevelId() + "\"";
/* 295 */       LOGGER.error(LogUtils.FATAL_MARKER, detailedMessage);
/* 297 */       OutOfMemoryError detailedException = new OutOfMemoryError("Ran out of memory reading level data");
/* 298 */       detailedException.initCause(e);
/* 299 */       CrashReport crashReport = CrashReport.forThrowable(detailedException, detailedMessage);
/* 300 */       CrashReportCategory worldDetails = crashReport.addCategory("World details");
/* 301 */       worldDetails.setDetail("World folder", worldAccess.getLevelId());
/* 303 */       throw new ReportedException(crashReport);
/*     */     } 
/* 305 */     openWorldCheckVersionCompatibility(worldAccess, summary, levelDataTag, onCancel);
/*     */   }
/*     */   
/*     */   private void openWorldCheckVersionCompatibility(LevelStorageSource.LevelStorageAccess worldAccess, LevelSummary summary, Dynamic<?> levelDataTag, Runnable onCancel) {
/* 309 */     if (!summary.isCompatible()) {
/* 310 */       worldAccess.safeClose();
/* 311 */       this.minecraft.gui.setScreen((Screen)new AlertScreen(onCancel, (Component)Component.translatable("selectWorld.incompatible.title").withColor(-65536), (Component)Component.translatable("selectWorld.incompatible.description", new Object[] { summary.getWorldVersionName() })));
/*     */       return;
/*     */     } 
/* 314 */     LevelSummary.BackupStatus backupStatus = summary.backupStatus();
/* 315 */     if (backupStatus.shouldBackup()) {
/* 316 */       String questionKey = "selectWorld.backupQuestion." + backupStatus.getTranslationKey();
/* 317 */       String warningKey = "selectWorld.backupWarning." + backupStatus.getTranslationKey();
/* 318 */       MutableComponent backupQuestion = Component.translatable(questionKey);
/* 319 */       if (backupStatus.isSevere())
/* 320 */         backupQuestion.withColor(-2142128); 
/* 322 */       MutableComponent mutableComponent1 = Component.translatable(warningKey, new Object[] { summary.getWorldVersionName(), SharedConstants.getCurrentVersion().name() });
/* 324 */       this.minecraft.gui.setScreen((Screen)new BackupConfirmScreen(() -> {
/*     */               worldAccess.safeClose();
/*     */               onCancel.run();
/* 324 */             }(backup, eraseCache) -> createBackupAndOpenWorld(worldAccess, levelDataTag, onCancel, backup), (Component)backupQuestion, (Component)mutableComponent1, false));
/*     */     } else {
/* 335 */       upgradeAndOpenWorld(worldAccess, levelDataTag, onCancel);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void createBackupAndOpenWorld(LevelStorageSource.LevelStorageAccess levelAccess, Dynamic<?> levelDataTag, Runnable onCancel, boolean backup) {
/* 340 */     EditWorldScreen.conditionallyMakeBackupAndShowToast(backup, levelAccess).thenAcceptAsync(paramBoolean -> upgradeAndOpenWorld(levelAccess, levelDataTag, onCancel), (Executor)this.minecraft);
/*     */   }
/*     */   
/*     */   private void upgradeAndOpenWorld(LevelStorageSource.LevelStorageAccess worldAccess, Dynamic<?> levelDataTag, Runnable onCancel) {
/* 346 */     Runnable cleanup = () -> {
/*     */         worldAccess.safeClose();
/*     */         onCancel.run();
/*     */       };
/* 350 */     int dataVersion = NbtUtils.getDataVersion(levelDataTag);
/* 351 */     boolean requiresFileFixing = DataFixers.getFileFixer().requiresFileFixing(dataVersion);
/* 353 */     UpgradeProgress upgradeProgress = new UpgradeProgress();
/* 354 */     if (requiresFileFixing) {
/* 355 */       FileFixerProgressScreen progressScreen = new FileFixerProgressScreen(upgradeProgress);
/* 356 */       this.minecraft.setScreenAndShow(progressScreen);
/*     */     } 
/* 359 */     Util.backgroundExecutor().execute(() -> {
/*     */           Dynamic<?> levelDataTagFixed = tryFileFixAndReportErrors(worldAccess, levelDataTag, upgradeProgress, cleanup);
/*     */           if (levelDataTagFixed == null)
/*     */             return; 
/*     */           this.minecraft.execute(());
/*     */         });
/*     */   }
/*     */   
/*     */   private Dynamic<?> tryFileFixAndReportErrors(LevelStorageSource.LevelStorageAccess worldAccess, Dynamic<?> levelDataTag, UpgradeProgress upgradeProgress, Runnable cleanup) {
/*     */     Dynamic<?> levelDataTagFixed;
/*     */     try {
/* 388 */       levelDataTagFixed = DataFixers.getFileFixer().fix(worldAccess, levelDataTag, upgradeProgress);
/* 389 */     } catch (CanceledFileFixException e) {
/* 390 */       this.minecraft.execute(() -> this.minecraft.setScreenAndShow((Screen)new AlertScreen(cleanup, (Component)Component.translatable("upgradeWorld.canceled.title"), (Component)Component.translatable("upgradeWorld.canceled.message"), CommonComponents.GUI_OK, true)));
/* 391 */       return null;
/* 392 */     } catch (AbortedFileFixException e) {
/* 393 */       this.minecraft.execute(() -> {
/*     */             if (e.getCause() instanceof net.minecraft.util.filefix.virtualfilesystem.exception.CowFSSymlinkException) {
/*     */               this.minecraft.setScreenAndShow((Screen)new AlertScreen(cleanup, (Component)Component.translatable("upgradeWorld.symlink.title"), (Component)Component.translatable("upgradeWorld.symlink.message")));
/*     */             } else {
/*     */               this.minecraft.setScreenAndShow((Screen)new FileFixerAbortedScreen(cleanup, (Component)Component.translatable("upgradeWorld.aborted.message")));
/*     */             } 
/*     */           });
/* 400 */       return null;
/* 401 */     } catch (FailedCleanupFileFixException e) {
/* 402 */       this.minecraft.execute(() -> this.minecraft.setScreenAndShow((Screen)new AlertScreen(cleanup, (Component)Component.translatable("upgradeWorld.failed_cleanup.title"), (Component)Component.translatable("upgradeWorld.failed_cleanup.message", new Object[] { Component.literal(e.newWorldFolderName()).withColor(-8355712) }))));
/* 405 */       return null;
/* 406 */     } catch (FileFixException e) {
/* 407 */       this.minecraft.delayCrash(e.makeReportedException().getReport());
/* 408 */       return null;
/* 409 */     } catch (Exception e) {
/* 410 */       LOGGER.error("Failed to upgrade the file structure of the world.", e);
/* 411 */       CrashReport report = CrashReport.forThrowable(e, "Failed to update file structure");
/* 412 */       this.minecraft.delayCrash(report);
/* 413 */       return null;
/*     */     } 
/* 415 */     return levelDataTagFixed;
/*     */   }
/*     */   
/*     */   private void openWorldLoadLevelStem(LevelStorageSource.LevelStorageAccess worldAccess, Dynamic<?> levelDataTag, boolean safeMode, Runnable onCancel) {
/*     */     WorldStem worldStem;
/* 419 */     this.minecraft.setScreenAndShow((Screen)new GenericMessageScreen((Component)Component.translatable("selectWorld.resource_load")));
/* 420 */     PackRepository packRepository = ServerPacksSource.createPackRepository(worldAccess);
/*     */     try {
/* 424 */       worldStem = loadWorldStem(worldAccess, levelDataTag, safeMode, packRepository);
/* 425 */       for (LevelStem levelStem : worldStem.registries().compositeAccess().lookupOrThrow(Registries.LEVEL_STEM))
/* 426 */         levelStem.generator().validate(); 
/* 428 */     } catch (Exception e) {
/* 429 */       LOGGER.warn("Failed to load level data or datapacks, can't proceed with server load", e);
/* 430 */       if (!safeMode) {
/* 431 */         this.minecraft.gui.setScreen((Screen)new DatapackLoadFailureScreen(() -> {
/*     */                 worldAccess.safeClose();
/*     */                 onCancel.run();
/*     */               }() -> openWorldLoadLevelStem(worldAccess, levelDataTag, true, onCancel)));
/*     */       } else {
/* 436 */         worldAccess.safeClose();
/* 437 */         this.minecraft.gui.setScreen((Screen)new AlertScreen(onCancel, (Component)Component.translatable("datapackFailure.safeMode.failed.title"), (Component)Component.translatable("datapackFailure.safeMode.failed.description"), CommonComponents.GUI_BACK, true));
/*     */       } 
/*     */       return;
/*     */     } 
/* 442 */     openWorldCheckWorldStemCompatibility(worldAccess, worldStem, packRepository, onCancel);
/*     */   }
/*     */   
/*     */   private void openWorldCheckWorldStemCompatibility(LevelStorageSource.LevelStorageAccess worldAccess, WorldStem worldStem, PackRepository packRepository, Runnable onCancel) {
/* 446 */     LevelDataAndDimensions.WorldDataAndGenSettings worldDataAndGenSettings = worldStem.worldDataAndGenSettings();
/* 447 */     WorldData data = worldDataAndGenSettings.data();
/* 449 */     boolean oldCustomized = worldDataAndGenSettings.genSettings().options().isOldCustomizedWorld();
/* 450 */     boolean unstable = (data.worldGenSettingsLifecycle() != Lifecycle.stable());
/* 452 */     if (oldCustomized || unstable) {
/* 453 */       askForBackup(worldAccess, oldCustomized, () -> openWorldLoadBundledResourcePack(worldAccess, worldStem, packRepository, onCancel), () -> {
/*     */             worldStem.close();
/*     */             worldAccess.safeClose();
/*     */             onCancel.run();
/*     */           });
/*     */       return;
/*     */     } 
/* 461 */     openWorldLoadBundledResourcePack(worldAccess, worldStem, packRepository, onCancel);
/*     */   }
/*     */   
/*     */   private void openWorldLoadBundledResourcePack(LevelStorageSource.LevelStorageAccess worldAccess, WorldStem worldStem, PackRepository packRepository, Runnable onCancel) {
/* 465 */     DownloadedPackSource packSource = this.minecraft.getDownloadedPackSource();
/* 466 */     loadBundledResourcePack(packSource, worldAccess)
/* 467 */       .thenApply(unused -> Boolean.valueOf(true))
/* 468 */       .exceptionallyComposeAsync(t -> {
/*     */           LOGGER.warn("Failed to load pack: ", t);
/*     */           return promptBundledPackLoadFailure();
/* 472 */         }(Executor)this.minecraft).thenAcceptAsync(result -> {
/*     */           if (result.booleanValue()) {
/*     */             openWorldCheckDiskSpace(worldAccess, worldStem, packSource, packRepository, onCancel);
/*     */           } else {
/*     */             packSource.popAll();
/*     */             worldStem.close();
/*     */             worldAccess.safeClose();
/*     */             onCancel.run();
/*     */           } 
/* 482 */         }(Executor)this.minecraft).exceptionally(e -> {
/*     */           this.minecraft.delayCrash(CrashReport.forThrowable(e, "Load world"));
/*     */           return null;
/*     */         });
/*     */   }
/*     */   
/*     */   private void openWorldCheckDiskSpace(LevelStorageSource.LevelStorageAccess worldAccess, WorldStem worldStem, DownloadedPackSource packSource, PackRepository packRepository, Runnable onCancel) {
/* 489 */     if (worldAccess.checkForLowDiskSpace()) {
/* 502 */       ConfirmScreen confirmScreen = new ConfirmScreen(skip -> {
/*     */             if (skip) {
/*     */               openWorldDoLoad(worldAccess, worldStem, packRepository);
/*     */             } else {
/*     */               packSource.popAll();
/*     */               worldStem.close();
/*     */               worldAccess.safeClose();
/*     */               onCancel.run();
/*     */             } 
/* 502 */           }(Component)Component.translatable("selectWorld.warning.lowDiskSpace.title").withStyle(ChatFormatting.RED), (Component)Component.translatable("selectWorld.warning.lowDiskSpace.description"), CommonComponents.GUI_CONTINUE, CommonComponents.GUI_BACK);
/* 506 */       this.minecraft.gui.setScreen((Screen)confirmScreen);
/*     */     } else {
/* 508 */       openWorldDoLoad(worldAccess, worldStem, packRepository);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void openWorldDoLoad(LevelStorageSource.LevelStorageAccess worldAccess, WorldStem worldStem, PackRepository packRepository) {
/* 513 */     this.minecraft.doWorldLoad(worldAccess, packRepository, worldStem, Optional.empty(), false);
/*     */   }
/*     */   
/*     */   private CompletableFuture<Void> loadBundledResourcePack(DownloadedPackSource packSource, LevelStorageSource.LevelStorageAccess levelSourceAccess) {
/* 517 */     Path mapResourceFile = levelSourceAccess.getLevelPath(LevelResource.MAP_RESOURCE_FILE);
/* 518 */     if (Files.exists(mapResourceFile, new java.nio.file.LinkOption[0]) && !Files.isDirectory(mapResourceFile, new java.nio.file.LinkOption[0])) {
/* 519 */       packSource.configureForLocalWorld();
/* 520 */       CompletableFuture<Void> result = packSource.waitForPackFeedback(WORLD_PACK_ID);
/* 521 */       packSource.pushLocalPack(WORLD_PACK_ID, mapResourceFile);
/* 522 */       return result;
/*     */     } 
/* 524 */     return CompletableFuture.completedFuture(null);
/*     */   }
/*     */   
/*     */   private CompletableFuture<Boolean> promptBundledPackLoadFailure() {
/* 529 */     CompletableFuture<Boolean> result = new CompletableFuture<>();
/* 531 */     Objects.requireNonNull(result);
/* 533 */     ConfirmScreen confirmScreen = new ConfirmScreen(result::complete, (Component)Component.translatable("multiplayer.texturePrompt.failure.line1"), (Component)Component.translatable("multiplayer.texturePrompt.failure.line2"), CommonComponents.GUI_PROCEED, CommonComponents.GUI_CANCEL);
/* 537 */     this.minecraft.gui.setScreen((Screen)confirmScreen);
/* 538 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\WorldOpenFlows.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */