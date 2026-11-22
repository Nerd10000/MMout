/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class Builder {
/*     */   private int x;
/*     */   
/*     */   private int y;
/*     */   
/* 269 */   private Component placeholder = CommonComponents.EMPTY;
/*     */   
/* 270 */   private int textColor = -2039584;
/*     */   
/*     */   private boolean textShadow = true;
/*     */   
/* 272 */   private int cursorColor = -3092272;
/*     */   
/*     */   private boolean showBackground = true;
/*     */   
/*     */   private boolean showDecorations = true;
/*     */   
/*     */   public Builder setX(int x) {
/* 280 */     this.x = x;
/* 281 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setY(int y) {
/* 285 */     this.y = y;
/* 286 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setPlaceholder(Component placeholder) {
/* 290 */     this.placeholder = placeholder;
/* 291 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setTextColor(int textColor) {
/* 295 */     this.textColor = textColor;
/* 296 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setTextShadow(boolean textShadow) {
/* 300 */     this.textShadow = textShadow;
/* 301 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setCursorColor(int cursorColor) {
/* 305 */     this.cursorColor = cursorColor;
/* 306 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setShowBackground(boolean showBackground) {
/* 310 */     this.showBackground = showBackground;
/* 311 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setShowDecorations(boolean showDecorations) {
/* 315 */     this.showDecorations = showDecorations;
/* 316 */     return this;
/*     */   }
/*     */   
/*     */   public MultiLineEditBox build(Font font, int width, int height, Component narration) {
/* 320 */     return new MultiLineEditBox(font, this.x, this.y, width, height, this.placeholder, narration, this.textColor, this.textShadow, this.cursorColor, this.showBackground, this.showDecorations);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\MultiLineEditBox$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */