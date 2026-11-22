package net.minecraft.world.ticks;

import java.util.List;

public interface SerializableTickContainer<T> {
  List<SavedTick<T>> pack(long paramLong);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\ticks\SerializableTickContainer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */