/*     */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ 
/*     */ class FoliageCoords {
/*     */   private final FoliagePlacer.FoliageAttachment attachment;
/*     */   
/*     */   private final int branchBase;
/*     */   
/*     */   public FoliageCoords(BlockPos pos, int branchBase) {
/* 193 */     this.attachment = new FoliagePlacer.FoliageAttachment(pos, 0, false);
/* 194 */     this.branchBase = branchBase;
/*     */   }
/*     */   
/*     */   public int getBranchBase() {
/* 198 */     return this.branchBase;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\FancyTrunkPlacer$FoliageCoords.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */