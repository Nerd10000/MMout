/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.client.OptionInstance;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.Tooltip;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.CommonLinks;
/*    */ import net.minecraft.world.flag.FeatureFlags;
/*    */ 
/*    */ public class AccessibilityOptionsScreen extends OptionsSubScreen {
/* 21 */   public static final Component TITLE = (Component)Component.translatable("options.accessibility.title");
/*    */   
/*    */   private static OptionInstance<?>[] options(Options options) {
/* 24 */     return (OptionInstance<?>[])new OptionInstance[] { 
/* 24 */         options
/* 25 */         .narrator(), options.showSubtitles(), options
/* 26 */         .highContrast(), options
/* 27 */         .menuBackgroundBlurriness(), options.textBackgroundOpacity(), options
/* 28 */         .backgroundForChatOnly(), options.chatOpacity(), options
/* 29 */         .chatLineSpacing(), options.chatDelay(), options
/* 30 */         .notificationDisplayTime(), 
/* 30 */         options.bobView(), options
/* 31 */         .screenEffectScale(), options.fovEffectScale(), options
/* 32 */         .darknessEffectScale(), options.damageTiltStrength(), options
/* 33 */         .glintSpeed(), options.glintStrength(), options
/* 34 */         .hideLightningFlash(), options.darkMojangStudiosBackground(), options
/* 35 */         .panoramaSpeed(), 
/* 35 */         options.hideSplashTexts(), options
/* 36 */         .narratorHotkey(), options.rotateWithMinecart(), options
/* 37 */         .highContrastBlockOutline() };
/*    */   }
/*    */   
/*    */   public AccessibilityOptionsScreen(Screen lastScreen, Options options) {
/* 42 */     super(lastScreen, options, TITLE);
/*    */   }
/*    */   
/*    */   protected void init() {
/* 47 */     super.init();
/* 48 */     AbstractWidget highContrast = this.list.findOption(this.options.highContrast());
/* 49 */     if (highContrast != null && 
/* 50 */       !this.minecraft.getResourcePackRepository().getAvailableIds().contains("high_contrast")) {
/* 51 */       highContrast.active = false;
/* 52 */       highContrast.setTooltip(Tooltip.create((Component)Component.translatable("options.accessibility.high_contrast.error.tooltip")));
/*    */     } 
/* 56 */     AbstractWidget rotateWithMinecart = this.list.findOption(this.options.rotateWithMinecart());
/* 57 */     if (rotateWithMinecart != null)
/* 58 */       rotateWithMinecart.active = isMinecartOptionEnabled(); 
/*    */   }
/*    */   
/*    */   protected void addOptions() {
/* 64 */     OptionInstance[] arrayOfOptionInstance = (OptionInstance[])options(this.options);
/* 67 */     Button controlsLink = Button.builder(OptionsScreen.CONTROLS, paramButton -> this.minecraft.gui.setScreen((Screen)new ControlsScreen(this, this.options))).build();
/* 68 */     OptionInstance<?> firstOptionInstance = arrayOfOptionInstance[0];
/* 69 */     this.list.addSmall(firstOptionInstance.createButton(this.options), this.options.narrator(), (AbstractWidget)controlsLink);
/* 70 */     this.list.addSmall((OptionInstance[])Arrays.<OptionInstance>stream(arrayOfOptionInstance).filter(instance -> (instance != firstOptionInstance)).toArray(x$0 -> new OptionInstance[x$0]));
/*    */   }
/*    */   
/*    */   protected void addFooter() {
/* 75 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 76 */     footer.addChild(
/* 77 */         (LayoutElement)Button.builder((Component)Component.translatable("options.accessibility.link"), ConfirmLinkScreen.confirmLink(this, CommonLinks.ACCESSIBILITY_HELP))
/* 78 */         .build());
/* 80 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> this.minecraft.gui.setScreen(this.lastScreen)).build());
/*    */   }
/*    */   
/*    */   private boolean isMinecartOptionEnabled() {
/* 84 */     return (this.minecraft.level != null && this.minecraft.level.enabledFeatures().contains(FeatureFlags.MINECART_IMPROVEMENTS));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\AccessibilityOptionsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */