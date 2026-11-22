/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WeepingVinesPlantBlock extends GrowingPlantBodyBlock {
/*  8 */   public static final MapCodec<WeepingVinesPlantBlock> CODEC = simpleCodec(WeepingVinesPlantBlock::new);
/*    */   
/*    */   public MapCodec<WeepingVinesPlantBlock> codec() {
/* 12 */     return CODEC;
/*    */   }
/*    */   
/* 15 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*    */   
/*    */   public WeepingVinesPlantBlock(BlockBehaviour.Properties properties) {
/* 18 */     super(properties, Direction.DOWN, SHAPE, false);
/*    */   }
/*    */   
/*    */   protected GrowingPlantHeadBlock getHeadBlock() {
/* 23 */     return (GrowingPlantHeadBlock)Blocks.WEEPING_VINES;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WeepingVinesPlantBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */