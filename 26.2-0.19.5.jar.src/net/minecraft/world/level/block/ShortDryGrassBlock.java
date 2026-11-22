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
/*    */ public class ShortDryGrassBlock extends DryVegetationBlock implements BonemealableBlock {
/* 16 */   public static final MapCodec<ShortDryGrassBlock> CODEC = simpleCodec(ShortDryGrassBlock::new);
/*    */   
/*    */   public MapCodec<ShortDryGrassBlock> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */   
/* 23 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 10.0D);
/*    */   
/*    */   protected ShortDryGrassBlock(BlockBehaviour.Properties properties) {
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
/* 41 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 46 */     return true;
/*    */   }
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 51 */     level.setBlockAndUpdate(pos, Blocks.TALL_DRY_GRASS.defaultBlockState());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ShortDryGrassBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */