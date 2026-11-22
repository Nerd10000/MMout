/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class ShapeOutlineFeatureRenderer extends RenderTypeFeatureRenderer<ShapeOutlineFeatureRenderer.Submit> {
/* 13 */   public static final FeatureRendererType<Submit> TYPE = FeatureRendererType.create("Shape Outline");
/*    */   
/*    */   protected void buildGroup(FeatureFrameContext context, List<Submit> submits) {
/* 17 */     Vector3f normal = new Vector3f();
/* 18 */     for (Iterator<Submit> iterator = submits.iterator(); iterator.hasNext(); ) {
/* 18 */       Submit submit = iterator.next();
/* 19 */       PoseStack.Pose pose = submit.pose();
/* 20 */       int color = submit.color();
/* 21 */       float width = submit.width();
/* 22 */       VertexConsumer builder = getVertexBuilder(submit.renderType());
/* 23 */       submit.shape().forAllEdges((x1, y1, z1, x2, y2, z2) -> {
/*    */             normal.set((float)(x2 - x1), (float)(y2 - y1), (float)(z2 - z1)).normalize();
/*    */             builder.addVertex(pose, (float)x1, (float)y1, (float)z1).setColor(color).setNormal(pose, (Vector3fc)normal).setLineWidth(width);
/*    */             builder.addVertex(pose, (float)x2, (float)y2, (float)z2).setColor(color).setNormal(pose, (Vector3fc)normal).setLineWidth(width);
/*    */           });
/*    */     } 
/*    */   }
/*    */   
/*    */   public static final class Submit extends Record implements SubmitNode {
/*    */     private final PoseStack.Pose pose;
/*    */     
/*    */     private final VoxelShape shape;
/*    */     
/*    */     private final RenderType renderType;
/*    */     
/*    */     private final int color;
/*    */     
/*    */     private final float width;
/*    */     
/*    */     public Submit(PoseStack.Pose pose, VoxelShape shape, RenderType renderType, int color, float width) {
/* 31 */       this.pose = pose;
/* 31 */       this.shape = shape;
/* 31 */       this.renderType = renderType;
/* 31 */       this.color = color;
/* 31 */       this.width = width;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/ShapeOutlineFeatureRenderer$Submit;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ShapeOutlineFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/ShapeOutlineFeatureRenderer$Submit;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ShapeOutlineFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/ShapeOutlineFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/ShapeOutlineFeatureRenderer$Submit;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public PoseStack.Pose pose() {
/* 31 */       return this.pose;
/*    */     }
/*    */     
/*    */     public VoxelShape shape() {
/* 31 */       return this.shape;
/*    */     }
/*    */     
/*    */     public RenderType renderType() {
/* 31 */       return this.renderType;
/*    */     }
/*    */     
/*    */     public int color() {
/* 31 */       return this.color;
/*    */     }
/*    */     
/*    */     public float width() {
/* 31 */       return this.width;
/*    */     }
/*    */     
/*    */     public FeatureRendererType<Submit> featureType() {
/* 40 */       return ShapeOutlineFeatureRenderer.TYPE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\ShapeOutlineFeatureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */