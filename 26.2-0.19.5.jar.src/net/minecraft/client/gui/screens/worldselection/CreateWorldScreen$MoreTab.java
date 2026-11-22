/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.tabs.GridLayoutTab;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class MoreTab extends GridLayoutTab {
/* 263 */   private static final Component TITLE = (Component)Component.translatable("createWorld.tab.more.title");
/*     */   
/* 264 */   private static final Component GAME_RULES_LABEL = (Component)Component.translatable("selectWorld.gameRules");
/*     */   
/* 265 */   private static final Component DATA_PACKS_LABEL = (Component)Component.translatable("selectWorld.dataPacks");
/*     */   
/*     */   private MoreTab() {
/* 268 */     super(TITLE);
/* 272 */     GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
/* 274 */     helper.addChild((LayoutElement)Button.builder(GAME_RULES_LABEL, b -> openGameRulesScreen())
/* 275 */         .width(210)
/* 276 */         .build());
/* 279 */     helper.addChild((LayoutElement)Button.builder(CreateWorldScreen.EXPERIMENTS_LABEL, b -> CreateWorldScreen.this.openExperimentsScreen(CreateWorldScreen.this.uiState.getSettings().dataConfiguration()))
/* 280 */         .width(210)
/* 281 */         .build());
/* 283 */     helper.addChild((LayoutElement)Button.builder(DATA_PACKS_LABEL, b -> CreateWorldScreen.this.openDataPackSelectionScreen(CreateWorldScreen.this.uiState.getSettings().dataConfiguration()))
/* 284 */         .width(210)
/* 285 */         .build());
/*     */   }
/*     */   
/*     */   private void openGameRulesScreen() {
/* 289 */     (CreateWorldScreen.access$700(CreateWorldScreen.this)).gui.setScreen(new WorldCreationGameRulesScreen(CreateWorldScreen.this.uiState.getGameRules().copy(CreateWorldScreen.this.uiState.getSettings().dataConfiguration().enabledFeatures()), gameRules -> {
/*     */             (CreateWorldScreen.access$600(CreateWorldScreen.this)).gui.setScreen(CreateWorldScreen.this);
/*     */             Objects.requireNonNull(CreateWorldScreen.this.uiState);
/*     */             gameRules.ifPresent(CreateWorldScreen.this.uiState::setGameRules);
/*     */           }));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\CreateWorldScreen$MoreTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */