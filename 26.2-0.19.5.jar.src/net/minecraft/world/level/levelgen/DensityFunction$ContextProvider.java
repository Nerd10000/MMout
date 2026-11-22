package net.minecraft.world.level.levelgen;

public interface ContextProvider {
  DensityFunction.FunctionContext forIndex(int paramInt);
  
  void fillAllDirectly(double[] paramArrayOfdouble, DensityFunction paramDensityFunction);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunction$ContextProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */