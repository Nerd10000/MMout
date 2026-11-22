/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ImageButton extends Button {
/*    */   protected final WidgetSprites sprites;
/*    */   
/*    */   public ImageButton(int x, int y, int width, int height, WidgetSprites sprites, Button.OnPress onPress) {
/* 13 */     this(x, y, width, height, sprites, onPress, CommonComponents.EMPTY);
/*    */   }
/*    */   
/*    */   public ImageButton(int width, int height, WidgetSprites sprites, Button.OnPress onPress, Component message) {
/* 17 */     this(0, 0, width, height, sprites, onPress, message);
/*    */   }
/*    */   
/*    */   public ImageButton(int x, int y, int width, int height, WidgetSprites sprites, Button.OnPress onPress, Component message) {
/* 21 */     super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
/* 22 */     this.sprites = sprites;
/*    */   }
/*    */   
/*    */   public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 27 */     Identifier sprite = this.sprites.get(isActive(), isHoveredOrFocused());
/* 28 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), this.width, this.height);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ImageButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */