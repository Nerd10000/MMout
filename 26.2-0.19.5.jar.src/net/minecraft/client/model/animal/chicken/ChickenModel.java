/*    */ package net.minecraft.client.model.animal.chicken;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.ChickenRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class ChickenModel extends EntityModel<ChickenRenderState> {
/*    */   public static final float Y_OFFSET = 16.0F;
/*    */   
/*    */   private final ModelPart rightLeg;
/*    */   
/*    */   private final ModelPart leftLeg;
/*    */   
/*    */   private final ModelPart rightWing;
/*    */   
/*    */   private final ModelPart leftWing;
/*    */   
/*    */   public ChickenModel(ModelPart root) {
/* 18 */     super(root);
/* 19 */     this.rightLeg = root.getChild("right_leg");
/* 20 */     this.leftLeg = root.getChild("left_leg");
/* 21 */     this.rightWing = root.getChild("right_wing");
/* 22 */     this.leftWing = root.getChild("left_wing");
/*    */   }
/*    */   
/*    */   public void setupAnim(ChickenRenderState state) {
/* 27 */     super.setupAnim(state);
/* 29 */     float flapAngle = (Mth.sin(state.flap) + 1.0F) * state.flapSpeed;
/* 30 */     float animationSpeed = state.walkAnimationSpeed;
/* 31 */     float animationPos = state.walkAnimationPos;
/* 32 */     this.rightLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/* 33 */     this.leftLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 34 */     this.rightWing.zRot = flapAngle;
/* 35 */     this.leftWing.zRot = -flapAngle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\chicken\ChickenModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */