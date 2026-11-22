/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.font.TextRenderable;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ class GlyphRenderer implements Font.GlyphVisitor {
/* 40 */   private final Matrix4f pose = new Matrix4f();
/*    */   
/* 41 */   private int lightCoords = 15728880;
/*    */   
/* 42 */   private Font.DisplayMode displayMode = Font.DisplayMode.NORMAL;
/*    */   
/*    */   public void acceptRenderable(TextRenderable renderable) {
/* 46 */     VertexConsumer builder = TextFeatureRenderer.this.getVertexBuilder(renderable.renderType(this.displayMode));
/* 47 */     renderable.render((Matrix4fc)this.pose, builder, this.lightCoords, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\TextFeatureRenderer$GlyphRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */