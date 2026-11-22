/*    */ package net.minecraft.client.model.animal.polarbear;
/*    */ 
/*    */ import net.minecraft.client.model.QuadrupedModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.PolarBearRenderState;
/*    */ 
/*    */ public class PolarBearModel extends QuadrupedModel<PolarBearRenderState> {
/*    */   public PolarBearModel(ModelPart root) {
/* 18 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 22 */     MeshDefinition mesh = new MeshDefinition();
/* 23 */     PartDefinition root = mesh.getRoot();
/* 25 */     root.addOrReplaceChild("head", 
/* 26 */         CubeListBuilder.create()
/* 27 */         .texOffs(0, 0).addBox(-3.5F, -3.0F, -3.0F, 7.0F, 7.0F, 7.0F)
/* 28 */         .texOffs(0, 44).addBox("mouth", -2.5F, 1.0F, -6.0F, 5.0F, 3.0F, 3.0F)
/* 29 */         .texOffs(26, 0).addBox("right_ear", -4.5F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F)
/* 30 */         .texOffs(26, 0).mirror().addBox("left_ear", 2.5F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F), 
/* 31 */         PartPose.offset(0.0F, 10.0F, -16.0F));
/* 33 */     root.addOrReplaceChild("body", 
/* 34 */         CubeListBuilder.create()
/* 35 */         .texOffs(0, 19).addBox(-5.0F, -13.0F, -7.0F, 14.0F, 14.0F, 11.0F)
/* 36 */         .texOffs(39, 0).addBox(-4.0F, -25.0F, -7.0F, 12.0F, 12.0F, 10.0F), 
/* 37 */         PartPose.offsetAndRotation(-2.0F, 9.0F, 12.0F, 1.5707964F, 0.0F, 0.0F));
/* 40 */     int legSize = 10;
/* 42 */     CubeListBuilder hindLeg = CubeListBuilder.create().texOffs(50, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 8.0F);
/* 43 */     root.addOrReplaceChild("right_hind_leg", hindLeg, PartPose.offset(-4.5F, 14.0F, 6.0F));
/* 44 */     root.addOrReplaceChild("left_hind_leg", hindLeg, PartPose.offset(4.5F, 14.0F, 6.0F));
/* 47 */     CubeListBuilder frontLeg = CubeListBuilder.create().texOffs(50, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 6.0F);
/* 48 */     root.addOrReplaceChild("right_front_leg", frontLeg, PartPose.offset(-3.5F, 14.0F, -8.0F));
/* 49 */     root.addOrReplaceChild("left_front_leg", frontLeg, PartPose.offset(3.5F, 14.0F, -8.0F));
/* 51 */     return LayerDefinition.create(mesh, 128, 64)
/* 52 */       .apply(MeshTransformer.scaling(1.2F));
/*    */   }
/*    */   
/*    */   public void setupAnim(PolarBearRenderState state) {
/* 57 */     super.setupAnim((LivingEntityRenderState)state);
/* 59 */     float standScale = state.standScale * state.standScale;
/* 60 */     float bodyAgeScale = state.ageScale;
/* 62 */     this.body.xRot -= standScale * 3.1415927F * 0.35F;
/* 63 */     this.body.y += standScale * bodyAgeScale * 2.0F;
/* 65 */     this.rightFrontLeg.y -= standScale * bodyAgeScale * 20.0F;
/* 66 */     this.rightFrontLeg.z += standScale * bodyAgeScale * 4.0F;
/* 67 */     this.rightFrontLeg.xRot -= standScale * 3.1415927F * 0.45F;
/* 69 */     this.leftFrontLeg.y = this.rightFrontLeg.y;
/* 70 */     this.leftFrontLeg.z = this.rightFrontLeg.z;
/* 71 */     this.leftFrontLeg.xRot -= standScale * 3.1415927F * 0.45F;
/* 73 */     this.head.y -= standScale * 24.0F;
/* 74 */     this.head.z += standScale * 13.0F;
/* 75 */     this.head.xRot += standScale * 3.1415927F * 0.15F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\polarbear\PolarBearModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */