/*    */ package net.minecraft.client.model.animal.armadillo;
/*    */ 
/*    */ import net.minecraft.client.animation.definitions.BabyArmadilloAnimation;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class BabyArmadilloModel extends ArmadilloModel {
/*    */   public BabyArmadilloModel(ModelPart root) {
/* 17 */     super(root, BabyArmadilloAnimation.ARMADILLO_BABY_WALK, BabyArmadilloAnimation.ARMADILLO_BABY_ROLL_OUT, BabyArmadilloAnimation.ARMADILLO_BABY_ROLL_UP, BabyArmadilloAnimation.ARMADILLO_BABY_PEEK);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 27 */     MeshDefinition mesh = new MeshDefinition();
/* 28 */     PartDefinition root = mesh.getRoot();
/* 30 */     PartDefinition body = root.addOrReplaceChild("body", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(0, 0).addBox(-2.5F, -2.0F, -3.5F, 5.0F, 4.0F, 7.0F, new CubeDeformation(0.3F))
/* 33 */         .texOffs(0, 11).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 6.0F), 
/* 34 */         PartPose.offset(0.0F, 20.0F, 0.5F));
/* 36 */     PartDefinition tail = body.addOrReplaceChild("tail", 
/* 37 */         CubeListBuilder.create(), 
/* 38 */         PartPose.offset(0.0F, 0.0F, 3.4F));
/* 40 */     tail.addOrReplaceChild("right_ear_cube", 
/* 41 */         CubeListBuilder.create()
/* 42 */         .texOffs(22, 11).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F), 
/* 43 */         PartPose.offsetAndRotation(0.0F, 1.5F, 1.0F, -1.0472F, 0.0F, 0.0F));
/* 45 */     PartDefinition head = body.addOrReplaceChild("head", 
/* 46 */         CubeListBuilder.create(), 
/* 47 */         PartPose.offset(0.0F, 0.0F, -3.2F));
/* 49 */     PartDefinition headGroup = head.addOrReplaceChild("head_cube", 
/* 50 */         CubeListBuilder.create()
/* 51 */         .texOffs(20, 17).addBox(-1.0F, -2.0F, -4.0F, 2.0F, 2.0F, 4.0F), 
/* 52 */         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7417649F, 0.0F, 0.0F));
/* 54 */     headGroup.addOrReplaceChild("right_ear", 
/* 55 */         CubeListBuilder.create()
/* 56 */         .texOffs(28, 8).mirror().addBox(-1.8F, -2.0F, 0.0F, 2.0F, 3.0F, 0.0F).mirror(false), 
/* 57 */         PartPose.offsetAndRotation(-1.0F, -2.0F, -0.3F, -0.4363F, -0.1134F, 0.0524F));
/* 59 */     headGroup.addOrReplaceChild("left_ear", 
/* 60 */         CubeListBuilder.create()
/* 61 */         .texOffs(28, 8).addBox(-0.2F, -2.0F, 0.0F, 2.0F, 3.0F, 0.0F), 
/* 62 */         PartPose.offsetAndRotation(1.0F, -2.0F, -0.3F, -0.4363F, 0.1134F, -0.0524F));
/* 64 */     root.addOrReplaceChild("right_hind_leg", 
/* 65 */         CubeListBuilder.create()
/* 66 */         .texOffs(20, 27).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F).mirror(false), 
/* 67 */         PartPose.offset(-1.5F, 22.0F, 2.5F));
/* 69 */     root.addOrReplaceChild("left_hind_leg", 
/* 70 */         CubeListBuilder.create()
/* 71 */         .texOffs(20, 27).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F), 
/* 72 */         PartPose.offset(1.5F, 22.0F, 2.5F));
/* 74 */     root.addOrReplaceChild("right_front_leg", 
/* 75 */         CubeListBuilder.create()
/* 76 */         .texOffs(20, 23).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F), 
/* 77 */         PartPose.offset(1.5F, 22.0F, -1.5F));
/* 79 */     root.addOrReplaceChild("left_front_leg", 
/* 80 */         CubeListBuilder.create()
/* 81 */         .texOffs(24, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F).mirror(false), 
/* 82 */         PartPose.offset(-1.5F, 22.0F, -1.5F));
/* 84 */     root.addOrReplaceChild("cube", 
/* 85 */         CubeListBuilder.create()
/* 86 */         .texOffs(0, 25).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.3F)), 
/* 87 */         PartPose.offset(0.0F, 20.7F, 0.5F));
/* 90 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\armadillo\BabyArmadilloModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */