/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class BuddingAmethystBlock extends AmethystBlock {
/* 12 */   public static final MapCodec<BuddingAmethystBlock> CODEC = simpleCodec(BuddingAmethystBlock::new);
/*    */   
/*    */   public static final int GROWTH_CHANCE = 5;
/*    */   
/*    */   public MapCodec<BuddingAmethystBlock> codec() {
/* 16 */     return CODEC;
/*    */   }
/*    */   
/* 21 */   private static final Direction[] DIRECTIONS = Direction.values();
/*    */   
/*    */   public BuddingAmethystBlock(BlockBehaviour.Properties properties) {
/* 24 */     super(properties);
/*    */   }
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 29 */     if (random.nextInt(5) != 0)
/*    */       return; 
/* 33 */     Direction growDirection = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
/* 34 */     BlockPos growPos = pos.relative(growDirection);
/* 35 */     BlockState relativeState = level.getBlockState(growPos);
/* 36 */     Block nextStage = null;
/* 37 */     if (canClusterGrowAtState(relativeState)) {
/* 38 */       nextStage = Blocks.SMALL_AMETHYST_BUD;
/* 39 */     } else if (relativeState.is(Blocks.SMALL_AMETHYST_BUD) && relativeState.getValue((Property)AmethystClusterBlock.FACING) == growDirection) {
/* 40 */       nextStage = Blocks.MEDIUM_AMETHYST_BUD;
/* 41 */     } else if (relativeState.is(Blocks.MEDIUM_AMETHYST_BUD) && relativeState.getValue((Property)AmethystClusterBlock.FACING) == growDirection) {
/* 42 */       nextStage = Blocks.LARGE_AMETHYST_BUD;
/* 43 */     } else if (relativeState.is(Blocks.LARGE_AMETHYST_BUD) && relativeState.getValue((Property)AmethystClusterBlock.FACING) == growDirection) {
/* 44 */       nextStage = Blocks.AMETHYST_CLUSTER;
/*    */     } 
/* 47 */     if (nextStage != null) {
/* 50 */       BlockState targetState = (BlockState)((BlockState)nextStage.defaultBlockState().setValue((Property)AmethystClusterBlock.FACING, (Comparable)growDirection)).setValue((Property)AmethystClusterBlock.WATERLOGGED, Boolean.valueOf(relativeState.getFluidState().is(Fluids.WATER)));
/* 51 */       level.setBlockAndUpdate(growPos, targetState);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static boolean canClusterGrowAtState(BlockState state) {
/* 56 */     return (state.isAir() || (state.is(Blocks.WATER) && state.getFluidState().isFull()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BuddingAmethystBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */