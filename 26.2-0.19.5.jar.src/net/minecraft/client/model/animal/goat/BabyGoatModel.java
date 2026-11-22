/*    */ package net.minecraft.client.model.animal.goat;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.GoatRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ 
/*    */ public class BabyGoatModel extends GoatModel {
/*    */   private static final String HEAD_MAIN = "HeadMain";
/*    */   
/*    */   public BabyGoatModel(ModelPart root) {
/* 19 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 23 */     MeshDefinition mesh = new MeshDefinition();
/* 24 */     PartDefinition root = mesh.getRoot();
/* 26 */     root.addOrReplaceChild("left_hind_leg", 
/* 27 */         CubeListBuilder.create()
/* 28 */         .texOffs(29, 12).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F), 
/* 29 */         PartPose.offset(1.5F, 19.5F, 3.0F));
/* 31 */     root.addOrReplaceChild("right_hind_leg", 
/* 32 */         CubeListBuilder.create()
/* 33 */         .texOffs(21, 12).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F), 
/* 34 */         PartPose.offset(-1.5F, 19.5F, 3.0F));
/* 36 */     root.addOrReplaceChild("right_front_leg", 
/* 37 */         CubeListBuilder.create()
/* 38 */         .texOffs(21, 5).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F), 
/* 39 */         PartPose.offset(-1.5F, 19.5F, -2.0F));
/* 41 */     root.addOrReplaceChild("left_front_leg", 
/* 42 */         CubeListBuilder.create()
/* 43 */         .texOffs(29, 5).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F), 
/* 44 */         PartPose.offset(1.5F, 19.5F, -2.0F));
/* 46 */     root.addOrReplaceChild("body", 
/* 47 */         CubeListBuilder.create()
/* 48 */         .texOffs(0, 10).addBox(-3.0F, -2.3F, -4.5F, 6.0F, 5.0F, 9.0F)
/* 49 */         .texOffs(0, 24).addBox(-2.5F, -2.2F, -4.0F, 5.0F, 4.0F, 8.0F), 
/* 50 */         PartPose.offset(0.0F, 17.8F, 0.0F));
/* 52 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 53 */         CubeListBuilder.create()
/* 54 */         .texOffs(0, 0).addBox(-2.0F, -3.8126F, -5.1548F, 4.0F, 4.0F, 6.0F), 
/* 55 */         PartPose.offsetAndRotation(0.0F, 15.5F, -3.0F, 0.4363F, 0.0F, 0.0F));
/* 57 */     head.addOrReplaceChild("right_horn", 
/* 58 */         CubeListBuilder.create()
/* 59 */         .texOffs(24, 0).mirror().addBox(0.0F, -4.5F, 0.0F, 1.0F, 2.0F, 1.0F).mirror(false), 
/* 60 */         PartPose.offsetAndRotation(-1.5F, -1.5F, -1.0F, -0.3926991F, 0.0F, 0.0F));
/* 62 */     head.addOrReplaceChild("left_horn", 
/* 63 */         CubeListBuilder.create()
/* 64 */         .texOffs(24, 0).mirror().addBox(2.0F, -4.5F, 0.0F, 1.0F, 2.0F, 1.0F).mirror(false), 
/* 65 */         PartPose.offsetAndRotation(-1.5F, -1.5F, -1.0F, -0.3926991F, 0.0F, 0.0F));
/* 67 */     head.addOrReplaceChild("right_ear", 
/* 68 */         CubeListBuilder.create()
/* 69 */         .texOffs(0, 12).mirror().addBox(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F).mirror(false), 
/* 70 */         PartPose.offsetAndRotation(-1.7F, -2.3126F, 0.1452F, 0.0F, -0.5236F, 0.0F));
/* 72 */     head.addOrReplaceChild("left_ear", 
/* 73 */         CubeListBuilder.create()
/* 74 */         .texOffs(0, 12).addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F), 
/* 75 */         PartPose.offsetAndRotation(1.7F, -2.3126F, 0.1452F, 0.0F, 0.5236F, 0.0F));
/* 77 */     head.addOrReplaceChild("HeadMain", 
/* 78 */         CubeListBuilder.create()
/* 79 */         .texOffs(0, 0).addBox(-2.0F, -2.5F, -4.0F, 4.0F, 4.0F, 6.0F), 
/* 80 */         PartPose.offset(0.0F, -1.3126F, -1.1548F));
/* 82 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   public void setupAnim(GoatRenderState state) {
/* 87 */     super.setupAnim(state);
/* 89 */     if (state.rammingXHeadRot == 0.0F)
/* 90 */       this.head.xRot = 0.3926991F; 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\goat\BabyGoatModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */