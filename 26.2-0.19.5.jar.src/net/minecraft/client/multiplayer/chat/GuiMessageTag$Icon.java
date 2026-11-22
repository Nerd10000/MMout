/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public enum Icon {
/* 49 */   CHAT_MODIFIED(Identifier.withDefaultNamespace("icon/chat_modified"), 9, 9);
/*    */   
/*    */   public final int height;
/*    */   
/*    */   public final int width;
/*    */   
/*    */   public final Identifier sprite;
/*    */   
/*    */   Icon(Identifier sprite, int width, int height) {
/* 57 */     this.sprite = sprite;
/* 58 */     this.width = width;
/* 59 */     this.height = height;
/*    */   }
/*    */   
/*    */   public void extractRenderState(GuiGraphicsExtractor graphics, int x, int y) {
/* 63 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, x, y, this.width, this.height);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\GuiMessageTag$Icon.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */