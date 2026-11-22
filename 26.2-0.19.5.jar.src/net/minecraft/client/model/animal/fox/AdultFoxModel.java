/*     */ package net.minecraft.client.model.animal.fox;
/*     */ 
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.FoxRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class AdultFoxModel extends FoxModel {
/*     */   private static final int LEG_SIZE = 6;
/*     */   
/*     */   private static final float HEAD_HEIGHT = 16.5F;
/*     */   
/*     */   private static final float LEG_POS = 17.5F;
/*     */   
/*     */   public AdultFoxModel(ModelPart root) {
/*  21 */     super(root);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  25 */     MeshDefinition mesh = new MeshDefinition();
/*  26 */     PartDefinition root = mesh.getRoot();
/*  28 */     PartDefinition head = root.addOrReplaceChild("head", 
/*  29 */         CubeListBuilder.create()
/*  30 */         .texOffs(1, 5).addBox(-3.0F, -2.0F, -5.0F, 8.0F, 6.0F, 6.0F), 
/*  31 */         PartPose.offset(-1.0F, 16.5F, -3.0F));
/*  33 */     head.addOrReplaceChild("right_ear", 
/*  34 */         CubeListBuilder.create()
/*  35 */         .texOffs(8, 1).addBox(-3.0F, -4.0F, -4.0F, 2.0F, 2.0F, 1.0F), PartPose.ZERO);
/*  38 */     head.addOrReplaceChild("left_ear", 
/*  39 */         CubeListBuilder.create()
/*  40 */         .texOffs(15, 1).addBox(3.0F, -4.0F, -4.0F, 2.0F, 2.0F, 1.0F), PartPose.ZERO);
/*  43 */     head.addOrReplaceChild("nose", 
/*  44 */         CubeListBuilder.create()
/*  45 */         .texOffs(6, 18).addBox(-1.0F, 2.01F, -8.0F, 4.0F, 2.0F, 3.0F), PartPose.ZERO);
/*  49 */     PartDefinition body = root.addOrReplaceChild("body", 
/*  50 */         CubeListBuilder.create()
/*  51 */         .texOffs(24, 15).addBox(-3.0F, 3.999F, -3.5F, 6.0F, 11.0F, 6.0F), 
/*  52 */         PartPose.offsetAndRotation(0.0F, 16.0F, -6.0F, 1.5707964F, 0.0F, 0.0F));
/*  54 */     CubeDeformation fudge = new CubeDeformation(0.001F);
/*  56 */     CubeListBuilder leftLeg = CubeListBuilder.create().texOffs(4, 24).addBox(2.0F, 0.5F, -1.0F, 2.0F, 6.0F, 2.0F, fudge);
/*  58 */     CubeListBuilder rightLeg = CubeListBuilder.create().texOffs(13, 24).addBox(2.0F, 0.5F, -1.0F, 2.0F, 6.0F, 2.0F, fudge);
/*  60 */     root.addOrReplaceChild("right_hind_leg", rightLeg, PartPose.offset(-5.0F, 17.5F, 7.0F));
/*  61 */     root.addOrReplaceChild("left_hind_leg", leftLeg, PartPose.offset(-1.0F, 17.5F, 7.0F));
/*  62 */     root.addOrReplaceChild("right_front_leg", rightLeg, PartPose.offset(-5.0F, 17.5F, 0.0F));
/*  63 */     root.addOrReplaceChild("left_front_leg", leftLeg, PartPose.offset(-1.0F, 17.5F, 0.0F));
/*  64 */     body.addOrReplaceChild("tail", 
/*  65 */         CubeListBuilder.create()
/*  66 */         .texOffs(30, 0).addBox(2.0F, 0.0F, -1.0F, 4.0F, 9.0F, 5.0F), 
/*  67 */         PartPose.offsetAndRotation(-4.0F, 15.0F, -1.0F, -0.05235988F, 0.0F, 0.0F));
/*  70 */     return LayerDefinition.create(mesh, 48, 32);
/*     */   }
/*     */   
/*     */   protected void setSittingPose(FoxRenderState state) {
/*  75 */     super.setSittingPose(state);
/*  76 */     this.body.xRot = 0.5235988F;
/*  77 */     this.body.y -= 7.0F;
/*  78 */     this.body.z += 3.0F;
/*  79 */     this.tail.xRot = 0.7853982F;
/*  81 */     this.head.y -= 6.5F;
/*  82 */     this.head.z += 2.75F;
/*  84 */     this.rightFrontLeg.xRot = -0.2617994F;
/*  85 */     this.leftFrontLeg.xRot = -0.2617994F;
/*  87 */     this.rightHindLeg.xRot = -1.3089969F;
/*  88 */     this.rightHindLeg.y += 4.0F;
/*  89 */     this.rightHindLeg.z -= 0.25F;
/*  91 */     this.leftHindLeg.xRot = -1.3089969F;
/*  92 */     this.leftHindLeg.y += 4.0F;
/*  93 */     this.leftHindLeg.z -= 0.25F;
/*  94 */     this.tail.z--;
/*     */   }
/*     */   
/*     */   protected void setSleepingPose(FoxRenderState state) {
/*  99 */     super.setSleepingPose(state);
/* 100 */     this.body.zRot = -1.5707964F;
/* 101 */     this.body.y += 5.0F;
/* 102 */     this.tail.xRot = -2.6179938F;
/* 103 */     this.head.x += 2.0F;
/* 104 */     this.head.y += 2.99F;
/* 105 */     this.head.yRot = -2.0943952F;
/* 106 */     this.head.zRot = 0.0F;
/*     */   }
/*     */   
/*     */   protected void setWalkingPose(FoxRenderState state) {
/* 111 */     super.setWalkingPose(state);
/* 112 */     float animationSpeed = state.walkAnimationSpeed;
/* 113 */     float animationPos = state.walkAnimationPos;
/* 114 */     this.rightHindLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/* 115 */     this.leftHindLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 116 */     this.rightFrontLeg.xRot = Mth.cos((animationPos * 0.6662F + 3.1415927F)) * 1.4F * animationSpeed;
/* 117 */     this.leftFrontLeg.xRot = Mth.cos((animationPos * 0.6662F)) * 1.4F * animationSpeed;
/*     */   }
/*     */   
/*     */   protected void setCrouchingPose(FoxRenderState state) {
/* 122 */     super.setCrouchingPose(state);
/* 123 */     this.body.y += state.crouchAmount;
/*     */   }
/*     */   
/*     */   protected void setPouncingPose(FoxRenderState state) {
/* 128 */     super.setPouncingPose(state);
/* 129 */     float crouch = state.crouchAmount / 2.0F;
/* 130 */     this.body.y -= crouch;
/* 131 */     this.head.y -= crouch;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\fox\AdultFoxModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */