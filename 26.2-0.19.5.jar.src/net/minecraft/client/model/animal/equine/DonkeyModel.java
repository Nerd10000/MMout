/*    */ package net.minecraft.client.model.animal.equine;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.DonkeyRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EquineRenderState;
/*    */ 
/*    */ public class DonkeyModel extends AbstractEquineModel<DonkeyRenderState> {
/*    */   public static final float DONKEY_SCALE = 0.87F;
/*    */   
/*    */   public static final float MULE_SCALE = 0.92F;
/*    */   
/*    */   private static final MeshTransformer DONKEY_TRANSFORMER;
/*    */   
/*    */   private final ModelPart leftChest;
/*    */   
/*    */   private final ModelPart rightChest;
/*    */   
/*    */   static {
/* 18 */     DONKEY_TRANSFORMER = (mesh -> {
/*    */         modifyMesh(mesh.getRoot());
/*    */         return mesh;
/*    */       });
/*    */   }
/*    */   
/*    */   public DonkeyModel(ModelPart root) {
/* 27 */     super(root);
/* 28 */     this.leftChest = this.body.getChild("left_chest");
/* 29 */     this.rightChest = this.body.getChild("right_chest");
/*    */   }
/*    */   
/*    */   public DonkeyModel(ModelPart root, ModelPart headParts, ModelPart rightHindLeg, ModelPart rightFrontLeg, ModelPart leftHindLeg, ModelPart leftFrontLeg, ModelPart tail) {
/* 33 */     super(root, headParts, rightHindLeg, rightFrontLeg, leftHindLeg, leftFrontLeg, tail);
/* 34 */     this.leftChest = this.body.getChild("left_chest");
/* 35 */     this.rightChest = this.body.getChild("right_chest");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(float scale) {
/* 39 */     return LayerDefinition.create(AbstractEquineModel.createBodyMesh(CubeDeformation.NONE), 64, 64)
/* 40 */       .apply(DONKEY_TRANSFORMER)
/* 41 */       .apply(MeshTransformer.scaling(scale));
/*    */   }
/*    */   
/*    */   public static LayerDefinition createSaddleLayer(float scale) {
/* 45 */     return EquineSaddleModel.createSaddleLayer()
/* 46 */       .apply(DONKEY_TRANSFORMER)
/* 47 */       .apply(MeshTransformer.scaling(scale));
/*    */   }
/*    */   
/*    */   private static void modifyMesh(PartDefinition root) {
/* 51 */     PartDefinition body = root.getChild("body");
/* 53 */     CubeListBuilder chest = CubeListBuilder.create().texOffs(26, 21).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 3.0F);
/* 54 */     body.addOrReplaceChild("left_chest", chest, PartPose.offsetAndRotation(6.0F, -8.0F, 0.0F, 0.0F, -1.5707964F, 0.0F));
/* 55 */     body.addOrReplaceChild("right_chest", chest, PartPose.offsetAndRotation(-6.0F, -8.0F, 0.0F, 0.0F, 1.5707964F, 0.0F));
/* 57 */     PartDefinition head = root.getChild("head_parts").getChild("head");
/* 59 */     CubeListBuilder ear = CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 1.0F);
/* 60 */     head.addOrReplaceChild("left_ear", ear, PartPose.offsetAndRotation(1.25F, -10.0F, 4.0F, 0.2617994F, 0.0F, 0.2617994F));
/* 61 */     head.addOrReplaceChild("right_ear", ear, PartPose.offsetAndRotation(-1.25F, -10.0F, 4.0F, 0.2617994F, 0.0F, -0.2617994F));
/*    */   }
/*    */   
/*    */   public void setupAnim(DonkeyRenderState state) {
/* 66 */     super.setupAnim(state);
/* 67 */     this.leftChest.visible = state.hasChest;
/* 68 */     this.rightChest.visible = state.hasChest;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\equine\DonkeyModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */