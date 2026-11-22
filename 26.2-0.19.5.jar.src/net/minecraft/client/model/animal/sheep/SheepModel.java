/*    */ package net.minecraft.client.model.animal.sheep;
/*    */ 
/*    */ import net.minecraft.client.model.QuadrupedModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SheepRenderState;
/*    */ 
/*    */ public class SheepModel extends QuadrupedModel<SheepRenderState> {
/*    */   public SheepModel(ModelPart root) {
/* 18 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 22 */     MeshDefinition mesh = QuadrupedModel.createBodyMesh(12, false, true, CubeDeformation.NONE);
/* 23 */     PartDefinition root = mesh.getRoot();
/* 25 */     root.addOrReplaceChild("head", 
/* 26 */         CubeListBuilder.create()
/* 27 */         .texOffs(0, 0).addBox(-3.0F, -4.0F, -6.0F, 6.0F, 6.0F, 8.0F), 
/* 28 */         PartPose.offset(0.0F, 6.0F, -8.0F));
/* 30 */     root.addOrReplaceChild("body", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(28, 8).addBox(-4.0F, -10.0F, -7.0F, 8.0F, 16.0F, 6.0F), 
/* 33 */         PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
/* 35 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */   
/*    */   public void setupAnim(SheepRenderState state) {
/* 40 */     super.setupAnim((LivingEntityRenderState)state);
/* 41 */     this.head.y += state.headEatPositionScale * 9.0F * state.ageScale;
/* 42 */     this.head.xRot = state.headEatAngleScale;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\sheep\SheepModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */