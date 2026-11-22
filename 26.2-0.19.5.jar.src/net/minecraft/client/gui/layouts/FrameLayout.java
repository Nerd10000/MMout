/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class FrameLayout extends AbstractLayout {
/*  18 */   private final List<ChildContainer> children = new ArrayList<>();
/*     */   
/*     */   private int minWidth;
/*     */   
/*     */   private int minHeight;
/*     */   
/*  22 */   private final LayoutSettings defaultChildLayoutSettings = LayoutSettings.defaults().align(0.5F, 0.5F);
/*     */   
/*     */   public FrameLayout() {
/*  25 */     this(0, 0, 0, 0);
/*     */   }
/*     */   
/*     */   public FrameLayout(int minWidth, int minHeight) {
/*  29 */     this(0, 0, minWidth, minHeight);
/*     */   }
/*     */   
/*     */   public FrameLayout(int x, int y, int minWidth, int minHeight) {
/*  33 */     super(x, y, minWidth, minHeight);
/*  34 */     setMinDimensions(minWidth, minHeight);
/*     */   }
/*     */   
/*     */   public FrameLayout setMinDimensions(int minWidth, int minHeight) {
/*  38 */     return setMinWidth(minWidth).setMinHeight(minHeight);
/*     */   }
/*     */   
/*     */   public FrameLayout setMinHeight(int minHeight) {
/*  42 */     this.minHeight = minHeight;
/*  43 */     return this;
/*     */   }
/*     */   
/*     */   public FrameLayout setMinWidth(int minWidth) {
/*  47 */     this.minWidth = minWidth;
/*  48 */     return this;
/*     */   }
/*     */   
/*     */   public LayoutSettings newChildLayoutSettings() {
/*  52 */     return this.defaultChildLayoutSettings.copy();
/*     */   }
/*     */   
/*     */   public LayoutSettings defaultChildLayoutSetting() {
/*  56 */     return this.defaultChildLayoutSettings;
/*     */   }
/*     */   
/*     */   public void arrangeElements() {
/*  61 */     super.arrangeElements();
/*  63 */     int resultWidth = this.minWidth;
/*  64 */     int resultHeight = this.minHeight;
/*  66 */     for (ChildContainer child : this.children) {
/*  67 */       resultWidth = Math.max(resultWidth, child.getWidth());
/*  68 */       resultHeight = Math.max(resultHeight, child.getHeight());
/*     */     } 
/*  71 */     for (ChildContainer child : this.children) {
/*  72 */       child.setX(getX(), resultWidth);
/*  73 */       child.setY(getY(), resultHeight);
/*     */     } 
/*  76 */     this.width = resultWidth;
/*  77 */     this.height = resultHeight;
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child) {
/*  81 */     return addChild(child, newChildLayoutSettings());
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, LayoutSettings childLayoutSettings) {
/*  85 */     this.children.add(new ChildContainer((LayoutElement)child, childLayoutSettings));
/*  86 */     return child;
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/*  90 */     return addChild(child, (LayoutSettings)Util.make(newChildLayoutSettings(), layoutSettingsAdjustments));
/*     */   }
/*     */   
/*     */   public void visitChildren(Consumer<LayoutElement> layoutElementVisitor) {
/*  95 */     this.children.forEach(wrapper -> layoutElementVisitor.accept(wrapper.child));
/*     */   }
/*     */   
/*     */   public void removeChildren() {
/* 100 */     this.children.clear();
/*     */   }
/*     */   
/*     */   public static void centerInRectangle(LayoutElement widget, int x, int y, int width, int height) {
/* 104 */     alignInRectangle(widget, x, y, width, height, 0.5F, 0.5F);
/*     */   }
/*     */   
/*     */   public static void centerInRectangle(LayoutElement widget, ScreenRectangle rectangle) {
/* 108 */     centerInRectangle(widget, rectangle.position().x(), rectangle.position().y(), rectangle.width(), rectangle.height());
/*     */   }
/*     */   
/*     */   public static void alignInRectangle(LayoutElement widget, ScreenRectangle rectangle, float alignX, float alignY) {
/* 112 */     alignInRectangle(widget, rectangle.left(), rectangle.top(), rectangle.width(), rectangle.height(), alignX, alignY);
/*     */   }
/*     */   
/*     */   public static void alignInRectangle(LayoutElement widget, int x, int y, int width, int height, float alignX, float alignY) {
/* 123 */     Objects.requireNonNull(widget);
/* 123 */     alignInDimension(x, width, widget.getWidth(), widget::setX, alignX);
/* 124 */     Objects.requireNonNull(widget);
/* 124 */     alignInDimension(y, height, widget.getHeight(), widget::setY, alignY);
/*     */   }
/*     */   
/*     */   public static void alignInDimension(int pos, int length, int widgetLength, Consumer<Integer> setWidgetPos, float align) {
/* 128 */     int offset = (int)Mth.lerp(align, 0.0F, (length - widgetLength));
/* 129 */     setWidgetPos.accept(Integer.valueOf(pos + offset));
/*     */   }
/*     */   
/*     */   private static class ChildContainer extends AbstractLayout.AbstractChildWrapper {
/*     */     protected ChildContainer(LayoutElement child, LayoutSettings layoutSettings) {
/* 134 */       super(child, layoutSettings);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\layouts\FrameLayout.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */