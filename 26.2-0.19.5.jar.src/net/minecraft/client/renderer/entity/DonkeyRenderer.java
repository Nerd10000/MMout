/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.equine.BabyDonkeyModel;
/*    */ import net.minecraft.client.model.animal.equine.DonkeyModel;
/*    */ import net.minecraft.client.model.animal.equine.EquineSaddleModel;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
/*    */ import net.minecraft.client.renderer.entity.state.DonkeyRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EquineRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
/*    */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class DonkeyRenderer<T extends AbstractChestedHorse> extends AbstractHorseRenderer<T, DonkeyRenderState, DonkeyModel> {
/*    */   private final Identifier adultTexture;
/*    */   
/*    */   private final Identifier babyTexture;
/*    */   
/*    */   public DonkeyRenderer(EntityRendererProvider.Context context, EquipmentClientInfo.LayerType saddleLayer, ModelLayerLocation saddleModel, Type adult, Type baby) {
/* 20 */     super(context, new DonkeyModel(context
/* 21 */           .bakeLayer(adult.model)), new BabyDonkeyModel(context
/* 22 */           .bakeLayer(baby.model)));
/* 25 */     this.adultTexture = adult.texture;
/* 26 */     this.babyTexture = baby.texture;
/* 28 */     addLayer((RenderLayer<DonkeyRenderState, DonkeyModel>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), saddleLayer, state -> state.saddle, (EntityModel)new EquineSaddleModel(context
/*    */             
/* 31 */             .bakeLayer(saddleModel)), null));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(DonkeyRenderState state) {
/* 38 */     return state.isBaby ? this.babyTexture : this.adultTexture;
/*    */   }
/*    */   
/*    */   public DonkeyRenderState createRenderState() {
/* 43 */     return new DonkeyRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(T entity, DonkeyRenderState state, float partialTicks) {
/* 48 */     super.extractRenderState(entity, state, partialTicks);
/* 49 */     state.hasChest = entity.hasChest();
/*    */   }
/*    */   
/*    */   public enum Type {
/* 53 */     DONKEY(
/* 54 */       (String)Identifier.withDefaultNamespace("textures/entity/horse/donkey.png"), ModelLayers.DONKEY),
/* 57 */     DONKEY_BABY(
/* 58 */       (String)Identifier.withDefaultNamespace("textures/entity/horse/donkey_baby.png"), ModelLayers.DONKEY_BABY),
/* 61 */     MULE(
/* 62 */       (String)Identifier.withDefaultNamespace("textures/entity/horse/mule.png"), ModelLayers.MULE),
/* 65 */     MULE_BABY(
/* 66 */       (String)Identifier.withDefaultNamespace("textures/entity/horse/mule_baby.png"), ModelLayers.MULE_BABY);
/*    */     
/*    */     private final Identifier texture;
/*    */     
/*    */     private final ModelLayerLocation model;
/*    */     
/*    */     Type(Identifier texture, ModelLayerLocation model) {
/* 74 */       this.texture = texture;
/* 75 */       this.model = model;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\DonkeyRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */