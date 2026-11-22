/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SoulSandBlock extends Block {
/* 13 */   public static final MapCodec<SoulSandBlock> CODEC = simpleCodec(SoulSandBlock::new);
/*    */   
/*    */   public MapCodec<SoulSandBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   
/* 20 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 14.0D);
/*    */   
/*    */   public SoulSandBlock(BlockBehaviour.Properties properties) {
/* 23 */     super(properties);
/*    */   }
/*    */   
/*    */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 28 */     return SHAPE;
/*    */   }
/*    */   
/*    */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/* 33 */     return Shapes.block();
/*    */   }
/*    */   
/*    */   protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 38 */     return Shapes.block();
/*    */   }
/*    */   
/*    */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 43 */     return false;
/*    */   }
/*    */   
/*    */   protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
/* 48 */     return 0.2F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SoulSandBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */