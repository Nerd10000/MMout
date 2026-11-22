/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HoglinRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*    */ 
/*    */ public class HoglinRenderer extends AbstractHoglinRenderer<Hoglin> {
/*  9 */   private static final Identifier HOGLIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/hoglin/hoglin.png");
/*    */   
/* 10 */   private static final Identifier BABY_HOGLIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/hoglin/hoglin_baby.png");
/*    */   
/*    */   public HoglinRenderer(EntityRendererProvider.Context context) {
/* 13 */     super(context, ModelLayers.HOGLIN, ModelLayers.HOGLIN_BABY, 0.7F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(HoglinRenderState state) {
/* 18 */     return state.isBaby ? BABY_HOGLIN_LOCATION : HOGLIN_LOCATION;
/*    */   }
/*    */   
/*    */   public void extractRenderState(Hoglin entity, HoglinRenderState state, float partialTicks) {
/* 23 */     super.extractRenderState(entity, state, partialTicks);
/* 24 */     state.isConverting = entity.isConverting();
/*    */   }
/*    */   
/*    */   protected boolean isShaking(HoglinRenderState state) {
/* 29 */     return (super.isShaking(state) || state.isConverting);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\HoglinRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */