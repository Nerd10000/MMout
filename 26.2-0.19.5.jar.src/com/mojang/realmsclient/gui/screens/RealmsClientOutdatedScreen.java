/*    */ package com.mojang.realmsclient.gui.screens;
/*    */ 
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.realms.RealmsScreen;
/*    */ 
/*    */ public class RealmsClientOutdatedScreen extends RealmsScreen {
/* 14 */   private static final Component INCOMPATIBLE_TITLE = (Component)Component.translatable("mco.client.incompatible.title").withColor(-65536);
/*    */   
/* 15 */   private static final Component INCOMPATIBLE_CLIENT_VERSION = (Component)Component.literal(SharedConstants.getCurrentVersion().name()).withColor(-65536);
/*    */   
/* 16 */   private static final Component UNSUPPORTED_SNAPSHOT_VERSION = (Component)Component.translatable("mco.client.unsupported.snapshot.version", new Object[] { INCOMPATIBLE_CLIENT_VERSION });
/*    */   
/* 17 */   private static final Component OUTDATED_STABLE_VERSION = (Component)Component.translatable("mco.client.outdated.stable.version", new Object[] { INCOMPATIBLE_CLIENT_VERSION });
/*    */   
/*    */   private final Screen lastScreen;
/*    */   
/* 21 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout((Screen)this);
/*    */   
/*    */   public RealmsClientOutdatedScreen(Screen lastScreen) {
/* 24 */     super(INCOMPATIBLE_TITLE);
/* 25 */     this.lastScreen = lastScreen;
/*    */   }
/*    */   
/*    */   public void init() {
/* 30 */     this.layout.addTitleHeader(INCOMPATIBLE_TITLE, this.font);
/* 31 */     this.layout.addToContents((LayoutElement)(new MultiLineTextWidget(getErrorMessage(), this.font)).setCentered(true));
/* 32 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).width(200).build());
/* 33 */     RealmsClientOutdatedScreen realmsClientOutdatedScreen = this;
/* 33 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/* 34 */     repositionElements();
/*    */   }
/*    */   
/*    */   protected void repositionElements() {
/* 39 */     this.layout.arrangeElements();
/*    */   }
/*    */   
/*    */   public void onClose() {
/* 44 */     this.minecraft.gui.setScreen(this.lastScreen);
/*    */   }
/*    */   
/*    */   private Component getErrorMessage() {
/* 48 */     if (SharedConstants.getCurrentVersion().stable())
/* 49 */       return OUTDATED_STABLE_VERSION; 
/* 51 */     return UNSUPPORTED_SNAPSHOT_VERSION;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsClientOutdatedScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */