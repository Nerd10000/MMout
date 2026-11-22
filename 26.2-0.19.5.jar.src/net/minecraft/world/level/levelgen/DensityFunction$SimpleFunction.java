/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ public interface SimpleFunction extends DensityFunction {
/*    */   default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 89 */     contextProvider.fillAllDirectly(output, this);
/*    */   }
/*    */   
/*    */   default DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 94 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunction$SimpleFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */