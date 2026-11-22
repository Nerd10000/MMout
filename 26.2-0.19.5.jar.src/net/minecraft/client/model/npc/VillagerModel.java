/*     */ package net.minecraft.client.model.npc;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.HeadedModel;
/*     */ import net.minecraft.client.model.VillagerLikeModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.VillagerRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class VillagerModel extends EntityModel<VillagerRenderState> implements HeadedModel, VillagerLikeModel<VillagerRenderState> {
/*     */   private final ModelPart head;
/*     */   
/*     */   private final ModelPart rightLeg;
/*     */   
/*     */   private final ModelPart leftLeg;
/*     */   
/*     */   private final ModelPart arms;
/*     */   
/*     */   public VillagerModel(ModelPart root) {
/*  25 */     super(root);
/*  26 */     this.head = root.getChild("head");
/*  27 */     this.rightLeg = root.getChild("right_leg");
/*  28 */     this.leftLeg = root.getChild("left_leg");
/*  29 */     this.arms = root.getChild("arms");
/*     */   }
/*     */   
/*     */   public static MeshDefinition createBodyModel() {
/*  33 */     MeshDefinition mesh = new MeshDefinition();
/*  34 */     PartDefinition root = mesh.getRoot();
/*  36 */     float offset = 0.5F;
/*  38 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  39 */         CubeListBuilder.create()
/*  40 */         .texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.ZERO);
/*  43 */     PartDefinition hat = head.addOrReplaceChild("hat", 
/*  44 */         CubeListBuilder.create()
/*  45 */         .texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.51F)), PartPose.ZERO);
/*  48 */     hat.addOrReplaceChild("hat_rim", 
/*  49 */         CubeListBuilder.create()
/*  50 */         .texOffs(30, 47).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 1.0F), 
/*  51 */         PartPose.rotation(-1.5707964F, 0.0F, 0.0F));
/*  53 */     head.addOrReplaceChild("nose", 
/*  54 */         CubeListBuilder.create()
/*  55 */         .texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F), 
/*  56 */         PartPose.offset(0.0F, -2.0F, 0.0F));
/*  58 */     PartDefinition body = root.addOrReplaceChild("body", 
/*  59 */         CubeListBuilder.create()
/*  60 */         .texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F), PartPose.ZERO);
/*  63 */     body.addOrReplaceChild("jacket", 
/*  64 */         CubeListBuilder.create()
/*  65 */         .texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.ZERO);
/*  68 */     root.addOrReplaceChild("arms", 
/*  69 */         CubeListBuilder.create()
/*  70 */         .texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F)
/*  71 */         .texOffs(44, 22).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, true)
/*  72 */         .texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F), 
/*  73 */         PartPose.offsetAndRotation(0.0F, 3.0F, -1.0F, -0.75F, 0.0F, 0.0F));
/*  75 */     root.addOrReplaceChild("right_leg", 
/*  76 */         CubeListBuilder.create()
/*  77 */         .texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), 
/*  78 */         PartPose.offset(-2.0F, 12.0F, 0.0F));
/*  80 */     root.addOrReplaceChild("left_leg", 
/*  81 */         CubeListBuilder.create()
/*  82 */         .texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), 
/*  83 */         PartPose.offset(2.0F, 12.0F, 0.0F));
/*  86 */     return mesh;
/*     */   }
/*     */   
/*     */   public static MeshDefinition createNoHatModel() {
/*  90 */     MeshDefinition mesh = createBodyModel();
/*  91 */     mesh.getRoot().clearChild("head").clearRecursively();
/*  92 */     return mesh;
/*     */   }
/*     */   
/*     */   public void setupAnim(VillagerRenderState state) {
/*  97 */     super.setupAnim(state);
/*  99 */     this.head.yRot = state.yRot * 0.017453292F;
/* 100 */     this.head.xRot = state.xRot * 0.017453292F;
/* 102 */     if (state.isUnhappy) {
/* 103 */       this.head.zRot = 0.3F * Mth.sin((0.45F * state.ageInTicks));
/* 104 */       this.head.xRot = 0.4F;
/*     */     } else {
/* 106 */       this.head.zRot = 0.0F;
/*     */     } 
/* 109 */     this.rightLeg.xRot = Mth.cos((state.walkAnimationPos * 0.6662F)) * 1.4F * state.walkAnimationSpeed * 0.5F;
/* 110 */     this.leftLeg.xRot = Mth.cos((state.walkAnimationPos * 0.6662F + 3.1415927F)) * 1.4F * state.walkAnimationSpeed * 0.5F;
/* 111 */     this.rightLeg.yRot = 0.0F;
/* 112 */     this.leftLeg.yRot = 0.0F;
/*     */   }
/*     */   
/*     */   public ModelPart getHead() {
/* 117 */     return this.head;
/*     */   }
/*     */   
/*     */   public void translateToArms(VillagerRenderState state, PoseStack outputPoseStack) {
/* 122 */     this.root.translateAndRotate(outputPoseStack);
/* 123 */     this.arms.translateAndRotate(outputPoseStack);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\npc\VillagerModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */