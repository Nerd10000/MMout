/*    */ package net.minecraft.client.model.animal.bee;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class BabyBeeModel extends BeeModel {
/*    */   public BabyBeeModel(ModelPart root) {
/* 14 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 18 */     MeshDefinition mesh = new MeshDefinition();
/* 19 */     PartDefinition root = mesh.getRoot();
/* 21 */     PartDefinition bone = root.addOrReplaceChild("bone", 
/* 22 */         CubeListBuilder.create()
/* 23 */         .texOffs(6, 12).addBox(1.0F, -1.6667F, -2.1633F, 1.0F, 2.0F, 2.0F)
/* 24 */         .texOffs(0, 12).addBox(-2.0F, -1.6667F, -2.1933F, 1.0F, 2.0F, 2.0F), 
/* 25 */         PartPose.offset(0.0F, 19.6667F, -1.8567F));
/* 27 */     PartDefinition body = bone.addOrReplaceChild("body", 
/* 28 */         CubeListBuilder.create()
/* 29 */         .texOffs(0, 0).addBox(-2.0F, -2.0F, -2.5F, 4.0F, 4.0F, 5.0F), 
/* 30 */         PartPose.offset(0.0F, 1.3333F, 2.3567F));
/* 32 */     body.addOrReplaceChild("stinger", 
/* 33 */         CubeListBuilder.create()
/* 34 */         .texOffs(13, 2).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 1.0F), 
/* 35 */         PartPose.offset(0.0F, 0.5F, 2.5F));
/* 37 */     bone.addOrReplaceChild("right_wing", 
/* 38 */         CubeListBuilder.create()
/* 39 */         .texOffs(3, 9).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 0.0F, 3.0F), 
/* 40 */         PartPose.offsetAndRotation(-1.0F, -0.6667F, 0.8567F, 0.2182F, 0.3491F, 0.0F));
/* 42 */     bone.addOrReplaceChild("left_wing", 
/* 43 */         CubeListBuilder.create()
/* 44 */         .texOffs(-3, 9).mirror().addBox(0.0F, 0.0F, 0.0F, 3.0F, 0.0F, 3.0F).mirror(false), 
/* 45 */         PartPose.offsetAndRotation(1.0F, -0.6667F, 0.8567F, 0.2182F, -0.3491F, 0.0F));
/* 47 */     bone.addOrReplaceChild("front_legs", 
/* 48 */         CubeListBuilder.create()
/* 49 */         .texOffs(13, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F), 
/* 50 */         PartPose.offset(0.0F, 3.3333F, 1.8567F));
/* 52 */     bone.addOrReplaceChild("middle_legs", 
/* 53 */         CubeListBuilder.create()
/* 54 */         .texOffs(13, 1).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F), 
/* 55 */         PartPose.offset(0.0F, 3.3333F, 2.8567F));
/* 57 */     bone.addOrReplaceChild("back_legs", 
/* 58 */         CubeListBuilder.create()
/* 59 */         .texOffs(13, 2).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F), 
/* 60 */         PartPose.offset(0.0F, 3.3333F, 3.8567F));
/* 63 */     return LayerDefinition.create(mesh, 32, 32);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\bee\BabyBeeModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */