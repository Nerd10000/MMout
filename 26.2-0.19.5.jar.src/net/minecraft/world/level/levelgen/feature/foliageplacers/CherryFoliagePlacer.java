/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class CherryFoliagePlacer extends FoliagePlacer {
/*    */   public static final MapCodec<CherryFoliagePlacer> CODEC;
/*    */   
/*    */   private final IntProvider height;
/*    */   
/*    */   private final float wideBottomLayerHoleChance;
/*    */   
/*    */   private final float cornerHoleChance;
/*    */   
/*    */   private final float hangingLeavesChance;
/*    */   
/*    */   private final float hangingLeavesExtensionChance;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).and(i.group((App)IntProviders.codec(4, 16).fieldOf("height").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("wide_bottom_layer_hole_chance").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("corner_hole_chance").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_chance").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_extension_chance").forGetter(()))).apply((Applicative)i, CherryFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public CherryFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider height, float wideBottomLayerHoleChance, float cornerHoleChance, float hangingLeavesChance, float hangingLeavesExtensionChance) {
/* 29 */     super(radius, offset);
/* 30 */     this.height = height;
/* 31 */     this.wideBottomLayerHoleChance = wideBottomLayerHoleChance;
/* 32 */     this.cornerHoleChance = cornerHoleChance;
/* 33 */     this.hangingLeavesChance = hangingLeavesChance;
/* 34 */     this.hangingLeavesExtensionChance = hangingLeavesExtensionChance;
/*    */   }
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 39 */     return FoliagePlacerType.CHERRY_FOLIAGE_PLACER;
/*    */   }
/*    */   
/*    */   protected void createFoliage(WorldGenLevel level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 58 */     boolean doubleTrunk = foliageAttachment.doubleTrunk();
/* 59 */     BlockPos foliagePos = foliageAttachment.pos().above(offset);
/* 61 */     int currentRadius = leafRadius + foliageAttachment.radiusOffset() - 1;
/* 63 */     placeLeavesRow(level, foliageSetter, random, config, foliagePos, currentRadius - 2, foliageHeight - 3, doubleTrunk);
/* 64 */     placeLeavesRow(level, foliageSetter, random, config, foliagePos, currentRadius - 1, foliageHeight - 4, doubleTrunk);
/* 66 */     for (int y = foliageHeight - 5; y >= 0; y--)
/* 67 */       placeLeavesRow(level, foliageSetter, random, config, foliagePos, currentRadius, y, doubleTrunk); 
/* 70 */     placeLeavesRowWithHangingLeavesBelow(level, foliageSetter, random, config, foliagePos, currentRadius, -1, doubleTrunk, this.hangingLeavesChance, this.hangingLeavesExtensionChance);
/* 71 */     placeLeavesRowWithHangingLeavesBelow(level, foliageSetter, random, config, foliagePos, currentRadius - 1, -2, doubleTrunk, this.hangingLeavesChance, this.hangingLeavesExtensionChance);
/*    */   }
/*    */   
/*    */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) {
/* 76 */     return this.height.sample(random);
/*    */   }
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 81 */     if (y == -1 && (dx == currentRadius || dz == currentRadius) && random.nextFloat() < this.wideBottomLayerHoleChance)
/* 82 */       return true; 
/* 85 */     boolean corner = (dx == currentRadius && dz == currentRadius);
/* 86 */     boolean wideLayer = (currentRadius > 2);
/* 88 */     if (wideLayer)
/* 90 */       return (corner || (dx + dz > currentRadius * 2 - 2 && random.nextFloat() < this.cornerHoleChance)); 
/* 92 */     return (corner && random.nextFloat() < this.cornerHoleChance);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\CherryFoliagePlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */