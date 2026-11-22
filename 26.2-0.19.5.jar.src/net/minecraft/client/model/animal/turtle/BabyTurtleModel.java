/*    */ package net.minecraft.client.model.animal.turtle;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ 
/*    */ public class BabyTurtleModel extends TurtleModel {
/*    */   public BabyTurtleModel(ModelPart root) {
/* 15 */     super(root, RenderTypes::entityCutoutCull);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 19 */     MeshDefinition mesh = new MeshDefinition();
/* 20 */     PartDefinition root = mesh.getRoot();
/* 22 */     root.addOrReplaceChild("body", 
/* 23 */         CubeListBuilder.create()
/* 24 */         .texOffs(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F), 
/* 25 */         PartPose.offset(0.0F, 22.9F, 1.0F));
/* 27 */     root.addOrReplaceChild("head", 
/* 28 */         CubeListBuilder.create()
/* 29 */         .texOffs(0, 6).addBox(-1.5F, -2.0F, -3.0F, 3.0F, 3.0F, 3.0F), 
/* 30 */         PartPose.offset(0.0F, 22.9F, -1.0F));
/* 32 */     root.addOrReplaceChild("right_hind_leg", 
/* 33 */         CubeListBuilder.create()
/* 34 */         .texOffs(-1, 0).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F), 
/* 35 */         PartPose.offset(-2.0F, 23.9F, 2.5F));
/* 37 */     root.addOrReplaceChild("left_hind_leg", 
/* 38 */         CubeListBuilder.create()
/* 39 */         .texOffs(-1, 1).addBox(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F), 
/* 40 */         PartPose.offset(2.0F, 23.9F, 2.5F));
/* 42 */     root.addOrReplaceChild("right_front_leg", 
/* 43 */         CubeListBuilder.create()
/* 44 */         .texOffs(8, 6).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F), 
/* 45 */         PartPose.offset(-2.0F, 23.9F, -0.5F));
/* 47 */     root.addOrReplaceChild("left_front_leg", 
/* 48 */         CubeListBuilder.create()
/* 49 */         .texOffs(8, 7).addBox(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F), 
/* 50 */         PartPose.offset(2.0F, 23.9F, -0.5F));
/* 53 */     return LayerDefinition.create(mesh, 16, 16);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\turtle\BabyTurtleModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */