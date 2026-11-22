package net.minecraft.world.level.block;

public interface Combiner<S, T> {
  T acceptDouble(S paramS1, S paramS2);
  
  T acceptSingle(S paramS);
  
  T acceptNone();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\DoubleBlockCombiner$Combiner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */