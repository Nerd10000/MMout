package net.minecraft.util;

import java.util.function.Consumer;

public interface Entry<K> {
  void visitRequiredDependencies(Consumer<K> paramConsumer);
  
  void visitOptionalDependencies(Consumer<K> paramConsumer);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\DependencySorter$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */