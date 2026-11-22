/*    */ package net.minecraft.client.model.monster.piglin;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class BabyPiglinModel extends PiglinModel {
/*    */   public BabyPiglinModel(ModelPart root) {
/* 13 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 17 */     MeshDefinition mesh = new MeshDefinition();
/* 18 */     PartDefinition root = mesh.getRoot();
/* 20 */     root.addOrReplaceChild("body", 
/* 21 */         CubeListBuilder.create()
/* 22 */         .texOffs(0, 13).addBox(-3.0F, -3.0F, -1.0F, 6.0F, 5.0F, 3.0F), 
/* 23 */         PartPose.offset(0.0F, 18.0F, -0.5F));
/* 25 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 26 */         CubeListBuilder.create()
/* 27 */         .texOffs(21, 30).addBox(-1.5F, -3.0F, -4.5F, 3.0F, 3.0F, 1.0F)
/* 28 */         .texOffs(0, 0).addBox(-4.5F, -6.0F, -3.5F, 9.0F, 6.0F, 7.0F), 
/* 29 */         PartPose.offset(0.0F, 15.0F, 0.0F));
/* 31 */     head.addOrReplaceChild("hat", 
/* 32 */         CubeListBuilder.create(), 
/* 33 */         PartPose.offset(0.0F, 0.0F, 0.0F));
/* 35 */     PartDefinition leftear = head.addOrReplaceChild("left_ear", 
/* 36 */         CubeListBuilder.create(), 
/* 37 */         PartPose.offset(4.2F, -4.0F, 0.0F));
/* 39 */     leftear.addOrReplaceChild("left_ear_r1", 
/* 40 */         CubeListBuilder.create()
/* 41 */         .texOffs(0, 21).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F), 
/* 42 */         PartPose.offsetAndRotation(1.0F, 1.75F, 0.0F, 0.0F, 0.0F, -0.6109F));
/* 44 */     PartDefinition rightear = head.addOrReplaceChild("right_ear", 
/* 45 */         CubeListBuilder.create(), 
/* 46 */         PartPose.offset(-4.2F, -4.0F, 0.0F));
/* 48 */     rightear.addOrReplaceChild("right_ear_r1", 
/* 49 */         CubeListBuilder.create()
/* 50 */         .texOffs(18, 13).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F), 
/* 51 */         PartPose.offsetAndRotation(-1.0F, 1.75F, 0.0F, 0.0F, 0.0F, 0.6109F));
/* 53 */     root.addOrReplaceChild("left_arm", 
/* 54 */         CubeListBuilder.create()
/* 55 */         .texOffs(28, 13).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 5.0F, 3.0F), 
/* 56 */         PartPose.offset(4.0F, 15.0F, 0.0F));
/* 58 */     root.addOrReplaceChild("right_arm", 
/* 59 */         CubeListBuilder.create()
/* 60 */         .texOffs(10, 30).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 5.0F, 3.0F), 
/* 61 */         PartPose.offset(-4.0F, 15.0F, 0.0F));
/* 63 */     root.addOrReplaceChild("right_leg", 
/* 64 */         CubeListBuilder.create()
/* 65 */         .texOffs(22, 23).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F), 
/* 66 */         PartPose.offset(-1.5F, 20.0F, 0.0F));
/* 68 */     root.addOrReplaceChild("left_leg", 
/* 69 */         CubeListBuilder.create()
/* 70 */         .texOffs(10, 23).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F), 
/* 71 */         PartPose.offset(1.5F, 20.0F, 0.0F));
/* 74 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   protected float getDefaultEarAngleInDegrees() {
/* 79 */     return 5.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\piglin\BabyPiglinModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */