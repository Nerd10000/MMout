/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.grower.TreeGrower;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class AzaleaBlock extends VegetationBlock implements BonemealableBlock {
/* 19 */   public static final MapCodec<AzaleaBlock> CODEC = simpleCodec(AzaleaBlock::new);
/*    */   
/*    */   public MapCodec<AzaleaBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/* 26 */   private static final VoxelShape SHAPE = Shapes.or(
/* 27 */       Block.column(16.0D, 8.0D, 16.0D), 
/* 28 */       Block.column(4.0D, 0.0D, 8.0D));
/*    */   
/*    */   protected AzaleaBlock(BlockBehaviour.Properties properties) {
/* 32 */     super(properties);
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 37 */     return SHAPE;
/*    */   }
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 42 */     return state.is(BlockTags.SUPPORTS_AZALEA);
/*    */   }
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/*    */     ServerLevel serverLevel;
/* 47 */     if (level instanceof ServerLevel) {
/* 47 */       serverLevel = (ServerLevel)level;
/*    */     } else {
/* 48 */       return false;
/*    */     } 
/* 51 */     int minHeight = TreeGrower.AZALEA.getMinimumHeight(serverLevel).orElse(0);
/* 52 */     return (level.isInsideBuildHeight(pos.above(minHeight + 2)) && level
/* 53 */       .getFluidState(pos.above()).isEmpty());
/*    */   }
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 58 */     return (level.getRandom().nextFloat() < 0.45D);
/*    */   }
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 63 */     TreeGrower.AZALEA.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
/*    */   }
/*    */   
/*    */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 68 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\AzaleaBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */