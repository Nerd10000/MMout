/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class TextAndIcon extends SpriteIconButton {
/*     */   protected TextAndIcon(int width, int height, Component message, int spriteWidth, int spriteHeight, int spriteOffsetX, int spriteOffsetY, WidgetSprites sprite, Button.OnPress onPress, Component tooltip, Button.CreateNarration narration, boolean switchToLoadingAfterPress) {
/* 127 */     super(width, height, message, spriteWidth, spriteHeight, spriteOffsetX, spriteOffsetY, sprite, onPress, tooltip, narration, switchToLoadingAfterPress);
/*     */   }
/*     */   
/*     */   public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 132 */     if (extractLoadingStateIfLoading(graphics))
/*     */       return; 
/* 135 */     extractDefaultSprite(graphics);
/* 137 */     int left = getX() + 2;
/* 138 */     int right = getX() + getWidth() - this.spriteWidth - 4;
/* 140 */     int centerX = getX() + getWidth() / 2;
/* 141 */     ActiveTextCollector output = graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE);
/* 142 */     output.acceptScrolling(getMessage(), centerX, left, right, getY(), getY() + getHeight());
/* 144 */     int x = this.spriteOffsetX + getX() + getWidth() - this.spriteWidth - 2;
/* 145 */     int y = this.spriteOffsetY + getY() + getHeight() / 2 - this.spriteHeight / 2;
/* 146 */     extractSprite(graphics, x, y);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\SpriteIconButton$TextAndIcon.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */