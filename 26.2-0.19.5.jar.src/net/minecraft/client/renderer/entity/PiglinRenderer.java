/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.monster.piglin.AdultPiglinModel;
/*    */ import net.minecraft.client.model.monster.piglin.BabyPiglinModel;
/*    */ import net.minecraft.client.model.monster.piglin.PiglinModel;
/*    */ import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.PiglinRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
/*    */ import net.minecraft.world.item.CrossbowItem;
/*    */ 
/*    */ public class PiglinRenderer extends HumanoidMobRenderer<AbstractPiglin, PiglinRenderState, PiglinModel> {
/* 16 */   private static final Identifier PIGLIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/piglin/piglin.png");
/*    */   
/* 17 */   private static final Identifier PIGLIN_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/piglin/piglin_baby.png");
/*    */   
/* 18 */   private static final Identifier PIGLIN_BRUTE_LOCATION = Identifier.withDefaultNamespace("textures/entity/piglin/piglin_brute.png");
/*    */   
/* 21 */   public static final CustomHeadLayer.Transforms PIGLIN_CUSTOM_HEAD_TRANSFORMS = new CustomHeadLayer.Transforms(0.0F, 0.0F, 1.0019531F);
/*    */   
/*    */   public PiglinRenderer(EntityRendererProvider.Context context, ModelLayerLocation body, ModelLayerLocation babyBody, ArmorModelSet<ModelLayerLocation> armorSet, ArmorModelSet<ModelLayerLocation> babyArmorSet) {
/* 24 */     super(context, new AdultPiglinModel(context.bakeLayer(body)), new BabyPiglinModel(context.bakeLayer(babyBody)), 0.5F, PIGLIN_CUSTOM_HEAD_TRANSFORMS);
/* 26 */     addLayer((RenderLayer<PiglinRenderState, PiglinModel>)new HumanoidArmorLayer(this, 
/* 27 */           ArmorModelSet.bake(armorSet, context.getModelSet(), AdultPiglinModel::new), 
/* 28 */           ArmorModelSet.bake(babyArmorSet, context.getModelSet(), BabyPiglinModel::new), context
/* 29 */           .getEquipmentRenderer()));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(PiglinRenderState state) {
/* 35 */     return state.isBrute ? PIGLIN_BRUTE_LOCATION : (state.isBaby ? PIGLIN_BABY_LOCATION : PIGLIN_LOCATION);
/*    */   }
/*    */   
/*    */   public PiglinRenderState createRenderState() {
/* 40 */     return new PiglinRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(AbstractPiglin entity, PiglinRenderState state, float partialTicks) {
/* 45 */     super.extractRenderState(entity, state, partialTicks);
/* 46 */     state.isBrute = entity.is(EntityTypes.PIGLIN_BRUTE);
/* 47 */     state.armPose = entity.getArmPose();
/* 48 */     state.maxCrossbowChageDuration = CrossbowItem.getChargeDuration(entity.getUseItem(), (LivingEntity)entity);
/* 49 */     state.isConverting = entity.isConverting();
/*    */   }
/*    */   
/*    */   protected boolean isShaking(PiglinRenderState state) {
/* 54 */     return (super.isShaking(state) || state.isConverting);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\PiglinRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */