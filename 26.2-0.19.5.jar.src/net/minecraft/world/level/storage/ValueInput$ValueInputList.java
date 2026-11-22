package net.minecraft.world.level.storage;

import java.util.stream.Stream;

public interface ValueInputList extends Iterable<ValueInput> {
  boolean isEmpty();
  
  Stream<ValueInput> stream();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\ValueInput$ValueInputList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */