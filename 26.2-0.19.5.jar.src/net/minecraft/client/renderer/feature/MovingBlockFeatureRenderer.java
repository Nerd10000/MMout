/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.QuadInstance;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.block.BlockAndTintGetter;
/*    */ import net.minecraft.client.renderer.block.BlockQuadOutput;
/*    */ import net.minecraft.client.renderer.block.ModelBlockRenderer;
/*    */ import net.minecraft.client.renderer.block.MovingBlockRenderState;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*    */ import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
/*    */ import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class MovingBlockFeatureRenderer extends RenderTypeFeatureRenderer<MovingBlockFeatureRenderer.Submit> {
/* 22 */   public static final FeatureRendererType<Submit> TYPE = FeatureRendererType.create("Moving Block");
/*    */   
/* 24 */   private final PoseStack poseStack = new PoseStack();
/*    */   
/*    */   protected void buildGroup(FeatureFrameContext context, List<Submit> submits) {
/* 28 */     boolean ambientOcclusion = (context.options()).ambientOcclusion;
/* 29 */     boolean cutoutLeaves = (context.options()).cutoutLeaves;
/* 30 */     ModelBlockRenderer blockRenderer = new ModelBlockRenderer(ambientOcclusion, false, context.blockColors());
/* 32 */     for (Iterator<Submit> iterator = submits.iterator(); iterator.hasNext(); ) {
/* 32 */       Submit submit = iterator.next();
/* 33 */       MovingBlockRenderState movingBlockRenderState = submit.movingBlockRenderState();
/* 34 */       BlockState blockState = movingBlockRenderState.blockState;
/* 35 */       BlockStateModel model = context.blockStateModelSet().get(blockState);
/* 37 */       this.poseStack.setIdentity();
/* 38 */       this.poseStack.mulPose(submit.pose());
/* 40 */       BlockQuadOutput quadOutput = (x, y, z, quad, instance) -> putBakedQuad(this.poseStack, x, y, z, quad, instance, quad.materialInfo().layer(), submit.outlineColor());
/* 42 */       BlockQuadOutput solidQuadOutput = (x, y, z, quad, instance) -> putBakedQuad(this.poseStack, x, y, z, quad, instance, ChunkSectionLayer.SOLID, submit.outlineColor());
/* 45 */       BlockQuadOutput blockOutput = ModelBlockRenderer.forceOpaque(cutoutLeaves, blockState) ? solidQuadOutput : quadOutput;
/* 46 */       long blockSeed = blockState.getSeed(movingBlockRenderState.randomSeedPos);
/* 47 */       blockRenderer.tesselateBlock(blockOutput, 0.0F, 0.0F, 0.0F, (BlockAndTintGetter)movingBlockRenderState, movingBlockRenderState.blockPos, blockState, model, blockSeed);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void putBakedQuad(PoseStack poseStack, float x, float y, float z, BakedQuad quad, QuadInstance instance, ChunkSectionLayer layer, int outlineColor) {
/*    */     VertexConsumer buffer;
/* 52 */     poseStack.pushPose();
/* 53 */     poseStack.translate(x, y, z);
/* 55 */     switch (layer) {
/*    */       default:
/* 55 */         throw new MatchException(null, null);
/*    */       case SOLID:
/*    */       
/*    */       case CUTOUT:
/*    */       
/*    */       case TRANSLUCENT:
/*    */         break;
/*    */     } 
/* 55 */     RenderType renderType = 
/*    */       
/* 58 */       RenderTypes.translucentMovingBlock();
/* 62 */     if (outlineColor != 0 && renderType.outline().isPresent()) {
/* 63 */       instance.setColor(outlineColor);
/* 64 */       buffer = getVertexBuilder(renderType.outline().get());
/*    */     } else {
/* 66 */       buffer = getVertexBuilder(renderType);
/*    */     } 
/* 69 */     buffer.putBakedQuad(poseStack.last(), quad, instance);
/* 70 */     poseStack.popPose();
/*    */   }
/*    */   
/*    */   public static final class Submit extends Record implements TranslucentSubmit {
/*    */     private final Matrix4fc pose;
/*    */     
/*    */     private final MovingBlockRenderState movingBlockRenderState;
/*    */     
/*    */     private final int outlineColor;
/*    */     
/*    */     public Submit(Matrix4fc pose, MovingBlockRenderState movingBlockRenderState, int outlineColor) {
/* 73 */       this.pose = pose;
/* 73 */       this.movingBlockRenderState = movingBlockRenderState;
/* 73 */       this.outlineColor = outlineColor;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/MovingBlockFeatureRenderer$Submit;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #73	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/MovingBlockFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/MovingBlockFeatureRenderer$Submit;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #73	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/MovingBlockFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/MovingBlockFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #73	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/MovingBlockFeatureRenderer$Submit;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Matrix4fc pose() {
/* 73 */       return this.pose;
/*    */     }
/*    */     
/*    */     public MovingBlockRenderState movingBlockRenderState() {
/* 73 */       return this.movingBlockRenderState;
/*    */     }
/*    */     
/*    */     public int outlineColor() {
/* 73 */       return this.outlineColor;
/*    */     }
/*    */     
/*    */     public float distanceToCameraSq() {
/* 80 */       return TranslucentSubmit.computeDistanceToCameraSq(this.pose, 0.5F, 0.5F, 0.5F);
/*    */     }
/*    */     
/*    */     public FeatureRendererType<Submit> featureType() {
/* 85 */       return MovingBlockFeatureRenderer.TYPE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\MovingBlockFeatureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */