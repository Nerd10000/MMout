/*     */ package net.minecraft.client.gui.screens.friends;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.LoadingDotsWidget;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.ScrollableLayout;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class PendingTab extends AbstractFriendsTab {
/*  22 */   private static final Component RECEIVED_HEADER = (Component)Component.translatable("gui.friends.pending.received").withStyle(new ChatFormatting[] { ChatFormatting.BOLD, ChatFormatting.UNDERLINE });
/*     */   
/*  23 */   private static final Component SENT_HEADER = (Component)Component.translatable("gui.friends.pending.sent").withStyle(new ChatFormatting[] { ChatFormatting.BOLD, ChatFormatting.UNDERLINE });
/*     */   
/*  24 */   private static final Component EMPTY_STATE = (Component)Component.translatable("gui.friends.pending.empty").withStyle(ChatFormatting.GRAY);
/*     */   
/*     */   private final FriendsOverlayScreen screen;
/*     */   
/*     */   private final LinearLayout layout;
/*     */   
/*     */   private final LinearLayout pendingScrollableContent;
/*     */   
/*     */   private final LoadingDotsWidget loadingDotsWidget;
/*     */   
/*     */   private final ScrollableLayout scrollableLayout;
/*     */   
/*     */   PendingTab(Minecraft minecraft, LoadingDotsWidget loadingDotsWidget, FriendsOverlayScreen screen, int width, int height) {
/*  34 */     super(width, height);
/*  35 */     this.screen = screen;
/*  36 */     this.layout = LinearLayout.vertical();
/*  37 */     this.layout.defaultCellSetting().alignHorizontallyCenter();
/*  38 */     this.loadingDotsWidget = loadingDotsWidget;
/*  39 */     this.pendingScrollableContent = LinearLayout.vertical();
/*  41 */     this.scrollableLayout = new ScrollableLayout(minecraft, (Layout)this.pendingScrollableContent, height, ScrollableLayout.ReserveStrategy.BOTH);
/*  42 */     this.scrollableLayout.setScrollbarSpacing(2);
/*  43 */     this.scrollableLayout.setMaxHeight(height);
/*  44 */     this.layout.addChild((LayoutElement)this.scrollableLayout);
/*  45 */     rearrangeElements();
/*     */   }
/*     */   
/*     */   void rearrangeElements() {
/*  51 */     this.scrollableLayout.setMinHeight(this.height);
/*  52 */     this.scrollableLayout.setMaxHeight(this.height);
/*     */   }
/*     */   
/*     */   public Component getTabTitle() {
/*  57 */     return (Component)Component.translatable("gui.friends.requests_count", new Object[] { Integer.valueOf(0) });
/*     */   }
/*     */   
/*     */   public Component getTabExtraNarration() {
/*  62 */     return (Component)Component.empty();
/*     */   }
/*     */   
/*     */   public void visitChildren(Consumer<AbstractWidget> childrenConsumer) {
/*  67 */     this.layout.visitWidgets(childrenConsumer);
/*     */   }
/*     */   
/*     */   public void doLayout(ScreenRectangle screenRectangle) {
/*  72 */     this.layout.arrangeElements();
/*  73 */     FrameLayout.alignInRectangle((LayoutElement)this.layout, screenRectangle, 0.5F, 0.16666667F);
/*     */   }
/*     */   
/*     */   public Layout getLayout() {
/*  78 */     return (Layout)this.layout;
/*     */   }
/*     */   
/*     */   public void showLoading() {
/*  82 */     this.pendingScrollableContent.removeChildren();
/*  83 */     this.pendingScrollableContent.addChild((LayoutElement)createCenteredFrame((LayoutElement)this.loadingDotsWidget, getListContentWidth(), this.height));
/*     */   }
/*     */   
/*     */   public void showError(Component message) {
/*  87 */     this.pendingScrollableContent.removeChildren();
/*  89 */     int maxWidth = getListContentWidth();
/*  90 */     MultiLineTextWidget text = createCenteredText((Component)message.copy().withStyle(ChatFormatting.GRAY), this.screen.getFont(), maxWidth);
/*  91 */     this.pendingScrollableContent.addChild((LayoutElement)createCenteredFrame((LayoutElement)text, maxWidth, this.height));
/*     */   }
/*     */   
/*     */   public void updateEntries(List<IncomingEntry> incomingEntries, List<OutgoingEntry> outgoingEntries) {
/*  95 */     this.pendingScrollableContent.removeChildren();
/*  97 */     if (!incomingEntries.isEmpty()) {
/*  98 */       this.pendingScrollableContent.addChild((LayoutElement)createText(RECEIVED_HEADER, this.screen.getFont(), getListContentWidth()), LayoutSettings::alignHorizontallyCenter);
/*  99 */       Objects.requireNonNull(this.pendingScrollableContent);
/*  99 */       incomingEntries.forEach(this.pendingScrollableContent::addChild);
/*     */     } 
/* 102 */     if (!outgoingEntries.isEmpty()) {
/* 103 */       this.pendingScrollableContent.addChild((LayoutElement)createText(SENT_HEADER, this.screen.getFont(), getListContentWidth()), LayoutSettings::alignHorizontallyCenter);
/* 104 */       Objects.requireNonNull(this.pendingScrollableContent);
/* 104 */       outgoingEntries.forEach(this.pendingScrollableContent::addChild);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Layout entriesContainer() {
/* 110 */     return (Layout)this.pendingScrollableContent;
/*     */   }
/*     */   
/*     */   public void showEmpty() {
/* 114 */     this.pendingScrollableContent.removeChildren();
/* 115 */     LinearLayout content = (new LinearLayout(0, 0, LinearLayout.Orientation.VERTICAL)).spacing(8);
/* 116 */     content.defaultCellSetting().alignHorizontallyCenter().alignVerticallyMiddle();
/* 118 */     int maxWidth = getListContentWidth();
/* 119 */     content.addChild((LayoutElement)createCenteredText(EMPTY_STATE, this.screen.getFont(), maxWidth));
/* 121 */     this.pendingScrollableContent.addChild((LayoutElement)createCenteredFrame((LayoutElement)content, maxWidth, this.height));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\friends\PendingTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */