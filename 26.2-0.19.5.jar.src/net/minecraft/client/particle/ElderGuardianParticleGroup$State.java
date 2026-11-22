/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ final class State extends Record implements ParticleGroupRenderState {
/*    */   private final List<ElderGuardianParticleGroup.ElderGuardianParticleRenderState> states;
/*    */   
/*    */   private State(List<ElderGuardianParticleGroup.ElderGuardianParticleRenderState> states) {
/* 31 */     this.states = states;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/ElderGuardianParticleGroup$State;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/particle/ElderGuardianParticleGroup$State;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/ElderGuardianParticleGroup$State;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/particle/ElderGuardianParticleGroup$State;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/ElderGuardianParticleGroup$State;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/particle/ElderGuardianParticleGroup$State;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<ElderGuardianParticleGroup.ElderGuardianParticleRenderState> states() {
/* 31 */     return this.states;
/*    */   }
/*    */   
/*    */   public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 34 */     for (ElderGuardianParticleGroup.ElderGuardianParticleRenderState state : this.states)
/* 35 */       submitNodeCollector.submitModel(state.model, Unit.INSTANCE, state.poseStack, state.renderType, 15728880, OverlayTexture.NO_OVERLAY, state.color, null, 0, null); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\ElderGuardianParticleGroup$State.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */