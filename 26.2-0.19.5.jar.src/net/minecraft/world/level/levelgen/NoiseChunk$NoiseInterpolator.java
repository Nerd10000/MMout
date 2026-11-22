/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class NoiseInterpolator implements NoiseChunk.NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */   private double[][] slice0;
/*     */   
/*     */   private double[][] slice1;
/*     */   
/*     */   private final DensityFunction noiseFiller;
/*     */   
/*     */   private double noise000;
/*     */   
/*     */   private double noise001;
/*     */   
/*     */   private double noise100;
/*     */   
/*     */   private double noise101;
/*     */   
/*     */   private double noise010;
/*     */   
/*     */   private double noise011;
/*     */   
/*     */   private double noise110;
/*     */   
/*     */   private double noise111;
/*     */   
/*     */   private double valueXZ00;
/*     */   
/*     */   private double valueXZ10;
/*     */   
/*     */   private double valueXZ01;
/*     */   
/*     */   private double valueXZ11;
/*     */   
/*     */   private double valueZ0;
/*     */   
/*     */   private double valueZ1;
/*     */   
/*     */   private double value;
/*     */   
/*     */   private NoiseInterpolator(DensityFunction noiseFiller) {
/* 558 */     this.noiseFiller = noiseFiller;
/* 559 */     this.slice0 = allocateSlice(NoiseChunk.this.cellCountY, NoiseChunk.this.cellCountXZ);
/* 560 */     this.slice1 = allocateSlice(NoiseChunk.this.cellCountY, NoiseChunk.this.cellCountXZ);
/* 562 */     NoiseChunk.this.interpolators.add(this);
/*     */   }
/*     */   
/*     */   private double[][] allocateSlice(int cellCountY, int cellCountZ) {
/* 566 */     int sizeZ = cellCountZ + 1;
/* 567 */     int sizeY = cellCountY + 1;
/* 568 */     double[][] result = new double[sizeZ][sizeY];
/* 569 */     for (int cellZIndex = 0; cellZIndex < sizeZ; cellZIndex++)
/* 570 */       result[cellZIndex] = new double[sizeY]; 
/* 572 */     return result;
/*     */   }
/*     */   
/*     */   private void selectCellYZ(int cellYIndex, int cellZIndex) {
/* 580 */     this.noise000 = this.slice0[cellZIndex][cellYIndex];
/* 581 */     this.noise001 = this.slice0[cellZIndex + 1][cellYIndex];
/* 582 */     this.noise100 = this.slice1[cellZIndex][cellYIndex];
/* 583 */     this.noise101 = this.slice1[cellZIndex + 1][cellYIndex];
/* 585 */     this.noise010 = this.slice0[cellZIndex][cellYIndex + 1];
/* 586 */     this.noise011 = this.slice0[cellZIndex + 1][cellYIndex + 1];
/* 587 */     this.noise110 = this.slice1[cellZIndex][cellYIndex + 1];
/* 588 */     this.noise111 = this.slice1[cellZIndex + 1][cellYIndex + 1];
/*     */   }
/*     */   
/*     */   private void updateForY(double factorY) {
/* 595 */     this.valueXZ00 = Mth.lerp(factorY, this.noise000, this.noise010);
/* 596 */     this.valueXZ10 = Mth.lerp(factorY, this.noise100, this.noise110);
/* 597 */     this.valueXZ01 = Mth.lerp(factorY, this.noise001, this.noise011);
/* 598 */     this.valueXZ11 = Mth.lerp(factorY, this.noise101, this.noise111);
/*     */   }
/*     */   
/*     */   private void updateForX(double factorX) {
/* 605 */     this.valueZ0 = Mth.lerp(factorX, this.valueXZ00, this.valueXZ10);
/* 606 */     this.valueZ1 = Mth.lerp(factorX, this.valueXZ01, this.valueXZ11);
/*     */   }
/*     */   
/*     */   private void updateForZ(double factorZ) {
/* 613 */     this.value = Mth.lerp(factorZ, this.valueZ0, this.valueZ1);
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 618 */     if (context != NoiseChunk.this)
/* 619 */       return this.noiseFiller.compute(context); 
/* 621 */     if (!NoiseChunk.this.interpolating)
/* 622 */       throw new IllegalStateException("Trying to sample interpolator outside the interpolation loop"); 
/* 624 */     if (NoiseChunk.this.fillingCell)
/* 625 */       return Mth.lerp3(NoiseChunk.this.inCellX / NoiseChunk.this.cellWidth, NoiseChunk.this.inCellY / NoiseChunk.this.cellHeight, NoiseChunk.this.inCellZ / NoiseChunk.this.cellWidth, this.noise000, this.noise100, this.noise010, this.noise110, this.noise001, this.noise101, this.noise011, this.noise111); 
/* 633 */     return this.value;
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 638 */     if (NoiseChunk.this.fillingCell) {
/* 640 */       contextProvider.fillAllDirectly(output, this);
/*     */       return;
/*     */     } 
/* 643 */     wrapped().fillArray(output, contextProvider);
/*     */   }
/*     */   
/*     */   public DensityFunction wrapped() {
/* 648 */     return this.noiseFiller;
/*     */   }
/*     */   
/*     */   private void swapSlices() {
/* 653 */     double[][] tmp = this.slice0;
/* 654 */     this.slice0 = this.slice1;
/* 655 */     this.slice1 = tmp;
/*     */   }
/*     */   
/*     */   public DensityFunctions.Marker.Type type() {
/* 660 */     return DensityFunctions.Marker.Type.Interpolated;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseChunk$NoiseInterpolator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */