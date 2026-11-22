/*    */ package net.minecraft.client.model.animal.turtle;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.QuadrupedModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.TurtleRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class TurtleModel extends QuadrupedModel<TurtleRenderState> {
/*    */   public TurtleModel(ModelPart root, Function<Identifier, RenderType> renderType) {
/* 15 */     super(root, renderType);
/*    */   }
/*    */   
/*    */   public void setupAnim(TurtleRenderState state) {
/* 20 */     super.setupAnim((LivingEntityRenderState)state);
/* 22 */     float animationPos = state.walkAnimationPos;
/* 23 */     float animationSpeed = state.walkAnimationSpeed;
/* 25 */     if (state.isOnLand) {
/* 26 */       float layEgg = state.isLayingEgg ? 4.0F : 1.0F;
/* 27 */       float layEggAmplitude = state.isLayingEgg ? 2.0F : 1.0F;
/* 28 */       float swingPos = animationPos * 5.0F;
/* 29 */       float frontSwing = Mth.cos((layEgg * swingPos));
/* 30 */       float hindSwing = Mth.cos(swingPos);
/* 32 */       this.rightFrontLeg.yRot = -frontSwing * 8.0F * animationSpeed * layEggAmplitude;
/* 33 */       this.leftFrontLeg.yRot = frontSwing * 8.0F * animationSpeed * layEggAmplitude;
/* 34 */       this.rightHindLeg.yRot = -hindSwing * 3.0F * animationSpeed;
/* 35 */       this.leftHindLeg.yRot = hindSwing * 3.0F * animationSpeed;
/*    */     } else {
/* 37 */       float swingScale = 0.5F * animationSpeed;
/* 38 */       float swing = Mth.cos((animationPos * 0.6662F * 0.6F)) * swingScale;
/* 39 */       this.rightHindLeg.xRot = swing;
/* 40 */       this.leftHindLeg.xRot = -swing;
/* 41 */       this.rightFrontLeg.zRot = -swing;
/* 42 */       this.leftFrontLeg.zRot = swing;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\turtle\TurtleModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */