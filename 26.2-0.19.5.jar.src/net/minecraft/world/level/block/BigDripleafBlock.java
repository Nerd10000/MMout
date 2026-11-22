/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
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
/*     */ import net.minecraft.world.level.block.state.properties.Tilt;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BigDripleafBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, BonemealableBlock {
/*  47 */   public static final MapCodec<BigDripleafBlock> CODEC = simpleCodec(BigDripleafBlock::new);
/*     */   
/*     */   public MapCodec<BigDripleafBlock> codec() {
/*  51 */     return CODEC;
/*     */   }
/*     */   
/*  54 */   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  55 */   private static final EnumProperty<Tilt> TILT = BlockStateProperties.TILT;
/*     */   
/*     */   private static final int NO_TICK = -1;
/*     */   
/*     */   private static final Object2IntMap<Tilt> DELAY_UNTIL_NEXT_TILT_STATE;
/*     */   
/*     */   private static final int MAX_GEN_HEIGHT = 5;
/*     */   
/*     */   private static final int ENTITY_DETECTION_MIN_Y = 11;
/*     */   
/*     */   private static final int LOWEST_LEAF_TOP = 13;
/*     */   
/*     */   static {
/*  59 */     DELAY_UNTIL_NEXT_TILT_STATE = (Object2IntMap<Tilt>)Util.make(new Object2IntArrayMap(), map -> {
/*     */           map.defaultReturnValue(-1);
/*     */           map.put(Tilt.UNSTABLE, 10);
/*     */           map.put(Tilt.PARTIAL, 10);
/*     */           map.put(Tilt.FULL, 100);
/*     */         });
/*     */   }
/*     */   
/*  71 */   private static final Map<Tilt, VoxelShape> SHAPE_LEAF = Maps.newEnumMap(Map.of(Tilt.NONE, 
/*     */         
/*  73 */         Block.column(16.0D, 11.0D, 15.0D), Tilt.UNSTABLE, 
/*  74 */         Block.column(16.0D, 11.0D, 15.0D), Tilt.PARTIAL, 
/*  75 */         Block.column(16.0D, 11.0D, 13.0D), Tilt.FULL, 
/*  76 */         Shapes.empty()));
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   protected BigDripleafBlock(BlockBehaviour.Properties properties) {
/*  82 */     super(properties);
/*  83 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
/*  84 */         .setValue((Property)WATERLOGGED, Boolean.valueOf(false)))
/*  85 */         .setValue((Property)FACING, (Comparable)Direction.NORTH))
/*  86 */         .setValue((Property)TILT, (Comparable)Tilt.NONE));
/*  88 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  92 */     Map<Direction, VoxelShape> stems = Shapes.rotateHorizontal(Block.column(6.0D, 0.0D, 13.0D).move(0.0D, 0.0D, 0.25D).optimize());
/*  94 */     return getShapeForEachState(state -> Shapes.or(SHAPE_LEAF.get(state.getValue((Property)TILT)), (VoxelShape)stems.get(state.getValue((Property)FACING))), (Property<?>[])new Property[] { (Property)WATERLOGGED });
/*     */   }
/*     */   
/*     */   public static void placeWithRandomHeight(LevelAccessor level, RandomSource random, BlockPos stemBottomPos, Direction facing) {
/* 101 */     int desiredHeight = Mth.nextInt(random, 2, 5);
/* 103 */     BlockPos.MutableBlockPos pos = stemBottomPos.mutable();
/* 106 */     int height = 0;
/* 107 */     while (height < desiredHeight && canPlaceAt((LevelReader)level, (BlockPos)pos)) {
/* 108 */       height++;
/* 109 */       pos.move(Direction.UP);
/*     */     } 
/* 111 */     int leafY = stemBottomPos.getY() + height - 1;
/* 114 */     pos.setY(stemBottomPos.getY());
/* 115 */     while (pos.getY() < leafY) {
/* 116 */       BigDripleafStemBlock.place(level, (BlockPos)pos, level.getFluidState((BlockPos)pos), facing);
/* 117 */       pos.move(Direction.UP);
/*     */     } 
/* 121 */     place(level, (BlockPos)pos, level.getFluidState((BlockPos)pos), facing);
/*     */   }
/*     */   
/*     */   private static boolean canReplace(BlockState oldState) {
/* 125 */     return (oldState.isAir() || oldState.is(Blocks.WATER) || oldState.is(Blocks.SMALL_DRIPLEAF));
/*     */   }
/*     */   
/*     */   protected static boolean canPlaceAt(LevelReader level, BlockPos pos) {
/* 129 */     return canGrowInto(level, pos);
/*     */   }
/*     */   
/*     */   protected static boolean canGrowInto(LevelReader level, BlockPos pos) {
/* 133 */     BlockState state = level.getBlockState(pos);
/* 134 */     return (level.isInsideBuildHeight(pos) && canReplace(state));
/*     */   }
/*     */   
/*     */   protected static boolean place(LevelAccessor level, BlockPos pos, FluidState fluidState, Direction facing) {
/* 140 */     BlockState newState = (BlockState)((BlockState)Blocks.BIG_DRIPLEAF.defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf(fluidState.isSourceOfType((Fluid)Fluids.WATER)))).setValue((Property)FACING, (Comparable)facing);
/* 141 */     return level.setBlock(pos, newState, 3);
/*     */   }
/*     */   
/*     */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {
/* 146 */     setTiltAndScheduleTick(state, level, blockHit.getBlockPos(), Tilt.FULL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN);
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 151 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 152 */       return Fluids.WATER.getSource(false); 
/* 154 */     return super.getFluidState(state);
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 159 */     BlockPos belowPos = pos.below();
/* 160 */     BlockState belowState = level.getBlockState(belowPos);
/* 161 */     return (belowState.is(this) || belowState.is(Blocks.BIG_DRIPLEAF_STEM) || belowState.is(BlockTags.SUPPORTS_BIG_DRIPLEAF));
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 166 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos))
/* 167 */       return Blocks.AIR.defaultBlockState(); 
/* 169 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 170 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 173 */     if (directionToNeighbour == Direction.UP && neighbourState.is(this))
/* 174 */       return Blocks.BIG_DRIPLEAF_STEM.withPropertiesOf(state); 
/* 177 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 182 */     return canGrowInto(level, pos.above());
/*     */   }
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 187 */     return true;
/*     */   }
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 192 */     BlockPos abovePos = pos.above();
/* 193 */     if (canPlaceAt((LevelReader)level, abovePos)) {
/* 194 */       Direction facing = (Direction)state.getValue((Property)FACING);
/* 195 */       BigDripleafStemBlock.place((LevelAccessor)level, pos, state.getFluidState(), facing);
/* 196 */       place((LevelAccessor)level, abovePos, level.getBlockState(abovePos).getFluidState(), facing);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 202 */     if (level.isClientSide())
/*     */       return; 
/* 206 */     if (state.getValue((Property)TILT) == Tilt.NONE && canEntityTilt(pos, entity) && !level.hasNeighborSignal(pos))
/* 207 */       setTiltAndScheduleTick(state, level, pos, Tilt.UNSTABLE, (SoundEvent)null); 
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 213 */     if (level.hasNeighborSignal(pos)) {
/* 214 */       resetTilt(state, (Level)level, pos);
/*     */       return;
/*     */     } 
/* 218 */     Tilt tilt = (Tilt)state.getValue((Property)TILT);
/* 220 */     if (tilt == Tilt.UNSTABLE) {
/* 221 */       setTiltAndScheduleTick(state, (Level)level, pos, Tilt.PARTIAL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN);
/* 222 */     } else if (tilt == Tilt.PARTIAL) {
/* 223 */       setTiltAndScheduleTick(state, (Level)level, pos, Tilt.FULL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN);
/* 224 */     } else if (tilt == Tilt.FULL) {
/* 225 */       resetTilt(state, (Level)level, pos);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 231 */     if (level.hasNeighborSignal(pos))
/* 232 */       resetTilt(state, level, pos); 
/*     */   }
/*     */   
/*     */   private static void playTiltSound(Level level, BlockPos pos, SoundEvent tiltSound) {
/* 237 */     float pitch = Mth.randomBetween(level.getRandom(), 0.8F, 1.2F);
/* 238 */     level.playSound(null, pos, tiltSound, SoundSource.BLOCKS, 1.0F, pitch);
/*     */   }
/*     */   
/*     */   private static boolean canEntityTilt(BlockPos pos, Entity entity) {
/* 243 */     return (entity.onGround() && (entity.position()).y > (pos.getY() + 0.6875F));
/*     */   }
/*     */   
/*     */   private void setTiltAndScheduleTick(BlockState state, Level level, BlockPos pos, Tilt tilt, SoundEvent sound) {
/* 247 */     setTilt(state, level, pos, tilt);
/* 248 */     if (sound != null)
/* 249 */       playTiltSound(level, pos, sound); 
/* 251 */     int tickDelay = DELAY_UNTIL_NEXT_TILT_STATE.getInt(tilt);
/* 252 */     if (tickDelay != -1)
/* 253 */       level.scheduleTick(pos, this, tickDelay); 
/*     */   }
/*     */   
/*     */   private static void resetTilt(BlockState state, Level level, BlockPos pos) {
/* 258 */     setTilt(state, level, pos, Tilt.NONE);
/* 259 */     if (state.getValue((Property)TILT) != Tilt.NONE)
/* 260 */       playTiltSound(level, pos, SoundEvents.BIG_DRIPLEAF_TILT_UP); 
/*     */   }
/*     */   
/*     */   private static void setTilt(BlockState state, Level level, BlockPos pos, Tilt tilt) {
/* 265 */     Tilt previousTilt = (Tilt)state.getValue((Property)TILT);
/* 266 */     level.setBlock(pos, (BlockState)state.setValue((Property)TILT, (Comparable)tilt), 2);
/* 267 */     if (tilt.causesVibration() && tilt != previousTilt)
/* 268 */       level.gameEvent(null, (Holder)GameEvent.BLOCK_CHANGE, pos); 
/*     */   }
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 274 */     return SHAPE_LEAF.get(state.getValue((Property)TILT));
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 279 */     return this.shapes.apply(state);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 284 */     BlockState belowState = context.getLevel().getBlockState(context.getClickedPos().below());
/* 285 */     FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 286 */     boolean belowIsDripleafPart = (belowState.is(Blocks.BIG_DRIPLEAF) || belowState.is(Blocks.BIG_DRIPLEAF_STEM));
/* 288 */     return (BlockState)((BlockState)defaultBlockState()
/* 289 */       .setValue((Property)WATERLOGGED, Boolean.valueOf(fluidState.isSourceOfType((Fluid)Fluids.WATER))))
/* 290 */       .setValue((Property)FACING, belowIsDripleafPart ? belowState.getValue((Property)FACING) : (Comparable)context.getHorizontalDirection().getOpposite());
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 295 */     builder.add(new Property[] { (Property)WATERLOGGED, (Property)FACING, (Property)TILT });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BigDripleafBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */