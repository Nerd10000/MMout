/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.RenderShape;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class FallingBlockRenderer extends EntityRenderer<FallingBlockEntity, FallingBlockRenderState> {
/*    */   public FallingBlockRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context);
/* 17 */     this.shadowRadius = 0.5F;
/*    */   }
/*    */   
/*    */   public boolean shouldRender(FallingBlockEntity entity, Frustum culler, double camX, double camY, double camZ) {
/* 22 */     if (!super.shouldRender(entity, culler, camX, camY, camZ))
/* 23 */       return false; 
/* 25 */     return (entity.getBlockState() != entity.level().getBlockState(entity.blockPosition()));
/*    */   }
/*    */   
/*    */   public void submit(FallingBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 30 */     BlockState blockState = state.movingBlockRenderState.blockState;
/* 31 */     if (blockState.getRenderShape() != RenderShape.MODEL)
/*    */       return; 
/* 35 */     poseStack.pushPose();
/* 36 */     poseStack.translate(-0.5D, 0.0D, -0.5D);
/* 37 */     submitNodeCollector.submitMovingBlock(poseStack, state.movingBlockRenderState, state.outlineColor);
/* 38 */     poseStack.popPose();
/* 40 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   public FallingBlockRenderState createRenderState() {
/* 45 */     return new FallingBlockRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(FallingBlockEntity entity, FallingBlockRenderState state, float partialTicks) {
/* 50 */     super.extractRenderState(entity, state, partialTicks);
/* 53 */     BlockPos pos = BlockPos.containing(entity.getX(), (entity.getBoundingBox()).maxY, entity.getZ());
/* 54 */     state.movingBlockRenderState.randomSeedPos = entity.getStartPos();
/* 55 */     state.movingBlockRenderState.blockPos = pos;
/* 56 */     state.movingBlockRenderState.blockState = entity.getBlockState();
/* 57 */     Level level = entity.level();
/* 57 */     if (level instanceof ClientLevel) {
/* 57 */       ClientLevel clientLevel = (ClientLevel)level;
/* 58 */       state.movingBlockRenderState.biome = clientLevel.getBiome(pos);
/* 59 */       state.movingBlockRenderState.cardinalLighting = clientLevel.cardinalLighting();
/* 60 */       state.movingBlockRenderState.lightEngine = clientLevel.getLightEngine();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\FallingBlockRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */