/*    */ package net.minecraft.client.model.animal.camel;
/*    */ 
/*    */ import net.minecraft.client.animation.definitions.CamelAnimation;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class AdultCamelModel extends CamelModel {
/*    */   public AdultCamelModel(ModelPart root) {
/* 15 */     super(root, CamelAnimation.CAMEL_WALK, CamelAnimation.CAMEL_SIT, CamelAnimation.CAMEL_SIT_POSE, CamelAnimation.CAMEL_STANDUP, CamelAnimation.CAMEL_IDLE, CamelAnimation.CAMEL_DASH);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 27 */     return LayerDefinition.create(createBodyMesh(), 128, 128);
/*    */   }
/*    */   
/*    */   protected static MeshDefinition createBodyMesh() {
/* 31 */     MeshDefinition mesh = new MeshDefinition();
/* 32 */     PartDefinition root = mesh.getRoot();
/* 34 */     PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
/* 35 */         .texOffs(0, 25).addBox(-7.5F, -12.0F, -23.5F, 15.0F, 12.0F, 27.0F), 
/* 36 */         PartPose.offset(0.0F, 4.0F, 9.5F));
/* 38 */     body.addOrReplaceChild("hump", CubeListBuilder.create()
/* 39 */         .texOffs(74, 0).addBox(-4.5F, -5.0F, -5.5F, 9.0F, 5.0F, 11.0F), 
/* 40 */         PartPose.offset(0.0F, -12.0F, -10.0F));
/* 42 */     body.addOrReplaceChild("tail", CubeListBuilder.create()
/* 43 */         .texOffs(122, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 14.0F, 0.0F), 
/* 44 */         PartPose.offset(0.0F, -9.0F, 3.5F));
/* 46 */     PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create()
/* 47 */         .texOffs(60, 24).addBox(-3.5F, -7.0F, -15.0F, 7.0F, 8.0F, 19.0F)
/* 48 */         .texOffs(21, 0).addBox(-3.5F, -21.0F, -15.0F, 7.0F, 14.0F, 7.0F)
/* 49 */         .texOffs(50, 0).addBox(-2.5F, -21.0F, -21.0F, 5.0F, 5.0F, 6.0F), 
/* 50 */         PartPose.offset(0.0F, -3.0F, -19.5F));
/* 52 */     head.addOrReplaceChild("left_ear", CubeListBuilder.create()
/* 53 */         .texOffs(45, 0).addBox(-0.5F, 0.5F, -1.0F, 3.0F, 1.0F, 2.0F), 
/* 54 */         PartPose.offset(2.5F, -21.0F, -9.5F));
/* 56 */     head.addOrReplaceChild("right_ear", CubeListBuilder.create()
/* 57 */         .texOffs(67, 0).addBox(-2.5F, 0.5F, -1.0F, 3.0F, 1.0F, 2.0F), 
/* 58 */         PartPose.offset(-2.5F, -21.0F, -9.5F));
/* 60 */     root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
/* 61 */         .texOffs(58, 16).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), 
/* 62 */         PartPose.offset(4.9F, 1.0F, 9.5F));
/* 64 */     root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
/* 65 */         .texOffs(94, 16).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), 
/* 66 */         PartPose.offset(-4.9F, 1.0F, 9.5F));
/* 68 */     root.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
/* 69 */         .texOffs(0, 0).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), 
/* 70 */         PartPose.offset(4.9F, 1.0F, -10.5F));
/* 72 */     root.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
/* 73 */         .texOffs(0, 26).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), 
/* 74 */         PartPose.offset(-4.9F, 1.0F, -10.5F));
/* 76 */     return mesh;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\camel\AdultCamelModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */