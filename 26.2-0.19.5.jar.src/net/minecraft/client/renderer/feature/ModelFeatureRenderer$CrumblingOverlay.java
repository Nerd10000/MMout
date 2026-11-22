/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ 
/*    */ public final class CrumblingOverlay extends Record {
/*    */   private final int progress;
/*    */   
/*    */   private final PoseStack.Pose cameraPose;
/*    */   
/*    */   public CrumblingOverlay(int progress, PoseStack.Pose cameraPose) {
/* 42 */     this.progress = progress;
/* 42 */     this.cameraPose = cameraPose;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int progress() {
/* 42 */     return this.progress;
/*    */   }
/*    */   
/*    */   public PoseStack.Pose cameraPose() {
/* 42 */     return this.cameraPose;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\ModelFeatureRenderer$CrumblingOverlay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */