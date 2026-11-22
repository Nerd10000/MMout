/*     */ package net.minecraft.client.model.animal.fox;
/*     */ 
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.renderer.entity.state.FoxRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public abstract class FoxModel extends EntityModel<FoxRenderState> {
/*     */   public final ModelPart head;
/*     */   
/*     */   protected final ModelPart body;
/*     */   
/*     */   protected final ModelPart rightHindLeg;
/*     */   
/*     */   protected final ModelPart leftHindLeg;
/*     */   
/*     */   protected final ModelPart rightFrontLeg;
/*     */   
/*     */   protected final ModelPart leftFrontLeg;
/*     */   
/*     */   protected final ModelPart tail;
/*     */   
/*     */   private float legMotionPos;
/*     */   
/*     */   public FoxModel(ModelPart root) {
/*  21 */     super(root);
/*  22 */     this.head = root.getChild("head");
/*  23 */     this.body = root.getChild("body");
/*  24 */     this.rightHindLeg = root.getChild("right_hind_leg");
/*  25 */     this.leftHindLeg = root.getChild("left_hind_leg");
/*  26 */     this.rightFrontLeg = root.getChild("right_front_leg");
/*  27 */     this.leftFrontLeg = root.getChild("left_front_leg");
/*  28 */     this.tail = this.body.getChild("tail");
/*     */   }
/*     */   
/*     */   public void setupAnim(FoxRenderState state) {
/*  33 */     super.setupAnim(state);
/*  34 */     setWalkingPose(state);
/*  36 */     if (state.isCrouching) {
/*  37 */       setCrouchingPose(state);
/*  39 */     } else if (state.isSleeping) {
/*  40 */       setSleepingPose(state);
/*  41 */     } else if (state.isSitting) {
/*  42 */       setSittingPose(state);
/*     */     } 
/*  45 */     if (state.isPouncing)
/*  46 */       setPouncingPose(state); 
/*  49 */     if (!state.isSleeping && !state.isFaceplanted && !state.isCrouching) {
/*  50 */       this.head.xRot = state.xRot * 0.017453292F;
/*  51 */       this.head.yRot = state.yRot * 0.017453292F;
/*     */     } 
/*  55 */     if (state.isSleeping) {
/*  56 */       this.head.xRot = 0.0F;
/*  57 */       this.head.yRot = -2.0943952F;
/*  58 */       this.head.zRot = Mth.cos((state.ageInTicks * 0.027F)) / 22.0F;
/*     */     } 
/*  62 */     if (state.isFaceplanted) {
/*  63 */       float legMoveFactor = 0.1F;
/*  64 */       this.legMotionPos += 0.67F;
/*  65 */       this.rightHindLeg.xRot = Mth.cos((this.legMotionPos * 0.4662F)) * 0.1F;
/*  66 */       this.leftHindLeg.xRot = Mth.cos((this.legMotionPos * 0.4662F + 3.1415927F)) * 0.1F;
/*  67 */       this.rightFrontLeg.xRot = Mth.cos((this.legMotionPos * 0.4662F + 3.1415927F)) * 0.1F;
/*  68 */       this.leftFrontLeg.xRot = Mth.cos((this.legMotionPos * 0.4662F)) * 0.1F;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setSittingPose(FoxRenderState state) {
/*  73 */     this.head.xRot = 0.0F;
/*  74 */     this.head.yRot = 0.0F;
/*     */   }
/*     */   
/*     */   protected void setSleepingPose(FoxRenderState state) {
/*  78 */     this.rightHindLeg.visible = false;
/*  79 */     this.leftHindLeg.visible = false;
/*  80 */     this.rightFrontLeg.visible = false;
/*  81 */     this.leftFrontLeg.visible = false;
/*     */   }
/*     */   
/*     */   protected void setWalkingPose(FoxRenderState state) {
/*  85 */     this.head.zRot = state.headRollAngle;
/*  86 */     this.rightHindLeg.visible = true;
/*  87 */     this.leftHindLeg.visible = true;
/*  88 */     this.rightFrontLeg.visible = true;
/*  89 */     this.leftFrontLeg.visible = true;
/*     */   }
/*     */   
/*     */   protected void setCrouchingPose(FoxRenderState state) {
/*  93 */     this.body.xRot += 0.10471976F;
/*  94 */     this.head.y += state.crouchAmount * state.ageScale;
/*  97 */     float wiggleAmount = Mth.cos(state.ageInTicks) * 0.05F;
/*  98 */     this.body.yRot = wiggleAmount;
/*  99 */     this.rightHindLeg.zRot = wiggleAmount;
/* 100 */     this.leftHindLeg.zRot = wiggleAmount;
/* 101 */     this.rightFrontLeg.zRot = wiggleAmount / 2.0F;
/* 102 */     this.leftFrontLeg.zRot = wiggleAmount / 2.0F;
/*     */   }
/*     */   
/*     */   protected void setPouncingPose(FoxRenderState state) {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\fox\FoxModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */