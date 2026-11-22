package net.minecraft.client.multiplayer;

@FunctionalInterface
interface ValueMapType<K, V> {
  ClientDebugSubscriber.ValueMap<K, V> get(ClientDebugSubscriber.ValueMaps<V> paramValueMaps);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientDebugSubscriber$ValueMapType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */