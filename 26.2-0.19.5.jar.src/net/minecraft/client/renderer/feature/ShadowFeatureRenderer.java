/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class ShadowFeatureRenderer extends RenderTypeFeatureRenderer<ShadowFeatureRenderer.Submit> {
/* 19 */   public static final FeatureRendererType<Submit> TYPE = FeatureRendererType.create("Shadow");
/*    */   
/* 21 */   private static final RenderType SHADOW_RENDER_TYPE = RenderTypes.entityShadow(Identifier.withDefaultNamespace("textures/misc/shadow.png"));
/*    */   
/*    */   protected void buildGroup(FeatureFrameContext context, List<Submit> submits) {
/* 25 */     VertexConsumer builder = getVertexBuilder(SHADOW_RENDER_TYPE);
/* 26 */     for (Submit submit : submits)
/* 27 */       prepare(submit, builder); 
/*    */   }
/*    */   
/*    */   private void prepare(Submit submit, VertexConsumer builder) {
/* 32 */     for (EntityRenderState.ShadowPiece piece : submit.pieces()) {
/* 34 */       AABB aabb = piece.shapeBelow().bounds();
/* 36 */       float x01 = piece.relativeX() + (float)aabb.minX;
/* 37 */       float x11 = piece.relativeX() + (float)aabb.maxX;
/* 38 */       float y01 = piece.relativeY() + (float)aabb.minY;
/* 39 */       float z01 = piece.relativeZ() + (float)aabb.minZ;
/* 40 */       float z11 = piece.relativeZ() + (float)aabb.maxZ;
/* 42 */       float radius = submit.radius();
/* 43 */       float u0 = -x01 / 2.0F / radius + 0.5F;
/* 44 */       float u1 = -x11 / 2.0F / radius + 0.5F;
/* 45 */       float v0 = -z01 / 2.0F / radius + 0.5F;
/* 46 */       float v1 = -z11 / 2.0F / radius + 0.5F;
/* 48 */       int color = ARGB.white(piece.alpha());
/* 49 */       shadowVertex(submit.pose(), builder, color, x01, y01, z01, u0, v0);
/* 50 */       shadowVertex(submit.pose(), builder, color, x01, y01, z11, u0, v1);
/* 51 */       shadowVertex(submit.pose(), builder, color, x11, y01, z11, u1, v1);
/* 52 */       shadowVertex(submit.pose(), builder, color, x11, y01, z01, u1, v0);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void shadowVertex(Matrix4fc pose, VertexConsumer buffer, int color, float x, float y, float z, float u, float v) {
/* 57 */     Vector3f position = pose.transformPosition(x, y, z, new Vector3f());
/* 58 */     buffer.addVertex(position.x(), position.y(), position.z(), color, u, v, OverlayTexture.NO_OVERLAY, 15728880, 0.0F, 1.0F, 0.0F);
/*    */   }
/*    */   
/*    */   public static final class Submit extends Record implements SubmitNode {
/*    */     private final Matrix4fc pose;
/*    */     
/*    */     private final float radius;
/*    */     
/*    */     private final List<EntityRenderState.ShadowPiece> pieces;
/*    */     
/*    */     public Submit(Matrix4fc pose, float radius, List<EntityRenderState.ShadowPiece> pieces) {
/* 61 */       this.pose = pose;
/* 61 */       this.radius = radius;
/* 61 */       this.pieces = pieces;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/ShadowFeatureRenderer$Submit;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ShadowFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/ShadowFeatureRenderer$Submit;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ShadowFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/ShadowFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/ShadowFeatureRenderer$Submit;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Matrix4fc pose() {
/* 61 */       return this.pose;
/*    */     }
/*    */     
/*    */     public float radius() {
/* 61 */       return this.radius;
/*    */     }
/*    */     
/*    */     public List<EntityRenderState.ShadowPiece> pieces() {
/* 61 */       return this.pieces;
/*    */     }
/*    */     
/*    */     public FeatureRendererType<Submit> featureType() {
/* 68 */       return ShadowFeatureRenderer.TYPE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\ShadowFeatureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */