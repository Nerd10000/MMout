/*     */ package net.minecraft.client.model.animal.feline;
/*     */ 
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.FelineRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class AdultFelineModel<T extends FelineRenderState> extends AbstractFelineModel<T> {
/*     */   private static final float XO = 0.0F;
/*     */   
/*     */   private static final float YO = 16.0F;
/*     */   
/*     */   private static final float ZO = -9.0F;
/*     */   
/*     */   protected static final float BACK_LEG_Y = 18.0F;
/*     */   
/*     */   protected static final float BACK_LEG_Z = 5.0F;
/*     */   
/*     */   protected static final float FRONT_LEG_Y = 14.1F;
/*     */   
/*     */   private static final float FRONT_LEG_Z = -5.0F;
/*     */   
/*     */   public AdultFelineModel(ModelPart root) {
/*  24 */     super(root);
/*     */   }
/*     */   
/*     */   public static MeshDefinition createBodyMesh(CubeDeformation g) {
/*  28 */     MeshDefinition mesh = new MeshDefinition();
/*  29 */     PartDefinition root = mesh.getRoot();
/*  30 */     CubeDeformation tail_g = new CubeDeformation(-0.02F);
/*  32 */     root.addOrReplaceChild("head", 
/*  33 */         CubeListBuilder.create()
/*  34 */         .addBox("main", -2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 5.0F, g)
/*  35 */         .addBox("nose", -1.5F, -0.001F, -4.0F, 3, 2, 2, g, 0, 24)
/*  36 */         .addBox("ear1", -2.0F, -3.0F, 0.0F, 1, 1, 2, g, 0, 10)
/*  37 */         .addBox("ear2", 1.0F, -3.0F, 0.0F, 1, 1, 2, g, 6, 10), 
/*  38 */         PartPose.offset(0.0F, 15.0F, -9.0F));
/*  40 */     root.addOrReplaceChild("body", 
/*  41 */         CubeListBuilder.create()
/*  42 */         .texOffs(20, 0).addBox(-2.0F, 3.0F, -8.0F, 4.0F, 16.0F, 6.0F, g), 
/*  43 */         PartPose.offsetAndRotation(0.0F, 12.0F, -10.0F, 1.5707964F, 0.0F, 0.0F));
/*  45 */     root.addOrReplaceChild("tail1", 
/*  46 */         CubeListBuilder.create()
/*  47 */         .texOffs(0, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, g), 
/*  48 */         PartPose.offsetAndRotation(0.0F, 15.0F, 8.0F, 0.9F, 0.0F, 0.0F));
/*  50 */     root.addOrReplaceChild("tail2", 
/*  51 */         CubeListBuilder.create()
/*  52 */         .texOffs(4, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, tail_g), 
/*  53 */         PartPose.offset(0.0F, 20.0F, 14.0F));
/*  56 */     CubeListBuilder hindLeg = CubeListBuilder.create().texOffs(8, 13).addBox(-1.0F, 0.0F, 1.0F, 2.0F, 6.0F, 2.0F, g);
/*  57 */     root.addOrReplaceChild("left_hind_leg", hindLeg, PartPose.offset(1.1F, 18.0F, 5.0F));
/*  58 */     root.addOrReplaceChild("right_hind_leg", hindLeg, PartPose.offset(-1.1F, 18.0F, 5.0F));
/*  61 */     CubeListBuilder frontLeg = CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F, g);
/*  62 */     root.addOrReplaceChild("left_front_leg", frontLeg, PartPose.offset(1.2F, 14.1F, -5.0F));
/*  63 */     root.addOrReplaceChild("right_front_leg", frontLeg, PartPose.offset(-1.2F, 14.1F, -5.0F));
/*  65 */     return mesh;
/*     */   }
/*     */   
/*     */   public void setupAnim(T state) {
/*  70 */     super.setupAnim(state);
/*  72 */     float ageScale = ((FelineRenderState)state).ageScale;
/*  74 */     if (((FelineRenderState)state).isCrouching) {
/*  75 */       this.body.y += 1.0F * ageScale;
/*  76 */       this.head.y += 2.0F * ageScale;
/*  77 */       this.tail1.y += 1.0F * ageScale;
/*  78 */       this.tail2.y += -4.0F * ageScale;
/*  79 */       this.tail2.z += 2.0F * ageScale;
/*  80 */       this.tail1.xRot = 1.5707964F;
/*  81 */       this.tail2.xRot = 1.5707964F;
/*  82 */     } else if (((FelineRenderState)state).isSprinting) {
/*  83 */       this.tail2.y = this.tail1.y;
/*  84 */       this.tail2.z += 2.0F * ageScale;
/*  85 */       this.tail1.xRot = 1.5707964F;
/*  86 */       this.tail2.xRot = 1.5707964F;
/*     */     } 
/*  89 */     this.head.xRot = ((FelineRenderState)state).xRot * 0.017453292F;
/*  90 */     this.head.yRot = ((FelineRenderState)state).yRot * 0.017453292F;
/*  92 */     if (!((FelineRenderState)state).isSitting) {
/*  93 */       this.body.xRot = 1.5707964F;
/*  94 */       float animationSpeed = ((FelineRenderState)state).walkAnimationSpeed;
/*  95 */       float animationPos = ((FelineRenderState)state).walkAnimationPos;
/*  96 */       if (((FelineRenderState)state).isSprinting) {
/*  97 */         this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * animationSpeed;
/*  98 */         this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 0.3F)) * animationSpeed;
/*  99 */         this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F + 0.3F)) * animationSpeed;
/* 100 */         this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * animationSpeed;
/* 101 */         this.tail2.xRot = 1.7278761F + 0.31415927F * Mth.cos(animationPos) * animationSpeed;
/*     */       } else {
/* 103 */         this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * animationSpeed;
/* 104 */         this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * animationSpeed;
/* 105 */         this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * animationSpeed;
/* 106 */         this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F)) * animationSpeed;
/* 108 */         if (!((FelineRenderState)state).isCrouching) {
/* 109 */           this.tail2.xRot = 1.7278761F + 0.7853982F * Mth.cos(animationPos) * animationSpeed;
/*     */         } else {
/* 111 */           this.tail2.xRot = 1.7278761F + 0.47123894F * Mth.cos(animationPos) * animationSpeed;
/*     */         } 
/*     */       } 
/*     */     } 
/* 116 */     if (((FelineRenderState)state).isSitting) {
/* 117 */       this.body.xRot = 0.7853982F;
/* 118 */       this.body.y += -4.0F * ageScale;
/* 119 */       this.body.z += 5.0F * ageScale;
/* 120 */       this.head.y += -3.3F * ageScale;
/* 121 */       this.head.z += 1.0F * ageScale;
/* 123 */       this.tail1.y += 8.0F * ageScale;
/* 124 */       this.tail1.z += -2.0F * ageScale;
/* 125 */       this.tail2.y += 2.0F * ageScale;
/* 126 */       this.tail2.z += -0.8F * ageScale;
/* 127 */       this.tail1.xRot = 1.7278761F;
/* 128 */       this.tail2.xRot = 2.670354F;
/* 130 */       this.leftFrontLeg.xRot = -0.15707964F;
/* 131 */       this.leftFrontLeg.y += 2.0F * ageScale;
/* 132 */       this.leftFrontLeg.z -= 2.0F * ageScale;
/* 134 */       this.rightFrontLeg.xRot = -0.15707964F;
/* 135 */       this.rightFrontLeg.y += 2.0F * ageScale;
/* 136 */       this.rightFrontLeg.z -= 2.0F * ageScale;
/* 138 */       this.leftHindLeg.xRot = -1.5707964F;
/* 139 */       this.leftHindLeg.y += 3.0F * ageScale;
/* 140 */       this.leftHindLeg.z -= 4.0F * ageScale;
/* 142 */       this.rightHindLeg.xRot = -1.5707964F;
/* 143 */       this.rightHindLeg.y += 3.0F * ageScale;
/* 144 */       this.rightHindLeg.z -= 4.0F * ageScale;
/*     */     } 
/* 147 */     if (((FelineRenderState)state).lieDownAmount > 0.0F) {
/* 148 */       this.head.zRot = Mth.rotLerp(((FelineRenderState)state).lieDownAmount, this.head.zRot, -1.2707963F);
/* 149 */       this.head.yRot = Mth.rotLerp(((FelineRenderState)state).lieDownAmount, this.head.yRot, 1.2707963F);
/* 150 */       this.leftFrontLeg.xRot = -1.2707963F;
/* 151 */       this.rightFrontLeg.xRot = -0.47079635F;
/* 152 */       this.rightFrontLeg.zRot = -0.2F;
/* 153 */       this.rightFrontLeg.x += ageScale;
/* 154 */       this.leftHindLeg.xRot = -0.4F;
/* 155 */       this.rightHindLeg.xRot = 0.5F;
/* 156 */       this.rightHindLeg.zRot = -0.5F;
/* 157 */       this.rightHindLeg.x += 0.8F * ageScale;
/* 158 */       this.rightHindLeg.y += 2.0F * ageScale;
/* 160 */       this.tail1.xRot = Mth.rotLerp(((FelineRenderState)state).lieDownAmountTail, this.tail1.xRot, 0.8F);
/* 161 */       this.tail2.xRot = Mth.rotLerp(((FelineRenderState)state).lieDownAmountTail, this.tail2.xRot, -0.4F);
/*     */     } 
/* 164 */     if (((FelineRenderState)state).relaxStateOneAmount > 0.0F)
/* 165 */       this.head.xRot = Mth.rotLerp(((FelineRenderState)state).relaxStateOneAmount, this.head.xRot, -0.58177644F); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\feline\AdultFelineModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */