/*    */ package net.minecraft.client.model.animal.rabbit;
/*    */ 
/*    */ import net.minecraft.client.animation.AnimationDefinition;
/*    */ import net.minecraft.client.animation.KeyframeAnimation;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.RabbitRenderState;
/*    */ 
/*    */ public abstract class RabbitModel extends EntityModel<RabbitRenderState> {
/*    */   protected static final String FRONT_LEGS = "frontlegs";
/*    */   
/*    */   protected static final String BACK_LEGS = "backlegs";
/*    */   
/*    */   protected static final String LEFT_HAUNCH = "left_haunch";
/*    */   
/*    */   protected static final String RIGHT_HAUNCH = "right_haunch";
/*    */   
/*    */   private final KeyframeAnimation hopAnimation;
/*    */   
/*    */   private final KeyframeAnimation idleHeadTiltAnimation;
/*    */   
/*    */   private final ModelPart head;
/*    */   
/*    */   public RabbitModel(ModelPart root, AnimationDefinition hop, AnimationDefinition idleHeadTilt) {
/* 29 */     super(root);
/* 30 */     this.head = root.getChild("body").getChild("head");
/* 31 */     this.hopAnimation = hop.bake(root);
/* 32 */     this.idleHeadTiltAnimation = idleHeadTilt.bake(root);
/*    */   }
/*    */   
/*    */   public void setupAnim(RabbitRenderState state) {
/* 37 */     super.setupAnim(state);
/* 38 */     if (!state.idleHeadTiltAnimationState.isStarted()) {
/* 39 */       this.head.yRot = state.yRot * 0.017453292F;
/* 40 */       this.head.xRot = state.xRot * 0.017453292F;
/*    */     } 
/* 42 */     this.hopAnimation.apply(state.hopAnimationState, state.ageInTicks);
/* 43 */     this.idleHeadTiltAnimation.apply(state.idleHeadTiltAnimationState, state.ageInTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\rabbit\RabbitModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */