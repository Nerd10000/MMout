/*    */ package net.minecraft.client.model.monster.slime;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ 
/*    */ public class SulfurCubeModel extends EntityModel<EntityRenderState> {
/*    */   public SulfurCubeModel(ModelPart root) {
/* 16 */     super(root, RenderTypes::entityTranslucent);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createOuterBodyLayer() {
/* 20 */     MeshDefinition mesh = new MeshDefinition();
/* 21 */     PartDefinition root = mesh.getRoot();
/* 23 */     root.addOrReplaceChild("cube", 
/* 24 */         CubeListBuilder.create()
/* 25 */         .texOffs(0, 0).addBox(-9.0F, -9.0F, -9.0F, 18.0F, 18.0F, 18.0F), PartPose.ZERO);
/* 29 */     return LayerDefinition.create(mesh, 128, 128);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createInnerBodyLayer() {
/* 33 */     MeshDefinition mesh = new MeshDefinition();
/* 34 */     PartDefinition root = mesh.getRoot();
/* 36 */     root.addOrReplaceChild("cube", 
/* 37 */         CubeListBuilder.create()
/* 38 */         .texOffs(0, 36).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.ZERO);
/* 42 */     return LayerDefinition.create(mesh, 128, 128);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\slime\SulfurCubeModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */