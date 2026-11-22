/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ 
/*     */ public class ChorusPlantBlock extends PipeBlock {
/*  18 */   public static final MapCodec<ChorusPlantBlock> CODEC = simpleCodec(ChorusPlantBlock::new);
/*     */   
/*     */   public MapCodec<ChorusPlantBlock> codec() {
/*  22 */     return CODEC;
/*     */   }
/*     */   
/*     */   protected ChorusPlantBlock(BlockBehaviour.Properties properties) {
/*  26 */     super(10.0F, properties);
/*  28 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false))).setValue((Property)UP, Boolean.valueOf(false))).setValue((Property)DOWN, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  33 */     return getStateWithConnections((BlockGetter)context.getLevel(), context.getClickedPos(), defaultBlockState());
/*     */   }
/*     */   
/*     */   public static BlockState getStateWithConnections(BlockGetter level, BlockPos pos, BlockState defaultState) {
/*  37 */     BlockState down = level.getBlockState(pos.below());
/*  38 */     BlockState up = level.getBlockState(pos.above());
/*  39 */     BlockState north = level.getBlockState(pos.north());
/*  40 */     BlockState east = level.getBlockState(pos.east());
/*  41 */     BlockState south = level.getBlockState(pos.south());
/*  42 */     BlockState west = level.getBlockState(pos.west());
/*  44 */     Block block = defaultState.getBlock();
/*  45 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)defaultState
/*  46 */       .trySetValue((Property)DOWN, Boolean.valueOf((down.is(block) || down.is(Blocks.CHORUS_FLOWER) || down.is(BlockTags.SUPPORTS_CHORUS_PLANT)))))
/*  47 */       .trySetValue((Property)UP, Boolean.valueOf((up.is(block) || up.is(Blocks.CHORUS_FLOWER)))))
/*  48 */       .trySetValue((Property)NORTH, Boolean.valueOf((north.is(block) || north.is(Blocks.CHORUS_FLOWER)))))
/*  49 */       .trySetValue((Property)EAST, Boolean.valueOf((east.is(block) || east.is(Blocks.CHORUS_FLOWER)))))
/*  50 */       .trySetValue((Property)SOUTH, Boolean.valueOf((south.is(block) || south.is(Blocks.CHORUS_FLOWER)))))
/*  51 */       .trySetValue((Property)WEST, Boolean.valueOf((west.is(block) || west.is(Blocks.CHORUS_FLOWER))));
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  57 */     if (!state.canSurvive(level, pos)) {
/*  58 */       ticks.scheduleTick(pos, this, 1);
/*  59 */       return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */     } 
/*  62 */     boolean connect = (neighbourState.is(this) || neighbourState.is(Blocks.CHORUS_FLOWER) || (directionToNeighbour == Direction.DOWN && neighbourState.is(BlockTags.SUPPORTS_CHORUS_PLANT)));
/*  64 */     return (BlockState)state.setValue((Property)PROPERTY_BY_DIRECTION.get(directionToNeighbour), Boolean.valueOf(connect));
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  69 */     if (!state.canSurvive((LevelReader)level, pos))
/*  70 */       level.destroyBlock(pos, true); 
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  79 */     BlockState belowState = level.getBlockState(pos.below());
/*  80 */     boolean blockAboveOrBelow = (!level.getBlockState(pos.above()).isAir() && !belowState.isAir());
/*  82 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  83 */       BlockPos neighborPos = pos.relative(direction);
/*  84 */       BlockState neighborState = level.getBlockState(neighborPos);
/*  85 */       if (neighborState.is(this)) {
/*  86 */         if (blockAboveOrBelow)
/*  87 */           return false; 
/*  89 */         BlockState below = level.getBlockState(neighborPos.below());
/*  90 */         if (below.is(this) || below.is(BlockTags.SUPPORTS_CHORUS_PLANT))
/*  91 */           return true; 
/*     */       } 
/*     */     } 
/*  95 */     return (belowState.is(this) || belowState.is(BlockTags.SUPPORTS_CHORUS_PLANT));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 100 */     builder.add(new Property[] { (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST, (Property)UP, (Property)DOWN });
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 105 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ChorusPlantBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */