/*     */ package net.minecraft.client.model.monster.hoglin;
/*     */ 
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.HoglinRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class HoglinModel extends EntityModel<HoglinRenderState> {
/*     */   private static final float DEFAULT_HEAD_X_ROT = 0.87266463F;
/*     */   
/*     */   private static final float ATTACK_HEAD_X_ROT_END = -0.34906584F;
/*     */   
/*     */   protected final ModelPart head;
/*     */   
/*     */   private final ModelPart rightEar;
/*     */   
/*     */   private final ModelPart leftEar;
/*     */   
/*     */   private final ModelPart rightFrontLeg;
/*     */   
/*     */   private final ModelPart leftFrontLeg;
/*     */   
/*     */   private final ModelPart rightHindLeg;
/*     */   
/*     */   private final ModelPart leftHindLeg;
/*     */   
/*     */   public HoglinModel(ModelPart root) {
/*  29 */     super(root);
/*  30 */     this.head = root.getChild("head");
/*  31 */     this.rightEar = this.head.getChild("right_ear");
/*  32 */     this.leftEar = this.head.getChild("left_ear");
/*  33 */     this.rightFrontLeg = root.getChild("right_front_leg");
/*  34 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*  35 */     this.rightHindLeg = root.getChild("right_hind_leg");
/*  36 */     this.leftHindLeg = root.getChild("left_hind_leg");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  40 */     MeshDefinition mesh = new MeshDefinition();
/*  41 */     PartDefinition root = mesh.getRoot();
/*  43 */     PartDefinition body = root.addOrReplaceChild("body", 
/*  44 */         CubeListBuilder.create()
/*  45 */         .texOffs(1, 1).addBox(-8.0F, -7.0F, -13.0F, 16.0F, 14.0F, 26.0F), 
/*  46 */         PartPose.offset(0.0F, 7.0F, 0.0F));
/*  47 */     body.addOrReplaceChild("mane", 
/*  48 */         CubeListBuilder.create()
/*  49 */         .texOffs(90, 33).addBox(0.0F, 0.0F, -9.0F, 0.0F, 10.0F, 19.0F, new CubeDeformation(0.001F)), 
/*  50 */         PartPose.offset(0.0F, -14.0F, -7.0F));
/*  52 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  53 */         CubeListBuilder.create()
/*  54 */         .texOffs(61, 1).addBox(-7.0F, -3.0F, -19.0F, 14.0F, 6.0F, 19.0F), 
/*  55 */         PartPose.offsetAndRotation(0.0F, 2.0F, -12.0F, 0.87266463F, 0.0F, 0.0F));
/*  57 */     head.addOrReplaceChild("right_ear", 
/*  58 */         CubeListBuilder.create()
/*  59 */         .texOffs(1, 1).addBox(-6.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F), 
/*  60 */         PartPose.offsetAndRotation(-6.0F, -2.0F, -3.0F, 0.0F, 0.0F, -0.6981317F));
/*  62 */     head.addOrReplaceChild("left_ear", 
/*  63 */         CubeListBuilder.create()
/*  64 */         .texOffs(1, 6).addBox(0.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F), 
/*  65 */         PartPose.offsetAndRotation(6.0F, -2.0F, -3.0F, 0.0F, 0.0F, 0.6981317F));
/*  67 */     head.addOrReplaceChild("right_horn", 
/*  68 */         CubeListBuilder.create()
/*  69 */         .texOffs(10, 13).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F), 
/*  70 */         PartPose.offset(-7.0F, 2.0F, -12.0F));
/*  72 */     head.addOrReplaceChild("left_horn", 
/*  73 */         CubeListBuilder.create()
/*  74 */         .texOffs(1, 13).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F), 
/*  75 */         PartPose.offset(7.0F, 2.0F, -12.0F));
/*  78 */     int frontLegHeight = 14;
/*  79 */     int backLegHeight = 11;
/*  80 */     root.addOrReplaceChild("right_front_leg", 
/*  81 */         CubeListBuilder.create()
/*  82 */         .texOffs(66, 42).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F), 
/*  83 */         PartPose.offset(-4.0F, 10.0F, -8.5F));
/*  85 */     root.addOrReplaceChild("left_front_leg", 
/*  86 */         CubeListBuilder.create()
/*  87 */         .texOffs(41, 42).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F), 
/*  88 */         PartPose.offset(4.0F, 10.0F, -8.5F));
/*  90 */     root.addOrReplaceChild("right_hind_leg", 
/*  91 */         CubeListBuilder.create()
/*  92 */         .texOffs(21, 45).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F), 
/*  93 */         PartPose.offset(-5.0F, 13.0F, 10.0F));
/*  95 */     root.addOrReplaceChild("left_hind_leg", 
/*  96 */         CubeListBuilder.create()
/*  97 */         .texOffs(0, 45).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F), 
/*  98 */         PartPose.offset(5.0F, 13.0F, 10.0F));
/* 100 */     return LayerDefinition.create(mesh, 128, 64);
/*     */   }
/*     */   
/*     */   public void setupAnim(HoglinRenderState state) {
/* 105 */     super.setupAnim(state);
/* 107 */     float animationSpeed = state.walkAnimationSpeed;
/* 108 */     float animationPos = state.walkAnimationPos;
/* 109 */     this.rightEar.zRot = -0.6981317F - animationSpeed * Mth.sin(animationPos);
/* 110 */     this.leftEar.zRot = 0.6981317F + animationSpeed * Mth.sin(animationPos);
/* 111 */     this.head.yRot = state.yRot * 0.017453292F;
/* 114 */     float headbuttLerpFactor = 1.0F - Mth.abs(10 - 2 * state.attackAnimationRemainingTicks) / 10.0F;
/* 115 */     animateHeadbutt(headbuttLerpFactor);
/* 117 */     float amplitudeMultiplier = 1.2F;
/* 118 */     this.rightFrontLeg.xRot = Mth.cos(animationPos) * 1.2F * animationSpeed;
/* 119 */     this.leftFrontLeg.xRot = Mth.cos((animationPos + 3.1415927F)) * 1.2F * animationSpeed;
/* 120 */     this.rightHindLeg.xRot = this.leftFrontLeg.xRot;
/* 121 */     this.leftHindLeg.xRot = this.rightFrontLeg.xRot;
/*     */   }
/*     */   
/*     */   protected void animateHeadbutt(float headbuttLerpFactor) {
/* 125 */     this.head.xRot = Mth.lerp(headbuttLerpFactor, 0.87266463F, -0.34906584F);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\hoglin\HoglinModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */