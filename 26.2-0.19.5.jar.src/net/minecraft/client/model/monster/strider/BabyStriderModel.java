/*    */ package net.minecraft.client.model.monster.strider;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class BabyStriderModel extends StriderModel {
/*    */   private static final String FRONT_BRISTLE = "bristle2";
/*    */   
/*    */   private static final String MIDDLE_BRISTLE = "bristle1";
/*    */   
/*    */   private static final String BACK_BRISTLE = "bristle0";
/*    */   
/*    */   private final ModelPart frontBristle;
/*    */   
/*    */   private final ModelPart middleBristle;
/*    */   
/*    */   private final ModelPart bottomBristle;
/*    */   
/*    */   public BabyStriderModel(ModelPart root) {
/* 24 */     super(root);
/* 25 */     this.frontBristle = this.body.getChild("bristle2");
/* 26 */     this.middleBristle = this.body.getChild("bristle1");
/* 27 */     this.bottomBristle = this.body.getChild("bristle0");
/*    */   }
/*    */   
/*    */   protected void customAnimations(float animationPos, float animationSpeed, float ageInTicks) {
/* 32 */     this.body.y = 17.25F;
/* 33 */     this.body.y -= 1.0F * Mth.cos((animationPos * 1.5F)) * 2.0F * animationSpeed;
/* 35 */     this.leftLeg.y = 20.0F + 2.0F * Mth.sin((animationPos * 1.5F * 0.5F + 3.1415927F)) * 2.0F * animationSpeed;
/* 36 */     this.rightLeg.y = 20.0F + 2.0F * Mth.sin((animationPos * 1.5F * 0.5F)) * 2.0F * animationSpeed;
/* 38 */     float bristleFlow = Mth.cos((animationPos * 1.5F + 3.1415927F)) * animationSpeed;
/* 39 */     animateBristle(ageInTicks, bristleFlow, this.frontBristle, this.middleBristle, this.bottomBristle, (modelPart, rotation) -> modelPart.xRot += rotation.floatValue());
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 43 */     MeshDefinition mesh = new MeshDefinition();
/* 44 */     PartDefinition root = mesh.getRoot();
/* 45 */     PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -3.75F, -4.0F, 7.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.75F, 0.0F));
/* 46 */     root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 20.0F, 0.0F));
/* 47 */     root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(8, 24).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 20.0F, 0.0F));
/* 48 */     body.addOrReplaceChild("bristle0", CubeListBuilder.create().texOffs(0, 21).addBox(-3.5F, -2.5F, 0.0F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.25F, 2.0F));
/* 49 */     body.addOrReplaceChild("bristle1", CubeListBuilder.create().texOffs(0, 18).addBox(-3.5F, -2.5F, 0.0F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.25F, 0.0F));
/* 50 */     body.addOrReplaceChild("bristle2", CubeListBuilder.create().texOffs(0, 15).addBox(-3.5F, -2.5F, 0.0F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.25F, -2.0F));
/* 51 */     return LayerDefinition.create(mesh, 32, 32);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\strider\BabyStriderModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */