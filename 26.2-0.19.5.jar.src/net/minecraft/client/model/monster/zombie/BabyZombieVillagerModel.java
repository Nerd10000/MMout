/*    */ package net.minecraft.client.model.monster.zombie;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieVillagerRenderState;
/*    */ 
/*    */ public class BabyZombieVillagerModel<S extends ZombieVillagerRenderState> extends ZombieVillagerModel<S> {
/*    */   public BabyZombieVillagerModel(ModelPart root) {
/* 16 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 20 */     MeshDefinition mesh = new MeshDefinition();
/* 21 */     PartDefinition root = mesh.getRoot();
/* 23 */     root.addOrReplaceChild("body", 
/* 24 */         CubeListBuilder.create()
/* 25 */         .texOffs(0, 15).addBox(-2.0F, -2.75F, -1.5F, 4.0F, 5.0F, 3.0F)
/* 26 */         .texOffs(16, 22).addBox(-2.0F, -2.75F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.1F)), 
/* 27 */         PartPose.offset(0.0F, 18.75F, 0.0F));
/* 29 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 30 */         CubeListBuilder.create()
/* 31 */         .texOffs(0, 0).addBox(-4.0F, -8.0F, -3.5F, 8.0F, 8.0F, 7.0F), 
/* 32 */         PartPose.offset(0.0F, 16.0F, 0.0F));
/* 34 */     head.addOrReplaceChild("hat", 
/* 35 */         CubeListBuilder.create()
/* 36 */         .texOffs(0, 31).addBox(-4.0F, -4.0F, -3.5F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.3F)), 
/* 37 */         PartPose.offset(0.0F, -4.0F, 0.0F));
/* 39 */     head.addOrReplaceChild("hat_rim", 
/* 40 */         CubeListBuilder.create()
/* 41 */         .texOffs(0, 46).addBox(-7.0F, -0.5F, -6.0F, 14.0F, 1.0F, 12.0F), 
/* 42 */         PartPose.offset(0.0F, -4.5F, 0.0F));
/* 44 */     head.addOrReplaceChild("nose", 
/* 45 */         CubeListBuilder.create()
/* 46 */         .texOffs(23, 0).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F), 
/* 47 */         PartPose.offset(0.0F, -1.0F, -4.0F));
/* 49 */     PartDefinition right_arm = root.addOrReplaceChild("right_arm", 
/* 50 */         CubeListBuilder.create()
/* 51 */         .texOffs(24, 15).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), 
/* 52 */         PartPose.offset(-3.0F, 15.5F, 0.0F));
/* 54 */     PartDefinition left_arm = root.addOrReplaceChild("left_arm", 
/* 55 */         CubeListBuilder.create().texOffs(16, 15)
/* 56 */         .addBox(-1.0F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), 
/* 57 */         PartPose.offset(3.0F, 15.5F, 0.0F));
/* 59 */     root.addOrReplaceChild("right_leg", 
/* 60 */         CubeListBuilder.create()
/* 61 */         .texOffs(8, 23).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F), 
/* 62 */         PartPose.offset(-1.0F, 21.5F, 0.0F));
/* 64 */     root.addOrReplaceChild("left_leg", 
/* 65 */         CubeListBuilder.create()
/* 66 */         .texOffs(0, 23).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F), 
/* 67 */         PartPose.offset(1.0F, 21.5F, 0.0F));
/* 70 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createNoHatLayer() {
/* 74 */     return createBodyLayer().apply(mesh -> {
/*    */           mesh.getRoot().clearChild("head").clearRecursively();
/*    */           return mesh;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\zombie\BabyZombieVillagerModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */