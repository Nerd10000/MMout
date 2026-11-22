/*     */ package net.minecraft.client.model.animal.sniffer;
/*     */ 
/*     */ import net.minecraft.client.animation.KeyframeAnimation;
/*     */ import net.minecraft.client.animation.definitions.SnifferAnimation;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.entity.state.SnifferRenderState;
/*     */ 
/*     */ public class SniffletModel extends SnifferModel {
/*     */   private static final String LOWER_BEAK = "lower_beak";
/*     */   
/*     */   private final KeyframeAnimation babyTransform;
/*     */   
/*     */   public SniffletModel(ModelPart root) {
/*  22 */     super(root);
/*  23 */     this.babyTransform = SnifferAnimation.BABY_TRANSFORM.bake(root);
/*     */   }
/*     */   
/*     */   public void setupAnim(SnifferRenderState state) {
/*  28 */     super.setupAnim(state);
/*  29 */     this.babyTransform.applyStatic();
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  33 */     MeshDefinition mesh = new MeshDefinition();
/*  34 */     PartDefinition root = mesh.getRoot();
/*  36 */     PartDefinition bone = root.addOrReplaceChild("bone", 
/*  37 */         CubeListBuilder.create(), 
/*  38 */         PartPose.offset(0.0F, 24.0F, 0.0F));
/*  40 */     PartDefinition body = bone.addOrReplaceChild("body", 
/*  41 */         CubeListBuilder.create()
/*  42 */         .texOffs(0, 35).addBox(-13.0F, -14.0F, -0.5F, 14.0F, 14.0F, 20.0F, new CubeDeformation(0.25F))
/*  43 */         .texOffs(0, 0).addBox(-13.0F, -14.0F, -0.5F, 14.0F, 15.0F, 20.0F)
/*  44 */         .texOffs(68, 0).addBox(-13.0F, 0.0F, -0.5F, 14.0F, 0.0F, 20.0F), 
/*  45 */         PartPose.offset(6.0F, -3.0F, -9.5F));
/*  47 */     PartDefinition head = body.addOrReplaceChild("head", 
/*  48 */         CubeListBuilder.create()
/*  49 */         .texOffs(68, 20).addBox(-5.0F, -4.25F, -7.5F, 10.0F, 9.0F, 9.0F)
/*  50 */         .texOffs(88, 20).addBox(-5.0F, 3.75F, -7.5F, 10.0F, 0.0F, 9.0F), 
/*  51 */         PartPose.offset(-6.0F, -4.75F, 0.0F));
/*  53 */     head.addOrReplaceChild("left_ear", 
/*  54 */         CubeListBuilder.create()
/*  55 */         .texOffs(104, 38).addBox(0.0F, 0.0F, -2.0F, 1.0F, 11.0F, 3.0F), 
/*  56 */         PartPose.offset(5.0F, -4.25F, -1.5F));
/*  58 */     head.addOrReplaceChild("right_ear", 
/*  59 */         CubeListBuilder.create()
/*  60 */         .texOffs(96, 38).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 11.0F, 3.0F), 
/*  61 */         PartPose.offset(-5.0F, -4.25F, -1.5F));
/*  63 */     head.addOrReplaceChild("nose", 
/*  64 */         CubeListBuilder.create()
/*  65 */         .texOffs(68, 47).addBox(-5.0F, -3.0F, -2.0F, 10.0F, 3.0F, 4.0F), 
/*  66 */         PartPose.offset(0.0F, -1.25F, -9.5F));
/*  68 */     head.addOrReplaceChild("lower_beak", 
/*  69 */         CubeListBuilder.create()
/*  70 */         .texOffs(68, 38).addBox(-5.0F, -2.5F, -2.0F, 10.0F, 5.0F, 4.0F), 
/*  71 */         PartPose.offset(0.0F, 1.25F, -9.5F));
/*  73 */     bone.addOrReplaceChild("right_front_leg", 
/*  74 */         CubeListBuilder.create()
/*  75 */         .texOffs(0, 69).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F), 
/*  76 */         PartPose.offset(-4.0F, -4.0F, -7.0F));
/*  78 */     bone.addOrReplaceChild("right_mid_leg", 
/*  79 */         CubeListBuilder.create()
/*  80 */         .texOffs(0, 78).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F), 
/*  81 */         PartPose.offset(-4.0F, -4.0F, 0.0F));
/*  83 */     bone.addOrReplaceChild("right_hind_leg", 
/*  84 */         CubeListBuilder.create()
/*  85 */         .texOffs(0, 87).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F), 
/*  86 */         PartPose.offset(-4.0F, -4.0F, 7.0F));
/*  88 */     bone.addOrReplaceChild("left_front_leg", 
/*  89 */         CubeListBuilder.create()
/*  90 */         .texOffs(16, 69).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F), 
/*  91 */         PartPose.offset(4.0F, -4.0F, -7.0F));
/*  93 */     bone.addOrReplaceChild("left_mid_leg", 
/*  94 */         CubeListBuilder.create()
/*  95 */         .texOffs(16, 78).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F), 
/*  96 */         PartPose.offset(4.0F, -4.0F, 0.0F));
/*  98 */     bone.addOrReplaceChild("left_hind_leg", 
/*  99 */         CubeListBuilder.create()
/* 100 */         .texOffs(16, 87).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F), 
/* 101 */         PartPose.offset(4.0F, -4.0F, 7.0F));
/* 104 */     return LayerDefinition.create(mesh, 128, 128);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\sniffer\SniffletModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */