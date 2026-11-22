/*    */ package net.minecraft.client.model.animal.equine;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.EquineRenderState;
/*    */ 
/*    */ public class EquineSaddleModel extends AbstractEquineModel<EquineRenderState> {
/*    */   private static final String SADDLE = "saddle";
/*    */   
/*    */   private static final String LEFT_SADDLE_MOUTH = "left_saddle_mouth";
/*    */   
/*    */   private static final String LEFT_SADDLE_LINE = "left_saddle_line";
/*    */   
/*    */   private static final String RIGHT_SADDLE_MOUTH = "right_saddle_mouth";
/*    */   
/*    */   private static final String RIGHT_SADDLE_LINE = "right_saddle_line";
/*    */   
/*    */   private static final String HEAD_SADDLE = "head_saddle";
/*    */   
/*    */   private static final String MOUTH_SADDLE_WRAP = "mouth_saddle_wrap";
/*    */   
/*    */   private final ModelPart[] ridingParts;
/*    */   
/*    */   public EquineSaddleModel(ModelPart root) {
/* 26 */     super(root);
/* 27 */     ModelPart leftSaddleLine = this.headParts.getChild("left_saddle_line");
/* 28 */     ModelPart rightSaddleLine = this.headParts.getChild("right_saddle_line");
/* 29 */     this.ridingParts = new ModelPart[] { leftSaddleLine, rightSaddleLine };
/*    */   }
/*    */   
/*    */   public static LayerDefinition createSaddleLayer() {
/* 33 */     MeshDefinition mesh = createBodyMesh(CubeDeformation.NONE);
/* 34 */     PartDefinition root = mesh.getRoot();
/* 35 */     PartDefinition body = root.getChild("body");
/* 37 */     PartDefinition headParts = root.getChild("head_parts");
/* 39 */     body.addOrReplaceChild("saddle", 
/* 40 */         CubeListBuilder.create()
/* 41 */         .texOffs(26, 0).addBox(-5.0F, -8.0F, -9.0F, 10.0F, 9.0F, 9.0F, new CubeDeformation(0.5F)), PartPose.ZERO);
/* 45 */     headParts.addOrReplaceChild("left_saddle_mouth", 
/* 46 */         CubeListBuilder.create()
/* 47 */         .texOffs(29, 5).addBox(2.0F, -9.0F, -6.0F, 1.0F, 2.0F, 2.0F), PartPose.ZERO);
/* 50 */     headParts.addOrReplaceChild("right_saddle_mouth", 
/* 51 */         CubeListBuilder.create()
/* 52 */         .texOffs(29, 5).addBox(-3.0F, -9.0F, -6.0F, 1.0F, 2.0F, 2.0F), PartPose.ZERO);
/* 55 */     headParts.addOrReplaceChild("left_saddle_line", 
/* 56 */         CubeListBuilder.create()
/* 57 */         .texOffs(32, 2).addBox(3.1F, -6.0F, -8.0F, 0.0F, 3.0F, 16.0F), 
/* 58 */         PartPose.rotation(-0.5235988F, 0.0F, 0.0F));
/* 60 */     headParts.addOrReplaceChild("right_saddle_line", 
/* 61 */         CubeListBuilder.create()
/* 62 */         .texOffs(32, 2).addBox(-3.1F, -6.0F, -8.0F, 0.0F, 3.0F, 16.0F), 
/* 63 */         PartPose.rotation(-0.5235988F, 0.0F, 0.0F));
/* 65 */     headParts.addOrReplaceChild("head_saddle", 
/* 66 */         CubeListBuilder.create()
/* 67 */         .texOffs(1, 1).addBox(-3.0F, -11.0F, -1.9F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.22F)), PartPose.ZERO);
/* 70 */     headParts.addOrReplaceChild("mouth_saddle_wrap", 
/* 71 */         CubeListBuilder.create()
/* 72 */         .texOffs(19, 0).addBox(-2.0F, -11.0F, -4.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.ZERO);
/* 76 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   public void setupAnim(EquineRenderState state) {
/* 81 */     super.setupAnim(state);
/* 82 */     for (ModelPart part : this.ridingParts)
/* 83 */       part.visible = state.isRidden; 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\equine\EquineSaddleModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */