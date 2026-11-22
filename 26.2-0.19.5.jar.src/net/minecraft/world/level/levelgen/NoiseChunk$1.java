/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ class null implements DensityFunction.ContextProvider {
/*    */   public DensityFunction.FunctionContext forIndex(int cellYIndex) {
/* 81 */     NoiseChunk.this.cellStartBlockY = (cellYIndex + NoiseChunk.this.cellNoiseMinY) * NoiseChunk.this.cellHeight;
/* 82 */     NoiseChunk.this.interpolationCounter++;
/* 84 */     NoiseChunk.this.inCellY = 0;
/* 85 */     NoiseChunk.this.arrayIndex = cellYIndex;
/* 86 */     return NoiseChunk.this;
/*    */   }
/*    */   
/*    */   public void fillAllDirectly(double[] output, DensityFunction function) {
/* 92 */     for (int cellYIndex = 0; cellYIndex < NoiseChunk.this.cellCountY + 1; cellYIndex++) {
/* 93 */       NoiseChunk.this.cellStartBlockY = (cellYIndex + NoiseChunk.this.cellNoiseMinY) * NoiseChunk.this.cellHeight;
/* 94 */       NoiseChunk.this.interpolationCounter++;
/* 96 */       NoiseChunk.this.inCellY = 0;
/* 97 */       NoiseChunk.this.arrayIndex = cellYIndex;
/* 99 */       output[cellYIndex] = function.compute(NoiseChunk.this);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseChunk$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */