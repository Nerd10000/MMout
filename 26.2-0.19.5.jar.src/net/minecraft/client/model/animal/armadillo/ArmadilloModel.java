/*    */ package net.minecraft.client.model.animal.armadillo;
/*    */ 
/*    */ import net.minecraft.client.animation.AnimationDefinition;
/*    */ import net.minecraft.client.animation.KeyframeAnimation;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.ArmadilloRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class ArmadilloModel extends EntityModel<ArmadilloRenderState> {
/*    */   private static final float MAX_DOWN_HEAD_ROTATION_EXTENT = 25.0F;
/*    */   
/*    */   private static final float MAX_UP_HEAD_ROTATION_EXTENT = 22.5F;
/*    */   
/*    */   private static final float MAX_WALK_ANIMATION_SPEED = 16.5F;
/*    */   
/*    */   private static final float WALK_ANIMATION_SCALE_FACTOR = 2.5F;
/*    */   
/*    */   protected static final String HEAD_CUBE = "head_cube";
/*    */   
/*    */   protected static final String RIGHT_EAR_CUBE = "right_ear_cube";
/*    */   
/*    */   protected static final String LEFT_EAR_CUBE = "left_ear_cube";
/*    */   
/*    */   private final ModelPart body;
/*    */   
/*    */   private final ModelPart rightHindLeg;
/*    */   
/*    */   private final ModelPart leftHindLeg;
/*    */   
/*    */   private final ModelPart cube;
/*    */   
/*    */   private final ModelPart head;
/*    */   
/*    */   private final ModelPart tail;
/*    */   
/*    */   private final KeyframeAnimation walkAnimation;
/*    */   
/*    */   private final KeyframeAnimation rollOutAnimation;
/*    */   
/*    */   private final KeyframeAnimation rollUpAnimation;
/*    */   
/*    */   private final KeyframeAnimation peekAnimation;
/*    */   
/*    */   public ArmadilloModel(ModelPart root, AnimationDefinition walk, AnimationDefinition rollOut, AnimationDefinition rollUp, AnimationDefinition peek) {
/* 41 */     super(root);
/* 43 */     this.body = root.getChild("body");
/* 44 */     this.rightHindLeg = root.getChild("right_hind_leg");
/* 45 */     this.leftHindLeg = root.getChild("left_hind_leg");
/* 46 */     this.head = this.body.getChild("head");
/* 47 */     this.tail = this.body.getChild("tail");
/* 48 */     this.cube = root.getChild("cube");
/* 50 */     this.walkAnimation = walk.bake(root);
/* 51 */     this.rollOutAnimation = rollOut.bake(root);
/* 52 */     this.rollUpAnimation = rollUp.bake(root);
/* 53 */     this.peekAnimation = peek.bake(root);
/*    */   }
/*    */   
/*    */   public void setupAnim(ArmadilloRenderState state) {
/* 58 */     super.setupAnim(state);
/* 59 */     if (state.isHidingInShell) {
/* 60 */       this.body.skipDraw = true;
/* 61 */       this.leftHindLeg.visible = false;
/* 62 */       this.rightHindLeg.visible = false;
/* 63 */       this.tail.visible = false;
/* 64 */       this.cube.visible = true;
/*    */     } else {
/* 66 */       this.body.skipDraw = false;
/* 67 */       this.leftHindLeg.visible = true;
/* 68 */       this.rightHindLeg.visible = true;
/* 69 */       this.tail.visible = true;
/* 70 */       this.cube.visible = false;
/* 72 */       this.head.xRot = Mth.clamp(state.xRot, -22.5F, 25.0F) * 0.017453292F;
/* 73 */       this.head.yRot = Mth.clamp(state.yRot, -32.5F, 32.5F) * 0.017453292F;
/*    */     } 
/* 76 */     if (!state.isHidingInShell)
/* 77 */       this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 16.5F, 2.5F); 
/* 80 */     this.rollOutAnimation.apply(state.rollOutAnimationState, state.ageInTicks);
/* 81 */     this.rollUpAnimation.apply(state.rollUpAnimationState, state.ageInTicks);
/* 82 */     this.peekAnimation.apply(state.peekAnimationState, state.ageInTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\armadillo\ArmadilloModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */