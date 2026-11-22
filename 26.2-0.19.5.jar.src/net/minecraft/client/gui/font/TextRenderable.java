/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public interface TextRenderable {
/*    */   void render(Matrix4fc paramMatrix4fc, VertexConsumer paramVertexConsumer, int paramInt, boolean paramBoolean);
/*    */   
/*    */   RenderType renderType(Font.DisplayMode paramDisplayMode);
/*    */   
/*    */   GpuTextureView textureView();
/*    */   
/*    */   RenderPipeline guiPipeline();
/*    */   
/*    */   float left();
/*    */   
/*    */   float top();
/*    */   
/*    */   float right();
/*    */   
/*    */   float bottom();
/*    */   
/*    */   public static interface Styled extends TextRenderable, ActiveArea {
/*    */     default float activeLeft() {
/* 30 */       return left();
/*    */     }
/*    */     
/*    */     default float activeTop() {
/* 35 */       return top();
/*    */     }
/*    */     
/*    */     default float activeRight() {
/* 40 */       return right();
/*    */     }
/*    */     
/*    */     default float activeBottom() {
/* 45 */       return bottom();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\TextRenderable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */