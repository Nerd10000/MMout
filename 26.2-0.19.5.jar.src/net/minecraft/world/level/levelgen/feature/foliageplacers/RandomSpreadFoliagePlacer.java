/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class RandomSpreadFoliagePlacer extends FoliagePlacer {
/*    */   public static final MapCodec<RandomSpreadFoliagePlacer> CODEC;
/*    */   
/*    */   private final IntProvider foliageHeight;
/*    */   
/*    */   private final int leafPlacementAttempts;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).and(i.group((App)IntProviders.codec(1, 512).fieldOf("foliage_height").forGetter(()), (App)Codec.intRange(0, 256).fieldOf("leaf_placement_attempts").forGetter(()))).apply((Applicative)i, RandomSpreadFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public RandomSpreadFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider foliageHeight, int leafPlacementAttempts) {
/* 25 */     super(radius, offset);
/* 27 */     this.foliageHeight = foliageHeight;
/* 28 */     this.leafPlacementAttempts = leafPlacementAttempts;
/*    */   }
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 33 */     return FoliagePlacerType.RANDOM_SPREAD_FOLIAGE_PLACER;
/*    */   }
/*    */   
/*    */   protected void createFoliage(WorldGenLevel level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 38 */     BlockPos origin = foliageAttachment.pos();
/* 39 */     BlockPos.MutableBlockPos pos = origin.mutable();
/* 41 */     for (int i = 0; i < this.leafPlacementAttempts; i++) {
/* 42 */       pos.setWithOffset((Vec3i)origin, random.nextInt(leafRadius) - random.nextInt(leafRadius), random.nextInt(foliageHeight) - random.nextInt(foliageHeight), random.nextInt(leafRadius) - random.nextInt(leafRadius));
/* 43 */       tryPlaceLeaf(level, foliageSetter, random, config, (BlockPos)pos);
/*    */     } 
/*    */   }
/*    */   
/*    */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) {
/* 49 */     return this.foliageHeight.sample(random);
/*    */   }
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 54 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\RandomSpreadFoliagePlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */