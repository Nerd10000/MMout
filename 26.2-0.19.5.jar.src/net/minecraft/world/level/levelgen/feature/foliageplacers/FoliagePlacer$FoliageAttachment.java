/*     */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ 
/*     */ public final class FoliageAttachment {
/*     */   private final BlockPos pos;
/*     */   
/*     */   private final int radiusOffset;
/*     */   
/*     */   private final boolean doubleTrunk;
/*     */   
/*     */   public FoliageAttachment(BlockPos pos, int radiusOffset, boolean doubleTrunk) {
/* 158 */     this.pos = pos;
/* 159 */     this.radiusOffset = radiusOffset;
/* 160 */     this.doubleTrunk = doubleTrunk;
/*     */   }
/*     */   
/*     */   public BlockPos pos() {
/* 164 */     return this.pos;
/*     */   }
/*     */   
/*     */   public int radiusOffset() {
/* 168 */     return this.radiusOffset;
/*     */   }
/*     */   
/*     */   public boolean doubleTrunk() {
/* 172 */     return this.doubleTrunk;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\FoliagePlacer$FoliageAttachment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */