/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.sounds.AmbientDesertBlockSoundsPlayer;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TallDryGrassBlock extends DryVegetationBlock implements BonemealableBlock {
/* 16 */   public static final MapCodec<TallDryGrassBlock> CODEC = simpleCodec(TallDryGrassBlock::new);
/*    */   
/*    */   public MapCodec<TallDryGrassBlock> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */   
/* 23 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*    */   
/*    */   protected TallDryGrassBlock(BlockBehaviour.Properties properties) {
/* 26 */     super(properties);
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 31 */     return SHAPE;
/*    */   }
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 36 */     AmbientDesertBlockSoundsPlayer.playAmbientDryGrassSounds(level, pos, random);
/*    */   }
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 41 */     return BonemealableBlock.hasSpreadableNeighbourPos(level, pos, Blocks.SHORT_DRY_GRASS.defaultBlockState());
/*    */   }
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 46 */     return true;
/*    */   }
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 51 */     BonemealableBlock.findSpreadableNeighbourPos((Level)level, pos, Blocks.SHORT_DRY_GRASS.defaultBlockState()).ifPresent(blockPos -> level.setBlockAndUpdate(blockPos, Blocks.SHORT_DRY_GRASS.defaultBlockState()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\TallDryGrassBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */