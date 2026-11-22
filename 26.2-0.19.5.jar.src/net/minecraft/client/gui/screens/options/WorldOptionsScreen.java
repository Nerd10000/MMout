/*     */ package net.minecraft.client.gui.screens.options;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.multiplayer.RestrictionsScreen;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class WorldOptionsScreen extends Screen implements HasGamemasterPermissionReaction, HasDifficultyReaction {
/*  22 */   private static final Component TITLE = (Component)Component.translatable("options.worldOptions.title");
/*     */   
/*  23 */   private static final Component ALLOW_COMMANDS = (Component)Component.translatable("selectWorld.allowCommands");
/*     */   
/*  24 */   private static final Component GAME_MODE = (Component)Component.translatable("selectWorld.gameMode");
/*     */   
/*  25 */   private static final Component GAME_RULES = (Component)Component.translatable("editGamerule.inGame.button");
/*     */   
/*  26 */   private static final Tooltip GAMERULES_DISABLED_TOOLTIP = Tooltip.create((Component)Component.translatable("editGamerule.inGame.disabled.tooltip"));
/*     */   
/*  27 */   private static final Tooltip GAMERULES_DISABLED_HARDCORE_TOOLTIP = Tooltip.create((Component)Component.translatable("editGamerule.inGame.disabled.hardcore.tooltip"));
/*     */   
/*  28 */   public static final Tooltip GAME_MODE_DISABLED_HARDCORE_TOOLTIP = Tooltip.create((Component)Component.translatable("options.worldOptions.game_mode.disabled.tooltip"));
/*     */   
/*  29 */   private static final Tooltip GAME_MODE_DISABLED_OPERATOR_TOOLTIP = Tooltip.create((Component)Component.translatable("options.worldOptions.game_mode.disabled.operator.tooltip"));
/*     */   
/*  30 */   public static final Tooltip ALLOW_COMMANDS_DISABLED_TOOLTIP = Tooltip.create((Component)Component.translatable("options.worldOptions.allow_commands.disabled.tooltip"));
/*     */   
/*  31 */   private static final Component RESTRICTIONS = (Component)Component.translatable("restrictions_screen.button");
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*  34 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private final DifficultyButtons difficultyButtons;
/*     */   
/*     */   private Button gameRulesButton;
/*     */   
/*     */   private CycleButton<GameType> gameModeButton;
/*     */   
/*     */   public WorldOptionsScreen(Screen lastScreen, Level level) {
/*  40 */     super(TITLE);
/*  41 */     this.lastScreen = lastScreen;
/*  42 */     this.difficultyButtons = DifficultyButtons.create(this.minecraft, level, this);
/*     */   }
/*     */   
/*     */   protected void init() {
/*  47 */     this.layout.addToHeader((LayoutElement)new StringWidget(TITLE, this.font), LayoutSettings::alignHorizontallyCenter);
/*  49 */     GridLayout content = (GridLayout)this.layout.addToContents((LayoutElement)new GridLayout(0, 0));
/*  50 */     GridLayout.RowHelper gridHelper = content.columnSpacing(8).rowSpacing(4).createRowHelper(2);
/*  51 */     IntegratedServer singleplayerServer = this.minecraft.getSingleplayerServer();
/*  52 */     gridHelper.addChild((LayoutElement)createGameRulesButton(singleplayerServer));
/*  53 */     gridHelper.addChild(this.difficultyButtons.layout());
/*  54 */     if (singleplayerServer != null) {
/*  55 */       gridHelper.addChild((LayoutElement)createGameModeButton(singleplayerServer));
/*  56 */       gridHelper.addChild((LayoutElement)createAllowCommandsButton(singleplayerServer));
/*     */     } 
/*  58 */     gridHelper.addChild((LayoutElement)createRestrictionsButton());
/*  60 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_DONE, paramButton -> onClose()).width(200).build());
/*  61 */     WorldOptionsScreen worldOptionsScreen = this;
/*  61 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/*  62 */     repositionElements();
/*     */   }
/*     */   
/*     */   private CycleButton<GameType> createGameModeButton(IntegratedServer singleplayerServer) {
/*  66 */     this
/*     */       
/*  68 */       .gameModeButton = CycleButton.builder(GameType::getShortDisplayName, singleplayerServer.getWorldData().getGameType()).withValues((Object[])new GameType[] { GameType.SURVIVAL, GameType.SPECTATOR, GameType.CREATIVE, GameType.ADVENTURE }).create(GAME_MODE, (paramCycleButton, value) -> singleplayerServer.setWorldGameType(value));
/*  71 */     updateButton((AbstractWidget)this.gameModeButton, singleplayerServer, GAME_MODE_DISABLED_HARDCORE_TOOLTIP, GAME_MODE_DISABLED_OPERATOR_TOOLTIP);
/*  72 */     return this.gameModeButton;
/*     */   }
/*     */   
/*     */   private CycleButton<Boolean> createAllowCommandsButton(IntegratedServer singleplayerServer) {
/*  76 */     CycleButton<Boolean> allowCommandsButton = CycleButton.onOffBuilder(singleplayerServer.getWorldData().isAllowCommands()).create(ALLOW_COMMANDS, (paramCycleButton, value) -> singleplayerServer.setWorldAllowCommands(value.booleanValue()));
/*  79 */     if (singleplayerServer.isHardcore() && (this.minecraft.player == null || !this.minecraft.player.permissions().hasPermission(Permissions.COMMANDS_OWNER))) {
/*  80 */       allowCommandsButton.active = false;
/*  81 */       allowCommandsButton.setTooltip(ALLOW_COMMANDS_DISABLED_TOOLTIP);
/*     */     } 
/*  83 */     return allowCommandsButton;
/*     */   }
/*     */   
/*     */   private Button createGameRulesButton(IntegratedServer singleplayerServer) {
/*  87 */     this
/*     */       
/*  95 */       .gameRulesButton = Button.builder(GAME_RULES, paramButton -> {
/*     */           if (this.minecraft.player != null)
/*     */             this.minecraft.gui.setScreen((Screen)new InWorldGameRulesScreen(this.minecraft.player.connection, (), this)); 
/*  95 */         }).build();
/*  96 */     updateButton((AbstractWidget)this.gameRulesButton, singleplayerServer, GAMERULES_DISABLED_HARDCORE_TOOLTIP, GAMERULES_DISABLED_TOOLTIP);
/*  97 */     return this.gameRulesButton;
/*     */   }
/*     */   
/*     */   private void updateButton(AbstractWidget widget, IntegratedServer singleplayerServer, Tooltip hardcoreTooltip, Tooltip disabledTooltip) {
/* 101 */     if (widget != null) {
/* 102 */       boolean hardcore = (singleplayerServer != null && singleplayerServer.isHardcore());
/* 103 */       boolean hasGameMasterPermission = (this.minecraft.player != null && this.minecraft.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER));
/* 104 */       widget.active = (!hardcore && hasGameMasterPermission);
/* 105 */       widget.setTooltip(hardcore ? hardcoreTooltip : (hasGameMasterPermission ? null : disabledTooltip));
/*     */     } 
/*     */   }
/*     */   
/*     */   private Button createRestrictionsButton() {
/* 110 */     return Button.builder(RESTRICTIONS, paramButton -> {
/*     */           if (this.minecraft.player != null)
/*     */             this.minecraft.gui.setScreen((Screen)new RestrictionsScreen(this, this.minecraft.player.chatAbilities())); 
/* 114 */         }).build();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/* 119 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 124 */     this.minecraft.gui.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   public void onGamemasterPermissionChanged(boolean hasGamemasterPermission) {
/* 129 */     IntegratedServer singleplayerServer = this.minecraft.getSingleplayerServer();
/* 130 */     updateButton((AbstractWidget)this.gameRulesButton, singleplayerServer, GAMERULES_DISABLED_HARDCORE_TOOLTIP, GAMERULES_DISABLED_TOOLTIP);
/* 131 */     updateButton((AbstractWidget)this.gameModeButton, singleplayerServer, GAME_MODE_DISABLED_HARDCORE_TOOLTIP, GAME_MODE_DISABLED_OPERATOR_TOOLTIP);
/* 132 */     if (!hasGamemasterPermission && !this.minecraft.hasSingleplayerServer()) {
/* 133 */       this.minecraft.gui.setScreen(this.lastScreen);
/* 134 */       Screen screen = this.minecraft.gui.screen();
/* 134 */       if (screen instanceof HasGamemasterPermissionReaction) {
/* 134 */         HasGamemasterPermissionReaction hasGamemasterPermissionReaction = (HasGamemasterPermissionReaction)screen;
/* 135 */         hasGamemasterPermissionReaction.onGamemasterPermissionChanged(hasGamemasterPermission);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void added() {
/* 142 */     this.difficultyButtons.refresh(this.minecraft);
/*     */   }
/*     */   
/*     */   public void onDifficultyChanged() {
/* 147 */     this.difficultyButtons.refresh(this.minecraft);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\WorldOptionsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */