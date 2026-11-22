/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.slime.SmallSulfurCubeModel;
/*    */ import net.minecraft.client.model.monster.slime.SulfurCubeModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.block.BlockModelResolver;
/*    */ import net.minecraft.client.renderer.block.model.BlockDisplayContext;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SulfurCubeInnerLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SlimeRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SulfurCubeRenderState;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
/*    */ import net.minecraft.world.entity.monster.cubemob.SulfurCube;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SulfurCubeRenderer extends AbstractCubeMobRenderer<SulfurCube, SulfurCubeRenderState, SulfurCubeModel> {
/* 23 */   private static final Identifier SULFUR_CUBE_LOCATION = Identifier.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_outer.png");
/*    */   
/* 24 */   private static final Identifier SULFUR_CUBE_SMALL_LOCATION = Identifier.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_outer_small.png");
/*    */   
/* 26 */   public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
/*    */   
/*    */   private final SulfurCubeModel normalModel;
/*    */   
/*    */   private final SmallSulfurCubeModel smallModel;
/*    */   
/*    */   private final BlockModelResolver blockModelResolver;
/*    */   
/*    */   public SulfurCubeRenderer(EntityRendererProvider.Context context) {
/* 34 */     super(context, normalModel);
/* 36 */     this.normalModel = normalModel;
/* 37 */     this.smallModel = new SmallSulfurCubeModel(context.bakeLayer(ModelLayers.SULFUR_CUBE_SMALL));
/* 38 */     this.blockModelResolver = context.getBlockModelResolver();
/* 40 */     addLayer((RenderLayer<SulfurCubeRenderState, SulfurCubeModel>)new SulfurCubeInnerLayer(this, context.getModelSet()));
/*    */   }
/*    */   
/*    */   public void submit(SulfurCubeRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 45 */     this.model = state.isBaby ? (SulfurCubeModel)this.smallModel : this.normalModel;
/* 46 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   protected void scale(SulfurCubeRenderState state, PoseStack poseStack) {
/* 51 */     downscaleSlightly(poseStack);
/* 52 */     super.scale(state, poseStack);
/* 54 */     float fuse = state.fuseRemainingTicks;
/* 55 */     if (fuse < 10.0F && fuse > 0.0F) {
/* 56 */       float s = 1.0F + TntRenderer.getSwellAmount(fuse);
/* 57 */       poseStack.scale(s, s, s);
/*    */     } 
/* 60 */     float vOffset = state.isBaby ? 1.24F : 0.98F;
/* 61 */     float extraDownscale = state.isBaby ? 1.0F : 0.5F;
/* 62 */     float onePixelUpIfVisible = (state.isInvisible ? 0.0F : 1.0F) / 16.0F;
/* 64 */     poseStack.scale(extraDownscale, extraDownscale, extraDownscale);
/* 65 */     poseStack.translate(-0.0F, vOffset - onePixelUpIfVisible, -0.0F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(SulfurCubeRenderState state) {
/* 70 */     return state.isBaby ? SULFUR_CUBE_SMALL_LOCATION : SULFUR_CUBE_LOCATION;
/*    */   }
/*    */   
/*    */   public SulfurCubeRenderState createRenderState() {
/* 75 */     return new SulfurCubeRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(SulfurCube entity, SulfurCubeRenderState state, float partialTicks) {
/* 80 */     super.extractRenderState(entity, state, partialTicks);
/* 82 */     state.fuseRemainingTicks = entity.isPrimed() ? (entity.getFuse() - partialTicks + 1.0F) : 0.0F;
/* 83 */     ItemStack containedBlock = entity.getBodyArmorItem();
/* 84 */     if (!containedBlock.isEmpty()) {
/* 85 */       BlockItemStateProperties blockItemState = (BlockItemStateProperties)containedBlock.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
/* 86 */       BlockState blockState = blockItemState.apply(Block.byItem(containedBlock.getItem()).defaultBlockState());
/* 87 */       this.blockModelResolver.update(state.containedBlock, blockState, BLOCK_DISPLAY_CONTEXT);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void applySizeAndSquish(SulfurCubeRenderState state, PoseStack poseStack) {
/* 93 */     float size = state.size;
/* 94 */     float ss = state.containedBlock.isEmpty() ? (state.squish / (size * 0.5F + 1.0F)) : 0.0F;
/* 95 */     float w = 1.0F / (ss + 1.0F);
/* 96 */     poseStack.scale(w * size, 1.0F / w * size, w * size);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\SulfurCubeRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */