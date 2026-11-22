/*    */ package net.minecraft.client.model.animal.turtle;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.TurtleRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ 
/*    */ public class AdultTurtleModel extends TurtleModel {
/*    */   private static final String EGG_BELLY = "egg_belly";
/*    */   
/*    */   private final ModelPart eggBelly;
/*    */   
/*    */   public AdultTurtleModel(ModelPart root) {
/* 21 */     super(root, RenderTypes::entityCutout);
/* 22 */     this.eggBelly = root.getChild("egg_belly");
/*    */   }
/*    */   
/*    */   public void setupAnim(TurtleRenderState state) {
/* 27 */     super.setupAnim(state);
/* 28 */     this.eggBelly.visible = state.hasEgg;
/* 30 */     if (this.eggBelly.visible)
/* 31 */       this.root.y--; 
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 36 */     MeshDefinition mesh = new MeshDefinition();
/* 37 */     PartDefinition root = mesh.getRoot();
/* 39 */     root.addOrReplaceChild("head", 
/* 40 */         CubeListBuilder.create()
/* 41 */         .texOffs(3, 0).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 5.0F, 6.0F), 
/* 42 */         PartPose.offset(0.0F, 19.0F, -10.0F));
/* 44 */     root.addOrReplaceChild("body", 
/* 45 */         CubeListBuilder.create()
/* 46 */         .texOffs(7, 37).addBox("shell", -9.5F, 3.0F, -10.0F, 19.0F, 20.0F, 6.0F)
/* 47 */         .texOffs(31, 1).addBox("belly", -5.5F, 3.0F, -13.0F, 11.0F, 18.0F, 3.0F), 
/* 48 */         PartPose.offsetAndRotation(0.0F, 11.0F, -10.0F, 1.5707964F, 0.0F, 0.0F));
/* 50 */     root.addOrReplaceChild("egg_belly", 
/* 51 */         CubeListBuilder.create()
/* 52 */         .texOffs(70, 33).addBox(-4.5F, 3.0F, -14.0F, 9.0F, 18.0F, 1.0F), 
/* 53 */         PartPose.offsetAndRotation(0.0F, 11.0F, -10.0F, 1.5707964F, 0.0F, 0.0F));
/* 55 */     int legHeight = 1;
/* 56 */     root.addOrReplaceChild("right_hind_leg", 
/* 57 */         CubeListBuilder.create()
/* 58 */         .texOffs(1, 23).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 1.0F, 10.0F), 
/* 59 */         PartPose.offset(-3.5F, 22.0F, 11.0F));
/* 61 */     root.addOrReplaceChild("left_hind_leg", 
/* 62 */         CubeListBuilder.create()
/* 63 */         .texOffs(1, 12).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 1.0F, 10.0F), 
/* 64 */         PartPose.offset(3.5F, 22.0F, 11.0F));
/* 66 */     root.addOrReplaceChild("right_front_leg", 
/* 67 */         CubeListBuilder.create()
/* 68 */         .texOffs(27, 30).addBox(-13.0F, 0.0F, -2.0F, 13.0F, 1.0F, 5.0F), 
/* 69 */         PartPose.offset(-5.0F, 21.0F, -4.0F));
/* 71 */     root.addOrReplaceChild("left_front_leg", 
/* 72 */         CubeListBuilder.create()
/* 73 */         .texOffs(27, 24).addBox(0.0F, 0.0F, -2.0F, 13.0F, 1.0F, 5.0F), 
/* 74 */         PartPose.offset(5.0F, 21.0F, -4.0F));
/* 77 */     return LayerDefinition.create(mesh, 128, 64);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\turtle\AdultTurtleModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */