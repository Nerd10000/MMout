/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*    */ import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public final class Submit extends Record implements TranslucentSubmit {
/*    */   private final PoseStack.Pose pose;
/*    */   
/*    */   private final RenderType renderType;
/*    */   
/*    */   private final List<BlockStateModelPart> modelParts;
/*    */   
/*    */   private final int[] tintLayers;
/*    */   
/*    */   private final int lightCoords;
/*    */   
/*    */   private final int overlayCoords;
/*    */   
/*    */   private final int tintColor;
/*    */   
/*    */   private final PoseStack.Pose sheetedDecalPose;
/*    */   
/*    */   public Submit(PoseStack.Pose pose, RenderType renderType, List<BlockStateModelPart> modelParts, int[] tintLayers, int lightCoords, int overlayCoords, int tintColor, PoseStack.Pose sheetedDecalPose) {
/* 58 */     this.pose = pose;
/* 58 */     this.renderType = renderType;
/* 58 */     this.modelParts = modelParts;
/* 58 */     this.tintLayers = tintLayers;
/* 58 */     this.lightCoords = lightCoords;
/* 58 */     this.overlayCoords = overlayCoords;
/* 58 */     this.tintColor = tintColor;
/* 58 */     this.sheetedDecalPose = sheetedDecalPose;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/BlockModelFeatureRenderer$Submit;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/BlockModelFeatureRenderer$Submit;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/BlockModelFeatureRenderer$Submit;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/BlockModelFeatureRenderer$Submit;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/BlockModelFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/feature/BlockModelFeatureRenderer$Submit;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public PoseStack.Pose pose() {
/* 58 */     return this.pose;
/*    */   }
/*    */   
/*    */   public RenderType renderType() {
/* 58 */     return this.renderType;
/*    */   }
/*    */   
/*    */   public List<BlockStateModelPart> modelParts() {
/* 58 */     return this.modelParts;
/*    */   }
/*    */   
/*    */   public int[] tintLayers() {
/* 58 */     return this.tintLayers;
/*    */   }
/*    */   
/*    */   public int lightCoords() {
/* 58 */     return this.lightCoords;
/*    */   }
/*    */   
/*    */   public int overlayCoords() {
/* 58 */     return this.overlayCoords;
/*    */   }
/*    */   
/*    */   public int tintColor() {
/* 58 */     return this.tintColor;
/*    */   }
/*    */   
/*    */   public PoseStack.Pose sheetedDecalPose() {
/* 58 */     return this.sheetedDecalPose;
/*    */   }
/*    */   
/*    */   public float distanceToCameraSq() {
/* 71 */     return TranslucentSubmit.computeDistanceToCameraSq((Matrix4fc)this.pose.pose(), 0.5F, 0.5F, 0.5F);
/*    */   }
/*    */   
/*    */   public FeatureRendererType<Submit> featureType() {
/* 76 */     return BlockModelFeatureRenderer.TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\BlockModelFeatureRenderer$Submit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */