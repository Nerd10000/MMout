/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalLong;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.components.tabs.GridLayoutTab;
/*     */ import net.minecraft.client.gui.components.tabs.MenuTabBar;
/*     */ import net.minecraft.client.gui.components.tabs.Tab;
/*     */ import net.minecraft.client.gui.components.tabs.TabManager;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.layouts.CommonLayouts;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.GenericMessageScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.RegistryLayer;
/*     */ import net.minecraft.server.ReloadableServerResources;
/*     */ import net.minecraft.server.WorldLoader;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.RepositorySource;
/*     */ import net.minecraft.server.packs.repository.ServerPacksSource;
/*     */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.DataPackConfig;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.gamerules.GameRuleMap;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import net.minecraft.world.level.storage.LevelDataAndDimensions;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.PrimaryLevelData;
/*     */ import net.minecraft.world.level.storage.WorldData;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class CreateWorldScreen extends Screen {
/*     */   private static final int GROUP_BOTTOM = 1;
/*     */   
/*     */   private static final int TAB_COLUMN_WIDTH = 210;
/*     */   
/*  92 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String TEMP_WORLD_PREFIX = "mcworld-";
/*     */   
/*  94 */   private static final Component GAME_MODEL_LABEL = (Component)Component.translatable("selectWorld.gameMode");
/*     */   
/*  95 */   private static final Component NAME_LABEL = (Component)Component.translatable("selectWorld.enterName");
/*     */   
/*  96 */   private static final Component EXPERIMENTS_LABEL = (Component)Component.translatable("selectWorld.experiments");
/*     */   
/*  97 */   private static final Component ALLOW_COMMANDS_INFO = (Component)Component.translatable("selectWorld.allowCommands.info");
/*     */   
/*  98 */   private static final Component PREPARING_WORLD_DATA = (Component)Component.translatable("createWorld.preparing");
/*     */   
/*     */   private static final int HORIZONTAL_BUTTON_SPACING = 10;
/*     */   
/*     */   private static final int VERTICAL_BUTTON_SPACING = 8;
/*     */   
/* 102 */   public static final Identifier TAB_HEADER_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/tab_header_background.png");
/*     */   
/* 104 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private final WorldCreationUiState uiState;
/*     */   
/*     */   private final TabManager tabManager;
/*     */   
/*     */   private boolean recreated;
/*     */   
/*     */   private final DirectoryValidator packValidator;
/*     */   
/*     */   private final CreateWorldCallback createWorldCallback;
/*     */   
/*     */   private final Runnable onClose;
/*     */   
/*     */   private Path tempDataPackDir;
/*     */   
/*     */   private PackRepository tempDataPackRepository;
/*     */   
/*     */   private MenuTabBar tabNavigationBar;
/*     */   
/*     */   private class GameTab extends GridLayoutTab {
/* 118 */     private static final Component TITLE = (Component)Component.translatable("createWorld.tab.game.title");
/*     */     
/* 119 */     private static final Component ALLOW_COMMANDS = (Component)Component.translatable("selectWorld.allowCommands");
/*     */     
/*     */     private final EditBox nameEdit;
/*     */     
/*     */     private GameTab(CreateWorldScreen this$0) {
/* 123 */       super(TITLE);
/* 127 */       GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
/* 128 */       LayoutSettings buttonLayoutSettings = helper.newCellSettings();
/* 130 */       this.nameEdit = new EditBox(this$0.font, 208, 20, (Component)Component.translatable("selectWorld.enterName"));
/* 131 */       this.nameEdit.setValue(this$0.uiState.getName());
/* 132 */       Objects.requireNonNull(this$0.uiState);
/* 132 */       this.nameEdit.setResponder(this$0.uiState::setName);
/* 133 */       this$0.uiState.addListener(uiState -> this.nameEdit.setTooltip(Tooltip.create((Component)Component.translatable("selectWorld.targetFolder", new Object[] { Component.literal(uiState.getTargetFolder()).withStyle(ChatFormatting.ITALIC) }))));
/* 134 */       this$0.setInitialFocus((GuiEventListener)this.nameEdit);
/* 135 */       helper.addChild((LayoutElement)CommonLayouts.labeledElement(this$0.font, (LayoutElement)this.nameEdit, CreateWorldScreen.NAME_LABEL), helper.newCellSettings().alignHorizontallyCenter());
/* 137 */       CycleButton<WorldCreationUiState.SelectedGameMode> gameModeButton = (CycleButton<WorldCreationUiState.SelectedGameMode>)helper.addChild(
/* 138 */           (LayoutElement)CycleButton.builder(selectedGameMode -> selectedGameMode.displayName, this$0.uiState.getGameMode())
/* 139 */           .withValues((Object[])new WorldCreationUiState.SelectedGameMode[] { WorldCreationUiState.SelectedGameMode.SURVIVAL, WorldCreationUiState.SelectedGameMode.HARDCORE, WorldCreationUiState.SelectedGameMode.CREATIVE }).create(0, 0, 210, 20, CreateWorldScreen.GAME_MODEL_LABEL, (button, gameMode) -> this$0.uiState.setGameMode(gameMode)), buttonLayoutSettings);
/* 144 */       this$0.uiState.addListener(data -> {
/*     */             gameModeButton.setValue(data.getGameMode());
/*     */             gameModeButton.active = !data.isDebug();
/*     */             gameModeButton.setTooltip(Tooltip.create(data.getGameMode().getInfo()));
/*     */           });
/* 150 */       CycleButton<Difficulty> difficultyButton = (CycleButton<Difficulty>)helper.addChild(
/* 151 */           (LayoutElement)CycleButton.builder(Difficulty::getDisplayName, this$0.uiState.getDifficulty())
/* 152 */           .withValues((Object[])Difficulty.values())
/* 153 */           .create(0, 0, 210, 20, (Component)Component.translatable("options.difficulty"), (button, value) -> this$0.uiState.setDifficulty(value)), buttonLayoutSettings);
/* 156 */       this$0.uiState.addListener(d -> {
/*     */             difficultyButton.setValue(this$0.uiState.getDifficulty());
/*     */             difficultyButton.active = !this$0.uiState.isHardcore();
/*     */             difficultyButton.setTooltip(Tooltip.create(this$0.uiState.getDifficulty().getInfo()));
/*     */           });
/* 162 */       CycleButton<Boolean> allowCommandsButton = (CycleButton<Boolean>)helper.addChild((LayoutElement)CycleButton.onOffBuilder(this$0.uiState.isAllowCommands())
/* 163 */           .withTooltip(state -> Tooltip.create(CreateWorldScreen.ALLOW_COMMANDS_INFO))
/* 164 */           .create(0, 0, 210, 20, ALLOW_COMMANDS, (b, state) -> this$0.uiState.setAllowCommands(state.booleanValue())));
/* 166 */       this$0.uiState.addListener(d -> {
/*     */             allowCommandsButton.setValue(Boolean.valueOf(this$0.uiState.isAllowCommands()));
/* 168 */             allowCommandsButton.active = (!this$0.uiState.isDebug() && !this$0.uiState.isHardcore());
/*     */           });
/* 171 */       if (!SharedConstants.getCurrentVersion().stable())
/* 172 */         helper.addChild((LayoutElement)Button.builder(CreateWorldScreen.EXPERIMENTS_LABEL, button -> this$0.openExperimentsScreen(this$0.uiState.getSettings().dataConfiguration())).width(210).build()); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class WorldTab extends GridLayoutTab {
/* 178 */     private static final Component TITLE = (Component)Component.translatable("createWorld.tab.world.title");
/*     */     
/* 179 */     private static final Component AMPLIFIED_HELP_TEXT = (Component)Component.translatable("generator.minecraft.amplified.info");
/*     */     
/* 180 */     private static final Component GENERATE_STRUCTURES = (Component)Component.translatable("selectWorld.mapFeatures");
/*     */     
/* 181 */     private static final Component GENERATE_STRUCTURES_INFO = (Component)Component.translatable("selectWorld.mapFeatures.info");
/*     */     
/* 182 */     private static final Component BONUS_CHEST = (Component)Component.translatable("selectWorld.bonusItems");
/*     */     
/* 183 */     private static final Component SEED_LABEL = (Component)Component.translatable("selectWorld.enterSeed");
/*     */     
/* 184 */     private static final Component SEED_EMPTY_HINT = (Component)Component.translatable("selectWorld.seedInfo");
/*     */     
/*     */     private static final int WORLD_TAB_WIDTH = 310;
/*     */     
/*     */     private final EditBox seedEdit;
/*     */     
/*     */     private final Button customizeTypeButton;
/*     */     
/*     */     private WorldTab() {
/* 190 */       super(TITLE);
/* 195 */       GridLayout.RowHelper helper = this.layout.columnSpacing(10).rowSpacing(8).createRowHelper(2);
/* 197 */       CycleButton<WorldCreationUiState.WorldTypeEntry> typeButton = (CycleButton<WorldCreationUiState.WorldTypeEntry>)helper.addChild((LayoutElement)CycleButton.builder(WorldCreationUiState.WorldTypeEntry::describePreset, CreateWorldScreen.this.uiState.getWorldType())
/* 198 */           .withValues(createWorldTypeValueSupplier())
/* 199 */           .create(0, 0, 150, 20, (Component)Component.translatable("selectWorld.mapType"), (button, newPreset) -> CreateWorldScreen.this.uiState.setWorldType(newPreset)));
/* 202 */       typeButton.setValue(CreateWorldScreen.this.uiState.getWorldType());
/* 203 */       CreateWorldScreen.this.uiState.addListener(data -> {
/*     */             WorldCreationUiState.WorldTypeEntry worldType = data.getWorldType();
/*     */             typeButton.setValue(worldType);
/*     */             if (worldType.isAmplified()) {
/*     */               typeButton.setTooltip(Tooltip.create(AMPLIFIED_HELP_TEXT));
/*     */             } else {
/*     */               typeButton.setTooltip(null);
/*     */             } 
/*     */             typeButton.active = (CreateWorldScreen.this.uiState.getWorldType().preset() != null);
/*     */           });
/* 215 */       this.customizeTypeButton = (Button)helper.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectWorld.customizeType"), b -> openPresetEditor()).build());
/* 216 */       CreateWorldScreen.this.uiState.addListener(data -> this.customizeTypeButton.active = (!data.isDebug() && data.getPresetEditor() != null));
/* 218 */       this.seedEdit = new EditBox(this, CreateWorldScreen.this.font, 308, 20, (Component)Component.translatable("selectWorld.enterSeed")) {
/*     */           protected MutableComponent createNarrationMessage() {
/* 221 */             return super.createNarrationMessage().append(CommonComponents.NARRATION_SEPARATOR).append(CreateWorldScreen.WorldTab.SEED_EMPTY_HINT);
/*     */           }
/*     */         };
/* 224 */       this.seedEdit.setHint(SEED_EMPTY_HINT);
/* 225 */       this.seedEdit.setValue(CreateWorldScreen.this.uiState.getSeed());
/* 226 */       this.seedEdit.setResponder(value -> CreateWorldScreen.this.uiState.setSeed(this.seedEdit.getValue()));
/* 227 */       helper.addChild((LayoutElement)CommonLayouts.labeledElement(CreateWorldScreen.this.font, (LayoutElement)this.seedEdit, SEED_LABEL), 2);
/* 229 */       SwitchGrid.Builder switchGridBuilder = SwitchGrid.builder(310);
/* 230 */       Objects.requireNonNull(CreateWorldScreen.this.uiState);
/* 230 */       Objects.requireNonNull(CreateWorldScreen.this.uiState);
/* 230 */       switchGridBuilder.addSwitch(GENERATE_STRUCTURES, CreateWorldScreen.this.uiState::isGenerateStructures, CreateWorldScreen.this.uiState::setGenerateStructures)
/* 231 */         .withIsActiveCondition(() -> !CreateWorldScreen.this.uiState.isDebug())
/* 232 */         .withInfo(GENERATE_STRUCTURES_INFO);
/* 233 */       Objects.requireNonNull(CreateWorldScreen.this.uiState);
/* 233 */       Objects.requireNonNull(CreateWorldScreen.this.uiState);
/* 233 */       switchGridBuilder.addSwitch(BONUS_CHEST, CreateWorldScreen.this.uiState::isBonusChest, CreateWorldScreen.this.uiState::setBonusChest)
/* 234 */         .withIsActiveCondition(() -> (!CreateWorldScreen.this.uiState.isHardcore() && !CreateWorldScreen.this.uiState.isDebug()));
/* 235 */       SwitchGrid switchGrid = switchGridBuilder.build();
/* 236 */       helper.addChild((LayoutElement)switchGrid.layout(), 2);
/* 237 */       CreateWorldScreen.this.uiState.addListener(d -> switchGrid.refreshStates());
/*     */     }
/*     */     
/*     */     private void openPresetEditor() {
/* 241 */       PresetEditor editor = CreateWorldScreen.this.uiState.getPresetEditor();
/* 242 */       if (editor != null)
/* 243 */         CreateWorldScreen.this.minecraft.gui.setScreen(editor.createEditScreen(CreateWorldScreen.this, CreateWorldScreen.this.uiState.getSettings())); 
/*     */     }
/*     */     
/*     */     private CycleButton.ValueListSupplier<WorldCreationUiState.WorldTypeEntry> createWorldTypeValueSupplier() {
/* 248 */       return new CycleButton.ValueListSupplier<WorldCreationUiState.WorldTypeEntry>() {
/*     */           public List<WorldCreationUiState.WorldTypeEntry> getSelectedList() {
/* 251 */             return CycleButton.DEFAULT_ALT_LIST_SELECTOR.getAsBoolean() ? CreateWorldScreen.this.uiState.getAltPresetList() : CreateWorldScreen.this.uiState.getNormalPresetList();
/*     */           }
/*     */           
/*     */           public List<WorldCreationUiState.WorldTypeEntry> getDefaultList() {
/* 256 */             return CreateWorldScreen.this.uiState.getNormalPresetList();
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   class null extends EditBox {
/*     */     null(CreateWorldScreen.WorldTab this$1, Font font, int width, int height, Component narration) {
/*     */       super(font, width, height, narration);
/*     */     }
/*     */     
/*     */     protected MutableComponent createNarrationMessage() {
/*     */       return super.createNarrationMessage().append(CommonComponents.NARRATION_SEPARATOR).append(CreateWorldScreen.WorldTab.SEED_EMPTY_HINT);
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements CycleButton.ValueListSupplier<WorldCreationUiState.WorldTypeEntry> {
/*     */     public List<WorldCreationUiState.WorldTypeEntry> getSelectedList() {
/*     */       return CycleButton.DEFAULT_ALT_LIST_SELECTOR.getAsBoolean() ? CreateWorldScreen.this.uiState.getAltPresetList() : CreateWorldScreen.this.uiState.getNormalPresetList();
/*     */     }
/*     */     
/*     */     public List<WorldCreationUiState.WorldTypeEntry> getDefaultList() {
/* 256 */       return CreateWorldScreen.this.uiState.getNormalPresetList();
/*     */     }
/*     */   }
/*     */   
/*     */   private class MoreTab extends GridLayoutTab {
/* 263 */     private static final Component TITLE = (Component)Component.translatable("createWorld.tab.more.title");
/*     */     
/* 264 */     private static final Component GAME_RULES_LABEL = (Component)Component.translatable("selectWorld.gameRules");
/*     */     
/* 265 */     private static final Component DATA_PACKS_LABEL = (Component)Component.translatable("selectWorld.dataPacks");
/*     */     
/*     */     private MoreTab() {
/* 268 */       super(TITLE);
/* 272 */       GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
/* 274 */       helper.addChild((LayoutElement)Button.builder(GAME_RULES_LABEL, b -> openGameRulesScreen())
/* 275 */           .width(210)
/* 276 */           .build());
/* 279 */       helper.addChild((LayoutElement)Button.builder(CreateWorldScreen.EXPERIMENTS_LABEL, b -> CreateWorldScreen.this.openExperimentsScreen(CreateWorldScreen.this.uiState.getSettings().dataConfiguration()))
/* 280 */           .width(210)
/* 281 */           .build());
/* 283 */       helper.addChild((LayoutElement)Button.builder(DATA_PACKS_LABEL, b -> CreateWorldScreen.this.openDataPackSelectionScreen(CreateWorldScreen.this.uiState.getSettings().dataConfiguration()))
/* 284 */           .width(210)
/* 285 */           .build());
/*     */     }
/*     */     
/*     */     private void openGameRulesScreen() {
/* 289 */       CreateWorldScreen.this.minecraft.gui.setScreen(new WorldCreationGameRulesScreen(CreateWorldScreen.this.uiState.getGameRules().copy(CreateWorldScreen.this.uiState.getSettings().dataConfiguration().enabledFeatures()), gameRules -> {
/*     */               CreateWorldScreen.this.minecraft.gui.setScreen(CreateWorldScreen.this);
/*     */               Objects.requireNonNull(CreateWorldScreen.this.uiState);
/*     */               gameRules.ifPresent(CreateWorldScreen.this.uiState::setGameRules);
/*     */             }));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void openFresh(Minecraft minecraft, Runnable onClose) {
/* 297 */     openFresh(minecraft, onClose, (createWorldScreen, finalLayers, worldDataAndGenSettings, gameRules, tempDataPackDir) -> createWorldScreen.createNewWorld(finalLayers, worldDataAndGenSettings, gameRules));
/*     */   }
/*     */   
/*     */   public static void openFresh(Minecraft minecraft, Runnable onClose, CreateWorldCallback createWorld) {
/* 306 */     WorldCreationContextMapper worldCreationContext = (managers, registries, cookie) -> new WorldCreationContext(cookie.worldGenSettings(), registries, managers, cookie.dataConfiguration());
/* 309 */     Function<WorldLoader.DataLoadContext, WorldGenSettings> worldGenSettings = context -> new WorldGenSettings(WorldOptions.defaultWithRandomSeed(), WorldPresets.createNormalWorldDimensions(context.datapackWorldgen()));
/* 315 */     openCreateWorldScreen(minecraft, onClose, worldGenSettings, worldCreationContext, WorldPresets.NORMAL, createWorld);
/*     */   }
/*     */   
/*     */   public static void testWorld(Minecraft minecraft, Runnable onClose) {
/* 319 */     WorldCreationContextMapper worldCreationContext = (managers, registries, cookie) -> new WorldCreationContext(cookie.worldGenSettings().options(), cookie.worldGenSettings().dimensions(), registries, managers, cookie.dataConfiguration(), new InitialWorldCreationOptions(WorldCreationUiState.SelectedGameMode.CREATIVE, (new GameRuleMap.Builder()).set(GameRules.ADVANCE_TIME, Boolean.valueOf(false)).set(GameRules.ADVANCE_WEATHER, Boolean.valueOf(false)).set(GameRules.SPAWN_MOBS, Boolean.valueOf(false)).build(), null));
/* 337 */     Function<WorldLoader.DataLoadContext, WorldGenSettings> worldGenSettings = context -> new WorldGenSettings(WorldOptions.testWorldWithRandomSeed(), WorldPresets.createTestWorldDimensions(context.datapackWorldgen()));
/* 343 */     openCreateWorldScreen(minecraft, onClose, worldGenSettings, worldCreationContext, WorldPresets.FLAT_ALL_DIMENSIONS, (createWorldScreen, finalLayers, worldDataAndGenSettings, gameRules, tempDataPackDir) -> createWorldScreen.createNewWorld(finalLayers, worldDataAndGenSettings, gameRules));
/*     */   }
/*     */   
/*     */   private static void openCreateWorldScreen(Minecraft minecraft, Runnable onClose, Function<WorldLoader.DataLoadContext, WorldGenSettings> worldGenSettings, WorldCreationContextMapper worldCreationContext, ResourceKey<WorldPreset> worldPreset, CreateWorldCallback createWorld) {
/* 352 */     queueLoadScreen(minecraft, PREPARING_WORLD_DATA);
/* 354 */     long start = Util.getMillis();
/* 355 */     PackRepository vanillaOnlyPackRepository = new PackRepository(new RepositorySource[] { (RepositorySource)new ServerPacksSource(minecraft.directoryValidator()) });
/* 358 */     WorldDataConfiguration dataConfig = SharedConstants.IS_RUNNING_IN_IDE ? new WorldDataConfiguration(new DataPackConfig(List.of("vanilla", "tests"), List.of()), FeatureFlags.DEFAULT_FLAGS) : WorldDataConfiguration.DEFAULT;
/* 359 */     WorldLoader.InitConfig loadConfig = createDefaultLoadConfig(vanillaOnlyPackRepository, dataConfig);
/* 361 */     CompletableFuture<WorldCreationContext> loadResult = WorldLoader.load(loadConfig, context -> new WorldLoader.DataLoadOutput(new DataPackReloadCookie(worldGenSettings.apply(context), context.dataConfiguration()), context.datapackDimensions()), (resources, managers, registries, cookie) -> {
/*     */           resources.close();
/*     */           return worldCreationContext.apply(managers, registries, cookie);
/* 374 */         }(Executor)Util.backgroundExecutor(), (Executor)minecraft);
/* 377 */     Objects.requireNonNull(loadResult);
/* 377 */     minecraft.managedBlock(loadResult::isDone);
/* 378 */     long end = Util.getMillis();
/* 379 */     LOGGER.debug("Resource load for world creation blocked for {} ms", Long.valueOf(end - start));
/* 382 */     minecraft.gui.setScreen(new CreateWorldScreen(minecraft, onClose, loadResult.join(), Optional.of(worldPreset), OptionalLong.empty(), createWorld));
/*     */   }
/*     */   
/*     */   public static CreateWorldScreen createFromExisting(Minecraft minecraft, Runnable onClose, LevelSettings levelSettings, WorldCreationContext worldCreationContext, Path newDataPackDir) {
/* 390 */     CreateWorldScreen result = new CreateWorldScreen(minecraft, onClose, worldCreationContext, WorldPresets.fromSettings(worldCreationContext.selectedDimensions()), OptionalLong.of(worldCreationContext.options().seed()), (createWorldScreen, finalLayers, worldDataAndGenSettings, gameRules, tempDataPackDir) -> createWorldScreen.createNewWorld(finalLayers, worldDataAndGenSettings, gameRules));
/* 396 */     result.recreated = true;
/* 398 */     result.uiState.setName(levelSettings.levelName());
/* 399 */     result.uiState.setAllowCommands(levelSettings.allowCommands());
/* 400 */     result.uiState.setDifficulty(levelSettings.difficultySettings().difficulty());
/* 401 */     result.uiState.getGameRules().setAll(worldCreationContext.initialWorldCreationOptions().gameRuleOverwrites(), null);
/* 403 */     if (levelSettings.difficultySettings().hardcore()) {
/* 404 */       result.uiState.setGameMode(WorldCreationUiState.SelectedGameMode.HARDCORE);
/* 405 */     } else if (levelSettings.gameType().isSurvival()) {
/* 406 */       result.uiState.setGameMode(WorldCreationUiState.SelectedGameMode.SURVIVAL);
/* 407 */     } else if (levelSettings.gameType().isCreative()) {
/* 408 */       result.uiState.setGameMode(WorldCreationUiState.SelectedGameMode.CREATIVE);
/*     */     } 
/* 410 */     result.tempDataPackDir = newDataPackDir;
/* 411 */     return result;
/*     */   }
/*     */   
/*     */   private CreateWorldScreen(Minecraft minecraft, Runnable onClose, WorldCreationContext settings, Optional<ResourceKey<WorldPreset>> preset, OptionalLong seed, CreateWorldCallback createWorldCallback) {
/* 415 */     super((Component)Component.translatable("selectWorld.create"));
/*     */     CreateWorldScreen createWorldScreen = this;
/*     */     createWorldScreen = this;
/*     */     this.tabManager = new TabManager(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0), x$0 -> rec$.removeWidget((GuiEventListener)x$0));
/* 416 */     this.onClose = onClose;
/* 417 */     this.packValidator = minecraft.directoryValidator();
/* 418 */     this.createWorldCallback = createWorldCallback;
/* 420 */     this.uiState = new WorldCreationUiState(minecraft.getLevelSource().getBaseDir(), settings, preset, seed);
/*     */   }
/*     */   
/*     */   public WorldCreationUiState getUiState() {
/* 424 */     return this.uiState;
/*     */   }
/*     */   
/*     */   protected void init() {
/* 429 */     this
/*     */       
/* 431 */       .tabNavigationBar = MenuTabBar.builder(this.tabManager, this.width).addTabs(new Tab[] { (Tab)new GameTab(this), (Tab)new WorldTab(), (Tab)new MoreTab() }).build();
/* 432 */     addRenderableWidget((GuiEventListener)this.tabNavigationBar);
/* 434 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 435 */     footer.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectWorld.create"), button -> onCreate()).build());
/* 436 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> popScreen()).build());
/* 438 */     this.layout.visitWidgets(button -> {
/*     */           button.setTabOrderGroup(1);
/*     */           addRenderableWidget((GuiEventListener)button);
/*     */         });
/* 443 */     this.tabNavigationBar.selectTab(0, false);
/* 446 */     this.uiState.onChanged();
/* 448 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void setInitialFocus() {}
/*     */   
/*     */   public void repositionElements() {
/* 458 */     if (this.tabNavigationBar == null)
/*     */       return; 
/* 461 */     this.tabNavigationBar.arrangeElements(this.width);
/* 463 */     int tabAreaTop = this.tabNavigationBar.getRectangle().bottom();
/* 464 */     ScreenRectangle tabArea = new ScreenRectangle(0, tabAreaTop, this.width, this.height - this.layout.getFooterHeight() - tabAreaTop);
/* 465 */     this.tabManager.setTabArea(tabArea);
/* 466 */     this.layout.setHeaderHeight(tabAreaTop);
/* 467 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   private static void queueLoadScreen(Minecraft minecraft, Component message) {
/* 471 */     minecraft.setScreenAndShow((Screen)new GenericMessageScreen(message));
/*     */   }
/*     */   
/*     */   private void onCreate() {
/*     */     GameRules gameRules;
/* 475 */     WorldCreationContext context = this.uiState.getSettings();
/* 476 */     WorldDimensions worldDimensions = context.selectedDimensions();
/* 477 */     WorldDimensions.Complete finalDimensions = worldDimensions.bake(context.datapackDimensions());
/* 479 */     LayeredRegistryAccess<RegistryLayer> finalLayers = context.worldgenRegistries().replaceFrom(RegistryLayer.DIMENSIONS, new RegistryAccess.Frozen[] { finalDimensions.dimensionsRegistryAccess() });
/* 480 */     FeatureFlagSet enabledFeatures = context.dataConfiguration().enabledFeatures();
/* 481 */     Lifecycle lifecycleFromFeatures = FeatureFlags.isExperimental(enabledFeatures) ? Lifecycle.experimental() : Lifecycle.stable();
/* 482 */     Lifecycle lifecycleFromRegistries = finalLayers.compositeAccess().allRegistriesLifecycle();
/* 483 */     Lifecycle lifecycle = lifecycleFromRegistries.add(lifecycleFromFeatures);
/* 486 */     boolean skipWarning = (!this.recreated && lifecycleFromRegistries == Lifecycle.stable());
/* 487 */     boolean isDebug = (finalDimensions.specialWorldProperty() == PrimaryLevelData.SpecialWorldProperty.DEBUG);
/* 488 */     LevelSettings levelSettings = createLevelSettings(isDebug);
/* 490 */     if (isDebug) {
/* 491 */       gameRules = MinecraftServer.DEFAULT_GAME_RULES.get();
/* 492 */       gameRules.set(GameRules.ADVANCE_TIME, Boolean.valueOf(false), null);
/*     */     } else {
/* 494 */       gameRules = this.uiState.getGameRules().copy(enabledFeatures);
/*     */     } 
/* 496 */     PrimaryLevelData worldData = new PrimaryLevelData(levelSettings, finalDimensions.specialWorldProperty(), lifecycle);
/* 497 */     WorldOptions options = this.uiState.getSettings().options();
/* 498 */     WorldGenSettings worldGenSettings = new WorldGenSettings(options, worldDimensions);
/* 499 */     LevelDataAndDimensions.WorldDataAndGenSettings worldDataAndGenSettings = new LevelDataAndDimensions.WorldDataAndGenSettings((WorldData)worldData, worldGenSettings);
/* 500 */     WorldOpenFlows.confirmWorldCreation(this.minecraft, this, lifecycle, () -> createWorldAndCleanup(finalLayers, worldDataAndGenSettings, Optional.of(gameRules)), skipWarning);
/*     */   }
/*     */   
/*     */   private void createWorldAndCleanup(LayeredRegistryAccess<RegistryLayer> finalLayers, LevelDataAndDimensions.WorldDataAndGenSettings worldDataAndGenSettings, Optional<GameRules> gameRules) {
/* 504 */     boolean worldCreationSuccessful = this.createWorldCallback.create(this, finalLayers, worldDataAndGenSettings, gameRules, this.tempDataPackDir);
/* 505 */     removeTempDataPackDir();
/* 506 */     if (!worldCreationSuccessful)
/* 507 */       popScreen(); 
/*     */   }
/*     */   
/*     */   private boolean createNewWorld(LayeredRegistryAccess<RegistryLayer> finalLayers, LevelDataAndDimensions.WorldDataAndGenSettings worldDataAndGenSettings, Optional<GameRules> gameRules) {
/* 512 */     String worldFolder = this.uiState.getTargetFolder();
/* 513 */     WorldCreationContext context = this.uiState.getSettings();
/* 514 */     queueLoadScreen(this.minecraft, PREPARING_WORLD_DATA);
/* 516 */     Optional<LevelStorageSource.LevelStorageAccess> newWorldAccess = createNewWorldDirectory(this.minecraft, worldFolder, this.tempDataPackDir);
/* 517 */     if (newWorldAccess.isEmpty()) {
/* 518 */       SystemToast.onPackCopyFailure(this.minecraft, worldFolder);
/* 519 */       return false;
/*     */     } 
/* 522 */     this.minecraft.createWorldOpenFlows().createLevelFromExistingSettings(newWorldAccess.get(), context.dataPackResources(), finalLayers, worldDataAndGenSettings, gameRules);
/* 524 */     return true;
/*     */   }
/*     */   
/*     */   private LevelSettings createLevelSettings(boolean isDebug) {
/* 528 */     String name = this.uiState.getName().trim();
/* 529 */     if (isDebug)
/* 530 */       return new LevelSettings(name, GameType.SPECTATOR, new LevelSettings.DifficultySettings(Difficulty.PEACEFUL, false, false), true, WorldDataConfiguration.DEFAULT); 
/* 532 */     return new LevelSettings(name, (this.uiState.getGameMode()).gameType, new LevelSettings.DifficultySettings(this.uiState.getDifficulty(), this.uiState.isHardcore(), false), this.uiState.isAllowCommands(), this.uiState.getSettings().dataConfiguration());
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 537 */     if (this.tabNavigationBar.keyPressed(event))
/* 538 */       return true; 
/* 541 */     if (super.keyPressed(event))
/* 542 */       return true; 
/* 545 */     if (event.isConfirmation()) {
/* 546 */       onCreate();
/* 547 */       return true;
/*     */     } 
/* 550 */     return false;
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 555 */     popScreen();
/*     */   }
/*     */   
/*     */   public void popScreen() {
/* 559 */     this.onClose.run();
/* 560 */     removeTempDataPackDir();
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 565 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/* 566 */     graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, 0, this.height - this.layout.getFooterHeight() - 2, 0.0F, 0.0F, this.width, 2, 32, 2);
/*     */   }
/*     */   
/*     */   protected void extractMenuBackground(GuiGraphicsExtractor graphics) {
/* 571 */     graphics.blit(RenderPipelines.GUI_TEXTURED, TAB_HEADER_BACKGROUND, 0, 0, 0.0F, 0.0F, this.width, this.layout.getHeaderHeight(), 16, 16);
/* 572 */     extractMenuBackground(graphics, 0, this.layout.getHeaderHeight(), this.width, this.height);
/*     */   }
/*     */   
/*     */   private Path getOrCreateTempDataPackDir() {
/* 578 */     if (this.tempDataPackDir == null)
/*     */       try {
/* 580 */         this.tempDataPackDir = Files.createTempDirectory("mcworld-", (FileAttribute<?>[])new FileAttribute[0]);
/* 581 */       } catch (IOException e) {
/* 582 */         LOGGER.warn("Failed to create temporary dir", e);
/* 583 */         SystemToast.onPackCopyFailure(this.minecraft, this.uiState.getTargetFolder());
/* 584 */         popScreen();
/*     */       }  
/* 588 */     return this.tempDataPackDir;
/*     */   }
/*     */   
/*     */   private void openExperimentsScreen(WorldDataConfiguration dataConfiguration) {
/* 592 */     Pair<Path, PackRepository> settings = getDataPackSelectionSettings(dataConfiguration);
/* 594 */     if (settings != null)
/* 595 */       this.minecraft.gui.setScreen(new ExperimentsScreen(this, (PackRepository)settings.getSecond(), packRepository -> tryApplyNewDataPacks(packRepository, false, this::openExperimentsScreen))); 
/*     */   }
/*     */   
/*     */   private void openDataPackSelectionScreen(WorldDataConfiguration dataConfiguration) {
/* 600 */     Pair<Path, PackRepository> settings = getDataPackSelectionSettings(dataConfiguration);
/* 602 */     if (settings != null)
/* 603 */       this.minecraft.gui.setScreen((Screen)new PackSelectionScreen((PackRepository)settings.getSecond(), packRepository -> tryApplyNewDataPacks(packRepository, true, this::openDataPackSelectionScreen), (Path)settings.getFirst(), (Component)Component.translatable("dataPack.title"))); 
/*     */   }
/*     */   
/*     */   private void tryApplyNewDataPacks(PackRepository packRepository, boolean isDataPackScreen, Consumer<WorldDataConfiguration> onAbort) {
/* 608 */     ImmutableList immutableList = ImmutableList.copyOf(packRepository.getSelectedIds());
/* 609 */     List<String> newDisabled = (List<String>)packRepository.getAvailableIds().stream().filter(id -> !newEnabled.contains(id)).collect(ImmutableList.toImmutableList());
/* 610 */     WorldDataConfiguration newConfig = new WorldDataConfiguration(new DataPackConfig((List)immutableList, newDisabled), this.uiState.getSettings().dataConfiguration().enabledFeatures());
/* 612 */     if (this.uiState.tryUpdateDataConfiguration(newConfig)) {
/* 614 */       this.minecraft.gui.setScreen(this);
/*     */       return;
/*     */     } 
/* 618 */     FeatureFlagSet requestedFeatureFlags = packRepository.getRequestedFeatureFlags();
/* 619 */     if (FeatureFlags.isExperimental(requestedFeatureFlags) && isDataPackScreen) {
/* 620 */       this.minecraft.gui.setScreen(new ConfirmExperimentalFeaturesScreen(packRepository
/* 621 */             .getSelectedPacks(), accepted -> {
/*     */               if (accepted) {
/*     */                 applyNewPackConfig(packRepository, newConfig, onAbort);
/*     */               } else {
/*     */                 onAbort.accept(this.uiState.getSettings().dataConfiguration());
/*     */               } 
/*     */             }));
/*     */     } else {
/* 631 */       applyNewPackConfig(packRepository, newConfig, onAbort);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void applyNewPackConfig(PackRepository packRepository, WorldDataConfiguration newConfig, Consumer<WorldDataConfiguration> onAbort) {
/* 637 */     this.minecraft.setScreenAndShow((Screen)new GenericMessageScreen((Component)Component.translatable("dataPack.validation.working")));
/* 639 */     WorldLoader.InitConfig config = createDefaultLoadConfig(packRepository, newConfig);
/* 675 */     Objects.requireNonNull(this.uiState);
/*     */     WorldLoader.load(config, context -> {
/*     */           if (context.datapackWorldgen().lookupOrThrow(Registries.WORLD_PRESET).listElements().findAny().isEmpty())
/*     */             throw new IllegalStateException("Needs at least one world preset to continue"); 
/*     */           if (context.datapackWorldgen().lookupOrThrow(Registries.BIOME).listElements().findAny().isEmpty())
/*     */             throw new IllegalStateException("Needs at least one biome continue"); 
/*     */           WorldCreationContext existingContext = this.uiState.getSettings();
/*     */           RegistryOps registryOps1 = existingContext.worldgenLoadContext().createSerializationContext((DynamicOps)JsonOps.INSTANCE);
/*     */           DataResult<JsonElement> encoded = WorldGenSettings.CODEC.encodeStart((DynamicOps)registryOps1, new WorldGenSettings(existingContext.options(), existingContext.selectedDimensions())).setLifecycle(Lifecycle.stable());
/*     */           RegistryOps registryOps2 = context.datapackWorldgen().createSerializationContext((DynamicOps)JsonOps.INSTANCE);
/*     */           WorldGenSettings settings = (WorldGenSettings)encoded.flatMap(()).getOrThrow(());
/*     */           return new WorldLoader.DataLoadOutput(new DataPackReloadCookie(settings, context.dataConfiguration()), context.datapackDimensions());
/*     */         }(resources, managers, registries, cookie) -> {
/*     */           resources.close();
/*     */           return new WorldCreationContext(cookie.worldGenSettings(), registries, managers, cookie.dataConfiguration());
/*     */         }(Executor)Util.backgroundExecutor(), (Executor)this.minecraft).thenApply(settings -> {
/*     */           settings.validate();
/*     */           return settings;
/* 675 */         }).thenAcceptAsync(this.uiState::setSettings, (Executor)this.minecraft)
/* 676 */       .handleAsync((nothing, throwable) -> {
/*     */           if (throwable != null) {
/*     */             LOGGER.warn("Failed to validate datapack", throwable);
/*     */             this.minecraft.gui.setScreen((Screen)new ConfirmScreen((), (Component)Component.translatable("dataPack.validation.failed"), CommonComponents.EMPTY, (Component)Component.translatable("dataPack.validation.back"), (Component)Component.translatable("dataPack.validation.reset")));
/*     */           } else {
/*     */             this.minecraft.gui.setScreen(this);
/*     */           } 
/*     */           return null;
/*     */         }(Executor)this.minecraft);
/*     */   }
/*     */   
/*     */   private static WorldLoader.InitConfig createDefaultLoadConfig(PackRepository packRepository, WorldDataConfiguration config) {
/* 699 */     WorldLoader.PackConfig packConfig = new WorldLoader.PackConfig(packRepository, config, false, true);
/* 700 */     return new WorldLoader.InitConfig(packConfig, Commands.CommandSelection.INTEGRATED, (PermissionSet)LevelBasedPermissionSet.GAMEMASTER);
/*     */   }
/*     */   
/*     */   private void removeTempDataPackDir() {
/* 704 */     if (this.tempDataPackDir != null && Files.exists(this.tempDataPackDir, new java.nio.file.LinkOption[0]))
/*     */       try {
/* 705 */         Stream<Path> files = Files.walk(this.tempDataPackDir, new java.nio.file.FileVisitOption[0]);
/*     */         try {
/* 706 */           files.sorted(Comparator.reverseOrder()).forEach(path -> {
/*     */                 try {
/*     */                   Files.delete(path);
/* 709 */                 } catch (IOException e) {
/*     */                   LOGGER.warn("Failed to remove temporary file {}", path, e);
/*     */                 } 
/*     */               });
/* 713 */           if (files != null)
/* 713 */             files.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (files != null)
/*     */             try {
/*     */               files.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/* 713 */       } catch (IOException e) {
/* 714 */         LOGGER.warn("Failed to list temporary dir {}", this.tempDataPackDir);
/*     */       }  
/* 717 */     this.tempDataPackDir = null;
/*     */   }
/*     */   
/*     */   private static void copyBetweenDirs(Path sourceDir, Path targetDir, Path sourcePath) {
/*     */     try {
/* 722 */       Util.copyBetweenDirs(sourceDir, targetDir, sourcePath);
/* 723 */     } catch (IOException e) {
/* 724 */       LOGGER.warn("Failed to copy datapack file from {} to {}", sourcePath, targetDir);
/* 725 */       throw new UncheckedIOException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Optional<LevelStorageSource.LevelStorageAccess> createNewWorldDirectory(Minecraft minecraft, String worldFolder, Path tempDataPackDir) {
/*     */     try {
/* 732 */       LevelStorageSource.LevelStorageAccess access = minecraft.getLevelSource().createAccess(worldFolder);
/* 733 */       if (tempDataPackDir == null)
/* 734 */         return Optional.of(access); 
/*     */       try {
/* 736 */         Stream<Path> files = Files.walk(tempDataPackDir, new java.nio.file.FileVisitOption[0]);
/*     */         try {
/* 737 */           Path targetDir = access.getLevelPath(LevelResource.DATAPACK_DIR);
/* 738 */           FileUtil.createDirectoriesSafe(targetDir);
/* 739 */           files.filter(f -> !f.equals(tempDataPackDir)).forEach(source -> copyBetweenDirs(tempDataPackDir, targetDir, source));
/* 740 */           Optional<LevelStorageSource.LevelStorageAccess> optional = Optional.of(access);
/* 741 */           if (files != null)
/* 741 */             files.close(); 
/*     */           return optional;
/*     */         } catch (Throwable throwable) {
/*     */           if (files != null)
/*     */             try {
/*     */               files.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/* 741 */       } catch (IOException|UncheckedIOException e) {
/* 742 */         LOGGER.warn("Failed to copy datapacks to world {}", worldFolder, e);
/* 743 */         access.close();
/*     */       } 
/* 745 */     } catch (IOException|UncheckedIOException e) {
/* 746 */       LOGGER.warn("Failed to create access for {}", worldFolder, e);
/*     */     } 
/* 748 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static Path createTempDataPackDirFromExistingWorld(Path sourcePackDir, Minecraft minecraft) {
/* 752 */     MutableObject<Path> tempDataPackDir = new MutableObject();
/*     */     try {
/* 753 */       Stream<Path> dataPackContents = Files.walk(sourcePackDir, new java.nio.file.FileVisitOption[0]);
/*     */       try {
/* 754 */         dataPackContents.filter(p -> !p.equals(sourcePackDir)).forEach(source -> {
/*     */               Path targetDir = (Path)tempDataPackDir.get();
/*     */               if (targetDir == null) {
/*     */                 try {
/*     */                   targetDir = Files.createTempDirectory("mcworld-", (FileAttribute<?>[])new FileAttribute[0]);
/* 760 */                 } catch (IOException e) {
/*     */                   LOGGER.warn("Failed to create temporary dir");
/*     */                   throw new UncheckedIOException(e);
/*     */                 } 
/*     */                 tempDataPackDir.setValue(targetDir);
/*     */               } 
/*     */               copyBetweenDirs(sourcePackDir, targetDir, source);
/*     */             });
/* 768 */         if (dataPackContents != null)
/* 768 */           dataPackContents.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (dataPackContents != null)
/*     */           try {
/*     */             dataPackContents.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 768 */     } catch (IOException|UncheckedIOException e) {
/* 769 */       LOGGER.warn("Failed to copy datapacks from world {}", sourcePackDir, e);
/* 770 */       SystemToast.onPackCopyFailure(minecraft, sourcePackDir.toString());
/* 771 */       return null;
/*     */     } 
/* 773 */     return (Path)tempDataPackDir.get();
/*     */   }
/*     */   
/*     */   private Pair<Path, PackRepository> getDataPackSelectionSettings(WorldDataConfiguration dataConfiguration) {
/* 777 */     Path dataPackDir = getOrCreateTempDataPackDir();
/* 778 */     if (dataPackDir != null) {
/* 779 */       if (this.tempDataPackRepository == null) {
/* 780 */         this.tempDataPackRepository = ServerPacksSource.createPackRepository(dataPackDir, this.packValidator);
/* 781 */         this.tempDataPackRepository.reload();
/*     */       } 
/* 784 */       this.tempDataPackRepository.setSelected(dataConfiguration.dataPacks().getEnabled());
/* 785 */       return Pair.of(dataPackDir, this.tempDataPackRepository);
/*     */     } 
/* 788 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\CreateWorldScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */