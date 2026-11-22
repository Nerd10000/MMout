/*    */ package net.minecraft.client.model.monster.piglin;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.model.player.PlayerModel;
/*    */ 
/*    */ public class AdultPiglinModel extends PiglinModel {
/*    */   public AdultPiglinModel(ModelPart root) {
/* 16 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 20 */     MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, false);
/* 22 */     PartDefinition root = mesh.getRoot();
/* 23 */     root.addOrReplaceChild("body", 
/* 24 */         CubeListBuilder.create()
/* 25 */         .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F), PartPose.ZERO);
/* 28 */     PartDefinition head = addHead(CubeDeformation.NONE, mesh);
/* 29 */     head.clearChild("hat");
/* 30 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   protected float getDefaultEarAngleInDegrees() {
/* 35 */     return 30.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\piglin\AdultPiglinModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */