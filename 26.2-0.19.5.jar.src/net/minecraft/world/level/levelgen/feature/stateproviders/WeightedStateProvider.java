/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeightedStateProvider extends BlockStateProvider {
/*    */   public static final MapCodec<WeightedStateProvider> CODEC;
/*    */   
/*    */   private final WeightedList<BlockState> weightedList;
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeightedList.nonEmptyCodec(BlockState.CODEC).fieldOf("entries").forGetter(())).apply((Applicative)i, WeightedStateProvider::new));
/*    */   }
/*    */   
/*    */   public WeightedStateProvider(WeightedList<BlockState> weightedList) {
/* 19 */     if (weightedList.isEmpty())
/* 20 */       throw new IllegalArgumentException("Weighted list must have at least one entry"); 
/* 22 */     this.weightedList = weightedList;
/*    */   }
/*    */   
/*    */   public WeightedStateProvider(WeightedList.Builder<BlockState> weightedList) {
/* 26 */     this(weightedList.build());
/*    */   }
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 31 */     return BlockStateProviderType.WEIGHTED_STATE_PROVIDER;
/*    */   }
/*    */   
/*    */   public BlockState getState(WorldGenLevel level, RandomSource random, BlockPos pos) {
/* 36 */     return (BlockState)this.weightedList.getRandomOrThrow(random);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\WeightedStateProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */