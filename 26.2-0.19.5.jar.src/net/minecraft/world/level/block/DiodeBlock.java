/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.SignalGetter;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import net.minecraft.world.ticks.TickPriority;
/*     */ 
/*     */ public abstract class DiodeBlock extends HorizontalDirectionalBlock {
/*  28 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*  30 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 2.0D);
/*     */   
/*     */   protected DiodeBlock(BlockBehaviour.Properties properties) {
/*  33 */     super(properties);
/*     */   }
/*     */   
/*     */   protected abstract MapCodec<? extends DiodeBlock> codec();
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  41 */     return SHAPE;
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  46 */     BlockPos belowPos = pos.below();
/*  47 */     return canSurviveOn(level, belowPos, level.getBlockState(belowPos));
/*     */   }
/*     */   
/*     */   protected boolean canSurviveOn(LevelReader level, BlockPos neightborPos, BlockState neighborState) {
/*  51 */     return neighborState.isFaceSturdy((BlockGetter)level, neightborPos, Direction.UP, SupportType.RIGID);
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  56 */     if (isLocked((LevelReader)level, pos, state))
/*     */       return; 
/*  60 */     boolean on = ((Boolean)state.getValue((Property)POWERED)).booleanValue();
/*  61 */     boolean shouldTurnOn = shouldTurnOn((Level)level, pos, state);
/*  62 */     if (on && !shouldTurnOn) {
/*  63 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, Boolean.valueOf(false)), 2);
/*  64 */     } else if (!on) {
/*  67 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, Boolean.valueOf(true)), 2);
/*  68 */       if (!shouldTurnOn)
/*  69 */         level.scheduleTick(pos, this, getDelay(state), TickPriority.VERY_HIGH); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  77 */     if (!level.getBlockState(pos).is(this))
/*     */       return; 
/*  80 */     if (state.canSurvive((LevelReader)level, pos)) {
/*  81 */       checkTickOnNeighbor(level, pos, state);
/*     */       return;
/*     */     } 
/*  85 */     BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
/*  86 */     dropResources(state, (LevelAccessor)level, pos, blockEntity);
/*  87 */     level.removeBlock(pos, false);
/*  88 */     for (Direction direction : Direction.values())
/*  89 */       level.updateNeighborsAt(pos.relative(direction), this); 
/*     */   }
/*     */   
/*     */   protected void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
/*  94 */     if (isLocked((LevelReader)level, pos, state))
/*     */       return; 
/*  98 */     boolean on = ((Boolean)state.getValue((Property)POWERED)).booleanValue();
/*  99 */     boolean shouldTurnOn = shouldTurnOn(level, pos, state);
/* 100 */     if (on != shouldTurnOn && !level.getBlockTicks().willTickThisTick(pos, this)) {
/* 101 */       TickPriority priority = TickPriority.HIGH;
/* 104 */       if (shouldPrioritize((BlockGetter)level, pos, state)) {
/* 105 */         priority = TickPriority.EXTREMELY_HIGH;
/* 106 */       } else if (on) {
/* 107 */         priority = TickPriority.VERY_HIGH;
/*     */       } 
/* 110 */       level.scheduleTick(pos, this, getDelay(state), priority);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isLocked(LevelReader level, BlockPos pos, BlockState state) {
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
/* 119 */     return (getInputSignal(level, pos, state) > 0);
/*     */   }
/*     */   
/*     */   protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
/* 123 */     Direction direction = (Direction)state.getValue((Property)FACING);
/* 125 */     BlockPos targetPos = pos.relative(direction);
/* 126 */     int input = level.getSignal(targetPos, direction);
/* 127 */     if (input >= 15)
/* 128 */       return input; 
/* 131 */     BlockState targetBlockState = level.getBlockState(targetPos);
/* 132 */     return Math.max(input, targetBlockState.is(Blocks.REDSTONE_WIRE) ? ((Integer)targetBlockState.getValue((Property)RedStoneWireBlock.POWER)).intValue() : 0);
/*     */   }
/*     */   
/*     */   protected int getAlternateSignal(SignalGetter level, BlockPos pos, BlockState state) {
/* 136 */     Direction direction = (Direction)state.getValue((Property)FACING);
/* 137 */     Direction clockWise = direction.getClockWise();
/* 138 */     Direction counterClockWise = direction.getCounterClockWise();
/* 139 */     boolean sideInputDiodesOnly = sideInputDiodesOnly();
/* 140 */     return Math.max(level
/* 141 */         .getControlInputSignal(pos.relative(clockWise), clockWise, sideInputDiodesOnly), level
/* 142 */         .getControlInputSignal(pos.relative(counterClockWise), counterClockWise, sideInputDiodesOnly));
/*     */   }
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/* 148 */     return true;
/*     */   }
/*     */   
/*     */   protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
/* 153 */     return ((Boolean)state.getValue((Property)POWERED)).booleanValue() ? getOutputSignal(level, pos, state) : 0;
/*     */   }
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 158 */     return state.getSignal(level, pos, direction);
/*     */   }
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 163 */     return (state.getValue((Property)FACING) == direction) ? ownSignal(state, level, pos) : 0;
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 168 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite());
/*     */   }
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/* 173 */     if (shouldTurnOn(level, pos, state))
/* 174 */       level.scheduleTick(pos, this, 1); 
/*     */   }
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 180 */     updateNeighborsInFront(level, pos, state);
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 185 */     if (!movedByPiston)
/* 186 */       updateNeighborsInFront((Level)level, pos, state); 
/*     */   }
/*     */   
/*     */   protected void updateNeighborsInFront(Level level, BlockPos pos, BlockState state) {
/* 191 */     Direction direction = (Direction)state.getValue((Property)FACING);
/* 192 */     BlockPos oppositePos = pos.relative(direction.getOpposite());
/* 194 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, direction.getOpposite(), Direction.UP);
/* 195 */     level.neighborChanged(oppositePos, this, orientation);
/* 196 */     level.updateNeighborsAtExceptFromFacing(oppositePos, this, direction, orientation);
/*     */   }
/*     */   
/*     */   protected boolean sideInputDiodesOnly() {
/* 200 */     return false;
/*     */   }
/*     */   
/*     */   protected int getOutputSignal(BlockGetter level, BlockPos pos, BlockState state) {
/* 204 */     return 15;
/*     */   }
/*     */   
/*     */   public static boolean isDiode(BlockState state) {
/* 208 */     return state.getBlock() instanceof DiodeBlock;
/*     */   }
/*     */   
/*     */   public boolean shouldPrioritize(BlockGetter level, BlockPos pos, BlockState state) {
/* 212 */     Direction direction = ((Direction)state.getValue((Property)FACING)).getOpposite();
/* 213 */     BlockState oppositeState = level.getBlockState(pos.relative(direction));
/* 215 */     return (isDiode(oppositeState) && oppositeState.getValue((Property)FACING) != direction);
/*     */   }
/*     */   
/*     */   protected abstract int getDelay(BlockState paramBlockState);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\DiodeBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */