/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BaseCoralFanBlock extends BaseCoralPlantTypeBlock {
/* 11 */   public static final MapCodec<BaseCoralFanBlock> CODEC = simpleCodec(BaseCoralFanBlock::new);
/*    */   
/*    */   public MapCodec<? extends BaseCoralFanBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */   
/* 18 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 4.0D);
/*    */   
/*    */   protected BaseCoralFanBlock(BlockBehaviour.Properties properties) {
/* 21 */     super(properties);
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 26 */     return SHAPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BaseCoralFanBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */