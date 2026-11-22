/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class CactusFlowerBlock extends VegetationBlock {
/* 13 */   public static final MapCodec<CactusFlowerBlock> CODEC = simpleCodec(CactusFlowerBlock::new);
/*    */   
/* 14 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 12.0D);
/*    */   
/*    */   public MapCodec<? extends CactusFlowerBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */   
/*    */   public CactusFlowerBlock(BlockBehaviour.Properties properties) {
/* 22 */     super(properties);
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 27 */     return SHAPE;
/*    */   }
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 32 */     BlockState blockBelow = level.getBlockState(pos);
/* 33 */     return (blockBelow.is(BlockTags.SUPPORT_OVERRIDE_CACTUS_FLOWER) || blockBelow.isFaceSturdy(level, pos, Direction.UP, SupportType.CENTER));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CactusFlowerBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */