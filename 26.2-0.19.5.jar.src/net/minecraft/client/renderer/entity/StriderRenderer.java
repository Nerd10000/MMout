/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.strider.AdultStriderModel;
/*    */ import net.minecraft.client.model.monster.strider.BabyStriderModel;
/*    */ import net.minecraft.client.model.monster.strider.StriderModel;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.StriderRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Strider;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class StriderRenderer extends AgeableMobRenderer<Strider, StriderRenderState, StriderModel> {
/* 15 */   private static final Identifier STRIDER_LOCATION = Identifier.withDefaultNamespace("textures/entity/strider/strider.png");
/*    */   
/* 16 */   private static final Identifier STRIDER_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/strider/strider_baby.png");
/*    */   
/* 17 */   private static final Identifier COLD_LOCATION = Identifier.withDefaultNamespace("textures/entity/strider/strider_cold.png");
/*    */   
/* 18 */   private static final Identifier COLD_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/strider/strider_cold_baby.png");
/*    */   
/*    */   private static final float SHADOW_RADIUS = 0.5F;
/*    */   
/*    */   public StriderRenderer(EntityRendererProvider.Context context) {
/* 23 */     super(context, new AdultStriderModel(context.bakeLayer(ModelLayers.STRIDER)), new BabyStriderModel(context.bakeLayer(ModelLayers.STRIDER_BABY)), 0.5F);
/* 25 */     addLayer((RenderLayer<StriderRenderState, StriderModel>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.STRIDER_SADDLE, state -> state.saddle, (EntityModel)new AdultStriderModel(context
/*    */             
/* 28 */             .bakeLayer(ModelLayers.STRIDER_SADDLE)), null));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(StriderRenderState state) {
/* 35 */     if (state.isSuffocating)
/* 36 */       return state.isBaby ? COLD_BABY_LOCATION : COLD_LOCATION; 
/* 38 */     return state.isBaby ? STRIDER_BABY_LOCATION : STRIDER_LOCATION;
/*    */   }
/*    */   
/*    */   protected float getShadowRadius(StriderRenderState state) {
/* 43 */     float radius = super.getShadowRadius(state);
/* 44 */     if (state.isBaby)
/* 46 */       return radius * 0.5F; 
/* 48 */     return radius;
/*    */   }
/*    */   
/*    */   public StriderRenderState createRenderState() {
/* 53 */     return new StriderRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Strider entity, StriderRenderState state, float partialTicks) {
/* 58 */     super.extractRenderState(entity, state, partialTicks);
/* 59 */     state.saddle = entity.getItemBySlot(EquipmentSlot.SADDLE).copy();
/* 60 */     state.isSuffocating = entity.isSuffocating();
/* 61 */     state.isRidden = entity.isVehicle();
/*    */   }
/*    */   
/*    */   protected boolean isShaking(StriderRenderState state) {
/* 66 */     return (super.isShaking(state) || state.isSuffocating);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\StriderRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */