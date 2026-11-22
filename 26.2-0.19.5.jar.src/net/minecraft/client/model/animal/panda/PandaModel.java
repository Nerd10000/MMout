/*     */ package net.minecraft.client.model.animal.panda;
/*     */ 
/*     */ import net.minecraft.client.model.QuadrupedModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.PandaRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class PandaModel extends QuadrupedModel<PandaRenderState> {
/*     */   public PandaModel(ModelPart root) {
/*  18 */     super(root);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  22 */     MeshDefinition mesh = new MeshDefinition();
/*  23 */     PartDefinition root = mesh.getRoot();
/*  25 */     root.addOrReplaceChild("head", 
/*  26 */         CubeListBuilder.create()
/*  27 */         .texOffs(0, 6).addBox(-6.5F, -5.0F, -4.0F, 13.0F, 10.0F, 9.0F)
/*  28 */         .texOffs(45, 16).addBox("nose", -3.5F, 0.0F, -6.0F, 7.0F, 5.0F, 2.0F)
/*  29 */         .texOffs(52, 25).addBox("left_ear", 3.5F, -8.0F, -1.0F, 5.0F, 4.0F, 1.0F)
/*  30 */         .texOffs(52, 25).addBox("right_ear", -8.5F, -8.0F, -1.0F, 5.0F, 4.0F, 1.0F), 
/*  31 */         PartPose.offset(0.0F, 11.5F, -17.0F));
/*  33 */     root.addOrReplaceChild("body", 
/*  34 */         CubeListBuilder.create()
/*  35 */         .texOffs(0, 25).addBox(-9.5F, -13.0F, -6.5F, 19.0F, 26.0F, 13.0F), 
/*  36 */         PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 1.5707964F, 0.0F, 0.0F));
/*  39 */     int legH = 9;
/*  40 */     int legW = 6;
/*  42 */     CubeListBuilder leg = CubeListBuilder.create().texOffs(40, 0).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F);
/*  43 */     root.addOrReplaceChild("right_hind_leg", leg, PartPose.offset(-5.5F, 15.0F, 9.0F));
/*  44 */     root.addOrReplaceChild("left_hind_leg", leg, PartPose.offset(5.5F, 15.0F, 9.0F));
/*  45 */     root.addOrReplaceChild("right_front_leg", leg, PartPose.offset(-5.5F, 15.0F, -9.0F));
/*  46 */     root.addOrReplaceChild("left_front_leg", leg, PartPose.offset(5.5F, 15.0F, -9.0F));
/*  48 */     return LayerDefinition.create(mesh, 64, 64);
/*     */   }
/*     */   
/*     */   public void setupAnim(PandaRenderState state) {
/*  53 */     super.setupAnim((LivingEntityRenderState)state);
/*  55 */     if (state.isUnhappy) {
/*  56 */       this.head.yRot = 0.35F * Mth.sin((0.6F * state.ageInTicks));
/*  57 */       this.head.zRot = 0.35F * Mth.sin((0.6F * state.ageInTicks));
/*  59 */       this.rightFrontLeg.xRot = -0.75F * Mth.sin((0.3F * state.ageInTicks));
/*  60 */       this.leftFrontLeg.xRot = 0.75F * Mth.sin((0.3F * state.ageInTicks));
/*     */     } else {
/*  62 */       this.head.zRot = 0.0F;
/*     */     } 
/*  65 */     if (state.isSneezing)
/*  66 */       if (state.sneezeTime < 15) {
/*  67 */         this.head.xRot = -0.7853982F * state.sneezeTime / 14.0F;
/*  68 */       } else if (state.sneezeTime < 20) {
/*  69 */         float internalSneezePos = ((state.sneezeTime - 15) / 5);
/*  70 */         this.head.xRot = -0.7853982F + 0.7853982F * internalSneezePos;
/*     */       }  
/*  74 */     if (state.sitAmount > 0.0F) {
/*  75 */       animateSitting(state);
/*  77 */       if (state.isEating) {
/*  78 */         this.head.xRot = 1.5707964F + 0.2F * Mth.sin((state.ageInTicks * 0.6F));
/*  80 */         this.rightFrontLeg.xRot = -0.4F - 0.2F * Mth.sin((state.ageInTicks * 0.6F));
/*  81 */         this.leftFrontLeg.xRot = -0.4F - 0.2F * Mth.sin((state.ageInTicks * 0.6F));
/*     */       } 
/*  84 */       if (state.isScared) {
/*  85 */         this.head.xRot = 2.1707964F;
/*  86 */         this.rightFrontLeg.xRot = -0.9F;
/*  87 */         this.leftFrontLeg.xRot = -0.9F;
/*     */       } 
/*     */     } else {
/*  90 */       this.rightHindLeg.zRot = 0.0F;
/*  91 */       this.leftHindLeg.zRot = 0.0F;
/*  92 */       this.rightFrontLeg.zRot = 0.0F;
/*  93 */       this.leftFrontLeg.zRot = 0.0F;
/*     */     } 
/*  96 */     if (state.lieOnBackAmount > 0.0F) {
/*  97 */       this.rightHindLeg.xRot = -0.6F * Mth.sin((state.ageInTicks * 0.15F));
/*  98 */       this.leftHindLeg.xRot = 0.6F * Mth.sin((state.ageInTicks * 0.15F));
/*  99 */       this.rightFrontLeg.xRot = 0.3F * Mth.sin((state.ageInTicks * 0.25F));
/* 100 */       this.leftFrontLeg.xRot = -0.3F * Mth.sin((state.ageInTicks * 0.25F));
/* 102 */       this.head.xRot = Mth.rotLerpRad(state.lieOnBackAmount, this.head.xRot, 1.5707964F);
/*     */     } 
/* 105 */     if (state.rollAmount > 0.0F) {
/* 106 */       this.head.xRot = Mth.rotLerpRad(state.rollAmount, this.head.xRot, 2.0561945F);
/* 108 */       this.rightHindLeg.xRot = -0.5F * Mth.sin((state.ageInTicks * 0.5F));
/* 109 */       this.leftHindLeg.xRot = 0.5F * Mth.sin((state.ageInTicks * 0.5F));
/* 110 */       this.rightFrontLeg.xRot = 0.5F * Mth.sin((state.ageInTicks * 0.5F));
/* 111 */       this.leftFrontLeg.xRot = -0.5F * Mth.sin((state.ageInTicks * 0.5F));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void animateSitting(PandaRenderState state) {
/* 116 */     this.body.xRot = Mth.rotLerpRad(state.sitAmount, this.body.xRot, 1.7407963F);
/* 117 */     this.head.xRot = Mth.rotLerpRad(state.sitAmount, this.head.xRot, 1.5707964F);
/* 120 */     this.rightFrontLeg.zRot = -0.27079642F;
/* 121 */     this.leftFrontLeg.zRot = 0.27079642F;
/* 124 */     this.rightHindLeg.zRot = 0.5707964F;
/* 125 */     this.leftHindLeg.zRot = -0.5707964F;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\panda\PandaModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */