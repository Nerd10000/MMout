/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.equine.AbstractEquineModel;
/*    */ import net.minecraft.client.model.animal.equine.BabyHorseModel;
/*    */ import net.minecraft.client.model.animal.equine.EquineSaddleModel;
/*    */ import net.minecraft.client.model.animal.equine.HorseModel;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EquineRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class UndeadHorseRenderer extends AbstractHorseRenderer<AbstractHorse, EquineRenderState, AbstractEquineModel<EquineRenderState>> {
/*    */   private final Identifier adultTexture;
/*    */   
/*    */   private final Identifier babyTexture;
/*    */   
/*    */   public UndeadHorseRenderer(EntityRendererProvider.Context context, EquipmentClientInfo.LayerType saddleLayer, ModelLayerLocation saddleModel, Type adult, Type baby) {
/* 21 */     super(context, new HorseModel(context
/* 22 */           .bakeLayer(adult.model)), new BabyHorseModel(context
/* 23 */           .bakeLayer(baby.model)));
/* 26 */     this.adultTexture = adult.texture;
/* 27 */     this.babyTexture = baby.texture;
/* 29 */     addLayer((RenderLayer<EquineRenderState, AbstractEquineModel<EquineRenderState>>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.HORSE_BODY, state -> state.bodyArmorItem, (EntityModel)new HorseModel(context
/*    */             
/* 32 */             .bakeLayer(ModelLayers.UNDEAD_HORSE_ARMOR)), null));
/* 36 */     addLayer((RenderLayer<EquineRenderState, AbstractEquineModel<EquineRenderState>>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), saddleLayer, state -> state.saddle, (EntityModel)new EquineSaddleModel(context
/*    */             
/* 39 */             .bakeLayer(saddleModel)), null));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(EquineRenderState state) {
/* 46 */     return state.isBaby ? this.babyTexture : this.adultTexture;
/*    */   }
/*    */   
/*    */   public EquineRenderState createRenderState() {
/* 51 */     return new EquineRenderState();
/*    */   }
/*    */   
/*    */   public enum Type {
/* 55 */     SKELETON(
/* 56 */       (String)Identifier.withDefaultNamespace("textures/entity/horse/horse_skeleton.png"), ModelLayers.SKELETON_HORSE),
/* 59 */     SKELETON_BABY(
/* 60 */       (String)Identifier.withDefaultNamespace("textures/entity/horse/horse_skeleton_baby.png"), ModelLayers.SKELETON_HORSE_BABY),
/* 63 */     ZOMBIE(
/* 64 */       (String)Identifier.withDefaultNamespace("textures/entity/horse/horse_zombie.png"), ModelLayers.ZOMBIE_HORSE),
/* 67 */     ZOMBIE_BABY(
/* 68 */       (String)Identifier.withDefaultNamespace("textures/entity/horse/horse_zombie_baby.png"), ModelLayers.ZOMBIE_HORSE_BABY);
/*    */     
/*    */     private final Identifier texture;
/*    */     
/*    */     private final ModelLayerLocation model;
/*    */     
/*    */     Type(Identifier texture, ModelLayerLocation model) {
/* 77 */       this.texture = texture;
/* 78 */       this.model = model;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\UndeadHorseRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */