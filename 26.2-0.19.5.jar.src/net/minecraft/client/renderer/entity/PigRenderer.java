/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.AdultAndBabyModelPair;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.pig.BabyPigModel;
/*    */ import net.minecraft.client.model.animal.pig.ColdPigModel;
/*    */ import net.minecraft.client.model.animal.pig.PigModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.PigRenderState;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.pig.Pig;
/*    */ import net.minecraft.world.entity.animal.pig.PigVariant;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class PigRenderer extends MobRenderer<Pig, PigRenderState, PigModel> {
/*    */   private final Map<PigVariant.ModelType, AdultAndBabyModelPair<PigModel>> models;
/*    */   
/*    */   public PigRenderer(EntityRendererProvider.Context context) {
/* 27 */     super(context, new PigModel(context.bakeLayer(ModelLayers.PIG)), 0.7F);
/* 28 */     this.models = bakeModels(context);
/* 29 */     addLayer((RenderLayer<PigRenderState, PigModel>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.PIG_SADDLE, state -> state.saddle, (EntityModel)new PigModel(context
/*    */             
/* 32 */             .bakeLayer(ModelLayers.PIG_SADDLE)), null));
/*    */   }
/*    */   
/*    */   private static Map<PigVariant.ModelType, AdultAndBabyModelPair<PigModel>> bakeModels(EntityRendererProvider.Context context) {
/* 38 */     return Maps.newEnumMap(Map.of(PigVariant.ModelType.NORMAL, new AdultAndBabyModelPair((Model)new PigModel(context
/* 39 */               .bakeLayer(ModelLayers.PIG)), (Model)new BabyPigModel(context.bakeLayer(ModelLayers.PIG_BABY))), PigVariant.ModelType.COLD, new AdultAndBabyModelPair((Model)new ColdPigModel(context
/* 40 */               .bakeLayer(ModelLayers.COLD_PIG)), (Model)new BabyPigModel(context.bakeLayer(ModelLayers.PIG_BABY)))));
/*    */   }
/*    */   
/*    */   public void submit(PigRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 46 */     if (state.variant == null)
/*    */       return; 
/* 49 */     this.model = (PigModel)((AdultAndBabyModelPair)this.models.get(state.variant.modelAndTexture().model())).getModel(state.isBaby);
/* 50 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(PigRenderState state) {
/* 55 */     return (state.variant == null) ? MissingTextureAtlasSprite.getLocation() : (state.isBaby ? state.variant.babyTexture().texturePath() : state.variant.modelAndTexture().asset().texturePath());
/*    */   }
/*    */   
/*    */   public PigRenderState createRenderState() {
/* 60 */     return new PigRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Pig entity, PigRenderState state, float partialTicks) {
/* 65 */     super.extractRenderState(entity, state, partialTicks);
/* 66 */     state.saddle = entity.getItemBySlot(EquipmentSlot.SADDLE).copy();
/* 67 */     state.variant = (PigVariant)entity.getVariant().value();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\PigRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */