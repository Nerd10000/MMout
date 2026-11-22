/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.npc.BabyVillagerModel;
/*    */ import net.minecraft.client.model.npc.VillagerModel;
/*    */ import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.VillagerRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ 
/*    */ public class VillagerRenderer extends AgeableMobRenderer<Villager, VillagerRenderState, VillagerModel> {
/* 16 */   private static final Identifier VILLAGER_BASE_LOCATION = Identifier.withDefaultNamespace("textures/entity/villager/villager.png");
/*    */   
/* 17 */   private static final Identifier VILLAGER_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/villager/villager_baby.png");
/*    */   
/* 19 */   public static final CustomHeadLayer.Transforms CUSTOM_HEAD_TRANSFORMS = new CustomHeadLayer.Transforms(-0.1171875F, -0.07421875F, 1.0F);
/*    */   
/*    */   public VillagerRenderer(EntityRendererProvider.Context context) {
/* 22 */     super(context, new VillagerModel(context.bakeLayer(ModelLayers.VILLAGER)), new BabyVillagerModel(context.bakeLayer(ModelLayers.VILLAGER_BABY)), 0.5F);
/* 24 */     addLayer((RenderLayer<VillagerRenderState, VillagerModel>)new CustomHeadLayer(this, context.getModelSet(), context.getPlayerSkinRenderCache(), CUSTOM_HEAD_TRANSFORMS));
/* 25 */     addLayer((RenderLayer<VillagerRenderState, VillagerModel>)new VillagerProfessionLayer(this, context.getResourceManager(), "villager", (EntityModel)new VillagerModel(context.bakeLayer(ModelLayers.VILLAGER_NO_HAT)), (EntityModel)new BabyVillagerModel(context.bakeLayer(ModelLayers.VILLAGER_BABY_NO_HAT))));
/* 26 */     addLayer((RenderLayer<VillagerRenderState, VillagerModel>)new CrossedArmsItemLayer(this));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(VillagerRenderState state) {
/* 31 */     return state.isBaby ? VILLAGER_BABY_LOCATION : VILLAGER_BASE_LOCATION;
/*    */   }
/*    */   
/*    */   protected float getShadowRadius(VillagerRenderState state) {
/* 36 */     float radius = super.getShadowRadius(state);
/* 37 */     if (state.isBaby)
/* 39 */       return radius * 0.5F; 
/* 41 */     return radius;
/*    */   }
/*    */   
/*    */   public VillagerRenderState createRenderState() {
/* 46 */     return new VillagerRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Villager entity, VillagerRenderState state, float partialTicks) {
/* 51 */     super.extractRenderState(entity, state, partialTicks);
/* 52 */     HoldingEntityRenderState.extractHoldingEntityRenderState((LivingEntity)entity, (HoldingEntityRenderState)state, this.itemModelResolver);
/* 53 */     state.isUnhappy = (entity.getUnhappyCounter() > 0);
/* 54 */     state.villagerData = entity.getVillagerData();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\VillagerRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */