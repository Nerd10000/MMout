/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class QuadrupedModel<T extends LivingEntityRenderState> extends EntityModel<T> {
/*    */   protected final ModelPart head;
/*    */   
/*    */   protected final ModelPart body;
/*    */   
/*    */   protected final ModelPart rightHindLeg;
/*    */   
/*    */   protected final ModelPart leftHindLeg;
/*    */   
/*    */   protected final ModelPart rightFrontLeg;
/*    */   
/*    */   protected final ModelPart leftFrontLeg;
/*    */   
/*    */   protected QuadrupedModel(ModelPart root) {
/* 27 */     this(root, RenderTypes::entityCutout);
/*    */   }
/*    */   
/*    */   protected QuadrupedModel(ModelPart root, Function<Identifier, RenderType> renderType) {
/* 31 */     super(root, renderType);
/* 32 */     this.head = root.getChild("head");
/* 33 */     this.body = root.getChild("body");
/* 34 */     this.rightHindLeg = root.getChild("right_hind_leg");
/* 35 */     this.leftHindLeg = root.getChild("left_hind_leg");
/* 36 */     this.rightFrontLeg = root.getChild("right_front_leg");
/* 37 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*    */   }
/*    */   
/*    */   public static MeshDefinition createBodyMesh(int legSize, boolean mirrorLeftLeg, boolean mirrorRightLeg, CubeDeformation g) {
/* 41 */     MeshDefinition mesh = new MeshDefinition();
/* 42 */     PartDefinition root = mesh.getRoot();
/* 44 */     root.addOrReplaceChild("head", 
/* 45 */         CubeListBuilder.create()
/* 46 */         .texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, g), 
/* 47 */         PartPose.offset(0.0F, (18 - legSize), -6.0F));
/* 49 */     root.addOrReplaceChild("body", 
/* 50 */         CubeListBuilder.create()
/* 51 */         .texOffs(28, 8).addBox(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 8.0F, g), 
/* 52 */         PartPose.offsetAndRotation(0.0F, (17 - legSize), 2.0F, 1.5707964F, 0.0F, 0.0F));
/* 54 */     createLegs(root, mirrorLeftLeg, mirrorRightLeg, legSize, g);
/* 55 */     return mesh;
/*    */   }
/*    */   
/*    */   public static void createLegs(PartDefinition root, boolean mirrorLeftLeg, boolean mirrorRightLeg, int legSize, CubeDeformation g) {
/* 59 */     CubeListBuilder rightLeg = CubeListBuilder.create().mirror(mirrorRightLeg).texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, legSize, 4.0F, g);
/* 60 */     CubeListBuilder leftLeg = CubeListBuilder.create().mirror(mirrorLeftLeg).texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, legSize, 4.0F, g);
/* 61 */     root.addOrReplaceChild("right_hind_leg", rightLeg, PartPose.offset(-3.0F, (24 - legSize), 7.0F));
/* 62 */     root.addOrReplaceChild("left_hind_leg", leftLeg, PartPose.offset(3.0F, (24 - legSize), 7.0F));
/* 63 */     root.addOrReplaceChild("right_front_leg", rightLeg, PartPose.offset(-3.0F, (24 - legSize), -5.0F));
/* 64 */     root.addOrReplaceChild("left_front_leg", leftLeg, PartPose.offset(3.0F, (24 - legSize), -5.0F));
/*    */   }
/*    */   
/*    */   public void setupAnim(T state) {
/* 69 */     super.setupAnim(state);
/* 71 */     this.head.xRot = ((LivingEntityRenderState)state).xRot * 0.017453292F;
/* 72 */     this.head.yRot = ((LivingEntityRenderState)state).yRot * 0.017453292F;
/* 74 */     float animationPos = ((LivingEntityRenderState)state).walkAnimationPos;
/* 75 */     float animationSpeed = ((LivingEntityRenderState)state).walkAnimationSpeed;
/* 77 */     this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/* 78 */     this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 79 */     this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 80 */     this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\QuadrupedModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */