/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.HopperBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class HopperBlock extends BaseEntityBlock {
/*  42 */   public static final MapCodec<HopperBlock> CODEC = simpleCodec(HopperBlock::new);
/*     */   
/*     */   public MapCodec<HopperBlock> codec() {
/*  46 */     return CODEC;
/*     */   }
/*     */   
/*  49 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING_HOPPER;
/*     */   
/*  50 */   public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   private final Map<Direction, VoxelShape> interactionShapes;
/*     */   
/*     */   public HopperBlock(BlockBehaviour.Properties properties) {
/*  56 */     super(properties);
/*  57 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.DOWN)).setValue((Property)ENABLED, Boolean.valueOf(true)));
/*  59 */     VoxelShape inside = Block.column(12.0D, 11.0D, 16.0D);
/*  60 */     this.shapes = makeShapes(inside);
/*  61 */     this
/*     */       
/*  67 */       .interactionShapes = (Map<Direction, VoxelShape>)ImmutableMap.builderWithExpectedSize(5).putAll(Shapes.rotateHorizontal(Shapes.or(inside, Block.boxZ(4.0D, 8.0D, 10.0D, 0.0D, 4.0D)))).put(Direction.DOWN, inside).build();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes(VoxelShape inside) {
/*  71 */     VoxelShape spoutlessHopperOutline = Shapes.or(
/*  72 */         Block.column(16.0D, 10.0D, 16.0D), 
/*  73 */         Block.column(8.0D, 4.0D, 10.0D));
/*  75 */     VoxelShape spoutlessHopper = Shapes.join(spoutlessHopperOutline, inside, BooleanOp.ONLY_FIRST);
/*  77 */     Map<Direction, VoxelShape> spouts = Shapes.rotateAll(Block.boxZ(4.0D, 4.0D, 8.0D, 0.0D, 8.0D), (new Vec3(8.0D, 6.0D, 8.0D)).scale(0.0625D));
/*  79 */     return getShapeForEachState(state -> Shapes.or(spoutlessHopper, Shapes.join((VoxelShape)spouts.get(state.getValue((Property)FACING)), Shapes.block(), BooleanOp.AND)), (Property<?>[])new Property[] { (Property)ENABLED });
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  88 */     return this.shapes.apply(state);
/*     */   }
/*     */   
/*     */   protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
/*  93 */     return this.interactionShapes.get(state.getValue((Property)FACING));
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  98 */     Direction direction = context.getClickedFace().getOpposite();
/*  99 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (direction.getAxis() == Direction.Axis.Y) ? (Comparable)Direction.DOWN : (Comparable)direction)).setValue((Property)ENABLED, Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 104 */     return (BlockEntity)new HopperBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 109 */     return level.isClientSide() ? null : createTickerHelper(type, BlockEntityTypes.HOPPER, HopperBlockEntity::pushItemsTick);
/*     */   }
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 114 */     if (oldState.is(state.getBlock()))
/*     */       return; 
/* 117 */     checkPoweredState(level, pos, state);
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 122 */     if (!level.isClientSide()) {
/* 122 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 122 */       if (blockEntity instanceof HopperBlockEntity) {
/* 122 */         HopperBlockEntity hopper = (HopperBlockEntity)blockEntity;
/* 123 */         player.openMenu((MenuProvider)hopper);
/* 124 */         player.awardStat(Stats.INSPECT_HOPPER);
/*     */       } 
/*     */     } 
/* 126 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 131 */     checkPoweredState(level, pos, state);
/*     */   }
/*     */   
/*     */   private void checkPoweredState(Level level, BlockPos pos, BlockState state) {
/* 135 */     boolean shouldBeOn = !level.hasNeighborSignal(pos);
/* 136 */     if (shouldBeOn != ((Boolean)state.getValue((Property)ENABLED)).booleanValue())
/* 137 */       level.setBlock(pos, (BlockState)state.setValue((Property)ENABLED, Boolean.valueOf(shouldBeOn)), 2); 
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 143 */     Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*     */   }
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 148 */     return true;
/*     */   }
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 153 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 158 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 163 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 168 */     builder.add(new Property[] { (Property)FACING, (Property)ENABLED });
/*     */   }
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 173 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 174 */     if (blockEntity instanceof HopperBlockEntity) {
/* 174 */       HopperBlockEntity hopperBlockEntity = (HopperBlockEntity)blockEntity;
/* 175 */       HopperBlockEntity.entityInside(level, pos, state, entity, hopperBlockEntity);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 181 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\HopperBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */