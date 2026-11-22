/*     */ package net.minecraft.client.model.animal.rabbit;
/*     */ 
/*     */ import net.minecraft.client.animation.definitions.BabyRabbitAnimation;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ 
/*     */ public class BabyRabbitModel extends RabbitModel {
/*     */   public BabyRabbitModel(ModelPart root) {
/*  15 */     super(root, BabyRabbitAnimation.HOP, BabyRabbitAnimation.IDLE_HEAD_TILT);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  23 */     MeshDefinition mesh = new MeshDefinition();
/*  24 */     PartDefinition root = mesh.getRoot();
/*  26 */     PartDefinition body = root.addOrReplaceChild("body", 
/*  27 */         CubeListBuilder.create(), 
/*  28 */         PartPose.offset(0.0F, 23.0F, 1.6F));
/*  30 */     body.addOrReplaceChild("body_r1", 
/*  31 */         CubeListBuilder.create()
/*  32 */         .texOffs(0, 8).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 3.0F, 6.0F), 
/*  33 */         PartPose.offsetAndRotation(0.0F, -2.0F, -1.6F, -0.5236F, 0.0F, 0.0F));
/*  35 */     PartDefinition tail = body.addOrReplaceChild("tail", 
/*  36 */         CubeListBuilder.create(), 
/*  37 */         PartPose.offset(0.0F, -2.2F, 2.0F));
/*  39 */     tail.addOrReplaceChild("tail_r1", 
/*  40 */         CubeListBuilder.create()
/*  41 */         .texOffs(0, 21).addBox(-1.4F, -2.0268F, -1.0177F, 3.0F, 3.0F, 3.0F), 
/*  42 */         PartPose.offsetAndRotation(-0.1F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0F));
/*  44 */     PartDefinition head = body.addOrReplaceChild("head", 
/*  45 */         CubeListBuilder.create()
/*  46 */         .texOffs(0, 0).addBox(-2.5F, -3.0F, -3.0F, 5.0F, 4.0F, 4.0F), 
/*  47 */         PartPose.offset(0.0F, -5.0F, -2.6F));
/*  49 */     head.addOrReplaceChild("right_ear", 
/*  50 */         CubeListBuilder.create()
/*  51 */         .texOffs(18, 0).addBox(-1.0F, -3.5F, -0.5F, 2.0F, 4.0F, 1.0F), 
/*  52 */         PartPose.offset(-1.5F, -3.5F, -0.5F));
/*  54 */     head.addOrReplaceChild("left_ear", 
/*  55 */         CubeListBuilder.create()
/*  56 */         .texOffs(24, 0).addBox(-1.0F, -3.5F, -0.5F, 2.0F, 4.0F, 1.0F), 
/*  57 */         PartPose.offset(1.5F, -3.5F, -0.5F));
/*  59 */     PartDefinition frontLegs = body.addOrReplaceChild("frontlegs", 
/*  60 */         CubeListBuilder.create(), 
/*  61 */         PartPose.offset(0.0F, -2.5F, -2.6F));
/*  63 */     PartDefinition leftFrontLeg = frontLegs.addOrReplaceChild("left_front_leg", 
/*  64 */         CubeListBuilder.create(), 
/*  65 */         PartPose.offsetAndRotation(1.0F, 1.0F, -0.5F, 0.3927F, 0.0F, 0.0F));
/*  67 */     leftFrontLeg.addOrReplaceChild("left_front_leg_r1", 
/*  68 */         CubeListBuilder.create()
/*  69 */         .texOffs(18, 8).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F), 
/*  70 */         PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
/*  72 */     PartDefinition rightFrontLeg = frontLegs.addOrReplaceChild("right_front_leg", 
/*  73 */         CubeListBuilder.create(), 
/*  74 */         PartPose.offsetAndRotation(-1.0F, 1.0F, -0.5F, 0.3927F, 0.0F, 0.0F));
/*  76 */     rightFrontLeg.addOrReplaceChild("right_front_leg_r1", 
/*  77 */         CubeListBuilder.create()
/*  78 */         .texOffs(14, 8).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F), 
/*  79 */         PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -0.3927F, 0.0F, 0.0F));
/*  81 */     PartDefinition backLegs = root.addOrReplaceChild("backlegs", 
/*  82 */         CubeListBuilder.create(), 
/*  83 */         PartPose.offset(0.0F, 23.0F, 2.0F));
/*  85 */     PartDefinition leftBackLeg = backLegs.addOrReplaceChild("left_hind_leg", 
/*  86 */         CubeListBuilder.create(), 
/*  87 */         PartPose.offsetAndRotation(1.5F, 0.5F, 0.5F, 0.0F, 3.1416F, 0.0F));
/*  89 */     leftBackLeg.addOrReplaceChild("left_haunch", 
/*  90 */         CubeListBuilder.create()
/*  91 */         .texOffs(10, 17).addBox(-2.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F), 
/*  92 */         PartPose.offsetAndRotation(1.0F, 0.0F, 0.5F, 0.0F, -0.7854F, 0.0F));
/*  94 */     PartDefinition rightBackLeg = backLegs.addOrReplaceChild("right_hind_leg", 
/*  95 */         CubeListBuilder.create(), 
/*  96 */         PartPose.offsetAndRotation(-1.5F, 0.5F, 0.5F, 0.0F, 3.1416F, 0.0F));
/*  98 */     rightBackLeg.addOrReplaceChild("right_haunch", 
/*  99 */         CubeListBuilder.create()
/* 100 */         .texOffs(0, 17).addBox(-2.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F), 
/* 101 */         PartPose.offsetAndRotation(0.5F, 0.0F, -0.9F, 0.0F, 0.7854F, 0.0F));
/* 104 */     return LayerDefinition.create(mesh, 32, 32);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\rabbit\BabyRabbitModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */