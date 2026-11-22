/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.AdultAndBabyModelPair;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.chicken.AdultChickenModel;
/*    */ import net.minecraft.client.model.animal.chicken.BabyChickenModel;
/*    */ import net.minecraft.client.model.animal.chicken.ChickenModel;
/*    */ import net.minecraft.client.model.animal.chicken.ColdChickenModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.ChickenRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.chicken.Chicken;
/*    */ import net.minecraft.world.entity.animal.chicken.ChickenVariant;
/*    */ 
/*    */ public class ChickenRenderer extends MobRenderer<Chicken, ChickenRenderState, ChickenModel> {
/*    */   private final Map<ChickenVariant.ModelType, AdultAndBabyModelPair<ChickenModel>> models;
/*    */   
/*    */   public ChickenRenderer(EntityRendererProvider.Context context) {
/* 26 */     super(context, new AdultChickenModel(context.bakeLayer(ModelLayers.CHICKEN)), 0.3F);
/* 27 */     this.models = bakeModels(context);
/*    */   }
/*    */   
/*    */   private static Map<ChickenVariant.ModelType, AdultAndBabyModelPair<ChickenModel>> bakeModels(EntityRendererProvider.Context context) {
/* 31 */     return Maps.newEnumMap(Map.of(ChickenVariant.ModelType.NORMAL, new AdultAndBabyModelPair((Model)new AdultChickenModel(context
/* 32 */               .bakeLayer(ModelLayers.CHICKEN)), (Model)new BabyChickenModel(context.bakeLayer(ModelLayers.CHICKEN_BABY))), ChickenVariant.ModelType.COLD, new AdultAndBabyModelPair((Model)new ColdChickenModel(context
/* 33 */               .bakeLayer(ModelLayers.COLD_CHICKEN)), (Model)new BabyChickenModel(context.bakeLayer(ModelLayers.CHICKEN_BABY)))));
/*    */   }
/*    */   
/*    */   public void submit(ChickenRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 39 */     if (state.variant == null)
/*    */       return; 
/* 42 */     this.model = (ChickenModel)((AdultAndBabyModelPair)this.models.get(state.variant.modelAndTexture().model())).getModel(state.isBaby);
/* 43 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(ChickenRenderState state) {
/* 48 */     return (state.variant == null) ? MissingTextureAtlasSprite.getLocation() : (state.isBaby ? state.variant.babyTexture().texturePath() : state.variant.modelAndTexture().asset().texturePath());
/*    */   }
/*    */   
/*    */   public ChickenRenderState createRenderState() {
/* 53 */     return new ChickenRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Chicken entity, ChickenRenderState state, float partialTicks) {
/* 58 */     super.extractRenderState(entity, state, partialTicks);
/* 59 */     state.flap = Mth.lerp(partialTicks, entity.oFlap, entity.flap);
/* 60 */     state.flapSpeed = Mth.lerp(partialTicks, entity.oFlapSpeed, entity.flapSpeed);
/* 61 */     state.variant = (ChickenVariant)entity.getVariant().value();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\ChickenRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */