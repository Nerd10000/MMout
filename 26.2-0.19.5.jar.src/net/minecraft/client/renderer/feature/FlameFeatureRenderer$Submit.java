/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*    */ import org.joml.Quaternionf;
/*    */ 
/*    */ public final class Submit extends Record implements SubmitNode {
/*    */   private final PoseStack.Pose pose;
/*    */   
/*    */   private final EntityRenderState entityRenderState;
/*    */   
/*    */   private final Quaternionf rotation;
/*    */   
/*    */   public Submit(PoseStack.Pose pose, EntityRenderState entityRenderState, Quaternionf rotation) {
/* 80 */     this.pose = pose;
/* 80 */     this.entityRenderState = entityRenderState;
/* 80 */     this.rotation = rotation;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #80	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #80	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #80	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public PoseStack.Pose pose() {
/* 80 */     return this.pose;
/*    */   }
/*    */   
/*    */   public EntityRenderState entityRenderState() {
/* 80 */     return this.entityRenderState;
/*    */   }
/*    */   
/*    */   public Quaternionf rotation() {
/* 80 */     return this.rotation;
/*    */   }
/*    */   
/*    */   public FeatureRendererType<Submit> featureType() {
/* 87 */     return FlameFeatureRenderer.TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\FlameFeatureRenderer$Submit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */