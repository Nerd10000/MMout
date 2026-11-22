/*     */ package com.mojang.realmsclient;
/*     */ 
/*     */ import com.mojang.realmsclient.dto.RealmsNotification;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ImageWidget;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.SpacerElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class NotificationMessageEntry extends RealmsMainScreen.Entry {
/*     */   private static final int SIDE_MARGINS = 40;
/*     */   
/*     */   public static final int PADDING = 7;
/*     */   
/*     */   public static final int HEIGHT_WITHOUT_TEXT = 38;
/*     */   
/*     */   private final Component text;
/*     */   
/* 911 */   private final List<AbstractWidget> children = new ArrayList<>();
/*     */   
/*     */   private final RealmsMainScreen.CrossButton dismissButton;
/*     */   
/*     */   private final MultiLineTextWidget textWidget;
/*     */   
/*     */   private final GridLayout gridLayout;
/*     */   
/*     */   private final FrameLayout textFrame;
/*     */   
/*     */   private final Button button;
/*     */   
/* 917 */   private int lastEntryWidth = -1;
/*     */   
/*     */   public NotificationMessageEntry(RealmsMainScreen paramRealmsMainScreen1, RealmsMainScreen realmsMainScreen, int messageHeight, Component text, RealmsNotification.VisitUrl notification) {
/* 919 */     super(paramRealmsMainScreen1);
/* 920 */     this.text = text;
/* 921 */     this.gridLayout = new GridLayout();
/* 922 */     this.gridLayout.addChild((LayoutElement)ImageWidget.sprite(20, 20, RealmsMainScreen.INFO_SPRITE), 0, 0, this.gridLayout.newCellSettings().padding(7, 7, 0, 0));
/* 923 */     this.gridLayout.addChild((LayoutElement)SpacerElement.width(40), 0, 0);
/* 924 */     this.textFrame = (FrameLayout)this.gridLayout.addChild((LayoutElement)new FrameLayout(0, messageHeight), 0, 1, this.gridLayout.newCellSettings().paddingTop(7));
/* 925 */     this.textWidget = (MultiLineTextWidget)this.textFrame.addChild((LayoutElement)(new MultiLineTextWidget(text, RealmsMainScreen.access$1700(paramRealmsMainScreen1))).setCentered(true), this.textFrame.newChildLayoutSettings().alignHorizontallyCenter().alignVerticallyTop());
/* 926 */     this.gridLayout.addChild((LayoutElement)SpacerElement.width(40), 0, 2);
/* 927 */     if (notification.dismissable()) {
/* 928 */       this.dismissButton = (RealmsMainScreen.CrossButton)this.gridLayout.addChild((LayoutElement)new RealmsMainScreen.CrossButton(b -> this$0.dismissNotification(notification.uuid()), (Component)Component.translatable("mco.notification.dismiss")), 0, 2, this.gridLayout.newCellSettings().alignHorizontallyRight().padding(0, 7, 7, 0));
/*     */     } else {
/* 930 */       this.dismissButton = null;
/*     */     } 
/* 932 */     this.button = (Button)this.gridLayout.addChild((LayoutElement)notification.buildOpenLinkButton((Screen)realmsMainScreen), 1, 1, this.gridLayout.newCellSettings().alignHorizontallyCenter().padding(4));
/* 933 */     NotificationMessageEntry notificationMessageEntry = this;
/* 933 */     this.button.setOverrideRenderHighlightedSprite(() -> Boolean.valueOf(rec$.isFocused()));
/* 934 */     Objects.requireNonNull(this.children);
/* 934 */     this.gridLayout.visitWidgets(this.children::add);
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 939 */     if (this.button.keyPressed(event))
/* 940 */       return true; 
/* 942 */     if (this.dismissButton != null && this.dismissButton.keyPressed(event))
/* 943 */       return true; 
/* 945 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   private void updateEntryWidth() {
/* 949 */     int entryWidth = getWidth();
/* 950 */     if (this.lastEntryWidth != entryWidth) {
/* 951 */       refreshLayout(entryWidth);
/* 952 */       this.lastEntryWidth = entryWidth;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void refreshLayout(int entryWidth) {
/* 957 */     int width = textWidth(entryWidth);
/* 958 */     this.textFrame.setMinWidth(width);
/* 959 */     this.textWidget.setMaxWidth(width);
/* 960 */     this.gridLayout.arrangeElements();
/*     */   }
/*     */   
/*     */   public static int textWidth(int rowWidth) {
/* 964 */     return rowWidth - 80;
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 969 */     this.gridLayout.setPosition(getContentX(), getContentY());
/* 970 */     updateEntryWidth();
/* 971 */     this.children.forEach(child -> child.extractRenderState(graphics, mouseX, mouseY, a));
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 976 */     if (this.dismissButton != null && this.dismissButton.mouseClicked(event, doubleClick))
/* 977 */       return true; 
/* 979 */     if (this.button.mouseClicked(event, doubleClick))
/* 980 */       return true; 
/* 983 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   public Component getText() {
/* 987 */     return this.text;
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 992 */     return getText();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\RealmsMainScreen$NotificationMessageEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */