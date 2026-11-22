/*     */ package net.minecraft.client.renderer.feature;
/*     */ 
/*     */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*     */ import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ 
/*     */ public final class Submit extends Record implements SubmitNode {
/*     */   private final DrawableGizmoPrimitives.Group group;
/*     */   
/*     */   private final CameraRenderState camera;
/*     */   
/*     */   public Submit(DrawableGizmoPrimitives.Group group, CameraRenderState camera) {
/* 181 */     this.group = group;
/* 181 */     this.camera = camera;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/GizmoFeatureRenderer$Submit;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #181	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/GizmoFeatureRenderer$Submit;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/GizmoFeatureRenderer$Submit;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #181	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/GizmoFeatureRenderer$Submit;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/GizmoFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #181	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/feature/GizmoFeatureRenderer$Submit;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public DrawableGizmoPrimitives.Group group() {
/* 181 */     return this.group;
/*     */   }
/*     */   
/*     */   public CameraRenderState camera() {
/* 181 */     return this.camera;
/*     */   }
/*     */   
/*     */   public FeatureRendererType<Submit> featureType() {
/* 187 */     return GizmoFeatureRenderer.TYPE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\GizmoFeatureRenderer$Submit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */