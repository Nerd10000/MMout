/*     */ package net.minecraft.util.filefix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import net.minecraft.util.filefix.FileFix;
/*     */ import net.minecraft.util.filefix.access.CompressedNbt;
/*     */ import net.minecraft.util.filefix.access.FileAccess;
/*     */ import net.minecraft.util.filefix.access.FileAccessProvider;
/*     */ import net.minecraft.util.filefix.access.FileRelation;
/*     */ import net.minecraft.util.filefix.access.FileResourceTypes;
/*     */ import net.minecraft.util.filefix.access.LevelDat;
/*     */ import net.minecraft.util.filefix.access.PlayerData;
/*     */ import net.minecraft.util.filefix.access.SavedDataNbt;
/*     */ import net.minecraft.util.filefix.operations.ModifyContent;
/*     */ import net.minecraft.util.worldupdate.UpgradeProgress;
/*     */ 
/*     */ public class LevelDatToSavedDataFileFix extends FileFix {
/*  32 */   private static final UUID FALLBACK_SINGLE_PLAYER_UUID = Util.NIL_UUID;
/*     */   
/*     */   private static final String OVERWORLD = "overworld";
/*     */   
/*     */   private static final String THE_NETHER = "the_nether";
/*     */   
/*     */   private static final String THE_END = "the_end";
/*     */   
/*     */   private static final String WORLD_BORDER_KEY = "world_border";
/*     */   
/*     */   private static final String WORLD_BORDER_FILE_NAME = "minecraft/world_border.dat";
/*     */   
/*     */   public LevelDatToSavedDataFileFix(Schema schema) {
/*  40 */     super(schema);
/*     */   }
/*     */   
/*     */   public void makeFixer() {
/*  45 */     addFileContentFix(files -> {
/*     */           FileAccess<LevelDat> levelDat = files.getFileAccess(FileResourceTypes.LEVEL_DAT, FileRelation.ORIGIN.forFile("level.dat"));
/*     */           FileAccess<SavedDataNbt> dragonFight = files.getFileAccess(FileResourceTypes.savedData(References.SAVED_DATA_ENDER_DRAGON_FIGHT), FileRelation.forDataFileInDimension("the_end", "minecraft/ender_dragon_fight.dat"));
/*     */           FileAccess<PlayerData> fallbackPlayerData = files.getFileAccess(FileResourceTypes.PLAYER_DATA, FileRelation.PLAYER_DATA.forFile(String.valueOf(FALLBACK_SINGLE_PLAYER_UUID) + ".dat"));
/*     */           FileAccess<SavedDataNbt> wanderingTrader = files.getFileAccess(FileResourceTypes.savedData(References.SAVED_DATA_WANDERING_TRADER), FileRelation.DATA.forFile("minecraft/wandering_trader.dat"));
/*     */           FileAccess<SavedDataNbt> customBossEvents = files.getFileAccess(FileResourceTypes.savedData(References.SAVED_DATA_CUSTOM_BOSS_EVENTS), FileRelation.DATA.forFile("minecraft/custom_boss_events.dat"));
/*     */           FileAccess<SavedDataNbt> weatherData = files.getFileAccess(FileResourceTypes.savedData(References.SAVED_DATA_WEATHER), FileRelation.DATA.forFile("minecraft/weather.dat"));
/*     */           FileAccess<SavedDataNbt> scheduledEvents = files.getFileAccess(FileResourceTypes.savedData(References.SAVED_DATA_SCHEDULED_EVENTS), FileRelation.DATA.forFile("minecraft/scheduled_events.dat"));
/*     */           FileAccess<SavedDataNbt> worldBorderOverworld = files.getFileAccess(FileResourceTypes.savedData(References.SAVED_DATA_WORLD_BORDER), FileRelation.forDataFileInDimension("overworld", "minecraft/world_border.dat"));
/*     */           FileAccess<SavedDataNbt> worldBorderNether = files.getFileAccess(FileResourceTypes.savedData(References.SAVED_DATA_WORLD_BORDER), FileRelation.forDataFileInDimension("the_nether", "minecraft/world_border.dat"));
/*     */           FileAccess<SavedDataNbt> worldBorderEnd = files.getFileAccess(FileResourceTypes.savedData(References.SAVED_DATA_WORLD_BORDER), FileRelation.forDataFileInDimension("the_end", "minecraft/world_border.dat"));
/*     */           FileAccess<SavedDataNbt> gameRules = files.getFileAccess(FileResourceTypes.savedData(References.SAVED_DATA_GAME_RULES), FileRelation.DATA.forFile("minecraft/game_rules.dat"));
/*     */           FileAccess<SavedDataNbt> worldGenSettings = files.getFileAccess(FileResourceTypes.savedData(References.SAVED_DATA_WORLD_GEN_SETTINGS), FileRelation.DATA.forFile("minecraft/world_gen_settings.dat"));
/*     */           FileAccess<SavedDataNbt> worldClocks = files.getFileAccess(FileResourceTypes.savedData(References.SAVED_DATA_WORLD_CLOCKS), FileRelation.DATA.forFile("minecraft/world_clocks.dat"));
/*     */           return ();
/*     */         });
/*     */   }
/*     */   
/*     */   private static Dynamic<?> extractToFile(FileAccess<? extends CompressedNbt> targetFile, Dynamic<?> content, String key) {
/*  83 */     OptionalDynamic<?> tagOpt = content.get(key);
/*  84 */     if (tagOpt.result().isEmpty())
/*  85 */       return content; 
/*  87 */     Dynamic<?> tag = tagOpt.result().get();
/*  88 */     ((CompressedNbt)targetFile.getOnlyFile()).write(tag);
/*  89 */     return content.remove(key);
/*     */   }
/*     */   
/*     */   private Dynamic<?> extractPlayerDataToFile(FileAccess<PlayerData> fallbackFile, Dynamic<?> content) {
/*     */     Dynamic<?> usedUuid;
/*  93 */     OptionalDynamic<?> playerTagOpt = content.get("Player");
/*  94 */     if (playerTagOpt.result().isEmpty())
/*  95 */       return content; 
/*  97 */     Dynamic<?> playerTag = playerTagOpt.result().get();
/*  98 */     int dataVersion = NbtUtils.getDataVersion(playerTag);
/*  99 */     Dynamic<?> playerTagFixed = DataFixTypes.PLAYER.update(DataFixers.getDataFixer(), playerTag, dataVersion, getVersion());
/* 100 */     Optional<? extends Dynamic<?>> playerUuid = playerTagFixed.get("UUID").result();
/* 102 */     if (playerUuid.isPresent()) {
/* 103 */       usedUuid = playerUuid.get();
/*     */     } else {
/* 105 */       ((PlayerData)fallbackFile.getOnlyFile()).write(playerTagFixed);
/* 106 */       usedUuid = content.createIntList(Arrays.stream(UUIDUtil.uuidToIntArray(FALLBACK_SINGLE_PLAYER_UUID)));
/*     */     } 
/* 108 */     return content.remove("Player").set("singleplayer_uuid", usedUuid);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> extractWorldBorderToFiles(FileAccess<? extends CompressedNbt> worldBorderOverworld, FileAccess<? extends CompressedNbt> worldBorderNether, FileAccess<? extends CompressedNbt> worldBorderEnd, Dynamic<?> content) {
/* 113 */     extractWorldBorderToFile(worldBorderOverworld, content, 1.0D);
/* 114 */     extractWorldBorderToFile(worldBorderNether, content, 8.0D);
/* 115 */     extractWorldBorderToFile(worldBorderEnd, content, 1.0D);
/* 116 */     return content.remove("world_border");
/*     */   }
/*     */   
/*     */   private static void extractWorldBorderToFile(FileAccess<? extends CompressedNbt> targetFile, Dynamic<?> content, double divider) {
/* 120 */     OptionalDynamic<?> worldBorderTagOpt = content.get("world_border");
/* 121 */     if (worldBorderTagOpt.result().isEmpty())
/*     */       return; 
/* 126 */     Dynamic<?> worldBorderTag = ((Dynamic)worldBorderTagOpt.result().get()).update("center_x", x -> x.createDouble(x.asDouble(0.0D) / divider)).update("center_z", z -> z.createDouble(z.asDouble(0.0D) / divider));
/* 127 */     ((CompressedNbt)targetFile.getOnlyFile()).write(worldBorderTag);
/*     */   }
/*     */   
/*     */   private Dynamic<?> extractWorldGenSettingsToFile(FileAccess<? extends CompressedNbt> targetFile, Dynamic<?> content) {
/* 131 */     OptionalDynamic<?> worldGenSettingsTagOpt = content.get("world_gen_settings");
/* 132 */     if (worldGenSettingsTagOpt.result().isEmpty())
/* 133 */       return content; 
/* 135 */     Dynamic<?> worldGenSettingsTag = worldGenSettingsTagOpt.result().get();
/* 136 */     int dataVersion = NbtUtils.getDataVersion(content);
/* 137 */     Dynamic<?> worldGenSettingsTagFixed = DataFixTypes.WORLD_GEN_SETTINGS.update(DataFixers.getDataFixer(), worldGenSettingsTag, dataVersion, getVersion());
/* 138 */     ((CompressedNbt)targetFile.getOnlyFile()).write(worldGenSettingsTagFixed);
/* 139 */     return content.remove("world_gen_settings");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\fixes\LevelDatToSavedDataFileFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */