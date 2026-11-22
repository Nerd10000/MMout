/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class CenteredIcon extends SpriteIconButton {
/*     */   protected CenteredIcon(int width, int height, Component message, int spriteWidth, int spriteHeight, int spriteOffsetX, int spriteOffsetY, WidgetSprites sprite, Button.OnPress onPress, Component tooltip, Button.CreateNarration narration, boolean switchToLoadingAfterPress) {
/* 104 */     super(width, height, message, spriteWidth, spriteHeight, spriteOffsetX, spriteOffsetY, sprite, onPress, tooltip, narration, switchToLoadingAfterPress);
/*     */   }
/*     */   
/*     */   public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 109 */     if (extractLoadingStateIfLoading(graphics))
/*     */       return; 
/* 112 */     extractDefaultSprite(graphics);
/* 114 */     int x = this.spriteOffsetX + getX() + getWidth() / 2 - this.spriteWidth / 2;
/* 115 */     int y = this.spriteOffsetY + getY() + getHeight() / 2 - this.spriteHeight / 2;
/* 116 */     extractSprite(graphics, x, y);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\SpriteIconButton$CenteredIcon.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */