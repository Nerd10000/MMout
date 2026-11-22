/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import java.net.URI;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class ConfirmLinkScreen extends ConfirmScreen {
/*  18 */   private static final Component WARNING_TEXT = (Component)Component.translatable("chat.link.warning")
/*  19 */     .withColor(-13108);
/*     */   
/*     */   private static final int BUTTON_WIDTH = 100;
/*     */   
/*     */   private final String url;
/*     */   
/*     */   private final boolean showWarning;
/*     */   
/*     */   public ConfirmLinkScreen(BooleanConsumer callback, String url, boolean trusted) {
/*  27 */     this(callback, (Component)confirmMessage(trusted), (Component)Component.literal(url), url, trusted ? CommonComponents.GUI_CANCEL : CommonComponents.GUI_NO, trusted);
/*     */   }
/*     */   
/*     */   public ConfirmLinkScreen(BooleanConsumer callback, Component title, String url, boolean trusted) {
/*  31 */     this(callback, title, (Component)confirmMessage(trusted, url), url, trusted ? CommonComponents.GUI_CANCEL : CommonComponents.GUI_NO, trusted);
/*     */   }
/*     */   
/*     */   public ConfirmLinkScreen(BooleanConsumer callback, Component title, URI uri, boolean trusted) {
/*  35 */     this(callback, title, uri.toString(), trusted);
/*     */   }
/*     */   
/*     */   public ConfirmLinkScreen(BooleanConsumer callback, Component title, Component message, URI uri, Component noButton, boolean trusted) {
/*  39 */     this(callback, title, message, uri.toString(), noButton, trusted);
/*     */   }
/*     */   
/*     */   public ConfirmLinkScreen(BooleanConsumer callback, Component title, Component message, String url, Component noButtonComponent, boolean trusted) {
/*  43 */     super(callback, title, message);
/*  44 */     this.yesButtonComponent = trusted ? CommonComponents.GUI_OPEN_IN_BROWSER : CommonComponents.GUI_YES;
/*  45 */     this.noButtonComponent = noButtonComponent;
/*  46 */     this.showWarning = !trusted;
/*  47 */     this.url = url;
/*     */   }
/*     */   
/*     */   protected static MutableComponent confirmMessage(boolean trusted, String url) {
/*  51 */     return confirmMessage(trusted).append(CommonComponents.SPACE).append((Component)Component.literal(url));
/*     */   }
/*     */   
/*     */   protected static MutableComponent confirmMessage(boolean trusted) {
/*  55 */     return Component.translatable(trusted ? "chat.link.confirmTrusted" : "chat.link.confirm");
/*     */   }
/*     */   
/*     */   protected void addAdditionalText() {
/*  60 */     if (this.showWarning)
/*  61 */       this.layout.addChild((LayoutElement)new StringWidget(WARNING_TEXT, this.font)); 
/*     */   }
/*     */   
/*     */   protected void addButtons(LinearLayout buttonLayout) {
/*  67 */     this.yesButton = (Button)buttonLayout.addChild((LayoutElement)Button.builder(this.yesButtonComponent, button -> this.callback.accept(true)).width(100).build());
/*  68 */     buttonLayout.addChild((LayoutElement)Button.builder(CommonComponents.GUI_COPY_TO_CLIPBOARD, button -> {
/*     */             copyToClipboard();
/*     */             this.callback.accept(false);
/*  71 */           }).width(100).build());
/*  72 */     this.noButton = (Button)buttonLayout.addChild((LayoutElement)Button.builder(this.noButtonComponent, button -> this.callback.accept(false)).width(100).build());
/*     */   }
/*     */   
/*     */   public void copyToClipboard() {
/*  76 */     this.minecraft.keyboardHandler.setClipboard(this.url);
/*     */   }
/*     */   
/*     */   public static void confirmLinkNow(Screen parentScreen, String uri, boolean trusted) {
/*  80 */     Minecraft minecraft = Minecraft.getInstance();
/*  81 */     minecraft.gui.setScreen(new ConfirmLinkScreen(shouldOpen -> {
/*     */             if (shouldOpen)
/*     */               Util.getPlatform().openUri(uri); 
/*     */             minecraft.gui.setScreen(parentScreen);
/*  81 */           }uri, trusted));
/*     */   }
/*     */   
/*     */   public static void confirmLinkNow(Screen parentScreen, URI uri, boolean trusted) {
/*  92 */     Minecraft minecraft = Minecraft.getInstance();
/*  93 */     minecraft.gui.setScreen(new ConfirmLinkScreen(shouldOpen -> {
/*     */             if (shouldOpen)
/*     */               Util.getPlatform().openUri(uri); 
/*     */             minecraft.gui.setScreen(parentScreen);
/*  93 */           }uri
/*     */           
/*  99 */           .toString(), trusted));
/*     */   }
/*     */   
/*     */   public static void confirmLinkNow(Screen parentScreen, URI uri) {
/* 104 */     confirmLinkNow(parentScreen, uri, true);
/*     */   }
/*     */   
/*     */   public static void confirmLinkNow(Screen parentScreen, String uri) {
/* 108 */     confirmLinkNow(parentScreen, uri, true);
/*     */   }
/*     */   
/*     */   public static Button.OnPress confirmLink(Screen parentScreen, String uri, boolean trusted) {
/* 112 */     return button -> confirmLinkNow(parentScreen, uri, trusted);
/*     */   }
/*     */   
/*     */   public static Button.OnPress confirmLink(Screen parentScreen, URI uri, boolean trusted) {
/* 116 */     return button -> confirmLinkNow(parentScreen, uri, trusted);
/*     */   }
/*     */   
/*     */   public static Button.OnPress confirmLink(Screen parentScreen, String uri) {
/* 120 */     return confirmLink(parentScreen, uri, true);
/*     */   }
/*     */   
/*     */   public static Button.OnPress confirmLink(Screen parentScreen, URI uri) {
/* 124 */     return confirmLink(parentScreen, uri, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\ConfirmLinkScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */