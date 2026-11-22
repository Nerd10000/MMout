/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ class null implements PreparableReloadListener.PreparationBarrier {
/*    */   public <T> CompletableFuture<T> wait(T t) {
/* 86 */     mainThreadExecutor.execute(() -> {
/*    */           SimpleReloadInstance.this.preparingListeners.remove(listener);
/*    */           if (SimpleReloadInstance.this.preparingListeners.isEmpty())
/*    */             SimpleReloadInstance.this.allPreparations.complete(Unit.INSTANCE); 
/*    */         });
/* 92 */     return SimpleReloadInstance.this.allPreparations.thenCombine(previousBarrier, (v1, v2) -> t);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\SimpleReloadInstance$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */