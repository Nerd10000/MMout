/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class GrowingPlantHeadBlock extends GrowingPlantBlock implements BonemealableBlock {
/*  19 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_25;
/*     */   
/*     */   public static final int MAX_AGE = 25;
/*     */   
/*     */   private final double growPerTickProbability;
/*     */   
/*     */   protected GrowingPlantHeadBlock(BlockBehaviour.Properties properties, Direction growthDirection, VoxelShape shape, boolean scheduleFluidTicks, double growPerTickProbability) {
/*  25 */     super(properties, growthDirection, shape, scheduleFluidTicks);
/*  26 */     this.growPerTickProbability = growPerTickProbability;
/*  27 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */   
/*     */   protected abstract MapCodec<? extends GrowingPlantHeadBlock> codec();
/*     */   
/*     */   public BlockState getStateForPlacement(RandomSource random) {
/*  35 */     return (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(random.nextInt(25)));
/*     */   }
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState state) {
/*  40 */     return (((Integer)state.getValue((Property)AGE)).intValue() < 25);
/*     */   }
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  45 */     if (((Integer)state.getValue((Property)AGE)).intValue() < 25 && random.nextDouble() < this.growPerTickProbability) {
/*  46 */       BlockPos growthPos = pos.relative(this.growthDirection);
/*  47 */       if (canGrowInto(level.getBlockState(growthPos)))
/*  48 */         level.setBlockAndUpdate(growthPos, getGrowIntoState(state, level.getRandom())); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BlockState getGrowIntoState(BlockState growFromState, RandomSource random) {
/*  54 */     return (BlockState)growFromState.cycle((Property)AGE);
/*     */   }
/*     */   
/*     */   public BlockState getMaxAgeState(BlockState fromState) {
/*  58 */     return (BlockState)fromState.setValue((Property)AGE, Integer.valueOf(25));
/*     */   }
/*     */   
/*     */   public boolean isMaxAge(BlockState state) {
/*  62 */     return (((Integer)state.getValue((Property)AGE)).intValue() == 25);
/*     */   }
/*     */   
/*     */   protected BlockState updateBodyAfterConvertedFromHead(BlockState headState, BlockState bodyState) {
/*  67 */     return bodyState;
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  72 */     if (directionToNeighbour == this.growthDirection.getOpposite())
/*  73 */       if (!state.canSurvive(level, pos)) {
/*  74 */         ticks.scheduleTick(pos, this, 1);
/*     */       } else {
/*  77 */         BlockState neighborInGrowthDirection = level.getBlockState(pos.relative(this.growthDirection));
/*  78 */         if (neighborInGrowthDirection.is(this) || neighborInGrowthDirection.is(getBodyBlock()))
/*  79 */           return updateBodyAfterConvertedFromHead(state, getBodyBlock().defaultBlockState()); 
/*     */       }  
/*  83 */     if (directionToNeighbour == this.growthDirection && (neighbourState.is(this) || neighbourState.is(getBodyBlock())))
/*  85 */       return updateBodyAfterConvertedFromHead(state, getBodyBlock().defaultBlockState()); 
/*  87 */     if (this.scheduleFluidTicks)
/*  88 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/*  91 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  96 */     builder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 101 */     BlockPos growthPos = pos.relative(this.growthDirection);
/* 102 */     return (canGrowInto(level.getBlockState(growthPos)) && level.isInsideBuildHeight(growthPos));
/*     */   }
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 107 */     return true;
/*     */   }
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 112 */     BlockPos forwardPos = pos.relative(this.growthDirection);
/* 113 */     int nextAge = Math.min(((Integer)state.getValue((Property)AGE)).intValue() + 1, 25);
/* 115 */     int blocksToGrow = getBlocksToGrowWhenBonemealed(random);
/* 116 */     for (int i = 0; i < blocksToGrow && 
/* 117 */       canGrowInto(level.getBlockState(forwardPos)) && !level.isOutsideBuildHeight(forwardPos); i++) {
/* 120 */       level.setBlockAndUpdate(forwardPos, (BlockState)state.setValue((Property)AGE, Integer.valueOf(nextAge)));
/* 122 */       forwardPos = forwardPos.relative(this.growthDirection);
/* 123 */       nextAge = Math.min(nextAge + 1, 25);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract int getBlocksToGrowWhenBonemealed(RandomSource paramRandomSource);
/*     */   
/*     */   protected abstract boolean canGrowInto(BlockState paramBlockState);
/*     */   
/*     */   protected GrowingPlantHeadBlock getHeadBlock() {
/* 133 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\GrowingPlantHeadBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */