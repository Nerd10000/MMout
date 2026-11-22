/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class SpruceFoliagePlacer extends FoliagePlacer {
/*    */   public static final MapCodec<SpruceFoliagePlacer> CODEC;
/*    */   
/*    */   private final IntProvider trunkHeight;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).and((App)IntProviders.codec(0, 24).fieldOf("trunk_height").forGetter(())).apply((Applicative)i, SpruceFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public SpruceFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider trunkHeight) {
/* 20 */     super(radius, offset);
/* 21 */     this.trunkHeight = trunkHeight;
/*    */   }
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 26 */     return FoliagePlacerType.SPRUCE_FOLIAGE_PLACER;
/*    */   }
/*    */   
/*    */   protected void createFoliage(WorldGenLevel level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 31 */     BlockPos foliagePos = foliageAttachment.pos();
/* 33 */     int currentRadius = random.nextInt(2);
/* 34 */     int maxRadius = 1;
/* 35 */     int minRadius = 0;
/* 37 */     for (int yo = offset; yo >= -foliageHeight; yo--) {
/* 38 */       placeLeavesRow(level, foliageSetter, random, config, foliagePos, currentRadius, yo, foliageAttachment.doubleTrunk());
/* 40 */       if (currentRadius >= maxRadius) {
/* 41 */         currentRadius = minRadius;
/* 42 */         minRadius = 1;
/* 43 */         maxRadius = Math.min(maxRadius + 1, leafRadius + foliageAttachment.radiusOffset());
/*    */       } else {
/* 45 */         currentRadius++;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) {
/* 53 */     return Math.max(4, treeHeight - this.trunkHeight.sample(random));
/*    */   }
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 58 */     return (dx == currentRadius && dz == currentRadius && currentRadius > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\SpruceFoliagePlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */