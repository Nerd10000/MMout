/*    */ package net.minecraft.client.model.animal.cow;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class BabyCowModel extends CowModel {
/*    */   public BabyCowModel(ModelPart root) {
/* 14 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 18 */     MeshDefinition mesh = new MeshDefinition();
/* 19 */     PartDefinition root = mesh.getRoot();
/* 21 */     root.addOrReplaceChild("head", 
/* 22 */         CubeListBuilder.create()
/* 23 */         .texOffs(0, 18).addBox(-3.0F, -4.569F, -4.8333F, 6.0F, 6.0F, 5.0F)
/* 24 */         .texOffs(8, 29).addBox(3.0F, -5.569F, -3.8333F, 1.0F, 2.0F, 1.0F)
/* 25 */         .texOffs(4, 29).mirror().addBox(-4.0F, -5.569F, -3.8333F, 1.0F, 2.0F, 1.0F).mirror(false)
/* 26 */         .texOffs(12, 29).addBox(-2.0F, -1.569F, -5.8333F, 4.0F, 3.0F, 1.0F), 
/* 27 */         PartPose.offset(0.0F, 13.569F, -5.1667F));
/* 29 */     root.addOrReplaceChild("body", 
/* 30 */         CubeListBuilder.create()
/* 31 */         .texOffs(0, 0).addBox(-7.0F, -7.0F, -1.0F, 8.0F, 6.0F, 12.0F), 
/* 32 */         PartPose.offset(3.0F, 19.0F, -5.0F));
/* 34 */     root.addOrReplaceChild("right_front_leg", 
/* 35 */         CubeListBuilder.create()
/* 36 */         .texOffs(22, 18).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), 
/* 37 */         PartPose.offset(-2.5F, 18.0F, -3.5F));
/* 39 */     root.addOrReplaceChild("left_front_leg", 
/* 40 */         CubeListBuilder.create()
/* 41 */         .texOffs(34, 18).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), 
/* 42 */         PartPose.offset(2.5F, 18.0F, -3.5F));
/* 44 */     root.addOrReplaceChild("right_hind_leg", 
/* 45 */         CubeListBuilder.create()
/* 46 */         .texOffs(22, 27).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), 
/* 47 */         PartPose.offset(-2.5F, 18.0F, 3.5F));
/* 49 */     root.addOrReplaceChild("left_hind_leg", 
/* 50 */         CubeListBuilder.create()
/* 51 */         .texOffs(34, 27).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), 
/* 52 */         PartPose.offset(2.5F, 18.0F, 3.5F));
/* 55 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\cow\BabyCowModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */