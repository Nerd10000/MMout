/*    */ package net.minecraft.client.model.monster.slime;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class SmallSulfurCubeModel extends SulfurCubeModel {
/*    */   public SmallSulfurCubeModel(ModelPart root) {
/* 13 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createOuterBodyLayer() {
/* 17 */     MeshDefinition mesh = new MeshDefinition();
/* 18 */     PartDefinition root = mesh.getRoot();
/* 20 */     root.addOrReplaceChild("cube", 
/* 21 */         CubeListBuilder.create()
/* 22 */         .texOffs(0, 0).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F), PartPose.ZERO);
/* 26 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createInnerBodyLayer() {
/* 30 */     MeshDefinition mesh = new MeshDefinition();
/* 31 */     PartDefinition root = mesh.getRoot();
/* 33 */     PartDefinition sulfur_cube = root.addOrReplaceChild("cube", 
/* 34 */         CubeListBuilder.create()
/* 35 */         .texOffs(0, 20).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
/* 39 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\slime\SmallSulfurCubeModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */