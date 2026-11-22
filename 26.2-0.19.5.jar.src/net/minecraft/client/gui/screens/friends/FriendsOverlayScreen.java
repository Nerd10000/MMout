/*     */ package net.minecraft.client.gui.screens.friends;
/*     */ 
/*     */ import com.mojang.authlib.yggdrasil.FriendsService;
/*     */ import com.mojang.authlib.yggdrasil.response.PresenceResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.PresenceStatusDto;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.LoadingDotsWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.components.tabs.Tab;
/*     */ import net.minecraft.client.gui.components.tabs.TabManager;
/*     */ import net.minecraft.client.gui.components.tabs.TabNavigationBar;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.social.PlayerSocialManager;
/*     */ import net.minecraft.client.gui.screens.social.RemoteFriendListUpdateHandler;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FriendsOverlayScreen extends Screen {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  35 */   private static final Component TITLE = (Component)Component.translatable("gui.friends.open");
/*     */   
/*  36 */   private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("friends/background");
/*     */   
/*  37 */   private static final Component LOADING_FRIENDS = (Component)Component.translatable("gui.friends.loading_friends");
/*     */   
/*  38 */   private static final Component LOADING_REQUESTS = (Component)Component.translatable("gui.friends.loading_requests");
/*     */   
/*  39 */   private static final Component ERROR_UPGRADE_NEEDED = (Component)Component.translatable("gui.friends.error.upgrade_needed");
/*     */   
/*  40 */   private static final Component ERROR_CONNECTION_ISSUE = (Component)Component.translatable("gui.friends.error.connection_issue");
/*     */   
/*  41 */   private static final Component ERROR_TEMPORARY_UNAVAILABLE = (Component)Component.translatable("gui.friends.error.temporary_unavailable");
/*     */   
/*  42 */   private static final Component ERROR_USER_MAY_LACK_ACTIVE_PROFILE = (Component)Component.translatable("gui.friends.error.user_may_lack_active_profile");
/*     */   
/*  43 */   private static final Component ERROR_UNAUTHORIZED = (Component)Component.translatable("gui.friends.error.unauthorized");
/*     */   
/*  44 */   private static final Component ERROR_GENERIC = (Component)Component.translatable("gui.friends.error.generic");
/*     */   
/*  45 */   private static final Component ERROR_TOAST_GENERIC = (Component)Component.translatable("gui.friends.toast.generic_error");
/*     */   
/*     */   private static final int BG_BORDER_WIDTH = 8;
/*     */   
/*     */   private static final int OVERLAY_WIDTH = 220;
/*     */   
/*     */   public static final int TAB_BUTTON_WIDTH = 110;
/*     */   
/*     */   private static final int TAB_BUTTON_HEIGHT = 20;
/*     */   
/*     */   private final Screen backgroundScreen;
/*     */   
/*     */   private FriendsTab friendsTab;
/*     */   
/*     */   private PendingTab pendingTab;
/*     */   
/*     */   private TabNavigationBar tabNavigationBar;
/*     */   
/*     */   private LinearLayout layout;
/*     */   
/*     */   private LinearLayout contentLayout;
/*     */   
/*     */   private FriendsOverlayTabButton pendingTabButton;
/*     */   
/*     */   private final TabManager tabManager;
/*     */   
/*  60 */   private final Runnable friendListUpdateListener = this::onFriendListUpdate;
/*     */   
/*  61 */   private final Set<UUID> pendingFriendRemovals = new HashSet<>();
/*     */   
/*     */   public FriendsOverlayScreen(Screen backgroundScreen) {
/*  64 */     super(TITLE);
/*  65 */     this.backgroundScreen = backgroundScreen;
/*  66 */     FriendsOverlayScreen friendsOverlayScreen = this;
/*  66 */     friendsOverlayScreen = this;
/*  66 */     this.tabManager = new TabManager(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0), x$0 -> rec$.removeWidget((GuiEventListener)x$0), this::selectTab, this::deselectTab);
/*     */   }
/*     */   
/*     */   public void added() {
/*  71 */     super.added();
/*  72 */     if (this.backgroundScreen != null)
/*  73 */       this.backgroundScreen.clearFocus(); 
/*  75 */     this.minecraft.getPlayerSocialManager().addFriendListUpdateListener(this.friendListUpdateListener);
/*     */   }
/*     */   
/*     */   public void removed() {
/*  80 */     this.minecraft.getPlayerSocialManager().removeFriendListUpdateListener(this.friendListUpdateListener);
/*  81 */     super.removed();
/*     */   }
/*     */   
/*     */   private void onFriendListUpdate() {
/*  85 */     if (this.minecraft.gui.screen() != this || !this.minecraft.getPlayerSocialManager().isFriendListEnabled())
/*     */       return; 
/*  88 */     refreshLists();
/*     */   }
/*     */   
/*     */   protected void init() {
/*  93 */     if (this.backgroundScreen != null)
/*  94 */       this.backgroundScreen.init(this.width, this.height); 
/*  97 */     this.layout = LinearLayout.vertical();
/*  99 */     int scrollableMaxHeight = this.height - 80;
/* 100 */     this.friendsTab = new FriendsTab(this.minecraft, new LoadingDotsWidget(this.font, LOADING_FRIENDS), this, 220, scrollableMaxHeight);
/* 101 */     this.pendingTab = new PendingTab(this.minecraft, new LoadingDotsWidget(this.font, LOADING_REQUESTS), this, 220, scrollableMaxHeight);
/* 102 */     this.pendingTabButton = new FriendsOverlayTabButton(this.tabManager, this.pendingTab, 110, 20);
/* 103 */     this
/*     */       
/* 106 */       .tabNavigationBar = TabNavigationBar.builder(this.tabManager, 0, 0, 220, 20).addTab(new FriendsOverlayTabButton(this.tabManager, this.friendsTab, 110, 20), this.friendsTab).addTab(this.pendingTabButton, this.pendingTab).build();
/* 107 */     addRenderableWidget((GuiEventListener)this.tabNavigationBar);
/* 109 */     this.contentLayout = (LinearLayout)this.layout.addChild((LayoutElement)LinearLayout.vertical());
/* 110 */     this.tabManager.setCurrentTab(this.friendsTab, false, false);
/* 111 */     FriendsOverlayScreen friendsOverlayScreen = this;
/* 111 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/* 112 */     repositionElements();
/* 113 */     refreshLists();
/*     */   }
/*     */   
/*     */   public int getOverlayWidth() {
/* 117 */     return 220;
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/* 122 */     if (this.backgroundScreen != null)
/* 123 */       this.backgroundScreen.resize(this.width, this.height); 
/* 125 */     this.friendsTab.setHeight(this.height - 80);
/* 126 */     this.pendingTab.setHeight(this.height - 80);
/* 127 */     this.layout.arrangeElements();
/* 128 */     FrameLayout.alignInRectangle((LayoutElement)this.layout, getRectangle(), 0.5F, 0.5F);
/* 129 */     this.tabNavigationBar.setPosition(this.layout.getX(), this.layout.getY() - 20 - 7);
/* 130 */     this.tabNavigationBar.arrangeElements(this.width);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 135 */     super.tick();
/* 136 */     this.minecraft.getPlayerSocialManager().getPresenceHandler().tryUpdatePresence();
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 141 */     if (this.backgroundScreen != null) {
/* 142 */       this.backgroundScreen.extractBackground(graphics, mouseX, mouseY, a);
/* 143 */       graphics.nextStratum();
/* 144 */       this.backgroundScreen.extractRenderState(graphics, -1, -1, a);
/* 145 */       graphics.nextStratum();
/* 146 */       extractBlurredBackground(graphics);
/*     */     } else {
/* 148 */       super.extractBackground(graphics, mouseX, mouseY, a);
/*     */     } 
/* 151 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, this.layout
/*     */         
/* 153 */         .getX() - 8, this.layout
/* 154 */         .getY() - 8, this.layout
/* 155 */         .getWidth() + 16, this.layout
/* 156 */         .getHeight() + 16 + 1);
/*     */   }
/*     */   
/*     */   private void selectTab(Tab tab) {
/* 160 */     if (this.contentLayout != null) {
/* 161 */       this.contentLayout.addChild((LayoutElement)tab.getLayout());
/* 162 */       repositionElements();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void deselectTab(Tab tab) {
/* 167 */     if (this.contentLayout != null)
/* 168 */       this.contentLayout.removeChildren(); 
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 174 */     int panelLeft = this.layout.getX() - 8;
/* 175 */     int panelRight = this.layout.getX() + this.layout.getWidth() + 8;
/* 176 */     int panelTop = this.tabNavigationBar.getY();
/* 177 */     int panelBottom = this.layout.getY() + this.layout.getHeight() + 16;
/* 178 */     if (event.x() < panelLeft || event.x() > panelRight || event.y() < panelTop || event.y() > panelBottom) {
/* 179 */       this.minecraft.gui.setScreen(this.backgroundScreen);
/* 180 */       return true;
/*     */     } 
/* 182 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 187 */     if (this.minecraft.options.keyFriends.matches(event)) {
/* 187 */       GuiEventListener guiEventListener = getFocused();
/* 187 */       if (guiEventListener instanceof AddFriendWidget) {
/* 187 */         AddFriendWidget addFriendWidget = (AddFriendWidget)guiEventListener;
/* 187 */         if (!addFriendWidget.getEditBox().isFocused()) {
/* 188 */           onClose();
/* 189 */           return true;
/*     */         } 
/*     */       } else {
/*     */         onClose();
/* 189 */         return true;
/*     */       } 
/*     */     } 
/* 191 */     if (this.tabNavigationBar != null && this.tabNavigationBar.keyPressed(event))
/* 192 */       return true; 
/* 194 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 199 */     this.minecraft.gui.setScreen(this.backgroundScreen);
/*     */   }
/*     */   
/*     */   public void refreshLists() {
/* 203 */     PlayerSocialManager playerSocialManager = this.minecraft.getPlayerSocialManager();
/* 204 */     RemoteFriendListUpdateHandler.State state = playerSocialManager.getFriendListState();
/* 206 */     switch (state) {
/*     */       case LOADING:
/* 208 */         this.friendsTab.showLoading();
/* 209 */         this.pendingTab.showLoading();
/*     */         break;
/*     */       case UPGRADE_NEEDED:
/* 211 */         showError(ERROR_UPGRADE_NEEDED);
/*     */         break;
/*     */       case CONNECTION_ISSUE:
/* 212 */         showError(ERROR_CONNECTION_ISSUE);
/*     */         break;
/*     */       case TEMPORARY_UNAVAILABLE:
/* 213 */         showError(ERROR_TEMPORARY_UNAVAILABLE);
/*     */         break;
/*     */       case USER_MAY_LACK_ACTIVE_PROFILE:
/* 214 */         showError(ERROR_USER_MAY_LACK_ACTIVE_PROFILE);
/*     */         break;
/*     */       case UNAUTHORIZED:
/* 215 */         showError(ERROR_UNAUTHORIZED);
/*     */         break;
/*     */       case GENERIC_ERROR:
/* 216 */         showError(ERROR_GENERIC);
/*     */         break;
/*     */       case SUCCESS:
/* 217 */         populateLists(playerSocialManager);
/*     */         break;
/*     */     } 
/* 220 */     if (this.layout != null) {
/* 221 */       this.layout.arrangeElements();
/* 222 */       FrameLayout.alignInRectangle((LayoutElement)this.layout, getRectangle(), 0.5F, 0.5F);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void applyPresenceUpdate() {
/* 227 */     if (this.friendsTab != null)
/* 228 */       this.friendsTab.applyPresenceUpdate(this.minecraft.getPlayerSocialManager().getPresenceHandler().getLatestPresence()); 
/*     */   }
/*     */   
/*     */   private void showError(Component message) {
/* 233 */     this.friendsTab.showError(message);
/* 234 */     this.pendingTab.showError(message);
/*     */   }
/*     */   
/*     */   private void populateLists(PlayerSocialManager playerSocialManager) {
/* 238 */     List<PlayerSocialManager.PlayerData> friends = playerSocialManager.getFriends();
/* 239 */     List<PlayerSocialManager.PlayerData> incomingRequests = playerSocialManager.getIncomingRequests();
/* 240 */     List<PlayerSocialManager.PlayerData> outgoingRequests = playerSocialManager.getOutgoingRequests();
/* 241 */     PresenceResponse latestPresence = this.minecraft.getPlayerSocialManager().getPresenceHandler().getLatestPresence();
/* 243 */     if (friends.isEmpty()) {
/* 244 */       this.friendsTab.showEmpty();
/*     */     } else {
/* 246 */       List<FriendEntry> entries = new ArrayList<>(friends.size());
/* 247 */       for (PlayerSocialManager.PlayerData friend : friends) {
/* 248 */         PresenceStatusDto presence = null;
/* 249 */         for (PresenceStatusDto presenceStatusDto : latestPresence.presence()) {
/* 250 */           if (presenceStatusDto.profileId().equals(friend.id())) {
/* 251 */             presence = presenceStatusDto;
/*     */             break;
/*     */           } 
/*     */         } 
/* 255 */         UUID friendId = friend.id();
/* 256 */         boolean removalPending = this.pendingFriendRemovals.contains(friendId);
/* 257 */         entries.add(new FriendEntry(this.minecraft, this, friend, presence, removalPending, () -> {
/*     */                 this.pendingFriendRemovals.add(friendId);
/*     */                 this.minecraft.getPlayerSocialManager().removeFriend(friendId).whenCompleteAsync((), (Executor)this.minecraft).thenAcceptAsync((), (Executor)this.minecraft).exceptionally(this::onActionFailed);
/*     */               }));
/*     */       } 
/* 265 */       entries.sort(Comparator.comparingInt(FriendEntry::presenceStatusSortOrder));
/* 266 */       this.friendsTab.updateEntries(entries);
/*     */     } 
/* 269 */     int incomingRequestsCount = incomingRequests.size();
/* 270 */     if (this.pendingTabButton != null)
/* 271 */       this.pendingTabButton.setMessage((Component)Component.translatable("gui.friends.requests_count", new Object[] { Integer.valueOf(incomingRequestsCount) })); 
/* 274 */     int totalRequestsCount = incomingRequestsCount + outgoingRequests.size();
/* 275 */     if (totalRequestsCount == 0) {
/* 276 */       this.pendingTab.showEmpty();
/*     */       return;
/*     */     } 
/* 279 */     List<IncomingEntry> incomingEntries = new ArrayList<>(incomingRequests.size());
/* 280 */     if (!incomingRequests.isEmpty())
/* 281 */       for (PlayerSocialManager.PlayerData incomingRequest : incomingRequests)
/* 282 */         incomingEntries.add(new IncomingEntry(this.minecraft, this, incomingRequest, () -> this.minecraft.getPlayerSocialManager().acceptIncomingFriendRequest(incomingRequest.id()).thenAcceptAsync((), (Executor)this.minecraft).exceptionally(this::onActionFailed), () -> this.minecraft.getPlayerSocialManager().declineIncomingFriendRequest(incomingRequest.id()).thenRunAsync(this::refreshLists, (Executor)this.minecraft).exceptionally(this::onActionFailed)));  
/* 289 */     List<OutgoingEntry> outgoingEntries = new ArrayList<>(outgoingRequests.size());
/* 290 */     if (!outgoingRequests.isEmpty())
/* 291 */       for (PlayerSocialManager.PlayerData outgoingRequest : outgoingRequests)
/* 292 */         outgoingEntries.add(new OutgoingEntry(this.minecraft, this, outgoingRequest, () -> this.minecraft.getPlayerSocialManager().revokeOutgoingFriendRequest(outgoingRequest.id()).thenRunAsync(this::refreshLists, (Executor)this.minecraft).exceptionally(this::onActionFailed)));  
/* 299 */     this.pendingTab.updateEntries(incomingEntries, outgoingEntries);
/* 301 */     if (this.layout != null) {
/* 302 */       this.layout.arrangeElements();
/* 303 */       FrameLayout.alignInRectangle((LayoutElement)this.layout, getRectangle(), 0.5F, 0.5F);
/*     */     } 
/*     */   }
/*     */   
/*     */   void startFriendAction() {
/* 310 */     this.friendsTab.disable();
/* 311 */     this.pendingTab.disable();
/*     */   }
/*     */   
/*     */   private Void onActionFailed(Throwable ex) {
/* 315 */     LOGGER.error("Friend action failed", ex);
/* 316 */     this.minecraft.execute(() -> {
/*     */           SystemToast.addOrUpdate(this.minecraft.gui.toastManager(), SystemToast.SystemToastId.FRIEND_SYSTEM_NOTIFICATION, ERROR_TOAST_GENERIC, null);
/*     */           refreshLists();
/*     */         });
/* 325 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\friends\FriendsOverlayScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */