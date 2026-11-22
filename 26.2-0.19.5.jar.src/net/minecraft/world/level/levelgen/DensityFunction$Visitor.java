/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ public interface Visitor {
/*    */   DensityFunction apply(DensityFunction paramDensityFunction);
/*    */   
/*    */   default DensityFunction.NoiseHolder visitNoise(DensityFunction.NoiseHolder noise) {
/* 82 */     return noise;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunction$Visitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */