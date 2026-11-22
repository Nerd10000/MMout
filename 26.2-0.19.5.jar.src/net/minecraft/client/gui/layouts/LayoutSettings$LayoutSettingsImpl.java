/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ public class LayoutSettingsImpl implements LayoutSettings {
/*     */   public int paddingLeft;
/*     */   
/*     */   public int paddingTop;
/*     */   
/*     */   public int paddingRight;
/*     */   
/*     */   public int paddingBottom;
/*     */   
/*     */   public float xAlignment;
/*     */   
/*     */   public float yAlignment;
/*     */   
/*     */   public LayoutSettingsImpl() {}
/*     */   
/*     */   public LayoutSettingsImpl(LayoutSettingsImpl copy) {
/*  76 */     this.paddingLeft = copy.paddingLeft;
/*  77 */     this.paddingTop = copy.paddingTop;
/*  78 */     this.paddingRight = copy.paddingRight;
/*  79 */     this.paddingBottom = copy.paddingBottom;
/*  80 */     this.xAlignment = copy.xAlignment;
/*  81 */     this.yAlignment = copy.yAlignment;
/*     */   }
/*     */   
/*     */   public LayoutSettingsImpl padding(int padding) {
/*  86 */     return padding(padding, padding);
/*     */   }
/*     */   
/*     */   public LayoutSettingsImpl padding(int horizontal, int vertical) {
/*  91 */     return paddingHorizontal(horizontal).paddingVertical(vertical);
/*     */   }
/*     */   
/*     */   public LayoutSettingsImpl padding(int left, int top, int right, int bottom) {
/*  96 */     return paddingLeft(left)
/*  97 */       .paddingRight(right)
/*  98 */       .paddingTop(top)
/*  99 */       .paddingBottom(bottom);
/*     */   }
/*     */   
/*     */   public LayoutSettingsImpl paddingLeft(int padding) {
/* 104 */     this.paddingLeft = padding;
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public LayoutSettingsImpl paddingTop(int padding) {
/* 110 */     this.paddingTop = padding;
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public LayoutSettingsImpl paddingRight(int padding) {
/* 116 */     this.paddingRight = padding;
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   public LayoutSettingsImpl paddingBottom(int padding) {
/* 122 */     this.paddingBottom = padding;
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   public LayoutSettingsImpl paddingHorizontal(int padding) {
/* 128 */     return paddingLeft(padding).paddingRight(padding);
/*     */   }
/*     */   
/*     */   public LayoutSettingsImpl paddingVertical(int padding) {
/* 133 */     return paddingTop(padding).paddingBottom(padding);
/*     */   }
/*     */   
/*     */   public LayoutSettingsImpl align(float xAlignment, float yAlignment) {
/* 138 */     this.xAlignment = xAlignment;
/* 139 */     this.yAlignment = yAlignment;
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public LayoutSettingsImpl alignHorizontally(float xAlignment) {
/* 145 */     this.xAlignment = xAlignment;
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public LayoutSettingsImpl alignVertically(float yAlignment) {
/* 151 */     this.yAlignment = yAlignment;
/* 152 */     return this;
/*     */   }
/*     */   
/*     */   public LayoutSettingsImpl copy() {
/* 157 */     return new LayoutSettingsImpl(this);
/*     */   }
/*     */   
/*     */   public LayoutSettingsImpl getExposed() {
/* 162 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\layouts\LayoutSettings$LayoutSettingsImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */