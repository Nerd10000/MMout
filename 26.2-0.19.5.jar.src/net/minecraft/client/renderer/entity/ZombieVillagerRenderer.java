/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.zombie.BabyZombieVillagerModel;
/*    */ import net.minecraft.client.model.monster.zombie.ZombieVillagerModel;
/*    */ import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieVillagerRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.monster.zombie.ZombieVillager;
/*    */ 
/*    */ public class ZombieVillagerRenderer extends HumanoidMobRenderer<ZombieVillager, ZombieVillagerRenderState, ZombieVillagerModel<ZombieVillagerRenderState>> {
/* 14 */   private static final Identifier ZOMBIE_VILLAGER_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie_villager/zombie_villager.png");
/*    */   
/* 15 */   private static final Identifier BABY_ZOMBIE_VILLAGER_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie_villager/zombie_villager_baby.png");
/*    */   
/*    */   public ZombieVillagerRenderer(EntityRendererProvider.Context context) {
/* 18 */     super(context, new ZombieVillagerModel(context
/*    */           
/* 20 */           .bakeLayer(ModelLayers.ZOMBIE_VILLAGER)), new BabyZombieVillagerModel(context
/* 21 */           .bakeLayer(ModelLayers.ZOMBIE_VILLAGER_BABY)), 0.5F, VillagerRenderer.CUSTOM_HEAD_TRANSFORMS);
/* 26 */     addLayer((RenderLayer<ZombieVillagerRenderState, ZombieVillagerModel<ZombieVillagerRenderState>>)new HumanoidArmorLayer(this, 
/* 27 */           ArmorModelSet.bake(ModelLayers.ZOMBIE_VILLAGER_ARMOR, context.getModelSet(), ZombieVillagerModel::new), 
/* 28 */           ArmorModelSet.bake(ModelLayers.ZOMBIE_VILLAGER_BABY_ARMOR, context.getModelSet(), BabyZombieVillagerModel::new), context
/* 29 */           .getEquipmentRenderer()));
/* 31 */     addLayer((RenderLayer<ZombieVillagerRenderState, ZombieVillagerModel<ZombieVillagerRenderState>>)new VillagerProfessionLayer(this, context
/* 32 */           .getResourceManager(), "zombie_villager", (EntityModel)new ZombieVillagerModel(context
/*    */             
/* 34 */             .bakeLayer(ModelLayers.ZOMBIE_VILLAGER_NO_HAT)), (EntityModel)new BabyZombieVillagerModel(context
/* 35 */             .bakeLayer(ModelLayers.ZOMBIE_VILLAGER_BABY_NO_HAT))));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(ZombieVillagerRenderState state) {
/* 41 */     return state.isBaby ? BABY_ZOMBIE_VILLAGER_LOCATION : ZOMBIE_VILLAGER_LOCATION;
/*    */   }
/*    */   
/*    */   public ZombieVillagerRenderState createRenderState() {
/* 46 */     return new ZombieVillagerRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(ZombieVillager entity, ZombieVillagerRenderState state, float partialTicks) {
/* 51 */     super.extractRenderState(entity, state, partialTicks);
/* 52 */     state.isConverting = entity.isConverting();
/* 53 */     state.villagerData = entity.getVillagerData();
/* 54 */     state.isAggressive = entity.isAggressive();
/*    */   }
/*    */   
/*    */   protected boolean isShaking(ZombieVillagerRenderState state) {
/* 59 */     return (super.isShaking(state) || state.isConverting);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\ZombieVillagerRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */