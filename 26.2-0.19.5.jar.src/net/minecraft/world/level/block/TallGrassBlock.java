/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TallGrassBlock extends VegetationBlock implements BonemealableBlock {
/* 15 */   public static final MapCodec<TallGrassBlock> CODEC = simpleCodec(TallGrassBlock::new);
/*    */   
/*    */   public MapCodec<TallGrassBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/* 22 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 13.0D);
/*    */   
/*    */   protected TallGrassBlock(BlockBehaviour.Properties properties) {
/* 25 */     super(properties);
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 30 */     return SHAPE;
/*    */   }
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 35 */     return (getGrownBlock(state).defaultBlockState().canSurvive(level, pos) && level
/* 36 */       .isEmptyBlock(pos.above()) && level
/* 37 */       .isInsideBuildHeight(pos.above()));
/*    */   }
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 42 */     return true;
/*    */   }
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 47 */     DoublePlantBlock.placeAt((LevelAccessor)level, getGrownBlock(state).defaultBlockState(), pos, 2);
/*    */   }
/*    */   
/*    */   private static DoublePlantBlock getGrownBlock(BlockState state) {
/* 51 */     return state.is(Blocks.FERN) ? (DoublePlantBlock)Blocks.LARGE_FERN : (DoublePlantBlock)Blocks.TALL_GRASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\TallGrassBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */