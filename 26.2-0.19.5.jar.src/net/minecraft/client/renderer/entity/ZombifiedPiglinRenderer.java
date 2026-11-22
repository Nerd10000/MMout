/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.monster.piglin.AdultZombifiedPiglinModel;
/*    */ import net.minecraft.client.model.monster.piglin.BabyZombifiedPiglinModel;
/*    */ import net.minecraft.client.model.monster.piglin.ZombifiedPiglinModel;
/*    */ import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombifiedPiglinRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
/*    */ 
/*    */ public class ZombifiedPiglinRenderer extends HumanoidMobRenderer<ZombifiedPiglin, ZombifiedPiglinRenderState, ZombifiedPiglinModel> {
/* 13 */   private static final Identifier ZOMBIFIED_PIGLIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/piglin/zombified_piglin.png");
/*    */   
/* 14 */   private static final Identifier BABY_ZOMBIFIED_PIGLIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/piglin/zombified_piglin_baby.png");
/*    */   
/*    */   public ZombifiedPiglinRenderer(EntityRendererProvider.Context context, ModelLayerLocation body, ModelLayerLocation babyBody, ArmorModelSet<ModelLayerLocation> armorSet, ArmorModelSet<ModelLayerLocation> babyArmorSet) {
/* 17 */     super(context, new AdultZombifiedPiglinModel(context.bakeLayer(body)), new BabyZombifiedPiglinModel(context.bakeLayer(babyBody)), 0.5F, PiglinRenderer.PIGLIN_CUSTOM_HEAD_TRANSFORMS);
/* 19 */     addLayer((RenderLayer<ZombifiedPiglinRenderState, ZombifiedPiglinModel>)new HumanoidArmorLayer(this, 
/* 20 */           ArmorModelSet.bake(armorSet, context.getModelSet(), AdultZombifiedPiglinModel::new), 
/* 21 */           ArmorModelSet.bake(babyArmorSet, context.getModelSet(), BabyZombifiedPiglinModel::new), context
/* 22 */           .getEquipmentRenderer()));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(ZombifiedPiglinRenderState state) {
/* 28 */     return state.isBaby ? BABY_ZOMBIFIED_PIGLIN_LOCATION : ZOMBIFIED_PIGLIN_LOCATION;
/*    */   }
/*    */   
/*    */   public ZombifiedPiglinRenderState createRenderState() {
/* 33 */     return new ZombifiedPiglinRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(ZombifiedPiglin entity, ZombifiedPiglinRenderState state, float partialTicks) {
/* 38 */     super.extractRenderState(entity, state, partialTicks);
/* 39 */     state.isAggressive = entity.isAggressive();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\ZombifiedPiglinRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */