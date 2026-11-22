/*    */ package net.minecraft.client.model.animal.goat;
/*    */ 
/*    */ import net.minecraft.client.model.QuadrupedModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.GoatRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ 
/*    */ public class GoatModel extends QuadrupedModel<GoatRenderState> {
/*    */   public GoatModel(ModelPart root) {
/* 15 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 19 */     MeshDefinition mesh = new MeshDefinition();
/* 20 */     PartDefinition root = mesh.getRoot();
/* 22 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 23 */         CubeListBuilder.create()
/* 24 */         .texOffs(2, 61).addBox("right ear", -6.0F, -11.0F, -10.0F, 3.0F, 2.0F, 1.0F)
/* 25 */         .texOffs(2, 61).mirror().addBox("left ear", 2.0F, -11.0F, -10.0F, 3.0F, 2.0F, 1.0F)
/* 26 */         .texOffs(23, 52).addBox("goatee", -0.5F, -3.0F, -14.0F, 0.0F, 7.0F, 5.0F), 
/* 27 */         PartPose.offset(1.0F, 14.0F, 0.0F));
/* 29 */     head.addOrReplaceChild("left_horn", 
/* 30 */         CubeListBuilder.create()
/* 31 */         .texOffs(12, 55).addBox(-0.01F, -16.0F, -10.0F, 2.0F, 7.0F, 2.0F), 
/* 32 */         PartPose.offset(0.0F, 0.0F, 0.0F));
/* 34 */     head.addOrReplaceChild("right_horn", 
/* 35 */         CubeListBuilder.create()
/* 36 */         .texOffs(12, 55).addBox(-2.99F, -16.0F, -10.0F, 2.0F, 7.0F, 2.0F), 
/* 37 */         PartPose.offset(0.0F, 0.0F, 0.0F));
/* 39 */     head.addOrReplaceChild("nose", 
/* 40 */         CubeListBuilder.create()
/* 41 */         .texOffs(34, 46).addBox(-3.0F, -4.0F, -8.0F, 5.0F, 7.0F, 10.0F), 
/* 42 */         PartPose.offsetAndRotation(0.0F, -8.0F, -8.0F, 0.9599F, 0.0F, 0.0F));
/* 44 */     root.addOrReplaceChild("body", 
/* 45 */         CubeListBuilder.create()
/* 46 */         .texOffs(1, 1).addBox(-4.0F, -17.0F, -7.0F, 9.0F, 11.0F, 16.0F)
/* 47 */         .texOffs(0, 28).addBox(-5.0F, -18.0F, -8.0F, 11.0F, 14.0F, 11.0F), 
/* 48 */         PartPose.offset(0.0F, 24.0F, 0.0F));
/* 51 */     root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(36, 29).addBox(0.0F, 4.0F, 0.0F, 3.0F, 6.0F, 3.0F), PartPose.offset(1.0F, 14.0F, 4.0F));
/* 52 */     root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(49, 29).addBox(0.0F, 4.0F, 0.0F, 3.0F, 6.0F, 3.0F), PartPose.offset(-3.0F, 14.0F, 4.0F));
/* 53 */     root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(49, 2).addBox(0.0F, 0.0F, 0.0F, 3.0F, 10.0F, 3.0F), PartPose.offset(1.0F, 14.0F, -6.0F));
/* 54 */     root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(35, 2).addBox(0.0F, 0.0F, 0.0F, 3.0F, 10.0F, 3.0F), PartPose.offset(-3.0F, 14.0F, -6.0F));
/* 56 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   public void setupAnim(GoatRenderState state) {
/* 61 */     super.setupAnim((LivingEntityRenderState)state);
/* 63 */     (this.head.getChild("left_horn")).visible = state.hasLeftHorn;
/* 64 */     (this.head.getChild("right_horn")).visible = state.hasRightHorn;
/* 66 */     if (state.rammingXHeadRot != 0.0F)
/* 67 */       this.head.xRot = state.rammingXHeadRot; 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\goat\GoatModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */