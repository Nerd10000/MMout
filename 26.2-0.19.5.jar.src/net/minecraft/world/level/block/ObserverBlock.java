/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ 
/*     */ public class ObserverBlock extends DirectionalBlock {
/*  22 */   public static final MapCodec<ObserverBlock> CODEC = simpleCodec(ObserverBlock::new);
/*     */   
/*     */   public MapCodec<ObserverBlock> codec() {
/*  26 */     return CODEC;
/*     */   }
/*     */   
/*  29 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   public ObserverBlock(BlockBehaviour.Properties properties) {
/*  32 */     super(properties);
/*  34 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.SOUTH)).setValue((Property)POWERED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  39 */     builder.add(new Property[] { (Property)FACING, (Property)POWERED });
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/*  44 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/*  49 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  54 */     if (((Boolean)state.getValue((Property)POWERED)).booleanValue()) {
/*  55 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, Boolean.valueOf(false)), 2);
/*     */     } else {
/*  57 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, Boolean.valueOf(true)), 2);
/*  58 */       level.scheduleTick(pos, this, 2);
/*     */     } 
/*  60 */     updateNeighborsInFront((Level)level, pos, state);
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  65 */     if (state.getValue((Property)FACING) == directionToNeighbour && !((Boolean)state.getValue((Property)POWERED)).booleanValue())
/*  66 */       startSignal(level, ticks, pos); 
/*  69 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   private void startSignal(LevelReader level, ScheduledTickAccess ticks, BlockPos pos) {
/*  73 */     if (!level.isClientSide() && !ticks.getBlockTicks().hasScheduledTick(pos, this))
/*  74 */       ticks.scheduleTick(pos, this, 2); 
/*     */   }
/*     */   
/*     */   protected void updateNeighborsInFront(Level level, BlockPos pos, BlockState state) {
/*  79 */     Direction direction = (Direction)state.getValue((Property)FACING);
/*  80 */     BlockPos oppositePos = pos.relative(direction.getOpposite());
/*  82 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, direction.getOpposite(), null);
/*  83 */     level.neighborChanged(oppositePos, this, orientation);
/*  84 */     level.updateNeighborsAtExceptFromFacing(oppositePos, this, direction, orientation);
/*     */   }
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/*  89 */     return true;
/*     */   }
/*     */   
/*     */   protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
/*  94 */     return ((Boolean)state.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  99 */     return state.getSignal(level, pos, direction);
/*     */   }
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 104 */     return (state.getValue((Property)FACING) == direction) ? ownSignal(state, level, pos) : 0;
/*     */   }
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 109 */     if (state.is(oldState.getBlock()))
/*     */       return; 
/* 113 */     if (!level.isClientSide() && ((Boolean)state.getValue((Property)POWERED)).booleanValue() && !level.getBlockTicks().hasScheduledTick(pos, this)) {
/* 114 */       BlockState newState = (BlockState)state.setValue((Property)POWERED, Boolean.valueOf(false));
/* 116 */       level.setBlock(pos, newState, 18);
/* 117 */       updateNeighborsInFront(level, pos, newState);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 123 */     if (((Boolean)state.getValue((Property)POWERED)).booleanValue() && level.getBlockTicks().hasScheduledTick(pos, this))
/* 125 */       updateNeighborsInFront((Level)level, pos, (BlockState)state.setValue((Property)POWERED, Boolean.valueOf(false))); 
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 131 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getNearestLookingDirection().getOpposite().getOpposite());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ObserverBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */