/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.camel.AdultCamelModel;
/*    */ import net.minecraft.client.model.animal.camel.BabyCamelModel;
/*    */ import net.minecraft.client.model.animal.camel.CamelModel;
/*    */ import net.minecraft.client.model.animal.camel.CamelSaddleModel;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
/*    */ import net.minecraft.client.renderer.entity.state.CamelRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.camel.Camel;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class CamelRenderer extends AgeableMobRenderer<Camel, CamelRenderState, CamelModel> {
/* 17 */   private static final Identifier CAMEL_LOCATION = Identifier.withDefaultNamespace("textures/entity/camel/camel.png");
/*    */   
/* 18 */   private static final Identifier CAMEL_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/camel/camel_baby.png");
/*    */   
/*    */   public CamelRenderer(EntityRendererProvider.Context context) {
/* 21 */     super(context, new AdultCamelModel(context.bakeLayer(ModelLayers.CAMEL)), new BabyCamelModel(context.bakeLayer(ModelLayers.CAMEL_BABY)), 0.7F);
/* 22 */     addLayer((RenderLayer)createCamelSaddleLayer(context, this, EquipmentClientInfo.LayerType.CAMEL_SADDLE, ModelLayers.CAMEL_SADDLE));
/*    */   }
/*    */   
/*    */   protected static SimpleEquipmentLayer<CamelRenderState, CamelModel, CamelSaddleModel> createCamelSaddleLayer(EntityRendererProvider.Context context, MobRenderer<Camel, CamelRenderState, CamelModel> renderer, EquipmentClientInfo.LayerType saddleLayerType, ModelLayerLocation saddleModelLayer) {
/* 26 */     return new SimpleEquipmentLayer(renderer, context.getEquipmentRenderer(), saddleLayerType, state -> state.saddle, (EntityModel)new CamelSaddleModel(context.bakeLayer(saddleModelLayer)), null);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(CamelRenderState state) {
/* 31 */     return state.isBaby ? CAMEL_BABY_LOCATION : CAMEL_LOCATION;
/*    */   }
/*    */   
/*    */   public CamelRenderState createRenderState() {
/* 36 */     return new CamelRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Camel entity, CamelRenderState state, float partialTicks) {
/* 41 */     super.extractRenderState(entity, state, partialTicks);
/* 42 */     extractAdditionalState(entity, state, partialTicks);
/*    */   }
/*    */   
/*    */   public static void extractAdditionalState(Camel entity, CamelRenderState state, float partialTicks) {
/* 46 */     state.saddle = entity.getItemBySlot(EquipmentSlot.SADDLE).copy();
/* 47 */     state.isRidden = entity.isVehicle();
/* 48 */     state.jumpCooldown = getJumpCooldown(entity, partialTicks);
/* 49 */     state.sitAnimationState.copyFrom(entity.sitAnimationState);
/* 50 */     state.sitPoseAnimationState.copyFrom(entity.sitPoseAnimationState);
/* 51 */     state.sitUpAnimationState.copyFrom(entity.sitUpAnimationState);
/* 52 */     state.idleAnimationState.copyFrom(entity.idleAnimationState);
/* 53 */     state.dashAnimationState.copyFrom(entity.dashAnimationState);
/*    */   }
/*    */   
/*    */   private static float getJumpCooldown(Camel camel, float partialTicks) {
/* 58 */     return Math.max(camel.getJumpCooldown() - partialTicks, 0.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\CamelRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */