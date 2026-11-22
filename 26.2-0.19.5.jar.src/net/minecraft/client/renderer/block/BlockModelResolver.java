/*    */ package net.minecraft.client.renderer.block;
/*    */ 
/*    */ import net.minecraft.client.renderer.block.model.BlockDisplayContext;
/*    */ import net.minecraft.client.renderer.entity.ItemFrameRenderer;
/*    */ import net.minecraft.client.resources.model.BlockStateDefinitions;
/*    */ import net.minecraft.client.resources.model.ModelManager;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockModelResolver {
/*    */   private static final long MODEL_SEED = 42L;
/*    */   
/*    */   private final ModelManager modelManager;
/*    */   
/*    */   public BlockModelResolver(ModelManager modelManager) {
/* 16 */     this.modelManager = modelManager;
/*    */   }
/*    */   
/*    */   public void update(BlockModelRenderState renderState, BlockState blockState, BlockDisplayContext displayContext) {
/* 20 */     renderState.clear();
/* 21 */     this.modelManager.getBlockModelSet().get(blockState).update(renderState, blockState, displayContext, 42L);
/* 22 */     renderState.blockLightCoords = blockState.emissiveRendering() ? 15728880 : LightCoordsUtil.pack(blockState.getLightEmission(), 0);
/*    */   }
/*    */   
/*    */   public void updateForItemFrame(BlockModelRenderState renderState, boolean isGlowing, boolean map) {
/* 26 */     BlockState fakeState = BlockStateDefinitions.getItemFrameFakeState(isGlowing, map);
/* 27 */     update(renderState, fakeState, ItemFrameRenderer.BLOCK_DISPLAY_CONTEXT);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\BlockModelResolver.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */