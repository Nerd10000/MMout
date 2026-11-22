/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.stats.Stat;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.CompoundContainer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.animal.feline.Cat;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.LidBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ChestBlock extends AbstractChestBlock<ChestBlockEntity> implements SimpleWaterloggedBlock {
/*     */   public static final MapCodec<ChestBlock> CODEC;
/*     */   
/*     */   static {
/*  65 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("open_sound").forGetter(ChestBlock::getOpenChestSound), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("close_sound").forGetter(ChestBlock::getCloseChestSound), (App)propertiesCodec()).apply((Applicative)i, ()));
/*     */   }
/*     */   
/*     */   public MapCodec<? extends ChestBlock> codec() {
/*  73 */     return CODEC;
/*     */   }
/*     */   
/*  76 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  77 */   public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;
/*     */   
/*  78 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*     */   public static final int EVENT_SET_OPEN_COUNT = 1;
/*     */   
/*  81 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 14.0D);
/*     */   
/*  82 */   private static final Map<Direction, VoxelShape> HALF_SHAPES = Shapes.rotateHorizontal(Block.boxZ(14.0D, 0.0D, 14.0D, 0.0D, 15.0D));
/*     */   
/*     */   private final SoundEvent openSound;
/*     */   
/*     */   private final SoundEvent closeSound;
/*     */   
/*     */   protected ChestBlock(Supplier<BlockEntityType<? extends ChestBlockEntity>> blockEntityType, SoundEvent openSound, SoundEvent closeSound, BlockBehaviour.Properties properties) {
/*  88 */     super(properties, blockEntityType);
/*  89 */     this.openSound = openSound;
/*  90 */     this.closeSound = closeSound;
/*  91 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)TYPE, (Comparable)ChestType.SINGLE)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   public static DoubleBlockCombiner.BlockType getBlockType(BlockState state) {
/*  95 */     ChestType type = (ChestType)state.getValue((Property)TYPE);
/*  96 */     if (type == ChestType.SINGLE)
/*  97 */       return DoubleBlockCombiner.BlockType.SINGLE; 
/*  99 */     if (type == ChestType.RIGHT)
/* 100 */       return DoubleBlockCombiner.BlockType.FIRST; 
/* 102 */     return DoubleBlockCombiner.BlockType.SECOND;
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 107 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 108 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 110 */     if (chestCanConnectTo(neighbourState) && directionToNeighbour.getAxis().isHorizontal()) {
/* 111 */       ChestType neighbourType = (ChestType)neighbourState.getValue((Property)TYPE);
/* 112 */       if (state.getValue((Property)TYPE) == ChestType.SINGLE && neighbourType != ChestType.SINGLE && 
/* 113 */         state.getValue((Property)FACING) == neighbourState.getValue((Property)FACING) && getConnectedDirection(neighbourState) == directionToNeighbour.getOpposite())
/* 114 */         return (BlockState)state.setValue((Property)TYPE, (Comparable)neighbourType.getOpposite()); 
/* 117 */     } else if (getConnectedDirection(state) == directionToNeighbour) {
/* 118 */       return (BlockState)state.setValue((Property)TYPE, (Comparable)ChestType.SINGLE);
/*     */     } 
/* 120 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   public boolean chestCanConnectTo(BlockState blockState) {
/* 124 */     return blockState.is(this);
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 129 */     switch ((ChestType)state.getValue((Property)TYPE)) {
/*     */       default:
/* 129 */         throw new MatchException(null, null);
/*     */       case SINGLE:
/*     */       
/*     */       case LEFT:
/*     */       case RIGHT:
/*     */         break;
/*     */     } 
/* 129 */     return 
/*     */       
/* 131 */       HALF_SHAPES.get(getConnectedDirection(state));
/*     */   }
/*     */   
/*     */   public static Direction getConnectedDirection(BlockState state) {
/* 136 */     Direction facing = (Direction)state.getValue((Property)FACING);
/* 137 */     return (state.getValue((Property)TYPE) == ChestType.LEFT) ? facing.getClockWise() : facing.getCounterClockWise();
/*     */   }
/*     */   
/*     */   public static BlockPos getConnectedBlockPos(BlockPos pos, BlockState state) {
/* 141 */     Direction connectedDirection = getConnectedDirection(state);
/* 142 */     return pos.relative(connectedDirection);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 147 */     ChestType type = ChestType.SINGLE;
/* 148 */     Direction facingDirection = context.getHorizontalDirection().getOpposite();
/* 149 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 151 */     boolean secondaryUse = context.isSecondaryUseActive();
/* 152 */     Direction clickedFace = context.getClickedFace();
/* 154 */     if (clickedFace.getAxis().isHorizontal() && secondaryUse) {
/* 155 */       Direction neighbourFacing = candidatePartnerFacing(context.getLevel(), context.getClickedPos(), clickedFace.getOpposite());
/* 156 */       if (neighbourFacing != null && neighbourFacing.getAxis() != clickedFace.getAxis()) {
/* 157 */         facingDirection = neighbourFacing;
/* 158 */         type = (facingDirection.getCounterClockWise() == clickedFace.getOpposite()) ? ChestType.RIGHT : ChestType.LEFT;
/*     */       } 
/*     */     } 
/* 161 */     if (type == ChestType.SINGLE && !secondaryUse)
/* 162 */       type = getChestType(context.getLevel(), context.getClickedPos(), facingDirection); 
/* 165 */     return (BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)facingDirection)).setValue((Property)TYPE, (Comparable)type)).setValue((Property)WATERLOGGED, Boolean.valueOf(replacedFluidState.is(Fluids.WATER)));
/*     */   }
/*     */   
/*     */   protected ChestType getChestType(Level level, BlockPos pos, Direction facingDirection) {
/* 169 */     if (facingDirection == candidatePartnerFacing(level, pos, facingDirection.getClockWise()))
/* 170 */       return ChestType.LEFT; 
/* 171 */     if (facingDirection == candidatePartnerFacing(level, pos, facingDirection.getCounterClockWise()))
/* 172 */       return ChestType.RIGHT; 
/* 174 */     return ChestType.SINGLE;
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 179 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 180 */       return Fluids.WATER.getSource(false); 
/* 182 */     return super.getFluidState(state);
/*     */   }
/*     */   
/*     */   private Direction candidatePartnerFacing(Level level, BlockPos pos, Direction neighbourDirection) {
/* 186 */     BlockState state = level.getBlockState(pos.relative(neighbourDirection));
/* 187 */     return (chestCanConnectTo(state) && state.getValue((Property)TYPE) == ChestType.SINGLE) ? (Direction)state.getValue((Property)FACING) : null;
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 192 */     Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 197 */     if (level instanceof ServerLevel) {
/* 197 */       ServerLevel serverLevel = (ServerLevel)level;
/* 198 */       MenuProvider menuProvider = getMenuProvider(state, level, pos);
/* 199 */       if (menuProvider != null) {
/* 200 */         player.openMenu(menuProvider);
/* 201 */         player.awardStat(getOpenChestStat());
/* 202 */         PiglinAi.angerNearbyPiglins(serverLevel, player, true);
/*     */       } 
/*     */     } 
/* 205 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected Stat<Identifier> getOpenChestStat() {
/* 209 */     return Stats.CUSTOM.get(Stats.OPEN_CHEST);
/*     */   }
/*     */   
/*     */   public BlockEntityType<? extends ChestBlockEntity> blockEntityType() {
/* 213 */     return this.blockEntityType.get();
/*     */   }
/*     */   
/* 216 */   private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>> CHEST_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>>() {
/*     */       public Optional<Container> acceptDouble(ChestBlockEntity first, ChestBlockEntity second) {
/* 219 */         return (Optional)Optional.of(new CompoundContainer((Container)first, (Container)second));
/*     */       }
/*     */       
/*     */       public Optional<Container> acceptSingle(ChestBlockEntity single) {
/* 224 */         return (Optional)Optional.of(single);
/*     */       }
/*     */       
/*     */       public Optional<Container> acceptNone() {
/* 229 */         return Optional.empty();
/*     */       }
/*     */     };
/*     */   
/*     */   public static Container getContainer(ChestBlock block, BlockState state, Level level, BlockPos pos, boolean ignoreBeingBlocked) {
/* 234 */     return ((Optional<Container>)block.combine(state, level, pos, ignoreBeingBlocked).<Optional<Container>>apply(CHEST_COMBINER)).orElse(null);
/*     */   }
/*     */   
/*     */   public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean ignoreBeingBlocked) {
/*     */     BiPredicate<LevelAccessor, BlockPos> predicate;
/* 240 */     if (ignoreBeingBlocked) {
/* 241 */       predicate = ((levelAccessor, blockPos) -> false);
/*     */     } else {
/* 243 */       predicate = ChestBlock::isChestBlockedAt;
/*     */     } 
/* 245 */     return DoubleBlockCombiner.combineWithNeigbour(this.blockEntityType.get(), ChestBlock::getBlockType, ChestBlock::getConnectedDirection, (Property<Direction>)FACING, state, (LevelAccessor)level, pos, predicate);
/*     */   }
/*     */   
/* 248 */   private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>>() {
/*     */       public Optional<MenuProvider> acceptDouble(final ChestBlockEntity first, final ChestBlockEntity second) {
/* 251 */         final CompoundContainer container = new CompoundContainer((Container)first, (Container)second);
/* 252 */         return Optional.of(new MenuProvider(this) {
/*     */               public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/* 255 */                 if (first.canOpen(player) && second.canOpen(player)) {
/* 256 */                   first.unpackLootTable(inventory.player);
/* 257 */                   second.unpackLootTable(inventory.player);
/* 259 */                   return (AbstractContainerMenu)ChestMenu.sixRows(containerId, inventory, container);
/*     */                 } 
/* 261 */                 Direction connectedDirection = ChestBlock.getConnectedDirection(first.getBlockState());
/* 262 */                 Vec3 firstCenter = Vec3.atCenterOf((Vec3i)first.getBlockPos());
/* 263 */                 Vec3 centerBetweenChests = firstCenter.add(connectedDirection.getStepX() / 2.0D, 0.0D, connectedDirection.getStepZ() / 2.0D);
/* 264 */                 BaseContainerBlockEntity.sendChestLockedNotifications(centerBetweenChests, player, getDisplayName());
/* 266 */                 return null;
/*     */               }
/*     */               
/*     */               public Component getDisplayName() {
/* 271 */                 if (first.hasCustomName())
/* 272 */                   return first.getDisplayName(); 
/* 274 */                 if (second.hasCustomName())
/* 275 */                   return second.getDisplayName(); 
/* 277 */                 return (Component)Component.translatable("container.chestDouble");
/*     */               }
/*     */             });
/*     */       }
/*     */       
/*     */       public Optional<MenuProvider> acceptSingle(ChestBlockEntity single) {
/* 284 */         return (Optional)Optional.of(single);
/*     */       }
/*     */       
/*     */       public Optional<MenuProvider> acceptNone() {
/* 289 */         return Optional.empty();
/*     */       }
/*     */     };
/*     */   
/*     */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
/* 295 */     return ((Optional<MenuProvider>)combine(state, level, pos, false).<Optional<MenuProvider>>apply(MENU_PROVIDER_COMBINER)).orElse(null);
/*     */   }
/*     */   
/*     */   public static DoubleBlockCombiner.Combiner<ChestBlockEntity, Float2FloatFunction> opennessCombiner(final LidBlockEntity entity) {
/* 299 */     return new DoubleBlockCombiner.Combiner<ChestBlockEntity, Float2FloatFunction>() {
/*     */         public Float2FloatFunction acceptDouble(ChestBlockEntity first, ChestBlockEntity second) {
/* 302 */           return partialTickTime -> Math.max(first.getOpenNess(partialTickTime), second.getOpenNess(partialTickTime));
/*     */         }
/*     */         
/*     */         public Float2FloatFunction acceptSingle(ChestBlockEntity single) {
/* 307 */           Objects.requireNonNull(single);
/* 307 */           return single::getOpenNess;
/*     */         }
/*     */         
/*     */         public Float2FloatFunction acceptNone() {
/* 312 */           Objects.requireNonNull(entity);
/* 312 */           return entity::getOpenNess;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 319 */     return (BlockEntity)new ChestBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 324 */     return level.isClientSide() ? createTickerHelper(type, (BlockEntityType)blockEntityType(), ChestBlockEntity::lidAnimateTick) : null;
/*     */   }
/*     */   
/*     */   public static boolean isChestBlockedAt(LevelAccessor level, BlockPos pos) {
/* 328 */     return (isBlockedChestByBlock((BlockGetter)level, pos) || isCatSittingOnChest(level, pos));
/*     */   }
/*     */   
/*     */   private static boolean isBlockedChestByBlock(BlockGetter level, BlockPos pos) {
/* 332 */     BlockPos above = pos.above();
/* 333 */     return level.getBlockState(above).isRedstoneConductor(level, above);
/*     */   }
/*     */   
/*     */   private static boolean isCatSittingOnChest(LevelAccessor level, BlockPos pos) {
/* 338 */     List<Cat> cats = level.getEntitiesOfClass(Cat.class, new AABB(pos.getX(), (pos.getY() + 1), pos.getZ(), (pos.getX() + 1), (pos.getY() + 2), (pos.getZ() + 1)));
/* 339 */     if (!cats.isEmpty())
/* 340 */       for (Cat cat : cats) {
/* 341 */         if (cat.isInSittingPose())
/* 342 */           return true; 
/*     */       }  
/* 346 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 351 */     return true;
/*     */   }
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 356 */     return AbstractContainerMenu.getRedstoneSignalFromContainer(getContainer(this, state, level, pos, false));
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 361 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 366 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 371 */     builder.add(new Property[] { (Property)FACING, (Property)TYPE, (Property)WATERLOGGED });
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 376 */     return false;
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 381 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 383 */     if (blockEntity instanceof ChestBlockEntity) {
/* 383 */       ChestBlockEntity chestBlockEntity = (ChestBlockEntity)blockEntity;
/* 384 */       chestBlockEntity.recheckOpen();
/*     */     } 
/*     */   }
/*     */   
/*     */   public SoundEvent getOpenChestSound() {
/* 389 */     return this.openSound;
/*     */   }
/*     */   
/*     */   public SoundEvent getCloseChestSound() {
/* 393 */     return this.closeSound;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ChestBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */