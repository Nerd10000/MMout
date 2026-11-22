/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import net.minecraft.client.renderer.block.MovingBlockRenderState;
/*    */ import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public final class Submit extends Record implements TranslucentSubmit {
/*    */   private final Matrix4fc pose;
/*    */   
/*    */   private final MovingBlockRenderState movingBlockRenderState;
/*    */   
/*    */   private final int outlineColor;
/*    */   
/*    */   public Submit(Matrix4fc pose, MovingBlockRenderState movingBlockRenderState, int outlineColor) {
/* 73 */     this.pose = pose;
/* 73 */     this.movingBlockRenderState = movingBlockRenderState;
/* 73 */     this.outlineColor = outlineColor;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/MovingBlockFeatureRenderer$Submit;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #73	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/MovingBlockFeatureRenderer$Submit;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/MovingBlockFeatureRenderer$Submit;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #73	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/MovingBlockFeatureRenderer$Submit;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/MovingBlockFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #73	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/feature/MovingBlockFeatureRenderer$Submit;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Matrix4fc pose() {
/* 73 */     return this.pose;
/*    */   }
/*    */   
/*    */   public MovingBlockRenderState movingBlockRenderState() {
/* 73 */     return this.movingBlockRenderState;
/*    */   }
/*    */   
/*    */   public int outlineColor() {
/* 73 */     return this.outlineColor;
/*    */   }
/*    */   
/*    */   public float distanceToCameraSq() {
/* 80 */     return TranslucentSubmit.computeDistanceToCameraSq(this.pose, 0.5F, 0.5F, 0.5F);
/*    */   }
/*    */   
/*    */   public FeatureRendererType<Submit> featureType() {
/* 85 */     return MovingBlockFeatureRenderer.TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\MovingBlockFeatureRenderer$Submit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */