/*     */ package net.minecraft.client.model.animal.fox;
/*     */ 
/*     */ import net.minecraft.client.animation.KeyframeAnimation;
/*     */ import net.minecraft.client.animation.definitions.FoxBabyAnimation;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.FoxRenderState;
/*     */ 
/*     */ public class BabyFoxModel extends FoxModel {
/*     */   private static final float MAX_WALK_ANIMATION_SPEED = 1.0F;
/*     */   
/*     */   private static final float WALK_ANIMATION_SCALE_FACTOR = 2.5F;
/*     */   
/*     */   private final KeyframeAnimation babyWalkAnimation;
/*     */   
/*     */   public BabyFoxModel(ModelPart root) {
/*  23 */     super(root);
/*  24 */     this.babyWalkAnimation = FoxBabyAnimation.FOX_BABY_WALK.bake(root);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  28 */     MeshDefinition meshdefinition = new MeshDefinition();
/*  29 */     PartDefinition root = meshdefinition.getRoot();
/*  30 */     root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.125F, -5.125F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
/*  31 */         .texOffs(18, 20).addBox(-1.0F, 0.875F, -7.125F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
/*  32 */         .texOffs(22, 8).addBox(-3.0F, -4.125F, -4.125F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
/*  33 */         .texOffs(22, 11).addBox(1.0F, -4.125F, -4.125F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.125F, 0.125F));
/*  34 */     root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(22, 4).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 22.0F, 4.0F));
/*  35 */     root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 22.0F, 4.0F));
/*  36 */     root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(22, 4).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 22.0F, 0.0F));
/*  37 */     root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 22.0F, 0.0F));
/*  38 */     PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 2.0F));
/*  39 */     body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, -1.48F, -1.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 3.0F));
/*  41 */     return LayerDefinition.create(meshdefinition, 32, 32);
/*     */   }
/*     */   
/*     */   protected void setSittingPose(FoxRenderState state) {
/*  46 */     super.setSittingPose(state);
/*  47 */     this.body.xRot = -0.959931F;
/*  48 */     this.body.z -= 4.5F * state.ageScale;
/*  49 */     this.body.y += 3.0F * state.ageScale;
/*  50 */     this.tail.y -= 0.6F;
/*  51 */     this.tail.z -= 2.0F * state.ageScale;
/*  52 */     this.tail.xRot = 0.95993114F;
/*  54 */     this.head.y -= 0.75F;
/*  55 */     this.head.z += 0.0F;
/*  57 */     this.rightFrontLeg.xRot = -0.2617994F;
/*  58 */     this.leftFrontLeg.xRot = -0.2617994F;
/*  60 */     this.rightFrontLeg.z -= 1.5F;
/*  61 */     this.leftFrontLeg.z -= 1.5F;
/*  62 */     this.rightFrontLeg.x += 0.01F;
/*  63 */     this.leftFrontLeg.x -= 0.01F;
/*  65 */     this.rightHindLeg.z -= 3.75F;
/*  66 */     this.leftHindLeg.z -= 3.75F;
/*  67 */     this.rightHindLeg.x += 0.01F;
/*  68 */     this.leftHindLeg.x -= 0.01F;
/*     */   }
/*     */   
/*     */   protected void setSleepingPose(FoxRenderState state) {
/*  73 */     super.setSleepingPose(state);
/*  74 */     this.body.zRot = -1.5707964F;
/*  75 */     this.body.xRot = -0.17453292F;
/*  76 */     this.body.y += 1.5F;
/*  77 */     this.body.z -= 1.5F;
/*  78 */     this.body.x -= 1.5F;
/*  80 */     this.tail.xRot = -2.6179938F;
/*  81 */     this.tail.xRot = -2.1816616F;
/*  82 */     this.tail.x -= 0.7F;
/*  83 */     this.tail.z += 0.6F;
/*  84 */     this.tail.y += 0.9F;
/*  86 */     this.head.x -= 2.0F;
/*  87 */     this.head.y += 2.8F;
/*  88 */     this.head.z -= 4.0F;
/*  89 */     this.head.yRot = -2.0943952F;
/*  90 */     this.head.zRot = 0.0F;
/*     */   }
/*     */   
/*     */   protected void setWalkingPose(FoxRenderState state) {
/*  95 */     super.setWalkingPose(state);
/*  96 */     this.babyWalkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 1.0F, 2.5F);
/*     */   }
/*     */   
/*     */   protected void setCrouchingPose(FoxRenderState state) {
/* 101 */     super.setCrouchingPose(state);
/* 102 */     this.body.y += state.crouchAmount / 6.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\fox\BabyFoxModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */