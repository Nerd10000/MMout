/*    */ package net.minecraft.client.model.monster.piglin;
/*    */ 
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.model.player.PlayerModel;
/*    */ import net.minecraft.client.renderer.entity.ArmorModelSet;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class AbstractPiglinModel<S extends HumanoidRenderState> extends HumanoidModel<S> {
/*    */   protected static final float ADULT_EAR_ANGLE_IN_DEGREES = 30.0F;
/*    */   
/*    */   protected static final float BABY_EAR_ANGLE_IN_DEGREES = 5.0F;
/*    */   
/*    */   public final ModelPart rightEar;
/*    */   
/*    */   public final ModelPart leftEar;
/*    */   
/*    */   public AbstractPiglinModel(ModelPart root) {
/* 25 */     super(root, RenderTypes::entityTranslucent);
/* 26 */     this.rightEar = this.head.getChild("right_ear");
/* 27 */     this.leftEar = this.head.getChild("left_ear");
/*    */   }
/*    */   
/*    */   public static ArmorModelSet<MeshDefinition> createArmorMeshSet(CubeDeformation innerDeformation, CubeDeformation outerDeformation) {
/* 31 */     return PlayerModel.createArmorMeshSet(innerDeformation, outerDeformation).map(AbstractPiglinModel::removeEars);
/*    */   }
/*    */   
/*    */   private static MeshDefinition removeEars(MeshDefinition mesh) {
/* 35 */     PartDefinition root = mesh.getRoot();
/* 36 */     PartDefinition head = root.getChild("head");
/* 37 */     head.addOrReplaceChild("left_ear", CubeListBuilder.create(), PartPose.ZERO);
/* 38 */     head.addOrReplaceChild("right_ear", CubeListBuilder.create(), PartPose.ZERO);
/* 39 */     return mesh;
/*    */   }
/*    */   
/*    */   public static PartDefinition addHead(CubeDeformation g, MeshDefinition mesh) {
/* 43 */     PartDefinition root = mesh.getRoot();
/* 44 */     PartDefinition head = root.addOrReplaceChild("head", 
/* 45 */         CubeListBuilder.create()
/* 46 */         .texOffs(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, g)
/* 47 */         .texOffs(31, 1).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, g)
/* 48 */         .texOffs(2, 4).addBox(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, g)
/* 49 */         .texOffs(2, 0).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, g), PartPose.ZERO);
/* 53 */     head.addOrReplaceChild("left_ear", 
/* 54 */         CubeListBuilder.create()
/* 55 */         .texOffs(51, 6).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, g), 
/* 56 */         PartPose.offsetAndRotation(4.5F, -6.0F, 0.0F, 0.0F, 0.0F, -0.5235988F));
/* 58 */     head.addOrReplaceChild("right_ear", 
/* 59 */         CubeListBuilder.create()
/* 60 */         .texOffs(39, 6).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, g), 
/* 61 */         PartPose.offsetAndRotation(-4.5F, -6.0F, 0.0F, 0.0F, 0.0F, 0.5235988F));
/* 63 */     return head;
/*    */   }
/*    */   
/*    */   public static ArmorModelSet<MeshDefinition> createBabyArmorMeshSet(CubeDeformation innerDeformation, CubeDeformation outerDeformation, PartPose armOffset) {
/* 67 */     return PlayerModel.createBabyArmorMeshSet(innerDeformation, outerDeformation, armOffset).map(AbstractPiglinModel::removeEars);
/*    */   }
/*    */   
/*    */   public void setupAnim(S state) {
/* 72 */     super.setupAnim((HumanoidRenderState)state);
/* 74 */     float animationPos = ((HumanoidRenderState)state).walkAnimationPos;
/* 75 */     float animationSpeed = ((HumanoidRenderState)state).walkAnimationSpeed;
/* 76 */     float defaultAngle = getDefaultEarAngleInDegrees() * 0.017453292F;
/* 77 */     float frequency = ((HumanoidRenderState)state).ageInTicks * 0.1F + animationPos * 0.5F;
/* 78 */     float amplitude = 0.08F + animationSpeed * 0.4F;
/* 79 */     this.leftEar.zRot = -defaultAngle - Mth.cos((frequency * 1.2F)) * amplitude;
/* 80 */     this.rightEar.zRot = defaultAngle + Mth.cos(frequency) * amplitude;
/*    */   }
/*    */   
/*    */   protected abstract float getDefaultEarAngleInDegrees();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\piglin\AbstractPiglinModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */