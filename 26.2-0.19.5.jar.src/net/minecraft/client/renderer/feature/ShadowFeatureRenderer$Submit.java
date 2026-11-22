/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public final class Submit extends Record implements SubmitNode {
/*    */   private final Matrix4fc pose;
/*    */   
/*    */   private final float radius;
/*    */   
/*    */   private final List<EntityRenderState.ShadowPiece> pieces;
/*    */   
/*    */   public Submit(Matrix4fc pose, float radius, List<EntityRenderState.ShadowPiece> pieces) {
/* 61 */     this.pose = pose;
/* 61 */     this.radius = radius;
/* 61 */     this.pieces = pieces;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/ShadowFeatureRenderer$Submit;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ShadowFeatureRenderer$Submit;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/ShadowFeatureRenderer$Submit;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ShadowFeatureRenderer$Submit;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/ShadowFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/feature/ShadowFeatureRenderer$Submit;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Matrix4fc pose() {
/* 61 */     return this.pose;
/*    */   }
/*    */   
/*    */   public float radius() {
/* 61 */     return this.radius;
/*    */   }
/*    */   
/*    */   public List<EntityRenderState.ShadowPiece> pieces() {
/* 61 */     return this.pieces;
/*    */   }
/*    */   
/*    */   public FeatureRendererType<Submit> featureType() {
/* 68 */     return ShadowFeatureRenderer.TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\ShadowFeatureRenderer$Submit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */