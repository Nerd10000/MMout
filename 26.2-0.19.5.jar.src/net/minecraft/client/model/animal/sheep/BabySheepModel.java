/*    */ package net.minecraft.client.model.animal.sheep;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class BabySheepModel extends SheepModel {
/*    */   public BabySheepModel(ModelPart root) {
/* 14 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 18 */     MeshDefinition mesh = new MeshDefinition();
/* 19 */     PartDefinition root = mesh.getRoot();
/* 21 */     root.addOrReplaceChild("body", 
/* 22 */         CubeListBuilder.create()
/* 23 */         .texOffs(0, 10).addBox(-3.0F, -2.0F, -4.5F, 6.0F, 4.0F, 9.0F), 
/* 24 */         PartPose.offset(0.0F, 17.0F, 0.5F));
/* 26 */     root.addOrReplaceChild("head", 
/* 27 */         CubeListBuilder.create()
/* 28 */         .texOffs(0, 0).addBox(-2.5F, -4.5F, -3.5F, 5.0F, 5.0F, 5.0F), 
/* 29 */         PartPose.offset(0.0F, 15.5F, -2.5F));
/* 31 */     root.addOrReplaceChild("right_hind_leg", 
/* 32 */         CubeListBuilder.create()
/* 33 */         .texOffs(0, 23).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F), 
/* 34 */         PartPose.offset(-2.0F, 19.0F, 3.0F));
/* 36 */     root.addOrReplaceChild("left_hind_leg", 
/* 37 */         CubeListBuilder.create()
/* 38 */         .texOffs(24, 12).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F), 
/* 39 */         PartPose.offset(2.0F, 19.0F, 3.0F));
/* 41 */     root.addOrReplaceChild("right_front_leg", 
/* 42 */         CubeListBuilder.create()
/* 43 */         .texOffs(8, 23).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F), 
/* 44 */         PartPose.offset(-2.0F, 19.0F, -2.0F));
/* 46 */     root.addOrReplaceChild("left_front_leg", 
/* 47 */         CubeListBuilder.create()
/* 48 */         .texOffs(24, 5).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F), 
/* 49 */         PartPose.offset(2.0F, 19.0F, -2.0F));
/* 52 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\sheep\BabySheepModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */