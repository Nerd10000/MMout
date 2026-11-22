/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.tabs.GridLayoutTab;
/*     */ import net.minecraft.client.gui.layouts.CommonLayouts;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ class WorldTab extends GridLayoutTab {
/* 178 */   private static final Component TITLE = (Component)Component.translatable("createWorld.tab.world.title");
/*     */   
/* 179 */   private static final Component AMPLIFIED_HELP_TEXT = (Component)Component.translatable("generator.minecraft.amplified.info");
/*     */   
/* 180 */   private static final Component GENERATE_STRUCTURES = (Component)Component.translatable("selectWorld.mapFeatures");
/*     */   
/* 181 */   private static final Component GENERATE_STRUCTURES_INFO = (Component)Component.translatable("selectWorld.mapFeatures.info");
/*     */   
/* 182 */   private static final Component BONUS_CHEST = (Component)Component.translatable("selectWorld.bonusItems");
/*     */   
/* 183 */   private static final Component SEED_LABEL = (Component)Component.translatable("selectWorld.enterSeed");
/*     */   
/* 184 */   private static final Component SEED_EMPTY_HINT = (Component)Component.translatable("selectWorld.seedInfo");
/*     */   
/*     */   private static final int WORLD_TAB_WIDTH = 310;
/*     */   
/*     */   private final EditBox seedEdit;
/*     */   
/*     */   private final Button customizeTypeButton;
/*     */   
/*     */   private WorldTab() {
/* 190 */     super(TITLE);
/* 195 */     GridLayout.RowHelper helper = this.layout.columnSpacing(10).rowSpacing(8).createRowHelper(2);
/* 197 */     CycleButton<WorldCreationUiState.WorldTypeEntry> typeButton = (CycleButton<WorldCreationUiState.WorldTypeEntry>)helper.addChild((LayoutElement)CycleButton.builder(WorldCreationUiState.WorldTypeEntry::describePreset, paramCreateWorldScreen.uiState.getWorldType())
/* 198 */         .withValues(createWorldTypeValueSupplier())
/* 199 */         .create(0, 0, 150, 20, (Component)Component.translatable("selectWorld.mapType"), (button, newPreset) -> CreateWorldScreen.this.uiState.setWorldType(newPreset)));
/* 202 */     typeButton.setValue(paramCreateWorldScreen.uiState.getWorldType());
/* 203 */     paramCreateWorldScreen.uiState.addListener(data -> {
/*     */           WorldCreationUiState.WorldTypeEntry worldType = data.getWorldType();
/*     */           typeButton.setValue(worldType);
/*     */           if (worldType.isAmplified()) {
/*     */             typeButton.setTooltip(Tooltip.create(AMPLIFIED_HELP_TEXT));
/*     */           } else {
/*     */             typeButton.setTooltip(null);
/*     */           } 
/*     */           typeButton.active = (CreateWorldScreen.this.uiState.getWorldType().preset() != null);
/*     */         });
/* 215 */     this.customizeTypeButton = (Button)helper.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectWorld.customizeType"), b -> openPresetEditor()).build());
/* 216 */     paramCreateWorldScreen.uiState.addListener(data -> this.customizeTypeButton.active = (!data.isDebug() && data.getPresetEditor() != null));
/* 218 */     this.seedEdit = new EditBox(this, CreateWorldScreen.access$300(paramCreateWorldScreen), 308, 20, (Component)Component.translatable("selectWorld.enterSeed")) {
/*     */         protected MutableComponent createNarrationMessage() {
/* 221 */           return super.createNarrationMessage().append(CommonComponents.NARRATION_SEPARATOR).append(CreateWorldScreen.WorldTab.SEED_EMPTY_HINT);
/*     */         }
/*     */       };
/* 224 */     this.seedEdit.setHint(SEED_EMPTY_HINT);
/* 225 */     this.seedEdit.setValue(paramCreateWorldScreen.uiState.getSeed());
/* 226 */     this.seedEdit.setResponder(value -> CreateWorldScreen.this.uiState.setSeed(this.seedEdit.getValue()));
/* 227 */     helper.addChild((LayoutElement)CommonLayouts.labeledElement(CreateWorldScreen.access$400(paramCreateWorldScreen), (LayoutElement)this.seedEdit, SEED_LABEL), 2);
/* 229 */     SwitchGrid.Builder switchGridBuilder = SwitchGrid.builder(310);
/* 230 */     Objects.requireNonNull(paramCreateWorldScreen.uiState);
/* 230 */     Objects.requireNonNull(paramCreateWorldScreen.uiState);
/* 230 */     switchGridBuilder.addSwitch(GENERATE_STRUCTURES, paramCreateWorldScreen.uiState::isGenerateStructures, paramCreateWorldScreen.uiState::setGenerateStructures)
/* 231 */       .withIsActiveCondition(() -> !CreateWorldScreen.this.uiState.isDebug())
/* 232 */       .withInfo(GENERATE_STRUCTURES_INFO);
/* 233 */     Objects.requireNonNull(paramCreateWorldScreen.uiState);
/* 233 */     Objects.requireNonNull(paramCreateWorldScreen.uiState);
/* 233 */     switchGridBuilder.addSwitch(BONUS_CHEST, paramCreateWorldScreen.uiState::isBonusChest, paramCreateWorldScreen.uiState::setBonusChest)
/* 234 */       .withIsActiveCondition(() -> (!CreateWorldScreen.this.uiState.isHardcore() && !CreateWorldScreen.this.uiState.isDebug()));
/* 235 */     SwitchGrid switchGrid = switchGridBuilder.build();
/* 236 */     helper.addChild((LayoutElement)switchGrid.layout(), 2);
/* 237 */     paramCreateWorldScreen.uiState.addListener(d -> switchGrid.refreshStates());
/*     */   }
/*     */   
/*     */   private void openPresetEditor() {
/* 241 */     PresetEditor editor = CreateWorldScreen.this.uiState.getPresetEditor();
/* 242 */     if (editor != null)
/* 243 */       (CreateWorldScreen.access$500(CreateWorldScreen.this)).gui.setScreen(editor.createEditScreen(CreateWorldScreen.this, CreateWorldScreen.this.uiState.getSettings())); 
/*     */   }
/*     */   
/*     */   private CycleButton.ValueListSupplier<WorldCreationUiState.WorldTypeEntry> createWorldTypeValueSupplier() {
/* 248 */     return new CycleButton.ValueListSupplier<WorldCreationUiState.WorldTypeEntry>() {
/*     */         public List<WorldCreationUiState.WorldTypeEntry> getSelectedList() {
/* 251 */           return CycleButton.DEFAULT_ALT_LIST_SELECTOR.getAsBoolean() ? CreateWorldScreen.this.uiState.getAltPresetList() : CreateWorldScreen.this.uiState.getNormalPresetList();
/*     */         }
/*     */         
/*     */         public List<WorldCreationUiState.WorldTypeEntry> getDefaultList() {
/* 256 */           return CreateWorldScreen.this.uiState.getNormalPresetList();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\CreateWorldScreen$WorldTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */