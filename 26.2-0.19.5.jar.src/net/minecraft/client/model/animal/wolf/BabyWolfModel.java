/*    */ package net.minecraft.client.model.animal.wolf;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.WolfRenderState;
/*    */ 
/*    */ public class BabyWolfModel extends WolfModel {
/*    */   public BabyWolfModel(ModelPart root) {
/* 17 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 21 */     MeshDefinition mesh = new MeshDefinition();
/* 22 */     PartDefinition root = mesh.getRoot();
/* 24 */     PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
/* 25 */         .texOffs(0, 12).addBox(-2.99F, -3.25F, -3.0F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.025F))
/* 26 */         .texOffs(17, 12).addBox(-1.5F, -0.24F, -5.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), 
/* 27 */         PartPose.offset(0.0F, 18.25F, -4.0F));
/* 29 */     head.addOrReplaceChild("right_ear", CubeListBuilder.create()
/* 30 */         .texOffs(0, 5).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F), 
/* 31 */         PartPose.offset(-2.0F, -4.25F, -0.5F));
/* 33 */     head.addOrReplaceChild("left_ear", 
/* 34 */         CubeListBuilder.create()
/* 35 */         .texOffs(20, 5).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F), 
/* 36 */         PartPose.offset(2.0F, -4.25F, -0.5F));
/* 38 */     root.addOrReplaceChild("body", 
/* 39 */         CubeListBuilder.create()
/* 40 */         .texOffs(0, 0).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 4.0F, 8.0F), 
/* 41 */         PartPose.offset(0.0F, 19.0F, 0.0F));
/* 43 */     root.addOrReplaceChild("right_hind_leg", 
/* 44 */         CubeListBuilder.create()
/* 45 */         .texOffs(0, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), 
/* 46 */         PartPose.offset(-1.5F, 21.0F, 3.0F));
/* 48 */     root.addOrReplaceChild("left_hind_leg", 
/* 49 */         CubeListBuilder.create()
/* 50 */         .texOffs(8, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), 
/* 51 */         PartPose.offset(1.5F, 21.0F, 3.0F));
/* 53 */     root.addOrReplaceChild("right_front_leg", 
/* 54 */         CubeListBuilder.create()
/* 55 */         .texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), 
/* 56 */         PartPose.offset(-1.5F, 21.0F, -3.0F));
/* 58 */     root.addOrReplaceChild("left_front_leg", 
/* 59 */         CubeListBuilder.create()
/* 60 */         .texOffs(20, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), 
/* 61 */         PartPose.offset(1.5F, 21.0F, -3.0F));
/* 64 */     PartDefinition tail = root.addOrReplaceChild("tail", 
/* 65 */         CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 19.0F, 3.0F, -0.5236F, 0.0F, 0.0F));
/* 67 */     tail.addOrReplaceChild("tail_r1", 
/* 68 */         CubeListBuilder.create()
/* 69 */         .texOffs(22, 16).addBox(-1.0F, -5.7F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), 
/* 70 */         PartPose.offsetAndRotation(0.0F, -0.6F, 0.2F, -3.1F, 0.0F, 0.0F));
/* 72 */     return LayerDefinition.create(mesh, 32, 32);
/*    */   }
/*    */   
/*    */   protected void shakeOffWater(WolfRenderState state) {
/* 77 */     super.shakeOffWater(state);
/* 78 */     this.head.zRot = state.headRollAngle + state.getBodyRollAngle(0.0F);
/* 79 */     this.tail.zRot = state.getBodyRollAngle(-0.2F);
/*    */   }
/*    */   
/*    */   protected void setSittingPose(WolfRenderState state) {
/* 84 */     super.setSittingPose(state);
/* 85 */     this.body.xRot -= 1.5707964F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\wolf\BabyWolfModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */