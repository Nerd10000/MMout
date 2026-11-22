/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class HeaderAndFooterLayout implements Layout {
/*     */   public static final int MAGIC_PADDING = 13;
/*     */   
/*     */   public static final int DEFAULT_HEADER_AND_FOOTER_HEIGHT = 33;
/*     */   
/*     */   private static final int CONTENT_MARGIN_TOP = 30;
/*     */   
/*  16 */   private final FrameLayout headerFrame = new FrameLayout();
/*     */   
/*  17 */   private final FrameLayout footerFrame = new FrameLayout();
/*     */   
/*  18 */   private final FrameLayout contentsFrame = new FrameLayout();
/*     */   
/*     */   private final Screen screen;
/*     */   
/*     */   private int headerHeight;
/*     */   
/*     */   private int footerHeight;
/*     */   
/*     */   public HeaderAndFooterLayout(Screen screen) {
/*  25 */     this(screen, 33);
/*     */   }
/*     */   
/*     */   public HeaderAndFooterLayout(Screen screen, int headerAndFooterHeight) {
/*  29 */     this(screen, headerAndFooterHeight, headerAndFooterHeight);
/*     */   }
/*     */   
/*     */   public HeaderAndFooterLayout(Screen screen, int headerHeight, int footerHeight) {
/*  33 */     this.screen = screen;
/*  34 */     this.headerHeight = headerHeight;
/*  35 */     this.footerHeight = footerHeight;
/*  36 */     this.headerFrame.defaultChildLayoutSetting().align(0.5F, 0.5F);
/*  37 */     this.footerFrame.defaultChildLayoutSetting().align(0.5F, 0.5F);
/*     */   }
/*     */   
/*     */   public void setX(int x) {}
/*     */   
/*     */   public void setY(int y) {}
/*     */   
/*     */   public int getX() {
/*  52 */     return 0;
/*     */   }
/*     */   
/*     */   public int getY() {
/*  57 */     return 0;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  62 */     return this.screen.width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/*  67 */     return this.screen.height;
/*     */   }
/*     */   
/*     */   public int getFooterHeight() {
/*  71 */     return this.footerHeight;
/*     */   }
/*     */   
/*     */   public void setFooterHeight(int footerHeight) {
/*  75 */     this.footerHeight = footerHeight;
/*     */   }
/*     */   
/*     */   public void setHeaderHeight(int headerHeight) {
/*  79 */     this.headerHeight = headerHeight;
/*     */   }
/*     */   
/*     */   public int getHeaderHeight() {
/*  83 */     return this.headerHeight;
/*     */   }
/*     */   
/*     */   public int getContentHeight() {
/*  87 */     return this.screen.height - getHeaderHeight() - getFooterHeight();
/*     */   }
/*     */   
/*     */   public void visitChildren(Consumer<LayoutElement> layoutElementVisitor) {
/*  92 */     this.headerFrame.visitChildren(layoutElementVisitor);
/*  93 */     this.contentsFrame.visitChildren(layoutElementVisitor);
/*  94 */     this.footerFrame.visitChildren(layoutElementVisitor);
/*     */   }
/*     */   
/*     */   public void removeChildren() {
/*  99 */     this.headerFrame.removeChildren();
/* 100 */     this.contentsFrame.removeChildren();
/* 101 */     this.footerFrame.removeChildren();
/*     */   }
/*     */   
/*     */   public void arrangeElements() {
/* 106 */     int headerHeight = getHeaderHeight();
/* 107 */     int footerHeight = getFooterHeight();
/* 109 */     this.headerFrame.setMinWidth(this.screen.width);
/* 110 */     this.headerFrame.setMinHeight(headerHeight);
/* 111 */     this.headerFrame.setPosition(0, 0);
/* 112 */     this.headerFrame.arrangeElements();
/* 114 */     this.footerFrame.setMinWidth(this.screen.width);
/* 115 */     this.footerFrame.setMinHeight(footerHeight);
/* 116 */     this.footerFrame.arrangeElements();
/* 117 */     this.footerFrame.setY(this.screen.height - footerHeight);
/* 119 */     this.contentsFrame.setMinWidth(this.screen.width);
/* 120 */     this.contentsFrame.arrangeElements();
/* 122 */     int preferredContentY = headerHeight + 30;
/* 123 */     int maxContentY = this.screen.height - footerHeight - this.contentsFrame.getHeight();
/* 124 */     this.contentsFrame.setPosition(0, Math.min(preferredContentY, maxContentY));
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addToHeader(T child) {
/* 128 */     return this.headerFrame.addChild(child);
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addToHeader(T child, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/* 132 */     return this.headerFrame.addChild(child, layoutSettingsAdjustments);
/*     */   }
/*     */   
/*     */   public void addTitleHeader(Component component, Font font) {
/* 136 */     this.headerFrame.addChild(new StringWidget(component, font));
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addToFooter(T child) {
/* 140 */     return this.footerFrame.addChild(child);
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addToFooter(T child, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/* 144 */     return this.footerFrame.addChild(child, layoutSettingsAdjustments);
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addToContents(T child) {
/* 148 */     return this.contentsFrame.addChild(child);
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addToContents(T child, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/* 152 */     return this.contentsFrame.addChild(child, layoutSettingsAdjustments);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\layouts\HeaderAndFooterLayout.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */