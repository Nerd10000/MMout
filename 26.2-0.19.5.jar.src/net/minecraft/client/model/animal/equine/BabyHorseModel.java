/*    */ package net.minecraft.client.model.animal.equine;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class BabyHorseModel extends HorseModel {
/*    */   public BabyHorseModel(ModelPart root) {
/* 15 */     super(root);
/*    */   }
/*    */   
/*    */   public static MeshDefinition createBabyMesh(CubeDeformation g) {
/* 19 */     MeshDefinition mesh = new MeshDefinition();
/* 20 */     PartDefinition root = mesh.getRoot();
/* 22 */     PartDefinition Body = root.addOrReplaceChild("body", 
/* 23 */         CubeListBuilder.create()
/* 24 */         .texOffs(0, 13).addBox(-4.0F, -3.5F, -7.0F, 8.0F, 7.0F, 14.0F, g), 
/* 25 */         PartPose.offset(0.0F, 12.5F, 0.0F));
/* 27 */     Body.addOrReplaceChild("tail", 
/* 28 */         CubeListBuilder.create()
/* 29 */         .texOffs(24, 34).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 8.0F, g), 
/* 30 */         PartPose.offsetAndRotation(0.0F, -1.0F, 7.0F, -0.7418F, 0.0F, 0.0F));
/* 32 */     root.addOrReplaceChild("left_hind_leg", 
/* 33 */         CubeListBuilder.create()
/* 34 */         .texOffs(12, 46).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 9.0F, 3.0F, g), 
/* 35 */         PartPose.offset(2.4F, 16.0F, 5.4F));
/* 37 */     root.addOrReplaceChild("right_hind_leg", 
/* 38 */         CubeListBuilder.create()
/* 39 */         .texOffs(0, 46).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 9.0F, 3.0F, g), 
/* 40 */         PartPose.offset(-2.4F, 16.0F, 5.4F));
/* 42 */     root.addOrReplaceChild("left_front_leg", 
/* 43 */         CubeListBuilder.create()
/* 44 */         .texOffs(12, 34).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 9.0F, 3.0F, g), 
/* 45 */         PartPose.offset(2.4F, 16.0F, -5.4F));
/* 47 */     root.addOrReplaceChild("right_front_leg", 
/* 48 */         CubeListBuilder.create()
/* 49 */         .texOffs(0, 34).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 9.0F, 3.0F, g), 
/* 50 */         PartPose.offset(-2.4F, 16.0F, -5.4F));
/* 52 */     PartDefinition neck = root.addOrReplaceChild("head_parts", 
/* 53 */         CubeListBuilder.create()
/* 54 */         .texOffs(30, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 8.0F, 4.0F, g), 
/* 55 */         PartPose.offsetAndRotation(0.0F, 10.0F, -6.0F, 0.6109F, 0.0F, 0.0F));
/* 57 */     PartDefinition head = neck.addOrReplaceChild("head", 
/* 58 */         CubeListBuilder.create()
/* 59 */         .texOffs(0, 0).addBox(-3.0F, -3.9484F, -6.705F, 6.0F, 4.0F, 9.0F, g), 
/* 60 */         PartPose.offset(0.0F, -6.0516F, -0.2951F));
/* 62 */     head.addOrReplaceChild("left_ear", 
/* 63 */         CubeListBuilder.create()
/* 64 */         .texOffs(0, 4).addBox(-1.0F, -2.5F, -0.8F, 2.0F, 3.0F, 1.0F, g), 
/* 65 */         PartPose.offsetAndRotation(2.0F, -4.2484F, 1.9451F, 0.0F, 0.0F, 0.2618F));
/* 67 */     head.addOrReplaceChild("right_ear", 
/* 68 */         CubeListBuilder.create()
/* 69 */         .texOffs(0, 0).addBox(-1.0F, -2.5F, -0.5F, 2.0F, 3.0F, 1.0F, g), 
/* 70 */         PartPose.offsetAndRotation(-2.0F, -4.2484F, 1.645F, 0.0F, 0.0F, -0.2618F));
/* 72 */     return mesh;
/*    */   }
/*    */   
/*    */   protected float getLegStandingYOffset() {
/* 77 */     return 4.0F;
/*    */   }
/*    */   
/*    */   protected float getLegStandingZOffset() {
/* 82 */     return 0.0F;
/*    */   }
/*    */   
/*    */   protected float getTailXRotOffset() {
/* 87 */     return -1.5707964F;
/*    */   }
/*    */   
/*    */   protected void animateHeadPartsPlacement(float eating, float standing) {
/* 92 */     this.headParts.y += Mth.lerp(eating, Mth.lerp(standing, 0.0F, -2.0F), 2.0F);
/* 93 */     this.headParts.z = Mth.lerp(standing, this.headParts.z, -4.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\equine\BabyHorseModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */