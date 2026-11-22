/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.allay.AllayModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.AllayRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.allay.Allay;
/*    */ 
/*    */ public class AllayRenderer extends MobRenderer<Allay, AllayRenderState, AllayModel> {
/* 14 */   private static final Identifier ALLAY_TEXTURE = Identifier.withDefaultNamespace("textures/entity/allay/allay.png");
/*    */   
/*    */   public AllayRenderer(EntityRendererProvider.Context context) {
/* 17 */     super(context, new AllayModel(context.bakeLayer(ModelLayers.ALLAY)), 0.4F);
/* 19 */     addLayer((RenderLayer<AllayRenderState, AllayModel>)new ItemInHandLayer(this));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(AllayRenderState state) {
/* 24 */     return ALLAY_TEXTURE;
/*    */   }
/*    */   
/*    */   public AllayRenderState createRenderState() {
/* 29 */     return new AllayRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Allay entity, AllayRenderState state, float partialTicks) {
/* 34 */     super.extractRenderState(entity, state, partialTicks);
/* 35 */     ArmedEntityRenderState.extractArmedEntityRenderState((LivingEntity)entity, (ArmedEntityRenderState)state, this.itemModelResolver, partialTicks);
/* 36 */     state.isDancing = entity.isDancing();
/* 37 */     state.isSpinning = entity.isSpinning();
/* 38 */     state.spinningProgress = entity.getSpinningProgress(partialTicks);
/* 39 */     state.holdingAnimationProgress = entity.getHoldingItemAnimationProgress(partialTicks);
/*    */   }
/*    */   
/*    */   protected int getBlockLightLevel(Allay entity, BlockPos blockPos) {
/* 44 */     return 15;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\AllayRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */