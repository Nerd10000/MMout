/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class GiantTrunkPlacer extends TrunkPlacer {
/*    */   public static final MapCodec<GiantTrunkPlacer> CODEC;
/*    */   
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).apply((Applicative)i, GiantTrunkPlacer::new));
/*    */   }
/*    */   
/*    */   public GiantTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
/* 20 */     super(baseHeight, heightRandA, heightRandB);
/*    */   }
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 25 */     return TrunkPlacerType.GIANT_TRUNK_PLACER;
/*    */   }
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/* 30 */     BlockPos below = origin.below();
/* 31 */     placeBelowTrunkBlock(level, trunkSetter, random, below, config);
/* 32 */     placeBelowTrunkBlock(level, trunkSetter, random, below.east(), config);
/* 33 */     placeBelowTrunkBlock(level, trunkSetter, random, below.south(), config);
/* 34 */     placeBelowTrunkBlock(level, trunkSetter, random, below.south().east(), config);
/* 36 */     BlockPos.MutableBlockPos trunkPos = new BlockPos.MutableBlockPos();
/* 38 */     for (int hh = 0; hh < treeHeight; hh++) {
/* 39 */       placeLogIfFreeWithOffset(level, trunkSetter, random, trunkPos, config, origin, 0, hh, 0);
/* 41 */       if (hh < treeHeight - 1) {
/* 42 */         placeLogIfFreeWithOffset(level, trunkSetter, random, trunkPos, config, origin, 1, hh, 0);
/* 43 */         placeLogIfFreeWithOffset(level, trunkSetter, random, trunkPos, config, origin, 1, hh, 1);
/* 44 */         placeLogIfFreeWithOffset(level, trunkSetter, random, trunkPos, config, origin, 0, hh, 1);
/*    */       } 
/*    */     } 
/* 48 */     return (List<FoliagePlacer.FoliageAttachment>)ImmutableList.of(new FoliagePlacer.FoliageAttachment(origin.above(treeHeight), 0, true));
/*    */   }
/*    */   
/*    */   private void placeLogIfFreeWithOffset(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, BlockPos.MutableBlockPos trunkPos, TreeConfiguration config, BlockPos treePos, int x, int y, int z) {
/* 52 */     trunkPos.setWithOffset((Vec3i)treePos, x, y, z);
/* 53 */     placeLogIfFree(level, trunkSetter, random, trunkPos, config);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\GiantTrunkPlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */