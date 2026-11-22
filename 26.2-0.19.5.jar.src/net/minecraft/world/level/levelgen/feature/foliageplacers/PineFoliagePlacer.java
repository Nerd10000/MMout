/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class PineFoliagePlacer extends FoliagePlacer {
/*    */   public static final MapCodec<PineFoliagePlacer> CODEC;
/*    */   
/*    */   private final IntProvider height;
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).and((App)IntProviders.codec(0, 24).fieldOf("height").forGetter(())).apply((Applicative)i, PineFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public PineFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider height) {
/* 19 */     super(radius, offset);
/* 20 */     this.height = height;
/*    */   }
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 25 */     return FoliagePlacerType.PINE_FOLIAGE_PLACER;
/*    */   }
/*    */   
/*    */   protected void createFoliage(WorldGenLevel level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 30 */     int currentRadius = 0;
/* 32 */     for (int yo = offset; yo >= offset - foliageHeight; yo--) {
/* 33 */       placeLeavesRow(level, foliageSetter, random, config, foliageAttachment.pos(), currentRadius, yo, foliageAttachment.doubleTrunk());
/* 35 */       if (currentRadius >= 1 && yo == offset - foliageHeight + 1) {
/* 36 */         currentRadius--;
/* 37 */       } else if (currentRadius < leafRadius + foliageAttachment.radiusOffset()) {
/* 38 */         currentRadius++;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public int foliageRadius(RandomSource random, int trunkHeight) {
/* 45 */     return super.foliageRadius(random, trunkHeight) + random.nextInt(Math.max(trunkHeight + 1, 1));
/*    */   }
/*    */   
/*    */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) {
/* 50 */     return this.height.sample(random);
/*    */   }
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 55 */     return (dx == currentRadius && dz == currentRadius && currentRadius > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\PineFoliagePlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */