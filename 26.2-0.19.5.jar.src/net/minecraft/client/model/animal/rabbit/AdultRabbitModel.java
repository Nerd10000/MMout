/*    */ package net.minecraft.client.model.animal.rabbit;
/*    */ 
/*    */ import net.minecraft.client.animation.definitions.RabbitAnimation;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class AdultRabbitModel extends RabbitModel {
/*    */   public AdultRabbitModel(ModelPart root) {
/* 14 */     super(root, RabbitAnimation.HOP, RabbitAnimation.IDLE_HEAD_TILT);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 22 */     MeshDefinition mesh = new MeshDefinition();
/* 23 */     PartDefinition root = mesh.getRoot();
/* 25 */     PartDefinition body = root.addOrReplaceChild("body", 
/* 26 */         CubeListBuilder.create()
/* 27 */         .texOffs(0, 0).addBox(-4.0F, -6.0F, -9.0F, 8.0F, 6.0F, 10.0F), 
/* 28 */         PartPose.offsetAndRotation(0.0F, 23.0F, 4.0F, -0.3927F, 0.0F, 0.0F));
/* 30 */     body.addOrReplaceChild("tail", 
/* 31 */         CubeListBuilder.create()
/* 32 */         .texOffs(20, 16).addBox(-2.0F, -3.0084F, -1.0125F, 4.0F, 4.0F, 4.0F), 
/* 33 */         PartPose.offset(0.0F, -4.9916F, 0.0125F));
/* 35 */     PartDefinition head = body.addOrReplaceChild("head", 
/* 36 */         CubeListBuilder.create()
/* 37 */         .texOffs(0, 16).addBox(-2.5F, -3.0F, -4.0F, 5.0F, 5.0F, 5.0F), 
/* 38 */         PartPose.offsetAndRotation(0.0F, -5.2929F, -8.1213F, 0.3927F, 0.0F, 0.0F));
/* 40 */     head.addOrReplaceChild("left_ear", 
/* 41 */         CubeListBuilder.create()
/* 42 */         .texOffs(32, 0).addBox(-1.0F, -4.2929F, -0.1213F, 2.0F, 5.0F, 1.0F), 
/* 43 */         PartPose.offset(1.5F, -3.7071F, -0.8787F));
/* 45 */     head.addOrReplaceChild("right_ear", 
/* 46 */         CubeListBuilder.create()
/* 47 */         .texOffs(26, 0).addBox(-1.0F, -4.2929F, -0.1213F, 2.0F, 5.0F, 1.0F), 
/* 48 */         PartPose.offset(-1.5F, -3.7071F, -0.8787F));
/* 50 */     PartDefinition frontLegs = body.addOrReplaceChild("frontlegs", 
/* 51 */         CubeListBuilder.create(), 
/* 52 */         PartPose.offset(0.0F, -1.5349F, -6.3108F));
/* 54 */     frontLegs.addOrReplaceChild("right_front_leg", 
/* 55 */         CubeListBuilder.create()
/* 56 */         .texOffs(36, 18).addBox(-0.9F, -1.0F, -0.9F, 2.0F, 4.0F, 2.0F), 
/* 57 */         PartPose.offsetAndRotation(-2.0F, 1.9239F, 0.3827F, 0.3927F, 0.0F, 0.0F));
/* 59 */     frontLegs.addOrReplaceChild("left_front_leg", 
/* 60 */         CubeListBuilder.create()
/* 61 */         .texOffs(44, 18).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 4.0F, 2.0F), 
/* 62 */         PartPose.offsetAndRotation(2.0F, 1.9239F, 0.4827F, 0.3927F, 0.0F, 0.0F));
/* 64 */     PartDefinition backLegs = root.addOrReplaceChild("backlegs", 
/* 65 */         CubeListBuilder.create(), 
/* 66 */         PartPose.offset(0.0F, 23.0F, 4.0F));
/* 68 */     PartDefinition rightBackLeg = backLegs.addOrReplaceChild("right_hind_leg", 
/* 69 */         CubeListBuilder.create(), 
/* 70 */         PartPose.offset(-3.0F, 0.5F, 0.0F));
/* 72 */     rightBackLeg.addOrReplaceChild("right_haunch", 
/* 73 */         CubeListBuilder.create()
/* 74 */         .texOffs(20, 24).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 1.0F, 6.0F), 
/* 75 */         PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, 0.0F, 0.3927F, 0.0F));
/* 77 */     PartDefinition leftBackLeg = backLegs.addOrReplaceChild("left_hind_leg", 
/* 78 */         CubeListBuilder.create(), 
/* 79 */         PartPose.offset(3.0F, 0.5F, 0.0F));
/* 81 */     leftBackLeg.addOrReplaceChild("left_haunch", 
/* 82 */         CubeListBuilder.create()
/* 83 */         .texOffs(36, 24).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 1.0F, 6.0F), 
/* 84 */         PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, 0.0F, -0.3927F, 0.0F));
/* 87 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\rabbit\AdultRabbitModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */