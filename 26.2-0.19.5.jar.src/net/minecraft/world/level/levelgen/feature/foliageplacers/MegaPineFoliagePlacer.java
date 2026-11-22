/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class MegaPineFoliagePlacer extends FoliagePlacer {
/*    */   public static final MapCodec<MegaPineFoliagePlacer> CODEC;
/*    */   
/*    */   private final IntProvider crownHeight;
/*    */   
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).and((App)IntProviders.codec(0, 24).fieldOf("crown_height").forGetter(())).apply((Applicative)i, MegaPineFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public MegaPineFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider crownHeight) {
/* 22 */     super(radius, offset);
/* 23 */     this.crownHeight = crownHeight;
/*    */   }
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 28 */     return FoliagePlacerType.MEGA_PINE_FOLIAGE_PLACER;
/*    */   }
/*    */   
/*    */   protected void createFoliage(WorldGenLevel level, FoliagePlacer.FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliagePlacer.FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
/* 33 */     BlockPos foliagePos = foliageAttachment.pos();
/* 35 */     int prevRadius = 0;
/* 36 */     for (int yy = foliagePos.getY() - foliageHeight + offset; yy <= foliagePos.getY() + offset; yy++) {
/* 37 */       int jaggedRadius, yo = foliagePos.getY() - yy;
/* 38 */       int smoothRadius = leafRadius + foliageAttachment.radiusOffset() + Mth.floor(yo / foliageHeight * 3.5F);
/* 40 */       if (yo > 0 && smoothRadius == prevRadius && (yy & 0x1) == 0) {
/* 41 */         jaggedRadius = smoothRadius + 1;
/*    */       } else {
/* 43 */         jaggedRadius = smoothRadius;
/*    */       } 
/* 46 */       placeLeavesRow(level, foliageSetter, random, config, new BlockPos(foliagePos.getX(), yy, foliagePos.getZ()), jaggedRadius, 0, foliageAttachment.doubleTrunk());
/* 47 */       prevRadius = smoothRadius;
/*    */     } 
/*    */   }
/*    */   
/*    */   public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration config) {
/* 53 */     return this.crownHeight.sample(random);
/*    */   }
/*    */   
/*    */   protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/* 58 */     if (dx + dz >= 7)
/* 59 */       return true; 
/* 61 */     return (dx * dx + dz * dz > currentRadius * currentRadius);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\MegaPineFoliagePlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */