/*    */ package com.mojang.realmsclient.gui.screens;
/*    */ 
/*    */ import net.minecraft.client.GameNarrator;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.layouts.FrameLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.realms.RealmsScreen;
/*    */ import net.minecraft.util.CommonLinks;
/*    */ 
/*    */ public class RealmsParentalConsentScreen extends RealmsScreen {
/* 17 */   private static final Component MESSAGE = (Component)Component.translatable("mco.account.privacy.information");
/*    */   
/*    */   private static final int SPACING = 15;
/*    */   
/* 20 */   private final LinearLayout layout = LinearLayout.vertical();
/*    */   
/*    */   private final Screen lastScreen;
/*    */   
/*    */   private MultiLineTextWidget textWidget;
/*    */   
/*    */   public RealmsParentalConsentScreen(Screen lastScreen) {
/* 25 */     super(GameNarrator.NO_TITLE);
/* 26 */     this.lastScreen = lastScreen;
/*    */   }
/*    */   
/*    */   public void init() {
/* 31 */     this.layout.spacing(15).defaultCellSetting().alignHorizontallyCenter();
/* 32 */     this.textWidget = (new MultiLineTextWidget(MESSAGE, this.font)).setCentered(true);
/* 33 */     this.layout.addChild((LayoutElement)this.textWidget);
/* 34 */     LinearLayout buttonLayout = (LinearLayout)this.layout.addChild((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 35 */     MutableComponent mutableComponent = Component.translatable("mco.account.privacy.info.button");
/* 36 */     buttonLayout.addChild((LayoutElement)Button.builder((Component)mutableComponent, 
/* 37 */           ConfirmLinkScreen.confirmLink((Screen)this, CommonLinks.GDPR))
/*    */         
/* 40 */         .build());
/* 42 */     buttonLayout.addChild((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).build());
/* 43 */     RealmsParentalConsentScreen realmsParentalConsentScreen = this;
/* 43 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/* 44 */     repositionElements();
/*    */   }
/*    */   
/*    */   public void onClose() {
/* 49 */     this.minecraft.gui.setScreen(this.lastScreen);
/*    */   }
/*    */   
/*    */   protected void repositionElements() {
/* 54 */     if (this.textWidget != null)
/* 55 */       this.textWidget.setMaxWidth(this.width - 15); 
/* 57 */     this.layout.arrangeElements();
/* 58 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*    */   }
/*    */   
/*    */   public Component getNarrationMessage() {
/* 63 */     return MESSAGE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsParentalConsentScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */