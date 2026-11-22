/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ import net.minecraft.world.level.BlockAndLightGetter;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockEntityRenderState {
/* 15 */   public BlockPos blockPos = BlockPos.ZERO;
/*    */   
/* 17 */   private BlockState blockState = Blocks.AIR.defaultBlockState();
/*    */   
/* 18 */   public BlockEntityType<?> blockEntityType = BlockEntityTypes.TEST_BLOCK;
/*    */   
/*    */   public int lightCoords;
/*    */   
/*    */   public ModelFeatureRenderer.CrumblingOverlay breakProgress;
/*    */   
/*    */   public static void extractBase(BlockEntity blockEntity, BlockEntityRenderState state, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 23 */     state.blockPos = blockEntity.getBlockPos();
/* 24 */     state.blockState = blockEntity.getBlockState();
/* 25 */     state.blockEntityType = blockEntity.getType();
/* 26 */     state.lightCoords = (blockEntity.getLevel() != null) ? LightCoordsUtil.getLightCoords((BlockAndLightGetter)blockEntity.getLevel(), blockEntity.getBlockPos()) : 15728880;
/* 27 */     state.breakProgress = breakProgress;
/*    */   }
/*    */   
/*    */   public void fillCrashReportCategory(CrashReportCategory category) {
/* 31 */     category.setDetail("BlockEntityRenderState", getClass().getCanonicalName());
/* 32 */     category.setDetail("Position", this.blockPos);
/* 33 */     Objects.requireNonNull(this.blockState);
/* 33 */     category.setDetail("Block state", this.blockState::toString);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\state\BlockEntityRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */