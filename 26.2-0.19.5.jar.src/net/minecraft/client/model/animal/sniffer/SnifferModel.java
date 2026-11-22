/*     */ package net.minecraft.client.model.animal.sniffer;
/*     */ 
/*     */ import net.minecraft.client.animation.KeyframeAnimation;
/*     */ import net.minecraft.client.animation.definitions.SnifferAnimation;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.SnifferRenderState;
/*     */ 
/*     */ public class SnifferModel extends EntityModel<SnifferRenderState> {
/*     */   private static final float WALK_ANIMATION_SPEED_MAX = 9.0F;
/*     */   
/*     */   private static final float WALK_ANIMATION_SCALE_FACTOR = 100.0F;
/*     */   
/*     */   private final ModelPart head;
/*     */   
/*     */   private final KeyframeAnimation sniffSearchAnimation;
/*     */   
/*     */   private final KeyframeAnimation walkAnimation;
/*     */   
/*     */   private final KeyframeAnimation digAnimation;
/*     */   
/*     */   private final KeyframeAnimation longSniffAnimation;
/*     */   
/*     */   private final KeyframeAnimation standUpAnimation;
/*     */   
/*     */   private final KeyframeAnimation happyAnimation;
/*     */   
/*     */   private final KeyframeAnimation sniffSniffAnimation;
/*     */   
/*     */   public SnifferModel(ModelPart root) {
/*  32 */     super(root);
/*  33 */     this
/*     */       
/*  36 */       .head = root.getChild("bone").getChild("body").getChild("head");
/*  38 */     this.sniffSearchAnimation = SnifferAnimation.SNIFFER_SNIFF_SEARCH.bake(root);
/*  39 */     this.walkAnimation = SnifferAnimation.SNIFFER_WALK.bake(root);
/*  40 */     this.digAnimation = SnifferAnimation.SNIFFER_DIG.bake(root);
/*  41 */     this.longSniffAnimation = SnifferAnimation.SNIFFER_LONGSNIFF.bake(root);
/*  42 */     this.standUpAnimation = SnifferAnimation.SNIFFER_STAND_UP.bake(root);
/*  43 */     this.happyAnimation = SnifferAnimation.SNIFFER_HAPPY.bake(root);
/*  44 */     this.sniffSniffAnimation = SnifferAnimation.SNIFFER_SNIFFSNIFF.bake(root);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  48 */     MeshDefinition mesh = new MeshDefinition();
/*  49 */     PartDefinition root = mesh.getRoot();
/*  51 */     PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), 
/*  52 */         PartPose.offset(0.0F, 5.0F, 0.0F));
/*  54 */     PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create()
/*  55 */         .texOffs(62, 68).addBox(-12.5F, -14.0F, -20.0F, 25.0F, 29.0F, 40.0F, new CubeDeformation(0.0F))
/*  56 */         .texOffs(62, 0).addBox(-12.5F, -14.0F, -20.0F, 25.0F, 24.0F, 40.0F, new CubeDeformation(0.5F))
/*  57 */         .texOffs(87, 68).addBox(-12.5F, 12.0F, -20.0F, 25.0F, 0.0F, 40.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
/*  59 */     bone.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
/*  60 */         .texOffs(32, 87).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, 10.0F, -15.0F));
/*  61 */     bone.addOrReplaceChild("right_mid_leg", CubeListBuilder.create()
/*  62 */         .texOffs(32, 105).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, 10.0F, 0.0F));
/*  63 */     bone.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
/*  64 */         .texOffs(32, 123).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, 10.0F, 15.0F));
/*  65 */     bone.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
/*  66 */         .texOffs(0, 87).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, 10.0F, -15.0F));
/*  67 */     bone.addOrReplaceChild("left_mid_leg", CubeListBuilder.create()
/*  68 */         .texOffs(0, 105).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, 10.0F, 0.0F));
/*  69 */     bone.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
/*  70 */         .texOffs(0, 123).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, 10.0F, 15.0F));
/*  72 */     PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create()
/*  73 */         .texOffs(8, 15).addBox(-6.5F, -7.5F, -11.5F, 13.0F, 18.0F, 11.0F, new CubeDeformation(0.0F))
/*  74 */         .texOffs(8, 4).addBox(-6.5F, 7.5F, -11.5F, 13.0F, 0.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.5F, -19.48F));
/*  76 */     head.addOrReplaceChild("left_ear", CubeListBuilder.create()
/*  77 */         .texOffs(2, 0).addBox(0.0F, 0.0F, -3.0F, 1.0F, 19.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(6.51F, -7.5F, -4.51F));
/*  78 */     head.addOrReplaceChild("right_ear", CubeListBuilder.create()
/*  79 */         .texOffs(48, 0).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 19.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.51F, -7.5F, -4.51F));
/*  80 */     head.addOrReplaceChild("nose", CubeListBuilder.create()
/*  81 */         .texOffs(10, 45).addBox(-6.5F, -2.0F, -9.0F, 13.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.5F, -11.5F));
/*  82 */     head.addOrReplaceChild("lower_beak", CubeListBuilder.create()
/*  83 */         .texOffs(10, 57).addBox(-6.5F, -7.0F, -8.0F, 13.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, -12.5F));
/*  85 */     return LayerDefinition.create(mesh, 192, 192);
/*     */   }
/*     */   
/*     */   public void setupAnim(SnifferRenderState state) {
/*  90 */     super.setupAnim(state);
/*  92 */     this.head.xRot = state.xRot * 0.017453292F;
/*  93 */     this.head.yRot = state.yRot * 0.017453292F;
/*  95 */     if (state.isSearching) {
/*  96 */       this.sniffSearchAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 9.0F, 100.0F);
/*     */     } else {
/*  98 */       this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 9.0F, 100.0F);
/*     */     } 
/* 101 */     this.digAnimation.apply(state.diggingAnimationState, state.ageInTicks);
/* 102 */     this.longSniffAnimation.apply(state.sniffingAnimationState, state.ageInTicks);
/* 103 */     this.standUpAnimation.apply(state.risingAnimationState, state.ageInTicks);
/* 104 */     this.happyAnimation.apply(state.feelingHappyAnimationState, state.ageInTicks);
/* 105 */     this.sniffSniffAnimation.apply(state.scentingAnimationState, state.ageInTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\sniffer\SnifferModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */