/*    */ package net.minecraft.client.model.animal.chicken;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ 
/*    */ public class ColdChickenModel extends AdultChickenModel {
/*    */   public ColdChickenModel(ModelPart root) {
/* 14 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 18 */     MeshDefinition mesh = createBaseChickenModel();
/* 19 */     mesh.getRoot().addOrReplaceChild("body", 
/* 20 */         CubeListBuilder.create()
/* 21 */         .texOffs(0, 9).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F)
/* 22 */         .texOffs(38, 9).addBox(0.0F, 3.0F, -1.0F, 0.0F, 3.0F, 5.0F), 
/* 23 */         PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 1.5707964F, 0.0F, 0.0F));
/* 25 */     mesh.getRoot().addOrReplaceChild("head", 
/* 26 */         CubeListBuilder.create()
/* 27 */         .texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 3.0F)
/* 28 */         .texOffs(44, 0).addBox(-3.0F, -7.0F, -2.015F, 6.0F, 3.0F, 4.0F), 
/* 29 */         PartPose.offset(0.0F, 15.0F, -4.0F));
/* 31 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\chicken\ColdChickenModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */