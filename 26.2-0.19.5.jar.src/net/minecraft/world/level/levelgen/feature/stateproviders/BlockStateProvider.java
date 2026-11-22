/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class BlockStateProvider {
/* 13 */   public static final Codec<BlockStateProvider> CODEC = BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE.byNameCodec().dispatch(BlockStateProvider::type, BlockStateProviderType::codec);
/*    */   
/*    */   public static SimpleStateProvider simple(BlockState state) {
/* 16 */     return new SimpleStateProvider(state);
/*    */   }
/*    */   
/*    */   public static SimpleStateProvider simple(Block block) {
/* 20 */     return new SimpleStateProvider(block.defaultBlockState());
/*    */   }
/*    */   
/*    */   protected abstract BlockStateProviderType<?> type();
/*    */   
/*    */   public abstract BlockState getState(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, BlockPos paramBlockPos);
/*    */   
/*    */   public BlockState getOptionalState(WorldGenLevel level, RandomSource random, BlockPos pos) {
/* 28 */     return getState(level, random, pos);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\BlockStateProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */