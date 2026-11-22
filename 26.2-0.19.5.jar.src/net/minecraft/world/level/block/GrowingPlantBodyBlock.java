/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class GrowingPlantBodyBlock extends GrowingPlantBlock implements BonemealableBlock {
/*     */   protected GrowingPlantBodyBlock(BlockBehaviour.Properties properties, Direction growthDirection, VoxelShape shape, boolean scheduleFluidTicks) {
/*  23 */     super(properties, growthDirection, shape, scheduleFluidTicks);
/*     */   }
/*     */   
/*     */   protected abstract MapCodec<? extends GrowingPlantBodyBlock> codec();
/*     */   
/*     */   protected BlockState updateHeadAfterConvertedFromBody(BlockState bodyState, BlockState headState) {
/*  31 */     return headState;
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  36 */     if (directionToNeighbour == this.growthDirection.getOpposite() && !state.canSurvive(level, pos))
/*  37 */       ticks.scheduleTick(pos, this, 1); 
/*  40 */     GrowingPlantHeadBlock headBlock = getHeadBlock();
/*  41 */     if (directionToNeighbour == this.growthDirection && 
/*  42 */       !neighbourState.is(this) && !neighbourState.is(headBlock))
/*  44 */       return updateHeadAfterConvertedFromBody(state, headBlock.getStateForPlacement(random)); 
/*  48 */     if (this.scheduleFluidTicks)
/*  49 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/*  52 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/*  57 */     return new ItemStack(getHeadBlock());
/*     */   }
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/*  62 */     Optional<BlockPos> headPos = getHeadPos((BlockGetter)level, pos, state.getBlock());
/*  63 */     if (headPos.isEmpty())
/*  64 */       return false; 
/*  66 */     BlockPos growthPos = ((BlockPos)headPos.get()).relative(this.growthDirection);
/*  67 */     return (getHeadBlock().canGrowInto(level.getBlockState(growthPos)) && level.isInsideBuildHeight(growthPos));
/*     */   }
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/*  72 */     return true;
/*     */   }
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/*  77 */     Optional<BlockPos> headPos = getHeadPos((BlockGetter)level, pos, state.getBlock());
/*  79 */     if (headPos.isPresent()) {
/*  80 */       BlockState forwardState = level.getBlockState(headPos.get());
/*  81 */       ((GrowingPlantHeadBlock)forwardState.getBlock()).performBonemeal(level, random, headPos.get(), forwardState);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> getHeadPos(BlockGetter level, BlockPos pos, Block bodyBlock) {
/*  86 */     return BlockUtil.getTopConnectedBlock(level, pos, bodyBlock, this.growthDirection, getHeadBlock());
/*     */   }
/*     */   
/*     */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/*  91 */     boolean result = super.canBeReplaced(state, context);
/*  92 */     if (result && context.getItemInHand().is(getHeadBlock().asItem()))
/*  93 */       return false; 
/*  95 */     return result;
/*     */   }
/*     */   
/*     */   protected Block getBodyBlock() {
/* 100 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\GrowingPlantBodyBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */