/*    */ package net.minecraft.client.model.animal.pig;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class BabyPigModel extends PigModel {
/*    */   public BabyPigModel(ModelPart root) {
/* 15 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(CubeDeformation g) {
/* 19 */     MeshDefinition mesh = new MeshDefinition();
/* 20 */     PartDefinition root = mesh.getRoot();
/* 22 */     root.addOrReplaceChild("body", 
/* 23 */         CubeListBuilder.create()
/* 24 */         .texOffs(0, 0).addBox(-3.5F, -3.0F, -4.5F, 7.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)), 
/* 25 */         PartPose.offset(0.0F, 19.0F, 0.5F));
/* 27 */     root.addOrReplaceChild("head", 
/* 28 */         CubeListBuilder.create()
/* 29 */         .texOffs(0, 15).addBox(-3.5F, -5.0F, -5.0F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.025F))
/* 30 */         .texOffs(6, 27).addBox(-1.5F, -1.975F, -6.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.015F)), 
/* 31 */         PartPose.offset(0.0F, 19.0F, -2.0F));
/* 33 */     root.addOrReplaceChild("left_front_leg", 
/* 34 */         CubeListBuilder.create()
/* 35 */         .texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), 
/* 36 */         PartPose.offset(2.5F, 22.0F, -3.0F));
/* 38 */     root.addOrReplaceChild("right_front_leg", 
/* 39 */         CubeListBuilder.create()
/* 40 */         .texOffs(23, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), 
/* 41 */         PartPose.offset(-2.5F, 22.0F, -3.0F));
/* 43 */     root.addOrReplaceChild("left_hind_leg", 
/* 44 */         CubeListBuilder.create()
/* 45 */         .texOffs(0, 4).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), 
/* 46 */         PartPose.offset(2.5F, 22.0F, 4.0F));
/* 48 */     root.addOrReplaceChild("right_hind_leg", 
/* 49 */         CubeListBuilder.create()
/* 50 */         .texOffs(23, 4).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), 
/* 51 */         PartPose.offset(-2.5F, 22.0F, 4.0F));
/* 54 */     return LayerDefinition.create(mesh, 32, 32);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\pig\BabyPigModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */