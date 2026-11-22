/*    */ package net.minecraft.client.model.animal.equine;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.DonkeyRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EquineRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class BabyDonkeyModel extends DonkeyModel {
/*    */   public BabyDonkeyModel(ModelPart root) {
/* 24 */     super(root, headParts, rightHindLeg, rightFrontLeg, leftHindLeg, leftFrontLeg, tail);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBabyLayer() {
/* 28 */     MeshDefinition meshdefinition = new MeshDefinition();
/* 29 */     PartDefinition partdefinition = meshdefinition.getRoot();
/* 31 */     PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 13).addBox(-5.0F, -3.0F, -7.0F, 8.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 14.0F, 0.0F));
/* 32 */     PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5F, 6.5F));
/* 33 */     tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(24, 33).addBox(-2.5F, -1.0F, -0.5F, 3.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7418F, 0.0F, 0.0F));
/* 34 */     body.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(12, 44).addBox(-2.5F, -1.5F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.25F, 3.5F, 5.25F));
/* 35 */     body.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 44).addBox(-2.5F, -1.5F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.4F, 3.5F, 5.4F));
/* 36 */     body.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(12, 33).addBox(-2.5F, -1.5F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.4F, 3.5F, -5.3F));
/* 37 */     body.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 33).addBox(-2.5F, -1.5F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.4F, 3.5F, -5.4F));
/* 39 */     PartDefinition neck = body.addOrReplaceChild("head_parts", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, -5.0F));
/* 40 */     neck.addOrReplaceChild("neck_r1", CubeListBuilder.create().texOffs(30, 9).addBox(-3.0F, -6.0F, -3.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
/* 41 */     PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, -3.0F));
/* 42 */     head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -3.6F, -8.4F, 6.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.3927F, 0.0F, 0.0F));
/* 43 */     head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.5F, -0.3F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -3.5F, -1.0F, 0.48F, 0.0F, 0.48F));
/* 44 */     head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(22, 0).mirror().addBox(-2.0F, -6.5F, -0.3F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -3.5F, -1.0F, 0.48F, 0.0F, -0.48F));
/* 45 */     body.addOrReplaceChild("right_chest", CubeListBuilder.create(), PartPose.offset(-1.0F, 10.0F, 0.0F));
/* 46 */     body.addOrReplaceChild("left_chest", CubeListBuilder.create(), PartPose.offset(-1.0F, 10.0F, 0.0F));
/* 47 */     return LayerDefinition.create(meshdefinition, 64, 64);
/*    */   }
/*    */   
/*    */   public void setupAnim(DonkeyRenderState state) {
/* 52 */     super.setupAnim(state);
/* 54 */     state.xRot = -30.0F;
/* 57 */     float headRotXRad = state.xRot * 0.017453292F;
/* 58 */     float eating = state.eatAnimation;
/* 59 */     float standing = state.standAnimation;
/* 60 */     float feedingAnim = state.feedingAnimation;
/* 61 */     float baseHeadAngle = (1.0F - Math.max(standing, eating)) * (0.5235988F + headRotXRad + feedingAnim * Mth.sin(state.ageInTicks) * 0.05F);
/* 62 */     this.headParts.xRot = standing * (0.2617994F + headRotXRad) + eating * (1.5707964F + Mth.sin(state.ageInTicks) * 0.05F) + baseHeadAngle;
/*    */   }
/*    */   
/*    */   protected void offsetLegPositionWhenStanding(float standing) {
/* 67 */     this.leftHindLeg.y = Mth.lerp(standing, this.leftHindLeg.y, -0.3F);
/* 68 */     this.rightHindLeg.y = Mth.lerp(standing, this.leftHindLeg.y, -0.3F);
/*    */   }
/*    */   
/*    */   protected float getLegStandAngle() {
/* 73 */     return 1.0471976F;
/*    */   }
/*    */   
/*    */   protected float getLegStandingYOffset() {
/* 78 */     return 1.0F;
/*    */   }
/*    */   
/*    */   protected float getLegStandingZOffset() {
/* 83 */     return 0.5F;
/*    */   }
/*    */   
/*    */   protected float getLegStandingXRotOffset() {
/* 88 */     return 0.0F;
/*    */   }
/*    */   
/*    */   protected float getTailXRotOffset() {
/* 93 */     return -0.7853982F;
/*    */   }
/*    */   
/*    */   protected void animateHeadPartsPlacement(float eating, float standing) {
/* 98 */     this.headParts.y = Mth.lerp(eating, this.headParts.y, -1.2F);
/* 99 */     this.headParts.z = Mth.lerp(standing, this.headParts.z, -3.6F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\equine\BabyDonkeyModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */