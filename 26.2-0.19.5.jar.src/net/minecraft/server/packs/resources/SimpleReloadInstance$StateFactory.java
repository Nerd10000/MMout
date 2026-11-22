/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface StateFactory<S> {
/*     */   public static final StateFactory<Void> SIMPLE;
/*     */   
/*     */   CompletableFuture<S> create(PreparableReloadListener.SharedState paramSharedState, PreparableReloadListener.PreparationBarrier paramPreparationBarrier, PreparableReloadListener paramPreparableReloadListener, Executor paramExecutor1, Executor paramExecutor2);
/*     */   
/*     */   static {
/* 116 */     SIMPLE = ((currentReload, previousStep, listener, taskExecutor, reloadExecutor) -> listener.reload(currentReload, taskExecutor, previousStep, reloadExecutor));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\SimpleReloadInstance$StateFactory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */