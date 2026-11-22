/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.MusicToastDisplayState;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CommonButtons;
/*     */ import net.minecraft.client.gui.components.FriendsButton;
/*     */ import net.minecraft.client.gui.components.SpriteIconButton;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.toasts.NowPlayingToast;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.achievement.StatsScreen;
/*     */ import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
/*     */ import net.minecraft.client.gui.screens.friends.FriendsOverlayScreen;
/*     */ import net.minecraft.client.gui.screens.options.OnlineOptionsScreen;
/*     */ import net.minecraft.client.gui.screens.options.OptionsScreen;
/*     */ import net.minecraft.client.gui.screens.social.PlayerSocialManager;
/*     */ import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.multiplayer.PlayerInfo;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.ServerLinks;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.server.dialog.Dialogs;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.DialogTags;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ 
/*     */ public class PauseScreen extends Screen {
/*  50 */   private static final Identifier DRAFT_REPORT_SPRITE = Identifier.withDefaultNamespace("icon/draft_report");
/*     */   
/*     */   private static final int COLUMNS = 2;
/*     */   
/*     */   private static final int MENU_PADDING_TOP = 50;
/*     */   
/*     */   private static final int BUTTON_PADDING = 4;
/*     */   
/*     */   private static final int BUTTON_WIDTH_FULL = 204;
/*     */   
/*     */   private static final int BUTTON_WIDTH_HALF = 98;
/*     */   
/*  58 */   private static final Component RETURN_TO_GAME = (Component)Component.translatable("menu.returnToGame");
/*     */   
/*  59 */   private static final Component ADVANCEMENTS = (Component)Component.translatable("gui.advancements");
/*     */   
/*  60 */   private static final Component STATS = (Component)Component.translatable("gui.stats");
/*     */   
/*  61 */   private static final Component SEND_FEEDBACK = (Component)Component.translatable("menu.sendFeedback");
/*     */   
/*  62 */   private static final Component REPORT_BUGS = (Component)Component.translatable("menu.reportBugs");
/*     */   
/*  63 */   private static final Component OPTIONS = (Component)Component.translatable("menu.options");
/*     */   
/*  64 */   private static final Component MULTIPLAYER_OPTIONS = (Component)Component.translatable("menu.multiplayerOptions.button");
/*     */   
/*  65 */   private static final Component PLAYER_REPORTING = (Component)Component.translatable("menu.playerReporting");
/*     */   
/*  66 */   private static final Component GAME = (Component)Component.translatable("menu.game");
/*     */   
/*  67 */   private static final Component PAUSED = (Component)Component.translatable("menu.paused");
/*     */   
/*  69 */   private static final Tooltip CUSTOM_OPTIONS_TOOLTIP = Tooltip.create((Component)Component.translatable("menu.custom_options.tooltip"));
/*     */   
/*  70 */   private static final Tooltip NO_PLAYERS_TO_REPORT_TOOLTIP = Tooltip.create((Component)Component.translatable("menu.playerReporting.no_players"));
/*     */   
/*  72 */   private final Runnable friendListUpdateListener = this::onFriendListUpdate;
/*     */   
/*     */   private FriendsButton friends;
/*     */   
/*     */   private final boolean showPauseMenu;
/*     */   
/*     */   private Button disconnectButton;
/*     */   
/*     */   public PauseScreen(boolean showPauseMenu) {
/*  80 */     super(showPauseMenu ? GAME : PAUSED);
/*  81 */     this.showPauseMenu = showPauseMenu;
/*     */   }
/*     */   
/*     */   public boolean showsPauseMenu() {
/*  85 */     return this.showPauseMenu;
/*     */   }
/*     */   
/*     */   protected void init() {
/*  90 */     if (this.showPauseMenu)
/*  91 */       createPauseMenu(); 
/*  94 */     int textWidth = this.font.width((FormattedText)this.title);
/*  95 */     Objects.requireNonNull(this.font);
/*  95 */     addRenderableWidget(new StringWidget(this.width / 2 - textWidth / 2, this.showPauseMenu ? 40 : 10, textWidth, 9, this.title, this.font));
/*     */   }
/*     */   
/*     */   private void createPauseMenu() {
/*  99 */     GridLayout gridLayout = new GridLayout();
/* 100 */     gridLayout.defaultCellSetting().padding(4, 4, 4, 0);
/* 102 */     GridLayout.RowHelper helper = gridLayout.createRowHelper(2);
/* 104 */     helper.addChild(
/* 105 */         (LayoutElement)Button.builder(RETURN_TO_GAME, button -> {
/*     */             this.minecraft.gui.setScreen(null);
/*     */             this.minecraft.mouseHandler.grabMouse();
/* 108 */           }).width(204).build(), 2, gridLayout
/* 109 */         .newCellSettings().paddingTop(50));
/* 112 */     helper.addChild((LayoutElement)openScreenButton(ADVANCEMENTS, () -> new AdvancementsScreen(this.minecraft.player.connection.getAdvancements(), this)));
/* 113 */     helper.addChild((LayoutElement)openScreenButton(STATS, () -> new StatsScreen(this, this.minecraft.player.getStats())));
/* 115 */     LinearLayout iconButtonRow = LinearLayout.horizontal().spacing(4);
/* 120 */     SpriteIconButton reportBugsButton = SpriteIconButton.builder(REPORT_BUGS, ConfirmLinkScreen.confirmLink(this, CommonLinks.SNAPSHOT_BUGS_FEEDBACK), true).width(20).sprite(Identifier.withDefaultNamespace("pause_menu/bug"), 15, 15).withTootip().build();
/* 121 */     reportBugsButton.active = !SharedConstants.getCurrentVersion().dataVersion().isSideSeries();
/* 122 */     iconButtonRow.addChild((LayoutElement)reportBugsButton);
/* 128 */     SpriteIconButton feedbackButton = SpriteIconButton.builder(SEND_FEEDBACK, ConfirmLinkScreen.confirmLink(this, SharedConstants.getCurrentVersion().stable() ? CommonLinks.RELEASE_FEEDBACK : CommonLinks.SNAPSHOT_FEEDBACK), true).width(20).sprite(Identifier.withDefaultNamespace("pause_menu/social_interactions"), 15, 15).withTootip().build();
/* 129 */     iconButtonRow.addChild((LayoutElement)feedbackButton);
/* 131 */     PlayerSocialManager playerSocialManager = this.minecraft.getPlayerSocialManager();
/* 132 */     if (playerSocialManager.isFriendListEnabled())
/* 133 */       playerSocialManager.addFriendListUpdateListener(this.friendListUpdateListener); 
/* 136 */     this.friends = CommonButtons.friends(20, paramButton -> OnlineOptionsScreen.confirmFriendsListEnabled(this.minecraft, (), this), 
/*     */         
/* 139 */         !this.minecraft.isDemo());
/* 140 */     iconButtonRow.addChild((LayoutElement)this.friends);
/* 146 */     SpriteIconButton playerReportingButton = SpriteIconButton.builder(PLAYER_REPORTING, paramButton -> this.minecraft.gui.setScreen((Screen)new SocialInteractionsScreen(this)), true).width(20).sprite(Identifier.withDefaultNamespace("pause_menu/player_reporting"), 15, 15).withTootip().build();
/* 147 */     iconButtonRow.addChild((LayoutElement)playerReportingButton);
/* 148 */     IntegratedServer integratedServer = this.minecraft.getSingleplayerServer();
/* 149 */     if (integratedServer != null && 
/* 150 */       this.minecraft.player != null) {
/* 151 */       List<Map.Entry<UUID, PlayerInfo>> list = this.minecraft.player.connection.getSeenPlayers().entrySet().stream().filter(entry -> (entry.getKey() != this.minecraft.player.getUUID())).toList();
/* 152 */       if (list.isEmpty()) {
/* 153 */         playerReportingButton.active = false;
/* 154 */         playerReportingButton.setTooltip(NO_PLAYERS_TO_REPORT_TOOLTIP);
/*     */       } 
/*     */     } 
/* 158 */     helper.addChild((LayoutElement)iconButtonRow, 2, gridLayout.newCellSettings().alignHorizontallyCenter());
/* 160 */     Optional<? extends Holder<Dialog>> additions = getCustomAdditions();
/* 161 */     additions.ifPresent(dialogHolder -> addCustomDialogButtons(this.minecraft, dialogHolder, helper));
/* 163 */     if (this.minecraft.hasSingleplayerServer()) {
/* 164 */       helper.addChild((LayoutElement)openScreenButton(OPTIONS, () -> new OptionsScreen(this, this.minecraft.options, true)));
/* 165 */       helper.addChild((LayoutElement)openScreenButton(MULTIPLAYER_OPTIONS, () -> new MultiplayerOptionsScreen(this)));
/*     */     } else {
/* 167 */       helper.addChild((LayoutElement)Button.builder(OPTIONS, paramButton -> this.minecraft.gui.setScreen((Screen)new OptionsScreen(this, this.minecraft.options, true))).width(204).build(), 2);
/*     */     } 
/* 170 */     this.disconnectButton = (Button)helper.addChild(
/* 171 */         (LayoutElement)Button.builder(CommonComponents.disconnectButtonLabel(this.minecraft.isLocalServer()), button -> {
/*     */             button.active = false;
/*     */             this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, (), true);
/* 175 */           }).width(204).build(), 2);
/* 179 */     gridLayout.arrangeElements();
/* 180 */     FrameLayout.alignInRectangle((LayoutElement)gridLayout, 0, 0, this.width, this.height, 0.5F, 0.25F);
/* 182 */     gridLayout.visitWidgets(this::addRenderableWidget);
/*     */   }
/*     */   
/*     */   private Optional<? extends Holder<Dialog>> getCustomAdditions() {
/* 186 */     Registry<Dialog> dialogRegistry = this.minecraft.player.connection.registryAccess().lookupOrThrow(Registries.DIALOG);
/* 187 */     Optional<? extends HolderSet<Dialog>> maybeCustomAdditions = dialogRegistry.get(DialogTags.PAUSE_SCREEN_ADDITIONS);
/* 189 */     if (maybeCustomAdditions.isPresent()) {
/* 190 */       HolderSet<Dialog> customAdditions = maybeCustomAdditions.get();
/* 191 */       if (customAdditions.size() > 0) {
/* 192 */         if (customAdditions.size() == 1)
/* 194 */           return Optional.of(customAdditions.get(0)); 
/* 197 */         return dialogRegistry.get(Dialogs.CUSTOM_OPTIONS);
/*     */       } 
/*     */     } 
/* 203 */     ServerLinks serverLinks = this.minecraft.player.connection.serverLinks();
/* 204 */     if (!serverLinks.isEmpty())
/* 205 */       return dialogRegistry.get(Dialogs.SERVER_LINKS); 
/* 208 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private void addCustomDialogButtons(Minecraft minecraft, Holder<Dialog> dialog, GridLayout.RowHelper helper) {
/* 212 */     helper.addChild(
/* 213 */         (LayoutElement)Button.builder(((Dialog)dialog.value()).common().computeExternalTitle(), button -> minecraft.player.connection.showDialog(dialog, this))
/* 214 */         .width(204)
/* 215 */         .tooltip(CUSTOM_OPTIONS_TOOLTIP)
/* 216 */         .build(), 2);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 223 */     if (rendersNowPlayingToast())
/* 224 */       NowPlayingToast.tickMusicNotes(); 
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 230 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/* 232 */     if (rendersNowPlayingToast())
/* 233 */       NowPlayingToast.extractToast(graphics, this.font); 
/* 236 */     if (this.showPauseMenu && this.minecraft.getReportingContext().hasDraftReport() && this.disconnectButton != null)
/* 237 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DRAFT_REPORT_SPRITE, this.disconnectButton.getX() + this.disconnectButton.getWidth() - 17, this.disconnectButton.getY() + 3, 15, 15); 
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 243 */     if (this.showPauseMenu) {
/* 244 */       if (isTopmostScreen())
/* 245 */         extractBlurredBackground(graphics); 
/* 247 */       extractMenuBackground(graphics);
/* 248 */       this.minecraft.gui.hud.extractDeferredSubtitles();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 254 */     super.onClose();
/* 255 */     this.minecraft.getPlayerSocialManager().removeFriendListUpdateListener(this.friendListUpdateListener);
/*     */   }
/*     */   
/*     */   public boolean rendersNowPlayingToast() {
/* 259 */     Options options = this.minecraft.options;
/* 260 */     return (((MusicToastDisplayState)options.musicToast().get()).renderInPauseScreen() && options
/* 261 */       .getFinalSoundSourceVolume(SoundSource.MUSIC) > 0.0F && this.showPauseMenu);
/*     */   }
/*     */   
/*     */   private boolean isTopmostScreen() {
/* 266 */     return (this.minecraft.gui.screen() == this);
/*     */   }
/*     */   
/*     */   private Button openScreenButton(Component message, Supplier<Screen> newScreen) {
/* 270 */     return Button.builder(message, paramButton -> this.minecraft.gui.setScreen(newScreen.get())).width(98).build();
/*     */   }
/*     */   
/*     */   private void onFriendListUpdate() {
/* 274 */     if (this.friends != null)
/* 275 */       this.friends.refreshIncomingRequestCount(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\PauseScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */