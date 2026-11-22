/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public final class Submit extends Record implements SubmitNode {
/*    */   private final PoseStack.Pose pose;
/*    */   
/*    */   private final VoxelShape shape;
/*    */   
/*    */   private final RenderType renderType;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   private final float width;
/*    */   
/*    */   public Submit(PoseStack.Pose pose, VoxelShape shape, RenderType renderType, int color, float width) {
/* 31 */     this.pose = pose;
/* 31 */     this.shape = shape;
/* 31 */     this.renderType = renderType;
/* 31 */     this.color = color;
/* 31 */     this.width = width;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/ShapeOutlineFeatureRenderer$Submit;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ShapeOutlineFeatureRenderer$Submit;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/ShapeOutlineFeatureRenderer$Submit;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ShapeOutlineFeatureRenderer$Submit;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/ShapeOutlineFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/feature/ShapeOutlineFeatureRenderer$Submit;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public PoseStack.Pose pose() {
/* 31 */     return this.pose;
/*    */   }
/*    */   
/*    */   public VoxelShape shape() {
/* 31 */     return this.shape;
/*    */   }
/*    */   
/*    */   public RenderType renderType() {
/* 31 */     return this.renderType;
/*    */   }
/*    */   
/*    */   public int color() {
/* 31 */     return this.color;
/*    */   }
/*    */   
/*    */   public float width() {
/* 31 */     return this.width;
/*    */   }
/*    */   
/*    */   public FeatureRendererType<Submit> featureType() {
/* 40 */     return ShapeOutlineFeatureRenderer.TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\ShapeOutlineFeatureRenderer$Submit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */