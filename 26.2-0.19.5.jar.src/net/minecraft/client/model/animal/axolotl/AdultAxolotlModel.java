/*     */ package net.minecraft.client.model.animal.axolotl;
/*     */ 
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.AxolotlRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class AdultAxolotlModel extends EntityModel<AxolotlRenderState> {
/*     */   private static final float SWIMMING_LEG_XROT = 1.8849558F;
/*     */   
/*     */   private final ModelPart tail;
/*     */   
/*     */   private final ModelPart leftHindLeg;
/*     */   
/*     */   private final ModelPart rightHindLeg;
/*     */   
/*     */   private final ModelPart leftFrontLeg;
/*     */   
/*     */   private final ModelPart rightFrontLeg;
/*     */   
/*     */   private final ModelPart body;
/*     */   
/*     */   private final ModelPart head;
/*     */   
/*     */   private final ModelPart topGills;
/*     */   
/*     */   private final ModelPart leftGills;
/*     */   
/*     */   private final ModelPart rightGills;
/*     */   
/*     */   public AdultAxolotlModel(ModelPart root) {
/*  31 */     super(root);
/*  33 */     this.body = root.getChild("body");
/*  34 */     this.head = this.body.getChild("head");
/*  35 */     this.rightHindLeg = this.body.getChild("right_hind_leg");
/*  36 */     this.leftHindLeg = this.body.getChild("left_hind_leg");
/*  37 */     this.rightFrontLeg = this.body.getChild("right_front_leg");
/*  38 */     this.leftFrontLeg = this.body.getChild("left_front_leg");
/*  39 */     this.tail = this.body.getChild("tail");
/*  40 */     this.topGills = this.head.getChild("top_gills");
/*  41 */     this.leftGills = this.head.getChild("left_gills");
/*  42 */     this.rightGills = this.head.getChild("right_gills");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  46 */     MeshDefinition mesh = new MeshDefinition();
/*  47 */     PartDefinition root = mesh.getRoot();
/*  49 */     PartDefinition body = root.addOrReplaceChild("body", 
/*  50 */         CubeListBuilder.create()
/*  51 */         .texOffs(0, 11).addBox(-4.0F, -2.0F, -9.0F, 8.0F, 4.0F, 10.0F)
/*  52 */         .texOffs(2, 17).addBox(0.0F, -3.0F, -8.0F, 0.0F, 5.0F, 9.0F), 
/*  53 */         PartPose.offset(0.0F, 19.5F, 5.0F));
/*  56 */     CubeDeformation fudge = new CubeDeformation(0.001F);
/*  57 */     PartDefinition head = body.addOrReplaceChild("head", 
/*  58 */         CubeListBuilder.create()
/*  59 */         .texOffs(0, 1).addBox(-4.0F, -3.0F, -5.0F, 8.0F, 5.0F, 5.0F, fudge), 
/*  60 */         PartPose.offset(0.0F, 0.0F, -9.0F));
/*  64 */     CubeListBuilder topGills = CubeListBuilder.create().texOffs(3, 37).addBox(-4.0F, -3.0F, 0.0F, 8.0F, 3.0F, 0.0F, fudge);
/*  66 */     CubeListBuilder leftGills = CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -5.0F, 0.0F, 3.0F, 7.0F, 0.0F, fudge);
/*  68 */     CubeListBuilder rightGills = CubeListBuilder.create().texOffs(11, 40).addBox(0.0F, -5.0F, 0.0F, 3.0F, 7.0F, 0.0F, fudge);
/*  70 */     head.addOrReplaceChild("top_gills", topGills, PartPose.offset(0.0F, -3.0F, -1.0F));
/*  71 */     head.addOrReplaceChild("left_gills", leftGills, PartPose.offset(-4.0F, 0.0F, -1.0F));
/*  72 */     head.addOrReplaceChild("right_gills", rightGills, PartPose.offset(4.0F, 0.0F, -1.0F));
/*  75 */     CubeListBuilder leftLeg = CubeListBuilder.create().texOffs(2, 13).addBox(-1.0F, 0.0F, 0.0F, 3.0F, 5.0F, 0.0F, fudge);
/*  77 */     CubeListBuilder rightLeg = CubeListBuilder.create().texOffs(2, 13).addBox(-2.0F, 0.0F, 0.0F, 3.0F, 5.0F, 0.0F, fudge);
/*  79 */     body.addOrReplaceChild("right_hind_leg", rightLeg, PartPose.offset(-3.5F, 1.0F, -1.0F));
/*  80 */     body.addOrReplaceChild("left_hind_leg", leftLeg, PartPose.offset(3.5F, 1.0F, -1.0F));
/*  81 */     body.addOrReplaceChild("right_front_leg", rightLeg, PartPose.offset(-3.5F, 1.0F, -8.0F));
/*  82 */     body.addOrReplaceChild("left_front_leg", leftLeg, PartPose.offset(3.5F, 1.0F, -8.0F));
/*  83 */     body.addOrReplaceChild("tail", 
/*  84 */         CubeListBuilder.create()
/*  85 */         .texOffs(2, 19).addBox(0.0F, -3.0F, 0.0F, 0.0F, 5.0F, 12.0F), 
/*  86 */         PartPose.offset(0.0F, 0.0F, 1.0F));
/*  89 */     return LayerDefinition.create(mesh, 64, 64);
/*     */   }
/*     */   
/*     */   public void setupAnim(AxolotlRenderState state) {
/*  94 */     super.setupAnim(state);
/*  96 */     float playingDeadFactor = state.playingDeadFactor;
/*  97 */     float inWaterFactor = state.inWaterFactor;
/*  98 */     float onGroundFactor = state.onGroundFactor;
/*  99 */     float movingFactor = state.movingFactor;
/* 100 */     float notMovingFactor = 1.0F - movingFactor;
/* 102 */     float mirroredLegsFactor = 1.0F - Math.min(onGroundFactor, movingFactor);
/* 104 */     this.body.yRot += state.yRot * 0.017453292F;
/* 106 */     setupSwimmingAnimation(state.ageInTicks, state.xRot, Math.min(movingFactor, inWaterFactor));
/* 107 */     setupWaterHoveringAnimation(state.ageInTicks, Math.min(notMovingFactor, inWaterFactor));
/* 108 */     setupGroundCrawlingAnimation(state.ageInTicks, Math.min(movingFactor, onGroundFactor));
/* 109 */     setupLayStillOnGroundAnimation(state.ageInTicks, Math.min(notMovingFactor, onGroundFactor));
/* 110 */     setupPlayDeadAnimation(playingDeadFactor);
/* 112 */     applyMirrorLegRotations(mirroredLegsFactor);
/*     */   }
/*     */   
/*     */   private void setupLayStillOnGroundAnimation(float ageInTicks, float factor) {
/* 117 */     if (factor <= 1.0E-5F)
/*     */       return; 
/* 121 */     float animMoveSpeed = ageInTicks * 0.09F;
/* 122 */     float sineSway = Mth.sin(animMoveSpeed);
/* 123 */     float cosineSway = Mth.cos(animMoveSpeed);
/* 124 */     float movement = sineSway * sineSway - 2.0F * sineSway;
/* 125 */     float movement2 = cosineSway * cosineSway - 3.0F * sineSway;
/* 127 */     this.head.xRot += -0.09F * movement * factor;
/* 128 */     this.head.zRot += -0.2F * factor;
/* 130 */     this.tail.yRot += (-0.1F + 0.1F * movement) * factor;
/* 132 */     float gillAngle = (0.6F + 0.05F * movement2) * factor;
/* 133 */     this.topGills.xRot += gillAngle;
/* 134 */     this.leftGills.yRot -= gillAngle;
/* 135 */     this.rightGills.yRot += gillAngle;
/* 137 */     this.leftHindLeg.xRot += 1.1F * factor;
/* 138 */     this.leftHindLeg.yRot += 1.0F * factor;
/* 139 */     this.leftFrontLeg.xRot += 0.8F * factor;
/* 140 */     this.leftFrontLeg.yRot += 2.3F * factor;
/* 141 */     this.leftFrontLeg.zRot -= 0.5F * factor;
/*     */   }
/*     */   
/*     */   private void setupGroundCrawlingAnimation(float ageInTicks, float factor) {
/* 146 */     if (factor <= 1.0E-5F)
/*     */       return; 
/* 150 */     float animMoveSpeed = ageInTicks * 0.11F;
/* 151 */     float cosineSway = Mth.cos(animMoveSpeed);
/* 152 */     float hindLegYRotSway = (cosineSway * cosineSway - 2.0F * cosineSway) / 5.0F;
/* 153 */     float frontLegYRotSway = 0.7F * cosineSway;
/* 155 */     float headAndTailYRot = 0.09F * cosineSway * factor;
/* 156 */     this.head.yRot += headAndTailYRot;
/* 157 */     this.tail.yRot += headAndTailYRot;
/* 159 */     float gillAngle = (0.6F - 0.08F * (cosineSway * cosineSway + 2.0F * Mth.sin(animMoveSpeed))) * factor;
/* 160 */     this.topGills.xRot += gillAngle;
/* 161 */     this.leftGills.yRot -= gillAngle;
/* 162 */     this.rightGills.yRot += gillAngle;
/* 164 */     float hindLegXRot = 0.9424779F * factor;
/* 165 */     float frontLegXRot = 1.0995574F * factor;
/* 166 */     this.leftHindLeg.xRot += hindLegXRot;
/* 167 */     this.leftHindLeg.yRot += (1.5F - hindLegYRotSway) * factor;
/* 168 */     this.leftHindLeg.zRot += -0.1F * factor;
/* 169 */     this.leftFrontLeg.xRot += frontLegXRot;
/* 170 */     this.leftFrontLeg.yRot += (1.5707964F - frontLegYRotSway) * factor;
/* 171 */     this.rightHindLeg.xRot += hindLegXRot;
/* 172 */     this.rightHindLeg.yRot += (-1.0F - hindLegYRotSway) * factor;
/* 173 */     this.rightFrontLeg.xRot += frontLegXRot;
/* 174 */     this.rightFrontLeg.yRot += (-1.5707964F - frontLegYRotSway) * factor;
/*     */   }
/*     */   
/*     */   private void setupWaterHoveringAnimation(float ageInTicks, float factor) {
/* 179 */     if (factor <= 1.0E-5F)
/*     */       return; 
/* 183 */     float animMoveSpeed = ageInTicks * 0.075F;
/* 184 */     float cosineSway = Mth.cos(animMoveSpeed);
/* 185 */     float sineSway = Mth.sin(animMoveSpeed) * 0.15F;
/* 187 */     float bodyXRot = (-0.15F + 0.075F * cosineSway) * factor;
/* 188 */     this.body.xRot += bodyXRot;
/* 189 */     this.body.y -= sineSway * factor;
/* 191 */     this.head.xRot -= bodyXRot;
/* 193 */     this.topGills.xRot += 0.2F * cosineSway * factor;
/* 194 */     float gillYRot = (-0.3F * cosineSway - 0.19F) * factor;
/* 195 */     this.leftGills.yRot += gillYRot;
/* 196 */     this.rightGills.yRot -= gillYRot;
/* 198 */     this.leftHindLeg.xRot += (2.3561945F - cosineSway * 0.11F) * factor;
/* 199 */     this.leftHindLeg.yRot += 0.47123894F * factor;
/* 200 */     this.leftHindLeg.zRot += 1.7278761F * factor;
/* 201 */     this.leftFrontLeg.xRot += (0.7853982F - cosineSway * 0.2F) * factor;
/* 202 */     this.leftFrontLeg.yRot += 2.042035F * factor;
/* 204 */     this.tail.yRot += 0.5F * cosineSway * factor;
/*     */   }
/*     */   
/*     */   private void setupSwimmingAnimation(float ageInTicks, float xRot, float factor) {
/* 209 */     if (factor <= 1.0E-5F)
/*     */       return; 
/* 213 */     float animMoveSpeed = ageInTicks * 0.33F;
/* 214 */     float sineSway = Mth.sin(animMoveSpeed);
/* 215 */     float cosineSway = Mth.cos(animMoveSpeed);
/* 216 */     float bodySway = 0.13F * sineSway;
/* 218 */     this.body.xRot += (xRot * 0.017453292F + bodySway) * factor;
/* 219 */     this.head.xRot -= bodySway * 1.8F * factor;
/* 220 */     this.body.y -= 0.45F * cosineSway * factor;
/* 222 */     this.topGills.xRot += (-0.5F * sineSway - 0.8F) * factor;
/* 223 */     float gillYRot = (0.3F * sineSway + 0.9F) * factor;
/* 224 */     this.leftGills.yRot += gillYRot;
/* 225 */     this.rightGills.yRot -= gillYRot;
/* 227 */     this.tail.yRot += 0.3F * Mth.cos((animMoveSpeed * 0.9F)) * factor;
/* 229 */     this.leftHindLeg.xRot += 1.8849558F * factor;
/* 230 */     this.leftHindLeg.yRot += -0.4F * sineSway * factor;
/* 231 */     this.leftHindLeg.zRot += 1.5707964F * factor;
/* 232 */     this.leftFrontLeg.xRot += 1.8849558F * factor;
/* 233 */     this.leftFrontLeg.yRot += (-0.2F * cosineSway - 0.1F) * factor;
/* 234 */     this.leftFrontLeg.zRot += 1.5707964F * factor;
/*     */   }
/*     */   
/*     */   private void setupPlayDeadAnimation(float factor) {
/* 239 */     if (factor <= 1.0E-5F)
/*     */       return; 
/* 243 */     this.leftHindLeg.xRot += 1.4137167F * factor;
/* 244 */     this.leftHindLeg.yRot += 1.0995574F * factor;
/* 245 */     this.leftHindLeg.zRot += 0.7853982F * factor;
/* 246 */     this.leftFrontLeg.xRot += 0.7853982F * factor;
/* 247 */     this.leftFrontLeg.yRot += 2.042035F * factor;
/* 249 */     this.body.xRot += -0.15F * factor;
/* 250 */     this.body.zRot += 0.35F * factor;
/*     */   }
/*     */   
/*     */   private void applyMirrorLegRotations(float factor) {
/* 255 */     if (factor <= 1.0E-5F)
/*     */       return; 
/* 258 */     this.rightHindLeg.xRot += this.leftHindLeg.xRot * factor;
/* 259 */     ModelPart modelPart = this.rightHindLeg;
/* 259 */     modelPart.yRot += -this.leftHindLeg.yRot * factor;
/* 260 */     modelPart = this.rightHindLeg;
/* 260 */     modelPart.zRot += -this.leftHindLeg.zRot * factor;
/* 261 */     this.rightFrontLeg.xRot += this.leftFrontLeg.xRot * factor;
/* 262 */     modelPart = this.rightFrontLeg;
/* 262 */     modelPart.yRot += -this.leftFrontLeg.yRot * factor;
/* 263 */     modelPart = this.rightFrontLeg;
/* 263 */     modelPart.zRot += -this.leftFrontLeg.zRot * factor;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\axolotl\AdultAxolotlModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */