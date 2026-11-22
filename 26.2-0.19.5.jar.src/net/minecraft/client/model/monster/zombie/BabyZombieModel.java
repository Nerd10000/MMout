/*    */ package net.minecraft.client.model.monster.zombie;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ 
/*    */ public class BabyZombieModel<S extends ZombieRenderState> extends ZombieModel<S> {
/*    */   public BabyZombieModel(ModelPart root) {
/* 16 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(CubeDeformation g) {
/* 20 */     MeshDefinition mesh = new MeshDefinition();
/* 21 */     PartDefinition root = mesh.getRoot();
/* 23 */     root.addOrReplaceChild("body", 
/* 24 */         CubeListBuilder.create()
/* 25 */         .texOffs(16, 16).addBox(-2.0F, -2.5F, -1.0F, 4.0F, 5.0F, 2.0F, g), 
/* 26 */         PartPose.offset(0.0F, 17.5F, 0.0F));
/* 28 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 29 */         CubeListBuilder.create()
/* 30 */         .texOffs(3, 3).addBox(-3.0F, -6.25F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
/* 31 */         .texOffs(35, 3).addBox(-3.0F, -6.15F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.25F)), 
/* 32 */         PartPose.offset(0.0F, 15.25F, 0.0F));
/* 34 */     head.addOrReplaceChild("hat", 
/* 35 */         CubeListBuilder.create(), PartPose.ZERO);
/* 38 */     root.addOrReplaceChild("right_arm", 
/* 39 */         CubeListBuilder.create()
/* 40 */         .texOffs(36, 16).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F, g), 
/* 41 */         PartPose.offset(-3.0F, 15.5F, 0.0F));
/* 43 */     root.addOrReplaceChild("left_arm", 
/* 44 */         CubeListBuilder.create()
/* 45 */         .texOffs(28, 16).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F, g), 
/* 46 */         PartPose.offset(3.0F, 15.5F, 0.0F));
/* 48 */     root.addOrReplaceChild("right_leg", 
/* 49 */         CubeListBuilder.create()
/* 50 */         .texOffs(8, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, g), 
/* 51 */         PartPose.offset(-1.0F, 20.0F, 0.0F));
/* 53 */     root.addOrReplaceChild("left_leg", 
/* 54 */         CubeListBuilder.create()
/* 55 */         .texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, g), 
/* 56 */         PartPose.offset(1.0F, 20.0F, 0.0F));
/* 59 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\zombie\BabyZombieModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */