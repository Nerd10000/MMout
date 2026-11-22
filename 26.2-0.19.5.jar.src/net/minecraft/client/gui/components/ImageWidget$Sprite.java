/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ class Sprite extends ImageWidget {
/*    */   private Identifier sprite;
/*    */   
/*    */   public Sprite(int x, int y, int width, int height, Identifier sprite) {
/* 50 */     super(x, y, width, height);
/* 51 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 56 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX(), getY(), getWidth(), getHeight());
/*    */   }
/*    */   
/*    */   public void updateResource(Identifier identifier) {
/* 61 */     this.sprite = identifier;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ImageWidget$Sprite.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */