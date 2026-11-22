/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.options.WorldOptionsScreen;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.HttpUtil;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public class MultiplayerOptionsScreen extends Screen {
/*     */   private static final int PORT_LOWER_BOUND = 1024;
/*     */   
/*     */   private static final int PORT_HIGHER_BOUND = 65535;
/*     */   
/*  28 */   private static final Component TITLE = (Component)Component.translatable("options.multiplayer.title");
/*     */   
/*  29 */   private static final Component ALLOW_COMMANDS_LABEL = (Component)Component.translatable("selectWorld.allowCommands");
/*     */   
/*  30 */   private static final Component GAME_MODE_LABEL = (Component)Component.translatable("selectWorld.gameMode");
/*     */   
/*  31 */   private static final Component PORT_INFO_TEXT = (Component)Component.translatable("lanServer.port");
/*     */   
/*  32 */   private static final Component PORT_UNAVAILABLE = (Component)Component.translatable("lanServer.port.unavailable", new Object[] { Integer.valueOf(1024), Integer.valueOf(65535) });
/*     */   
/*  33 */   private static final Component INVALID_PORT = (Component)Component.translatable("lanServer.port.invalid", new Object[] { Integer.valueOf(1024), Integer.valueOf(65535) });
/*     */   
/*  34 */   private static final Component OTHER_PLAYERS_HEADER = (Component)Component.translatable("menu.multiplayerOptions.otherPlayers.header").withStyle(new ChatFormatting[] { ChatFormatting.UNDERLINE, ChatFormatting.BOLD });
/*     */   
/*  35 */   private static final Component APPLY_CHANGES = (Component)Component.translatable("menu.multiplayerOptions.applyChanges");
/*     */   
/*  36 */   private static final Identifier INWORLD_MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");
/*     */   
/*  38 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*  41 */   private MinecraftServer.MultiplayerScope wantedMultiplayerScope = MinecraftServer.MultiplayerScope.OFF;
/*     */   
/*  43 */   private GameType gameMode = GameType.SURVIVAL;
/*     */   
/*     */   private boolean commands;
/*     */   
/*  45 */   private int port = HttpUtil.getAvailablePort();
/*     */   
/*     */   private boolean portValid = true;
/*     */   
/*     */   private Button applyChanges;
/*     */   
/*     */   private EditBox portEdit;
/*     */   
/*     */   private StringWidget portLabel;
/*     */   
/*  50 */   private MinecraftServer.MultiplayerScope initialMultiplayerScope = MinecraftServer.MultiplayerScope.OFF;
/*     */   
/*  51 */   private GameType initialGameMode = GameType.SURVIVAL;
/*     */   
/*     */   private boolean initialCommands;
/*     */   
/*     */   private int initialPort;
/*     */   
/*     */   public MultiplayerOptionsScreen(Screen lastScreen) {
/*  56 */     super(TITLE);
/*  57 */     this.lastScreen = lastScreen;
/*     */   }
/*     */   
/*     */   protected void init() {
/*  62 */     IntegratedServer singleplayerServer = this.minecraft.getSingleplayerServer();
/*  63 */     if (singleplayerServer == null) {
/*  64 */       onClose();
/*     */       return;
/*     */     } 
/*  68 */     this.layout.addTitleHeader(this.title, this.font);
/*  70 */     LinearLayout content = (LinearLayout)this.layout.addToContents((LayoutElement)LinearLayout.vertical().spacing(8));
/*  71 */     content.defaultCellSetting().alignHorizontallyCenter();
/*  72 */     this.initialMultiplayerScope = singleplayerServer.getMultiplayerScope();
/*  73 */     content.addChild((LayoutElement)CycleButton.onOffBuilder((this.initialMultiplayerScope == MinecraftServer.MultiplayerScope.LAN))
/*  74 */         .withTooltip(lan -> Tooltip.create(lan.booleanValue() ? MinecraftServer.MultiplayerScope.LAN.getTooltip() : MinecraftServer.MultiplayerScope.OFF.getTooltip()))
/*  75 */         .create((Component)Component.translatable("menu.multiplayerOptions.lan"), (paramCycleButton, value) -> {
/*     */             this.wantedMultiplayerScope = value.booleanValue() ? MinecraftServer.MultiplayerScope.LAN : MinecraftServer.MultiplayerScope.OFF;
/*     */             updatePortControlsState();
/*     */             updateApplyChangesActiveState();
/*     */           }));
/*  83 */     this.initialMultiplayerScope = singleplayerServer.getMultiplayerScope();
/*  84 */     this.wantedMultiplayerScope = this.initialMultiplayerScope;
/*  85 */     if (this.initialMultiplayerScope == MinecraftServer.MultiplayerScope.LAN) {
/*  86 */       this.port = singleplayerServer.getPort();
/*  87 */       this.initialPort = this.port;
/*     */     } 
/*  90 */     this
/*     */       
/* 101 */       .applyChanges = Button.builder(APPLY_CHANGES, paramButton -> {
/*     */           this.minecraft.gui.setScreen(null);
/*     */           if (this.gameMode != this.initialGameMode)
/*     */             singleplayerServer.setGameTypeForOtherPlayers(this.gameMode); 
/*     */           if (this.commands != this.initialCommands)
/*     */             singleplayerServer.setCommandsAllowedForOtherPlayers(this.commands); 
/*     */           if (this.wantedMultiplayerScope != this.initialMultiplayerScope || lanPortChanged())
/*     */             changeMultiplayerScope(singleplayerServer); 
/* 101 */         }).build();
/* 102 */     this.applyChanges.active = false;
/* 104 */     this.portEdit = new EditBox(this.font, PORT_INFO_TEXT);
/* 105 */     this.portEdit.setResponder(value -> {
/*     */           setPortError(tryParsePort(value));
/*     */           this.portEdit.setHint((Component)Component.literal(String.valueOf(this.port)));
/*     */           updateApplyChangesActiveState();
/*     */         });
/* 110 */     if (this.initialMultiplayerScope == MinecraftServer.MultiplayerScope.LAN)
/* 111 */       this.portEdit.setValue(String.valueOf(this.port)); 
/* 113 */     LinearLayout portRow = LinearLayout.vertical().spacing(4);
/* 114 */     this.portLabel = (StringWidget)portRow.addChild((LayoutElement)new StringWidget(PORT_INFO_TEXT, this.font));
/* 115 */     portRow.addChild((LayoutElement)this.portEdit);
/* 116 */     content.addChild((LayoutElement)portRow);
/* 118 */     content.addChild((LayoutElement)new StringWidget(OTHER_PLAYERS_HEADER, this.font));
/* 119 */     LinearLayout otherPlayerSettings = (LinearLayout)content.addChild((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 120 */     otherPlayerSettings.defaultCellSetting().alignHorizontallyCenter();
/* 122 */     this.gameMode = singleplayerServer.getGameTypeForOtherPlayers();
/* 123 */     this.initialGameMode = this.gameMode;
/* 124 */     CycleButton<GameType> gameModeButton = (CycleButton<GameType>)otherPlayerSettings.addChild((LayoutElement)CycleButton.builder(GameType::getShortDisplayName, this.gameMode)
/* 125 */         .withValues((Object[])new GameType[] { GameType.SURVIVAL, GameType.SPECTATOR, GameType.CREATIVE, GameType.ADVENTURE }).create(GAME_MODE_LABEL, (paramCycleButton, value) -> {
/*     */             this.gameMode = value;
/*     */             updateApplyChangesActiveState();
/*     */           }));
/* 131 */     this.commands = singleplayerServer.commandsAllowedForOtherPlayers();
/* 132 */     this.initialCommands = this.commands;
/* 133 */     CycleButton<Boolean> allowCommandsButton = (CycleButton<Boolean>)otherPlayerSettings.addChild((LayoutElement)CycleButton.onOffBuilder(this.commands).create(ALLOW_COMMANDS_LABEL, (paramCycleButton, value) -> {
/*     */             this.commands = value.booleanValue();
/*     */             updateApplyChangesActiveState();
/*     */           }));
/* 137 */     if (singleplayerServer.isHardcore()) {
/* 138 */       gameModeButton.active = false;
/* 139 */       gameModeButton.setTooltip(WorldOptionsScreen.GAME_MODE_DISABLED_HARDCORE_TOOLTIP);
/* 140 */       allowCommandsButton.active = false;
/* 141 */       allowCommandsButton.setTooltip(WorldOptionsScreen.ALLOW_COMMANDS_DISABLED_TOOLTIP);
/*     */     } 
/* 144 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 145 */     footer.addChild((LayoutElement)this.applyChanges);
/* 146 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, paramButton -> onClose()).build());
/* 147 */     this.layout.visitWidgets(this::addRenderableWidget);
/* 148 */     updatePortControlsState();
/* 149 */     repositionElements();
/*     */   }
/*     */   
/*     */   private void updatePortControlsState() {
/* 153 */     boolean lanWanted = (this.wantedMultiplayerScope == MinecraftServer.MultiplayerScope.LAN);
/* 154 */     if (this.portEdit != null) {
/* 155 */       String desired = lanWanted ? ((this.initialMultiplayerScope == MinecraftServer.MultiplayerScope.LAN) ? String.valueOf(this.initialPort) : "") : "";
/* 156 */       if (!this.portEdit.getValue().equals(desired))
/* 157 */         this.portEdit.setValue(desired); 
/* 159 */       this.portEdit.setEditable(lanWanted);
/* 160 */       this.portEdit.active = lanWanted;
/* 161 */       this.portEdit.setHint(lanWanted ? (Component)Component.literal(String.valueOf(this.port)) : (Component)Component.empty());
/* 162 */       if (!lanWanted) {
/* 163 */         this.portEdit.setFocused(false);
/* 164 */         setPortError((Component)null);
/*     */       } 
/*     */     } 
/* 167 */     if (this.portLabel != null)
/* 168 */       this.portLabel.setMessage(lanWanted ? 
/* 169 */           PORT_INFO_TEXT : 
/* 170 */           (Component)PORT_INFO_TEXT.copy().withStyle(ChatFormatting.GRAY)); 
/*     */   }
/*     */   
/*     */   private void setPortError(Component errorMessage) {
/* 175 */     if (this.portEdit == null)
/*     */       return; 
/* 178 */     this.portValid = (errorMessage == null);
/* 179 */     if (errorMessage == null) {
/* 180 */       this.portEdit.setTextColor(-2039584);
/* 181 */       this.portEdit.setTooltip(null);
/*     */     } else {
/* 183 */       this.portEdit.setTextColor(-2142128);
/* 184 */       this.portEdit.setTooltip(Tooltip.create(errorMessage));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void changeMultiplayerScope(IntegratedServer singleplayerServer) {
/* 189 */     if (singleplayerServer.unpublishServer())
/* 190 */       sendPublishMessage((Component)Component.translatable("menu.multiplayerOptions.publish.stopped")); 
/* 192 */     if (this.wantedMultiplayerScope != MinecraftServer.MultiplayerScope.OFF)
/* 193 */       publish(singleplayerServer, this.wantedMultiplayerScope); 
/* 195 */     this.minecraft.getPlayerSocialManager().getPresenceHandler().tryUpdatePresence();
/*     */   }
/*     */   
/*     */   private void updateApplyChangesActiveState() {
/* 199 */     if (this.applyChanges != null)
/* 200 */       this.applyChanges.active = ((!portIsRequired() || this.portValid) && hasSettingsChanges()); 
/*     */   }
/*     */   
/*     */   private boolean portIsRequired() {
/* 205 */     return (this.wantedMultiplayerScope == MinecraftServer.MultiplayerScope.LAN);
/*     */   }
/*     */   
/*     */   private boolean lanPortChanged() {
/* 209 */     return (this.wantedMultiplayerScope == MinecraftServer.MultiplayerScope.LAN && this.initialMultiplayerScope == MinecraftServer.MultiplayerScope.LAN && this.port != this.initialPort);
/*     */   }
/*     */   
/*     */   private boolean hasSettingsChanges() {
/* 215 */     return (this.wantedMultiplayerScope != this.initialMultiplayerScope || this.gameMode != this.initialGameMode || this.commands != this.initialCommands || 
/*     */       
/* 218 */       lanPortChanged());
/*     */   }
/*     */   
/*     */   private void publish(IntegratedServer singleplayerServer, MinecraftServer.MultiplayerScope scope) {
/* 222 */     boolean published = singleplayerServer.publishServer(scope, this.port);
/* 223 */     if (!published) {
/* 224 */       sendPublishMessage((Component)Component.translatable("commands.publish.failed"));
/*     */       return;
/*     */     } 
/* 229 */     MutableComponent mutableComponent = (scope == MinecraftServer.MultiplayerScope.LAN) ? Component.translatable("menu.multiplayerOptions.publish.started.lan", new Object[] { ComponentUtils.copyOnClickText(String.valueOf(this.port)) }) : Component.translatable("menu.multiplayerOptions.publish.started.online");
/* 230 */     sendPublishMessage((Component)mutableComponent);
/*     */   }
/*     */   
/*     */   private void sendPublishMessage(Component message) {
/* 234 */     this.minecraft.gui.hud.getChat().addClientSystemMessage(message);
/* 235 */     this.minecraft.getNarrator().saySystemQueued(message);
/* 236 */     this.minecraft.updateTitle();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/* 241 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 246 */     this.minecraft.gui.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   private Component tryParsePort(String value) {
/* 250 */     if (value.isBlank()) {
/* 251 */       this.port = HttpUtil.getAvailablePort();
/* 252 */       return null;
/*     */     } 
/*     */     try {
/* 255 */       int parsed = Integer.parseInt(value);
/* 256 */       if (parsed < 1024 || parsed > 65535)
/* 257 */         return INVALID_PORT; 
/* 258 */       if (parsed != this.initialPort && !HttpUtil.isPortAvailable(parsed))
/* 259 */         return PORT_UNAVAILABLE; 
/* 261 */       this.port = parsed;
/* 262 */       return null;
/* 264 */     } catch (NumberFormatException e) {
/* 265 */       this.port = HttpUtil.getAvailablePort();
/* 266 */       return INVALID_PORT;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 273 */     super.extractBackground(graphics, mouseX, mouseY, a);
/* 274 */     Identifier headerSeparator = (this.minecraft.level == null) ? Screen.HEADER_SEPARATOR : Screen.INWORLD_HEADER_SEPARATOR;
/* 275 */     Identifier footerSeparator = (this.minecraft.level == null) ? Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR;
/* 276 */     graphics.blit(RenderPipelines.GUI_TEXTURED, headerSeparator, 0, this.layout.getHeaderHeight() - 2, 0.0F, 0.0F, this.width, 2, 32, 2);
/* 277 */     graphics.blit(RenderPipelines.GUI_TEXTURED, footerSeparator, 0, this.height - this.layout.getFooterHeight() - 2, 0.0F, 0.0F, this.width, 2, 32, 2);
/* 278 */     graphics.blit(RenderPipelines.GUI_TEXTURED, INWORLD_MENU_LIST_BACKGROUND, 0, this.layout.getHeaderHeight(), this.width, (this.height - this.layout.getFooterHeight()), this.width, this.layout.getContentHeight() - 2, 32, 32);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\MultiplayerOptionsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */