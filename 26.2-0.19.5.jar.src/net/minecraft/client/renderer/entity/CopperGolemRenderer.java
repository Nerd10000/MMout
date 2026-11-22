/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.golem.CopperGolemModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.block.BlockModelRenderState;
/*    */ import net.minecraft.client.renderer.block.BlockModelResolver;
/*    */ import net.minecraft.client.renderer.block.model.BlockDisplayContext;
/*    */ import net.minecraft.client.renderer.entity.layers.BlockDecorationLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.CopperGolemRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolem;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolemOxidationLevels;
/*    */ import net.minecraft.world.item.BlockItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class CopperGolemRenderer extends MobRenderer<CopperGolem, CopperGolemRenderState, CopperGolemModel> {
/* 26 */   public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
/*    */   
/*    */   private final BlockModelResolver blockModelResolver;
/*    */   
/*    */   public CopperGolemRenderer(EntityRendererProvider.Context context) {
/* 31 */     super(context, new CopperGolemModel(context.bakeLayer(ModelLayers.COPPER_GOLEM)), 0.5F);
/* 32 */     this.blockModelResolver = context.getBlockModelResolver();
/* 33 */     addLayer((RenderLayer<CopperGolemRenderState, CopperGolemModel>)new LivingEntityEmissiveLayer(this, getEyeTextureLocationProvider(), (copperGolem, ageInTicks) -> 1.0F, (EntityModel)new CopperGolemModel(context.bakeLayer(ModelLayers.COPPER_GOLEM)), RenderTypes::eyes, false));
/* 34 */     addLayer((RenderLayer<CopperGolemRenderState, CopperGolemModel>)new ItemInHandLayer(this));
/* 35 */     Objects.requireNonNull(this.model);
/* 35 */     addLayer((RenderLayer<CopperGolemRenderState, CopperGolemModel>)new BlockDecorationLayer(this, s -> s.blockOnAntenna, this.model::applyBlockOnAntennaTransform));
/* 36 */     addLayer((RenderLayer<CopperGolemRenderState, CopperGolemModel>)new CustomHeadLayer(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(CopperGolemRenderState state) {
/* 41 */     return CopperGolemOxidationLevels.getOxidationLevel(state.weathering).texture();
/*    */   }
/*    */   
/*    */   private static Function<CopperGolemRenderState, Identifier> getEyeTextureLocationProvider() {
/* 45 */     return renderState -> CopperGolemOxidationLevels.getOxidationLevel(renderState.weathering).eyeTexture();
/*    */   }
/*    */   
/*    */   public CopperGolemRenderState createRenderState() {
/* 50 */     return new CopperGolemRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(CopperGolem entity, CopperGolemRenderState state, float partialTicks) {
/* 55 */     super.extractRenderState(entity, state, partialTicks);
/* 56 */     ArmedEntityRenderState.extractArmedEntityRenderState((LivingEntity)entity, (ArmedEntityRenderState)state, this.itemModelResolver, partialTicks);
/* 57 */     state.weathering = entity.getWeatherState();
/* 58 */     state.copperGolemState = entity.getState();
/* 59 */     state.idleAnimationState.copyFrom(entity.getIdleAnimationState());
/* 60 */     state.interactionGetItem.copyFrom(entity.getInteractionGetItemAnimationState());
/* 61 */     state.interactionGetNoItem.copyFrom(entity.getInteractionGetNoItemAnimationState());
/* 62 */     state.interactionDropItem.copyFrom(entity.getInteractionDropItemAnimationState());
/* 63 */     state.interactionDropNoItem.copyFrom(entity.getInteractionDropNoItemAnimationState());
/* 65 */     ItemStack antennaItem = entity.getItemBySlot(CopperGolem.EQUIPMENT_SLOT_ANTENNA);
/* 66 */     Item item = antennaItem.getItem();
/* 66 */     if (item instanceof BlockItem) {
/* 66 */       BlockItem blockItem = (BlockItem)item;
/* 67 */       BlockItemStateProperties blockItemState = (BlockItemStateProperties)antennaItem.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
/* 68 */       BlockState blockState = blockItemState.apply(blockItem.getBlock().defaultBlockState());
/* 69 */       this.blockModelResolver.update(state.blockOnAntenna, blockState, BLOCK_DISPLAY_CONTEXT);
/*    */     } else {
/* 71 */       state.blockOnAntenna.clear();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\CopperGolemRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */