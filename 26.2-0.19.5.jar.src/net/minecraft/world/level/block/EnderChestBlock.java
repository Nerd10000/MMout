/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.SimpleMenuProvider;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.inventory.PlayerEnderChestContainer;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class EnderChestBlock extends AbstractChestBlock<EnderChestBlockEntity> implements SimpleWaterloggedBlock {
/*  45 */   public static final MapCodec<EnderChestBlock> CODEC = simpleCodec(EnderChestBlock::new);
/*     */   
/*     */   public MapCodec<EnderChestBlock> codec() {
/*  49 */     return CODEC;
/*     */   }
/*     */   
/*  52 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  53 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  55 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 14.0D);
/*     */   
/*  56 */   private static final Component CONTAINER_TITLE = (Component)Component.translatable("container.enderchest");
/*     */   
/*     */   protected EnderChestBlock(BlockBehaviour.Properties properties) {
/*  59 */     super(properties, () -> BlockEntityTypes.ENDER_CHEST);
/*  60 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean ignoreBeingBlocked) {
/*  65 */     return DoubleBlockCombiner.Combiner::acceptNone;
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  70 */     return SHAPE;
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  75 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  76 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite())).setValue((Property)WATERLOGGED, Boolean.valueOf(replacedFluidState.is(Fluids.WATER)));
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*     */     EnderChestBlockEntity enderChest;
/*  81 */     PlayerEnderChestContainer container = player.getEnderChestInventory();
/*  82 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  83 */     if (container != null && blockEntity instanceof EnderChestBlockEntity) {
/*  83 */       enderChest = (EnderChestBlockEntity)blockEntity;
/*     */     } else {
/*  84 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*  87 */     BlockPos above = pos.above();
/*  88 */     if (level.getBlockState(above).isRedstoneConductor((BlockGetter)level, above))
/*  89 */       return (InteractionResult)InteractionResult.SUCCESS; 
/*  92 */     if (level instanceof ServerLevel) {
/*  92 */       ServerLevel serverLevel = (ServerLevel)level;
/*  93 */       container.setActiveChest(enderChest);
/*  95 */       player.openMenu((MenuProvider)new SimpleMenuProvider((containerId, inventory, p) -> ChestMenu.threeRows(containerId, inventory, (Container)container), CONTAINER_TITLE));
/*  96 */       player.awardStat(Stats.OPEN_ENDERCHEST);
/*  97 */       PiglinAi.angerNearbyPiglins(serverLevel, player, true);
/*     */     } 
/*  99 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 104 */     return (BlockEntity)new EnderChestBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 109 */     return level.isClientSide() ? createTickerHelper(type, BlockEntityTypes.ENDER_CHEST, EnderChestBlockEntity::lidAnimateTick) : null;
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 114 */     for (int i = 0; i < 3; i++) {
/* 115 */       int flipX = random.nextInt(2) * 2 - 1;
/* 116 */       int flipZ = random.nextInt(2) * 2 - 1;
/* 118 */       double x = pos.getX() + 0.5D + 0.25D * flipX;
/* 119 */       double y = (pos.getY() + random.nextFloat());
/* 120 */       double z = pos.getZ() + 0.5D + 0.25D * flipZ;
/* 121 */       double xa = (random.nextFloat() * flipX);
/* 122 */       double ya = (random.nextFloat() - 0.5D) * 0.125D;
/* 123 */       double za = (random.nextFloat() * flipZ);
/* 125 */       level.addParticle((ParticleOptions)ParticleTypes.PORTAL, x, y, z, xa, ya, za);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 131 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 136 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 141 */     builder.add(new Property[] { (Property)FACING, (Property)WATERLOGGED });
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 146 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 147 */       return Fluids.WATER.getSource(false); 
/* 149 */     return super.getFluidState(state);
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 154 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 155 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 157 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 162 */     return false;
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 167 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 169 */     if (blockEntity instanceof EnderChestBlockEntity) {
/* 169 */       EnderChestBlockEntity enderChestBlockEntity = (EnderChestBlockEntity)blockEntity;
/* 170 */       enderChestBlockEntity.recheckOpen();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\EnderChestBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */