/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class KelpBlock extends GrowingPlantHeadBlock implements LiquidBlockContainer {
/* 21 */   public static final MapCodec<KelpBlock> CODEC = simpleCodec(KelpBlock::new);
/*    */   
/*    */   private static final double GROW_PER_TICK_PROBABILITY = 0.14D;
/*    */   
/*    */   public MapCodec<KelpBlock> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */   
/* 30 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 9.0D);
/*    */   
/*    */   protected KelpBlock(BlockBehaviour.Properties properties) {
/* 33 */     super(properties, Direction.UP, SHAPE, true, 0.14D);
/*    */   }
/*    */   
/*    */   protected boolean canGrowInto(BlockState state) {
/* 38 */     return state.is(Blocks.WATER);
/*    */   }
/*    */   
/*    */   protected Block getBodyBlock() {
/* 43 */     return Blocks.KELP_PLANT;
/*    */   }
/*    */   
/*    */   protected boolean canAttachTo(BlockState state) {
/* 48 */     return !state.is(BlockTags.CANNOT_SUPPORT_KELP);
/*    */   }
/*    */   
/*    */   public boolean canPlaceLiquid(LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) {
/* 53 */     return false;
/*    */   }
/*    */   
/*    */   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
/* 58 */     return false;
/*    */   }
/*    */   
/*    */   protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
/* 63 */     return 1;
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 68 */     FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 69 */     if (fluidState.is(FluidTags.WATER) && fluidState.isFull())
/* 70 */       return super.getStateForPlacement(context); 
/* 72 */     return null;
/*    */   }
/*    */   
/*    */   protected FluidState getFluidState(BlockState state) {
/* 77 */     return Fluids.WATER.getSource(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\KelpBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */