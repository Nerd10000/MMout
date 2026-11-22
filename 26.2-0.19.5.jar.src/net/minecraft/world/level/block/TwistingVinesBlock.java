/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TwistingVinesBlock extends GrowingPlantHeadBlock {
/* 10 */   public static final MapCodec<TwistingVinesBlock> CODEC = simpleCodec(TwistingVinesBlock::new);
/*    */   
/*    */   public MapCodec<TwistingVinesBlock> codec() {
/* 14 */     return CODEC;
/*    */   }
/*    */   
/* 17 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 15.0D);
/*    */   
/*    */   public TwistingVinesBlock(BlockBehaviour.Properties properties) {
/* 20 */     super(properties, Direction.UP, SHAPE, false, 0.1D);
/*    */   }
/*    */   
/*    */   protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
/* 25 */     return NetherVines.getBlocksToGrowWhenBonemealed(random);
/*    */   }
/*    */   
/*    */   protected Block getBodyBlock() {
/* 30 */     return Blocks.TWISTING_VINES_PLANT;
/*    */   }
/*    */   
/*    */   protected boolean canGrowInto(BlockState state) {
/* 35 */     return NetherVines.isValidGrowthState(state);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\TwistingVinesBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */