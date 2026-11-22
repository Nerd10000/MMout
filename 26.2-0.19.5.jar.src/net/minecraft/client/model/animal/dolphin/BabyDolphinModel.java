/*    */ package net.minecraft.client.model.animal.dolphin;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class BabyDolphinModel extends DolphinModel {
/*    */   public BabyDolphinModel(ModelPart root) {
/* 15 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 19 */     MeshDefinition mesh = new MeshDefinition();
/* 20 */     PartDefinition root = mesh.getRoot();
/* 22 */     PartDefinition body = root.addOrReplaceChild("body", 
/* 23 */         CubeListBuilder.create()
/* 24 */         .texOffs(20, 0).addBox(-3.0F, -2.5F, -4.0F, 6.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), 
/* 25 */         PartPose.offset(0.0F, 21.5F, 0.0F));
/* 27 */     PartDefinition head = body.addOrReplaceChild("head", 
/* 28 */         CubeListBuilder.create()
/* 29 */         .texOffs(0, 0).addBox(-3.0F, -3.5F, -4.0F, 6.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), 
/* 30 */         PartPose.offset(0.0F, 1.0F, -4.0F));
/* 32 */     head.addOrReplaceChild("nose", 
/* 33 */         CubeListBuilder.create()
/* 34 */         .texOffs(0, 9).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), 
/* 35 */         PartPose.offset(0.0F, 0.5F, -4.0F));
/* 37 */     body.addOrReplaceChild("left_fin", 
/* 38 */         CubeListBuilder.create()
/* 39 */         .texOffs(34, 18).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), 
/* 40 */         PartPose.offsetAndRotation(1.8F, 0.85F, -2.6F, 0.8727F, 0.0F, 1.7017F));
/* 42 */     body.addOrReplaceChild("right_fin", 
/* 43 */         CubeListBuilder.create()
/* 44 */         .texOffs(48, 18).mirror().addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), 
/* 45 */         PartPose.offsetAndRotation(-1.8F, 0.85F, -2.6F, 0.8727F, 0.0F, -1.7017F));
/* 47 */     PartDefinition tail = body.addOrReplaceChild("tail", 
/* 48 */         CubeListBuilder.create()
/* 49 */         .texOffs(0, 13).addBox(-2.0F, -1.5F, 0.0F, 4.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), 
/* 50 */         PartPose.offset(0.0F, 1.0F, 4.0F));
/* 52 */     tail.addOrReplaceChild("tail_fin", 
/* 53 */         CubeListBuilder.create()
/* 54 */         .texOffs(22, 13).addBox(-4.0F, -0.5F, -1.0F, 8.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), 
/* 55 */         PartPose.offset(0.0F, 0.0F, 6.0F));
/* 57 */     body.addOrReplaceChild("back_fin", 
/* 58 */         CubeListBuilder.create()
/* 59 */         .texOffs(42, 0).addBox(-0.5F, -1.0F, 1.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), 
/* 60 */         PartPose.offsetAndRotation(0.0F, -1.0F, -2.7F, 0.8727F, 0.0F, 0.0F));
/* 63 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\dolphin\BabyDolphinModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */