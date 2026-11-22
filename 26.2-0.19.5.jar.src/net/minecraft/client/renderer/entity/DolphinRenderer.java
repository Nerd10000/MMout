/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.dolphin.DolphinModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.DolphinCarryingItemLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.DolphinRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.dolphin.Dolphin;
/*    */ 
/*    */ public class DolphinRenderer extends AgeableMobRenderer<Dolphin, DolphinRenderState, DolphinModel> {
/* 13 */   private static final Identifier DOLPHIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/dolphin/dolphin.png");
/*    */   
/* 14 */   private static final Identifier DOLPHIN_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/dolphin/dolphin_baby.png");
/*    */   
/*    */   public DolphinRenderer(EntityRendererProvider.Context context) {
/* 17 */     super(context, new DolphinModel(context.bakeLayer(ModelLayers.DOLPHIN)), new DolphinModel(context.bakeLayer(ModelLayers.DOLPHIN_BABY)), 0.7F);
/* 19 */     addLayer((RenderLayer<DolphinRenderState, DolphinModel>)new DolphinCarryingItemLayer(this));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(DolphinRenderState state) {
/* 24 */     return state.isBaby ? DOLPHIN_BABY_LOCATION : DOLPHIN_LOCATION;
/*    */   }
/*    */   
/*    */   public DolphinRenderState createRenderState() {
/* 29 */     return new DolphinRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Dolphin entity, DolphinRenderState state, float partialTicks) {
/* 34 */     super.extractRenderState(entity, state, partialTicks);
/* 35 */     HoldingEntityRenderState.extractHoldingEntityRenderState((LivingEntity)entity, (HoldingEntityRenderState)state, this.itemModelResolver);
/* 36 */     state.isMoving = (entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-7D);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\DolphinRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */