/*    */ package net.minecraft.client.model.animal.armadillo;
/*    */ 
/*    */ import net.minecraft.client.animation.definitions.ArmadilloAnimation;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class AdultArmadilloModel extends ArmadilloModel {
/*    */   public AdultArmadilloModel(ModelPart root) {
/* 16 */     super(root, ArmadilloAnimation.ARMADILLO_WALK, ArmadilloAnimation.ARMADILLO_ROLL_OUT, ArmadilloAnimation.ARMADILLO_ROLL_UP, ArmadilloAnimation.ARMADILLO_PEEK);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 27 */     MeshDefinition mesh = new MeshDefinition();
/* 28 */     PartDefinition root = mesh.getRoot();
/* 30 */     PartDefinition body = root.addOrReplaceChild("body", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(0, 20).addBox(-4.0F, -7.0F, -10.0F, 8.0F, 8.0F, 12.0F, new CubeDeformation(0.3F))
/* 33 */         .texOffs(0, 40).addBox(-4.0F, -7.0F, -10.0F, 8.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)), 
/* 34 */         PartPose.offset(0.0F, 21.0F, 4.0F));
/* 36 */     body.addOrReplaceChild("tail", 
/* 37 */         CubeListBuilder.create()
/* 38 */         .texOffs(44, 53).addBox(-0.5F, -0.0865F, 0.0933F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), 
/* 39 */         PartPose.offsetAndRotation(0.0F, -3.0F, 1.0F, 0.5061F, 0.0F, 0.0F));
/* 41 */     PartDefinition head = body.addOrReplaceChild("head", 
/* 42 */         CubeListBuilder.create(), 
/* 43 */         PartPose.offset(0.0F, -2.0F, -11.0F));
/* 45 */     head.addOrReplaceChild("head_cube", 
/* 46 */         CubeListBuilder.create()
/* 47 */         .texOffs(43, 15).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), 
/* 48 */         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
/* 50 */     PartDefinition rightEar = head.addOrReplaceChild("right_ear", 
/* 51 */         CubeListBuilder.create(), 
/* 52 */         PartPose.offset(-1.0F, -1.0F, 0.0F));
/* 54 */     rightEar.addOrReplaceChild("right_ear_cube", 
/* 55 */         CubeListBuilder.create()
/* 56 */         .texOffs(43, 10).addBox(-2.0F, -3.0F, 0.0F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), 
/* 57 */         PartPose.offsetAndRotation(-0.5F, 0.0F, -0.6F, 0.1886F, -0.3864F, -0.0718F));
/* 59 */     PartDefinition leftEar = head.addOrReplaceChild("left_ear", 
/* 60 */         CubeListBuilder.create(), 
/* 61 */         PartPose.offset(1.0F, -2.0F, 0.0F));
/* 63 */     leftEar.addOrReplaceChild("left_ear_cube", 
/* 64 */         CubeListBuilder.create()
/* 65 */         .texOffs(47, 10).addBox(0.0F, -3.0F, 0.0F, 2.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), 
/* 66 */         PartPose.offsetAndRotation(0.5F, 1.0F, -0.6F, 0.1886F, 0.3864F, 0.0718F));
/* 68 */     root.addOrReplaceChild("right_hind_leg", 
/* 69 */         CubeListBuilder.create()
/* 70 */         .texOffs(51, 31).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), 
/* 71 */         PartPose.offset(-2.0F, 21.0F, 4.0F));
/* 73 */     root.addOrReplaceChild("left_hind_leg", 
/* 74 */         CubeListBuilder.create()
/* 75 */         .texOffs(42, 31).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), 
/* 76 */         PartPose.offset(2.0F, 21.0F, 4.0F));
/* 78 */     root.addOrReplaceChild("right_front_leg", 
/* 79 */         CubeListBuilder.create()
/* 80 */         .texOffs(51, 43).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), 
/* 81 */         PartPose.offset(-2.0F, 21.0F, -4.0F));
/* 83 */     root.addOrReplaceChild("left_front_leg", 
/* 84 */         CubeListBuilder.create()
/* 85 */         .texOffs(42, 43).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), 
/* 86 */         PartPose.offset(2.0F, 21.0F, -4.0F));
/* 88 */     root.addOrReplaceChild("cube", 
/* 89 */         CubeListBuilder.create()
/* 90 */         .texOffs(0, 0).addBox(-5.0F, -10.0F, -6.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), 
/* 91 */         PartPose.offset(0.0F, 24.0F, 0.0F));
/* 94 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\armadillo\AdultArmadilloModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */