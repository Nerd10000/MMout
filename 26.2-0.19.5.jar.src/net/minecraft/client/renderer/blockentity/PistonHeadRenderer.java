/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.block.MovingBlockRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.PistonHeadRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.piston.PistonBaseBlock;
/*    */ import net.minecraft.world.level.block.piston.PistonHeadBlock;
/*    */ import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.PistonType;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class PistonHeadRenderer implements BlockEntityRenderer<PistonMovingBlockEntity, PistonHeadRenderState> {
/*    */   public PistonHeadRenderState createRenderState() {
/* 26 */     return new PistonHeadRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(PistonMovingBlockEntity blockEntity, PistonHeadRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 31 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 33 */     state.xOffset = blockEntity.getXOff(partialTicks);
/* 34 */     state.yOffset = blockEntity.getYOff(partialTicks);
/* 35 */     state.zOffset = blockEntity.getZOff(partialTicks);
/* 36 */     state.block = null;
/* 37 */     state.base = null;
/* 39 */     BlockState blockState = blockEntity.getMovedState();
/* 41 */     Level level = blockEntity.getLevel();
/* 41 */     if (level instanceof ClientLevel) {
/* 41 */       ClientLevel clientLevel = (ClientLevel)level;
/* 41 */       if (!blockState.isAir()) {
/* 42 */         BlockPos pos = blockEntity.getBlockPos().relative(blockEntity.getMovementDirection().getOpposite());
/* 43 */         Holder<Biome> biome = clientLevel.getBiome(pos);
/* 45 */         if (blockState.is(Blocks.PISTON_HEAD) && blockEntity.getProgress(partialTicks) <= 4.0F) {
/* 47 */           blockState = (BlockState)blockState.setValue((Property)PistonHeadBlock.SHORT, Boolean.valueOf((blockEntity.getProgress(partialTicks) <= 0.5F)));
/* 48 */           state.block = createMovingBlock(pos, blockState, biome, clientLevel);
/* 49 */         } else if (blockEntity.isSourcePiston() && !blockEntity.isExtending()) {
/* 51 */           PistonType value = blockState.is(Blocks.STICKY_PISTON) ? PistonType.STICKY : PistonType.DEFAULT;
/* 52 */           BlockState pistonHeadState = (BlockState)((BlockState)Blocks.PISTON_HEAD.defaultBlockState().setValue((Property)PistonHeadBlock.TYPE, (Comparable)value)).setValue((Property)PistonHeadBlock.FACING, blockState.getValue((Property)PistonBaseBlock.FACING));
/* 53 */           pistonHeadState = (BlockState)pistonHeadState.setValue((Property)PistonHeadBlock.SHORT, Boolean.valueOf((blockEntity.getProgress(partialTicks) >= 0.5F)));
/* 54 */           state.block = createMovingBlock(pos, pistonHeadState, biome, clientLevel);
/* 56 */           BlockPos basePos = pos.relative(blockEntity.getMovementDirection());
/* 57 */           blockState = (BlockState)blockState.setValue((Property)PistonBaseBlock.EXTENDED, Boolean.valueOf(true));
/* 58 */           state.base = createMovingBlock(basePos, blockState, biome, clientLevel);
/*    */         } else {
/* 60 */           state.block = createMovingBlock(pos, blockState, biome, clientLevel);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void submit(PistonHeadRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 67 */     if (state.block == null)
/*    */       return; 
/* 71 */     poseStack.pushPose();
/* 72 */     poseStack.translate(state.xOffset, state.yOffset, state.zOffset);
/* 73 */     submitNodeCollector.submitMovingBlock(poseStack, state.block, 0);
/* 74 */     poseStack.popPose();
/* 75 */     if (state.base != null)
/* 76 */       submitNodeCollector.submitMovingBlock(poseStack, state.base, 0); 
/*    */   }
/*    */   
/*    */   private static MovingBlockRenderState createMovingBlock(BlockPos pos, BlockState blockState, Holder<Biome> biome, ClientLevel level) {
/* 81 */     MovingBlockRenderState movingBlockRenderState = new MovingBlockRenderState();
/* 82 */     movingBlockRenderState.randomSeedPos = pos;
/* 83 */     movingBlockRenderState.blockPos = pos;
/* 84 */     movingBlockRenderState.blockState = blockState;
/* 85 */     movingBlockRenderState.biome = biome;
/* 86 */     movingBlockRenderState.cardinalLighting = level.cardinalLighting();
/* 87 */     movingBlockRenderState.lightEngine = level.getLightEngine();
/* 88 */     return movingBlockRenderState;
/*    */   }
/*    */   
/*    */   public int getViewDistance() {
/* 94 */     return 68;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\PistonHeadRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */