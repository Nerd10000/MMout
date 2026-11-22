/*    */ package net.minecraft.client.model.animal.wolf;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.WolfRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class WolfModel extends EntityModel<WolfRenderState> {
/*    */   protected final ModelPart head;
/*    */   
/*    */   protected final ModelPart body;
/*    */   
/*    */   protected final ModelPart rightHindLeg;
/*    */   
/*    */   protected final ModelPart leftHindLeg;
/*    */   
/*    */   protected final ModelPart rightFrontLeg;
/*    */   
/*    */   protected final ModelPart leftFrontLeg;
/*    */   
/*    */   protected final ModelPart tail;
/*    */   
/*    */   public WolfModel(ModelPart root) {
/* 21 */     super(root);
/* 22 */     this.head = root.getChild("head");
/* 23 */     this.body = root.getChild("body");
/* 24 */     this.rightHindLeg = root.getChild("right_hind_leg");
/* 25 */     this.leftHindLeg = root.getChild("left_hind_leg");
/* 26 */     this.rightFrontLeg = root.getChild("right_front_leg");
/* 27 */     this.leftFrontLeg = root.getChild("left_front_leg");
/* 28 */     this.tail = root.getChild("tail");
/*    */   }
/*    */   
/*    */   public void setupAnim(WolfRenderState state) {
/* 33 */     super.setupAnim(state);
/* 35 */     float animationPos = state.walkAnimationPos;
/* 36 */     float animationSpeed = state.walkAnimationSpeed;
/* 38 */     if (state.isAngry) {
/* 39 */       this.tail.yRot = 0.0F;
/*    */     } else {
/* 41 */       this.tail.yRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/*    */     } 
/* 44 */     if (state.isSitting) {
/* 45 */       setSittingPose(state);
/*    */     } else {
/* 47 */       this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/* 48 */       this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 49 */       this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 50 */       this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/*    */     } 
/* 53 */     shakeOffWater(state);
/* 55 */     this.head.xRot = state.xRot * 0.017453292F;
/* 56 */     this.head.yRot = state.yRot * 0.017453292F;
/* 58 */     this.tail.xRot = state.tailAngle;
/*    */   }
/*    */   
/*    */   protected void shakeOffWater(WolfRenderState state) {
/* 62 */     this.body.zRot = state.getBodyRollAngle(-0.16F);
/*    */   }
/*    */   
/*    */   protected void setSittingPose(WolfRenderState state) {
/* 66 */     float ageScale = state.ageScale;
/* 68 */     this.body.y += 4.0F * ageScale;
/* 69 */     this.body.z -= 2.0F * ageScale;
/* 70 */     this.body.xRot = 0.7853982F;
/* 72 */     this.tail.y += 9.0F * ageScale;
/* 73 */     this.tail.z -= 2.0F * ageScale;
/* 75 */     this.rightHindLeg.y += 6.7F * ageScale;
/* 76 */     this.rightHindLeg.z -= 5.0F * ageScale;
/* 77 */     this.rightHindLeg.xRot = 4.712389F;
/* 78 */     this.leftHindLeg.y += 6.7F * ageScale;
/* 79 */     this.leftHindLeg.z -= 5.0F * ageScale;
/* 80 */     this.leftHindLeg.xRot = 4.712389F;
/* 82 */     this.rightFrontLeg.xRot = 5.811947F;
/* 83 */     this.rightFrontLeg.x += 0.01F * ageScale;
/* 84 */     this.rightFrontLeg.y += 1.0F * ageScale;
/* 85 */     this.leftFrontLeg.xRot = 5.811947F;
/* 86 */     this.leftFrontLeg.x -= 0.01F * ageScale;
/* 87 */     this.leftFrontLeg.y += 1.0F * ageScale;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\wolf\WolfModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */