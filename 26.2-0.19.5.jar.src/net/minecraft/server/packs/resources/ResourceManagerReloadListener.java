/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.util.profiling.Profiler;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ 
/*    */ public interface ResourceManagerReloadListener extends PreparableReloadListener {
/*    */   default CompletableFuture<Void> reload(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor) {
/* 13 */     ResourceManager manager = currentReload.resourceManager();
/* 14 */     return preparationBarrier.<Unit>wait(Unit.INSTANCE).thenRunAsync(() -> {
/*    */           ProfilerFiller reloadProfiler = Profiler.get();
/*    */           reloadProfiler.push("listener");
/*    */           onResourceManagerReload(manager);
/*    */           reloadProfiler.pop();
/* 14 */         }reloadExecutor);
/*    */   }
/*    */   
/*    */   void onResourceManagerReload(ResourceManager paramResourceManager);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\ResourceManagerReloadListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */