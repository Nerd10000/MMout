/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
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
/*    */ 
/*    */ public class WaterloggedTransparentBlock extends TransparentBlock implements SimpleWaterloggedBlock {
/* 19 */   public static final MapCodec<WaterloggedTransparentBlock> CODEC = simpleCodec(WaterloggedTransparentBlock::new);
/*    */   
/*    */   protected MapCodec<? extends WaterloggedTransparentBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/* 26 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/*    */   protected WaterloggedTransparentBlock(BlockBehaviour.Properties properties) {
/* 29 */     super(properties);
/* 30 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 35 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 36 */     return (BlockState)super.getStateForPlacement(context).setValue((Property)WATERLOGGED, Boolean.valueOf(replacedFluidState.is(Fluids.WATER)));
/*    */   }
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 41 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 42 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 45 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */   
/*    */   protected FluidState getFluidState(BlockState state) {
/* 50 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 51 */       return Fluids.WATER.getSource(true); 
/* 54 */     return super.getFluidState(state);
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 59 */     builder.add(new Property[] { (Property)WATERLOGGED });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WaterloggedTransparentBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */