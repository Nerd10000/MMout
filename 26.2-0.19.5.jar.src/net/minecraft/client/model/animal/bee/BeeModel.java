/*    */ package net.minecraft.client.model.animal.bee;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.BeeRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class BeeModel extends EntityModel<BeeRenderState> {
/*    */   protected static final String BONE = "bone";
/*    */   
/*    */   protected static final String STINGER = "stinger";
/*    */   
/*    */   protected static final String FRONT_LEGS = "front_legs";
/*    */   
/*    */   protected static final String MIDDLE_LEGS = "middle_legs";
/*    */   
/*    */   protected static final String BACK_LEGS = "back_legs";
/*    */   
/*    */   protected final ModelPart bone;
/*    */   
/*    */   private final ModelPart rightWing;
/*    */   
/*    */   private final ModelPart leftWing;
/*    */   
/*    */   private final ModelPart frontLeg;
/*    */   
/*    */   private final ModelPart midLeg;
/*    */   
/*    */   private final ModelPart backLeg;
/*    */   
/*    */   private final ModelPart stinger;
/*    */   
/*    */   public BeeModel(ModelPart root) {
/* 26 */     super(root);
/* 27 */     this.bone = root.getChild("bone");
/* 29 */     ModelPart body = this.bone.getChild("body");
/* 30 */     this.stinger = body.getChild("stinger");
/* 32 */     this.rightWing = this.bone.getChild("right_wing");
/* 33 */     this.leftWing = this.bone.getChild("left_wing");
/* 35 */     this.frontLeg = this.bone.getChild("front_legs");
/* 36 */     this.midLeg = this.bone.getChild("middle_legs");
/* 37 */     this.backLeg = this.bone.getChild("back_legs");
/*    */   }
/*    */   
/*    */   public void setupAnim(BeeRenderState state) {
/* 42 */     super.setupAnim(state);
/* 45 */     this.stinger.visible = state.hasStinger;
/* 47 */     if (!state.isOnGround) {
/* 48 */       float speed = state.ageInTicks * 120.32113F * 0.017453292F;
/* 49 */       this.rightWing.yRot = 0.0F;
/* 50 */       this.rightWing.zRot = Mth.cos(speed) * 3.1415927F * 0.15F;
/* 52 */       this.leftWing.xRot = this.rightWing.xRot;
/* 53 */       this.leftWing.yRot = this.rightWing.yRot;
/* 54 */       this.leftWing.zRot = -this.rightWing.zRot;
/* 56 */       this.frontLeg.xRot = 0.7853982F;
/* 57 */       this.midLeg.xRot = 0.7853982F;
/* 58 */       this.backLeg.xRot = 0.7853982F;
/*    */     } 
/* 61 */     if (!state.isAngry && !state.isOnGround) {
/* 62 */       float speed = Mth.cos((state.ageInTicks * 0.18F));
/* 63 */       bobUpAndDown(speed, state.ageInTicks);
/*    */     } 
/* 66 */     float rollAmount = state.rollAmount;
/* 67 */     if (rollAmount > 0.0F)
/* 68 */       this.bone.xRot = Mth.rotLerpRad(rollAmount, this.bone.xRot, 3.0915928F); 
/*    */   }
/*    */   
/*    */   protected void bobUpAndDown(float speed, float ageInTicks) {
/* 73 */     this.bone.xRot = 0.1F + speed * 3.1415927F * 0.025F;
/* 74 */     this.bone.y -= Mth.cos((ageInTicks * 0.18F)) * 0.9F;
/* 76 */     this.frontLeg.xRot = -speed * 3.1415927F * 0.1F + 0.3926991F;
/* 77 */     this.backLeg.xRot = -speed * 3.1415927F * 0.05F + 0.7853982F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\bee\BeeModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */