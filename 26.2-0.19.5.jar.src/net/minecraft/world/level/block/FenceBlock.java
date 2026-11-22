/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.LeadItem;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FenceBlock extends CrossCollisionBlock {
/*  28 */   public static final MapCodec<FenceBlock> CODEC = simpleCodec(FenceBlock::new);
/*     */   
/*     */   private final Function<BlockState, VoxelShape> occlusionShapes;
/*     */   
/*     */   public MapCodec<FenceBlock> codec() {
/*  32 */     return CODEC;
/*     */   }
/*     */   
/*     */   public FenceBlock(BlockBehaviour.Properties properties) {
/*  38 */     super(4.0F, 16.0F, 4.0F, 16.0F, 24.0F, properties);
/*  39 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*  41 */     this.occlusionShapes = makeShapes(4.0F, 16.0F, 2.0F, 6.0F, 15.0F);
/*     */   }
/*     */   
/*     */   protected VoxelShape getOcclusionShape(BlockState state) {
/*  46 */     return this.occlusionShapes.apply(state);
/*     */   }
/*     */   
/*     */   protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  51 */     return getShape(state, level, pos, context);
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/*  56 */     return false;
/*     */   }
/*     */   
/*     */   public boolean connectsTo(BlockState state, boolean faceSolid, Direction direction) {
/*  60 */     Block block = state.getBlock();
/*  62 */     boolean sameFence = isSameFence(state);
/*  63 */     boolean gate = (block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(state, direction));
/*  64 */     return ((!isExceptionForConnection(state) && faceSolid) || sameFence || gate);
/*     */   }
/*     */   
/*     */   private boolean isSameFence(BlockState state) {
/*  69 */     return (state.is(BlockTags.FENCES) && state.is(BlockTags.WOODEN_FENCES) == defaultBlockState().is(BlockTags.WOODEN_FENCES));
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  74 */     return !level.isClientSide() ? LeadItem.bindPlayerMobs(player, level, pos) : (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  79 */     Level level = context.getLevel();
/*  80 */     BlockPos pos = context.getClickedPos();
/*  81 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  83 */     BlockPos north = pos.north();
/*  84 */     BlockPos east = pos.east();
/*  85 */     BlockPos south = pos.south();
/*  86 */     BlockPos west = pos.west();
/*  88 */     BlockState northState = level.getBlockState(north);
/*  89 */     BlockState eastState = level.getBlockState(east);
/*  90 */     BlockState southState = level.getBlockState(south);
/*  91 */     BlockState westState = level.getBlockState(west);
/*  93 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)super.getStateForPlacement(context)
/*  94 */       .setValue((Property)NORTH, Boolean.valueOf(connectsTo(northState, northState.isFaceSturdy((BlockGetter)level, north, Direction.SOUTH), Direction.SOUTH))))
/*  95 */       .setValue((Property)EAST, Boolean.valueOf(connectsTo(eastState, eastState.isFaceSturdy((BlockGetter)level, east, Direction.WEST), Direction.WEST))))
/*  96 */       .setValue((Property)SOUTH, Boolean.valueOf(connectsTo(southState, southState.isFaceSturdy((BlockGetter)level, south, Direction.NORTH), Direction.NORTH))))
/*  97 */       .setValue((Property)WEST, Boolean.valueOf(connectsTo(westState, westState.isFaceSturdy((BlockGetter)level, west, Direction.EAST), Direction.EAST))))
/*  98 */       .setValue((Property)WATERLOGGED, Boolean.valueOf(replacedFluidState.is(Fluids.WATER)));
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 103 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 104 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 106 */     if (directionToNeighbour.getAxis().isHorizontal())
/* 107 */       return (BlockState)state.setValue((Property)PROPERTY_BY_DIRECTION.get(directionToNeighbour), Boolean.valueOf(connectsTo(neighbourState, neighbourState.isFaceSturdy((BlockGetter)level, neighbourPos, directionToNeighbour.getOpposite()), directionToNeighbour.getOpposite()))); 
/* 109 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 114 */     builder.add(new Property[] { (Property)NORTH, (Property)EAST, (Property)WEST, (Property)SOUTH, (Property)WATERLOGGED });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\FenceBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */