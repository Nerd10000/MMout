/*    */ package net.minecraft.client.model.monster.hoglin;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class BabyHoglinModel extends HoglinModel {
/*    */   public BabyHoglinModel(ModelPart root) {
/* 15 */     super(root);
/*    */   }
/*    */   
/*    */   protected void animateHeadbutt(float headbuttLerpFactor) {
/* 20 */     super.animateHeadbutt(headbuttLerpFactor);
/* 22 */     this.head.y += headbuttLerpFactor * 2.5F;
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 26 */     MeshDefinition mesh = new MeshDefinition();
/* 27 */     PartDefinition root = mesh.getRoot();
/* 29 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 30 */         CubeListBuilder.create()
/* 31 */         .texOffs(0, 0).addBox(-5.0F, -2.2605F, -10.547F, 10.0F, 4.0F, 12.0F)
/* 32 */         .texOffs(44, 29).addBox(-7.0F, -4.0981F, -8.4879F, 2.0F, 5.0F, 2.0F)
/* 33 */         .texOffs(52, 29).addBox(5.0F, -4.0981F, -8.4879F, 2.0F, 5.0F, 2.0F), 
/* 34 */         PartPose.offsetAndRotation(0.0F, 13.0F, -7.0F, 0.8727F, 0.0F, 0.0F));
/* 36 */     root.addOrReplaceChild("body", 
/* 37 */         CubeListBuilder.create()
/* 38 */         .texOffs(0, 16).addBox(-4.0F, -14.0F, -7.0F, 8.0F, 8.0F, 14.0F, new CubeDeformation(0.02F))
/* 39 */         .texOffs(24, 39).addBox(0.0F, -18.0F, -8.0F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.02F)), 
/* 40 */         PartPose.offset(0.0F, 24.0F, 0.0F));
/* 42 */     head.addOrReplaceChild("right_ear", 
/* 43 */         CubeListBuilder.create()
/* 44 */         .texOffs(32, 5).addBox(-5.1F, -0.5F, -2.0F, 6.0F, 1.0F, 4.0F), 
/* 45 */         PartPose.offsetAndRotation(-5.0F, -1.0F, -1.5F, 0.0F, 0.0F, -0.8727F));
/* 47 */     head.addOrReplaceChild("left_ear", 
/* 48 */         CubeListBuilder.create()
/* 49 */         .texOffs(32, 0).mirror().addBox(-0.9F, -0.5F, -2.0F, 6.0F, 1.0F, 4.0F).mirror(false), 
/* 50 */         PartPose.offsetAndRotation(5.0F, -1.0F, -1.5F, 0.0F, 0.0F, 0.8727F));
/* 52 */     root.addOrReplaceChild("right_hind_leg", 
/* 53 */         CubeListBuilder.create()
/* 54 */         .texOffs(0, 47).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), 
/* 55 */         PartPose.offset(-2.5F, 18.0F, 4.5F));
/* 57 */     root.addOrReplaceChild("left_hind_leg", 
/* 58 */         CubeListBuilder.create()
/* 59 */         .texOffs(12, 47).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), 
/* 60 */         PartPose.offset(2.5F, 18.0F, 4.5F));
/* 62 */     root.addOrReplaceChild("right_front_leg", 
/* 63 */         CubeListBuilder.create()
/* 64 */         .texOffs(0, 38).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), 
/* 65 */         PartPose.offset(-2.5F, 18.0F, -4.5F));
/* 67 */     root.addOrReplaceChild("left_front_leg", 
/* 68 */         CubeListBuilder.create()
/* 69 */         .texOffs(12, 38).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), 
/* 70 */         PartPose.offset(2.5F, 18.0F, -4.5F));
/* 73 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\hoglin\BabyHoglinModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */