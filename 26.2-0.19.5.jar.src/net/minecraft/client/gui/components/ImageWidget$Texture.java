/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ class Texture extends ImageWidget {
/*    */   private Identifier texture;
/*    */   
/*    */   private final int textureWidth;
/*    */   
/*    */   private final int textureHeight;
/*    */   
/*    */   public Texture(int x, int y, int width, int height, Identifier texture, int textureWidth, int textureHeight) {
/* 71 */     super(x, y, width, height);
/* 72 */     this.texture = texture;
/* 73 */     this.textureWidth = textureWidth;
/* 74 */     this.textureHeight = textureHeight;
/*    */   }
/*    */   
/*    */   protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 79 */     graphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, getX(), getY(), 0.0F, 0.0F, getWidth(), getHeight(), this.textureWidth, this.textureHeight);
/*    */   }
/*    */   
/*    */   public void updateResource(Identifier identifier) {
/* 84 */     this.texture = identifier;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ImageWidget$Texture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */