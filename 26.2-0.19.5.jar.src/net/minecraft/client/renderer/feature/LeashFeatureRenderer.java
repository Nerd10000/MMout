/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ import net.minecraft.util.Mth;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class LeashFeatureRenderer extends RenderTypeFeatureRenderer<LeashFeatureRenderer.Submit> {
/* 15 */   public static final FeatureRendererType<Submit> TYPE = FeatureRendererType.create("Leash");
/*    */   
/*    */   private static final int LEASH_RENDER_STEPS = 24;
/*    */   
/*    */   private static final float LEASH_WIDTH = 0.05F;
/*    */   
/*    */   protected void buildGroup(FeatureFrameContext context, List<Submit> submits) {
/* 22 */     for (Submit submit : submits)
/* 23 */       prepare(submit); 
/*    */   }
/*    */   
/*    */   private void prepare(Submit submit) {
/* 28 */     VertexConsumer builder = getVertexBuilder(RenderTypes.leash());
/* 30 */     Matrix4f pose = submit.pose();
/* 31 */     EntityRenderState.LeashState leashState = submit.leashState();
/* 32 */     float dx = (float)(leashState.end.x - leashState.start.x);
/* 33 */     float dy = (float)(leashState.end.y - leashState.start.y);
/* 34 */     float dz = (float)(leashState.end.z - leashState.start.z);
/* 36 */     float offsetFactor = Mth.invSqrt(dx * dx + dz * dz) * 0.05F / 2.0F;
/* 39 */     float dxOff = dz * offsetFactor;
/* 40 */     float dzOff = dx * offsetFactor;
/* 43 */     pose.translate((float)leashState.offset.x, (float)leashState.offset.y, (float)leashState.offset.z);
/*    */     int k;
/* 45 */     for (k = 0; k <= 24; k++)
/* 46 */       addVertexPair(builder, (Matrix4fc)pose, dx, dy, dz, 0.05F, dxOff, dzOff, k, false, leashState); 
/* 48 */     for (k = 24; k >= 0; k--)
/* 49 */       addVertexPair(builder, (Matrix4fc)pose, dx, dy, dz, 0.0F, dxOff, dzOff, k, true, leashState); 
/*    */   }
/*    */   
/*    */   private static void addVertexPair(VertexConsumer builder, Matrix4fc pose, float dx, float dy, float dz, float fudge, float dxOff, float dzOff, int k, boolean backwards, EntityRenderState.LeashState state) {
/* 54 */     float y, progress = k / 24.0F;
/* 55 */     int block = (int)Mth.lerp(progress, state.startBlockLight, state.endBlockLight);
/* 56 */     int sky = (int)Mth.lerp(progress, state.startSkyLight, state.endSkyLight);
/* 57 */     int lightCoords = LightCoordsUtil.pack(block, sky);
/* 59 */     float colorModifier = (k % 2 == (backwards ? 1 : 0)) ? 0.7F : 1.0F;
/* 60 */     float r = 0.5F * colorModifier;
/* 61 */     float g = 0.4F * colorModifier;
/* 62 */     float b = 0.3F * colorModifier;
/* 64 */     float x = dx * progress;
/* 66 */     if (state.slack) {
/* 67 */       y = (dy > 0.0F) ? (dy * progress * progress) : (dy - dy * (1.0F - progress) * (1.0F - progress));
/*    */     } else {
/* 69 */       y = dy * progress;
/*    */     } 
/* 71 */     float z = dz * progress;
/* 73 */     builder.addVertex(pose, x - dxOff, y + fudge, z + dzOff).setColor(r, g, b, 1.0F).setLight(lightCoords);
/* 74 */     builder.addVertex(pose, x + dxOff, y + 0.05F - fudge, z - dzOff).setColor(r, g, b, 1.0F).setLight(lightCoords);
/*    */   }
/*    */   
/*    */   public static final class Submit extends Record implements SubmitNode {
/*    */     private final Matrix4f pose;
/*    */     
/*    */     private final EntityRenderState.LeashState leashState;
/*    */     
/*    */     public Submit(Matrix4f pose, EntityRenderState.LeashState leashState) {
/* 77 */       this.pose = pose;
/* 77 */       this.leashState = leashState;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/LeashFeatureRenderer$Submit;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #77	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/LeashFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/LeashFeatureRenderer$Submit;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #77	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/LeashFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/LeashFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #77	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/LeashFeatureRenderer$Submit;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Matrix4f pose() {
/* 77 */       return this.pose;
/*    */     }
/*    */     
/*    */     public EntityRenderState.LeashState leashState() {
/* 77 */       return this.leashState;
/*    */     }
/*    */     
/*    */     public FeatureRendererType<Submit> featureType() {
/* 83 */       return LeashFeatureRenderer.TYPE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\LeashFeatureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */