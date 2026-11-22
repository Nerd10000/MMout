/*    */ package net.minecraft.client.model.animal.bee;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class AdultBeeModel extends BeeModel {
/*    */   private static final String LEFT_ANTENNA = "left_antenna";
/*    */   
/*    */   private static final String RIGHT_ANTENNA = "right_antenna";
/*    */   
/*    */   private final ModelPart leftAntenna;
/*    */   
/*    */   private final ModelPart rightAntenna;
/*    */   
/*    */   public AdultBeeModel(ModelPart root) {
/* 22 */     super(root);
/* 24 */     ModelPart body = this.bone.getChild("body");
/* 25 */     this.leftAntenna = body.getChild("left_antenna");
/* 26 */     this.rightAntenna = body.getChild("right_antenna");
/*    */   }
/*    */   
/*    */   protected void bobUpAndDown(float speed, float ageInTicks) {
/* 31 */     super.bobUpAndDown(speed, ageInTicks);
/* 32 */     this.leftAntenna.xRot = speed * 3.1415927F * 0.03F;
/* 33 */     this.rightAntenna.xRot = speed * 3.1415927F * 0.03F;
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 37 */     MeshDefinition mesh = new MeshDefinition();
/* 38 */     PartDefinition root = mesh.getRoot();
/* 40 */     PartDefinition bone = root.addOrReplaceChild("bone", 
/* 41 */         CubeListBuilder.create(), 
/* 42 */         PartPose.offset(0.0F, 19.0F, 0.0F));
/* 44 */     PartDefinition body = bone.addOrReplaceChild("body", 
/* 45 */         CubeListBuilder.create()
/* 46 */         .texOffs(0, 0).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F), PartPose.ZERO);
/* 48 */     body.addOrReplaceChild("stinger", 
/* 49 */         CubeListBuilder.create()
/* 50 */         .texOffs(26, 7).addBox(0.0F, -1.0F, 5.0F, 0.0F, 1.0F, 2.0F), PartPose.ZERO);
/* 53 */     body.addOrReplaceChild("left_antenna", 
/* 54 */         CubeListBuilder.create()
/* 55 */         .texOffs(2, 0).addBox(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), 
/* 56 */         PartPose.offset(0.0F, -2.0F, -5.0F));
/* 58 */     body.addOrReplaceChild("right_antenna", 
/* 59 */         CubeListBuilder.create()
/* 60 */         .texOffs(2, 3).addBox(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), 
/* 61 */         PartPose.offset(0.0F, -2.0F, -5.0F));
/* 64 */     CubeDeformation wingDeformation = new CubeDeformation(0.001F);
/* 65 */     bone.addOrReplaceChild("right_wing", 
/* 66 */         CubeListBuilder.create()
/* 67 */         .texOffs(0, 18).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, wingDeformation), 
/* 68 */         PartPose.offsetAndRotation(-1.5F, -4.0F, -3.0F, 0.0F, -0.2618F, 0.0F));
/* 70 */     bone.addOrReplaceChild("left_wing", 
/* 71 */         CubeListBuilder.create()
/* 72 */         .texOffs(0, 18).mirror().addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, wingDeformation), 
/* 73 */         PartPose.offsetAndRotation(1.5F, -4.0F, -3.0F, 0.0F, 0.2618F, 0.0F));
/* 75 */     bone.addOrReplaceChild("front_legs", 
/* 76 */         CubeListBuilder.create()
/* 77 */         .addBox("front_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 1), 
/* 78 */         PartPose.offset(1.5F, 3.0F, -2.0F));
/* 80 */     bone.addOrReplaceChild("middle_legs", 
/* 81 */         CubeListBuilder.create()
/* 82 */         .addBox("middle_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 3), 
/* 83 */         PartPose.offset(1.5F, 3.0F, 0.0F));
/* 85 */     bone.addOrReplaceChild("back_legs", 
/* 86 */         CubeListBuilder.create()
/* 87 */         .addBox("back_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 5), 
/* 88 */         PartPose.offset(1.5F, 3.0F, 2.0F));
/* 91 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\bee\AdultBeeModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */