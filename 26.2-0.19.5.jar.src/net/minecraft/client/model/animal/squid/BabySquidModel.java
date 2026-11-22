/*    */ package net.minecraft.client.model.animal.squid;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public class BabySquidModel extends SquidModel {
/*    */   public BabySquidModel(ModelPart root) {
/* 14 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 18 */     MeshDefinition meshdefinition = new MeshDefinition();
/* 19 */     PartDefinition root = meshdefinition.getRoot();
/* 21 */     PartDefinition body = root.addOrReplaceChild("body", 
/* 22 */         CubeListBuilder.create()
/* 23 */         .texOffs(0, 0).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), 
/* 24 */         PartPose.offset(0.0F, 13.0F, 0.0F));
/* 27 */     int tentacleCount = 8;
/* 29 */     CubeListBuilder tentacle = CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 6.0F, 2.0F);
/* 31 */     for (int i = 0; i < 8; i++) {
/* 32 */       double angle = i * Math.PI * 2.0D / 8.0D;
/* 33 */       float x = (float)Math.cos(angle) * 3.0F;
/* 34 */       float y = 18.5F;
/* 35 */       float z = (float)Math.sin(angle) * 3.0F;
/* 37 */       angle = i * Math.PI * -2.0D / 8.0D + 1.5707963267948966D;
/* 38 */       float yRot = (float)angle;
/* 40 */       root.addOrReplaceChild(createTentacleName(i), tentacle, PartPose.offsetAndRotation(x, 18.5F, z, 0.0F, yRot, 0.0F));
/*    */     } 
/* 43 */     return LayerDefinition.create(meshdefinition, 32, 32);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\squid\BabySquidModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */