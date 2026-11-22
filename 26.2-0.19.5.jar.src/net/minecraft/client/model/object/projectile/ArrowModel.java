/*    */ package net.minecraft.client.model.object.projectile;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.ArrowRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class ArrowModel extends EntityModel<ArrowRenderState> {
/*    */   public ArrowModel(ModelPart root) {
/* 17 */     super(root, RenderTypes::entityCutoutCull);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 21 */     MeshDefinition mesh = new MeshDefinition();
/* 22 */     PartDefinition root = mesh.getRoot();
/* 24 */     root.addOrReplaceChild("back", 
/* 25 */         CubeListBuilder.create().texOffs(0, 0)
/* 26 */         .addBox(0.0F, -2.5F, -2.5F, 0.0F, 5.0F, 5.0F), 
/*    */         
/* 28 */         PartPose.offsetAndRotation(-11.0F, 0.0F, 0.0F, 0.7853982F, 0.0F, 0.0F).withScale(0.8F));
/* 31 */     CubeListBuilder cross = CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -2.0F, 0.0F, 16.0F, 4.0F, 0.0F, CubeDeformation.NONE, 1.0F, 0.8F);
/* 32 */     root.addOrReplaceChild("cross_1", cross, PartPose.rotation(0.7853982F, 0.0F, 0.0F));
/* 33 */     root.addOrReplaceChild("cross_2", cross, PartPose.rotation(2.3561945F, 0.0F, 0.0F));
/* 35 */     return LayerDefinition.create(mesh.transformed(pose -> pose.scaled(0.9F)), 32, 32);
/*    */   }
/*    */   
/*    */   public void setupAnim(ArrowRenderState state) {
/* 40 */     super.setupAnim(state);
/* 41 */     if (state.shake > 0.0F) {
/* 42 */       float pow = -Mth.sin((state.shake * 3.0F)) * state.shake;
/* 43 */       this.root.zRot += pow * 0.017453292F;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\object\projectile\ArrowModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */