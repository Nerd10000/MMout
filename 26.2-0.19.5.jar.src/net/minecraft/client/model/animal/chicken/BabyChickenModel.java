/*    */ package net.minecraft.client.model.animal.chicken;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class BabyChickenModel extends ChickenModel {
/*    */   public BabyChickenModel(ModelPart root) {
/* 14 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 18 */     MeshDefinition mesh = new MeshDefinition();
/* 19 */     PartDefinition root = mesh.getRoot();
/* 20 */     root.addOrReplaceChild("body", 
/* 21 */         CubeListBuilder.create()
/* 22 */         .texOffs(0, 0).addBox(-2.0F, -2.25F, -0.75F, 4.0F, 4.0F, 4.0F)
/* 23 */         .texOffs(10, 8).addBox(-1.0F, -0.25F, -1.75F, 2.0F, 1.0F, 1.0F), 
/* 24 */         PartPose.offset(0.0F, 20.25F, -1.25F));
/* 26 */     root.addOrReplaceChild("left_leg", 
/* 27 */         CubeListBuilder.create()
/* 28 */         .texOffs(2, 2).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F)
/* 29 */         .texOffs(0, 1).addBox(-0.5F, 2.0F, -1.0F, 1.0F, 0.0F, 1.0F), 
/* 30 */         PartPose.offset(1.0F, 22.0F, 0.5F));
/* 32 */     root.addOrReplaceChild("right_leg", 
/* 33 */         CubeListBuilder.create()
/* 34 */         .texOffs(0, 2).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F)
/* 35 */         .texOffs(0, 0).addBox(-0.5F, 2.0F, -1.0F, 1.0F, 0.0F, 1.0F), 
/* 36 */         PartPose.offset(-1.0F, 22.0F, 0.5F));
/* 38 */     root.addOrReplaceChild("right_wing", 
/* 39 */         CubeListBuilder.create()
/* 40 */         .texOffs(6, 8).addBox(0.0F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F), 
/* 41 */         PartPose.offset(2.0F, 20.0F, 0.0F));
/* 43 */     root.addOrReplaceChild("left_wing", 
/* 44 */         CubeListBuilder.create()
/* 45 */         .texOffs(4, 8).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F), 
/* 46 */         PartPose.offset(-2.0F, 20.0F, 0.0F));
/* 49 */     return LayerDefinition.create(mesh, 16, 16);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\chicken\BabyChickenModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */