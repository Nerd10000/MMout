/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.wolf.AdultWolfModel;
/*    */ import net.minecraft.client.model.animal.wolf.BabyWolfModel;
/*    */ import net.minecraft.client.model.animal.wolf.WolfModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.WolfArmorLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.WolfRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*    */ 
/*    */ public class WolfRenderer extends AgeableMobRenderer<Wolf, WolfRenderState, WolfModel> {
/*    */   public WolfRenderer(EntityRendererProvider.Context context) {
/* 17 */     super(context, new AdultWolfModel(context.bakeLayer(ModelLayers.WOLF)), new BabyWolfModel(context.bakeLayer(ModelLayers.WOLF_BABY)), 0.5F);
/* 19 */     addLayer((RenderLayer<WolfRenderState, WolfModel>)new WolfArmorLayer(this, context.getModelSet(), context.getEquipmentRenderer()));
/* 20 */     addLayer((RenderLayer<WolfRenderState, WolfModel>)new WolfCollarLayer(this));
/*    */   }
/*    */   
/*    */   protected int getModelTint(WolfRenderState state) {
/* 26 */     float wetShade = state.wetShade;
/* 27 */     if (wetShade == 1.0F)
/* 28 */       return -1; 
/* 30 */     return ARGB.colorFromFloat(1.0F, wetShade, wetShade, wetShade);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(WolfRenderState state) {
/* 35 */     return state.texture;
/*    */   }
/*    */   
/*    */   public WolfRenderState createRenderState() {
/* 40 */     return new WolfRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Wolf entity, WolfRenderState state, float partialTicks) {
/* 45 */     super.extractRenderState(entity, state, partialTicks);
/* 46 */     state.isAngry = entity.isAngry();
/* 47 */     state.isSitting = entity.isInSittingPose();
/* 48 */     state.tailAngle = entity.getTailAngle();
/* 49 */     state.headRollAngle = entity.getHeadRollAngle(partialTicks);
/* 50 */     state.shakeAnim = entity.getShakeAnim(partialTicks);
/* 51 */     state.texture = entity.getTexture();
/* 52 */     state.wetShade = entity.getWetShade(partialTicks);
/* 53 */     state.collarColor = entity.isTame() ? entity.getCollarColor() : null;
/* 54 */     state.bodyArmorItem = entity.getBodyArmorItem().copy();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\WolfRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */