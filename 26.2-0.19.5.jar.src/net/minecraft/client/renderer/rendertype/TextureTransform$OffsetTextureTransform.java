/*    */ package net.minecraft.client.renderer.rendertype;
/*    */ 
/*    */ import org.joml.Matrix4f;
/*    */ 
/*    */ public final class OffsetTextureTransform extends TextureTransform {
/*    */   public OffsetTextureTransform(float uOffset, float vOffset) {
/* 34 */     super("offset_texturing", () -> (new Matrix4f()).translation(uOffset, vOffset, 0.0F));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\rendertype\TextureTransform$OffsetTextureTransform.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */