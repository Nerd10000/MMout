/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.sounds.AmbientDesertBlockSoundsPlayer;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class DryVegetationBlock extends VegetationBlock {
/* 15 */   public static final MapCodec<DryVegetationBlock> CODEC = simpleCodec(DryVegetationBlock::new);
/*    */   
/*    */   public MapCodec<? extends DryVegetationBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/* 22 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 13.0D);
/*    */   
/*    */   protected DryVegetationBlock(BlockBehaviour.Properties properties) {
/* 25 */     super(properties);
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 30 */     return SHAPE;
/*    */   }
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 35 */     return state.is(BlockTags.SUPPORTS_DRY_VEGETATION);
/*    */   }
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 40 */     AmbientDesertBlockSoundsPlayer.playAmbientDeadBushSounds(level, pos, random);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\DryVegetationBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */