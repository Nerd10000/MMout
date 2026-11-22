package net.minecraft.world.level.storage;

import java.util.stream.Stream;

public interface TypedInputList<T> extends Iterable<T> {
  boolean isEmpty();
  
  Stream<T> stream();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\ValueInput$TypedInputList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */