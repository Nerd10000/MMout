/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.DustColorTransitionOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.SculkSensorBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SculkSensorBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*  46 */   public static final MapCodec<SculkSensorBlock> CODEC = simpleCodec(SculkSensorBlock::new);
/*     */   
/*     */   public static final int ACTIVE_TICKS = 30;
/*     */   
/*     */   public static final int COOLDOWN_TICKS = 10;
/*     */   
/*     */   public MapCodec<? extends SculkSensorBlock> codec() {
/*  50 */     return CODEC;
/*     */   }
/*     */   
/*  56 */   public static final EnumProperty<SculkSensorPhase> PHASE = BlockStateProperties.SCULK_SENSOR_PHASE;
/*     */   
/*  57 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*     */   
/*  58 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  60 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 8.0D);
/*     */   
/*     */   private static final float[] RESONANCE_PITCH_BEND;
/*     */   
/*     */   static {
/*  62 */     RESONANCE_PITCH_BEND = (float[])Util.make(new float[16], arr -> {
/*     */           int[] toneMap = { 
/*     */               0, 0, 2, 4, 6, 7, 9, 10, 12, 14, 
/*     */               15, 18, 19, 21, 22, 24 };
/*     */           for (int i = 0; i < 16; i++)
/*     */             arr[i] = NoteBlock.getPitchFromNote(toneMap[i]); 
/*     */         });
/*     */   }
/*     */   
/*     */   public SculkSensorBlock(BlockBehaviour.Properties properties) {
/*  76 */     super(properties);
/*  77 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)PHASE, (Comparable)SculkSensorPhase.INACTIVE)).setValue((Property)POWER, Integer.valueOf(0))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  82 */     BlockPos pos = context.getClickedPos();
/*  83 */     FluidState replacedFluidState = context.getLevel().getFluidState(pos);
/*  85 */     return (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf(replacedFluidState.is(Fluids.WATER)));
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/*  90 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/*  91 */       return Fluids.WATER.getSource(false); 
/*  93 */     return super.getFluidState(state);
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  98 */     if (getPhase(state) != SculkSensorPhase.ACTIVE) {
/*  99 */       if (getPhase(state) == SculkSensorPhase.COOLDOWN) {
/* 100 */         level.setBlock(pos, (BlockState)state.setValue((Property)PHASE, (Comparable)SculkSensorPhase.INACTIVE), 3);
/* 101 */         if (!((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 102 */           level.playSound(null, pos, SoundEvents.SCULK_CLICKING_STOP, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.2F + 0.8F); 
/*     */       } 
/*     */       return;
/*     */     } 
/* 109 */     deactivate((Level)level, pos, state);
/*     */   }
/*     */   
/*     */   public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
/* 114 */     if (!level.isClientSide() && canActivate(onState) && !entity.is(EntityTypes.WARDEN)) {
/* 115 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 116 */       if (blockEntity instanceof SculkSensorBlockEntity) {
/* 116 */         SculkSensorBlockEntity sculkSensor = (SculkSensorBlockEntity)blockEntity;
/* 116 */         if (level instanceof ServerLevel) {
/* 116 */           ServerLevel serverLevel = (ServerLevel)level;
/* 117 */           if (sculkSensor.getVibrationUser().canReceiveVibration(serverLevel, pos, (Holder)GameEvent.STEP, GameEvent.Context.of(onState)))
/* 118 */             sculkSensor.getListener().forceScheduleVibration(serverLevel, (Holder)GameEvent.STEP, GameEvent.Context.of(entity), entity.position()); 
/*     */         } 
/*     */       } 
/*     */     } 
/* 122 */     super.stepOn(level, pos, onState, entity);
/*     */   }
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 127 */     if (level.isClientSide() || state.is(oldState.getBlock()))
/*     */       return; 
/* 131 */     if (((Integer)state.getValue((Property)POWER)).intValue() > 0 && !level.getBlockTicks().hasScheduledTick(pos, this))
/* 132 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWER, Integer.valueOf(0)), 18); 
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 138 */     if (getPhase(state) == SculkSensorPhase.ACTIVE)
/* 139 */       updateNeighbours((Level)level, pos, state); 
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 145 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 146 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 148 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   private static void updateNeighbours(Level level, BlockPos pos, BlockState state) {
/* 152 */     Block block = state.getBlock();
/* 153 */     level.updateNeighborsAt(pos, block);
/* 154 */     level.updateNeighborsAt(pos.below(), block);
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 159 */     return (BlockEntity)new SculkSensorBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 164 */     if (!level.isClientSide())
/* 165 */       return createTickerHelper(type, BlockEntityTypes.SCULK_SENSOR, (innerLevel, pos, state, entity) -> VibrationSystem.Ticker.tick(innerLevel, entity.getVibrationData(), entity.getVibrationUser())); 
/* 168 */     return null;
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 173 */     return SHAPE;
/*     */   }
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/* 178 */     return true;
/*     */   }
/*     */   
/*     */   protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
/* 183 */     return ((Integer)state.getValue((Property)POWER)).intValue();
/*     */   }
/*     */   
/*     */   public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 188 */     if (direction == Direction.UP)
/* 189 */       return state.getSignal(level, pos, direction); 
/* 192 */     return 0;
/*     */   }
/*     */   
/*     */   public static SculkSensorPhase getPhase(BlockState state) {
/* 196 */     return (SculkSensorPhase)state.getValue((Property)PHASE);
/*     */   }
/*     */   
/*     */   public static boolean canActivate(BlockState state) {
/* 200 */     return (getPhase(state) == SculkSensorPhase.INACTIVE);
/*     */   }
/*     */   
/*     */   public static void deactivate(Level level, BlockPos pos, BlockState state) {
/* 204 */     level.setBlock(pos, (BlockState)((BlockState)state.setValue((Property)PHASE, (Comparable)SculkSensorPhase.COOLDOWN)).setValue((Property)POWER, Integer.valueOf(0)), 3);
/* 205 */     level.scheduleTick(pos, state.getBlock(), 10);
/* 206 */     updateNeighbours(level, pos, state);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getActiveTicks() {
/* 211 */     return 30;
/*     */   }
/*     */   
/*     */   public void activate(Entity sourceEntity, Level level, BlockPos pos, BlockState state, int calculatedPower, int vibrationFrequency) {
/* 215 */     level.setBlock(pos, (BlockState)((BlockState)state.setValue((Property)PHASE, (Comparable)SculkSensorPhase.ACTIVE)).setValue((Property)POWER, Integer.valueOf(calculatedPower)), 3);
/* 217 */     level.scheduleTick(pos, state.getBlock(), getActiveTicks());
/* 218 */     updateNeighbours(level, pos, state);
/* 219 */     tryResonateVibration(sourceEntity, level, pos, vibrationFrequency);
/* 220 */     level.gameEvent(sourceEntity, (Holder)GameEvent.SCULK_SENSOR_TENDRILS_CLICKING, pos);
/* 221 */     if (!((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 222 */       level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.SCULK_CLICKING, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.2F + 0.8F); 
/*     */   }
/*     */   
/*     */   public static void tryResonateVibration(Entity sourceEntity, Level level, BlockPos pos, int vibrationFrequency) {
/* 227 */     for (Direction direction : Direction.values()) {
/* 228 */       BlockPos relativePos = pos.relative(direction);
/* 229 */       BlockState blockState = level.getBlockState(relativePos);
/* 230 */       if (blockState.is(BlockTags.VIBRATION_RESONATORS)) {
/* 231 */         level.gameEvent(VibrationSystem.getResonanceEventByFrequency(vibrationFrequency), relativePos, GameEvent.Context.of(sourceEntity, blockState));
/* 232 */         float pitch = RESONANCE_PITCH_BEND[vibrationFrequency];
/* 233 */         level.playSound(null, relativePos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, pitch);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 240 */     if (getPhase(state) != SculkSensorPhase.ACTIVE)
/*     */       return; 
/* 244 */     Direction dir = Direction.getRandom(random);
/* 246 */     if (dir == Direction.UP || dir == Direction.DOWN)
/*     */       return; 
/* 250 */     double x = pos.getX() + 0.5D + ((dir.getStepX() == 0) ? (0.5D - random.nextDouble()) : (dir.getStepX() * 0.6D));
/* 251 */     double y = pos.getY() + 0.25D;
/* 252 */     double z = pos.getZ() + 0.5D + ((dir.getStepZ() == 0) ? (0.5D - random.nextDouble()) : (dir.getStepZ() * 0.6D));
/* 253 */     double ya = random.nextFloat() * 0.04D;
/* 254 */     level.addParticle((ParticleOptions)DustColorTransitionOptions.SCULK_TO_REDSTONE, x, y, z, 0.0D, ya, 0.0D);
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 259 */     builder.add(new Property[] { (Property)PHASE, (Property)POWER, (Property)WATERLOGGED });
/*     */   }
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 264 */     return true;
/*     */   }
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 269 */     BlockEntity entity = level.getBlockEntity(pos);
/* 271 */     if (entity instanceof SculkSensorBlockEntity) {
/* 271 */       SculkSensorBlockEntity sculk = (SculkSensorBlockEntity)entity;
/* 272 */       return (getPhase(state) == SculkSensorPhase.ACTIVE) ? sculk.getLastVibrationFrequency() : 0;
/*     */     } 
/* 275 */     return 0;
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 280 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState state) {
/* 285 */     return true;
/*     */   }
/*     */   
/*     */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 290 */     super.spawnAfterBreak(state, level, pos, tool, dropExperience);
/* 291 */     if (dropExperience)
/* 292 */       tryDropExperience(level, pos, tool, (IntProvider)ConstantInt.of(5)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SculkSensorBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */