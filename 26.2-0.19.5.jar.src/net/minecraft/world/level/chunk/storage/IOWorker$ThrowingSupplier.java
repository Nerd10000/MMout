package net.minecraft.world.level.chunk.storage;

@FunctionalInterface
interface ThrowingSupplier<T> {
  T get() throws Exception;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\storage\IOWorker$ThrowingSupplier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */