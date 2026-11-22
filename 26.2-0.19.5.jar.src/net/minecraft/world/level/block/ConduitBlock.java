/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*    */ import net.minecraft.world.level.block.entity.ConduitBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class ConduitBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/* 30 */   public static final MapCodec<ConduitBlock> CODEC = simpleCodec(ConduitBlock::new);
/*    */   
/*    */   public MapCodec<ConduitBlock> codec() {
/* 34 */     return CODEC;
/*    */   }
/*    */   
/* 37 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/* 39 */   private static final VoxelShape SHAPE = Block.cube(6.0D);
/*    */   
/*    */   public ConduitBlock(BlockBehaviour.Properties properties) {
/* 42 */     super(properties);
/* 43 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)WATERLOGGED, Boolean.valueOf(true)));
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 48 */     builder.add(new Property[] { (Property)WATERLOGGED });
/*    */   }
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 53 */     return (BlockEntity)new ConduitBlockEntity(worldPosition, blockState);
/*    */   }
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 58 */     return createTickerHelper(type, BlockEntityTypes.CONDUIT, level.isClientSide() ? ConduitBlockEntity::clientTick : ConduitBlockEntity::serverTick);
/*    */   }
/*    */   
/*    */   protected FluidState getFluidState(BlockState state) {
/* 63 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 64 */       return Fluids.WATER.getSource(false); 
/* 67 */     return super.getFluidState(state);
/*    */   }
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 72 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 73 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 76 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 81 */     return SHAPE;
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 86 */     FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 87 */     return (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.is(FluidTags.WATER) && fluidState.isFull())));
/*    */   }
/*    */   
/*    */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 92 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ConduitBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */