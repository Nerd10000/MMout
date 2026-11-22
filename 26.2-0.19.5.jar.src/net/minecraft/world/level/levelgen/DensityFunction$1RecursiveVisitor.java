/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ class RecursiveVisitor implements DensityFunction.Visitor {
/*    */   RecursiveVisitor(DensityFunction this$0) {}
/*    */   
/*    */   public DensityFunction apply(DensityFunction input) {
/* 36 */     return visitor.apply(input.mapChildren(this));
/*    */   }
/*    */   
/*    */   public DensityFunction.NoiseHolder visitNoise(DensityFunction.NoiseHolder noise) {
/* 41 */     return visitor.visitNoise(noise);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunction$1RecursiveVisitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */