/*    */ package net.minecraft.client.model.animal.camel;
/*    */ 
/*    */ import net.minecraft.client.animation.AnimationDefinition;
/*    */ import net.minecraft.client.animation.KeyframeAnimation;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.CamelRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class CamelModel extends EntityModel<CamelRenderState> {
/*    */   private static final float MAX_WALK_ANIMATION_SPEED = 2.0F;
/*    */   
/*    */   private static final float WALK_ANIMATION_SCALE_FACTOR = 2.5F;
/*    */   
/*    */   protected final ModelPart head;
/*    */   
/*    */   private final KeyframeAnimation walkAnimation;
/*    */   
/*    */   private final KeyframeAnimation sitAnimation;
/*    */   
/*    */   private final KeyframeAnimation sitPoseAnimation;
/*    */   
/*    */   private final KeyframeAnimation standupAnimation;
/*    */   
/*    */   private final KeyframeAnimation idleAnimation;
/*    */   
/*    */   private final KeyframeAnimation dashAnimation;
/*    */   
/*    */   public CamelModel(ModelPart root, AnimationDefinition walk, AnimationDefinition sit, AnimationDefinition sitPose, AnimationDefinition standup, AnimationDefinition idle, AnimationDefinition dash) {
/* 34 */     super(root);
/* 35 */     ModelPart body = root.getChild("body");
/* 36 */     this.head = body.getChild("head");
/* 38 */     this.walkAnimation = walk.bake(root);
/* 39 */     this.sitAnimation = sit.bake(root);
/* 40 */     this.sitPoseAnimation = sitPose.bake(root);
/* 41 */     this.standupAnimation = standup.bake(root);
/* 42 */     this.idleAnimation = idle.bake(root);
/* 43 */     this.dashAnimation = dash.bake(root);
/*    */   }
/*    */   
/*    */   public void setupAnim(CamelRenderState state) {
/* 48 */     super.setupAnim(state);
/* 51 */     applyHeadRotation(state, state.yRot, state.xRot);
/* 53 */     this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 2.0F, 2.5F);
/* 55 */     this.sitAnimation.apply(state.sitAnimationState, state.ageInTicks);
/* 56 */     this.sitPoseAnimation.apply(state.sitPoseAnimationState, state.ageInTicks);
/* 57 */     this.standupAnimation.apply(state.sitUpAnimationState, state.ageInTicks);
/* 58 */     this.idleAnimation.apply(state.idleAnimationState, state.ageInTicks);
/* 59 */     this.dashAnimation.apply(state.dashAnimationState, state.ageInTicks);
/*    */   }
/*    */   
/*    */   private void applyHeadRotation(CamelRenderState state, float yRot, float xRot) {
/* 63 */     yRot = Mth.clamp(yRot, -30.0F, 30.0F);
/* 64 */     xRot = Mth.clamp(xRot, -25.0F, 45.0F);
/* 66 */     if (state.jumpCooldown > 0.0F) {
/* 67 */       float headRotation = 45.0F * state.jumpCooldown / 55.0F;
/* 68 */       xRot = Mth.clamp(xRot + headRotation, -25.0F, 70.0F);
/*    */     } 
/* 71 */     this.head.yRot = yRot * 0.017453292F;
/* 72 */     this.head.xRot = xRot * 0.017453292F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\camel\CamelModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */