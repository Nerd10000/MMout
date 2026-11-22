/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class Builder {
/*     */   private final Component message;
/*     */   
/*     */   private final Button.OnPress onPress;
/*     */   
/*     */   private final boolean iconOnly;
/*     */   
/* 155 */   private int width = 150;
/*     */   
/* 156 */   private int height = 20;
/*     */   
/*     */   private WidgetSprites sprite;
/*     */   
/*     */   private int spriteWidth;
/*     */   
/*     */   private int spriteHeight;
/*     */   
/*     */   private int spriteOffsetX;
/*     */   
/*     */   private int spriteOffsetY;
/*     */   
/*     */   private Component tooltip;
/*     */   
/*     */   private Button.CreateNarration narration;
/*     */   
/*     */   private boolean switchToLoadingAfterPress;
/*     */   
/*     */   private Builder(Component message, Button.OnPress onPress, boolean iconOnly) {
/* 168 */     this.message = message;
/* 169 */     this.onPress = onPress;
/* 170 */     this.iconOnly = iconOnly;
/*     */   }
/*     */   
/*     */   public Builder width(int width) {
/* 174 */     this.width = width;
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public Builder size(int width, int height) {
/* 179 */     this.width = width;
/* 180 */     this.height = height;
/* 181 */     return this;
/*     */   }
/*     */   
/*     */   public Builder sprite(Identifier sprite, int spriteWidth, int spriteHeight) {
/* 185 */     this.sprite = new WidgetSprites(sprite);
/* 186 */     this.spriteWidth = spriteWidth;
/* 187 */     this.spriteHeight = spriteHeight;
/* 188 */     return this;
/*     */   }
/*     */   
/*     */   public Builder sprite(WidgetSprites sprite, int spriteWidth, int spriteHeight) {
/* 191 */     this.sprite = sprite;
/* 192 */     this.spriteWidth = spriteWidth;
/* 193 */     this.spriteHeight = spriteHeight;
/* 194 */     return this;
/*     */   }
/*     */   
/*     */   public Builder spriteOffset(int spriteOffsetX, int spriteOffsetY) {
/* 198 */     this.spriteOffsetX = spriteOffsetX;
/* 199 */     this.spriteOffsetY = spriteOffsetY;
/* 200 */     return this;
/*     */   }
/*     */   
/*     */   public Builder tooltip(Component tooltip) {
/* 204 */     this.tooltip = tooltip;
/* 205 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withTootip() {
/* 209 */     this.tooltip = this.message;
/* 210 */     return this;
/*     */   }
/*     */   
/*     */   public Builder narration(Button.CreateNarration narration) {
/* 214 */     this.narration = narration;
/* 215 */     return this;
/*     */   }
/*     */   
/*     */   public Builder switchToLoadingAfterPress() {
/* 219 */     this.switchToLoadingAfterPress = true;
/* 220 */     return this;
/*     */   }
/*     */   
/*     */   public SpriteIconButton build() {
/* 224 */     if (this.sprite == null)
/* 225 */       throw new IllegalStateException("Sprite not set"); 
/* 227 */     if (this.iconOnly)
/* 228 */       return new SpriteIconButton.CenteredIcon(this.width, this.height, this.message, this.spriteWidth, this.spriteHeight, this.spriteOffsetX, this.spriteOffsetY, this.sprite, this.onPress, this.tooltip, this.narration, this.switchToLoadingAfterPress); 
/* 230 */     return new SpriteIconButton.TextAndIcon(this.width, this.height, this.message, this.spriteWidth, this.spriteHeight, this.spriteOffsetX, this.spriteOffsetY, this.sprite, this.onPress, this.tooltip, this.narration, this.switchToLoadingAfterPress);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\SpriteIconButton$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */