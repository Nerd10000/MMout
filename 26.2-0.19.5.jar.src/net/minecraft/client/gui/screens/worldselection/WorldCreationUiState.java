/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalLong;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.tags.WorldPresetTags;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import net.minecraft.world.level.storage.LevelStorageException;
/*     */ 
/*     */ public class WorldCreationUiState {
/*  32 */   private static final Component DEFAULT_WORLD_NAME = (Component)Component.translatable("selectWorld.newWorld");
/*     */   
/*  34 */   private final List<Consumer<WorldCreationUiState>> listeners = new ArrayList<>();
/*     */   
/*  36 */   private String name = DEFAULT_WORLD_NAME.getString();
/*     */   
/*  38 */   private SelectedGameMode gameMode = SelectedGameMode.SURVIVAL;
/*     */   
/*  39 */   private Difficulty difficulty = Difficulty.NORMAL;
/*     */   
/*     */   private Boolean allowCommands;
/*     */   
/*     */   private String seed;
/*     */   
/*     */   private boolean generateStructures;
/*     */   
/*     */   private boolean bonusChest;
/*     */   
/*     */   private final Path savesFolder;
/*     */   
/*     */   private String targetFolder;
/*     */   
/*     */   private WorldCreationContext settings;
/*     */   
/*     */   private WorldTypeEntry worldType;
/*     */   
/*  49 */   private final List<WorldTypeEntry> normalPresetList = new ArrayList<>();
/*     */   
/*  50 */   private final List<WorldTypeEntry> altPresetList = new ArrayList<>();
/*     */   
/*     */   private GameRules gameRules;
/*     */   
/*     */   public WorldCreationUiState(Path savesFolder, WorldCreationContext settings, Optional<ResourceKey<WorldPreset>> preset, OptionalLong seed) {
/*  55 */     this.savesFolder = savesFolder;
/*  56 */     this.settings = settings;
/*  57 */     this.worldType = new WorldTypeEntry(findPreset(settings, preset).orElse(null));
/*  58 */     updatePresetLists();
/*  59 */     this.seed = seed.isPresent() ? Long.toString(seed.getAsLong()) : "";
/*  60 */     this.generateStructures = settings.options().generateStructures();
/*  61 */     this.bonusChest = settings.options().generateBonusChest();
/*  62 */     this.targetFolder = findResultFolder(this.name);
/*  63 */     this.gameMode = settings.initialWorldCreationOptions().selectedGameMode();
/*  64 */     this.gameRules = new GameRules(settings.dataConfiguration().enabledFeatures());
/*  65 */     this.gameRules.setAll(settings.initialWorldCreationOptions().gameRuleOverwrites(), null);
/*  66 */     Optional.<ResourceKey<FlatLevelGeneratorPreset>>ofNullable(settings.initialWorldCreationOptions().flatLevelPreset())
/*  67 */       .flatMap(key -> settings.worldgenLoadContext().lookup(Registries.FLAT_LEVEL_GENERATOR_PRESET).flatMap(()))
/*  68 */       .map(reference -> ((FlatLevelGeneratorPreset)reference.value()).settings())
/*  69 */       .ifPresent(generatorSettings -> updateDimensions(PresetEditor.flatWorldConfigurator(generatorSettings)));
/*     */   }
/*     */   
/*     */   public void addListener(Consumer<WorldCreationUiState> action) {
/*  73 */     this.listeners.add(action);
/*     */   }
/*     */   
/*     */   public void onChanged() {
/*  77 */     boolean bonusChest = isBonusChest();
/*  78 */     if (bonusChest != this.settings.options().generateBonusChest())
/*  79 */       this.settings = this.settings.withOptions(options -> options.withBonusChest(bonusChest)); 
/*  81 */     boolean generateStructures = isGenerateStructures();
/*  82 */     if (generateStructures != this.settings.options().generateStructures())
/*  83 */       this.settings = this.settings.withOptions(options -> options.withStructures(generateStructures)); 
/*  85 */     for (Consumer<WorldCreationUiState> listener : this.listeners)
/*  86 */       listener.accept(this); 
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  91 */     this.name = name;
/*  92 */     this.targetFolder = findResultFolder(name);
/*  93 */     onChanged();
/*     */   }
/*     */   
/*     */   private String findResultFolder(String name) {
/*  97 */     if (!Files.exists(this.savesFolder, new java.nio.file.LinkOption[0]))
/*     */       try {
/*  99 */         Files.createDirectory(this.savesFolder, (FileAttribute<?>[])new FileAttribute[0]);
/* 100 */       } catch (IOException e) {
/* 101 */         throw new LevelStorageException(Component.literal("Could not create save folder"), e);
/*     */       }  
/* 104 */     String trimmedName = name.trim();
/*     */     try {
/* 106 */       return FileUtil.findAvailableName(this.savesFolder, !trimmedName.isEmpty() ? trimmedName : DEFAULT_WORLD_NAME.getString(), "");
/* 107 */     } catch (Exception exception) {
/*     */       try {
/* 110 */         return FileUtil.findAvailableName(this.savesFolder, "World", "");
/* 111 */       } catch (IOException e) {
/* 112 */         throw new LevelStorageException(Component.literal("Could not create world folder"), e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getName() {
/* 117 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getTargetFolder() {
/* 121 */     return this.targetFolder;
/*     */   }
/*     */   
/*     */   public void setGameMode(SelectedGameMode gameMode) {
/* 125 */     this.gameMode = gameMode;
/* 126 */     onChanged();
/*     */   }
/*     */   
/*     */   public SelectedGameMode getGameMode() {
/* 130 */     if (isDebug())
/* 131 */       return SelectedGameMode.DEBUG; 
/* 133 */     return this.gameMode;
/*     */   }
/*     */   
/*     */   public void setDifficulty(Difficulty difficulty) {
/* 137 */     this.difficulty = difficulty;
/* 138 */     onChanged();
/*     */   }
/*     */   
/*     */   public Difficulty getDifficulty() {
/* 142 */     if (isHardcore())
/* 143 */       return Difficulty.HARD; 
/* 145 */     return this.difficulty;
/*     */   }
/*     */   
/*     */   public boolean isHardcore() {
/* 149 */     return (getGameMode() == SelectedGameMode.HARDCORE);
/*     */   }
/*     */   
/*     */   public void setAllowCommands(boolean allowCommands) {
/* 153 */     this.allowCommands = Boolean.valueOf(allowCommands);
/* 154 */     onChanged();
/*     */   }
/*     */   
/*     */   public boolean isAllowCommands() {
/* 158 */     if (isDebug())
/* 159 */       return true; 
/* 161 */     if (isHardcore())
/* 162 */       return false; 
/* 164 */     if (this.allowCommands == null)
/* 165 */       return (getGameMode() == SelectedGameMode.CREATIVE); 
/* 167 */     return this.allowCommands.booleanValue();
/*     */   }
/*     */   
/*     */   public void setSeed(String seed) {
/* 171 */     this.seed = seed;
/* 172 */     this.settings = this.settings.withOptions(options -> options.withSeed(WorldOptions.parseSeed(getSeed())));
/* 173 */     onChanged();
/*     */   }
/*     */   
/*     */   public String getSeed() {
/* 177 */     return this.seed;
/*     */   }
/*     */   
/*     */   public void setGenerateStructures(boolean generateStructures) {
/* 181 */     this.generateStructures = generateStructures;
/* 182 */     onChanged();
/*     */   }
/*     */   
/*     */   public boolean isGenerateStructures() {
/* 186 */     if (isDebug())
/* 187 */       return false; 
/* 189 */     return this.generateStructures;
/*     */   }
/*     */   
/*     */   public void setBonusChest(boolean bonusChest) {
/* 193 */     this.bonusChest = bonusChest;
/* 194 */     onChanged();
/*     */   }
/*     */   
/*     */   public boolean isBonusChest() {
/* 198 */     if (isDebug() || isHardcore())
/* 199 */       return false; 
/* 201 */     return this.bonusChest;
/*     */   }
/*     */   
/*     */   public void setSettings(WorldCreationContext settings) {
/* 205 */     this.settings = settings;
/* 206 */     updatePresetLists();
/* 207 */     onChanged();
/*     */   }
/*     */   
/*     */   public WorldCreationContext getSettings() {
/* 211 */     return this.settings;
/*     */   }
/*     */   
/*     */   public void updateDimensions(WorldCreationContext.DimensionsUpdater modifier) {
/* 215 */     this.settings = this.settings.withDimensions(modifier);
/* 216 */     onChanged();
/*     */   }
/*     */   
/*     */   protected boolean tryUpdateDataConfiguration(WorldDataConfiguration newConfig) {
/* 220 */     WorldDataConfiguration oldConfig = this.settings.dataConfiguration();
/* 221 */     if (oldConfig.dataPacks().getEnabled().equals(newConfig.dataPacks().getEnabled()) && oldConfig
/* 222 */       .enabledFeatures().equals(newConfig.enabledFeatures())) {
/* 225 */       this.settings = new WorldCreationContext(this.settings.options(), this.settings.datapackDimensions(), this.settings.selectedDimensions(), this.settings.worldgenRegistries(), this.settings.dataPackResources(), newConfig, this.settings.initialWorldCreationOptions());
/* 226 */       return true;
/*     */     } 
/* 228 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isDebug() {
/* 232 */     return this.settings.selectedDimensions().isDebug();
/*     */   }
/*     */   
/*     */   public void setWorldType(WorldTypeEntry worldType) {
/* 236 */     this.worldType = worldType;
/* 237 */     Holder<WorldPreset> preset = worldType.preset();
/* 238 */     if (preset != null)
/* 239 */       updateDimensions((registryAccess, dimensions) -> ((WorldPreset)preset.value()).createWorldDimensions()); 
/*     */   }
/*     */   
/*     */   public WorldTypeEntry getWorldType() {
/* 244 */     return this.worldType;
/*     */   }
/*     */   
/*     */   public PresetEditor getPresetEditor() {
/* 248 */     Holder<WorldPreset> preset = getWorldType().preset();
/* 249 */     return (preset != null) ? PresetEditor.EDITORS.get(preset.unwrapKey()) : null;
/*     */   }
/*     */   
/*     */   public List<WorldTypeEntry> getNormalPresetList() {
/* 253 */     return this.normalPresetList;
/*     */   }
/*     */   
/*     */   public List<WorldTypeEntry> getAltPresetList() {
/* 257 */     return this.altPresetList;
/*     */   }
/*     */   
/*     */   private void updatePresetLists() {
/* 261 */     Registry<WorldPreset> presetRegistry = getSettings().worldgenLoadContext().lookupOrThrow(Registries.WORLD_PRESET);
/* 263 */     this.normalPresetList.clear();
/* 264 */     this.normalPresetList.addAll(getNonEmptyList(presetRegistry, WorldPresetTags.NORMAL).orElseGet(() -> presetRegistry.listElements().map(WorldTypeEntry::new).toList()));
/* 265 */     this.altPresetList.clear();
/* 266 */     this.altPresetList.addAll(getNonEmptyList(presetRegistry, WorldPresetTags.EXTENDED).orElse(this.normalPresetList));
/* 267 */     Holder<WorldPreset> preset = this.worldType.preset();
/* 268 */     if (preset != null) {
/* 269 */       WorldTypeEntry newPreset = findPreset(getSettings(), preset.unwrapKey()).<WorldTypeEntry>map(WorldTypeEntry::new).orElse(this.normalPresetList.getFirst());
/* 270 */       boolean isCustomizablePreset = (PresetEditor.EDITORS.get(preset.unwrapKey()) != null);
/* 271 */       if (isCustomizablePreset) {
/* 274 */         this.worldType = newPreset;
/*     */       } else {
/* 277 */         setWorldType(newPreset);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final class WorldTypeEntry extends Record {
/*     */     private final Holder<WorldPreset> preset;
/*     */     
/*     */     public WorldTypeEntry(Holder<WorldPreset> preset) {
/* 282 */       this.preset = preset;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #282	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #282	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #282	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Holder<WorldPreset> preset() {
/* 282 */       return this.preset;
/*     */     }
/*     */     
/* 283 */     private static final Component CUSTOM_WORLD_DESCRIPTION = (Component)Component.translatable("generator.custom");
/*     */     
/*     */     public Component describePreset() {
/* 286 */       return Optional.<Holder<WorldPreset>>ofNullable(this.preset).flatMap(Holder::unwrapKey)
/* 287 */         .map(key -> Component.translatable(key.identifier().toLanguageKey("generator")))
/* 288 */         .orElse(CUSTOM_WORLD_DESCRIPTION);
/*     */     }
/*     */     
/*     */     public boolean isAmplified() {
/* 292 */       return Optional.<Holder<WorldPreset>>ofNullable(this.preset).flatMap(Holder::unwrapKey).filter(k -> k.equals(WorldPresets.AMPLIFIED)).isPresent();
/*     */     }
/*     */   }
/*     */   
/*     */   private static Optional<Holder<WorldPreset>> findPreset(WorldCreationContext settings, Optional<ResourceKey<WorldPreset>> preset) {
/* 297 */     return preset.flatMap(k -> settings.worldgenLoadContext().lookupOrThrow(Registries.WORLD_PRESET).get(k));
/*     */   }
/*     */   
/*     */   private static Optional<List<WorldTypeEntry>> getNonEmptyList(Registry<WorldPreset> presetRegistry, TagKey<WorldPreset> id) {
/* 301 */     return presetRegistry.get(id).map(tag -> tag.stream().map(WorldTypeEntry::new).toList()).filter(l -> !l.isEmpty());
/*     */   }
/*     */   
/*     */   public void setGameRules(GameRules gameRules) {
/* 305 */     this.gameRules = gameRules;
/* 306 */     onChanged();
/*     */   }
/*     */   
/*     */   public GameRules getGameRules() {
/* 310 */     return this.gameRules;
/*     */   }
/*     */   
/*     */   public enum SelectedGameMode {
/* 314 */     SURVIVAL("survival", GameType.SURVIVAL),
/* 315 */     HARDCORE("hardcore", GameType.SURVIVAL),
/* 316 */     CREATIVE("creative", GameType.CREATIVE),
/* 318 */     DEBUG("spectator", GameType.SPECTATOR);
/*     */     
/*     */     public final GameType gameType;
/*     */     
/*     */     public final Component displayName;
/*     */     
/*     */     private final Component info;
/*     */     
/*     */     SelectedGameMode(String name, GameType gameType) {
/* 326 */       this.gameType = gameType;
/* 327 */       this.displayName = (Component)Component.translatable("selectWorld.gameMode." + name);
/* 328 */       this.info = (Component)Component.translatable("selectWorld.gameMode." + name + ".info");
/*     */     }
/*     */     
/*     */     public Component getInfo() {
/* 332 */       return this.info;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\WorldCreationUiState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */