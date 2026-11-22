/*    */ package net.minecraft.client.model.animal.chicken;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.ChickenRenderState;
/*    */ 
/*    */ public class AdultChickenModel extends ChickenModel {
/*    */   private static final String RED_THING = "red_thing";
/*    */   
/*    */   private final ModelPart head;
/*    */   
/*    */   public AdultChickenModel(ModelPart root) {
/* 19 */     super(root);
/* 20 */     this.head = root.getChild("head");
/*    */   }
/*    */   
/*    */   public void setupAnim(ChickenRenderState state) {
/* 25 */     super.setupAnim(state);
/* 26 */     this.head.xRot = state.xRot * 0.017453292F;
/* 27 */     this.head.yRot = state.yRot * 0.017453292F;
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 31 */     MeshDefinition mesh = createBaseChickenModel();
/* 32 */     return LayerDefinition.create(mesh, 64, 32);
/*    */   }
/*    */   
/*    */   protected static MeshDefinition createBaseChickenModel() {
/* 36 */     MeshDefinition mesh = new MeshDefinition();
/* 37 */     PartDefinition root = mesh.getRoot();
/* 39 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 40 */         CubeListBuilder.create()
/* 41 */         .texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 3.0F), 
/* 42 */         PartPose.offset(0.0F, 15.0F, -4.0F));
/* 44 */     head.addOrReplaceChild("beak", 
/* 45 */         CubeListBuilder.create()
/* 46 */         .texOffs(14, 0).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 2.0F), PartPose.ZERO);
/* 49 */     head.addOrReplaceChild("red_thing", 
/* 50 */         CubeListBuilder.create()
/* 51 */         .texOffs(14, 4).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
/* 54 */     root.addOrReplaceChild("body", 
/* 55 */         CubeListBuilder.create()
/* 56 */         .texOffs(0, 9).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F), 
/* 57 */         PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 1.5707964F, 0.0F, 0.0F));
/* 60 */     CubeListBuilder leg = CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F);
/* 62 */     root.addOrReplaceChild("right_leg", leg, PartPose.offset(-2.0F, 19.0F, 1.0F));
/* 63 */     root.addOrReplaceChild("left_leg", leg, PartPose.offset(1.0F, 19.0F, 1.0F));
/* 65 */     root.addOrReplaceChild("right_wing", 
/* 66 */         CubeListBuilder.create()
/* 67 */         .texOffs(24, 13).addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), 
/* 68 */         PartPose.offset(-4.0F, 13.0F, 0.0F));
/* 70 */     root.addOrReplaceChild("left_wing", 
/* 71 */         CubeListBuilder.create()
/* 72 */         .texOffs(24, 13).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), 
/* 73 */         PartPose.offset(4.0F, 13.0F, 0.0F));
/* 76 */     return mesh;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\chicken\AdultChickenModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */