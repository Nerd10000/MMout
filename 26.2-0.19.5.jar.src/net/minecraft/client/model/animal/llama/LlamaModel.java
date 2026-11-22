/*    */ package net.minecraft.client.model.animal.llama;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.LlamaRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class LlamaModel extends EntityModel<LlamaRenderState> {
/*    */   private final ModelPart head;
/*    */   
/*    */   private final ModelPart rightHindLeg;
/*    */   
/*    */   private final ModelPart leftHindLeg;
/*    */   
/*    */   private final ModelPart rightFrontLeg;
/*    */   
/*    */   private final ModelPart leftFrontLeg;
/*    */   
/*    */   private final ModelPart rightChest;
/*    */   
/*    */   private final ModelPart leftChest;
/*    */   
/*    */   public LlamaModel(ModelPart root) {
/* 26 */     super(root);
/* 27 */     this.head = root.getChild("head");
/* 28 */     this.rightChest = root.getChild("right_chest");
/* 29 */     this.leftChest = root.getChild("left_chest");
/* 30 */     this.rightHindLeg = root.getChild("right_hind_leg");
/* 31 */     this.leftHindLeg = root.getChild("left_hind_leg");
/* 32 */     this.rightFrontLeg = root.getChild("right_front_leg");
/* 33 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(CubeDeformation g) {
/* 37 */     MeshDefinition mesh = new MeshDefinition();
/* 38 */     PartDefinition root = mesh.getRoot();
/* 40 */     root.addOrReplaceChild("head", 
/* 41 */         CubeListBuilder.create()
/* 42 */         .texOffs(0, 0).addBox(-2.0F, -14.0F, -10.0F, 4.0F, 4.0F, 9.0F, g)
/* 43 */         .texOffs(0, 14).addBox("neck", -4.0F, -16.0F, -6.0F, 8.0F, 18.0F, 6.0F, g)
/* 44 */         .texOffs(17, 0).addBox("ear", -4.0F, -19.0F, -4.0F, 3.0F, 3.0F, 2.0F, g)
/* 45 */         .texOffs(17, 0).addBox("ear", 1.0F, -19.0F, -4.0F, 3.0F, 3.0F, 2.0F, g), 
/* 46 */         PartPose.offset(0.0F, 7.0F, -6.0F));
/* 48 */     root.addOrReplaceChild("body", 
/* 49 */         CubeListBuilder.create()
/* 50 */         .texOffs(29, 0).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F, g), 
/* 51 */         PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
/* 53 */     root.addOrReplaceChild("right_chest", 
/* 54 */         CubeListBuilder.create()
/* 55 */         .texOffs(45, 28).addBox(-3.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, g), 
/* 56 */         PartPose.offsetAndRotation(-8.5F, 3.0F, 3.0F, 0.0F, 1.5707964F, 0.0F));
/* 58 */     root.addOrReplaceChild("left_chest", 
/* 59 */         CubeListBuilder.create()
/* 60 */         .texOffs(45, 41).addBox(-3.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, g), 
/* 61 */         PartPose.offsetAndRotation(5.5F, 3.0F, 3.0F, 0.0F, 1.5707964F, 0.0F));
/* 63 */     int legWidth = 4;
/* 64 */     int legHeight = 14;
/* 66 */     CubeListBuilder leg = CubeListBuilder.create().texOffs(29, 29).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, g);
/* 67 */     root.addOrReplaceChild("right_hind_leg", leg, PartPose.offset(-3.5F, 10.0F, 6.0F));
/* 68 */     root.addOrReplaceChild("left_hind_leg", leg, PartPose.offset(3.5F, 10.0F, 6.0F));
/* 69 */     root.addOrReplaceChild("right_front_leg", leg, PartPose.offset(-3.5F, 10.0F, -5.0F));
/* 70 */     root.addOrReplaceChild("left_front_leg", leg, PartPose.offset(3.5F, 10.0F, -5.0F));
/* 72 */     return LayerDefinition.create(mesh, 128, 64);
/*    */   }
/*    */   
/*    */   public void setupAnim(LlamaRenderState state) {
/* 77 */     super.setupAnim(state);
/* 79 */     this.head.xRot = state.xRot * 0.017453292F;
/* 80 */     this.head.yRot = state.yRot * 0.017453292F;
/* 82 */     float animationSpeed = state.walkAnimationSpeed;
/* 83 */     float animationPos = state.walkAnimationPos;
/* 84 */     this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/* 85 */     this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 86 */     this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 87 */     this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/* 88 */     this.rightChest.visible = state.hasChest;
/* 89 */     this.leftChest.visible = state.hasChest;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\llama\LlamaModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */