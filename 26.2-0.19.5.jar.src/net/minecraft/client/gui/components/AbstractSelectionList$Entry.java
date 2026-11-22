/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ 
/*     */ public abstract class Entry<E extends AbstractSelectionList.Entry<E>> implements LayoutElement, GuiEventListener {
/*     */   public static final int CONTENT_PADDING = 2;
/*     */   
/* 422 */   private int x = 0;
/*     */   
/* 423 */   private int y = 0;
/*     */   
/* 424 */   private int width = 0;
/*     */   
/*     */   private int height;
/*     */   
/*     */   @Deprecated
/*     */   private AbstractSelectionList<E> list;
/*     */   
/*     */   public void setFocused(boolean focused) {}
/*     */   
/*     */   public boolean isFocused() {
/* 437 */     return (this.list.getFocused() == this);
/*     */   }
/*     */   
/*     */   public abstract void extractContent(GuiGraphicsExtractor paramGuiGraphicsExtractor, int paramInt1, int paramInt2, boolean paramBoolean, float paramFloat);
/*     */   
/*     */   public boolean isMouseOver(double mx, double my) {
/* 444 */     return getRectangle().containsPoint((int)mx, (int)my);
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/* 449 */     this.x = x;
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/* 454 */     this.y = y;
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/* 458 */     this.width = width;
/*     */   }
/*     */   
/*     */   public void setHeight(int height) {
/* 462 */     this.height = height;
/*     */   }
/*     */   
/*     */   public int getContentX() {
/* 466 */     return getX() + 2;
/*     */   }
/*     */   
/*     */   public int getContentY() {
/* 470 */     return getY() + 2;
/*     */   }
/*     */   
/*     */   public int getContentHeight() {
/* 474 */     return getHeight() - 4;
/*     */   }
/*     */   
/*     */   public int getContentYMiddle() {
/* 478 */     return getContentY() + getContentHeight() / 2;
/*     */   }
/*     */   
/*     */   public int getContentBottom() {
/* 482 */     return getContentY() + getContentHeight();
/*     */   }
/*     */   
/*     */   public int getContentWidth() {
/* 486 */     return getWidth() - 4;
/*     */   }
/*     */   
/*     */   public int getContentXMiddle() {
/* 490 */     return getContentX() + getContentWidth() / 2;
/*     */   }
/*     */   
/*     */   public int getContentRight() {
/* 494 */     return getContentX() + getContentWidth();
/*     */   }
/*     */   
/*     */   public int getX() {
/* 499 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getY() {
/* 504 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 509 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 514 */     return this.height;
/*     */   }
/*     */   
/*     */   public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {}
/*     */   
/*     */   public ScreenRectangle getRectangle() {
/* 523 */     return super.getRectangle();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\AbstractSelectionList$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */