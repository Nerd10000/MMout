/*    */ package net.minecraft.client.model.animal.polarbear;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class BabyPolarBearModel extends PolarBearModel {
/*    */   public BabyPolarBearModel(ModelPart root) {
/* 14 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 18 */     MeshDefinition mesh = new MeshDefinition();
/* 19 */     PartDefinition root = mesh.getRoot();
/* 21 */     root.addOrReplaceChild("body", 
/* 22 */         CubeListBuilder.create()
/* 23 */         .texOffs(0, 9).addBox(-4.0F, -3.5F, -6.0F, 8.0F, 7.0F, 12.0F), 
/* 24 */         PartPose.offset(0.0F, 17.5F, 0.0F));
/* 26 */     root.addOrReplaceChild("head", 
/* 27 */         CubeListBuilder.create()
/* 28 */         .texOffs(0, 0).addBox(-3.0F, -2.625F, -4.25F, 6.0F, 5.0F, 4.0F)
/* 29 */         .texOffs(20, 3).addBox(-2.0F, 0.375F, -6.25F, 4.0F, 2.0F, 2.0F)
/* 30 */         .texOffs(20, 0).addBox(-4.0F, -3.625F, -2.75F, 2.0F, 2.0F, 1.0F)
/* 31 */         .texOffs(26, 0).addBox(2.0F, -3.625F, -2.75F, 2.0F, 2.0F, 1.0F), 
/* 32 */         PartPose.offset(0.0F, 18.625F, -5.75F));
/* 34 */     root.addOrReplaceChild("right_hind_leg", 
/* 35 */         CubeListBuilder.create()
/* 36 */         .texOffs(0, 34).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 3.0F, 3.0F), 
/* 37 */         PartPose.offset(-2.5F, 21.5F, 4.5F));
/* 39 */     root.addOrReplaceChild("left_hind_leg", 
/* 40 */         CubeListBuilder.create()
/* 41 */         .texOffs(12, 34).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 3.0F, 3.0F), 
/* 42 */         PartPose.offset(2.5F, 21.5F, 4.5F));
/* 44 */     root.addOrReplaceChild("right_front_leg", 
/* 45 */         CubeListBuilder.create()
/* 46 */         .texOffs(0, 28).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 3.0F, 3.0F), 
/* 47 */         PartPose.offset(-2.5F, 21.5F, -4.5F));
/* 49 */     root.addOrReplaceChild("left_front_leg", 
/* 50 */         CubeListBuilder.create()
/* 51 */         .texOffs(12, 28).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 3.0F, 3.0F), 
/* 52 */         PartPose.offset(2.5F, 21.5F, -4.5F));
/* 55 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\polarbear\BabyPolarBearModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */