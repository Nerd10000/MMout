/*      */ package com.mojang.realmsclient;
/*      */ 
/*      */ import com.mojang.realmsclient.dto.RealmsServer;
/*      */ import com.mojang.realmsclient.gui.screens.RealmsCreateRealmScreen;
/*      */ import java.util.Objects;
/*      */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*      */ import net.minecraft.client.gui.components.PopupScreen;
/*      */ import net.minecraft.client.gui.components.Tooltip;
/*      */ import net.minecraft.client.gui.components.WidgetTooltipHolder;
/*      */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*      */ import net.minecraft.client.gui.screens.Screen;
/*      */ import net.minecraft.client.input.KeyEvent;
/*      */ import net.minecraft.client.input.MouseButtonEvent;
/*      */ import net.minecraft.client.renderer.RenderPipelines;
/*      */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SoundInstance;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ 
/*      */ class AvailableSnapshotEntry extends RealmsMainScreen.Entry {
/*  997 */   private static final Component START_SNAPSHOT_REALM = (Component)Component.translatable("mco.snapshot.start");
/*      */   
/*      */   private static final int TEXT_PADDING = 5;
/*      */   
/* 1000 */   private final WidgetTooltipHolder tooltip = new WidgetTooltipHolder();
/*      */   
/*      */   private final RealmsServer parent;
/*      */   
/*      */   public AvailableSnapshotEntry(RealmsServer parent) {
/* 1004 */     this.parent = parent;
/* 1005 */     this.tooltip.set(Tooltip.create((Component)Component.translatable("mco.snapshot.tooltip")));
/*      */   }
/*      */   
/*      */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 1010 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsMainScreen.NEW_REALM_SPRITE, getContentX() - 5, getContentYMiddle() - 10, 40, 20);
/* 1012 */     Objects.requireNonNull(RealmsMainScreen.access$1800(RealmsMainScreen.this));
/* 1012 */     int textYPos = getContentYMiddle() - 9 / 2;
/* 1013 */     graphics.text(RealmsMainScreen.access$1900(RealmsMainScreen.this), START_SNAPSHOT_REALM, getContentX() + 40 - 2, textYPos - 5, -8388737);
/* 1014 */     graphics.text(RealmsMainScreen.access$2000(RealmsMainScreen.this), (Component)Component.translatable("mco.snapshot.description", new Object[] { Objects.requireNonNullElse(this.parent.name, "unknown server") }), getContentX() + 40 - 2, textYPos + 5, -8355712);
/* 1015 */     this.tooltip.refreshTooltipForNextRenderPass(graphics, mouseX, mouseY, hovered, (isFocused() && (RealmsMainScreen.access$2100(RealmsMainScreen.this)).gui.screen() == RealmsMainScreen.this), new ScreenRectangle(getContentX(), getContentY(), getContentWidth(), getContentHeight()));
/*      */   }
/*      */   
/*      */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 1020 */     addSnapshotRealm();
/* 1021 */     return true;
/*      */   }
/*      */   
/*      */   public boolean keyPressed(KeyEvent event) {
/* 1026 */     if (event.isSelection()) {
/* 1027 */       addSnapshotRealm();
/* 1028 */       return false;
/*      */     } 
/* 1030 */     return super.keyPressed(event);
/*      */   }
/*      */   
/*      */   private void addSnapshotRealm() {
/* 1034 */     RealmsMainScreen.access$2200(RealmsMainScreen.this).getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/* 1035 */     (RealmsMainScreen.access$2400(RealmsMainScreen.this)).gui.setScreen((Screen)(new PopupScreen.Builder((Screen)RealmsMainScreen.this, (Component)Component.translatable("mco.snapshot.createSnapshotPopup.title")))
/* 1036 */         .addMessage((Component)Component.translatable("mco.snapshot.createSnapshotPopup.text"))
/* 1037 */         .addButton((Component)Component.translatable("mco.selectServer.create"), paramPopupScreen -> (RealmsMainScreen.access$2300(RealmsMainScreen.this)).gui.setScreen((Screen)new RealmsCreateRealmScreen(RealmsMainScreen.this, this.parent, true)))
/*      */         
/* 1039 */         .addButton(CommonComponents.GUI_CANCEL, PopupScreen::onClose)
/* 1040 */         .build());
/*      */   }
/*      */   
/*      */   public Component getNarration() {
/* 1045 */     return (Component)Component.translatable("gui.narrate.button", new Object[] { CommonComponents.joinForNarration(new Component[] { START_SNAPSHOT_REALM, 
/* 1046 */               (Component)Component.translatable("mco.snapshot.description", new Object[] { Objects.requireNonNullElse(this.parent.name, "unknown server") }) }) });
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\RealmsMainScreen$AvailableSnapshotEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */