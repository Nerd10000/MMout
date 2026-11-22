/*     */ package net.minecraft.world.level.block.piston;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.SignalGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DirectionalBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PistonBaseBlock extends DirectionalBlock {
/*     */   public static final MapCodec<PistonBaseBlock> CODEC;
/*     */   
/*     */   static {
/*  48 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.fieldOf("sticky").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, PistonBaseBlock::new));
/*     */   }
/*     */   
/*     */   public MapCodec<PistonBaseBlock> codec() {
/*  55 */     return CODEC;
/*     */   }
/*     */   
/*  58 */   public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;
/*     */   
/*     */   public static final int TRIGGER_EXTEND = 0;
/*     */   
/*     */   public static final int TRIGGER_CONTRACT = 1;
/*     */   
/*     */   public static final int TRIGGER_DROP = 2;
/*     */   
/*     */   public static final int PLATFORM_THICKNESS = 4;
/*     */   
/*  65 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(Block.boxZ(16.0D, 4.0D, 16.0D));
/*     */   
/*     */   private final boolean isSticky;
/*     */   
/*     */   public PistonBaseBlock(boolean isSticky, BlockBehaviour.Properties properties) {
/*  70 */     super(properties);
/*  71 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)EXTENDED, Boolean.valueOf(false)));
/*  72 */     this.isSticky = isSticky;
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  77 */     if (((Boolean)state.getValue((Property)EXTENDED)).booleanValue())
/*  78 */       return SHAPES.get(state.getValue((Property)FACING)); 
/*  80 */     return Shapes.block();
/*     */   }
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/*  85 */     if (!level.isClientSide())
/*  86 */       checkIfExtend(level, pos, state); 
/*     */   }
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  92 */     if (!level.isClientSide())
/*  93 */       checkIfExtend(level, pos, state); 
/*     */   }
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/*  99 */     if (oldState.is(state.getBlock()))
/*     */       return; 
/* 102 */     if (!level.isClientSide() && level.getBlockEntity(pos) == null)
/* 103 */       checkIfExtend(level, pos, state); 
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 109 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getNearestLookingDirection().getOpposite())).setValue((Property)EXTENDED, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   private void checkIfExtend(Level level, BlockPos pos, BlockState state) {
/* 113 */     Direction direction = (Direction)state.getValue((Property)FACING);
/* 115 */     boolean extend = getNeighborSignal((SignalGetter)level, pos, direction);
/* 117 */     if (extend && !((Boolean)state.getValue((Property)EXTENDED)).booleanValue()) {
/* 118 */       if ((new PistonStructureResolver(level, pos, direction, true)).resolve())
/* 119 */         level.blockEvent(pos, (Block)this, 0, direction.get3DDataValue()); 
/* 121 */     } else if (!extend && ((Boolean)state.getValue((Property)EXTENDED)).booleanValue()) {
/* 122 */       BlockPos pushedPos = pos.relative(direction, 2);
/* 123 */       BlockState pushedState = level.getBlockState(pushedPos);
/* 125 */       int event = 1;
/* 126 */       if (pushedState.is(Blocks.MOVING_PISTON) && pushedState.getValue((Property)FACING) == direction) {
/* 127 */         BlockEntity entity = level.getBlockEntity(pushedPos);
/* 129 */         if (entity instanceof PistonMovingBlockEntity) {
/* 129 */           PistonMovingBlockEntity pistonEntity = (PistonMovingBlockEntity)entity;
/* 130 */           if (pistonEntity.isExtending() && (pistonEntity.getProgress(0.0F) < 0.5F || level.getGameTime() == pistonEntity.getLastTicked() || ((ServerLevel)level).isHandlingTick()))
/* 131 */             event = 2; 
/*     */         } 
/*     */       } 
/* 136 */       level.blockEvent(pos, (Block)this, event, direction.get3DDataValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean getNeighborSignal(SignalGetter level, BlockPos pos, Direction pushDirection) {
/* 145 */     for (Direction direction : Direction.values()) {
/* 146 */       if (direction != pushDirection && level.hasSignal(pos.relative(direction), direction))
/* 147 */         return true; 
/*     */     } 
/* 152 */     if (level.hasSignal(pos, Direction.DOWN))
/* 153 */       return true; 
/* 156 */     BlockPos above = pos.above();
/* 157 */     for (Direction direction : Direction.values()) {
/* 158 */       if (direction != Direction.DOWN && level.hasSignal(above.relative(direction), direction))
/* 159 */         return true; 
/*     */     } 
/* 163 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
/* 168 */     Direction direction = (Direction)state.getValue((Property)FACING);
/* 169 */     BlockState extendedState = (BlockState)state.setValue((Property)EXTENDED, Boolean.valueOf(true));
/* 170 */     if (!level.isClientSide()) {
/* 171 */       boolean extend = getNeighborSignal((SignalGetter)level, pos, direction);
/* 173 */       if (extend && (b0 == 1 || b0 == 2)) {
/* 175 */         level.setBlock(pos, extendedState, 2);
/* 176 */         return false;
/*     */       } 
/* 177 */       if (!extend && b0 == 0)
/* 178 */         return false; 
/*     */     } 
/* 182 */     RandomSource random = level.getRandom();
/* 183 */     if (b0 == 0) {
/* 184 */       if (moveBlocks(level, pos, direction, true)) {
/* 185 */         level.setBlock(pos, extendedState, 67);
/* 186 */         level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.25F + 0.6F);
/* 187 */         level.gameEvent((Holder)GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Context.of(extendedState));
/*     */       } else {
/* 189 */         return false;
/*     */       } 
/* 191 */     } else if (b0 == 1 || b0 == 2) {
/* 192 */       BlockEntity prevBlockEntity = level.getBlockEntity(pos.relative(direction));
/* 193 */       if (prevBlockEntity instanceof PistonMovingBlockEntity) {
/* 193 */         PistonMovingBlockEntity pistonMovingBlockEntity = (PistonMovingBlockEntity)prevBlockEntity;
/* 194 */         pistonMovingBlockEntity.finalTick();
/*     */       } 
/* 197 */       BlockState movingPistonState = (BlockState)((BlockState)Blocks.MOVING_PISTON.defaultBlockState().setValue((Property)MovingPistonBlock.FACING, (Comparable)direction)).setValue((Property)MovingPistonBlock.TYPE, this.isSticky ? (Comparable)PistonType.STICKY : (Comparable)PistonType.DEFAULT);
/* 198 */       level.setBlock(pos, movingPistonState, 276);
/* 199 */       level.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(pos, movingPistonState, (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)Direction.from3DDataValue(b1 & 0x7)), direction, false, true));
/* 201 */       level.updateNeighborsAt(pos, movingPistonState.getBlock());
/* 202 */       movingPistonState.updateNeighbourShapes((LevelAccessor)level, pos, 2);
/* 205 */       if (this.isSticky) {
/* 206 */         BlockPos twoPos = pos.offset(direction.getStepX() * 2, direction.getStepY() * 2, direction.getStepZ() * 2);
/* 207 */         BlockState movingState = level.getBlockState(twoPos);
/* 208 */         boolean pistonPiece = false;
/* 210 */         if (movingState.is(Blocks.MOVING_PISTON)) {
/* 213 */           BlockEntity blockEntity = level.getBlockEntity(twoPos);
/* 214 */           if (blockEntity instanceof PistonMovingBlockEntity) {
/* 214 */             PistonMovingBlockEntity entity = (PistonMovingBlockEntity)blockEntity;
/* 215 */             if (entity.getDirection() == direction && entity.isExtending()) {
/* 217 */               entity.finalTick();
/* 218 */               pistonPiece = true;
/*     */             } 
/*     */           } 
/*     */         } 
/* 223 */         if (!pistonPiece)
/* 224 */           if (b0 == 1 && !movingState.isAir() && isPushable(movingState, level, twoPos, direction.getOpposite(), false, direction) && (movingState.getPistonPushReaction() == PushReaction.NORMAL || movingState.is(Blocks.PISTON) || movingState.is(Blocks.STICKY_PISTON))) {
/* 225 */             moveBlocks(level, pos, direction, false);
/*     */           } else {
/* 227 */             level.removeBlock(pos.relative(direction), false);
/*     */           }  
/*     */       } else {
/* 231 */         level.removeBlock(pos.relative(direction), false);
/*     */       } 
/* 234 */       level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.15F + 0.6F);
/* 235 */       level.gameEvent((Holder)GameEvent.BLOCK_DEACTIVATE, pos, GameEvent.Context.of(movingPistonState));
/*     */     } 
/* 237 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isPushable(BlockState state, Level level, BlockPos pos, Direction direction, boolean allowDestroyable, Direction connectionDirection) {
/* 241 */     if (pos.getY() < level.getMinY() || pos.getY() > level.getMaxY() || !level.getWorldBorder().isWithinBounds(pos))
/* 242 */       return false; 
/* 246 */     if (state.isAir())
/* 247 */       return true; 
/* 251 */     if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(Blocks.RESPAWN_ANCHOR) || state.is(Blocks.REINFORCED_DEEPSLATE))
/* 252 */       return false; 
/* 255 */     if (direction == Direction.DOWN && pos.getY() == level.getMinY())
/* 256 */       return false; 
/* 259 */     if (direction == Direction.UP && pos.getY() == level.getMaxY())
/* 260 */       return false; 
/* 263 */     if (state.is(Blocks.PISTON) || state.is(Blocks.STICKY_PISTON)) {
/* 265 */       if (((Boolean)state.getValue((Property)EXTENDED)).booleanValue())
/* 266 */         return false; 
/*     */     } else {
/* 269 */       if (state.getDestroySpeed((BlockGetter)level, pos) == -1.0F)
/* 270 */         return false; 
/* 273 */       switch (state.getPistonPushReaction()) {
/*     */         case BLOCK:
/* 275 */           return false;
/*     */         case DESTROY:
/* 278 */           return allowDestroyable;
/*     */         case PUSH_ONLY:
/* 281 */           return (direction == connectionDirection);
/*     */       } 
/*     */     } 
/* 287 */     return !state.hasBlockEntity();
/*     */   }
/*     */   
/*     */   private boolean moveBlocks(Level level, BlockPos pistonPos, Direction direction, boolean extending) {
/* 291 */     BlockPos armPos = pistonPos.relative(direction);
/* 292 */     if (!extending && level.getBlockState(armPos).is(Blocks.PISTON_HEAD))
/* 294 */       level.setBlock(armPos, Blocks.AIR.defaultBlockState(), 276); 
/* 297 */     PistonStructureResolver resolver = new PistonStructureResolver(level, pistonPos, direction, extending);
/* 298 */     if (!resolver.resolve())
/* 299 */       return false; 
/* 302 */     Map<BlockPos, BlockState> deleteAfterMove = Maps.newHashMap();
/* 303 */     List<BlockPos> toPush = resolver.getToPush();
/* 304 */     List<BlockState> toPushShapes = Lists.newArrayList();
/* 305 */     for (BlockPos pos : toPush) {
/* 306 */       BlockState state = level.getBlockState(pos);
/* 307 */       toPushShapes.add(state);
/* 308 */       deleteAfterMove.put(pos, state);
/*     */     } 
/* 310 */     List<BlockPos> toDestroy = resolver.getToDestroy();
/* 312 */     BlockState[] toUpdate = new BlockState[toPush.size() + toDestroy.size()];
/* 313 */     Direction pushDirection = extending ? direction : direction.getOpposite();
/* 315 */     int updateIndex = 0;
/*     */     int i;
/* 317 */     for (i = toDestroy.size() - 1; i >= 0; i--) {
/* 318 */       BlockPos pos = toDestroy.get(i);
/* 320 */       BlockState state = level.getBlockState(pos);
/* 322 */       BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
/* 324 */       dropResources(state, (LevelAccessor)level, pos, blockEntity);
/* 325 */       if (!state.is(BlockTags.FIRE) && level.isClientSide())
/* 326 */         level.levelEvent(2001, pos, getId(state)); 
/* 328 */       level.setBlock(pos, Blocks.AIR.defaultBlockState(), 18);
/* 329 */       level.gameEvent((Holder)GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
/* 331 */       toUpdate[updateIndex++] = state;
/*     */     } 
/* 335 */     for (i = toPush.size() - 1; i >= 0; i--) {
/* 336 */       BlockPos pos = toPush.get(i);
/* 337 */       BlockState blockState = level.getBlockState(pos);
/* 339 */       pos = pos.relative(pushDirection);
/* 341 */       deleteAfterMove.remove(pos);
/* 343 */       BlockState actualState = (BlockState)Blocks.MOVING_PISTON.defaultBlockState().setValue((Property)FACING, (Comparable)direction);
/* 344 */       level.setBlock(pos, actualState, 324);
/* 345 */       level.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(pos, actualState, toPushShapes.get(i), direction, extending, false));
/* 347 */       toUpdate[updateIndex++] = blockState;
/*     */     } 
/* 350 */     if (extending) {
/* 351 */       PistonType type = this.isSticky ? PistonType.STICKY : PistonType.DEFAULT;
/* 352 */       BlockState state = (BlockState)((BlockState)Blocks.PISTON_HEAD.defaultBlockState().setValue((Property)PistonHeadBlock.FACING, (Comparable)direction)).setValue((Property)PistonHeadBlock.TYPE, (Comparable)type);
/* 356 */       BlockState blockState = (BlockState)((BlockState)Blocks.MOVING_PISTON.defaultBlockState().setValue((Property)MovingPistonBlock.FACING, (Comparable)direction)).setValue((Property)MovingPistonBlock.TYPE, this.isSticky ? (Comparable)PistonType.STICKY : (Comparable)PistonType.DEFAULT);
/* 358 */       deleteAfterMove.remove(armPos);
/* 360 */       level.setBlock(armPos, blockState, 324);
/* 361 */       level.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(armPos, blockState, state, direction, true, true));
/*     */     } 
/* 364 */     BlockState air = Blocks.AIR.defaultBlockState();
/* 365 */     for (BlockPos pos : deleteAfterMove.keySet())
/* 366 */       level.setBlock(pos, air, 82); 
/* 369 */     for (Map.Entry<BlockPos, BlockState> entry : deleteAfterMove.entrySet()) {
/* 370 */       BlockPos pos = entry.getKey();
/* 371 */       BlockState oldState = entry.getValue();
/* 372 */       oldState.updateIndirectNeighbourShapes((LevelAccessor)level, pos, 2);
/* 373 */       air.updateNeighbourShapes((LevelAccessor)level, pos, 2);
/* 374 */       air.updateIndirectNeighbourShapes((LevelAccessor)level, pos, 2);
/*     */     } 
/* 377 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, resolver.getPushDirection(), null);
/* 378 */     updateIndex = 0;
/*     */     int j;
/* 380 */     for (j = toDestroy.size() - 1; j >= 0; j--) {
/* 381 */       BlockState state = toUpdate[updateIndex++];
/* 382 */       BlockPos pos = toDestroy.get(j);
/* 383 */       if (level instanceof ServerLevel) {
/* 383 */         ServerLevel serverLevel = (ServerLevel)level;
/* 384 */         state.affectNeighborsAfterRemoval(serverLevel, pos, false);
/*     */       } 
/* 386 */       state.updateIndirectNeighbourShapes((LevelAccessor)level, pos, 2);
/* 387 */       level.updateNeighborsAt(pos, state.getBlock(), orientation);
/*     */     } 
/* 391 */     for (j = toPush.size() - 1; j >= 0; j--)
/* 392 */       level.updateNeighborsAt(toPush.get(j), toUpdate[updateIndex++].getBlock(), orientation); 
/* 395 */     if (extending)
/* 396 */       level.updateNeighborsAt(armPos, Blocks.PISTON_HEAD, orientation); 
/* 399 */     return true;
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 404 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 409 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 414 */     builder.add(new Property[] { (Property)FACING, (Property)EXTENDED });
/*     */   }
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState state) {
/* 419 */     return ((Boolean)state.getValue((Property)EXTENDED)).booleanValue();
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 424 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\piston\PistonBaseBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */