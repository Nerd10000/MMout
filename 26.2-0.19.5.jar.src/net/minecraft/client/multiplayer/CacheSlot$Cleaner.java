package net.minecraft.client.multiplayer;

@FunctionalInterface
public interface Cleaner<C extends CacheSlot.Cleaner<C>> {
  void registerForCleaning(CacheSlot<C, ?> paramCacheSlot);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\CacheSlot$Cleaner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */