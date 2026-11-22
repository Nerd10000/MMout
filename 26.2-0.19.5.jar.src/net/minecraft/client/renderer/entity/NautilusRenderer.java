/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.nautilus.NautilusArmorModel;
/*    */ import net.minecraft.client.model.animal.nautilus.NautilusModel;
/*    */ import net.minecraft.client.model.animal.nautilus.NautilusSaddleModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.NautilusRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class NautilusRenderer<T extends AbstractNautilus> extends AgeableMobRenderer<T, NautilusRenderState, NautilusModel> {
/* 15 */   private static final Identifier NAUTILUS_LOCATION = Identifier.withDefaultNamespace("textures/entity/nautilus/nautilus.png");
/*    */   
/* 16 */   private static final Identifier NAUTILUS_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/nautilus/nautilus_baby.png");
/*    */   
/*    */   public NautilusRenderer(EntityRendererProvider.Context context) {
/* 19 */     super(context, new NautilusModel(context
/* 20 */           .bakeLayer(ModelLayers.NAUTILUS)), new NautilusModel(context
/* 21 */           .bakeLayer(ModelLayers.NAUTILUS_BABY)), 0.7F);
/* 22 */     addLayer((RenderLayer<NautilusRenderState, NautilusModel>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.NAUTILUS_BODY, state -> state.bodyArmorItem, (EntityModel)new NautilusArmorModel(context
/*    */             
/* 25 */             .bakeLayer(ModelLayers.NAUTILUS_ARMOR)), null));
/* 28 */     addLayer((RenderLayer<NautilusRenderState, NautilusModel>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.NAUTILUS_SADDLE, state -> state.saddle, (EntityModel)new NautilusSaddleModel(context
/*    */             
/* 31 */             .bakeLayer(ModelLayers.NAUTILUS_SADDLE)), null));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(NautilusRenderState state) {
/* 38 */     return state.isBaby ? NAUTILUS_BABY_LOCATION : NAUTILUS_LOCATION;
/*    */   }
/*    */   
/*    */   public NautilusRenderState createRenderState() {
/* 43 */     return new NautilusRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(T entity, NautilusRenderState state, float partialTicks) {
/* 48 */     super.extractRenderState(entity, state, partialTicks);
/* 49 */     state.saddle = entity.getItemBySlot(EquipmentSlot.SADDLE).copy();
/* 50 */     state.bodyArmorItem = entity.getBodyArmorItem().copy();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\NautilusRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */