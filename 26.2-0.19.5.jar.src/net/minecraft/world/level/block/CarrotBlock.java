/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class CarrotBlock extends CropBlock {
/* 13 */   public static final MapCodec<CarrotBlock> CODEC = simpleCodec(CarrotBlock::new);
/*    */   
/*    */   private static final VoxelShape[] SHAPES;
/*    */   
/*    */   public MapCodec<CarrotBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   
/*    */   static {
/* 20 */     SHAPES = Block.boxes(7, age -> Block.column(16.0D, 0.0D, (2 + age)));
/*    */   }
/*    */   
/*    */   public CarrotBlock(BlockBehaviour.Properties properties) {
/* 23 */     super(properties);
/*    */   }
/*    */   
/*    */   protected ItemLike getBaseSeedId() {
/* 28 */     return (ItemLike)Items.CARROT;
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 33 */     return SHAPES[getAge(state)];
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CarrotBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */