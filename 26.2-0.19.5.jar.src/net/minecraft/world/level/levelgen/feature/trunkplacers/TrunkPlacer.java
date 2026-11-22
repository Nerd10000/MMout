/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ 
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public abstract class TrunkPlacer {
/* 21 */   public static final Codec<TrunkPlacer> CODEC = BuiltInRegistries.TRUNK_PLACER_TYPE.byNameCodec().dispatch(TrunkPlacer::type, TrunkPlacerType::codec);
/*    */   
/*    */   private static final int MAX_BASE_HEIGHT = 32;
/*    */   
/*    */   private static final int MAX_RAND = 24;
/*    */   
/*    */   public static final int MAX_HEIGHT = 80;
/*    */   
/*    */   protected final int baseHeight;
/*    */   
/*    */   protected final int heightRandA;
/*    */   
/*    */   protected final int heightRandB;
/*    */   
/*    */   protected static <P extends TrunkPlacer> Products.P3<RecordCodecBuilder.Mu<P>, Integer, Integer, Integer> trunkPlacerParts(RecordCodecBuilder.Instance<P> instance) {
/* 28 */     return instance.group(
/* 29 */         (App)Codec.intRange(0, 32).fieldOf("base_height").forGetter(p -> Integer.valueOf(p.baseHeight)), 
/* 30 */         (App)Codec.intRange(0, 24).fieldOf("height_rand_a").forGetter(p -> Integer.valueOf(p.heightRandA)), 
/* 31 */         (App)Codec.intRange(0, 24).fieldOf("height_rand_b").forGetter(p -> Integer.valueOf(p.heightRandB)));
/*    */   }
/*    */   
/*    */   public TrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
/* 40 */     this.baseHeight = baseHeight;
/* 41 */     this.heightRandA = heightRandA;
/* 42 */     this.heightRandB = heightRandB;
/*    */   }
/*    */   
/*    */   public int getBaseHeight() {
/* 50 */     return this.baseHeight;
/*    */   }
/*    */   
/*    */   public int getTreeHeight(RandomSource random) {
/* 54 */     return this.baseHeight + random.nextInt(this.heightRandA + 1) + random.nextInt(this.heightRandB + 1);
/*    */   }
/*    */   
/*    */   protected static void placeBelowTrunkBlock(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, BlockPos pos, TreeConfiguration config) {
/* 58 */     BlockState blockBelowTrunk = config.belowTrunkProvider.getOptionalState(level, random, pos);
/* 59 */     if (blockBelowTrunk != null)
/* 60 */       trunkSetter.accept(pos, blockBelowTrunk); 
/*    */   }
/*    */   
/*    */   protected boolean placeLog(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, BlockPos pos, TreeConfiguration config) {
/* 65 */     return placeLog(level, trunkSetter, random, pos, config, Function.identity());
/*    */   }
/*    */   
/*    */   protected boolean placeLog(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, BlockPos pos, TreeConfiguration config, Function<BlockState, BlockState> stateModifier) {
/* 69 */     if (validTreePos(level, pos)) {
/* 70 */       trunkSetter.accept(pos, stateModifier.apply(config.trunkProvider.getState(level, random, pos)));
/* 72 */       return true;
/*    */     } 
/* 74 */     return false;
/*    */   }
/*    */   
/*    */   protected void placeLogIfFree(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, BlockPos.MutableBlockPos pos, TreeConfiguration config) {
/* 78 */     if (isFree(level, (BlockPos)pos))
/* 79 */       placeLog(level, trunkSetter, random, (BlockPos)pos, config); 
/*    */   }
/*    */   
/*    */   protected boolean validTreePos(WorldGenLevel level, BlockPos pos) {
/* 84 */     return TreeFeature.validTreePos((LevelSimulatedReader)level, pos);
/*    */   }
/*    */   
/*    */   public boolean isFree(WorldGenLevel level, BlockPos pos) {
/* 89 */     return (validTreePos(level, pos) || level.isStateAtPosition(pos, state -> state.is(BlockTags.LOGS)));
/*    */   }
/*    */   
/*    */   protected abstract TrunkPlacerType<?> type();
/*    */   
/*    */   public abstract List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel paramWorldGenLevel, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\TrunkPlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */