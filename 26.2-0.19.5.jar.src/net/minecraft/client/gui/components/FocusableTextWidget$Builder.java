/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class Builder {
/*     */   private final Component message;
/*     */   
/*     */   private final Font font;
/*     */   
/*     */   private final int padding;
/*     */   
/* 158 */   private int maxWidth = -1;
/*     */   
/*     */   private boolean alwaysShowBorder = true;
/*     */   
/* 160 */   private FocusableTextWidget.BackgroundFill backgroundFill = FocusableTextWidget.BackgroundFill.ALWAYS;
/*     */   
/*     */   private Builder(Component message, Font font) {
/* 163 */     this(message, font, 4);
/*     */   }
/*     */   
/*     */   private Builder(Component message, Font font, int padding) {
/* 167 */     this.message = message;
/* 168 */     this.font = font;
/* 169 */     this.padding = padding;
/*     */   }
/*     */   
/*     */   public Builder maxWidth(int maxWidth) {
/* 173 */     this.maxWidth = maxWidth;
/* 174 */     return this;
/*     */   }
/*     */   
/*     */   public Builder textWidth(int textWidth) {
/* 178 */     this.maxWidth = textWidth + this.padding * 2;
/* 179 */     return this;
/*     */   }
/*     */   
/*     */   public Builder alwaysShowBorder(boolean alwaysShowBorder) {
/* 183 */     this.alwaysShowBorder = alwaysShowBorder;
/* 184 */     return this;
/*     */   }
/*     */   
/*     */   public Builder backgroundFill(FocusableTextWidget.BackgroundFill backgroundFill) {
/* 188 */     this.backgroundFill = backgroundFill;
/* 189 */     return this;
/*     */   }
/*     */   
/*     */   public FocusableTextWidget build() {
/* 193 */     return new FocusableTextWidget(this.message, this.font, this.padding, this.maxWidth, this.backgroundFill, this.alwaysShowBorder);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\FocusableTextWidget$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */