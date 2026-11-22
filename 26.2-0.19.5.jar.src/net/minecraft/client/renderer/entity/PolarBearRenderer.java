/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.polarbear.PolarBearModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.PolarBearRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.polarbear.PolarBear;
/*    */ 
/*    */ public class PolarBearRenderer extends AgeableMobRenderer<PolarBear, PolarBearRenderState, PolarBearModel> {
/* 10 */   private static final Identifier BEAR_LOCATION = Identifier.withDefaultNamespace("textures/entity/bear/polarbear.png");
/*    */   
/* 11 */   private static final Identifier BABY_BEAR_LOCATION = Identifier.withDefaultNamespace("textures/entity/bear/polarbear_baby.png");
/*    */   
/*    */   public PolarBearRenderer(EntityRendererProvider.Context context) {
/* 14 */     super(context, new PolarBearModel(context.bakeLayer(ModelLayers.POLAR_BEAR)), new PolarBearModel(context.bakeLayer(ModelLayers.POLAR_BEAR_BABY)), 0.9F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(PolarBearRenderState state) {
/* 19 */     return state.isBaby ? BABY_BEAR_LOCATION : BEAR_LOCATION;
/*    */   }
/*    */   
/*    */   public PolarBearRenderState createRenderState() {
/* 24 */     return new PolarBearRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(PolarBear entity, PolarBearRenderState state, float partialTicks) {
/* 29 */     super.extractRenderState(entity, state, partialTicks);
/* 30 */     state.standScale = entity.getStandingAnimationScale(partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\PolarBearRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */