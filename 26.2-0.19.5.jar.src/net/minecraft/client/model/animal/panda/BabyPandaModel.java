/*    */ package net.minecraft.client.model.animal.panda;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.PandaRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class BabyPandaModel extends PandaModel {
/*    */   public BabyPandaModel(ModelPart root) {
/* 16 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 20 */     MeshDefinition mesh = new MeshDefinition();
/* 21 */     PartDefinition root = mesh.getRoot();
/* 23 */     root.addOrReplaceChild("body", 
/* 24 */         CubeListBuilder.create()
/* 25 */         .texOffs(0, 11).addBox(-4.5F, -3.5F, -5.5F, 9.0F, 7.0F, 11.0F), 
/* 26 */         PartPose.offset(0.0F, 18.5F, 2.5F));
/* 28 */     root.addOrReplaceChild("head", 
/* 29 */         CubeListBuilder.create()
/* 30 */         .texOffs(0, 0).addBox(-3.5F, -3.0F, -5.0F, 7.0F, 6.0F, 5.0F)
/* 31 */         .texOffs(24, 6).addBox(-2.0F, 1.0F, -6.0F, 4.0F, 2.0F, 1.0F)
/* 32 */         .texOffs(24, 0).addBox(-4.5F, -4.0F, -3.5F, 3.0F, 3.0F, 1.0F)
/* 33 */         .texOffs(33, 0).addBox(1.5F, -4.0F, -3.5F, 3.0F, 3.0F, 1.0F), 
/* 34 */         PartPose.offset(0.0F, 19.0F, -3.0F));
/* 36 */     root.addOrReplaceChild("right_hind_leg", 
/* 37 */         CubeListBuilder.create()
/* 38 */         .texOffs(0, 34).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F), 
/* 39 */         PartPose.offset(-3.0F, 22.0F, 6.5F));
/* 41 */     root.addOrReplaceChild("left_hind_leg", 
/* 42 */         CubeListBuilder.create()
/* 43 */         .texOffs(12, 34).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F), 
/* 44 */         PartPose.offset(3.0F, 22.0F, 6.5F));
/* 46 */     root.addOrReplaceChild("right_front_leg", 
/* 47 */         CubeListBuilder.create()
/* 48 */         .texOffs(0, 29).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F), 
/* 49 */         PartPose.offset(-3.0F, 22.0F, -1.5F));
/* 51 */     root.addOrReplaceChild("left_front_leg", 
/* 52 */         CubeListBuilder.create()
/* 53 */         .texOffs(12, 29).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F), 
/* 54 */         PartPose.offset(3.0F, 22.0F, -1.5F));
/* 57 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   protected void animateSitting(PandaRenderState state) {
/* 62 */     this.body.xRot = Mth.rotLerpRad(state.sitAmount, this.body.xRot, 0.17453292F);
/* 63 */     this.body.z = Mth.lerp(state.sitAmount, this.body.z, -1.5F);
/* 64 */     this.head.z = Mth.lerp(state.sitAmount, this.head.z, -11.5F);
/* 65 */     this.head.y = Mth.lerp(state.sitAmount, this.head.y, 17.5F);
/* 66 */     this.rightFrontLeg.z = Mth.lerp(state.sitAmount, this.rightFrontLeg.z, -5.0F);
/* 67 */     this.leftFrontLeg.z = Mth.lerp(state.sitAmount, this.leftFrontLeg.z, -5.0F);
/* 68 */     this.rightHindLeg.z = Mth.lerp(state.sitAmount, this.rightHindLeg.z, 3.0F);
/* 69 */     this.leftHindLeg.z = Mth.lerp(state.sitAmount, this.leftHindLeg.z, 3.0F);
/* 70 */     this.rightFrontLeg.zRot = -0.27079642F;
/* 71 */     this.leftFrontLeg.zRot = 0.27079642F;
/* 72 */     this.rightHindLeg.zRot = 0.5707964F;
/* 73 */     this.leftHindLeg.zRot = -0.5707964F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\panda\BabyPandaModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */