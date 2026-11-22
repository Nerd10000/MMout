/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.font.TextRenderable;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ class null implements Font.GlyphVisitor {
/*    */   public void acceptRenderable(TextRenderable renderable) {
/* 70 */     VertexConsumer buffer = GizmoFeatureRenderer.this.getVertexBuilder(renderable.renderType(Font.DisplayMode.NORMAL));
/* 71 */     renderable.render((Matrix4fc)pose, buffer, 15728880, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\GizmoFeatureRenderer$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */