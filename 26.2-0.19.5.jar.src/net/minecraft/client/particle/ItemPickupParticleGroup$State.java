/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
/*    */ 
/*    */ final class State extends Record implements ParticleGroupRenderState {
/*    */   private final List<ItemPickupParticleGroup.ParticleInstance> instances;
/*    */   
/*    */   private State(List<ItemPickupParticleGroup.ParticleInstance> instances) {
/* 27 */     this.instances = instances;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/ItemPickupParticleGroup$State;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/particle/ItemPickupParticleGroup$State;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/ItemPickupParticleGroup$State;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/particle/ItemPickupParticleGroup$State;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/ItemPickupParticleGroup$State;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/particle/ItemPickupParticleGroup$State;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<ItemPickupParticleGroup.ParticleInstance> instances() {
/* 27 */     return this.instances;
/*    */   }
/*    */   
/*    */   public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 30 */     PoseStack poseStack = new PoseStack();
/* 31 */     EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
/* 32 */     for (ItemPickupParticleGroup.ParticleInstance instance : this.instances)
/* 33 */       entityRenderDispatcher.submit(instance.itemRenderState, camera, instance.xOffset, instance.yOffset, instance.zOffset, poseStack, submitNodeCollector); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\ItemPickupParticleGroup$State.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */