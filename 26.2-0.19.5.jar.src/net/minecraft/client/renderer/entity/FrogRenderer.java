/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.frog.FrogModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.FrogRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.frog.Frog;
/*    */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*    */ 
/*    */ public class FrogRenderer extends MobRenderer<Frog, FrogRenderState, FrogModel> {
/*    */   public FrogRenderer(EntityRendererProvider.Context context) {
/* 11 */     super(context, new FrogModel(context.bakeLayer(ModelLayers.FROG)), 0.3F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(FrogRenderState state) {
/* 16 */     return state.texture;
/*    */   }
/*    */   
/*    */   public FrogRenderState createRenderState() {
/* 21 */     return new FrogRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Frog entity, FrogRenderState state, float partialTicks) {
/* 26 */     super.extractRenderState(entity, state, partialTicks);
/* 27 */     state.isSwimming = entity.isInWater();
/* 28 */     state.jumpAnimationState.copyFrom(entity.jumpAnimationState);
/* 29 */     state.croakAnimationState.copyFrom(entity.croakAnimationState);
/* 30 */     state.tongueAnimationState.copyFrom(entity.tongueAnimationState);
/* 31 */     state.swimIdleAnimationState.copyFrom(entity.swimIdleAnimationState);
/* 32 */     state.texture = ((FrogVariant)entity.getVariant().value()).assetInfo().texturePath();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\FrogRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */