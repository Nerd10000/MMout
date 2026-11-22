/*    */ package net.minecraft.client.gui.screens.reporting;
/*    */ 
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*    */ import net.minecraft.client.gui.components.StringWidget;
/*    */ import net.minecraft.client.gui.components.Tooltip;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.layouts.FrameLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.layouts.SpacerElement;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.gui.screens.social.PlayerEntry;
/*    */ import net.minecraft.client.multiplayer.chat.report.ReportingContext;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ReportPlayerScreen extends Screen {
/* 17 */   private static final Component TITLE = (Component)Component.translatable("gui.abuseReport.title");
/*    */   
/* 18 */   private static final Component MESSAGE = (Component)Component.translatable("gui.abuseReport.message");
/*    */   
/* 20 */   private static final Component REPORT_CHAT = (Component)Component.translatable("gui.abuseReport.type.chat");
/*    */   
/* 21 */   private static final Component REPORT_SKIN = (Component)Component.translatable("gui.abuseReport.type.skin");
/*    */   
/* 22 */   private static final Component REPORT_NAME = (Component)Component.translatable("gui.abuseReport.type.name");
/*    */   
/*    */   private static final int SPACING = 6;
/*    */   
/*    */   private final Screen lastScreen;
/*    */   
/*    */   private final ReportingContext context;
/*    */   
/*    */   private final PlayerEntry player;
/*    */   
/*    */   private final boolean chatDisabledOrBlocked;
/*    */   
/* 31 */   private final LinearLayout layout = LinearLayout.vertical().spacing(6);
/*    */   
/*    */   public ReportPlayerScreen(Screen lastScreen, ReportingContext context, PlayerEntry player, boolean chatDisabledOrBlocked) {
/* 34 */     super(TITLE);
/* 35 */     this.lastScreen = lastScreen;
/* 36 */     this.context = context;
/* 37 */     this.player = player;
/* 38 */     this.chatDisabledOrBlocked = chatDisabledOrBlocked;
/*    */   }
/*    */   
/*    */   public Component getNarrationMessage() {
/* 43 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), MESSAGE });
/*    */   }
/*    */   
/*    */   protected void init() {
/* 48 */     this.layout.defaultCellSetting().alignHorizontallyCenter();
/* 49 */     this.layout.addChild((LayoutElement)new StringWidget(this.title, this.font), this.layout.newCellSettings().paddingBottom(6));
/* 51 */     this.layout.addChild((LayoutElement)(new MultiLineTextWidget(MESSAGE, this.font)).setCentered(true), this.layout.newCellSettings().paddingBottom(6));
/* 53 */     Button chatButton = (Button)this.layout.addChild((LayoutElement)Button.builder(REPORT_CHAT, paramButton -> this.minecraft.gui.setScreen(new ChatReportScreen(this.lastScreen, this.context, this.player.getPlayerId()))).build());
/* 54 */     if (this.chatDisabledOrBlocked) {
/* 55 */       chatButton.active = false;
/* 56 */       chatButton.setTooltip(Tooltip.create((Component)Component.translatable("gui.socialInteractions.tooltip.report.chat_disabled_or_blocked")));
/* 57 */     } else if (!this.player.isChatReportable()) {
/* 58 */       chatButton.active = false;
/* 59 */       chatButton.setTooltip(Tooltip.create((Component)Component.translatable("gui.socialInteractions.tooltip.report.not_reportable")));
/* 60 */     } else if (!this.player.hasRecentMessages()) {
/* 61 */       chatButton.active = false;
/* 62 */       chatButton.setTooltip(Tooltip.create((Component)Component.translatable("gui.socialInteractions.tooltip.report.no_messages", new Object[] { this.player.getPlayerName() })));
/*    */     } 
/* 65 */     this.layout.addChild((LayoutElement)Button.builder(REPORT_SKIN, paramButton -> this.minecraft.gui.setScreen(new SkinReportScreen(this.lastScreen, this.context, this.player.getPlayerId(), this.player.getSkinGetter()))).build());
/* 66 */     this.layout.addChild((LayoutElement)Button.builder(REPORT_NAME, b -> this.minecraft.gui.setScreen(new NameReportScreen(this.lastScreen, this.context, this.player.getPlayerId(), this.player.getPlayerName()))).build());
/* 68 */     this.layout.addChild((LayoutElement)SpacerElement.height(20));
/* 70 */     this.layout.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, b -> onClose()).build());
/* 72 */     ReportPlayerScreen reportPlayerScreen = this;
/* 72 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/* 73 */     repositionElements();
/*    */   }
/*    */   
/*    */   protected void repositionElements() {
/* 78 */     this.layout.arrangeElements();
/* 79 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*    */   }
/*    */   
/*    */   public void onClose() {
/* 84 */     this.minecraft.gui.setScreen(this.lastScreen);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\reporting\ReportPlayerScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */