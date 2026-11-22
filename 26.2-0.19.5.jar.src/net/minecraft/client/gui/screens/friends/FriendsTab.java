/*     */ package net.minecraft.client.gui.screens.friends;
/*     */ 
/*     */ import com.mojang.authlib.yggdrasil.response.PresenceResponse;
/*     */ import com.mojang.authlib.yggdrasil.response.PresenceStatusDto;
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.ImageWidget;
/*     */ import net.minecraft.client.gui.components.LoadingDotsWidget;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.ScrollableLayout;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.PrivacyConfirmLinkScreen;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ 
/*     */ class FriendsTab extends AbstractFriendsTab {
/*  29 */   private static final Component TAB_TITLE = (Component)Component.translatable("gui.friends.tab_friends");
/*     */   
/*     */   private static final Component MICROSOFT_ACCOUNT_LINK;
/*     */   
/*     */   static {
/*  31 */     MICROSOFT_ACCOUNT_LINK = (Component)Component.translatable("gui.friends.empty_state.link").withStyle(style -> style.withUnderlined(Boolean.valueOf(true)).withColor(ChatFormatting.GRAY).withClickEvent((ClickEvent)new ClickEvent.OpenUrl(CommonLinks.PRIVACY_AND_ONLINE_SETTINGS)));
/*     */   }
/*     */   
/*  35 */   private static final Component EMPTY_STATE = (Component)Component.translatable("gui.friends.empty_state", new Object[] { MICROSOFT_ACCOUNT_LINK }).withStyle(ChatFormatting.GRAY);
/*     */   
/*  36 */   private static final Component MANAGE_ACCOUNT_FOOTER = (Component)Component.translatable("gui.friends.manage_account_footer", new Object[] { MICROSOFT_ACCOUNT_LINK }).withStyle(ChatFormatting.GRAY);
/*     */   
/*  37 */   private static final Identifier ILLUSTRATION = Identifier.withDefaultNamespace("friends/illustrations_00");
/*     */   
/*     */   private final FriendsOverlayScreen screen;
/*     */   
/*     */   private final LinearLayout layout;
/*     */   
/*     */   private final LinearLayout friendScrollableContent;
/*     */   
/*     */   private final LoadingDotsWidget loadingDotsWidget;
/*     */   
/*     */   private final AddFriendWidget addFriendWidget;
/*     */   
/*     */   private final ScrollableLayout scrollableLayout;
/*     */   
/*     */   FriendsTab(Minecraft minecraft, LoadingDotsWidget loadingDotsWidget, FriendsOverlayScreen screen, int width, int height) {
/*  48 */     super(width, height);
/*  49 */     this.screen = screen;
/*  50 */     this.layout = LinearLayout.vertical();
/*  51 */     this.layout.defaultCellSetting().alignHorizontallyCenter();
/*  52 */     this.loadingDotsWidget = loadingDotsWidget;
/*  53 */     this.addFriendWidget = new AddFriendWidget(width, this::onSendFriendRequestFinished);
/*  54 */     this.layout.addChild((LayoutElement)this.addFriendWidget);
/*  56 */     this.friendScrollableContent = LinearLayout.vertical();
/*  57 */     this.friendScrollableContent.defaultCellSetting();
/*  59 */     this.scrollableLayout = new ScrollableLayout(minecraft, (Layout)this.friendScrollableContent, height - this.addFriendWidget.contentHeight(), ScrollableLayout.ReserveStrategy.BOTH);
/*  60 */     this.scrollableLayout.setScrollbarSpacing(2);
/*  61 */     this.layout.addChild((LayoutElement)this.scrollableLayout);
/*  62 */     rearrangeElements();
/*     */   }
/*     */   
/*     */   public void showLoading() {
/*  66 */     this.friendScrollableContent.removeChildren();
/*  67 */     this.friendScrollableContent.addChild((LayoutElement)createCenteredFrame((LayoutElement)this.loadingDotsWidget, getListContentWidth(), this.height - this.addFriendWidget.contentHeight()));
/*  68 */     this.addFriendWidget.applyState(AddFriendWidget.State.SENDING);
/*     */   }
/*     */   
/*     */   public void showError(Component message) {
/*  72 */     this.friendScrollableContent.removeChildren();
/*  74 */     int maxWidth = getListContentWidth();
/*  75 */     MultiLineTextWidget text = createCenteredText((Component)message.copy().withStyle(ChatFormatting.GRAY), this.screen.getFont(), maxWidth);
/*  76 */     this.friendScrollableContent.addChild((LayoutElement)createCenteredFrame((LayoutElement)text, maxWidth, this.height - this.addFriendWidget.contentHeight()));
/*  78 */     this.addFriendWidget.applyState(AddFriendWidget.State.DISABLED);
/*     */   }
/*     */   
/*     */   public void showEmpty() {
/*  82 */     this.friendScrollableContent.removeChildren();
/*  83 */     LinearLayout content = (new LinearLayout(0, 0, LinearLayout.Orientation.VERTICAL)).spacing(8);
/*  84 */     content.defaultCellSetting().alignHorizontallyCenter().alignVerticallyMiddle();
/*  86 */     content.addChild((LayoutElement)ImageWidget.sprite(128, 48, ILLUSTRATION));
/*  88 */     int maxWidth = getListContentWidth();
/*  89 */     MultiLineTextWidget textWidget = createCenteredText(EMPTY_STATE, this.screen.getFont(), maxWidth);
/*  90 */     textWidget.setComponentClickHandler(style -> {
/*     */           ClickEvent patt1$temp = style.getClickEvent();
/*     */           if (patt1$temp instanceof ClickEvent.OpenUrl) {
/*     */             ClickEvent.OpenUrl $b$0 = (ClickEvent.OpenUrl)patt1$temp;
/*     */             try {
/*     */               URI patt2$temp = $b$0.uri();
/*     */               URI uri = patt2$temp;
/*     */               PrivacyConfirmLinkScreen.confirmLinkNow(this.screen, uri);
/*  91 */             } catch (Throwable throwable) {
/*     */               throw new MatchException(throwable.toString(), throwable);
/*     */             } 
/*     */           } 
/*     */         });
/*  95 */     content.addChild((LayoutElement)textWidget);
/*  97 */     int frameHeight = this.scrollableLayout.getHeight();
/*  98 */     this.friendScrollableContent.addChild((LayoutElement)createCenteredFrame((LayoutElement)content, maxWidth, frameHeight));
/* 100 */     this.addFriendWidget.applyState(this.addFriendWidget.getValue().isEmpty() ? AddFriendWidget.State.EMPTY_INPUT : AddFriendWidget.State.READY);
/*     */   }
/*     */   
/*     */   void rearrangeElements() {
/* 106 */     this.scrollableLayout.setMinHeight(this.height - this.addFriendWidget.contentHeight());
/* 107 */     this.scrollableLayout.setMaxHeight(this.height - this.addFriendWidget.contentHeight());
/*     */   }
/*     */   
/*     */   private void onSendFriendRequestFinished() {
/* 111 */     this.screen.refreshLists();
/*     */   }
/*     */   
/*     */   public Component getTabTitle() {
/* 117 */     return TAB_TITLE;
/*     */   }
/*     */   
/*     */   public Component getTabExtraNarration() {
/* 122 */     return (Component)Component.empty();
/*     */   }
/*     */   
/*     */   public void visitChildren(Consumer<AbstractWidget> childrenConsumer) {
/* 127 */     this.layout.visitWidgets(childrenConsumer);
/*     */   }
/*     */   
/*     */   public void doLayout(ScreenRectangle screenRectangle) {
/* 132 */     this.layout.arrangeElements();
/* 133 */     FrameLayout.alignInRectangle((LayoutElement)this.layout, screenRectangle, 0.5F, 0.16666667F);
/*     */   }
/*     */   
/*     */   public Layout getLayout() {
/* 138 */     return (Layout)this.layout;
/*     */   }
/*     */   
/*     */   void updateEntries(List<FriendEntry> friendEntries) {
/* 143 */     this.friendScrollableContent.removeChildren();
/* 144 */     Objects.requireNonNull(this.friendScrollableContent);
/* 144 */     friendEntries.forEach(this.friendScrollableContent::addChild);
/* 145 */     this.friendScrollableContent.addChild((LayoutElement)createManageAccountFooter());
/*     */   }
/*     */   
/*     */   void applyPresenceUpdate(PresenceResponse latestPresence) {
/* 150 */     this.friendScrollableContent.visitWidgets(widget -> {
/*     */           if (widget instanceof FriendEntry) {
/*     */             FriendEntry entry = (FriendEntry)widget;
/*     */             PresenceStatusDto newPresenceStatus = null;
/*     */             for (PresenceStatusDto presenceStatus : latestPresence.presence()) {
/*     */               if (presenceStatus.profileId().equals(entry.playerId())) {
/*     */                 newPresenceStatus = presenceStatus;
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */             entry.applyPresence(newPresenceStatus);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private FrameLayout createManageAccountFooter() {
/* 165 */     int maxWidth = getListContentWidth();
/* 166 */     MultiLineTextWidget textWidget = createCenteredText(MANAGE_ACCOUNT_FOOTER, this.screen.getFont(), maxWidth);
/* 167 */     textWidget.setComponentClickHandler(style -> {
/*     */           ClickEvent patt1$temp = style.getClickEvent();
/*     */           if (patt1$temp instanceof ClickEvent.OpenUrl) {
/*     */             ClickEvent.OpenUrl $b$0 = (ClickEvent.OpenUrl)patt1$temp;
/*     */             try {
/*     */               URI patt2$temp = $b$0.uri();
/*     */               URI uri = patt2$temp;
/*     */               AbstractWidget.playButtonClickSound(Minecraft.getInstance().getSoundManager());
/*     */               PrivacyConfirmLinkScreen.confirmLinkNow(this.screen, uri);
/* 168 */             } catch (Throwable throwable) {
/*     */               throw new MatchException(throwable.toString(), throwable);
/*     */             } 
/*     */           } 
/*     */         });
/* 173 */     FrameLayout frame = new FrameLayout(maxWidth, textWidget.getHeight());
/* 174 */     frame.defaultChildLayoutSetting().alignHorizontallyCenter().alignVerticallyMiddle();
/* 175 */     frame.addChild((LayoutElement)textWidget);
/* 176 */     return frame;
/*     */   }
/*     */   
/*     */   protected Layout entriesContainer() {
/* 181 */     return (Layout)this.friendScrollableContent;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\friends\FriendsTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */