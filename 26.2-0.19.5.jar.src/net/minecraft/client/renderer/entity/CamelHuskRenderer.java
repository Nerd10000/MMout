/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.camel.AdultCamelModel;
/*    */ import net.minecraft.client.model.animal.camel.CamelModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.CamelRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.camel.Camel;
/*    */ 
/*    */ public class CamelHuskRenderer extends MobRenderer<Camel, CamelRenderState, CamelModel> {
/* 12 */   private static final Identifier CAMEL_HUSK_LOCATION = Identifier.withDefaultNamespace("textures/entity/camel/camel_husk.png");
/*    */   
/*    */   public CamelHuskRenderer(EntityRendererProvider.Context context) {
/* 15 */     super(context, new AdultCamelModel(context.bakeLayer(ModelLayers.CAMEL)), 0.7F);
/* 16 */     addLayer((RenderLayer)CamelRenderer.createCamelSaddleLayer(context, this, EquipmentClientInfo.LayerType.CAMEL_HUSK_SADDLE, ModelLayers.CAMEL_HUSK_SADDLE));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(CamelRenderState state) {
/* 21 */     return CAMEL_HUSK_LOCATION;
/*    */   }
/*    */   
/*    */   public CamelRenderState createRenderState() {
/* 26 */     return new CamelRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Camel entity, CamelRenderState state, float partialTicks) {
/* 31 */     super.extractRenderState(entity, state, partialTicks);
/* 32 */     CamelRenderer.extractAdditionalState(entity, state, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\CamelHuskRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */