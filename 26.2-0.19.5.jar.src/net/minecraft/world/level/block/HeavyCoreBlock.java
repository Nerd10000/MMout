/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
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
/*    */ public class HeavyCoreBlock extends Block implements SimpleWaterloggedBlock {
/* 22 */   public static final MapCodec<HeavyCoreBlock> CODEC = simpleCodec(HeavyCoreBlock::new);
/*    */   
/* 24 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/* 26 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 8.0D);
/*    */   
/*    */   public HeavyCoreBlock(BlockBehaviour.Properties properties) {
/* 29 */     super(properties);
/* 30 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */   
/*    */   public MapCodec<HeavyCoreBlock> codec() {
/* 35 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 40 */     builder.add(new Property[] { (Property)WATERLOGGED });
/*    */   }
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 45 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 46 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 48 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */   
/*    */   protected FluidState getFluidState(BlockState state) {
/* 53 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 54 */       return Fluids.WATER.getSource(false); 
/* 57 */     return super.getFluidState(state);
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 62 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 63 */     return (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf(replacedFluidState.is(Fluids.WATER)));
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 68 */     return SHAPE;
/*    */   }
/*    */   
/*    */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 73 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\HeavyCoreBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */