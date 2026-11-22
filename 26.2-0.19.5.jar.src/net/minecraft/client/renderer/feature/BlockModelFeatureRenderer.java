/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.QuadInstance;
/*    */ import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*    */ import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.ARGB;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class BlockModelFeatureRenderer extends RenderTypeFeatureRenderer<BlockModelFeatureRenderer.Submit> {
/* 19 */   public static final FeatureRendererType<Submit> TYPE = FeatureRendererType.create("Block Model");
/*    */   
/* 21 */   private static final Direction[] DIRECTIONS = Direction.values();
/*    */   
/* 23 */   private final QuadInstance quadInstance = new QuadInstance();
/*    */   
/*    */   protected void buildGroup(FeatureFrameContext context, List<Submit> submits) {
/* 27 */     for (Submit submit : submits) {
/* 28 */       VertexConsumer buffer = getVertexBuilder(submit.renderType());
/* 29 */       VertexConsumer wrappedBuffer = (submit.sheetedDecalPose() != null) ? (VertexConsumer)new SheetedDecalTextureGenerator(buffer, submit.sheetedDecalPose(), 1.0F) : buffer;
/* 31 */       this.quadInstance.setLightCoords(submit.lightCoords());
/* 32 */       this.quadInstance.setOverlayCoords(submit.overlayCoords());
/* 34 */       for (BlockStateModelPart part : submit.modelParts())
/* 35 */         putPartQuads(part, submit.pose(), this.quadInstance, submit.tintColor(), submit.tintLayers(), wrappedBuffer); 
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void putPartQuads(BlockStateModelPart part, PoseStack.Pose pose, QuadInstance quadInstance, int baseTintColor, int[] tintLayers, VertexConsumer buffer) {
/* 41 */     for (Direction direction : DIRECTIONS) {
/* 42 */       for (BakedQuad quad : part.getQuads(direction))
/* 43 */         putQuad(pose, quad, quadInstance, baseTintColor, tintLayers, buffer); 
/*    */     } 
/* 46 */     for (BakedQuad quad : part.getQuads(null))
/* 47 */       putQuad(pose, quad, quadInstance, baseTintColor, tintLayers, buffer); 
/*    */   }
/*    */   
/*    */   private static void putQuad(PoseStack.Pose pose, BakedQuad quad, QuadInstance instance, int baseTintColor, int[] tintLayers, VertexConsumer buffer) {
/* 52 */     int tintIndex = quad.materialInfo().tintIndex();
/* 53 */     boolean useTintLayer = (tintIndex != -1 && tintIndex < tintLayers.length);
/* 54 */     instance.setColor(useTintLayer ? ARGB.multiply(baseTintColor, tintLayers[tintIndex]) : baseTintColor);
/* 55 */     buffer.putBakedQuad(pose, quad, instance);
/*    */   }
/*    */   
/*    */   public static final class Submit extends Record implements TranslucentSubmit {
/*    */     private final PoseStack.Pose pose;
/*    */     
/*    */     private final RenderType renderType;
/*    */     
/*    */     private final List<BlockStateModelPart> modelParts;
/*    */     
/*    */     private final int[] tintLayers;
/*    */     
/*    */     private final int lightCoords;
/*    */     
/*    */     private final int overlayCoords;
/*    */     
/*    */     private final int tintColor;
/*    */     
/*    */     private final PoseStack.Pose sheetedDecalPose;
/*    */     
/*    */     public Submit(PoseStack.Pose pose, RenderType renderType, List<BlockStateModelPart> modelParts, int[] tintLayers, int lightCoords, int overlayCoords, int tintColor, PoseStack.Pose sheetedDecalPose) {
/* 58 */       this.pose = pose;
/* 58 */       this.renderType = renderType;
/* 58 */       this.modelParts = modelParts;
/* 58 */       this.tintLayers = tintLayers;
/* 58 */       this.lightCoords = lightCoords;
/* 58 */       this.overlayCoords = overlayCoords;
/* 58 */       this.tintColor = tintColor;
/* 58 */       this.sheetedDecalPose = sheetedDecalPose;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/BlockModelFeatureRenderer$Submit;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #58	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/BlockModelFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/BlockModelFeatureRenderer$Submit;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #58	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/BlockModelFeatureRenderer$Submit;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/BlockModelFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #58	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/BlockModelFeatureRenderer$Submit;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public PoseStack.Pose pose() {
/* 58 */       return this.pose;
/*    */     }
/*    */     
/*    */     public RenderType renderType() {
/* 58 */       return this.renderType;
/*    */     }
/*    */     
/*    */     public List<BlockStateModelPart> modelParts() {
/* 58 */       return this.modelParts;
/*    */     }
/*    */     
/*    */     public int[] tintLayers() {
/* 58 */       return this.tintLayers;
/*    */     }
/*    */     
/*    */     public int lightCoords() {
/* 58 */       return this.lightCoords;
/*    */     }
/*    */     
/*    */     public int overlayCoords() {
/* 58 */       return this.overlayCoords;
/*    */     }
/*    */     
/*    */     public int tintColor() {
/* 58 */       return this.tintColor;
/*    */     }
/*    */     
/*    */     public PoseStack.Pose sheetedDecalPose() {
/* 58 */       return this.sheetedDecalPose;
/*    */     }
/*    */     
/*    */     public float distanceToCameraSq() {
/* 71 */       return TranslucentSubmit.computeDistanceToCameraSq((Matrix4fc)this.pose.pose(), 0.5F, 0.5F, 0.5F);
/*    */     }
/*    */     
/*    */     public FeatureRendererType<Submit> featureType() {
/* 76 */       return BlockModelFeatureRenderer.TYPE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\BlockModelFeatureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */