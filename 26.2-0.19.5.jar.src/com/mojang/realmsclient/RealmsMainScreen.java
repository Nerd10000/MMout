/*      */ package com.mojang.realmsclient;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.util.concurrent.RateLimiter;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.realmsclient.client.Ping;
/*      */ import com.mojang.realmsclient.client.RealmsClient;
/*      */ import com.mojang.realmsclient.dto.PingResult;
/*      */ import com.mojang.realmsclient.dto.RealmsNews;
/*      */ import com.mojang.realmsclient.dto.RealmsNotification;
/*      */ import com.mojang.realmsclient.dto.RealmsServer;
/*      */ import com.mojang.realmsclient.dto.RealmsServerPlayerLists;
/*      */ import com.mojang.realmsclient.dto.RegionPingResult;
/*      */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*      */ import com.mojang.realmsclient.gui.RealmsDataFetcher;
/*      */ import com.mojang.realmsclient.gui.RealmsServerList;
/*      */ import com.mojang.realmsclient.gui.screens.AddRealmPopupScreen;
/*      */ import com.mojang.realmsclient.gui.screens.RealmsCreateRealmScreen;
/*      */ import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
/*      */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
/*      */ import com.mojang.realmsclient.gui.screens.RealmsPendingInvitesScreen;
/*      */ import com.mojang.realmsclient.gui.screens.RealmsPopups;
/*      */ import com.mojang.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
/*      */ import com.mojang.realmsclient.gui.task.DataFetcher;
/*      */ import com.mojang.realmsclient.util.RealmsPersistence;
/*      */ import com.mojang.realmsclient.util.RealmsUtil;
/*      */ import com.mojang.realmsclient.util.task.GetServerDetailsTask;
/*      */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Supplier;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.gui.Font;
/*      */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*      */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*      */ import net.minecraft.client.gui.components.AbstractWidget;
/*      */ import net.minecraft.client.gui.components.Button;
/*      */ import net.minecraft.client.gui.components.CycleButton;
/*      */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*      */ import net.minecraft.client.gui.components.ImageButton;
/*      */ import net.minecraft.client.gui.components.ImageWidget;
/*      */ import net.minecraft.client.gui.components.LoadingDotsWidget;
/*      */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*      */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*      */ import net.minecraft.client.gui.components.PlayerFaceExtractor;
/*      */ import net.minecraft.client.gui.components.PopupScreen;
/*      */ import net.minecraft.client.gui.components.SpriteIconButton;
/*      */ import net.minecraft.client.gui.components.Tooltip;
/*      */ import net.minecraft.client.gui.components.WidgetSprites;
/*      */ import net.minecraft.client.gui.components.WidgetTooltipHolder;
/*      */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*      */ import net.minecraft.client.gui.layouts.FrameLayout;
/*      */ import net.minecraft.client.gui.layouts.GridLayout;
/*      */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*      */ import net.minecraft.client.gui.layouts.Layout;
/*      */ import net.minecraft.client.gui.layouts.LayoutElement;
/*      */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*      */ import net.minecraft.client.gui.layouts.LinearLayout;
/*      */ import net.minecraft.client.gui.layouts.SpacerElement;
/*      */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*      */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*      */ import net.minecraft.client.gui.screens.Screen;
/*      */ import net.minecraft.client.gui.screens.inventory.tooltip.ClientActivePlayersTooltip;
/*      */ import net.minecraft.client.input.KeyEvent;
/*      */ import net.minecraft.client.input.MouseButtonEvent;
/*      */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*      */ import net.minecraft.client.renderer.RenderPipelines;
/*      */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SoundInstance;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.FormattedText;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.realms.RealmsScreen;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.util.CommonLinks;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.item.component.ResolvableProfile;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class RealmsMainScreen extends RealmsScreen {
/*   91 */   private static final Identifier INFO_SPRITE = Identifier.withDefaultNamespace("icon/info");
/*      */   
/*   92 */   private static final Identifier NEW_REALM_SPRITE = Identifier.withDefaultNamespace("icon/new_realm");
/*      */   
/*   93 */   private static final Identifier EXPIRED_SPRITE = Identifier.withDefaultNamespace("realm_status/expired");
/*      */   
/*   94 */   private static final Identifier EXPIRES_SOON_SPRITE = Identifier.withDefaultNamespace("realm_status/expires_soon");
/*      */   
/*   95 */   private static final Identifier OPEN_SPRITE = Identifier.withDefaultNamespace("realm_status/open");
/*      */   
/*   96 */   private static final Identifier CLOSED_SPRITE = Identifier.withDefaultNamespace("realm_status/closed");
/*      */   
/*   97 */   private static final Identifier INVITE_SPRITE = Identifier.withDefaultNamespace("icon/invite");
/*      */   
/*   98 */   private static final Identifier NEWS_SPRITE = Identifier.withDefaultNamespace("icon/news");
/*      */   
/*   99 */   public static final Identifier HARDCORE_MODE_SPRITE = Identifier.withDefaultNamespace("hud/heart/hardcore_full");
/*      */   
/*  101 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*  103 */   private static final Identifier NO_REALMS_LOCATION = Identifier.withDefaultNamespace("textures/gui/realms/no_realms.png");
/*      */   
/*  105 */   private static final Component TITLE = (Component)Component.translatable("menu.online");
/*      */   
/*  106 */   private static final Component LOADING_TEXT = (Component)Component.translatable("mco.selectServer.loading");
/*      */   
/*  107 */   private static final Component SERVER_UNITIALIZED_TEXT = (Component)Component.translatable("mco.selectServer.uninitialized");
/*      */   
/*  108 */   private static final Component SUBSCRIPTION_EXPIRED_TEXT = (Component)Component.translatable("mco.selectServer.expiredList");
/*      */   
/*  109 */   private static final Component SUBSCRIPTION_RENEW_TEXT = (Component)Component.translatable("mco.selectServer.expiredRenew");
/*      */   
/*  110 */   private static final Component TRIAL_EXPIRED_TEXT = (Component)Component.translatable("mco.selectServer.expiredTrial");
/*      */   
/*  111 */   private static final Component PLAY_TEXT = (Component)Component.translatable("mco.selectServer.play");
/*      */   
/*  112 */   private static final Component LEAVE_SERVER_TEXT = (Component)Component.translatable("mco.selectServer.leave");
/*      */   
/*  113 */   private static final Component CONFIGURE_SERVER_TEXT = (Component)Component.translatable("mco.selectServer.configure");
/*      */   
/*  114 */   private static final Component SERVER_EXPIRED_TOOLTIP = (Component)Component.translatable("mco.selectServer.expired");
/*      */   
/*  115 */   private static final Component SERVER_EXPIRES_SOON_TOOLTIP = (Component)Component.translatable("mco.selectServer.expires.soon");
/*      */   
/*  116 */   private static final Component SERVER_EXPIRES_IN_DAY_TOOLTIP = (Component)Component.translatable("mco.selectServer.expires.day");
/*      */   
/*  117 */   private static final Component SERVER_OPEN_TOOLTIP = (Component)Component.translatable("mco.selectServer.open");
/*      */   
/*  118 */   private static final Component SERVER_CLOSED_TOOLTIP = (Component)Component.translatable("mco.selectServer.closed");
/*      */   
/*  119 */   private static final Component UNITIALIZED_WORLD_NARRATION = (Component)Component.translatable("gui.narrate.button", new Object[] { SERVER_UNITIALIZED_TEXT });
/*      */   
/*  120 */   private static final Component NO_REALMS_TEXT = (Component)Component.translatable("mco.selectServer.noRealms");
/*      */   
/*  121 */   private static final Component NO_PENDING_INVITES = (Component)Component.translatable("mco.invites.nopending");
/*      */   
/*  122 */   private static final Component PENDING_INVITES = (Component)Component.translatable("mco.invites.pending");
/*      */   
/*  123 */   private static final Component INCOMPATIBLE_POPUP_TITLE = (Component)Component.translatable("mco.compatibility.incompatible.popup.title");
/*      */   
/*  124 */   private static final Component INCOMPATIBLE_RELEASE_TYPE_POPUP_MESSAGE = (Component)Component.translatable("mco.compatibility.incompatible.releaseType.popup.message");
/*      */   
/*      */   private static final int BUTTON_WIDTH = 100;
/*      */   
/*      */   private static final int BUTTON_COLUMNS = 3;
/*      */   
/*      */   private static final int BUTTON_SPACING = 4;
/*      */   
/*      */   private static final int CONTENT_WIDTH = 308;
/*      */   
/*      */   private static final int LOGO_PADDING = 5;
/*      */   
/*      */   private static final int HEADER_HEIGHT = 44;
/*      */   
/*      */   private static final int FOOTER_PADDING = 11;
/*      */   
/*      */   private static final int NEW_REALM_SPRITE_WIDTH = 40;
/*      */   
/*      */   private static final int NEW_REALM_SPRITE_HEIGHT = 20;
/*      */   
/*  136 */   private static final boolean SNAPSHOT = !SharedConstants.getCurrentVersion().stable();
/*      */   
/*  137 */   private static boolean snapshotToggle = SNAPSHOT;
/*      */   
/*  139 */   private final CompletableFuture<RealmsAvailability.Result> availability = RealmsAvailability.get();
/*      */   
/*      */   private DataFetcher.Subscription dataSubscription;
/*      */   
/*  143 */   private final Set<UUID> handledSeenNotifications = new HashSet<>();
/*      */   
/*      */   private static boolean regionsPinged;
/*      */   
/*      */   private final RateLimiter inviteNarrationLimiter;
/*      */   
/*      */   private final Screen lastScreen;
/*      */   
/*      */   private Button playButton;
/*      */   
/*      */   private Button backButton;
/*      */   
/*      */   private Button renewButton;
/*      */   
/*      */   private Button configureButton;
/*      */   
/*      */   private Button leaveButton;
/*      */   
/*      */   private RealmSelectionList realmSelectionList;
/*      */   
/*      */   private RealmsServerList serverList;
/*      */   
/*  159 */   private List<RealmsServer> availableSnapshotServers = List.of();
/*      */   
/*  160 */   private RealmsServerPlayerLists onlinePlayersPerRealm = new RealmsServerPlayerLists(Map.of());
/*      */   
/*      */   private volatile boolean trialsAvailable;
/*      */   
/*      */   private volatile String newsLink;
/*      */   
/*  165 */   private final List<RealmsNotification> notifications = new ArrayList<>();
/*      */   
/*      */   private Button addRealmButton;
/*      */   
/*      */   private NotificationButton pendingInvitesButton;
/*      */   
/*      */   private NotificationButton newsButton;
/*      */   
/*      */   private LayoutState activeLayoutState;
/*      */   
/*      */   private HeaderAndFooterLayout layout;
/*      */   
/*      */   public RealmsMainScreen(Screen lastScreen) {
/*  176 */     super(TITLE);
/*  177 */     this.lastScreen = lastScreen;
/*  178 */     this.inviteNarrationLimiter = RateLimiter.create(0.01666666753590107D);
/*      */   }
/*      */   
/*      */   public void init() {
/*  183 */     this.serverList = new RealmsServerList(this.minecraft);
/*  185 */     this.realmSelectionList = new RealmSelectionList();
/*  187 */     MutableComponent mutableComponent1 = Component.translatable("mco.invites.title");
/*  188 */     this.pendingInvitesButton = new NotificationButton((Component)mutableComponent1, INVITE_SPRITE, paramButton -> this.minecraft.gui.setScreen((Screen)new RealmsPendingInvitesScreen((Screen)this, invitesTitle)), null);
/*  190 */     MutableComponent mutableComponent2 = Component.translatable("mco.news");
/*  191 */     this.newsButton = new NotificationButton((Component)mutableComponent2, NEWS_SPRITE, b -> {
/*      */           String newsLink = this.newsLink;
/*      */           if (newsLink == null)
/*      */             return; 
/*      */           ConfirmLinkScreen.confirmLinkNow((Screen)this, newsLink);
/*      */           if (this.newsButton.notificationCount() != 0) {
/*      */             RealmsPersistence.RealmsPersistenceData data = RealmsPersistence.readFile();
/*      */             data.hasUnreadNews = false;
/*      */             RealmsPersistence.writeFile(data);
/*      */             this.newsButton.setNotificationCount(0);
/*      */           } 
/*  191 */         }(Component)mutableComponent2);
/*  207 */     this.playButton = Button.builder(PLAY_TEXT, button -> play(getSelectedServer(), (Screen)this)).width(100).build();
/*  208 */     this.configureButton = Button.builder(CONFIGURE_SERVER_TEXT, button -> configureClicked(getSelectedServer())).width(100).build();
/*  209 */     this.renewButton = Button.builder(SUBSCRIPTION_RENEW_TEXT, button -> onRenew(getSelectedServer())).width(100).build();
/*  211 */     this.leaveButton = Button.builder(LEAVE_SERVER_TEXT, button -> leaveClicked(getSelectedServer())).width(100).build();
/*  212 */     this.addRealmButton = Button.builder((Component)Component.translatable("mco.selectServer.purchase"), button -> openTrialAvailablePopup()).size(100, 20).build();
/*  213 */     this.backButton = Button.builder(CommonComponents.GUI_BACK, button -> onClose()).width(100).build();
/*  215 */     if (RealmsClient.ENVIRONMENT == RealmsClient.Environment.STAGE)
/*  216 */       addRenderableWidget((GuiEventListener)CycleButton.booleanBuilder((Component)Component.literal("Snapshot"), (Component)Component.literal("Release"), snapshotToggle).create(5, 5, 100, 20, (Component)Component.literal("Realm"), (button, value) -> {
/*      */               snapshotToggle = value.booleanValue();
/*      */               this.availableSnapshotServers = List.of();
/*      */               debugRefreshDataFetchers();
/*      */             })); 
/*  223 */     updateLayout(LayoutState.LOADING);
/*  224 */     updateButtonStates();
/*  226 */     this.availability.thenAcceptAsync(result -> {
/*      */           Screen errorScreen = result.createErrorScreen(this.lastScreen);
/*      */           if (errorScreen == null) {
/*      */             this.dataSubscription = initDataFetcher(this.minecraft.realmsDataFetcher());
/*      */           } else {
/*      */             this.minecraft.gui.setScreen(errorScreen);
/*      */           } 
/*  226 */         }this.screenExecutor);
/*      */   }
/*      */   
/*      */   public static boolean isSnapshot() {
/*  237 */     return (SNAPSHOT && snapshotToggle);
/*      */   }
/*      */   
/*      */   protected void repositionElements() {
/*  242 */     if (this.layout != null) {
/*  243 */       this.realmSelectionList.updateSize(this.width, this.layout);
/*  244 */       this.layout.arrangeElements();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void onClose() {
/*  250 */     this.minecraft.gui.setScreen(this.lastScreen);
/*      */   }
/*      */   
/*      */   private void updateLayout() {
/*  254 */     if (this.serverList.isEmpty() && this.availableSnapshotServers.isEmpty() && this.notifications.isEmpty()) {
/*  255 */       updateLayout(LayoutState.NO_REALMS);
/*      */     } else {
/*  257 */       updateLayout(LayoutState.LIST);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateLayout(LayoutState state) {
/*  262 */     if (this.activeLayoutState == state)
/*      */       return; 
/*  265 */     if (this.layout != null) {
/*  266 */       RealmsMainScreen realmsMainScreen1 = this;
/*  266 */       this.layout.visitWidgets(x$0 -> rec$.removeWidget((GuiEventListener)x$0));
/*      */     } 
/*  268 */     this.layout = createLayout(state);
/*  269 */     this.activeLayoutState = state;
/*  270 */     RealmsMainScreen realmsMainScreen = this;
/*  270 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/*  271 */     repositionElements();
/*      */   }
/*      */   
/*      */   private HeaderAndFooterLayout createLayout(LayoutState state) {
/*  275 */     HeaderAndFooterLayout layout = new HeaderAndFooterLayout((Screen)this);
/*  276 */     layout.setHeaderHeight(44);
/*  277 */     layout.addToHeader((LayoutElement)createHeader());
/*  279 */     Layout footer = createFooter(state);
/*  280 */     footer.arrangeElements();
/*  281 */     layout.setFooterHeight(footer.getHeight() + 22);
/*  282 */     layout.addToFooter((LayoutElement)footer);
/*  284 */     switch (state.ordinal()) {
/*      */       case 0:
/*  285 */         layout.addToContents((LayoutElement)new LoadingDotsWidget(this.font, LOADING_TEXT));
/*      */         break;
/*      */       case 1:
/*  286 */         layout.addToContents((LayoutElement)createNoRealmsContent());
/*      */         break;
/*      */       case 2:
/*  287 */         layout.addToContents((LayoutElement)this.realmSelectionList);
/*      */         break;
/*      */     } 
/*  290 */     return layout;
/*      */   }
/*      */   
/*      */   private Layout createHeader() {
/*  294 */     int sideCellWidth = 90;
/*  296 */     LinearLayout buttons = LinearLayout.horizontal().spacing(4);
/*  297 */     buttons.defaultCellSetting().alignVerticallyMiddle();
/*  298 */     buttons.addChild((LayoutElement)this.pendingInvitesButton);
/*  299 */     buttons.addChild((LayoutElement)this.newsButton);
/*  301 */     LinearLayout header = LinearLayout.horizontal();
/*  302 */     header.defaultCellSetting().alignVerticallyMiddle();
/*  304 */     header.addChild((LayoutElement)SpacerElement.width(90));
/*  305 */     header.addChild((LayoutElement)realmsLogo(), LayoutSettings::alignHorizontallyCenter);
/*  306 */     ((FrameLayout)header.addChild((LayoutElement)new FrameLayout(90, 44))).addChild((LayoutElement)buttons, LayoutSettings::alignHorizontallyRight);
/*  308 */     return (Layout)header;
/*      */   }
/*      */   
/*      */   private Layout createFooter(LayoutState state) {
/*  312 */     GridLayout footer = (new GridLayout()).spacing(4);
/*  313 */     GridLayout.RowHelper helper = footer.createRowHelper(3);
/*  315 */     if (state == LayoutState.LIST) {
/*  316 */       helper.addChild((LayoutElement)this.playButton);
/*  317 */       helper.addChild((LayoutElement)this.configureButton);
/*  318 */       helper.addChild((LayoutElement)this.renewButton);
/*  319 */       helper.addChild((LayoutElement)this.leaveButton);
/*      */     } 
/*  321 */     helper.addChild((LayoutElement)this.addRealmButton);
/*  322 */     helper.addChild((LayoutElement)this.backButton);
/*  324 */     return (Layout)footer;
/*      */   }
/*      */   
/*      */   private LinearLayout createNoRealmsContent() {
/*  328 */     LinearLayout content = LinearLayout.vertical().spacing(8);
/*  329 */     content.defaultCellSetting().alignHorizontallyCenter();
/*  331 */     content.addChild((LayoutElement)ImageWidget.texture(130, 64, NO_REALMS_LOCATION, 130, 64));
/*  333 */     content.addChild((LayoutElement)FocusableTextWidget.builder(NO_REALMS_TEXT, this.font).maxWidth(308).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.ON_FOCUS).build());
/*  335 */     return content;
/*      */   }
/*      */   
/*      */   private void updateButtonStates() {
/*  339 */     RealmsServer server = getSelectedServer();
/*  340 */     boolean serverSelected = (server != null);
/*  341 */     this.addRealmButton.active = (this.activeLayoutState != LayoutState.LOADING);
/*  342 */     this.playButton.active = (serverSelected && server.shouldPlayButtonBeActive());
/*  343 */     if (!this.playButton.active && serverSelected && server.state == RealmsServer.State.CLOSED)
/*  344 */       this.playButton.setTooltip(Tooltip.create(RealmsServer.WORLD_CLOSED_COMPONENT)); 
/*  346 */     this.renewButton.active = (serverSelected && shouldRenewButtonBeActive(server));
/*  347 */     this.leaveButton.active = (serverSelected && shouldLeaveButtonBeActive(server));
/*  348 */     this.configureButton.active = (serverSelected && shouldConfigureButtonBeActive(server));
/*      */   }
/*      */   
/*      */   private boolean shouldRenewButtonBeActive(RealmsServer server) {
/*  352 */     return (server.expired && isSelfOwnedServer(server));
/*      */   }
/*      */   
/*      */   private boolean shouldConfigureButtonBeActive(RealmsServer server) {
/*  356 */     return (isSelfOwnedServer(server) && server.state != RealmsServer.State.UNINITIALIZED);
/*      */   }
/*      */   
/*      */   private boolean shouldLeaveButtonBeActive(RealmsServer server) {
/*  360 */     return !isSelfOwnedServer(server);
/*      */   }
/*      */   
/*      */   public void tick() {
/*  365 */     super.tick();
/*  367 */     if (this.dataSubscription != null)
/*  368 */       this.dataSubscription.tick(); 
/*      */   }
/*      */   
/*      */   public static void refreshPendingInvites() {
/*  373 */     (Minecraft.getInstance().realmsDataFetcher()).pendingInvitesTask.reset();
/*      */   }
/*      */   
/*      */   public static void refreshServerList() {
/*  377 */     (Minecraft.getInstance().realmsDataFetcher()).serverListUpdateTask.reset();
/*      */   }
/*      */   
/*      */   private void debugRefreshDataFetchers() {
/*  381 */     for (DataFetcher.Task<?> task : (Iterable<DataFetcher.Task<?>>)this.minecraft.realmsDataFetcher().getTasks())
/*  382 */       task.reset(); 
/*      */   }
/*      */   
/*      */   private DataFetcher.Subscription initDataFetcher(RealmsDataFetcher dataSource) {
/*  387 */     DataFetcher.Subscription result = dataSource.dataFetcher.createSubscription();
/*  389 */     result.subscribe(dataSource.serverListUpdateTask, updatedServers -> {
/*      */           this.serverList.updateServersList(updatedServers.serverList());
/*      */           this.availableSnapshotServers = updatedServers.availableSnapshotServers();
/*      */           refreshListAndLayout();
/*      */           boolean ownsNonExpiredRealmServer = false;
/*      */           for (RealmsServer retrievedServer : this.serverList) {
/*      */             if (isSelfOwnedNonExpiredServer(retrievedServer))
/*      */               ownsNonExpiredRealmServer = true; 
/*      */           } 
/*      */           if (!regionsPinged && ownsNonExpiredRealmServer) {
/*      */             regionsPinged = true;
/*      */             pingRegions();
/*      */           } 
/*      */         });
/*  407 */     callRealmsClient(RealmsClient::getNotifications, retrievedNotifications -> {
/*      */           this.notifications.clear();
/*      */           this.notifications.addAll(retrievedNotifications);
/*      */           for (RealmsNotification notification : retrievedNotifications) {
/*      */             if (notification instanceof RealmsNotification.InfoPopup) {
/*      */               RealmsNotification.InfoPopup popup = (RealmsNotification.InfoPopup)notification;
/*      */               PopupScreen popupScreen = popup.buildScreen((Screen)this, this::dismissNotification);
/*      */               if (popupScreen != null) {
/*      */                 this.minecraft.gui.setScreen((Screen)popupScreen);
/*      */                 markNotificationsAsSeen(List.of(notification));
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           if (!this.notifications.isEmpty() && this.activeLayoutState != LayoutState.LOADING)
/*      */             refreshListAndLayout(); 
/*      */         });
/*  428 */     result.subscribe(dataSource.pendingInvitesTask, numberOfPendingInvites -> {
/*      */           this.pendingInvitesButton.setNotificationCount(numberOfPendingInvites.intValue());
/*      */           this.pendingInvitesButton.setTooltip((numberOfPendingInvites.intValue() == 0) ? Tooltip.create(NO_PENDING_INVITES) : Tooltip.create(PENDING_INVITES));
/*      */           if (numberOfPendingInvites.intValue() > 0 && this.inviteNarrationLimiter.tryAcquire(1))
/*      */             this.minecraft.getNarrator().saySystemNow((Component)Component.translatable("mco.configure.world.invite.narration", new Object[] { numberOfPendingInvites })); 
/*      */         });
/*  436 */     result.subscribe(dataSource.trialAvailabilityTask, newStatus -> this.trialsAvailable = newStatus.booleanValue());
/*  438 */     result.subscribe(dataSource.onlinePlayersTask, playerList -> this.onlinePlayersPerRealm = playerList);
/*  440 */     result.subscribe(dataSource.newsTask, news -> {
/*      */           dataSource.newsManager.updateUnreadNews(news);
/*      */           this.newsLink = dataSource.newsManager.newsLink();
/*      */           this.newsButton.setNotificationCount(dataSource.newsManager.hasUnreadNews() ? Integer.MAX_VALUE : 0);
/*      */         });
/*  446 */     return result;
/*      */   }
/*      */   
/*      */   private void markNotificationsAsSeen(Collection<RealmsNotification> notifications) {
/*  450 */     List<UUID> seenNotifications = new ArrayList<>(notifications.size());
/*  451 */     for (RealmsNotification notification : notifications) {
/*  452 */       if (!notification.seen() && !this.handledSeenNotifications.contains(notification.uuid()))
/*  453 */         seenNotifications.add(notification.uuid()); 
/*      */     } 
/*  457 */     if (!seenNotifications.isEmpty())
/*  458 */       callRealmsClient(realmsClient -> {
/*      */             realmsClient.notificationsSeen(seenNotifications);
/*      */             return null;
/*      */           }ignored -> this.handledSeenNotifications.addAll(seenNotifications)); 
/*      */   }
/*      */   
/*      */   private static <T> void callRealmsClient(RealmsCall<T> supplier, Consumer<T> callback) {
/*  470 */     Minecraft minecraft = Minecraft.getInstance();
/*  471 */     CompletableFuture.<T>supplyAsync(() -> {
/*      */           try {
/*      */             return supplier.request(RealmsClient.getOrCreate(minecraft));
/*  474 */           } catch (RealmsServiceException e) {
/*      */             throw new RuntimeException(e);
/*      */           } 
/*  477 */         }(Executor)Util.nonCriticalIoPool()).thenAcceptAsync(callback, (Executor)minecraft).exceptionally(e -> {
/*      */           LOGGER.error("Failed to execute call to Realms Service", e);
/*      */           return null;
/*      */         });
/*      */   }
/*      */   
/*      */   private void refreshListAndLayout() {
/*  484 */     this.realmSelectionList.refreshEntries(this);
/*  485 */     updateLayout();
/*  486 */     updateButtonStates();
/*      */   }
/*      */   
/*      */   private void pingRegions() {
/*  490 */     Thread pingThread = new Thread(() -> {
/*      */           List<RegionPingResult> regionPingResultList = Ping.pingAllRegions();
/*      */           RealmsClient client = RealmsClient.getOrCreate();
/*      */           PingResult pingResult = new PingResult(regionPingResultList, getOwnedNonExpiredRealmIds());
/*      */           try {
/*      */             client.sendPingResults(pingResult);
/*  500 */           } catch (Throwable t) {
/*      */             LOGGER.warn("Could not send ping result to Realms: ", t);
/*      */           } 
/*      */         }"Realms ping");
/*  504 */     pingThread.setDaemon(true);
/*  505 */     pingThread.start();
/*      */   }
/*      */   
/*      */   private List<Long> getOwnedNonExpiredRealmIds() {
/*  509 */     List<Long> ids = Lists.newArrayList();
/*  511 */     for (RealmsServer server : this.serverList) {
/*  512 */       if (isSelfOwnedNonExpiredServer(server))
/*  513 */         ids.add(Long.valueOf(server.id)); 
/*      */     } 
/*  517 */     return ids;
/*      */   }
/*      */   
/*      */   private void onRenew(RealmsServer server) {
/*  521 */     if (server != null) {
/*  522 */       String extensionUrl = CommonLinks.extendRealms(server.remoteSubscriptionId, this.minecraft.getUser().getProfileId(), server.expiredTrial);
/*  523 */       this.minecraft.gui.setScreen((Screen)new ConfirmLinkScreen(result -> {
/*      */               if (result) {
/*      */                 Util.getPlatform().openUri(extensionUrl);
/*      */               } else {
/*      */                 this.minecraft.gui.setScreen((Screen)this);
/*      */               } 
/*  523 */             }extensionUrl, true));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void configureClicked(RealmsServer selectedServer) {
/*  534 */     if (selectedServer != null && this.minecraft.isLocalPlayer(selectedServer.ownerUUID))
/*  535 */       this.minecraft.gui.setScreen((Screen)new RealmsConfigureWorldScreen(this, selectedServer.id)); 
/*      */   }
/*      */   
/*      */   private void leaveClicked(RealmsServer selectedServer) {
/*  540 */     if (selectedServer != null && !this.minecraft.isLocalPlayer(selectedServer.ownerUUID)) {
/*  541 */       MutableComponent mutableComponent = Component.translatable("mco.configure.world.leave.question.line1");
/*  542 */       this.minecraft.gui.setScreen((Screen)RealmsPopups.infoPopupScreen((Screen)this, (Component)mutableComponent, paramPopupScreen -> leaveServer(selectedServer)));
/*      */     } 
/*      */   }
/*      */   
/*      */   private RealmsServer getSelectedServer() {
/*  549 */     AbstractSelectionList.Entry entry = this.realmSelectionList.getSelected();
/*  549 */     if (entry instanceof ServerEntry) {
/*  549 */       ServerEntry serverEntry = (ServerEntry)entry;
/*  550 */       return serverEntry.getServer();
/*      */     } 
/*  552 */     return null;
/*      */   }
/*      */   
/*      */   private void leaveServer(final RealmsServer server) {
/*  556 */     (new Thread("Realms-leave-server") {
/*      */         public void run() {
/*      */           try {
/*  560 */             RealmsClient client = RealmsClient.getOrCreate();
/*  561 */             client.uninviteMyselfFrom(server.id);
/*  562 */             RealmsMainScreen.this.minecraft.execute(RealmsMainScreen::refreshServerList);
/*  563 */           } catch (RealmsServiceException e) {
/*  564 */             RealmsMainScreen.LOGGER.error("Couldn't configure world", (Throwable)e);
/*  565 */             RealmsMainScreen.this.minecraft.execute(() -> RealmsMainScreen.this.minecraft.gui.setScreen((Screen)new RealmsGenericErrorScreen(e, (Screen)RealmsMainScreen.this)));
/*      */           } 
/*      */         }
/*  568 */       }).start();
/*  570 */     this.minecraft.gui.setScreen((Screen)this);
/*      */   }
/*      */   
/*      */   private void dismissNotification(UUID uuid) {
/*  574 */     callRealmsClient(realmsClient -> {
/*      */           realmsClient.notificationsDismiss(List.of(uuid));
/*      */           return null;
/*      */         }ignored -> {
/*      */           this.notifications.removeIf(());
/*      */           refreshListAndLayout();
/*      */         });
/*      */   }
/*      */   
/*      */   public void resetScreen() {
/*  584 */     this.realmSelectionList.setSelected((Entry)null);
/*  585 */     refreshServerList();
/*      */   }
/*      */   
/*      */   public Component getNarrationMessage() {
/*  590 */     switch (this.activeLayoutState.ordinal()) {
/*      */       default:
/*  590 */         throw new MatchException(null, null);
/*      */       case 0:
/*      */       
/*      */       case 1:
/*      */       
/*      */       case 2:
/*      */         break;
/*      */     } 
/*  590 */     return 
/*      */       
/*  593 */       super.getNarrationMessage();
/*      */   }
/*      */   
/*      */   public void extractRenderState(GuiGraphicsExtractor graphics, int xm, int ym, float a) {
/*  599 */     super.extractRenderState(graphics, xm, ym, a);
/*  601 */     if (isSnapshot())
/*  602 */       graphics.text(this.font, "Minecraft " + SharedConstants.getCurrentVersion().name(), 2, this.height - 10, -1); 
/*  605 */     if (this.trialsAvailable && this.addRealmButton.active)
/*  606 */       AddRealmPopupScreen.extractDiamond(graphics, this.addRealmButton); 
/*  609 */     switch (RealmsClient.ENVIRONMENT) {
/*      */       case COMPATIBLE:
/*  610 */         extractEnvironment(graphics, "STAGE!", -256);
/*      */         break;
/*      */       case UNVERIFIABLE:
/*  611 */         extractEnvironment(graphics, "LOCAL!", -8388737);
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void openTrialAvailablePopup() {
/*  616 */     this.minecraft.gui.setScreen((Screen)new AddRealmPopupScreen((Screen)this, this.trialsAvailable));
/*      */   }
/*      */   
/*      */   public static void play(RealmsServer server, Screen cancelScreen) {
/*  620 */     play(server, cancelScreen, false);
/*      */   }
/*      */   
/*      */   public static void play(RealmsServer server, Screen cancelScreen, boolean skipCompatibility) {
/*  624 */     if (server != null) {
/*  625 */       if (!isSnapshot() || skipCompatibility || server.isMinigameActive()) {
/*  626 */         (Minecraft.getInstance()).gui.setScreen((Screen)new RealmsLongRunningMcoTaskScreen(cancelScreen, new LongRunningTask[] { (LongRunningTask)new GetServerDetailsTask(cancelScreen, server) }));
/*      */         return;
/*      */       } 
/*  629 */       switch (server.compatibility) {
/*      */         case COMPATIBLE:
/*  631 */           (Minecraft.getInstance()).gui.setScreen((Screen)new RealmsLongRunningMcoTaskScreen(cancelScreen, new LongRunningTask[] { (LongRunningTask)new GetServerDetailsTask(cancelScreen, server) }));
/*      */           break;
/*      */         case UNVERIFIABLE:
/*  632 */           confirmToPlay(server, cancelScreen, 
/*  633 */               (Component)Component.translatable("mco.compatibility.unverifiable.title").withColor(-171), 
/*  634 */               (Component)Component.translatable("mco.compatibility.unverifiable.message"), CommonComponents.GUI_CONTINUE);
/*      */           break;
/*      */         case NEEDS_DOWNGRADE:
/*  637 */           confirmToPlay(server, cancelScreen, 
/*  638 */               (Component)Component.translatable("selectWorld.backupQuestion.downgrade").withColor(-2142128), 
/*  639 */               (Component)Component.translatable("mco.compatibility.downgrade.description", new Object[] { Component.literal(server.activeVersion).withColor(-171), 
/*  641 */                   Component.literal(SharedConstants.getCurrentVersion().name()).withColor(-171) }), (Component)Component.translatable("mco.compatibility.downgrade"));
/*      */           break;
/*      */         case NEEDS_UPGRADE:
/*  644 */           upgradeRealmAndPlay(server, cancelScreen);
/*      */           break;
/*      */         case INCOMPATIBLE:
/*  646 */           (Minecraft.getInstance()).gui.setScreen((Screen)(new PopupScreen.Builder(cancelScreen, INCOMPATIBLE_POPUP_TITLE))
/*  647 */               .addMessage((Component)Component.translatable("mco.compatibility.incompatible.series.popup.message", new Object[] { Component.literal(server.activeVersion).withColor(-171), 
/*  649 */                     Component.literal(SharedConstants.getCurrentVersion().name()).withColor(-171) })).addButton(CommonComponents.GUI_BACK, PopupScreen::onClose)
/*  651 */               .build());
/*      */           break;
/*      */         case RELEASE_TYPE_INCOMPATIBLE:
/*  654 */           (Minecraft.getInstance()).gui.setScreen((Screen)(new PopupScreen.Builder(cancelScreen, INCOMPATIBLE_POPUP_TITLE))
/*  655 */               .addMessage(INCOMPATIBLE_RELEASE_TYPE_POPUP_MESSAGE)
/*  656 */               .addButton(CommonComponents.GUI_BACK, PopupScreen::onClose)
/*  657 */               .build());
/*      */           break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void confirmToPlay(RealmsServer server, Screen lastScreen, Component title, Component message, Component confirmButton) {
/*  664 */     (Minecraft.getInstance()).gui.setScreen((Screen)(new PopupScreen.Builder(lastScreen, title))
/*      */         
/*  666 */         .addMessage(message)
/*  667 */         .addButton(confirmButton, paramPopupScreen -> {
/*      */             (Minecraft.getInstance()).gui.setScreen((Screen)new RealmsLongRunningMcoTaskScreen(lastScreen, new LongRunningTask[] { (LongRunningTask)new GetServerDetailsTask(lastScreen, server) }));
/*      */             refreshServerList();
/*  671 */           }).addButton(CommonComponents.GUI_CANCEL, PopupScreen::onClose)
/*  672 */         .build());
/*      */   }
/*      */   
/*      */   private static void upgradeRealmAndPlay(RealmsServer server, Screen cancelScreen) {
/*  677 */     MutableComponent mutableComponent1 = Component.translatable("mco.compatibility.upgrade.title").withColor(-171);
/*  678 */     MutableComponent mutableComponent2 = Component.translatable("mco.compatibility.upgrade");
/*  679 */     MutableComponent mutableComponent3 = Component.literal(server.activeVersion).withColor(-171);
/*  680 */     MutableComponent mutableComponent4 = Component.literal(SharedConstants.getCurrentVersion().name()).withColor(-171);
/*  683 */     MutableComponent mutableComponent5 = isSelfOwnedServer(server) ? Component.translatable("mco.compatibility.upgrade.description", new Object[] { mutableComponent3, mutableComponent4 }) : Component.translatable("mco.compatibility.upgrade.friend.description", new Object[] { mutableComponent3, mutableComponent4 });
/*  684 */     confirmToPlay(server, cancelScreen, (Component)mutableComponent1, (Component)mutableComponent5, (Component)mutableComponent2);
/*      */   }
/*      */   
/*      */   private class RealmSelectionList extends ObjectSelectionList<Entry> {
/*      */     public RealmSelectionList() {
/*  689 */       super(Minecraft.getInstance(), RealmsMainScreen.this.width, RealmsMainScreen.this.height, 0, 36);
/*      */     }
/*      */     
/*      */     public void setSelected(RealmsMainScreen.Entry selected) {
/*  694 */       super.setSelected((AbstractSelectionList.Entry)selected);
/*  695 */       RealmsMainScreen.this.updateButtonStates();
/*      */     }
/*      */     
/*      */     public int getRowWidth() {
/*  700 */       return 300;
/*      */     }
/*      */     
/*      */     private void refreshEntries(RealmsMainScreen realmsMainScreen) {
/*  704 */       RealmsMainScreen.Entry previouslySelected = (RealmsMainScreen.Entry)getSelected();
/*  705 */       clearEntries();
/*  707 */       for (RealmsNotification notification : RealmsMainScreen.this.notifications) {
/*  708 */         if (notification instanceof RealmsNotification.VisitUrl) {
/*  708 */           RealmsNotification.VisitUrl visitUrl = (RealmsNotification.VisitUrl)notification;
/*  709 */           addEntriesForNotification(visitUrl, realmsMainScreen, previouslySelected);
/*  710 */           RealmsMainScreen.this.markNotificationsAsSeen(List.of(notification));
/*      */           break;
/*      */         } 
/*      */       } 
/*  716 */       refreshServerEntries(previouslySelected);
/*      */     }
/*      */     
/*      */     private void addEntriesForNotification(RealmsNotification.VisitUrl visitUrl, RealmsMainScreen realmsMainScreen, RealmsMainScreen.Entry previouslySelected) {
/*  720 */       Component message = visitUrl.getMessage();
/*  721 */       int messageHeight = RealmsMainScreen.this.font.wordWrapHeight((FormattedText)message, RealmsMainScreen.NotificationMessageEntry.textWidth(getRowWidth()));
/*  722 */       RealmsMainScreen.NotificationMessageEntry entry = new RealmsMainScreen.NotificationMessageEntry(RealmsMainScreen.this, realmsMainScreen, messageHeight, message, visitUrl);
/*  723 */       addEntry((AbstractSelectionList.Entry)entry, 38 + messageHeight);
/*  724 */       if (previouslySelected instanceof RealmsMainScreen.NotificationMessageEntry) {
/*  724 */         RealmsMainScreen.NotificationMessageEntry notificationMessageEntry = (RealmsMainScreen.NotificationMessageEntry)previouslySelected;
/*  724 */         if (notificationMessageEntry.getText().equals(message))
/*  725 */           setSelected(entry); 
/*      */       } 
/*      */     }
/*      */     
/*      */     private void refreshServerEntries(RealmsMainScreen.Entry previouslySelected) {
/*  730 */       for (RealmsServer eligibleForSnapshotServer : RealmsMainScreen.this.availableSnapshotServers)
/*  731 */         addEntry((AbstractSelectionList.Entry)new RealmsMainScreen.AvailableSnapshotEntry(eligibleForSnapshotServer)); 
/*  734 */       for (RealmsServer server : RealmsMainScreen.this.serverList) {
/*      */         RealmsMainScreen.Entry entry;
/*  736 */         if (RealmsMainScreen.isSnapshot() && !server.isSnapshotRealm()) {
/*  737 */           if (server.state == RealmsServer.State.UNINITIALIZED)
/*      */             continue; 
/*  740 */           entry = new RealmsMainScreen.ParentEntry(RealmsMainScreen.this, server);
/*      */         } else {
/*  742 */           entry = new RealmsMainScreen.ServerEntry(server);
/*      */         } 
/*  744 */         addEntry((AbstractSelectionList.Entry)entry);
/*  745 */         if (previouslySelected instanceof RealmsMainScreen.ServerEntry) {
/*  745 */           RealmsMainScreen.ServerEntry serverEntry = (RealmsMainScreen.ServerEntry)previouslySelected;
/*  745 */           if (serverEntry.serverData.id == server.id)
/*  746 */             setSelected(entry); 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static Component getVersionComponent(String version, boolean isCompatible) {
/*  753 */     return getVersionComponent(version, isCompatible ? -8355712 : -2142128);
/*      */   }
/*      */   
/*      */   public static Component getVersionComponent(String version, int color) {
/*  757 */     if (StringUtils.isBlank(version))
/*  758 */       return CommonComponents.EMPTY; 
/*  760 */     return (Component)Component.literal(version).withColor(color);
/*      */   }
/*      */   
/*      */   public static Component getGameModeComponent(int gameMode, boolean hardcore) {
/*  765 */     if (hardcore)
/*  766 */       return (Component)Component.translatable("gameMode.hardcore").withColor(-65536); 
/*  768 */     return GameType.byId(gameMode).getLongDisplayName();
/*      */   }
/*      */   
/*      */   private abstract class Entry extends ObjectSelectionList.Entry<Entry> {
/*      */     protected static final int STATUS_LIGHT_WIDTH = 10;
/*      */     
/*      */     private static final int STATUS_LIGHT_HEIGHT = 28;
/*      */     
/*      */     protected static final int PADDING_X = 7;
/*      */     
/*      */     protected static final int PADDING_Y = 2;
/*      */     
/*      */     protected void extractStatusLights(RealmsServer serverData, GuiGraphicsExtractor graphics, int rowRight, int rowTop, int mouseX, int mouseY) {
/*  778 */       int x = rowRight - 10 - 7;
/*  779 */       int y = rowTop + 2;
/*  780 */       if (serverData.expired) {
/*  781 */         extractRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.EXPIRED_SPRITE, () -> RealmsMainScreen.SERVER_EXPIRED_TOOLTIP);
/*  782 */       } else if (serverData.state == RealmsServer.State.CLOSED) {
/*  783 */         extractRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.CLOSED_SPRITE, () -> RealmsMainScreen.SERVER_CLOSED_TOOLTIP);
/*  784 */       } else if (RealmsMainScreen.isSelfOwnedServer(serverData) && serverData.daysLeft < 7) {
/*  785 */         extractRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.EXPIRES_SOON_SPRITE, () -> (serverData.daysLeft <= 0) ? RealmsMainScreen.SERVER_EXPIRES_SOON_TOOLTIP : ((serverData.daysLeft == 1) ? RealmsMainScreen.SERVER_EXPIRES_IN_DAY_TOOLTIP : Component.translatable("mco.selectServer.expires.days", new Object[] { Integer.valueOf(serverData.daysLeft) })));
/*  794 */       } else if (serverData.state == RealmsServer.State.OPEN) {
/*  795 */         extractRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.OPEN_SPRITE, () -> RealmsMainScreen.SERVER_OPEN_TOOLTIP);
/*      */       } 
/*      */     }
/*      */     
/*      */     private void extractRealmStatus(GuiGraphicsExtractor graphics, int x, int y, int xm, int ym, Identifier sprite, Supplier<Component> tooltip) {
/*  800 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, 10, 28);
/*  801 */       if (RealmsMainScreen.this.realmSelectionList.isMouseOver(xm, ym) && xm >= x && xm <= x + 10 && ym >= y && ym <= y + 28)
/*  802 */         graphics.setTooltipForNextFrame(tooltip.get(), xm, ym); 
/*      */     }
/*      */     
/*      */     protected void extractFirstLine(GuiGraphicsExtractor graphics, int rowTop, int rowLeft, int rowWidth, int serverNameColor, RealmsServer serverData) {
/*  807 */       int textX = textX(rowLeft);
/*  808 */       int firstLineY = firstLineY(rowTop);
/*  810 */       Component versionComponent = RealmsMainScreen.getVersionComponent(serverData.activeVersion, serverData.isCompatible());
/*  811 */       int versionTextX = versionTextX(rowLeft, rowWidth, versionComponent);
/*  812 */       extractClampedString(graphics, serverData.getName(), textX, firstLineY, versionTextX, serverNameColor);
/*  813 */       if (versionComponent != CommonComponents.EMPTY && !serverData.isMinigameActive())
/*  814 */         graphics.text(RealmsMainScreen.this.font, versionComponent, versionTextX, firstLineY, -8355712); 
/*      */     }
/*      */     
/*      */     protected void extractSecondLine(GuiGraphicsExtractor graphics, int rowTop, int rowLeft, int rowWidth, RealmsServer serverData) {
/*  819 */       int textX = textX(rowLeft);
/*  820 */       int firstLineY = firstLineY(rowTop);
/*  821 */       int secondLineY = secondLineY(firstLineY);
/*  823 */       String minigameName = serverData.getMinigameName();
/*  824 */       boolean minigameActive = serverData.isMinigameActive();
/*  825 */       if (minigameActive && minigameName != null) {
/*  826 */         MutableComponent mutableComponent = Component.literal(minigameName).withStyle(ChatFormatting.GRAY);
/*  827 */         graphics.text(RealmsMainScreen.this.font, (Component)Component.translatable("mco.selectServer.minigameName", new Object[] { mutableComponent }).withColor(-171), textX, secondLineY, -1);
/*      */       } else {
/*  829 */         int maxX = extractGameMode(serverData, graphics, rowLeft, rowWidth, firstLineY);
/*  830 */         extractClampedString(graphics, serverData.getDescription(), textX, secondLineY(firstLineY), maxX, -8355712);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void extractThirdLine(GuiGraphicsExtractor graphics, int rowTop, int rowLeft, RealmsServer server) {
/*  835 */       int textX = textX(rowLeft);
/*  836 */       int firstLineY = firstLineY(rowTop);
/*  837 */       int thirdLineY = thirdLineY(firstLineY);
/*  839 */       if (!RealmsMainScreen.isSelfOwnedServer(server)) {
/*  840 */         graphics.text(RealmsMainScreen.this.font, server.owner, textX, thirdLineY(firstLineY), -8355712);
/*  841 */       } else if (server.expired) {
/*  842 */         Component expirationText = server.expiredTrial ? RealmsMainScreen.TRIAL_EXPIRED_TEXT : RealmsMainScreen.SUBSCRIPTION_EXPIRED_TEXT;
/*  843 */         graphics.text(RealmsMainScreen.this.font, expirationText, textX, thirdLineY, -2142128);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void extractClampedString(GuiGraphicsExtractor graphics, String string, int x, int y, int maxX, int color) {
/*  848 */       if (string == null)
/*      */         return; 
/*  851 */       int availableSpace = maxX - x;
/*  852 */       if (RealmsMainScreen.this.font.width(string) > availableSpace) {
/*  853 */         String clampedName = RealmsMainScreen.this.font.plainSubstrByWidth(string, availableSpace - RealmsMainScreen.this.font.width("... "));
/*  854 */         graphics.text(RealmsMainScreen.this.font, clampedName + "...", x, y, color);
/*      */       } else {
/*  856 */         graphics.text(RealmsMainScreen.this.font, string, x, y, color);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected int versionTextX(int rowLeft, int rowWidth, Component versionComponent) {
/*  861 */       return rowLeft + rowWidth - RealmsMainScreen.this.font.width((FormattedText)versionComponent) - 20;
/*      */     }
/*      */     
/*      */     protected int gameModeTextX(int rowLeft, int rowWidth, Component versionComponent) {
/*  865 */       return rowLeft + rowWidth - RealmsMainScreen.this.font.width((FormattedText)versionComponent) - 20;
/*      */     }
/*      */     
/*      */     protected int extractGameMode(RealmsServer server, GuiGraphicsExtractor graphics, int rowLeft, int rowWidth, int firstLineY) {
/*  869 */       boolean hardcore = server.isHardcore;
/*  870 */       int gameMode = server.gameMode;
/*  871 */       int x = rowLeft;
/*  872 */       if (GameType.isValidId(gameMode)) {
/*  873 */         Component gameModeComponent = RealmsMainScreen.getGameModeComponent(gameMode, hardcore);
/*  874 */         x = gameModeTextX(rowLeft, rowWidth, gameModeComponent);
/*  875 */         graphics.text(RealmsMainScreen.this.font, gameModeComponent, x, secondLineY(firstLineY), -8355712);
/*      */       } 
/*  877 */       if (hardcore) {
/*  878 */         x -= 10;
/*  879 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsMainScreen.HARDCORE_MODE_SPRITE, x, secondLineY(firstLineY), 8, 8);
/*      */       } 
/*  881 */       return x;
/*      */     }
/*      */     
/*      */     protected int firstLineY(int rowTop) {
/*  885 */       return rowTop + 1;
/*      */     }
/*      */     
/*      */     protected int lineHeight() {
/*  889 */       Objects.requireNonNull(RealmsMainScreen.this.font);
/*  889 */       return 2 + 9;
/*      */     }
/*      */     
/*      */     protected int textX(int rowLeft) {
/*  893 */       return rowLeft + 36 + 2;
/*      */     }
/*      */     
/*      */     protected int secondLineY(int firstLineY) {
/*  897 */       return firstLineY + lineHeight();
/*      */     }
/*      */     
/*      */     protected int thirdLineY(int firstLineY) {
/*  901 */       return firstLineY + lineHeight() * 2;
/*      */     }
/*      */   }
/*      */   
/*      */   private class NotificationMessageEntry extends Entry {
/*      */     private static final int SIDE_MARGINS = 40;
/*      */     
/*      */     public static final int PADDING = 7;
/*      */     
/*      */     public static final int HEIGHT_WITHOUT_TEXT = 38;
/*      */     
/*      */     private final Component text;
/*      */     
/*  911 */     private final List<AbstractWidget> children = new ArrayList<>();
/*      */     
/*      */     private final RealmsMainScreen.CrossButton dismissButton;
/*      */     
/*      */     private final MultiLineTextWidget textWidget;
/*      */     
/*      */     private final GridLayout gridLayout;
/*      */     
/*      */     private final FrameLayout textFrame;
/*      */     
/*      */     private final Button button;
/*      */     
/*  917 */     private int lastEntryWidth = -1;
/*      */     
/*      */     public NotificationMessageEntry(RealmsMainScreen this$0, RealmsMainScreen realmsMainScreen, int messageHeight, Component text, RealmsNotification.VisitUrl notification) {
/*  919 */       super(this$0);
/*  920 */       this.text = text;
/*  921 */       this.gridLayout = new GridLayout();
/*  922 */       this.gridLayout.addChild((LayoutElement)ImageWidget.sprite(20, 20, RealmsMainScreen.INFO_SPRITE), 0, 0, this.gridLayout.newCellSettings().padding(7, 7, 0, 0));
/*  923 */       this.gridLayout.addChild((LayoutElement)SpacerElement.width(40), 0, 0);
/*  924 */       this.textFrame = (FrameLayout)this.gridLayout.addChild((LayoutElement)new FrameLayout(0, messageHeight), 0, 1, this.gridLayout.newCellSettings().paddingTop(7));
/*  925 */       this.textWidget = (MultiLineTextWidget)this.textFrame.addChild((LayoutElement)(new MultiLineTextWidget(text, this$0.font)).setCentered(true), this.textFrame.newChildLayoutSettings().alignHorizontallyCenter().alignVerticallyTop());
/*  926 */       this.gridLayout.addChild((LayoutElement)SpacerElement.width(40), 0, 2);
/*  927 */       if (notification.dismissable()) {
/*  928 */         this.dismissButton = (RealmsMainScreen.CrossButton)this.gridLayout.addChild((LayoutElement)new RealmsMainScreen.CrossButton(b -> this$0.dismissNotification(notification.uuid()), (Component)Component.translatable("mco.notification.dismiss")), 0, 2, this.gridLayout.newCellSettings().alignHorizontallyRight().padding(0, 7, 7, 0));
/*      */       } else {
/*  930 */         this.dismissButton = null;
/*      */       } 
/*  932 */       this.button = (Button)this.gridLayout.addChild((LayoutElement)notification.buildOpenLinkButton((Screen)realmsMainScreen), 1, 1, this.gridLayout.newCellSettings().alignHorizontallyCenter().padding(4));
/*  933 */       NotificationMessageEntry notificationMessageEntry = this;
/*  933 */       this.button.setOverrideRenderHighlightedSprite(() -> Boolean.valueOf(rec$.isFocused()));
/*  934 */       Objects.requireNonNull(this.children);
/*  934 */       this.gridLayout.visitWidgets(this.children::add);
/*      */     }
/*      */     
/*      */     public boolean keyPressed(KeyEvent event) {
/*  939 */       if (this.button.keyPressed(event))
/*  940 */         return true; 
/*  942 */       if (this.dismissButton != null && this.dismissButton.keyPressed(event))
/*  943 */         return true; 
/*  945 */       return super.keyPressed(event);
/*      */     }
/*      */     
/*      */     private void updateEntryWidth() {
/*  949 */       int entryWidth = getWidth();
/*  950 */       if (this.lastEntryWidth != entryWidth) {
/*  951 */         refreshLayout(entryWidth);
/*  952 */         this.lastEntryWidth = entryWidth;
/*      */       } 
/*      */     }
/*      */     
/*      */     private void refreshLayout(int entryWidth) {
/*  957 */       int width = textWidth(entryWidth);
/*  958 */       this.textFrame.setMinWidth(width);
/*  959 */       this.textWidget.setMaxWidth(width);
/*  960 */       this.gridLayout.arrangeElements();
/*      */     }
/*      */     
/*      */     public static int textWidth(int rowWidth) {
/*  964 */       return rowWidth - 80;
/*      */     }
/*      */     
/*      */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*  969 */       this.gridLayout.setPosition(getContentX(), getContentY());
/*  970 */       updateEntryWidth();
/*  971 */       this.children.forEach(child -> child.extractRenderState(graphics, mouseX, mouseY, a));
/*      */     }
/*      */     
/*      */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/*  976 */       if (this.dismissButton != null && this.dismissButton.mouseClicked(event, doubleClick))
/*  977 */         return true; 
/*  979 */       if (this.button.mouseClicked(event, doubleClick))
/*  980 */         return true; 
/*  983 */       return super.mouseClicked(event, doubleClick);
/*      */     }
/*      */     
/*      */     public Component getText() {
/*  987 */       return this.text;
/*      */     }
/*      */     
/*      */     public Component getNarration() {
/*  992 */       return getText();
/*      */     }
/*      */   }
/*      */   
/*      */   private class AvailableSnapshotEntry extends Entry {
/*  997 */     private static final Component START_SNAPSHOT_REALM = (Component)Component.translatable("mco.snapshot.start");
/*      */     
/*      */     private static final int TEXT_PADDING = 5;
/*      */     
/* 1000 */     private final WidgetTooltipHolder tooltip = new WidgetTooltipHolder();
/*      */     
/*      */     private final RealmsServer parent;
/*      */     
/*      */     public AvailableSnapshotEntry(RealmsServer parent) {
/* 1004 */       this.parent = parent;
/* 1005 */       this.tooltip.set(Tooltip.create((Component)Component.translatable("mco.snapshot.tooltip")));
/*      */     }
/*      */     
/*      */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 1010 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsMainScreen.NEW_REALM_SPRITE, getContentX() - 5, getContentYMiddle() - 10, 40, 20);
/* 1012 */       Objects.requireNonNull(RealmsMainScreen.this.font);
/* 1012 */       int textYPos = getContentYMiddle() - 9 / 2;
/* 1013 */       graphics.text(RealmsMainScreen.this.font, START_SNAPSHOT_REALM, getContentX() + 40 - 2, textYPos - 5, -8388737);
/* 1014 */       graphics.text(RealmsMainScreen.this.font, (Component)Component.translatable("mco.snapshot.description", new Object[] { Objects.requireNonNullElse(this.parent.name, "unknown server") }), getContentX() + 40 - 2, textYPos + 5, -8355712);
/* 1015 */       this.tooltip.refreshTooltipForNextRenderPass(graphics, mouseX, mouseY, hovered, (isFocused() && RealmsMainScreen.this.minecraft.gui.screen() == RealmsMainScreen.this), new ScreenRectangle(getContentX(), getContentY(), getContentWidth(), getContentHeight()));
/*      */     }
/*      */     
/*      */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 1020 */       addSnapshotRealm();
/* 1021 */       return true;
/*      */     }
/*      */     
/*      */     public boolean keyPressed(KeyEvent event) {
/* 1026 */       if (event.isSelection()) {
/* 1027 */         addSnapshotRealm();
/* 1028 */         return false;
/*      */       } 
/* 1030 */       return super.keyPressed(event);
/*      */     }
/*      */     
/*      */     private void addSnapshotRealm() {
/* 1034 */       RealmsMainScreen.this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/* 1035 */       RealmsMainScreen.this.minecraft.gui.setScreen((Screen)(new PopupScreen.Builder((Screen)RealmsMainScreen.this, (Component)Component.translatable("mco.snapshot.createSnapshotPopup.title")))
/* 1036 */           .addMessage((Component)Component.translatable("mco.snapshot.createSnapshotPopup.text"))
/* 1037 */           .addButton((Component)Component.translatable("mco.selectServer.create"), param1PopupScreen -> RealmsMainScreen.this.minecraft.gui.setScreen((Screen)new RealmsCreateRealmScreen(RealmsMainScreen.this, this.parent, true)))
/*      */           
/* 1039 */           .addButton(CommonComponents.GUI_CANCEL, PopupScreen::onClose)
/* 1040 */           .build());
/*      */     }
/*      */     
/*      */     public Component getNarration() {
/* 1045 */       return (Component)Component.translatable("gui.narrate.button", new Object[] { CommonComponents.joinForNarration(new Component[] { START_SNAPSHOT_REALM, 
/* 1046 */                 (Component)Component.translatable("mco.snapshot.description", new Object[] { Objects.requireNonNullElse(this.parent.name, "unknown server") }) }) });
/*      */     }
/*      */   }
/*      */   
/*      */   private class ParentEntry extends Entry {
/*      */     private final RealmsServer server;
/*      */     
/* 1053 */     private final WidgetTooltipHolder tooltip = new WidgetTooltipHolder();
/*      */     
/*      */     public ParentEntry(RealmsMainScreen this$0, RealmsServer server) {
/* 1055 */       super(this$0);
/* 1056 */       this.server = server;
/* 1057 */       if (!server.expired)
/* 1058 */         this.tooltip.set(Tooltip.create((Component)Component.translatable("mco.snapshot.parent.tooltip"))); 
/*      */     }
/*      */     
/*      */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 1064 */       extractStatusLights(this.server, graphics, getContentRight(), getContentY(), mouseX, mouseY);
/* 1065 */       RealmsUtil.extractPlayerFace(graphics, getContentX(), getContentY(), 32, this.server.ownerUUID);
/* 1066 */       extractFirstLine(graphics, getContentY(), getContentX(), getContentWidth(), -8355712, this.server);
/* 1067 */       extractSecondLine(graphics, getContentY(), getContentX(), getContentWidth(), this.server);
/* 1068 */       extractThirdLine(graphics, getContentY(), getContentX(), this.server);
/* 1069 */       this.tooltip.refreshTooltipForNextRenderPass(graphics, mouseX, mouseY, hovered, isFocused(), new ScreenRectangle(getContentX(), getContentY(), getContentWidth(), getContentHeight()));
/*      */     }
/*      */     
/*      */     public Component getNarration() {
/* 1074 */       return (Component)Component.literal(Objects.<String>requireNonNullElse(this.server.name, "unknown server"));
/*      */     }
/*      */   }
/*      */   
/*      */   private class ServerEntry extends Entry {
/* 1079 */     private static final Component ONLINE_PLAYERS_TOOLTIP_HEADER = (Component)Component.translatable("mco.onlinePlayers");
/*      */     
/*      */     private static final int PLAYERS_ONLINE_SPRITE_SIZE = 9;
/*      */     
/*      */     private static final int PLAYERS_ONLINE_SPRITE_SEPARATION = 3;
/*      */     
/*      */     private static final int SKIN_HEAD_LARGE_WIDTH = 36;
/*      */     
/*      */     private final RealmsServer serverData;
/*      */     
/* 1086 */     private final WidgetTooltipHolder tooltip = new WidgetTooltipHolder();
/*      */     
/*      */     public ServerEntry(RealmsServer serverData) {
/* 1089 */       this.serverData = serverData;
/* 1090 */       boolean selfOwnedServer = RealmsMainScreen.isSelfOwnedServer(serverData);
/* 1091 */       if (RealmsMainScreen.isSnapshot() && selfOwnedServer && serverData.isSnapshotRealm()) {
/* 1092 */         this.tooltip.set(Tooltip.create((Component)Component.translatable("mco.snapshot.paired", new Object[] { serverData.parentWorldName })));
/* 1093 */       } else if (!selfOwnedServer && serverData.needsDowngrade()) {
/* 1094 */         this.tooltip.set(Tooltip.create((Component)Component.translatable("mco.snapshot.friendsRealm.downgrade", new Object[] { serverData.activeVersion })));
/*      */       } 
/*      */     }
/*      */     
/*      */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 1100 */       if (this.serverData.state == RealmsServer.State.UNINITIALIZED) {
/* 1101 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsMainScreen.NEW_REALM_SPRITE, getContentX() - 5, getContentYMiddle() - 10, 40, 20);
/* 1103 */         Objects.requireNonNull(RealmsMainScreen.this.font);
/* 1103 */         int textYPos = getContentYMiddle() - 9 / 2;
/* 1104 */         graphics.text(RealmsMainScreen.this.font, RealmsMainScreen.SERVER_UNITIALIZED_TEXT, getContentX() + 40 - 2, textYPos, -8388737);
/*      */         return;
/*      */       } 
/* 1109 */       RealmsUtil.extractPlayerFace(graphics, getContentX(), getContentY(), 32, this.serverData.ownerUUID);
/* 1111 */       extractFirstLine(graphics, getContentY(), getContentX(), getContentWidth(), -1, this.serverData);
/* 1112 */       extractSecondLine(graphics, getContentY(), getContentX(), getContentWidth(), this.serverData);
/* 1113 */       extractThirdLine(graphics, getContentY(), getContentX(), this.serverData);
/* 1115 */       extractStatusLights(this.serverData, graphics, getContentRight(), getContentY(), mouseX, mouseY);
/* 1116 */       boolean hasTooltip = extractOnlinePlayers(graphics, getContentY(), getContentX(), getContentWidth(), getContentHeight(), mouseX, mouseY, a);
/* 1117 */       if (!hasTooltip)
/* 1118 */         this.tooltip.refreshTooltipForNextRenderPass(graphics, mouseX, mouseY, hovered, isFocused(), new ScreenRectangle(getContentX(), getContentY(), getContentWidth(), getContentHeight())); 
/*      */     }
/*      */     
/*      */     private boolean extractOnlinePlayers(GuiGraphicsExtractor graphics, int rowTop, int rowLeft, int rowWidth, int rowHeight, int mouseX, int mouseY, float a) {
/* 1123 */       List<ResolvableProfile> profileResults = RealmsMainScreen.this.onlinePlayersPerRealm.getProfileResultsFor(this.serverData.id);
/* 1124 */       int playerCount = profileResults.size();
/* 1125 */       if (playerCount > 0) {
/*      */         List<PlayerSkinRenderCache.RenderInfo> tooltipEntries;
/* 1126 */         int playersOnlineXEnd = rowLeft + rowWidth - 21;
/* 1127 */         int playersOnlineY = rowTop + rowHeight - 9 - 2;
/* 1128 */         int playerOnlineWidth = 9 * playerCount + 3 * (playerCount - 1);
/* 1129 */         int playersOnlineXStart = playersOnlineXEnd - playerOnlineWidth;
/* 1131 */         if (mouseX >= playersOnlineXStart && mouseX <= playersOnlineXEnd && mouseY >= playersOnlineY && mouseY <= playersOnlineY + 9) {
/* 1132 */           tooltipEntries = new ArrayList<>(playerCount);
/*      */         } else {
/* 1134 */           tooltipEntries = null;
/*      */         } 
/* 1137 */         PlayerSkinRenderCache skinCache = RealmsMainScreen.this.minecraft.playerSkinRenderCache();
/* 1138 */         for (int i = 0; i < profileResults.size(); i++) {
/* 1139 */           ResolvableProfile profile = profileResults.get(i);
/* 1140 */           PlayerSkinRenderCache.RenderInfo profileRenderInfo = skinCache.getOrDefault(profile);
/* 1141 */           int xPos = playersOnlineXStart + 12 * i;
/* 1142 */           PlayerFaceExtractor.extractRenderState(graphics, profileRenderInfo.playerSkin(), xPos, playersOnlineY, 9);
/* 1144 */           if (tooltipEntries != null)
/* 1145 */             tooltipEntries.add(profileRenderInfo); 
/*      */         } 
/* 1149 */         if (tooltipEntries != null) {
/* 1150 */           graphics.setTooltipForNextFrame(RealmsMainScreen.this.font, List.of(ONLINE_PLAYERS_TOOLTIP_HEADER), Optional.of(new ClientActivePlayersTooltip.ActivePlayersTooltip(tooltipEntries)), mouseX, mouseY);
/* 1151 */           return true;
/*      */         } 
/*      */       } 
/* 1154 */       return false;
/*      */     }
/*      */     
/*      */     private void playRealm() {
/* 1158 */       RealmsMainScreen.this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/* 1159 */       RealmsMainScreen.play(this.serverData, (Screen)RealmsMainScreen.this);
/*      */     }
/*      */     
/*      */     private void createUnitializedRealm() {
/* 1163 */       RealmsMainScreen.this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/* 1164 */       RealmsCreateRealmScreen createScreen = new RealmsCreateRealmScreen(RealmsMainScreen.this, this.serverData, this.serverData.isSnapshotRealm());
/* 1165 */       RealmsMainScreen.this.minecraft.gui.setScreen((Screen)createScreen);
/*      */     }
/*      */     
/*      */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 1170 */       if (this.serverData.state == RealmsServer.State.UNINITIALIZED) {
/* 1171 */         createUnitializedRealm();
/* 1172 */       } else if (this.serverData.shouldPlayButtonBeActive() && 
/* 1173 */         doubleClick && isFocused()) {
/* 1174 */         playRealm();
/*      */       } 
/* 1177 */       return true;
/*      */     }
/*      */     
/*      */     public boolean keyPressed(KeyEvent event) {
/* 1182 */       if (event.isSelection()) {
/* 1183 */         if (this.serverData.state == RealmsServer.State.UNINITIALIZED) {
/* 1184 */           createUnitializedRealm();
/* 1185 */           return true;
/*      */         } 
/* 1186 */         if (this.serverData.shouldPlayButtonBeActive()) {
/* 1187 */           playRealm();
/* 1188 */           return true;
/*      */         } 
/*      */       } 
/* 1191 */       return super.keyPressed(event);
/*      */     }
/*      */     
/*      */     public Component getNarration() {
/* 1196 */       if (this.serverData.state == RealmsServer.State.UNINITIALIZED)
/* 1197 */         return RealmsMainScreen.UNITIALIZED_WORLD_NARRATION; 
/* 1199 */       return (Component)Component.translatable("narrator.select", new Object[] { Objects.requireNonNullElse(this.serverData.name, "unknown server") });
/*      */     }
/*      */     
/*      */     public RealmsServer getServer() {
/* 1204 */       return this.serverData;
/*      */     }
/*      */   }
/*      */   
/*      */   private static boolean isSelfOwnedServer(RealmsServer serverData) {
/* 1209 */     return Minecraft.getInstance().isLocalPlayer(serverData.ownerUUID);
/*      */   }
/*      */   
/*      */   private boolean isSelfOwnedNonExpiredServer(RealmsServer serverData) {
/* 1213 */     return (isSelfOwnedServer(serverData) && !serverData.expired);
/*      */   }
/*      */   
/*      */   private void extractEnvironment(GuiGraphicsExtractor graphics, String text, int color) {
/* 1217 */     graphics.pose().pushMatrix();
/* 1218 */     graphics.pose().translate((this.width / 2 - 25), 20.0F);
/* 1219 */     graphics.pose().rotate(-0.34906584F);
/* 1220 */     graphics.pose().scale(1.5F, 1.5F);
/* 1222 */     graphics.text(this.font, text, 0, 0, color);
/* 1224 */     graphics.pose().popMatrix();
/*      */   }
/*      */   
/*      */   private static class NotificationButton extends SpriteIconButton.CenteredIcon {
/* 1228 */     private static final Identifier[] NOTIFICATION_ICONS = new Identifier[] { Identifier.withDefaultNamespace("notification/1"), 
/* 1230 */         Identifier.withDefaultNamespace("notification/2"), 
/* 1231 */         Identifier.withDefaultNamespace("notification/3"), 
/* 1232 */         Identifier.withDefaultNamespace("notification/4"), 
/* 1233 */         Identifier.withDefaultNamespace("notification/5"), 
/* 1234 */         Identifier.withDefaultNamespace("notification/more") };
/*      */     
/*      */     private static final int UNKNOWN_COUNT = 2147483647;
/*      */     
/*      */     private static final int SIZE = 20;
/*      */     
/*      */     private static final int SPRITE_SIZE = 14;
/*      */     
/*      */     private int notificationCount;
/*      */     
/*      */     public NotificationButton(Component title, Identifier texture, Button.OnPress onPress, Component tooltip) {
/* 1245 */       super(20, 20, title, 14, 14, 0, 0, new WidgetSprites(texture), onPress, tooltip, null, false);
/*      */     }
/*      */     
/*      */     private int notificationCount() {
/* 1249 */       return this.notificationCount;
/*      */     }
/*      */     
/*      */     public void setNotificationCount(int notificationCount) {
/* 1253 */       this.notificationCount = notificationCount;
/*      */     }
/*      */     
/*      */     public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 1258 */       super.extractContents(graphics, mouseX, mouseY, a);
/* 1259 */       if (isActive() && this.notificationCount != 0)
/* 1260 */         extractNotificationCounter(graphics); 
/*      */     }
/*      */     
/*      */     private void extractNotificationCounter(GuiGraphicsExtractor graphics) {
/* 1265 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, NOTIFICATION_ICONS[Math.min(this.notificationCount, 6) - 1], getX() + getWidth() - 5, getY() - 3, 8, 8);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class CrossButton extends ImageButton {
/* 1270 */     private static final WidgetSprites SPRITES = new WidgetSprites(
/* 1271 */         Identifier.withDefaultNamespace("widget/cross_button"), 
/* 1272 */         Identifier.withDefaultNamespace("widget/cross_button_highlighted"));
/*      */     
/*      */     protected CrossButton(Button.OnPress onPress, Component tooltip) {
/* 1276 */       super(0, 0, 14, 14, SPRITES, onPress);
/* 1277 */       setTooltip(Tooltip.create(tooltip));
/*      */     }
/*      */   }
/*      */   
/*      */   private enum LayoutState {
/* 1282 */     LOADING, NO_REALMS, LIST;
/*      */   }
/*      */   
/*      */   private static interface RealmsCall<T> {
/*      */     T request(RealmsClient param1RealmsClient) throws RealmsServiceException;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\RealmsMainScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */