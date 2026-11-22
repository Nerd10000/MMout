/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.ComponentPath;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.client.sounds.SoundManager;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public abstract class ImageWidget extends AbstractWidget {
/*    */   private ImageWidget(int x, int y, int width, int height) {
/* 15 */     super(x, y, width, height, CommonComponents.EMPTY);
/*    */   }
/*    */   
/*    */   public static ImageWidget texture(int width, int height, Identifier texture, int textureWidth, int textureHeight) {
/* 19 */     return new Texture(0, 0, width, height, texture, textureWidth, textureHeight);
/*    */   }
/*    */   
/*    */   public static ImageWidget sprite(int width, int height, Identifier sprite) {
/* 23 */     return new Sprite(0, 0, width, height, sprite);
/*    */   }
/*    */   
/*    */   protected void updateWidgetNarration(NarrationElementOutput output) {}
/*    */   
/*    */   public void playDownSound(SoundManager soundManager) {}
/*    */   
/*    */   public boolean isActive() {
/* 36 */     return false;
/*    */   }
/*    */   
/*    */   public abstract void updateResource(Identifier paramIdentifier);
/*    */   
/*    */   public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 43 */     return null;
/*    */   }
/*    */   
/*    */   private static class Sprite extends ImageWidget {
/*    */     private Identifier sprite;
/*    */     
/*    */     public Sprite(int x, int y, int width, int height, Identifier sprite) {
/* 50 */       super(x, y, width, height);
/* 51 */       this.sprite = sprite;
/*    */     }
/*    */     
/*    */     public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 56 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX(), getY(), getWidth(), getHeight());
/*    */     }
/*    */     
/*    */     public void updateResource(Identifier identifier) {
/* 61 */       this.sprite = identifier;
/*    */     }
/*    */   }
/*    */   
/*    */   private static class Texture extends ImageWidget {
/*    */     private Identifier texture;
/*    */     
/*    */     private final int textureWidth;
/*    */     
/*    */     private final int textureHeight;
/*    */     
/*    */     public Texture(int x, int y, int width, int height, Identifier texture, int textureWidth, int textureHeight) {
/* 71 */       super(x, y, width, height);
/* 72 */       this.texture = texture;
/* 73 */       this.textureWidth = textureWidth;
/* 74 */       this.textureHeight = textureHeight;
/*    */     }
/*    */     
/*    */     protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 79 */       graphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, getX(), getY(), 0.0F, 0.0F, getWidth(), getHeight(), this.textureWidth, this.textureHeight);
/*    */     }
/*    */     
/*    */     public void updateResource(Identifier identifier) {
/* 84 */       this.texture = identifier;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ImageWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */