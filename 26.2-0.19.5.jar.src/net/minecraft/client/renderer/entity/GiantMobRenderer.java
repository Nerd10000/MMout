/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.zombie.GiantZombieModel;
/*    */ import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Giant;
/*    */ 
/*    */ public class GiantMobRenderer extends MobRenderer<Giant, ZombieRenderState, HumanoidModel<ZombieRenderState>> {
/* 13 */   private static final Identifier ZOMBIE_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/zombie.png");
/*    */   
/*    */   public GiantMobRenderer(EntityRendererProvider.Context context, float scale) {
/* 16 */     super(context, new GiantZombieModel(context.bakeLayer(ModelLayers.GIANT)), 0.5F * scale);
/* 18 */     addLayer((RenderLayer<ZombieRenderState, HumanoidModel<ZombieRenderState>>)new ItemInHandLayer(this));
/* 19 */     addLayer((RenderLayer<ZombieRenderState, HumanoidModel<ZombieRenderState>>)new HumanoidArmorLayer(this, 
/* 20 */           ArmorModelSet.bake(ModelLayers.GIANT_ARMOR, context.getModelSet(), GiantZombieModel::new), context
/* 21 */           .getEquipmentRenderer()));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(ZombieRenderState state) {
/* 27 */     return ZOMBIE_LOCATION;
/*    */   }
/*    */   
/*    */   public ZombieRenderState createRenderState() {
/* 32 */     return new ZombieRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Giant entity, ZombieRenderState state, float partialTicks) {
/* 37 */     super.extractRenderState(entity, state, partialTicks);
/* 38 */     HumanoidMobRenderer.extractHumanoidRenderState((LivingEntity)entity, (HumanoidRenderState)state, partialTicks, this.itemModelResolver);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\GiantMobRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */