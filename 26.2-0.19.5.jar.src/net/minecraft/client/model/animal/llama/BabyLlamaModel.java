/*    */ package net.minecraft.client.model.animal.llama;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class BabyLlamaModel extends LlamaModel {
/*    */   public BabyLlamaModel(ModelPart root) {
/* 15 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(CubeDeformation g) {
/* 19 */     MeshDefinition mesh = new MeshDefinition();
/* 20 */     PartDefinition root = mesh.getRoot();
/* 22 */     root.addOrReplaceChild("head", 
/* 23 */         CubeListBuilder.create()
/* 24 */         .texOffs(0, 0).addBox(-3.0F, -9.0F, -4.0F, 6.0F, 11.0F, 4.0F, g)
/* 25 */         .texOffs(0, 15).addBox(-1.5F, -7.0F, -7.0F, 3.0F, 3.0F, 3.0F, g)
/* 26 */         .texOffs(20, 4).addBox(0.5F, -11.0F, -3.0F, 2.0F, 2.0F, 2.0F, g)
/* 27 */         .texOffs(20, 0).addBox(-2.5F, -11.0F, -3.0F, 2.0F, 2.0F, 2.0F, g), 
/* 28 */         PartPose.offset(0.0F, 12.0F, -4.0F));
/* 30 */     root.addOrReplaceChild("right_hind_leg", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(0, 45).addBox(-1.4F, -0.5F, -1.5F, 3.0F, 8.0F, 3.0F, g), 
/* 33 */         PartPose.offset(-2.5F, 16.5F, 4.5F));
/* 35 */     root.addOrReplaceChild("left_hind_leg", 
/* 36 */         CubeListBuilder.create()
/* 37 */         .texOffs(12, 45).addBox(-1.6F, -0.5F, -1.5F, 3.0F, 8.0F, 3.0F, g), 
/* 38 */         PartPose.offset(2.5F, 16.5F, 4.5F));
/* 40 */     root.addOrReplaceChild("right_front_leg", 
/* 41 */         CubeListBuilder.create()
/* 42 */         .texOffs(0, 34).addBox(-1.4F, -0.5F, -1.5F, 3.0F, 8.0F, 3.0F, g), 
/* 43 */         PartPose.offset(-2.5F, 16.5F, -3.5F));
/* 45 */     root.addOrReplaceChild("left_front_leg", 
/* 46 */         CubeListBuilder.create()
/* 47 */         .texOffs(12, 34).addBox(-1.6F, -0.5F, -1.5F, 3.0F, 8.0F, 3.0F, g), 
/* 48 */         PartPose.offset(2.5F, 16.5F, -3.5F));
/* 50 */     root.addOrReplaceChild("body", 
/* 51 */         CubeListBuilder.create()
/* 52 */         .texOffs(0, 15).addBox(-4.0F, -3.0F, -8.5F, 8.0F, 6.0F, 13.0F, g), 
/* 53 */         PartPose.offset(0.0F, 14.0F, 2.5F));
/* 56 */     root.addOrReplaceChild("right_chest", 
/* 57 */         CubeListBuilder.create()
/* 58 */         .texOffs(45, 28).addBox(-3.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, g), 
/* 59 */         PartPose.offsetAndRotation(-8.5F, 4.0F, 3.0F, 0.0F, 1.5707964F, 0.0F));
/* 61 */     root.addOrReplaceChild("left_chest", 
/* 62 */         CubeListBuilder.create()
/* 63 */         .texOffs(45, 41).addBox(-3.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, g), 
/* 64 */         PartPose.offsetAndRotation(5.5F, 4.0F, 3.0F, 0.0F, 1.5707964F, 0.0F));
/* 67 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\llama\BabyLlamaModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */