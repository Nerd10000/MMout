/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*    */ import java.net.URI;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*    */ import net.minecraft.client.gui.components.StringWidget;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class PrivacyConfirmLinkScreen extends ConfirmLinkScreen {
/*    */   private static final Component TITLE;
/*    */   
/*    */   static {
/* 20 */     TITLE = (Component)Component.translatable("gui.privacy_link.title").withStyle(style -> style.withColor(-256).withUnderlined(Boolean.valueOf(true)));
/*    */   }
/*    */   
/* 21 */   private static final Component MESSAGE = (Component)Component.translatable("gui.privacy_link.message");
/*    */   
/*    */   private final Component urlComponent;
/*    */   
/*    */   public PrivacyConfirmLinkScreen(BooleanConsumer callback, String url) {
/* 26 */     super(callback, TITLE, MESSAGE, url, CommonComponents.GUI_CANCEL, true);
/* 27 */     this.urlComponent = (Component)Component.literal(url).withStyle(ChatFormatting.WHITE);
/*    */   }
/*    */   
/*    */   protected void init() {
/* 32 */     this.layout.defaultCellSetting().alignHorizontallyCenter();
/* 34 */     this.layout.addChild((LayoutElement)(new MultiLineTextWidget(this.title, this.font))
/* 35 */         .setMaxWidth(this.width - 50)
/* 36 */         .setMaxRows(4)
/* 37 */         .setCentered(true));
/* 39 */     this.layout.addChild((LayoutElement)(new MultiLineTextWidget(MESSAGE, this.font))
/* 40 */         .setMaxWidth(this.width - 50)
/* 41 */         .setMaxRows(15)
/* 42 */         .setCentered(true));
/* 44 */     addAdditionalText();
/* 46 */     LinearLayout buttonLayout = (LinearLayout)this.layout.addChild((LayoutElement)LinearLayout.horizontal().spacing(4));
/* 47 */     buttonLayout.defaultCellSetting().paddingTop(16);
/* 48 */     addButtons(buttonLayout);
/* 50 */     this.layout.visitWidgets(this::addRenderableWidget);
/* 51 */     repositionElements();
/*    */   }
/*    */   
/*    */   protected void addAdditionalText() {
/* 56 */     this.layout.addChild((LayoutElement)new StringWidget(this.urlComponent, this.font));
/*    */   }
/*    */   
/*    */   public static void confirmLinkNow(Screen parentScreen, URI uri) {
/* 60 */     confirmLinkNow(parentScreen, uri.toString());
/*    */   }
/*    */   
/*    */   public static void confirmLinkNow(Screen parentScreen, String uri) {
/* 64 */     Minecraft minecraft = Minecraft.getInstance();
/* 65 */     minecraft.gui.setScreen(new PrivacyConfirmLinkScreen(shouldOpen -> {
/*    */             if (shouldOpen)
/*    */               Util.getPlatform().openUri(uri); 
/*    */             minecraft.gui.setScreen(parentScreen);
/* 65 */           }uri));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\PrivacyConfirmLinkScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */