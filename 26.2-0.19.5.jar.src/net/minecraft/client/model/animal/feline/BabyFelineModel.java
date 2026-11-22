/*     */ package net.minecraft.client.model.animal.feline;
/*     */ 
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.FelineRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class BabyFelineModel<S extends FelineRenderState> extends AbstractFelineModel<S> {
/*     */   protected BabyFelineModel(ModelPart root) {
/*  15 */     super(root);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBabyLayer() {
/*  20 */     MeshDefinition meshdefinition = new MeshDefinition();
/*  21 */     PartDefinition partdefinition = meshdefinition.getRoot();
/*  23 */     partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
/*  24 */         .texOffs(0, 0).addBox(-2.5F, -3.0F, -2.875F, 5.0F, 4.0F, 4.0F)
/*  25 */         .texOffs(18, 0).addBox(-2.0F, -4.0F, -0.875F, 1.0F, 1.0F, 2.0F)
/*  26 */         .texOffs(24, 0).addBox(1.0F, -4.0F, -0.875F, 1.0F, 1.0F, 2.0F)
/*  27 */         .texOffs(18, 3).addBox(-1.5F, -1.0F, -3.875F, 3.0F, 2.0F, 1.0F), PartPose.offset(0.0F, 20.0F, -3.125F));
/*  30 */     partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
/*  31 */         .texOffs(18, 18).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F), PartPose.offset(1.0F, 22.0F, -1.5F));
/*  34 */     partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
/*  35 */         .texOffs(12, 18).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F), PartPose.offset(-1.0F, 22.0F, -1.5F));
/*  38 */     partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
/*  39 */         .texOffs(18, 22).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F), PartPose.offset(1.0F, 22.0F, 2.5F));
/*  42 */     partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
/*  43 */         .texOffs(0, 8).addBox(-2.0F, -1.5F, -3.5F, 4.0F, 3.0F, 7.0F), PartPose.offset(0.0F, 20.5F, 0.5F));
/*  46 */     partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
/*  47 */         .texOffs(12, 22).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F), PartPose.offset(-1.0F, 22.0F, 2.5F));
/*  50 */     partdefinition.addOrReplaceChild("tail1", CubeListBuilder.create()
/*  51 */         .texOffs(0, 18).addBox(-0.5F, -0.107F, 0.0849F, 1.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 19.107F, 3.9151F, -0.567232F, 0.0F, 0.0F));
/*  54 */     partdefinition.addOrReplaceChild("tail2", CubeListBuilder.create(), PartPose.ZERO);
/*  56 */     return LayerDefinition.create(meshdefinition, 32, 32);
/*     */   }
/*     */   
/*     */   public void setupAnim(S state) {
/*  61 */     super.setupAnim(state);
/*  63 */     float ageScale = ((FelineRenderState)state).ageScale;
/*  65 */     if (((FelineRenderState)state).isCrouching) {
/*  66 */       this.body.y += 1.0F * ageScale;
/*  67 */       this.head.y += 2.0F * ageScale;
/*  68 */       this.tail1.y += 1.0F * ageScale;
/*  69 */       this.tail2.y += -4.0F * ageScale;
/*  70 */       this.tail2.z += 2.0F * ageScale;
/*  71 */       this.tail1.xRot = 1.5707964F;
/*  72 */       this.tail2.xRot = 1.5707964F;
/*  73 */     } else if (((FelineRenderState)state).isSprinting) {
/*  74 */       this.tail2.y = this.tail1.y;
/*  75 */       this.tail2.z += 2.0F * ageScale;
/*  76 */       this.tail1.xRot = 1.5707964F;
/*  77 */       this.tail2.xRot = 1.5707964F;
/*     */     } 
/*  80 */     this.head.xRot = ((FelineRenderState)state).xRot * 0.017453292F;
/*  81 */     this.head.yRot = ((FelineRenderState)state).yRot * 0.017453292F;
/*  83 */     if (!((FelineRenderState)state).isSitting) {
/*  84 */       float animationSpeed = ((FelineRenderState)state).walkAnimationSpeed;
/*  85 */       float animationPos = ((FelineRenderState)state).walkAnimationPos;
/*  86 */       if (((FelineRenderState)state).isSprinting) {
/*  87 */         this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * animationSpeed;
/*  88 */         this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 0.3F)) * animationSpeed;
/*  89 */         this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F + 0.3F)) * animationSpeed;
/*  90 */         this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * animationSpeed;
/*  91 */         this.tail2.xRot = 1.7278761F + 0.31415927F * Mth.cos(animationPos) * animationSpeed;
/*     */       } else {
/*  93 */         this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * animationSpeed;
/*  94 */         this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * animationSpeed;
/*  95 */         this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * animationSpeed;
/*  96 */         this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F)) * animationSpeed;
/*  98 */         if (!((FelineRenderState)state).isCrouching) {
/*  99 */           this.tail2.xRot = 1.7278761F + 0.7853982F * Mth.cos(animationPos) * animationSpeed;
/*     */         } else {
/* 101 */           this.tail2.xRot = 1.7278761F + 0.47123894F * Mth.cos(animationPos) * animationSpeed;
/*     */         } 
/*     */       } 
/* 104 */     } else if (((FelineRenderState)state).isSitting) {
/* 105 */       this.body.xRot += -0.43633232F;
/* 106 */       this.body.y += 1.25F;
/* 108 */       this.head.z += 0.75F;
/* 110 */       this.tail1.xRot += 0.5454154F;
/* 111 */       this.tail1.y += 4.0F;
/* 112 */       this.tail1.z -= 0.9F;
/* 114 */       this.leftHindLeg.z -= 0.9F;
/* 116 */       this.rightHindLeg.z -= 0.9F;
/*     */     } 
/* 119 */     if (((FelineRenderState)state).lieDownAmount > 0.0F) {
/* 120 */       this.body.x++;
/* 122 */       this.head.xRot = Mth.rotLerp(((FelineRenderState)state).lieDownAmount, this.head.xRot, 0.17453292F);
/* 123 */       this.head.zRot = Mth.rotLerp(((FelineRenderState)state).lieDownAmount, this.head.zRot, -1.3089969F);
/* 124 */       this.head.x += 1.5F;
/* 125 */       this.head.y += 0.75F;
/* 126 */       this.head.z -= 0.5F;
/* 128 */       this.rightFrontLeg.xRot = -0.7853982F;
/* 129 */       this.rightFrontLeg.x += 3.5F;
/* 130 */       this.rightFrontLeg.y -= 0.5F;
/* 131 */       this.rightFrontLeg.z += 0.0F;
/* 133 */       this.leftFrontLeg.xRot = -1.5707964F;
/* 134 */       this.leftFrontLeg.x += 1.5F;
/* 135 */       this.leftFrontLeg.y--;
/* 136 */       this.leftFrontLeg.z -= 2.0F;
/* 138 */       this.rightHindLeg.xRot = 0.6981317F;
/* 139 */       this.rightHindLeg.yRot = 0.34906584F;
/* 140 */       this.rightHindLeg.zRot = -0.34906584F;
/* 141 */       this.rightHindLeg.x += 2.5F;
/* 142 */       this.rightHindLeg.y -= 0.25F;
/* 143 */       this.rightHindLeg.z += 0.5F;
/* 145 */       this.leftHindLeg.x += 1.5F;
/* 146 */       this.leftHindLeg.z--;
/* 148 */       this.tail1.xRot += Mth.rotLerp(((FelineRenderState)state).lieDownAmountTail, this.tail1.xRot, -0.5235988F);
/* 149 */       this.tail1.yRot += Mth.rotLerp(((FelineRenderState)state).lieDownAmountTail, this.tail1.yRot, 0.0F);
/* 150 */       this.tail1.zRot += Mth.rotLerp(((FelineRenderState)state).lieDownAmountTail, this.tail1.zRot, -0.17453292F);
/* 151 */       this.tail1.x++;
/* 152 */       this.tail1.y += 0.5F;
/* 153 */       this.tail1.z -= 0.25F;
/*     */     } 
/* 156 */     if (((FelineRenderState)state).relaxStateOneAmount > 0.0F)
/* 157 */       this.head.xRot = Mth.rotLerp(((FelineRenderState)state).relaxStateOneAmount, this.head.xRot, -0.58177644F); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\feline\BabyFelineModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */