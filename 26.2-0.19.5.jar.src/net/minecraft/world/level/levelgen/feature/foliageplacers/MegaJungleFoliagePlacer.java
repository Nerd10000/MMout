/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class MegaJungleFoliagePlacer extends FoliagePlacer {
/*    */   public static final MapCodec<MegaJungleFoliagePlacer> CODEC;
/*    */   
/*    */   protected final int height;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).and((App)Codec.intRange(0, 16).fieldOf("height").forGetter(())).apply((Applicative)i, MegaJungleFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public MegaJungleFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
/* 20 */     super(radius, offset);
/* 21 */     this.height = height;
/*    */   }
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 26 */     return FoliagePlacerType.MEGA_JUNGLE_FOLIAGE_PLACER;
/*    */   }
/*    */   
/*    */   protected void createFoliage(WorldGenLevel level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 32 */     int leafHeight = foliageAttachment.doubleTrunk() ? foliageHeight : (1 + random.nextInt(2));
/* 34 */     for (int yo = offset; yo >= offset - leafHeight; yo--) {
/* 35 */       int currentRadius = leafRadius + foliageAttachment.radiusOffset() + 1 - yo;
/* 36 */       placeLeavesRow(level, foliageSetter, random, config, foliageAttachment.pos(), currentRadius, yo, foliageAttachment.doubleTrunk());
/*    */     } 
/*    */   }
/*    */   
/*    */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) {
/* 42 */     return this.height;
/*    */   }
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 47 */     if (dx + dz >= 7)
/* 48 */       return true; 
/* 50 */     return (dx * dx + dz * dz > currentRadius * currentRadius);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\MegaJungleFoliagePlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */