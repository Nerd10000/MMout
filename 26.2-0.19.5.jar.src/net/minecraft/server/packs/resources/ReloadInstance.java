/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ public interface ReloadInstance {
/*    */   CompletableFuture<?> done();
/*    */   
/*    */   float getActualProgress();
/*    */   
/*    */   default boolean isDone() {
/* 11 */     return done().isDone();
/*    */   }
/*    */   
/*    */   default void checkExceptions() {
/* 15 */     CompletableFuture<?> done = done();
/* 16 */     if (done.isCompletedExceptionally())
/* 17 */       done.join(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\ReloadInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */