package net.minecraft.server.jsonrpc.internalapi;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface MinecraftExecutorService {
  <V> CompletableFuture<V> submit(Supplier<V> paramSupplier);
  
  CompletableFuture<Void> submit(Runnable paramRunnable);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftExecutorService.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */