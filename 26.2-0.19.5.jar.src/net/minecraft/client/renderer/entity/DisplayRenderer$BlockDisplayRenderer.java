/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.state.BlockDisplayEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.DisplayEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.Display;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class BlockDisplayRenderer extends DisplayRenderer<Display.BlockDisplay, Display.BlockDisplay.BlockRenderState, BlockDisplayEntityRenderState> {
/*     */   protected BlockDisplayRenderer(EntityRendererProvider.Context context) {
/* 160 */     super(context);
/*     */   }
/*     */   
/*     */   public BlockDisplayEntityRenderState createRenderState() {
/* 165 */     return new BlockDisplayEntityRenderState();
/*     */   }
/*     */   
/*     */   public void extractRenderState(Display.BlockDisplay entity, BlockDisplayEntityRenderState state, float partialTicks) {
/* 170 */     super.extractRenderState(entity, state, partialTicks);
/* 171 */     Display.BlockDisplay.BlockRenderState blockRenderState = entity.blockRenderState();
/* 172 */     if (blockRenderState != null) {
/* 173 */       this.blockModelResolver.update(state.blockModel, blockRenderState.blockState(), BLOCK_DISPLAY_CONTEXT);
/*     */     } else {
/* 175 */       state.blockModel.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void submitInner(BlockDisplayEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float interpolationProgress) {
/* 181 */     state.blockModel.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\DisplayRenderer$BlockDisplayRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */