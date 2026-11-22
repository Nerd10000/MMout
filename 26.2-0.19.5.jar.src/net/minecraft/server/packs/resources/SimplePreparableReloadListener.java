/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.util.profiling.Profiler;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ 
/*    */ public abstract class SimplePreparableReloadListener<T> implements PreparableReloadListener {
/*    */   public final CompletableFuture<Void> reload(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor) {
/* 12 */     ResourceManager manager = currentReload.resourceManager();
/* 14 */     Objects.requireNonNull(preparationBarrier);
/* 14 */     return CompletableFuture.supplyAsync(() -> prepare(manager, Profiler.get()), taskExecutor).thenCompose(preparationBarrier::wait)
/* 15 */       .thenAcceptAsync(preparations -> apply((T)preparations, manager, Profiler.get()), reloadExecutor);
/*    */   }
/*    */   
/*    */   protected abstract T prepare(ResourceManager paramResourceManager, ProfilerFiller paramProfilerFiller);
/*    */   
/*    */   protected abstract void apply(T paramT, ResourceManager paramResourceManager, ProfilerFiller paramProfilerFiller);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\SimplePreparableReloadListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */