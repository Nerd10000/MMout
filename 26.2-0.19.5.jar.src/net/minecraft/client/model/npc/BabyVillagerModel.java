/*    */ package net.minecraft.client.model.npc;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class BabyVillagerModel extends VillagerModel {
/*    */   public BabyVillagerModel(ModelPart root) {
/* 14 */     super(root);
/*    */   }
/*    */   
/*    */   public static MeshDefinition createNoHatModel() {
/* 18 */     MeshDefinition mesh = createBodyModel();
/* 19 */     mesh.getRoot().clearChild("head").clearRecursively();
/* 20 */     return mesh;
/*    */   }
/*    */   
/*    */   public static MeshDefinition createBodyModel() {
/* 24 */     MeshDefinition mesh = new MeshDefinition();
/* 25 */     PartDefinition root = mesh.getRoot();
/* 27 */     PartDefinition arms = root.addOrReplaceChild("arms", 
/* 28 */         CubeListBuilder.create(), 
/* 29 */         PartPose.offset(0.0F, 17.5F, 0.0F));
/* 31 */     arms.addOrReplaceChild("right_hand", 
/* 32 */         CubeListBuilder.create()
/* 33 */         .texOffs(36, 15).addBox(-1.0F, -2.4925F, -1.8401F, 2.0F, 4.0F, 2.0F)
/* 34 */         .texOffs(16, 15).addBox(5.0F, -2.4925F, -1.8401F, 2.0F, 4.0F, 2.0F), 
/* 35 */         PartPose.offsetAndRotation(-3.0F, 1.4025F, -0.9599F, -1.0472F, 0.0F, 0.0F));
/* 37 */     arms.addOrReplaceChild("middlearm_r1", 
/* 38 */         CubeListBuilder.create()
/* 39 */         .texOffs(24, 17).addBox(-2.0F, -0.9924F, -0.9825F, 4.0F, 2.0F, 2.0F), 
/* 40 */         PartPose.offsetAndRotation(0.0F, 0.9024F, -1.8175F, -1.0472F, 0.0F, 0.0F));
/* 42 */     root.addOrReplaceChild("right_leg", 
/* 43 */         CubeListBuilder.create()
/* 44 */         .texOffs(8, 23).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F), 
/* 45 */         PartPose.offset(-1.0F, 21.5F, 0.0F));
/* 47 */     root.addOrReplaceChild("left_leg", 
/* 48 */         CubeListBuilder.create()
/* 49 */         .texOffs(0, 23).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F), 
/* 50 */         PartPose.offset(1.0F, 21.5F, 0.0F));
/* 52 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 53 */         CubeListBuilder.create()
/* 54 */         .texOffs(0, 0).addBox(-4.0F, -8.0F, -3.5F, 8.0F, 8.0F, 7.0F), 
/* 55 */         PartPose.offset(0.0F, 16.0F, 0.0F));
/* 57 */     head.addOrReplaceChild("hat", 
/* 58 */         CubeListBuilder.create()
/* 59 */         .texOffs(0, 30).addBox(-4.0F, -4.0F, -3.5F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.3F)), 
/* 60 */         PartPose.offset(0.0F, -4.0F, 0.0F));
/* 62 */     head.addOrReplaceChild("hat_rim", 
/* 63 */         CubeListBuilder.create()
/* 64 */         .texOffs(0, 45).addBox(-7.0F, -0.5F, -6.0F, 14.0F, 1.0F, 12.0F), 
/* 65 */         PartPose.offset(0.0F, -4.5F, 0.0F));
/* 67 */     head.addOrReplaceChild("nose", 
/* 68 */         CubeListBuilder.create()
/* 69 */         .texOffs(23, 0).addBox(-1.0F, 0.0F, -0.5F, 2.0F, 2.0F, 1.0F), 
/* 70 */         PartPose.offset(0.0F, -2.0F, -4.0F));
/* 72 */     root.addOrReplaceChild("body", 
/* 73 */         CubeListBuilder.create()
/* 74 */         .texOffs(0, 15).addBox(-2.0F, -2.75F, -1.5F, 4.0F, 5.0F, 3.0F), 
/* 75 */         PartPose.offset(0.0F, 18.75F, 0.0F));
/* 77 */     root.addOrReplaceChild("bb_main", 
/* 78 */         CubeListBuilder.create()
/* 79 */         .texOffs(16, 21).addBox(-2.5F, -8.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.2F)), 
/* 80 */         PartPose.offset(0.5F, 24.0F, 0.0F));
/* 83 */     return mesh;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\npc\BabyVillagerModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */