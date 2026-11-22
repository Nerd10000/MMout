/*    */ package net.minecraft.client.model.animal.axolotl;
/*    */ 
/*    */ import net.minecraft.client.animation.KeyframeAnimation;
/*    */ import net.minecraft.client.animation.definitions.BabyAxolotlAnimation;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.entity.state.AxolotlRenderState;
/*    */ 
/*    */ public class BabyAxolotlModel extends EntityModel<AxolotlRenderState> {
/*    */   private static final float MAX_WALK_ANIMATION_SPEED = 15.0F;
/*    */   
/*    */   private static final float WALK_ANIMATION_SCALE_FACTOR = 30.0F;
/*    */   
/*    */   private final KeyframeAnimation walkAnimation;
/*    */   
/*    */   private final KeyframeAnimation walkUnderwaterAnimation;
/*    */   
/*    */   private final KeyframeAnimation swimAnimation;
/*    */   
/*    */   private final KeyframeAnimation idleOnGroundAnimation;
/*    */   
/*    */   private final KeyframeAnimation idleOnGroundUnderWaterAnimation;
/*    */   
/*    */   private final KeyframeAnimation idleUnderWaterAnimation;
/*    */   
/*    */   private final KeyframeAnimation playDeadAnimation;
/*    */   
/*    */   public BabyAxolotlModel(ModelPart root) {
/* 30 */     super(root);
/* 32 */     this.swimAnimation = BabyAxolotlAnimation.BABY_AXOLOTL_SWIM.bake(root);
/* 33 */     this.walkAnimation = BabyAxolotlAnimation.AXOLOTL_WALK_FLOOR.bake(root);
/* 34 */     this.walkUnderwaterAnimation = BabyAxolotlAnimation.WALK_FLOOR_UNDERWATER.bake(root);
/* 35 */     this.idleUnderWaterAnimation = BabyAxolotlAnimation.IDLE_UNDERWATER.bake(root);
/* 36 */     this.idleOnGroundUnderWaterAnimation = BabyAxolotlAnimation.IDLE_FLOOR_UNDERWATER.bake(root);
/* 37 */     this.idleOnGroundAnimation = BabyAxolotlAnimation.BABY_AXOLOTL_IDLE_FLOOR.bake(root);
/* 38 */     this.playDeadAnimation = BabyAxolotlAnimation.BABY_AXOLOTL_PLAY_DEAD.bake(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 42 */     MeshDefinition meshdefinition = new MeshDefinition();
/* 43 */     PartDefinition partdefinition = meshdefinition.getRoot();
/* 45 */     PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
/* 46 */     PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -0.75F, -2.75F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
/* 47 */         .texOffs(0, 12).addBox(0.0F, -1.75F, -2.75F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.25F, 1.75F));
/* 48 */     body.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(20, 16).addBox(-3.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.25F, -1.25F));
/* 49 */     PartDefinition right_leg = body.addOrReplaceChild("right_hind_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 0.25F, 1.75F, 0.0F, 1.5708F, 1.5708F));
/* 50 */     right_leg.addOrReplaceChild("right_leg_r1", CubeListBuilder.create().texOffs(20, 14).addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));
/* 51 */     body.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(20, 13).addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.25F, -1.25F));
/* 52 */     body.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(20, 14).addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.25F, 1.75F));
/* 53 */     body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(10, 9).addBox(0.0F, -1.5F, -1.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.25F, 3.25F));
/* 54 */     PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 8).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.25F, -2.75F));
/* 55 */     head.addOrReplaceChild("left_gills", CubeListBuilder.create().texOffs(20, 8).addBox(0.0F, -3.5F, 0.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -0.5F, -2.0F));
/* 56 */     head.addOrReplaceChild("right_gills", CubeListBuilder.create().texOffs(20, 3).addBox(-3.0F, -3.5F, 0.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -0.5F, -2.0F));
/* 57 */     head.addOrReplaceChild("top_gills", CubeListBuilder.create().texOffs(20, 0).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -2.0F));
/* 59 */     return LayerDefinition.create(meshdefinition, 32, 32);
/*    */   }
/*    */   
/*    */   public void setupAnim(AxolotlRenderState state) {
/* 64 */     super.setupAnim(state);
/* 66 */     if (state.walkAnimationState.isStarted())
/* 67 */       this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 15.0F, 30.0F); 
/* 69 */     this.swimAnimation.apply(state.swimAnimation, state.ageInTicks);
/* 70 */     this.walkUnderwaterAnimation.apply(state.walkAnimationState, state.ageInTicks);
/* 71 */     this.idleOnGroundAnimation.apply(state.idleOnGroundAnimationState, state.ageInTicks);
/* 72 */     this.idleUnderWaterAnimation.apply(state.idleUnderWaterAnimationState, state.ageInTicks);
/* 73 */     this.idleOnGroundUnderWaterAnimation.apply(state.idleUnderWaterOnGroundAnimationState, state.ageInTicks);
/* 74 */     this.playDeadAnimation.apply(state.playDeadAnimationState, state.ageInTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\axolotl\BabyAxolotlModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */