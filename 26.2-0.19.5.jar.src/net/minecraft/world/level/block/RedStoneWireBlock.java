/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureFlags;
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
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RedstoneSide;
/*     */ import net.minecraft.world.level.redstone.DefaultRedstoneWireEvaluator;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneWireEvaluator;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.level.redstone.RedstoneWireEvaluator;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class RedStoneWireBlock extends Block {
/*  45 */   public static final MapCodec<RedStoneWireBlock> CODEC = simpleCodec(RedStoneWireBlock::new);
/*     */   
/*     */   public MapCodec<RedStoneWireBlock> codec() {
/*  49 */     return CODEC;
/*     */   }
/*     */   
/*  52 */   public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.NORTH_REDSTONE;
/*     */   
/*  53 */   public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.EAST_REDSTONE;
/*     */   
/*  54 */   public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.SOUTH_REDSTONE;
/*     */   
/*  55 */   public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.WEST_REDSTONE;
/*     */   
/*  56 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*     */   
/*  58 */   public static final Map<Direction, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION = (Map<Direction, EnumProperty<RedstoneSide>>)ImmutableMap.copyOf(Maps.newEnumMap(Map.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST)));
/*     */   
/*     */   private static final int[] COLORS;
/*     */   
/*     */   private static final float PARTICLE_DENSITY = 0.2F;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   private final BlockState crossState;
/*     */   
/*     */   static {
/*  65 */     COLORS = (int[])Util.make(new int[16], list -> {
/*     */           for (int i = 0; i <= 15; i++) {
/*     */             float power = i / 15.0F;
/*     */             float red = power * 0.6F + ((power > 0.0F) ? 0.4F : 0.3F);
/*     */             float green = Mth.clamp(power * power * 0.7F - 0.5F, 0.0F, 1.0F);
/*     */             float blue = Mth.clamp(power * power * 0.6F - 0.7F, 0.0F, 1.0F);
/*     */             list[i] = ARGB.colorFromFloat(1.0F, red, green, blue);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*  80 */   private final RedstoneWireEvaluator evaluator = (RedstoneWireEvaluator)new DefaultRedstoneWireEvaluator(this);
/*     */   
/*     */   private boolean shouldSignal = true;
/*     */   
/*     */   public RedStoneWireBlock(BlockBehaviour.Properties properties) {
/*  84 */     super(properties);
/*  85 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, (Comparable)RedstoneSide.NONE)).setValue((Property)EAST, (Comparable)RedstoneSide.NONE)).setValue((Property)SOUTH, (Comparable)RedstoneSide.NONE)).setValue((Property)WEST, (Comparable)RedstoneSide.NONE)).setValue((Property)POWER, Integer.valueOf(0)));
/*  87 */     this.shapes = makeShapes();
/*  89 */     this.crossState = (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)NORTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)EAST, (Comparable)RedstoneSide.SIDE)).setValue((Property)SOUTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)WEST, (Comparable)RedstoneSide.SIDE);
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  93 */     int height = 1;
/*  94 */     int width = 10;
/*  96 */     VoxelShape dot = Block.column(10.0D, 0.0D, 1.0D);
/*  97 */     Map<Direction, VoxelShape> floor = Shapes.rotateHorizontal(Block.boxZ(10.0D, 0.0D, 1.0D, 0.0D, 8.0D));
/*  98 */     Map<Direction, VoxelShape> up = Shapes.rotateHorizontal(Block.boxZ(10.0D, 16.0D, 0.0D, 1.0D));
/* 100 */     return getShapeForEachState(state -> {
/*     */           VoxelShape shape = dot;
/*     */           for (Map.Entry<Direction, EnumProperty<RedstoneSide>> entry : PROPERTY_BY_DIRECTION.entrySet()) {
/*     */             switch ((RedstoneSide)state.getValue((Property)entry.getValue())) {
/*     */               default:
/*     */                 throw new MatchException(null, null);
/*     */               case LEFT_RIGHT:
/*     */               
/*     */               case FRONT_BACK:
/*     */               
/*     */               case null:
/*     */                 break;
/*     */             } 
/*     */             shape = shape;
/*     */           } 
/*     */           return shape;
/* 100 */         }(Property<?>[])new Property[] { (Property)POWER });
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 116 */     return this.shapes.apply(state);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 121 */     return getConnectionState((BlockGetter)context.getLevel(), this.crossState, context.getClickedPos());
/*     */   }
/*     */   
/*     */   private BlockState getConnectionState(BlockGetter level, BlockState state, BlockPos pos) {
/* 125 */     boolean wasDot = isDot(state);
/* 126 */     state = getMissingConnections(level, (BlockState)defaultBlockState().setValue((Property)POWER, state.getValue((Property)POWER)), pos);
/* 129 */     if (wasDot && isDot(state))
/* 130 */       return state; 
/* 133 */     boolean north = ((RedstoneSide)state.getValue((Property)NORTH)).isConnected();
/* 134 */     boolean south = ((RedstoneSide)state.getValue((Property)SOUTH)).isConnected();
/* 135 */     boolean east = ((RedstoneSide)state.getValue((Property)EAST)).isConnected();
/* 136 */     boolean west = ((RedstoneSide)state.getValue((Property)WEST)).isConnected();
/* 137 */     boolean northSouthEmpty = (!north && !south);
/* 138 */     boolean eastWestEmpty = (!east && !west);
/* 140 */     if (!west && northSouthEmpty)
/* 141 */       state = (BlockState)state.setValue((Property)WEST, (Comparable)RedstoneSide.SIDE); 
/* 143 */     if (!east && northSouthEmpty)
/* 144 */       state = (BlockState)state.setValue((Property)EAST, (Comparable)RedstoneSide.SIDE); 
/* 146 */     if (!north && eastWestEmpty)
/* 147 */       state = (BlockState)state.setValue((Property)NORTH, (Comparable)RedstoneSide.SIDE); 
/* 149 */     if (!south && eastWestEmpty)
/* 150 */       state = (BlockState)state.setValue((Property)SOUTH, (Comparable)RedstoneSide.SIDE); 
/* 152 */     return state;
/*     */   }
/*     */   
/*     */   private BlockState getMissingConnections(BlockGetter level, BlockState state, BlockPos pos) {
/* 156 */     boolean canConnectUp = !level.getBlockState(pos.above()).isRedstoneConductor(level, pos);
/* 157 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 158 */       if (!((RedstoneSide)state.getValue((Property)PROPERTY_BY_DIRECTION.get(direction))).isConnected()) {
/* 159 */         RedstoneSide sideConnection = getConnectingSide(level, pos, direction, canConnectUp);
/* 160 */         state = (BlockState)state.setValue((Property)PROPERTY_BY_DIRECTION.get(direction), (Comparable)sideConnection);
/*     */       } 
/*     */     } 
/* 163 */     return state;
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 168 */     if (directionToNeighbour == Direction.DOWN) {
/* 169 */       if (!canSurviveOn((BlockGetter)level, neighbourPos, neighbourState))
/* 170 */         return Blocks.AIR.defaultBlockState(); 
/* 172 */       return state;
/*     */     } 
/* 174 */     if (directionToNeighbour == Direction.UP)
/* 175 */       return getConnectionState((BlockGetter)level, state, pos); 
/* 178 */     RedstoneSide sideConnection = getConnectingSide((BlockGetter)level, pos, directionToNeighbour);
/* 179 */     if (sideConnection.isConnected() == ((RedstoneSide)state.getValue((Property)PROPERTY_BY_DIRECTION.get(directionToNeighbour))).isConnected() && !isCross(state))
/* 180 */       return (BlockState)state.setValue((Property)PROPERTY_BY_DIRECTION.get(directionToNeighbour), (Comparable)sideConnection); 
/* 182 */     return getConnectionState((BlockGetter)level, (BlockState)((BlockState)this.crossState.setValue((Property)POWER, state.getValue((Property)POWER))).setValue((Property)PROPERTY_BY_DIRECTION.get(directionToNeighbour), (Comparable)sideConnection), pos);
/*     */   }
/*     */   
/*     */   private static boolean isCross(BlockState state) {
/* 186 */     return (((RedstoneSide)state.getValue((Property)NORTH)).isConnected() && ((RedstoneSide)state.getValue((Property)SOUTH)).isConnected() && ((RedstoneSide)state.getValue((Property)EAST)).isConnected() && ((RedstoneSide)state.getValue((Property)WEST)).isConnected());
/*     */   }
/*     */   
/*     */   private static boolean isDot(BlockState state) {
/* 190 */     return (!((RedstoneSide)state.getValue((Property)NORTH)).isConnected() && !((RedstoneSide)state.getValue((Property)SOUTH)).isConnected() && !((RedstoneSide)state.getValue((Property)EAST)).isConnected() && !((RedstoneSide)state.getValue((Property)WEST)).isConnected());
/*     */   }
/*     */   
/*     */   protected void updateIndirectNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos, int updateFlags, int updateLimit) {
/* 195 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 196 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 197 */       RedstoneSide value = (RedstoneSide)state.getValue((Property)PROPERTY_BY_DIRECTION.get(direction));
/* 198 */       if (value != RedstoneSide.NONE && !level.getBlockState((BlockPos)blockPos.setWithOffset((Vec3i)pos, direction)).is(this)) {
/* 199 */         blockPos.move(Direction.DOWN);
/* 200 */         BlockState blockStateDown = level.getBlockState((BlockPos)blockPos);
/* 201 */         if (blockStateDown.is(this)) {
/* 202 */           BlockPos neighborPos = blockPos.relative(direction.getOpposite());
/* 203 */           level.neighborShapeChanged(direction.getOpposite(), (BlockPos)blockPos, neighborPos, level.getBlockState(neighborPos), updateFlags, updateLimit);
/*     */         } 
/* 206 */         blockPos.setWithOffset((Vec3i)pos, direction).move(Direction.UP);
/* 207 */         BlockState blockStateUp = level.getBlockState((BlockPos)blockPos);
/* 208 */         if (blockStateUp.is(this)) {
/* 209 */           BlockPos neighborPos = blockPos.relative(direction.getOpposite());
/* 210 */           level.neighborShapeChanged(direction.getOpposite(), (BlockPos)blockPos, neighborPos, level.getBlockState(neighborPos), updateFlags, updateLimit);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private RedstoneSide getConnectingSide(BlockGetter level, BlockPos pos, Direction direction) {
/* 217 */     return getConnectingSide(level, pos, direction, !level.getBlockState(pos.above()).isRedstoneConductor(level, pos));
/*     */   }
/*     */   
/*     */   private RedstoneSide getConnectingSide(BlockGetter level, BlockPos pos, Direction direction, boolean canConnectUp) {
/* 221 */     BlockPos relativePos = pos.relative(direction);
/* 222 */     BlockState relativeState = level.getBlockState(relativePos);
/* 223 */     if (canConnectUp) {
/* 225 */       boolean isPlaceableAbove = (relativeState.getBlock() instanceof TrapDoorBlock || canSurviveOn(level, relativePos, relativeState));
/* 226 */       if (isPlaceableAbove && shouldConnectTo(level.getBlockState(relativePos.above()))) {
/* 229 */         if (relativeState.isFaceSturdy(level, relativePos, direction.getOpposite()))
/* 230 */           return RedstoneSide.UP; 
/* 232 */         return RedstoneSide.SIDE;
/*     */       } 
/*     */     } 
/* 237 */     if (shouldConnectTo(relativeState, direction) || (!relativeState.isRedstoneConductor(level, relativePos) && shouldConnectTo(level.getBlockState(relativePos.below()))))
/* 238 */       return RedstoneSide.SIDE; 
/* 240 */     return RedstoneSide.NONE;
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 245 */     BlockPos below = pos.below();
/* 246 */     BlockState belowState = level.getBlockState(below);
/* 247 */     return canSurviveOn((BlockGetter)level, below, belowState);
/*     */   }
/*     */   
/*     */   private boolean canSurviveOn(BlockGetter level, BlockPos relativePos, BlockState relativeState) {
/* 251 */     return (relativeState.isFaceSturdy(level, relativePos, Direction.UP) || relativeState.is(Blocks.HOPPER));
/*     */   }
/*     */   
/*     */   private void updatePowerStrength(Level level, BlockPos pos, BlockState state, Orientation orientation, boolean shapeUpdateWiresAroundInitialPosition) {
/* 255 */     if (useExperimentalEvaluator(level)) {
/* 256 */       (new ExperimentalRedstoneWireEvaluator(this)).updatePowerStrength(level, pos, state, orientation, shapeUpdateWiresAroundInitialPosition);
/*     */     } else {
/* 258 */       this.evaluator.updatePowerStrength(level, pos, state, orientation, shapeUpdateWiresAroundInitialPosition);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getBlockSignal(Level level, BlockPos pos) {
/* 264 */     this.shouldSignal = false;
/* 265 */     int blockSignal = level.getBestNeighborSignal(pos);
/* 266 */     this.shouldSignal = true;
/* 267 */     return blockSignal;
/*     */   }
/*     */   
/*     */   private void checkCornerChangeAt(Level level, BlockPos pos) {
/* 271 */     if (!level.getBlockState(pos).is(this))
/*     */       return; 
/* 275 */     level.updateNeighborsAt(pos, this);
/* 276 */     for (Direction direction : Direction.values())
/* 277 */       level.updateNeighborsAt(pos.relative(direction), this); 
/*     */   }
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 283 */     if (oldState.is(state.getBlock()) || level.isClientSide())
/*     */       return; 
/* 287 */     updatePowerStrength(level, pos, state, (Orientation)null, true);
/* 289 */     for (Direction direction : Direction.Plane.VERTICAL)
/* 290 */       level.updateNeighborsAt(pos.relative(direction), this); 
/* 293 */     updateNeighborsOfNeighboringWires(level, pos);
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 298 */     if (movedByPiston)
/*     */       return; 
/* 301 */     for (Direction direction : Direction.values())
/* 302 */       level.updateNeighborsAt(pos.relative(direction), this); 
/* 304 */     updatePowerStrength((Level)level, pos, state, (Orientation)null, false);
/* 306 */     updateNeighborsOfNeighboringWires((Level)level, pos);
/*     */   }
/*     */   
/*     */   private void updateNeighborsOfNeighboringWires(Level level, BlockPos pos) {
/* 310 */     for (Direction direction : Direction.Plane.HORIZONTAL)
/* 311 */       checkCornerChangeAt(level, pos.relative(direction)); 
/* 314 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 315 */       BlockPos target = pos.relative(direction);
/* 317 */       if (level.getBlockState(target).isRedstoneConductor((BlockGetter)level, target)) {
/* 318 */         checkCornerChangeAt(level, target.above());
/*     */         continue;
/*     */       } 
/* 320 */       checkCornerChangeAt(level, target.below());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 327 */     if (level.isClientSide())
/*     */       return; 
/* 332 */     if (block == this && useExperimentalEvaluator(level))
/*     */       return; 
/* 336 */     if (state.canSurvive((LevelReader)level, pos)) {
/* 337 */       updatePowerStrength(level, pos, state, orientation, false);
/*     */     } else {
/* 339 */       dropResources(state, level, pos);
/* 340 */       level.removeBlock(pos, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean useExperimentalEvaluator(Level level) {
/* 345 */     return level.enabledFeatures().contains(FeatureFlags.REDSTONE_EXPERIMENTS);
/*     */   }
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 350 */     if (!this.shouldSignal)
/* 351 */       return 0; 
/* 353 */     return state.getSignal(level, pos, direction);
/*     */   }
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 358 */     if (!this.shouldSignal || direction == Direction.DOWN)
/* 359 */       return 0; 
/* 361 */     int power = ownSignal(state, level, pos);
/* 362 */     if (power == 0)
/* 363 */       return 0; 
/* 366 */     if (direction == Direction.UP || ((RedstoneSide)getConnectionState(level, state, pos).getValue((Property)PROPERTY_BY_DIRECTION.get(direction.getOpposite()))).isConnected())
/* 367 */       return power; 
/* 369 */     return 0;
/*     */   }
/*     */   
/*     */   protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
/* 375 */     return ((Integer)state.getValue((Property)POWER)).intValue();
/*     */   }
/*     */   
/*     */   protected static boolean shouldConnectTo(BlockState blockState) {
/* 379 */     return shouldConnectTo(blockState, (Direction)null);
/*     */   }
/*     */   
/*     */   protected static boolean shouldConnectTo(BlockState blockState, Direction direction) {
/* 383 */     if (blockState.is(Blocks.REDSTONE_WIRE))
/* 384 */       return true; 
/* 387 */     if (blockState.is(Blocks.REPEATER)) {
/* 388 */       Direction repeaterDirection = (Direction)blockState.getValue((Property)RepeaterBlock.FACING);
/* 389 */       return (repeaterDirection == direction || repeaterDirection.getOpposite() == direction);
/*     */     } 
/* 392 */     if (blockState.is(Blocks.OBSERVER))
/* 393 */       return (direction == blockState.getValue((Property)ObserverBlock.FACING)); 
/* 396 */     return (blockState.isSignalSource() && direction != null);
/*     */   }
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/* 401 */     return this.shouldSignal;
/*     */   }
/*     */   
/*     */   public static int getColorForPower(int power) {
/* 405 */     return COLORS[power];
/*     */   }
/*     */   
/*     */   private static void spawnParticlesAlongLine(Level level, RandomSource random, BlockPos pos, int color, Direction side, Direction along, float from, float to) {
/* 409 */     float span = to - from;
/* 410 */     if (random.nextFloat() >= 0.2F * span)
/*     */       return; 
/* 413 */     float sideOfBlock = 0.4375F;
/* 414 */     float positionOnLine = from + span * random.nextFloat();
/* 415 */     double x = 0.5D + (0.4375F * side.getStepX()) + (positionOnLine * along.getStepX());
/* 416 */     double y = 0.5D + (0.4375F * side.getStepY()) + (positionOnLine * along.getStepY());
/* 417 */     double z = 0.5D + (0.4375F * side.getStepZ()) + (positionOnLine * along.getStepZ());
/* 418 */     level.addParticle((ParticleOptions)new DustParticleOptions(color, 1.0F), pos.getX() + x, pos.getY() + y, pos.getZ() + z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 423 */     int power = ((Integer)state.getValue((Property)POWER)).intValue();
/* 424 */     if (power == 0)
/*     */       return; 
/* 427 */     for (Direction horizontal : Direction.Plane.HORIZONTAL) {
/* 428 */       RedstoneSide connection = (RedstoneSide)state.getValue((Property)PROPERTY_BY_DIRECTION.get(horizontal));
/* 429 */       switch (connection) {
/*     */         case LEFT_RIGHT:
/* 431 */           spawnParticlesAlongLine(level, random, pos, COLORS[power], horizontal, Direction.UP, -0.5F, 0.5F);
/*     */         case FRONT_BACK:
/* 434 */           spawnParticlesAlongLine(level, random, pos, COLORS[power], Direction.DOWN, horizontal, 0.0F, 0.5F);
/*     */           continue;
/*     */       } 
/* 438 */       spawnParticlesAlongLine(level, random, pos, COLORS[power], Direction.DOWN, horizontal, 0.0F, 0.3F);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 445 */     switch (rotation) {
/*     */       case LEFT_RIGHT:
/*     */       
/*     */       case FRONT_BACK:
/*     */       
/*     */       case null:
/*     */       
/*     */     } 
/* 445 */     return 
/*     */       
/* 449 */       state;
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 455 */     switch (mirror) {
/*     */       case LEFT_RIGHT:
/* 457 */         return (BlockState)((BlockState)state.setValue((Property)NORTH, state.getValue((Property)SOUTH))).setValue((Property)SOUTH, state.getValue((Property)NORTH));
/*     */       case FRONT_BACK:
/* 460 */         return (BlockState)((BlockState)state.setValue((Property)EAST, state.getValue((Property)WEST))).setValue((Property)WEST, state.getValue((Property)EAST));
/*     */     } 
/* 465 */     return super.mirror(state, mirror);
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 470 */     builder.add(new Property[] { (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST, (Property)POWER });
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 475 */     if (!(player.getAbilities()).mayBuild)
/* 476 */       return (InteractionResult)InteractionResult.PASS; 
/* 479 */     if (isCross(state) || isDot(state)) {
/* 480 */       BlockState newState = isCross(state) ? defaultBlockState() : this.crossState;
/* 481 */       newState = (BlockState)newState.setValue((Property)POWER, state.getValue((Property)POWER));
/* 482 */       newState = getConnectionState((BlockGetter)level, newState, pos);
/* 483 */       if (newState != state) {
/* 484 */         level.setBlock(pos, newState, 3);
/* 486 */         updatesOnShapeChange(level, pos, state, newState);
/* 487 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/* 490 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   private void updatesOnShapeChange(Level level, BlockPos pos, BlockState oldState, BlockState newState) {
/* 494 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, null, Direction.UP);
/* 495 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 496 */       BlockPos relativePos = pos.relative(direction);
/* 497 */       if (((RedstoneSide)oldState.getValue((Property)PROPERTY_BY_DIRECTION.get(direction))).isConnected() != ((RedstoneSide)newState.getValue((Property)PROPERTY_BY_DIRECTION.get(direction))).isConnected() && level.getBlockState(relativePos).isRedstoneConductor((BlockGetter)level, relativePos))
/* 498 */         level.updateNeighborsAtExceptFromFacing(relativePos, newState.getBlock(), direction.getOpposite(), ExperimentalRedstoneUtils.withFront(orientation, direction)); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\RedStoneWireBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */