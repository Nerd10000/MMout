/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.components.tabs.GridLayoutTab;
/*     */ import net.minecraft.client.gui.layouts.CommonLayouts;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.Difficulty;
/*     */ 
/*     */ class GameTab extends GridLayoutTab {
/* 118 */   private static final Component TITLE = (Component)Component.translatable("createWorld.tab.game.title");
/*     */   
/* 119 */   private static final Component ALLOW_COMMANDS = (Component)Component.translatable("selectWorld.allowCommands");
/*     */   
/*     */   private final EditBox nameEdit;
/*     */   
/*     */   private GameTab(CreateWorldScreen paramCreateWorldScreen) {
/* 123 */     super(TITLE);
/* 127 */     GridLayout.RowHelper helper = this.layout.rowSpacing(8).createRowHelper(1);
/* 128 */     LayoutSettings buttonLayoutSettings = helper.newCellSettings();
/* 130 */     this.nameEdit = new EditBox(CreateWorldScreen.access$000(paramCreateWorldScreen), 208, 20, (Component)Component.translatable("selectWorld.enterName"));
/* 131 */     this.nameEdit.setValue(paramCreateWorldScreen.uiState.getName());
/* 132 */     Objects.requireNonNull(paramCreateWorldScreen.uiState);
/* 132 */     this.nameEdit.setResponder(paramCreateWorldScreen.uiState::setName);
/* 133 */     paramCreateWorldScreen.uiState.addListener(uiState -> this.nameEdit.setTooltip(Tooltip.create((Component)Component.translatable("selectWorld.targetFolder", new Object[] { Component.literal(uiState.getTargetFolder()).withStyle(ChatFormatting.ITALIC) }))));
/* 134 */     CreateWorldScreen.access$100(paramCreateWorldScreen, (GuiEventListener)this.nameEdit);
/* 135 */     helper.addChild((LayoutElement)CommonLayouts.labeledElement(CreateWorldScreen.access$200(paramCreateWorldScreen), (LayoutElement)this.nameEdit, CreateWorldScreen.NAME_LABEL), helper.newCellSettings().alignHorizontallyCenter());
/* 137 */     CycleButton<WorldCreationUiState.SelectedGameMode> gameModeButton = (CycleButton<WorldCreationUiState.SelectedGameMode>)helper.addChild(
/* 138 */         (LayoutElement)CycleButton.builder(selectedGameMode -> selectedGameMode.displayName, paramCreateWorldScreen.uiState.getGameMode())
/* 139 */         .withValues((Object[])new WorldCreationUiState.SelectedGameMode[] { WorldCreationUiState.SelectedGameMode.SURVIVAL, WorldCreationUiState.SelectedGameMode.HARDCORE, WorldCreationUiState.SelectedGameMode.CREATIVE }).create(0, 0, 210, 20, CreateWorldScreen.GAME_MODEL_LABEL, (button, gameMode) -> this$0.uiState.setGameMode(gameMode)), buttonLayoutSettings);
/* 144 */     paramCreateWorldScreen.uiState.addListener(data -> {
/*     */           gameModeButton.setValue(data.getGameMode());
/*     */           gameModeButton.active = !data.isDebug();
/*     */           gameModeButton.setTooltip(Tooltip.create(data.getGameMode().getInfo()));
/*     */         });
/* 150 */     CycleButton<Difficulty> difficultyButton = (CycleButton<Difficulty>)helper.addChild(
/* 151 */         (LayoutElement)CycleButton.builder(Difficulty::getDisplayName, paramCreateWorldScreen.uiState.getDifficulty())
/* 152 */         .withValues((Object[])Difficulty.values())
/* 153 */         .create(0, 0, 210, 20, (Component)Component.translatable("options.difficulty"), (button, value) -> this$0.uiState.setDifficulty(value)), buttonLayoutSettings);
/* 156 */     paramCreateWorldScreen.uiState.addListener(d -> {
/*     */           difficultyButton.setValue(this$0.uiState.getDifficulty());
/*     */           difficultyButton.active = !this$0.uiState.isHardcore();
/*     */           difficultyButton.setTooltip(Tooltip.create(this$0.uiState.getDifficulty().getInfo()));
/*     */         });
/* 162 */     CycleButton<Boolean> allowCommandsButton = (CycleButton<Boolean>)helper.addChild((LayoutElement)CycleButton.onOffBuilder(paramCreateWorldScreen.uiState.isAllowCommands())
/* 163 */         .withTooltip(state -> Tooltip.create(CreateWorldScreen.ALLOW_COMMANDS_INFO))
/* 164 */         .create(0, 0, 210, 20, ALLOW_COMMANDS, (b, state) -> this$0.uiState.setAllowCommands(state.booleanValue())));
/* 166 */     paramCreateWorldScreen.uiState.addListener(d -> {
/*     */           allowCommandsButton.setValue(Boolean.valueOf(this$0.uiState.isAllowCommands()));
/* 168 */           allowCommandsButton.active = (!this$0.uiState.isDebug() && !this$0.uiState.isHardcore());
/*     */         });
/* 171 */     if (!SharedConstants.getCurrentVersion().stable())
/* 172 */       helper.addChild((LayoutElement)Button.builder(CreateWorldScreen.EXPERIMENTS_LABEL, button -> this$0.openExperimentsScreen(this$0.uiState.getSettings().dataConfiguration())).width(210).build()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\CreateWorldScreen$GameTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */