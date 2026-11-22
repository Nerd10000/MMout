/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class PiglinWallSkullBlock extends WallSkullBlock {
/* 15 */   public static final MapCodec<PiglinWallSkullBlock> CODEC = simpleCodec(PiglinWallSkullBlock::new);
/*    */   
/*    */   public MapCodec<PiglinWallSkullBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/* 22 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(10.0D, 8.0D, 8.0D, 16.0D));
/*    */   
/*    */   public PiglinWallSkullBlock(BlockBehaviour.Properties properties) {
/* 25 */     super(SkullBlock.Types.PIGLIN, properties);
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 30 */     return SHAPES.get(state.getValue((Property)FACING));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\PiglinWallSkullBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */