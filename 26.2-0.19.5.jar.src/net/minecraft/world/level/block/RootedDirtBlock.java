/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RootedDirtBlock extends Block implements BonemealableBlock {
/* 12 */   public static final MapCodec<RootedDirtBlock> CODEC = simpleCodec(RootedDirtBlock::new);
/*    */   
/*    */   public MapCodec<RootedDirtBlock> codec() {
/* 16 */     return CODEC;
/*    */   }
/*    */   
/*    */   public RootedDirtBlock(BlockBehaviour.Properties properties) {
/* 20 */     super(properties);
/*    */   }
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 25 */     return (level.getBlockState(pos.below()).isAir() && level.isInsideBuildHeight(pos.below()));
/*    */   }
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 30 */     return true;
/*    */   }
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 35 */     level.setBlockAndUpdate(pos.below(), Blocks.HANGING_ROOTS.defaultBlockState());
/*    */   }
/*    */   
/*    */   public BlockPos getParticlePos(BlockPos blockPos) {
/* 40 */     return blockPos.below();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\RootedDirtBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */