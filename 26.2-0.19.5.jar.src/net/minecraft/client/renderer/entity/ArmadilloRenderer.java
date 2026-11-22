/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.armadillo.AdultArmadilloModel;
/*    */ import net.minecraft.client.model.animal.armadillo.ArmadilloModel;
/*    */ import net.minecraft.client.model.animal.armadillo.BabyArmadilloModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.ArmadilloRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*    */ 
/*    */ public class ArmadilloRenderer extends AgeableMobRenderer<Armadillo, ArmadilloRenderState, ArmadilloModel> {
/* 12 */   private static final Identifier ARMADILLO_LOCATION = Identifier.withDefaultNamespace("textures/entity/armadillo/armadillo.png");
/*    */   
/* 13 */   private static final Identifier ARMADILLO_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/armadillo/armadillo_baby.png");
/*    */   
/*    */   public ArmadilloRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context, new AdultArmadilloModel(context.bakeLayer(ModelLayers.ARMADILLO)), new BabyArmadilloModel(context.bakeLayer(ModelLayers.ARMADILLO_BABY)), 0.4F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(ArmadilloRenderState state) {
/* 21 */     return state.isBaby ? ARMADILLO_BABY_LOCATION : ARMADILLO_LOCATION;
/*    */   }
/*    */   
/*    */   public ArmadilloRenderState createRenderState() {
/* 26 */     return new ArmadilloRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Armadillo entity, ArmadilloRenderState state, float partialTicks) {
/* 31 */     super.extractRenderState(entity, state, partialTicks);
/* 32 */     state.isHidingInShell = entity.shouldHideInShell();
/* 33 */     state.peekAnimationState.copyFrom(entity.peekAnimationState);
/* 34 */     state.rollOutAnimationState.copyFrom(entity.rollOutAnimationState);
/* 35 */     state.rollUpAnimationState.copyFrom(entity.rollUpAnimationState);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\ArmadilloRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */