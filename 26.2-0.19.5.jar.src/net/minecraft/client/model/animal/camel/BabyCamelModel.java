/*    */ package net.minecraft.client.model.animal.camel;
/*    */ 
/*    */ import net.minecraft.client.animation.definitions.CamelBabyAnimation;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class BabyCamelModel extends CamelModel {
/*    */   public BabyCamelModel(ModelPart root) {
/* 15 */     super(root, CamelBabyAnimation.CAMEL_BABY_WALK, CamelBabyAnimation.CAMEL_BABY_SIT, CamelBabyAnimation.CAMEL_BABY_SIT_POSE, CamelBabyAnimation.CAMEL_BABY_STANDUP, CamelBabyAnimation.CAMEL_BABY_IDLE, CamelBabyAnimation.CAMEL_BABY_DASH);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 27 */     MeshDefinition mesh = new MeshDefinition();
/* 28 */     PartDefinition root = mesh.getRoot();
/* 30 */     PartDefinition body = root.addOrReplaceChild("body", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(0, 14).addBox(-4.5F, -4.0F, -8.0F, 9.0F, 8.0F, 16.0F), 
/* 33 */         PartPose.offset(0.0F, 7.0F, 0.0F));
/* 35 */     body.addOrReplaceChild("tail", 
/* 36 */         CubeListBuilder.create()
/* 37 */         .texOffs(50, 38).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 9.0F, 0.0F), 
/* 38 */         PartPose.offset(0.0F, -1.5F, 8.05F));
/* 40 */     PartDefinition head = body.addOrReplaceChild("head", 
/* 41 */         CubeListBuilder.create()
/* 42 */         .texOffs(20, 0).addBox(-2.5F, -3.0F, -7.5F, 5.0F, 5.0F, 7.0F)
/* 43 */         .texOffs(0, 0).addBox(-2.5F, -12.0F, -7.5F, 5.0F, 9.0F, 5.0F)
/* 44 */         .texOffs(0, 14).addBox(-2.5F, -12.0F, -10.5F, 5.0F, 4.0F, 3.0F), 
/* 45 */         PartPose.offset(0.0F, 1.0F, -7.5F));
/* 47 */     head.addOrReplaceChild("right_ear", 
/* 48 */         CubeListBuilder.create()
/* 49 */         .texOffs(37, 0).addBox(-3.0F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F), 
/* 50 */         PartPose.offset(-2.5F, -11.0F, -4.0F));
/* 52 */     head.addOrReplaceChild("left_ear", 
/* 53 */         CubeListBuilder.create()
/* 54 */         .texOffs(47, 0).addBox(0.0F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F), 
/* 55 */         PartPose.offset(2.5F, -11.0F, -4.0F));
/* 57 */     root.addOrReplaceChild("right_front_leg", 
/* 58 */         CubeListBuilder.create()
/* 59 */         .texOffs(36, 14).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 13.0F, 3.0F), 
/* 60 */         PartPose.offset(-3.0F, 11.5F, -5.5F));
/* 62 */     root.addOrReplaceChild("left_front_leg", 
/* 63 */         CubeListBuilder.create()
/* 64 */         .texOffs(48, 14).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 13.0F, 3.0F), 
/* 65 */         PartPose.offset(3.0F, 11.5F, -5.5F));
/* 67 */     root.addOrReplaceChild("left_hind_leg", 
/* 68 */         CubeListBuilder.create()
/* 69 */         .texOffs(12, 38).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 13.0F, 3.0F), 
/* 70 */         PartPose.offset(3.0F, 11.5F, 5.5F));
/* 72 */     root.addOrReplaceChild("right_hind_leg", 
/* 73 */         CubeListBuilder.create()
/* 74 */         .texOffs(0, 38).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 13.0F, 3.0F), 
/* 75 */         PartPose.offset(-3.0F, 11.5F, 5.5F));
/* 78 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\camel\BabyCamelModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */