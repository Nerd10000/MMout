/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class LinearLayout implements Layout {
/*     */   private final GridLayout wrapped;
/*     */   
/*     */   private final Orientation orientation;
/*     */   
/*  11 */   private int nextChildIndex = 0;
/*     */   
/*     */   private LinearLayout(Orientation orientation) {
/*  14 */     this(0, 0, orientation);
/*     */   }
/*     */   
/*     */   public LinearLayout(int x, int y, Orientation orientation) {
/*  18 */     this.wrapped = new GridLayout(x, y);
/*  19 */     this.orientation = orientation;
/*     */   }
/*     */   
/*     */   public LinearLayout spacing(int spacing) {
/*  23 */     this.orientation.setSpacing(this.wrapped, spacing);
/*  24 */     return this;
/*     */   }
/*     */   
/*     */   public LayoutSettings newCellSettings() {
/*  28 */     return this.wrapped.newCellSettings();
/*     */   }
/*     */   
/*     */   public LayoutSettings defaultCellSetting() {
/*  32 */     return this.wrapped.defaultCellSetting();
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, LayoutSettings cellSettings) {
/*  36 */     return this.orientation.addChild(this.wrapped, child, this.nextChildIndex++, cellSettings);
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child) {
/*  40 */     return addChild(child, newCellSettings());
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/*  44 */     return this.orientation.addChild(this.wrapped, child, this.nextChildIndex++, (LayoutSettings)Util.make(newCellSettings(), layoutSettingsAdjustments));
/*     */   }
/*     */   
/*     */   public void visitChildren(Consumer<LayoutElement> layoutElementVisitor) {
/*  49 */     this.wrapped.visitChildren(layoutElementVisitor);
/*     */   }
/*     */   
/*     */   public void removeChildren() {
/*  54 */     this.wrapped.removeChildren();
/*  55 */     this.nextChildIndex = 0;
/*     */   }
/*     */   
/*     */   public void arrangeElements() {
/*  60 */     this.wrapped.arrangeElements();
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  65 */     return this.wrapped.getWidth();
/*     */   }
/*     */   
/*     */   public int getHeight() {
/*  70 */     return this.wrapped.getHeight();
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/*  75 */     this.wrapped.setX(x);
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/*  80 */     this.wrapped.setY(y);
/*     */   }
/*     */   
/*     */   public int getX() {
/*  85 */     return this.wrapped.getX();
/*     */   }
/*     */   
/*     */   public int getY() {
/*  90 */     return this.wrapped.getY();
/*     */   }
/*     */   
/*     */   public static LinearLayout vertical() {
/*  94 */     return new LinearLayout(Orientation.VERTICAL);
/*     */   }
/*     */   
/*     */   public static LinearLayout horizontal() {
/*  98 */     return new LinearLayout(Orientation.HORIZONTAL);
/*     */   }
/*     */   
/*     */   public enum Orientation {
/* 102 */     HORIZONTAL, VERTICAL;
/*     */     
/*     */     private void setSpacing(GridLayout gridLayout, int spacing) {
/* 105 */       switch (ordinal()) {
/*     */         case 0:
/* 106 */           gridLayout.columnSpacing(spacing);
/*     */           break;
/*     */         case 1:
/* 107 */           gridLayout.rowSpacing(spacing);
/*     */           break;
/*     */       } 
/*     */     }
/*     */     
/*     */     public <T extends LayoutElement> T addChild(GridLayout gridLayout, T child, int index, LayoutSettings cellSettings) {
/* 112 */       switch (ordinal()) {
/*     */         default:
/* 112 */           throw new MatchException(null, null);
/*     */         case 0:
/*     */         
/*     */         case 1:
/*     */           break;
/*     */       } 
/* 112 */       return 
/*     */         
/* 114 */         gridLayout.addChild(child, index, 0, cellSettings);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\layouts\LinearLayout.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */