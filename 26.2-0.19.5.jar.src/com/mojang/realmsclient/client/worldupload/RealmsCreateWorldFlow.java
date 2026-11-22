/*     */ package com.mojang.realmsclient.client.worldupload;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RealmsSetting;
/*     */ import com.mojang.realmsclient.dto.RealmsSlot;
/*     */ import com.mojang.realmsclient.dto.RealmsWorldOptions;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
/*     */ import com.mojang.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
/*     */ import com.mojang.realmsclient.util.task.RealmCreationTask;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.screens.AlertScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.LevelDataAndDimensions;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.WorldData;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsCreateWorldFlow {
/*  37 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static void createWorld(Minecraft minecraft, Screen returnScreen, Screen lastScreen, int slot, RealmsServer realmsServer, RealmCreationTask realmCreationTask) {
/*  40 */     CreateWorldScreen.openFresh(minecraft, () -> minecraft.gui.setScreen(returnScreen), (createWorldScreen, finalLayers, worldDataAndGenSettings, gameRules, tempDataPackDir) -> {
/*     */           Path worldFolder;
/*     */           try {
/*     */             worldFolder = createTemporaryWorldFolder((RegistryAccess)finalLayers.compositeAccess(), worldDataAndGenSettings, gameRules, tempDataPackDir);
/*  44 */           } catch (IOException e) {
/*     */             LOGGER.warn("Failed to create temporary world folder", e);
/*     */             minecraft.gui.setScreen((Screen)new RealmsGenericErrorScreen((Component)Component.translatable("mco.create.world.failed"), lastScreen));
/*     */             return true;
/*     */           } 
/*     */           RealmsWorldOptions realmsWorldOptions = RealmsWorldOptions.createFromSettings(worldDataAndGenSettings.data().getLevelSettings(), SharedConstants.getCurrentVersion().name());
/*     */           RealmsSlot realmsSlot = new RealmsSlot(slot, realmsWorldOptions, List.of(RealmsSetting.hardcoreSetting(worldDataAndGenSettings.data().isHardcore())));
/*     */           RealmsWorldUpload realmsWorldUpload = new RealmsWorldUpload(worldFolder, realmsSlot, minecraft.getUser(), realmsServer.id, RealmsWorldUploadStatusTracker.noOp());
/*     */           Objects.requireNonNull(realmsWorldUpload);
/*     */           minecraft.setScreenAndShow((Screen)new AlertScreen(realmsWorldUpload::cancel, (Component)Component.translatable("mco.create.world.reset.title"), (Component)Component.empty(), CommonComponents.GUI_CANCEL, false));
/*     */           if (realmCreationTask != null)
/*     */             realmCreationTask.run(); 
/*     */           realmsWorldUpload.packAndUpload().handleAsync((), (Executor)minecraft);
/*     */           return true;
/*     */         });
/*     */   }
/*     */   
/*     */   private static Path createTemporaryWorldFolder(RegistryAccess registryAccess, LevelDataAndDimensions.WorldDataAndGenSettings worldDataAndGenSettings, Optional<GameRules> gameRulesOpt, Path tempDataPackDir) throws IOException {
/*  90 */     Path worldFolder = Files.createTempDirectory("minecraft_realms_world_upload", (FileAttribute<?>[])new FileAttribute[0]);
/*  91 */     if (tempDataPackDir != null)
/*  92 */       Files.move(tempDataPackDir, worldFolder.resolve("datapacks"), new java.nio.file.CopyOption[0]); 
/*  95 */     WorldData worldData = worldDataAndGenSettings.data();
/*  97 */     CompoundTag dataTag = worldData.createTag(null);
/*  98 */     CompoundTag root = new CompoundTag();
/*  99 */     root.put("Data", (Tag)dataTag);
/* 100 */     Path levelDat = Files.createFile(worldFolder.resolve("level.dat"), (FileAttribute<?>[])new FileAttribute[0]);
/* 101 */     NbtIo.writeCompressed(root, levelDat);
/* 102 */     LevelStorageSource.writeWorldGenSettings(registryAccess, worldFolder, worldDataAndGenSettings.genSettings());
/* 103 */     if (gameRulesOpt.isPresent())
/* 104 */       LevelStorageSource.writeGameRules(worldData, worldFolder, gameRulesOpt.get()); 
/* 106 */     return worldFolder;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\worldupload\RealmsCreateWorldFlow.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */