package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;

public interface IndexMerger {
  DoubleList getList();
  
  boolean forMergedIndexes(IndexConsumer paramIndexConsumer);
  
  int size();
  
  public static interface IndexConsumer {
    boolean merge(int param1Int1, int param1Int2, int param1Int3);
  }
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\shapes\IndexMerger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */