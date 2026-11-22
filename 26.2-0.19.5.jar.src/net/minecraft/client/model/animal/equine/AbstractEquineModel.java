/*     */ package net.minecraft.client.model.animal.equine;
/*     */ 
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.EquineRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public abstract class AbstractEquineModel<T extends EquineRenderState> extends EntityModel<T> {
/*     */   private static final float DEG_125 = 2.1816616F;
/*     */   
/*     */   private static final float DEG_60 = 1.0471976F;
/*     */   
/*     */   private static final float DEG_45 = 0.7853982F;
/*     */   
/*     */   private static final float DEG_30 = 0.5235988F;
/*     */   
/*     */   private static final float DEG_15 = 0.2617994F;
/*     */   
/*     */   protected static final String HEAD_PARTS = "head_parts";
/*     */   
/*     */   protected final ModelPart body;
/*     */   
/*     */   protected final ModelPart headParts;
/*     */   
/*     */   protected final ModelPart rightHindLeg;
/*     */   
/*     */   protected final ModelPart leftHindLeg;
/*     */   
/*     */   protected final ModelPart rightFrontLeg;
/*     */   
/*     */   protected final ModelPart leftFrontLeg;
/*     */   
/*     */   private final ModelPart tail;
/*     */   
/*     */   public AbstractEquineModel(ModelPart root) {
/*  33 */     super(root);
/*  34 */     this.body = root.getChild("body");
/*  35 */     this.headParts = root.getChild("head_parts");
/*  37 */     this.rightHindLeg = root.getChild("right_hind_leg");
/*  38 */     this.leftHindLeg = root.getChild("left_hind_leg");
/*  39 */     this.rightFrontLeg = root.getChild("right_front_leg");
/*  40 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*  42 */     this.tail = this.body.getChild("tail");
/*     */   }
/*     */   
/*     */   public AbstractEquineModel(ModelPart root, ModelPart headParts, ModelPart rightHindLeg, ModelPart rightFrontLeg, ModelPart leftHindLeg, ModelPart leftFrontLeg, ModelPart tail) {
/*  46 */     super(root);
/*  47 */     this.body = root.getChild("body");
/*  48 */     this.headParts = headParts;
/*  49 */     this.rightHindLeg = rightHindLeg;
/*  50 */     this.leftHindLeg = leftHindLeg;
/*  51 */     this.rightFrontLeg = rightFrontLeg;
/*  52 */     this.leftFrontLeg = leftFrontLeg;
/*  53 */     this.tail = tail;
/*     */   }
/*     */   
/*     */   public static MeshDefinition createBodyMesh(CubeDeformation g) {
/*  57 */     MeshDefinition mesh = new MeshDefinition();
/*  58 */     PartDefinition root = mesh.getRoot();
/*  59 */     PartDefinition body = root.addOrReplaceChild("body", 
/*  60 */         CubeListBuilder.create()
/*  61 */         .texOffs(0, 32).addBox(-5.0F, -8.0F, -17.0F, 10.0F, 10.0F, 22.0F, new CubeDeformation(0.05F)), 
/*  62 */         PartPose.offset(0.0F, 11.0F, 5.0F));
/*  66 */     PartDefinition headParts = root.addOrReplaceChild("head_parts", 
/*  67 */         CubeListBuilder.create()
/*  68 */         .texOffs(0, 35).addBox(-2.05F, -6.0F, -2.0F, 4.0F, 12.0F, 7.0F), 
/*  69 */         PartPose.offsetAndRotation(0.0F, 4.0F, -12.0F, 0.5235988F, 0.0F, 0.0F));
/*  72 */     PartDefinition head = headParts.addOrReplaceChild("head", 
/*  73 */         CubeListBuilder.create()
/*  74 */         .texOffs(0, 13).addBox(-3.0F, -11.0F, -2.0F, 6.0F, 5.0F, 7.0F, g), PartPose.ZERO);
/*  76 */     headParts.addOrReplaceChild("mane", 
/*  77 */         CubeListBuilder.create()
/*  78 */         .texOffs(56, 36).addBox(-1.0F, -11.0F, 5.01F, 2.0F, 16.0F, 2.0F, g), PartPose.ZERO);
/*  81 */     headParts.addOrReplaceChild("upper_mouth", 
/*  82 */         CubeListBuilder.create()
/*  83 */         .texOffs(0, 25).addBox(-2.0F, -11.0F, -7.0F, 4.0F, 5.0F, 5.0F, g), PartPose.ZERO);
/*  87 */     root.addOrReplaceChild("left_hind_leg", 
/*  88 */         CubeListBuilder.create()
/*  89 */         .texOffs(48, 21).mirror().addBox(-3.0F, -1.01F, -1.0F, 4.0F, 11.0F, 4.0F, g), 
/*  90 */         PartPose.offset(4.0F, 14.0F, 7.0F));
/*  92 */     root.addOrReplaceChild("right_hind_leg", 
/*  93 */         CubeListBuilder.create()
/*  94 */         .texOffs(48, 21).addBox(-1.0F, -1.01F, -1.0F, 4.0F, 11.0F, 4.0F, g), 
/*  95 */         PartPose.offset(-4.0F, 14.0F, 7.0F));
/*  97 */     root.addOrReplaceChild("left_front_leg", 
/*  98 */         CubeListBuilder.create()
/*  99 */         .texOffs(48, 21).mirror().addBox(-3.0F, -1.01F, -1.9F, 4.0F, 11.0F, 4.0F, g), 
/* 100 */         PartPose.offset(4.0F, 14.0F, -10.0F));
/* 102 */     root.addOrReplaceChild("right_front_leg", 
/* 103 */         CubeListBuilder.create()
/* 104 */         .texOffs(48, 21).addBox(-1.0F, -1.01F, -1.9F, 4.0F, 11.0F, 4.0F, g), 
/* 105 */         PartPose.offset(-4.0F, 14.0F, -10.0F));
/* 108 */     body.addOrReplaceChild("tail", 
/* 109 */         CubeListBuilder.create()
/* 110 */         .texOffs(42, 36).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 14.0F, 4.0F, g), 
/* 111 */         PartPose.offsetAndRotation(0.0F, -5.0F, 2.0F, 0.5235988F, 0.0F, 0.0F));
/* 114 */     head.addOrReplaceChild("left_ear", 
/* 115 */         CubeListBuilder.create()
/* 116 */         .texOffs(19, 16).addBox(0.55F, -13.0F, 4.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.ZERO);
/* 119 */     head.addOrReplaceChild("right_ear", 
/* 120 */         CubeListBuilder.create()
/* 121 */         .texOffs(19, 16).addBox(-2.55F, -13.0F, 4.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)), PartPose.ZERO);
/* 125 */     return mesh;
/*     */   }
/*     */   
/*     */   public void setupAnim(T state) {
/* 130 */     super.setupAnim(state);
/* 133 */     float clampedYRot = Mth.clamp(((EquineRenderState)state).yRot, -20.0F, 20.0F);
/* 135 */     float headRotXRad = ((EquineRenderState)state).xRot * 0.017453292F;
/* 136 */     float animationSpeed = ((EquineRenderState)state).walkAnimationSpeed;
/* 137 */     float animationPos = ((EquineRenderState)state).walkAnimationPos;
/* 138 */     if (animationSpeed > 0.2F)
/* 139 */       headRotXRad += Mth.cos((animationPos * 0.8F)) * 0.15F * animationSpeed; 
/* 142 */     float eating = ((EquineRenderState)state).eatAnimation;
/* 143 */     float standing = ((EquineRenderState)state).standAnimation;
/* 144 */     float iStanding = 1.0F - standing;
/* 145 */     float feedingAnim = ((EquineRenderState)state).feedingAnimation;
/* 146 */     boolean animateTail = ((EquineRenderState)state).animateTail;
/* 148 */     this.headParts.xRot = 0.5235988F + headRotXRad;
/* 149 */     this.headParts.yRot = clampedYRot * 0.017453292F;
/* 151 */     float waterMultiplier = ((EquineRenderState)state).isInWater ? 0.2F : 1.0F;
/* 152 */     float legAnim1 = Mth.cos((waterMultiplier * animationPos * 0.6662F + 3.1415927F));
/* 153 */     float legXRotAnim = legAnim1 * 0.8F * animationSpeed;
/* 156 */     float baseHeadAngle = (1.0F - Math.max(standing, eating)) * (0.5235988F + headRotXRad + feedingAnim * Mth.sin(((EquineRenderState)state).ageInTicks) * 0.05F);
/* 157 */     this.headParts.xRot = standing * (0.2617994F + headRotXRad) + eating * (2.1816616F + Mth.sin(((EquineRenderState)state).ageInTicks) * 0.05F) + baseHeadAngle;
/* 158 */     this.headParts.yRot = standing * clampedYRot * 0.017453292F + (1.0F - Math.max(standing, eating)) * this.headParts.yRot;
/* 160 */     animateHeadPartsPlacement(eating, standing);
/* 162 */     this.body.xRot = standing * -0.7853982F + iStanding * this.body.xRot;
/* 163 */     this.leftFrontLeg.y -= getLegStandingYOffset() * standing;
/* 164 */     this.leftFrontLeg.z += getLegStandingZOffset() * standing;
/* 165 */     this.rightFrontLeg.y = this.leftFrontLeg.y;
/* 166 */     this.rightFrontLeg.z = this.leftFrontLeg.z;
/* 168 */     float standAngle = getLegStandAngle() * standing;
/* 169 */     float bobValue = Mth.cos((((EquineRenderState)state).ageInTicks * 0.6F + 3.1415927F));
/* 170 */     float legStandingXRotOffset = getLegStandingXRotOffset();
/* 171 */     float rlegRot = (legStandingXRotOffset + bobValue) * standing + legXRotAnim * iStanding;
/* 172 */     float llegRot = (legStandingXRotOffset - bobValue) * standing - legXRotAnim * iStanding;
/* 173 */     this.leftHindLeg.xRot = standAngle - legAnim1 * 0.5F * animationSpeed * iStanding;
/* 174 */     this.rightHindLeg.xRot = standAngle + legAnim1 * 0.5F * animationSpeed * iStanding;
/* 175 */     this.leftFrontLeg.xRot = rlegRot;
/* 176 */     this.rightFrontLeg.xRot = llegRot;
/* 177 */     offsetLegPositionWhenStanding(standing);
/* 179 */     float ageScale = ((EquineRenderState)state).ageScale;
/* 180 */     this.tail.xRot = getTailXRotOffset() + 0.5235988F + animationSpeed * 0.75F;
/* 181 */     this.tail.y += animationSpeed * ageScale;
/* 182 */     this.tail.z += animationSpeed * 2.0F * ageScale;
/* 184 */     if (animateTail) {
/* 185 */       this.tail.yRot = Mth.cos((((EquineRenderState)state).ageInTicks * 0.7F));
/*     */     } else {
/* 187 */       this.tail.yRot = 0.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void offsetLegPositionWhenStanding(float standing) {}
/*     */   
/*     */   protected float getLegStandAngle() {
/* 196 */     return 0.2617994F;
/*     */   }
/*     */   
/*     */   protected float getLegStandingYOffset() {
/* 200 */     return 12.0F;
/*     */   }
/*     */   
/*     */   protected float getLegStandingZOffset() {
/* 204 */     return 4.0F;
/*     */   }
/*     */   
/*     */   protected float getLegStandingXRotOffset() {
/* 208 */     return -1.0471976F;
/*     */   }
/*     */   
/*     */   protected float getTailXRotOffset() {
/* 212 */     return 0.0F;
/*     */   }
/*     */   
/*     */   protected void animateHeadPartsPlacement(float eating, float standing) {
/* 216 */     this.headParts.y += Mth.lerp(eating, Mth.lerp(standing, 0.0F, -8.0F), 7.0F);
/* 217 */     this.headParts.z = Mth.lerp(standing, this.headParts.z, -4.0F);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\equine\AbstractEquineModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */