/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.RotatedPillarBlock;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class RotatedBlockProvider extends BlockStateProvider {
/*    */   public static final MapCodec<RotatedBlockProvider> CODEC;
/*    */   
/*    */   private final Block block;
/*    */   
/*    */   static {
/* 15 */     CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockBehaviour.BlockStateBase::getBlock, Block::defaultBlockState).xmap(RotatedBlockProvider::new, p -> p.block);
/*    */   }
/*    */   
/*    */   public RotatedBlockProvider(Block block) {
/* 20 */     this.block = block;
/*    */   }
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 25 */     return BlockStateProviderType.ROTATED_BLOCK_PROVIDER;
/*    */   }
/*    */   
/*    */   public BlockState getState(WorldGenLevel level, RandomSource random, BlockPos pos) {
/* 30 */     Direction.Axis randomAxis = Direction.Axis.getRandom(random);
/* 31 */     return (BlockState)this.block.defaultBlockState().trySetValue((Property)RotatedPillarBlock.AXIS, (Comparable)randomAxis);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\RotatedBlockProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */