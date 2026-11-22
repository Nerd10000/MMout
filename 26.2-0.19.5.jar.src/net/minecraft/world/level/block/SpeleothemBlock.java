/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SpeleothemThickness;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class SpeleothemBlock extends Block implements SimpleWaterloggedBlock, Fallable {
/*  43 */   public static final EnumProperty<Direction> TIP_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;
/*     */   
/*  44 */   public static final EnumProperty<SpeleothemThickness> THICKNESS = BlockStateProperties.SPELEOTHEM_THICKNESS;
/*     */   
/*  45 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*     */   private static final int DELAY_BEFORE_FALLING = 2;
/*     */   
/*     */   private static final double MIN_TRIDENT_VELOCITY_TO_BREAK_SPELEOTHEM = 0.6D;
/*     */   
/*     */   private static final float STALACTITE_DAMAGE_PER_FALL_DISTANCE_AND_SIZE = 1.0F;
/*     */   
/*     */   private static final int STALACTITE_MAX_DAMAGE = 40;
/*     */   
/*     */   private static final int MAX_STALACTITE_HEIGHT_FOR_DAMAGE_CALCULATION = 6;
/*     */   
/*  54 */   private static final VoxelShape SHAPE_TIP_MERGE = Block.column(6.0D, 0.0D, 16.0D);
/*     */   
/*  55 */   private static final VoxelShape SHAPE_TIP_UP = Block.column(6.0D, 0.0D, 11.0D);
/*     */   
/*  56 */   protected static final VoxelShape SHAPE_TIP_DOWN = Block.column(6.0D, 5.0D, 16.0D);
/*     */   
/*  57 */   private static final VoxelShape SHAPE_FRUSTUM = Block.column(8.0D, 0.0D, 16.0D);
/*     */   
/*  58 */   private static final VoxelShape SHAPE_MIDDLE = Block.column(10.0D, 0.0D, 16.0D);
/*     */   
/*  59 */   private static final VoxelShape SHAPE_BASE = Block.column(12.0D, 0.0D, 16.0D);
/*     */   
/*  62 */   private static final float MAX_HORIZONTAL_OFFSET = (float)SHAPE_BASE.min(Direction.Axis.X);
/*     */   
/*     */   private static final float AVERAGE_DAYS_PER_GROWTH = 5.0F;
/*     */   
/*     */   private static final float GROWTH_PROBABILITY_PER_RANDOM_TICK = 0.011377778F;
/*     */   
/*     */   private static final int MAX_GROWTH_LENGTH = 7;
/*     */   
/*     */   private static final int MAX_STALAGMITE_SEARCH_RANGE_WHEN_GROWING = 10;
/*     */   
/*     */   protected final BlockState blockToGrowOn;
/*     */   
/*     */   public SpeleothemBlock(BlockState blockToGrowOn, BlockBehaviour.Properties properties) {
/*  73 */     super(properties);
/*  74 */     this.blockToGrowOn = blockToGrowOn;
/*  75 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
/*  76 */         .setValue((Property)TIP_DIRECTION, (Comparable)Direction.UP))
/*  77 */         .setValue((Property)THICKNESS, (Comparable)SpeleothemThickness.TIP))
/*  78 */         .setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  84 */     builder.add(new Property[] { (Property)TIP_DIRECTION, (Property)THICKNESS, (Property)WATERLOGGED });
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  89 */     return isValidSpeleothemPlacement(level, pos, (Direction)state.getValue((Property)TIP_DIRECTION));
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  98 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/*  99 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 102 */     if (directionToNeighbour != Direction.UP && directionToNeighbour != Direction.DOWN)
/* 103 */       return state; 
/* 106 */     Direction tipDirection = (Direction)state.getValue((Property)TIP_DIRECTION);
/* 107 */     if (tipDirection == Direction.DOWN && ticks.getBlockTicks().hasScheduledTick(pos, this))
/* 109 */       return state; 
/* 112 */     if (directionToNeighbour == tipDirection.getOpposite() && !canSurvive(state, level, pos)) {
/* 113 */       if (tipDirection == Direction.DOWN) {
/* 115 */         ticks.scheduleTick(pos, this, 2);
/*     */       } else {
/* 118 */         ticks.scheduleTick(pos, this, 1);
/*     */       } 
/* 120 */       return state;
/*     */     } 
/* 123 */     boolean mergeOpposingTips = (state.getValue((Property)THICKNESS) == SpeleothemThickness.TIP_MERGE);
/* 124 */     SpeleothemThickness newThickness = calculateSpeleothemThickness(level, pos, tipDirection, mergeOpposingTips);
/* 126 */     return (BlockState)state.setValue((Property)THICKNESS, (Comparable)newThickness);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 131 */     Level level = context.getLevel();
/* 132 */     BlockPos pos = context.getClickedPos();
/* 133 */     Direction defaultTipDirection = context.getNearestLookingVerticalDirection().getOpposite();
/* 134 */     Direction tipDirection = calculateTipDirection((LevelReader)level, pos, defaultTipDirection);
/* 135 */     if (tipDirection == null)
/* 136 */       return null; 
/* 139 */     boolean mergeOpposingTips = !context.isSecondaryUseActive();
/* 140 */     SpeleothemThickness thickness = calculateSpeleothemThickness((LevelReader)level, pos, tipDirection, mergeOpposingTips);
/* 142 */     return (BlockState)((BlockState)((BlockState)defaultBlockState()
/* 143 */       .setValue((Property)TIP_DIRECTION, (Comparable)tipDirection))
/* 144 */       .setValue((Property)THICKNESS, (Comparable)thickness))
/* 145 */       .setValue((Property)WATERLOGGED, Boolean.valueOf(level.getFluidState(pos).is(Fluids.WATER)));
/*     */   }
/*     */   
/*     */   private Direction calculateTipDirection(LevelReader level, BlockPos pos, Direction defaultTipDirection) {
/*     */     Direction tipDirection;
/* 151 */     if (isValidSpeleothemPlacement(level, pos, defaultTipDirection)) {
/* 152 */       tipDirection = defaultTipDirection;
/* 153 */     } else if (isValidSpeleothemPlacement(level, pos, defaultTipDirection.getOpposite())) {
/* 154 */       tipDirection = defaultTipDirection.getOpposite();
/*     */     } else {
/* 156 */       return null;
/*     */     } 
/* 158 */     return tipDirection;
/*     */   }
/*     */   
/*     */   private SpeleothemThickness calculateSpeleothemThickness(LevelReader level, BlockPos pos, Direction tipDirection, boolean mergeOpposingTips) {
/* 163 */     Direction baseDirection = tipDirection.getOpposite();
/* 164 */     BlockState inFrontState = level.getBlockState(pos.relative(tipDirection));
/* 166 */     if (isSpeleothemWithDirection(inFrontState, baseDirection) && inFrontState.is(this)) {
/* 168 */       if (mergeOpposingTips || inFrontState.getValue((Property)THICKNESS) == SpeleothemThickness.TIP_MERGE)
/* 169 */         return SpeleothemThickness.TIP_MERGE; 
/* 171 */       return SpeleothemThickness.TIP;
/*     */     } 
/* 175 */     if (!isSpeleothemWithDirection(inFrontState, tipDirection))
/* 176 */       return SpeleothemThickness.TIP; 
/* 180 */     SpeleothemThickness inFrontThickness = (SpeleothemThickness)inFrontState.getValue((Property)THICKNESS);
/* 181 */     if (inFrontThickness == SpeleothemThickness.TIP || inFrontThickness == SpeleothemThickness.TIP_MERGE)
/* 182 */       return SpeleothemThickness.FRUSTUM; 
/* 185 */     BlockState behindState = level.getBlockState(pos.relative(baseDirection));
/* 186 */     if (!isSpeleothemWithDirection(behindState, tipDirection))
/* 187 */       return SpeleothemThickness.BASE; 
/* 189 */     return SpeleothemThickness.MIDDLE;
/*     */   }
/*     */   
/*     */   private boolean isValidSpeleothemPlacement(LevelReader level, BlockPos pos, Direction tipDirection) {
/* 193 */     BlockPos behindPos = pos.relative(tipDirection.getOpposite());
/* 194 */     BlockState behindState = level.getBlockState(behindPos);
/* 196 */     return (behindState.isFaceSturdy((BlockGetter)level, behindPos, tipDirection) || (isSpeleothemWithDirection(behindState, tipDirection) && behindState.is(this)));
/*     */   }
/*     */   
/*     */   private static boolean isSpeleothemWithDirection(BlockState blockState, Direction tipDirection) {
/* 200 */     return (blockState.is(BlockTags.SPELEOTHEMS) && blockState.getValue((Property)TIP_DIRECTION) == tipDirection);
/*     */   }
/*     */   
/*     */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {
/* 205 */     if (level.isClientSide())
/*     */       return; 
/* 208 */     BlockPos blockPos = blockHit.getBlockPos();
/* 209 */     if (level instanceof ServerLevel) {
/* 209 */       ServerLevel serverLevel = (ServerLevel)level;
/* 209 */       if (projectile.mayInteract(serverLevel, blockPos) && projectile.mayBreak(serverLevel) && projectile instanceof net.minecraft.world.entity.projectile.arrow.ThrownTrident && projectile.getDeltaMovement().length() > 0.6D)
/* 210 */         level.destroyBlock(blockPos, true); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 216 */     if (isStalagmite(state) && !canSurvive(state, (LevelReader)level, pos)) {
/* 217 */       level.destroyBlock(pos, true);
/*     */     } else {
/* 219 */       spawnFallingStalactite(state, level, pos);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void spawnFallingStalactite(BlockState state, ServerLevel level, BlockPos pos) {
/* 224 */     BlockPos.MutableBlockPos fallPos = pos.mutable();
/* 225 */     BlockState fallState = state;
/* 227 */     while (isStalactite(fallState)) {
/* 228 */       FallingBlockEntity entity = FallingBlockEntity.fall((Level)level, (BlockPos)fallPos, fallState);
/* 229 */       if (isTip(fallState, true)) {
/* 232 */         int size = Math.max(1 + pos.getY() - fallPos.getY(), 6);
/* 233 */         float damagePerFallDistance = 1.0F * size;
/* 235 */         entity.setHurtsEntities(damagePerFallDistance, 40);
/*     */         break;
/*     */       } 
/* 238 */       fallPos.move(Direction.DOWN);
/* 239 */       fallState = level.getBlockState((BlockPos)fallPos);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isStalagmite(BlockState state) {
/* 244 */     return isSpeleothemWithDirection(state, Direction.UP);
/*     */   }
/*     */   
/*     */   protected static boolean isStalactite(BlockState state) {
/* 248 */     return isSpeleothemWithDirection(state, Direction.DOWN);
/*     */   }
/*     */   
/*     */   private static boolean isTip(BlockState state, boolean includeMergedTip) {
/* 252 */     if (!state.is(BlockTags.SPELEOTHEMS))
/* 253 */       return false; 
/* 255 */     SpeleothemThickness thickness = (SpeleothemThickness)state.getValue((Property)THICKNESS);
/* 256 */     return (thickness == SpeleothemThickness.TIP || (includeMergedTip && thickness == SpeleothemThickness.TIP_MERGE));
/*     */   }
/*     */   
/*     */   public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity entity) {
/* 261 */     if (!entity.isSilent())
/* 262 */       level.levelEvent(getStalactiteLandingSound(), pos, 0); 
/*     */   }
/*     */   
/*     */   public DamageSource getFallDamageSource(Entity entity) {
/* 270 */     return entity.damageSources().fallingStalactite(entity);
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 275 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 276 */       return Fluids.WATER.getSource(false); 
/* 278 */     return super.getFluidState(state);
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 283 */     switch ((SpeleothemThickness)state.getValue((Property)THICKNESS)) {
/*     */       default:
/* 283 */         throw new MatchException(null, null);
/*     */       case TIP_MERGE:
/*     */       
/*     */       case TIP:
/* 283 */         shape = 
/*     */           
/* 285 */           (state.getValue((Property)TIP_DIRECTION) == Direction.DOWN) ? SHAPE_TIP_DOWN : SHAPE_TIP_UP;
/* 290 */         return shape.move(state.getOffset(pos));
/*     */       case FRUSTUM:
/*     */       
/*     */       case MIDDLE:
/*     */       
/*     */       case BASE:
/*     */         break;
/*     */     } 
/*     */     VoxelShape shape = SHAPE_BASE;
/* 290 */     return shape.move(state.getOffset(pos));
/*     */   }
/*     */   
/*     */   protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
/* 295 */     return false;
/*     */   }
/*     */   
/*     */   protected float getMaxHorizontalOffset() {
/* 300 */     return MAX_HORIZONTAL_OFFSET;
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 305 */     return false;
/*     */   }
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 310 */     if (random.nextFloat() < 0.011377778F && isStalactiteStartPos(state, (LevelReader)level, pos))
/* 311 */       growStalactiteOrStalagmiteIfPossible(state, level, pos, random); 
/*     */   }
/*     */   
/*     */   protected static boolean isStalactiteStartPos(BlockState state, LevelReader level, BlockPos pos) {
/* 316 */     return (isStalactite(state) && !level.getBlockState(pos.above()).is(state.getBlock()));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void growStalactiteOrStalagmiteIfPossible(BlockState stalactiteStartState, ServerLevel level, BlockPos stalactiteStartPos, RandomSource random) {
/* 321 */     if (!canGrow((LevelReader)level, stalactiteStartPos))
/*     */       return; 
/* 325 */     BlockPos stalactiteTipPos = findTip(stalactiteStartState, (LevelAccessor)level, stalactiteStartPos, getMaxGrowthLength(), false);
/* 326 */     if (stalactiteTipPos == null)
/*     */       return; 
/* 331 */     BlockState stalactiteTipState = level.getBlockState(stalactiteTipPos);
/* 332 */     if (!isFreeHangingStalactite(stalactiteTipState) || !canTipGrow(stalactiteTipState, level, stalactiteTipPos))
/*     */       return; 
/* 337 */     if (random.nextBoolean()) {
/* 338 */       grow(level, stalactiteTipPos, Direction.DOWN);
/*     */     } else {
/* 340 */       growStalagmiteBelow(level, stalactiteTipPos);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean canGrow(LevelReader level, BlockPos pos) {
/* 345 */     return level.getBlockState(pos.above()).is(this.blockToGrowOn.getBlock());
/*     */   }
/*     */   
/*     */   protected static BlockPos findTip(BlockState speleothemState, LevelAccessor level, BlockPos speleothemPos, int maxSearchLength, boolean includeMergedTip) {
/* 349 */     if (isTip(speleothemState, includeMergedTip))
/* 350 */       return speleothemPos; 
/* 352 */     Direction searchDirection = (Direction)speleothemState.getValue((Property)TIP_DIRECTION);
/* 353 */     BiPredicate<BlockPos, BlockState> pathPredicate = (pos, state) -> (state.is(speleothemState.getBlock()) && state.getValue((Property)TIP_DIRECTION) == searchDirection);
/* 354 */     return findBlockVertical(level, speleothemPos, searchDirection.getAxisDirection(), pathPredicate, speleothem -> isTip(speleothem, includeMergedTip), maxSearchLength).orElse(null);
/*     */   }
/*     */   
/*     */   protected static Optional<BlockPos> findBlockVertical(LevelAccessor level, BlockPos pos, Direction.AxisDirection axisDirection, BiPredicate<BlockPos, BlockState> pathPredicate, Predicate<BlockState> targetPredicate, int maxSteps) {
/* 358 */     Direction direction = Direction.get(axisDirection, Direction.Axis.Y);
/* 359 */     BlockPos.MutableBlockPos mutablePos = pos.mutable();
/* 361 */     for (int i = 1; i < maxSteps; i++) {
/* 362 */       mutablePos.move(direction);
/* 363 */       BlockState state = level.getBlockState((BlockPos)mutablePos);
/* 364 */       if (targetPredicate.test(state))
/* 365 */         return Optional.of(mutablePos.immutable()); 
/* 367 */       if (level.isOutsideBuildHeight(mutablePos.getY()) || !pathPredicate.test(mutablePos, state))
/* 368 */         return Optional.empty(); 
/*     */     } 
/* 371 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private boolean canTipGrow(BlockState tipState, ServerLevel level, BlockPos tipPos) {
/* 375 */     Direction growDirection = (Direction)tipState.getValue((Property)TIP_DIRECTION);
/* 376 */     BlockPos growPos = tipPos.relative(growDirection);
/* 377 */     BlockState stateAtGrowPos = level.getBlockState(growPos);
/* 379 */     if (!stateAtGrowPos.getFluidState().isEmpty())
/* 380 */       return false; 
/* 382 */     if (stateAtGrowPos.isAir())
/* 383 */       return true; 
/* 387 */     return isUnmergedTipWithDirection(stateAtGrowPos, growDirection.getOpposite());
/*     */   }
/*     */   
/*     */   private boolean isUnmergedTipWithDirection(BlockState state, Direction tipDirection) {
/* 391 */     return (isTip(state, false) && state.getValue((Property)TIP_DIRECTION) == tipDirection && state.is(this));
/*     */   }
/*     */   
/*     */   private void grow(ServerLevel level, BlockPos growFromPos, Direction growToDirection) {
/* 395 */     BlockPos targetPos = growFromPos.relative(growToDirection);
/* 396 */     BlockState existingStateAtTargetPos = level.getBlockState(targetPos);
/* 397 */     if (isUnmergedTipWithDirection(existingStateAtTargetPos, growToDirection.getOpposite())) {
/* 398 */       createMergedTips(existingStateAtTargetPos, (LevelAccessor)level, targetPos);
/* 399 */     } else if (existingStateAtTargetPos.isAir() || existingStateAtTargetPos.is(Blocks.WATER)) {
/* 400 */       createSpeleothem((LevelAccessor)level, targetPos, growToDirection, SpeleothemThickness.TIP);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void createSpeleothem(LevelAccessor level, BlockPos pos, Direction direction, SpeleothemThickness thickness) {
/* 408 */     BlockState state = (BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)TIP_DIRECTION, (Comparable)direction)).setValue((Property)THICKNESS, (Comparable)thickness)).setValue((Property)WATERLOGGED, Boolean.valueOf(level.getFluidState(pos).is(Fluids.WATER)));
/* 409 */     level.setBlock(pos, state, 3);
/*     */   }
/*     */   
/*     */   private void createMergedTips(BlockState tipState, LevelAccessor level, BlockPos tipPos) {
/*     */     BlockPos stalactitePos, stalagmitePos;
/* 416 */     if (tipState.getValue((Property)TIP_DIRECTION) == Direction.UP) {
/* 417 */       stalagmitePos = tipPos;
/* 418 */       stalactitePos = tipPos.above();
/*     */     } else {
/* 420 */       stalactitePos = tipPos;
/* 421 */       stalagmitePos = tipPos.below();
/*     */     } 
/* 424 */     createSpeleothem(level, stalactitePos, Direction.DOWN, SpeleothemThickness.TIP_MERGE);
/* 425 */     createSpeleothem(level, stalagmitePos, Direction.UP, SpeleothemThickness.TIP_MERGE);
/*     */   }
/*     */   
/*     */   private void growStalagmiteBelow(ServerLevel level, BlockPos posAboveStalagmite) {
/* 430 */     BlockPos.MutableBlockPos pos = posAboveStalagmite.mutable();
/* 431 */     for (int i = 0; i < 10; i++) {
/* 432 */       pos.move(Direction.DOWN);
/* 433 */       BlockState state = level.getBlockState((BlockPos)pos);
/* 434 */       if (!state.getFluidState().isEmpty())
/*     */         return; 
/* 438 */       if (isUnmergedTipWithDirection(state, Direction.UP) && canTipGrow(state, level, (BlockPos)pos)) {
/* 440 */         grow(level, (BlockPos)pos, Direction.UP);
/*     */         return;
/*     */       } 
/* 443 */       if (isValidSpeleothemPlacement((LevelReader)level, (BlockPos)pos, Direction.UP) && !level.isWaterAt(pos.below())) {
/* 445 */         grow(level, pos.below(), Direction.UP);
/*     */         return;
/*     */       } 
/* 448 */       if (blocksStalagmiteScan((LevelReader)level, (BlockPos)pos, state))
/*     */         return; 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean blocksStalagmiteScan(LevelReader level, BlockPos pos, BlockState state) {
/* 455 */     return false;
/*     */   }
/*     */   
/*     */   protected static boolean isFreeHangingStalactite(BlockState state) {
/* 459 */     return (isStalactite(state) && state.getValue((Property)THICKNESS) == SpeleothemThickness.TIP && !((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue());
/*     */   }
/*     */   
/*     */   protected int getMaxGrowthLength() {
/* 463 */     return 7;
/*     */   }
/*     */   
/*     */   public abstract MapCodec<? extends SpeleothemBlock> codec();
/*     */   
/*     */   protected abstract int getStalactiteLandingSound();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SpeleothemBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */