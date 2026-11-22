/*     */ package net.minecraft.client.model.monster.strider;
/*     */ 
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class AdultStriderModel extends StriderModel {
/*     */   private static final String RIGHT_BOTTOM_BRISTLE = "right_bottom_bristle";
/*     */   
/*     */   private static final String RIGHT_MIDDLE_BRISTLE = "right_middle_bristle";
/*     */   
/*     */   private static final String RIGHT_TOP_BRISTLE = "right_top_bristle";
/*     */   
/*     */   private static final String LEFT_TOP_BRISTLE = "left_top_bristle";
/*     */   
/*     */   private static final String LEFT_MIDDLE_BRISTLE = "left_middle_bristle";
/*     */   
/*     */   private static final String LEFT_BOTTOM_BRISTLE = "left_bottom_bristle";
/*     */   
/*     */   private final ModelPart rightBottomBristle;
/*     */   
/*     */   private final ModelPart rightMiddleBristle;
/*     */   
/*     */   private final ModelPart rightTopBristle;
/*     */   
/*     */   private final ModelPart leftTopBristle;
/*     */   
/*     */   private final ModelPart leftMiddleBristle;
/*     */   
/*     */   private final ModelPart leftBottomBristle;
/*     */   
/*     */   public AdultStriderModel(ModelPart root) {
/*  29 */     super(root);
/*  30 */     this.rightBottomBristle = this.body.getChild("right_bottom_bristle");
/*  31 */     this.rightMiddleBristle = this.body.getChild("right_middle_bristle");
/*  32 */     this.rightTopBristle = this.body.getChild("right_top_bristle");
/*  33 */     this.leftTopBristle = this.body.getChild("left_top_bristle");
/*  34 */     this.leftMiddleBristle = this.body.getChild("left_middle_bristle");
/*  35 */     this.leftBottomBristle = this.body.getChild("left_bottom_bristle");
/*     */   }
/*     */   
/*     */   protected void customAnimations(float animationPos, float animationSpeed, float ageInTicks) {
/*  40 */     this.rightBottomBristle.zRot = -1.2217305F;
/*  41 */     this.rightMiddleBristle.zRot = -1.134464F;
/*  42 */     this.rightTopBristle.zRot = -0.87266463F;
/*  43 */     this.leftTopBristle.zRot = 0.87266463F;
/*  44 */     this.leftMiddleBristle.zRot = 1.134464F;
/*  45 */     this.leftBottomBristle.zRot = 1.2217305F;
/*  47 */     float bristleFlow = Mth.cos((animationPos * 1.5F + 3.1415927F)) * animationSpeed;
/*  49 */     animateBristle(ageInTicks, bristleFlow, this.rightTopBristle, this.rightMiddleBristle, this.rightBottomBristle, (modelPart, rotation) -> modelPart.zRot += rotation.floatValue());
/*  50 */     animateBristle(ageInTicks, bristleFlow, this.leftTopBristle, this.leftMiddleBristle, this.leftBottomBristle, (modelPart, rotation) -> modelPart.zRot += rotation.floatValue());
/*  52 */     this.body.y = 2.0F;
/*  53 */     this.body.y -= 2.0F * Mth.cos((animationPos * 1.5F)) * 2.0F * animationSpeed;
/*  55 */     this.leftLeg.y = 8.0F + 2.0F * Mth.sin((animationPos * 1.5F * 0.5F + 3.1415927F)) * 2.0F * animationSpeed;
/*  56 */     this.rightLeg.y = 8.0F + 2.0F * Mth.sin((animationPos * 1.5F * 0.5F)) * 2.0F * animationSpeed;
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  60 */     MeshDefinition mesh = new MeshDefinition();
/*  61 */     PartDefinition root = mesh.getRoot();
/*  63 */     root.addOrReplaceChild("right_leg", 
/*  64 */         CubeListBuilder.create()
/*  65 */         .texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F), 
/*  66 */         PartPose.offset(-4.0F, 8.0F, 0.0F));
/*  68 */     root.addOrReplaceChild("left_leg", 
/*  69 */         CubeListBuilder.create()
/*  70 */         .texOffs(0, 55).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F), 
/*  71 */         PartPose.offset(4.0F, 8.0F, 0.0F));
/*  73 */     PartDefinition body = root.addOrReplaceChild("body", 
/*  74 */         CubeListBuilder.create()
/*  75 */         .texOffs(0, 0).addBox(-8.0F, -6.0F, -8.0F, 16.0F, 14.0F, 16.0F), 
/*  76 */         PartPose.offset(0.0F, 1.0F, 0.0F));
/*  79 */     body.addOrReplaceChild("right_bottom_bristle", 
/*  80 */         CubeListBuilder.create()
/*  81 */         .texOffs(16, 65).addBox(-12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F, true), 
/*  82 */         PartPose.offsetAndRotation(-8.0F, 4.0F, -8.0F, 0.0F, 0.0F, -1.2217305F));
/*  84 */     body.addOrReplaceChild("right_middle_bristle", 
/*  85 */         CubeListBuilder.create()
/*  86 */         .texOffs(16, 49).addBox(-12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F, true), 
/*  87 */         PartPose.offsetAndRotation(-8.0F, -1.0F, -8.0F, 0.0F, 0.0F, -1.134464F));
/*  89 */     body.addOrReplaceChild("right_top_bristle", 
/*  90 */         CubeListBuilder.create()
/*  91 */         .texOffs(16, 33).addBox(-12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F, true), 
/*  92 */         PartPose.offsetAndRotation(-8.0F, -5.0F, -8.0F, 0.0F, 0.0F, -0.87266463F));
/*  94 */     body.addOrReplaceChild("left_top_bristle", 
/*  95 */         CubeListBuilder.create()
/*  96 */         .texOffs(16, 33).addBox(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F), 
/*  97 */         PartPose.offsetAndRotation(8.0F, -6.0F, -8.0F, 0.0F, 0.0F, 0.87266463F));
/*  99 */     body.addOrReplaceChild("left_middle_bristle", 
/* 100 */         CubeListBuilder.create()
/* 101 */         .texOffs(16, 49).addBox(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F), 
/* 102 */         PartPose.offsetAndRotation(8.0F, -2.0F, -8.0F, 0.0F, 0.0F, 1.134464F));
/* 104 */     body.addOrReplaceChild("left_bottom_bristle", 
/* 105 */         CubeListBuilder.create()
/* 106 */         .texOffs(16, 65).addBox(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 16.0F), 
/* 107 */         PartPose.offsetAndRotation(8.0F, 3.0F, -8.0F, 0.0F, 0.0F, 1.2217305F));
/* 110 */     return LayerDefinition.create(mesh, 64, 128);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\strider\AdultStriderModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */