/*     */ package net.minecraft.client.renderer.feature;
/*     */ 
/*     */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*     */ import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
/*     */ 
/*     */ public final class Submit extends Record implements SubmitNode {
/*     */   private final QuadParticleRenderState particles;
/*     */   
/*     */   private final boolean translucent;
/*     */   
/*     */   public Submit(QuadParticleRenderState particles, boolean translucent) {
/* 123 */     this.particles = particles;
/* 123 */     this.translucent = translucent;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$Submit;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #123	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$Submit;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$Submit;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #123	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$Submit;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #123	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$Submit;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public QuadParticleRenderState particles() {
/* 123 */     return this.particles;
/*     */   }
/*     */   
/*     */   public boolean translucent() {
/* 123 */     return this.translucent;
/*     */   }
/*     */   
/*     */   public FeatureRendererType<Submit> featureType() {
/* 129 */     return QuadParticleFeatureRenderer.TYPE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\QuadParticleFeatureRenderer$Submit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */