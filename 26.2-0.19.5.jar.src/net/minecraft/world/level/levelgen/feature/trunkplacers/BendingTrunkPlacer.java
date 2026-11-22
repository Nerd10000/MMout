/*    */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*    */ 
/*    */ public class BendingTrunkPlacer extends TrunkPlacer {
/*    */   public static final MapCodec<BendingTrunkPlacer> CODEC;
/*    */   
/*    */   private final int minHeightForLeaves;
/*    */   
/*    */   private final IntProvider bendLength;
/*    */   
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).and(i.group((App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("min_height_for_leaves", Integer.valueOf(1)).forGetter(()), (App)IntProviders.codec(1, 64).fieldOf("bend_length").forGetter(()))).apply((Applicative)i, BendingTrunkPlacer::new));
/*    */   }
/*    */   
/*    */   public BendingTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, int minHeightForLeaves, IntProvider bendLength) {
/* 33 */     super(baseHeight, heightRandA, heightRandB);
/* 35 */     this.minHeightForLeaves = minHeightForLeaves;
/* 36 */     this.bendLength = bendLength;
/*    */   }
/*    */   
/*    */   protected TrunkPlacerType<?> type() {
/* 41 */     return TrunkPlacerType.BENDING_TRUNK_PLACER;
/*    */   }
/*    */   
/*    */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/* 46 */     Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/* 47 */     int logHeight = treeHeight - 1;
/* 48 */     BlockPos.MutableBlockPos pos = origin.mutable();
/* 49 */     BlockPos belowPos = pos.below();
/* 51 */     placeBelowTrunkBlock(level, trunkSetter, random, belowPos, config);
/* 52 */     List<FoliagePlacer.FoliageAttachment> foliagePoints = Lists.newArrayList();
/* 54 */     for (int i = 0; i <= logHeight; i++) {
/* 56 */       if (i + 1 >= logHeight + random.nextInt(2))
/* 57 */         pos.move(direction); 
/* 60 */       if (TreeFeature.validTreePos((LevelSimulatedReader)level, (BlockPos)pos))
/* 61 */         placeLog(level, trunkSetter, random, (BlockPos)pos, config); 
/* 64 */       if (i >= this.minHeightForLeaves)
/* 65 */         foliagePoints.add(new FoliagePlacer.FoliageAttachment(pos.immutable(), 0, false)); 
/* 68 */       pos.move(Direction.UP);
/*    */     } 
/* 72 */     int dirLength = this.bendLength.sample(random);
/* 73 */     for (int j = 0; j <= dirLength; j++) {
/* 74 */       if (TreeFeature.validTreePos((LevelSimulatedReader)level, (BlockPos)pos))
/* 75 */         placeLog(level, trunkSetter, random, (BlockPos)pos, config); 
/* 78 */       foliagePoints.add(new FoliagePlacer.FoliageAttachment(pos.immutable(), 0, false));
/* 79 */       pos.move(direction);
/*    */     } 
/* 82 */     return foliagePoints;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\BendingTrunkPlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */