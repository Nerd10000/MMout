/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Unit;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ final class ElderGuardianParticleRenderState extends Record {
/*    */   private final Model<Unit> model;
/*    */   
/*    */   private final PoseStack poseStack;
/*    */   
/*    */   private final RenderType renderType;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   private ElderGuardianParticleRenderState(Model<Unit> model, PoseStack poseStack, RenderType renderType, int color) {
/* 40 */     this.model = model;
/* 40 */     this.poseStack = poseStack;
/* 40 */     this.renderType = renderType;
/* 40 */     this.color = color;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/ElderGuardianParticleGroup$ElderGuardianParticleRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #40	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/particle/ElderGuardianParticleGroup$ElderGuardianParticleRenderState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/ElderGuardianParticleGroup$ElderGuardianParticleRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #40	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/particle/ElderGuardianParticleGroup$ElderGuardianParticleRenderState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/ElderGuardianParticleGroup$ElderGuardianParticleRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #40	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/particle/ElderGuardianParticleGroup$ElderGuardianParticleRenderState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Model<Unit> model() {
/* 40 */     return this.model;
/*    */   }
/*    */   
/*    */   public PoseStack poseStack() {
/* 40 */     return this.poseStack;
/*    */   }
/*    */   
/*    */   public RenderType renderType() {
/* 40 */     return this.renderType;
/*    */   }
/*    */   
/*    */   public int color() {
/* 40 */     return this.color;
/*    */   }
/*    */   
/*    */   public static ElderGuardianParticleRenderState fromParticle(ElderGuardianParticle particle, Camera camera, float partialTickTime) {
/* 42 */     float ageScale = (particle.age + partialTickTime) / particle.lifetime;
/* 43 */     float alpha = 0.05F + 0.5F * Mth.sin((ageScale * 3.1415927F));
/* 44 */     int color = ARGB.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F);
/* 45 */     PoseStack poseStack = new PoseStack();
/* 47 */     poseStack.pushPose();
/* 48 */     poseStack.mulPose((Quaternionfc)camera.rotation());
/* 49 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(60.0F - 150.0F * ageScale));
/* 51 */     float scale = 0.42553192F;
/* 52 */     poseStack.scale(0.42553192F, -0.42553192F, -0.42553192F);
/* 53 */     poseStack.translate(0.0F, -0.56F, 3.5F);
/* 55 */     return new ElderGuardianParticleRenderState((Model<Unit>)particle.model, poseStack, particle.renderType, color);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\ElderGuardianParticleGroup$ElderGuardianParticleRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */