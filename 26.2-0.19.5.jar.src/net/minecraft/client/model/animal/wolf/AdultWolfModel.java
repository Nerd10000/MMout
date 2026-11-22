/*    */ package net.minecraft.client.model.animal.wolf;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.WolfRenderState;
/*    */ 
/*    */ public class AdultWolfModel extends WolfModel {
/*    */   private static final String REAL_HEAD = "real_head";
/*    */   
/*    */   private static final String UPPER_BODY = "upper_body";
/*    */   
/*    */   private static final String REAL_TAIL = "real_tail";
/*    */   
/*    */   private final ModelPart realHead;
/*    */   
/*    */   private final ModelPart realTail;
/*    */   
/*    */   private final ModelPart upperBody;
/*    */   
/*    */   private static final int LEG_SIZE = 8;
/*    */   
/*    */   public AdultWolfModel(ModelPart root) {
/* 25 */     super(root);
/* 26 */     this.realHead = this.head.getChild("real_head");
/* 27 */     this.upperBody = root.getChild("upper_body");
/* 28 */     this.realTail = this.tail.getChild("real_tail");
/*    */   }
/*    */   
/*    */   public static MeshDefinition createBodyLayer(CubeDeformation g) {
/* 32 */     MeshDefinition mesh = new MeshDefinition();
/* 33 */     PartDefinition root = mesh.getRoot();
/* 35 */     float headHeight = 13.5F;
/* 37 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 38 */         CubeListBuilder.create(), 
/* 39 */         PartPose.offset(-1.0F, 13.5F, -7.0F));
/* 41 */     head.addOrReplaceChild("real_head", 
/* 42 */         CubeListBuilder.create()
/* 43 */         .texOffs(0, 0).addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, g)
/* 44 */         .texOffs(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, g)
/* 45 */         .texOffs(16, 14).addBox(2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, g)
/* 46 */         .texOffs(0, 10).addBox(-0.5F, -0.001F, -5.0F, 3.0F, 3.0F, 4.0F, g), PartPose.ZERO);
/* 49 */     root.addOrReplaceChild("body", 
/* 50 */         CubeListBuilder.create()
/* 51 */         .texOffs(18, 14).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, g), 
/* 52 */         PartPose.offsetAndRotation(0.0F, 14.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
/* 54 */     root.addOrReplaceChild("upper_body", 
/* 55 */         CubeListBuilder.create()
/* 56 */         .texOffs(21, 0).addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F, g), 
/* 57 */         PartPose.offsetAndRotation(-1.0F, 14.0F, -3.0F, 1.5707964F, 0.0F, 0.0F));
/* 60 */     CubeListBuilder leftLeg = CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, g);
/* 61 */     CubeListBuilder rightLeg = CubeListBuilder.create().mirror().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, g);
/* 62 */     root.addOrReplaceChild("right_hind_leg", rightLeg, PartPose.offset(-2.5F, 16.0F, 7.0F));
/* 63 */     root.addOrReplaceChild("left_hind_leg", leftLeg, PartPose.offset(0.5F, 16.0F, 7.0F));
/* 64 */     root.addOrReplaceChild("right_front_leg", rightLeg, PartPose.offset(-2.5F, 16.0F, -4.0F));
/* 65 */     root.addOrReplaceChild("left_front_leg", leftLeg, PartPose.offset(0.5F, 16.0F, -4.0F));
/* 67 */     PartDefinition tail = root.addOrReplaceChild("tail", 
/* 68 */         CubeListBuilder.create(), 
/* 69 */         PartPose.offsetAndRotation(-1.0F, 12.0F, 8.0F, 0.62831855F, 0.0F, 0.0F));
/* 71 */     tail.addOrReplaceChild("real_tail", 
/* 72 */         CubeListBuilder.create()
/* 73 */         .texOffs(9, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, g), PartPose.ZERO);
/* 77 */     return mesh;
/*    */   }
/*    */   
/*    */   protected void setSittingPose(WolfRenderState state) {
/* 82 */     super.setSittingPose(state);
/* 83 */     this.upperBody.y += 2.0F;
/* 84 */     this.upperBody.xRot = 1.2566371F;
/* 85 */     this.upperBody.yRot = 0.0F;
/*    */   }
/*    */   
/*    */   protected void shakeOffWater(WolfRenderState state) {
/* 90 */     super.shakeOffWater(state);
/* 91 */     this.realHead.zRot = state.headRollAngle + state.getBodyRollAngle(0.0F);
/* 92 */     this.upperBody.zRot = state.getBodyRollAngle(-0.08F);
/* 93 */     this.realTail.zRot = state.getBodyRollAngle(-0.2F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\wolf\AdultWolfModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */