/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.ModelBakery;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ import org.joml.Quaternionf;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class FlameFeatureRenderer extends RenderTypeFeatureRenderer<FlameFeatureRenderer.Submit> {
/* 20 */   public static final FeatureRendererType<Submit> TYPE = FeatureRendererType.create("Flame");
/*    */   
/*    */   protected void buildGroup(FeatureFrameContext context, List<Submit> submits) {
/* 24 */     VertexConsumer builder = getVertexBuilder(RenderTypes.entityCutoutCull(TextureAtlas.LOCATION_BLOCKS));
/* 25 */     TextureAtlasSprite fire1 = context.atlasManager().get(ModelBakery.FIRE_0);
/* 26 */     TextureAtlasSprite fire2 = context.atlasManager().get(ModelBakery.FIRE_1);
/* 27 */     for (Submit submit : submits)
/* 28 */       prepare(submit, builder, fire1, fire2); 
/*    */   }
/*    */   
/*    */   private void prepare(Submit submit, VertexConsumer buffer, TextureAtlasSprite fire1, TextureAtlasSprite fire2) {
/* 33 */     PoseStack.Pose pose = submit.pose();
/* 34 */     EntityRenderState state = submit.entityRenderState();
/* 36 */     float s = state.boundingBoxWidth * 1.4F;
/* 37 */     pose.scale(s, s, s);
/* 39 */     float r = 0.5F;
/* 40 */     float xo = 0.0F;
/* 42 */     float h = state.boundingBoxHeight / s;
/* 43 */     float yo = 0.0F;
/* 45 */     pose.rotate((Quaternionfc)submit.rotation());
/* 47 */     pose.translate(0.0F, 0.0F, 0.3F - (int)h * 0.02F);
/* 48 */     float zo = 0.0F;
/* 49 */     int ss = 0;
/* 50 */     int lightCoords = LightCoordsUtil.withBlock(state.lightCoords, 15);
/* 52 */     while (h > 0.0F) {
/* 53 */       TextureAtlasSprite tex = (ss % 2 == 0) ? fire1 : fire2;
/* 55 */       float u0 = tex.getU0();
/* 56 */       float v0 = tex.getV0();
/* 57 */       float u1 = tex.getU1();
/* 58 */       float v1 = tex.getV1();
/* 59 */       if (ss / 2 % 2 == 0) {
/* 60 */         float tmp = u1;
/* 61 */         u1 = u0;
/* 62 */         u0 = tmp;
/*    */       } 
/* 64 */       fireVertex(pose, buffer, -r - 0.0F, 0.0F - yo, zo, u1, v1, lightCoords);
/* 65 */       fireVertex(pose, buffer, r - 0.0F, 0.0F - yo, zo, u0, v1, lightCoords);
/* 66 */       fireVertex(pose, buffer, r - 0.0F, 1.4F - yo, zo, u0, v0, lightCoords);
/* 67 */       fireVertex(pose, buffer, -r - 0.0F, 1.4F - yo, zo, u1, v0, lightCoords);
/* 68 */       h -= 0.45F;
/* 69 */       yo -= 0.45F;
/* 70 */       r *= 0.9F;
/* 71 */       zo -= 0.03F;
/* 72 */       ss++;
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void fireVertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float z, float u, float v, int lightCoords) {
/* 77 */     buffer.addVertex(pose, x, y, z).setColor(-1).setUv(u, v).setUv1(0, 10).setLight(lightCoords).setNormal(pose, 0.0F, 1.0F, 0.0F);
/*    */   }
/*    */   
/*    */   public static final class Submit extends Record implements SubmitNode {
/*    */     private final PoseStack.Pose pose;
/*    */     
/*    */     private final EntityRenderState entityRenderState;
/*    */     
/*    */     private final Quaternionf rotation;
/*    */     
/*    */     public Submit(PoseStack.Pose pose, EntityRenderState entityRenderState, Quaternionf rotation) {
/* 80 */       this.pose = pose;
/* 80 */       this.entityRenderState = entityRenderState;
/* 80 */       this.rotation = rotation;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #80	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #80	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #80	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public PoseStack.Pose pose() {
/* 80 */       return this.pose;
/*    */     }
/*    */     
/*    */     public EntityRenderState entityRenderState() {
/* 80 */       return this.entityRenderState;
/*    */     }
/*    */     
/*    */     public Quaternionf rotation() {
/* 80 */       return this.rotation;
/*    */     }
/*    */     
/*    */     public FeatureRendererType<Submit> featureType() {
/* 87 */       return FlameFeatureRenderer.TYPE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\FlameFeatureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */