/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.feline.AbstractFelineModel;
/*    */ import net.minecraft.client.model.animal.feline.AdultOcelotModel;
/*    */ import net.minecraft.client.model.animal.feline.BabyOcelotModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.FelineRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.feline.Ocelot;
/*    */ 
/*    */ public class OcelotRenderer extends AgeableMobRenderer<Ocelot, FelineRenderState, AbstractFelineModel<FelineRenderState>> {
/* 12 */   private static final Identifier CAT_OCELOT_LOCATION = Identifier.withDefaultNamespace("textures/entity/cat/ocelot.png");
/*    */   
/* 13 */   private static final Identifier CAT_OCELOT_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/cat/ocelot_baby.png");
/*    */   
/*    */   public OcelotRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context, new AdultOcelotModel(context.bakeLayer(ModelLayers.OCELOT)), new BabyOcelotModel(context.bakeLayer(ModelLayers.OCELOT_BABY)), 0.4F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(FelineRenderState state) {
/* 21 */     return state.isBaby ? CAT_OCELOT_BABY_LOCATION : CAT_OCELOT_LOCATION;
/*    */   }
/*    */   
/*    */   public FelineRenderState createRenderState() {
/* 26 */     return new FelineRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Ocelot entity, FelineRenderState state, float partialTicks) {
/* 31 */     super.extractRenderState(entity, state, partialTicks);
/* 32 */     state.isCrouching = entity.isCrouching();
/* 33 */     state.isSprinting = entity.isSprinting();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\OcelotRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */