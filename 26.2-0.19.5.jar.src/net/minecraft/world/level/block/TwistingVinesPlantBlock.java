/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TwistingVinesPlantBlock extends GrowingPlantBodyBlock {
/*  8 */   public static final MapCodec<TwistingVinesPlantBlock> CODEC = simpleCodec(TwistingVinesPlantBlock::new);
/*    */   
/*    */   public MapCodec<TwistingVinesPlantBlock> codec() {
/* 12 */     return CODEC;
/*    */   }
/*    */   
/* 15 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 16.0D);
/*    */   
/*    */   public TwistingVinesPlantBlock(BlockBehaviour.Properties properties) {
/* 18 */     super(properties, Direction.UP, SHAPE, false);
/*    */   }
/*    */   
/*    */   protected GrowingPlantHeadBlock getHeadBlock() {
/* 23 */     return (GrowingPlantHeadBlock)Blocks.TWISTING_VINES;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\TwistingVinesPlantBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */