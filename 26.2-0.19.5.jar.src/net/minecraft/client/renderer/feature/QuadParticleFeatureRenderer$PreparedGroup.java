/*     */ package net.minecraft.client.renderer.feature;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.particle.SingleQuadParticle;
/*     */ import net.minecraft.client.renderer.StagedVertexBuffer;
/*     */ 
/*     */ final class PreparedGroup extends Record {
/*     */   private final Map<SingleQuadParticle.Layer, StagedVertexBuffer.Draw> layers;
/*     */   
/*     */   private final boolean translucent;
/*     */   
/*     */   private PreparedGroup(Map<SingleQuadParticle.Layer, StagedVertexBuffer.Draw> layers, boolean translucent) {
/* 117 */     this.layers = layers;
/* 117 */     this.translucent = translucent;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$PreparedGroup;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #117	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$PreparedGroup;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$PreparedGroup;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #117	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$PreparedGroup;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$PreparedGroup;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #117	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/feature/QuadParticleFeatureRenderer$PreparedGroup;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Map<SingleQuadParticle.Layer, StagedVertexBuffer.Draw> layers() {
/* 117 */     return this.layers;
/*     */   }
/*     */   
/*     */   public boolean translucent() {
/* 117 */     return this.translucent;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\QuadParticleFeatureRenderer$PreparedGroup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */