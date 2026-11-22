/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ public interface TaskScheduler<R extends Runnable> extends AutoCloseable {
/*    */   default void close() {}
/*    */   
/*    */   default <Source> CompletableFuture<Source> scheduleWithResult(Consumer<CompletableFuture<Source>> futureConsumer) {
/* 20 */     CompletableFuture<Source> future = new CompletableFuture<>();
/* 21 */     schedule(wrapRunnable(() -> futureConsumer.accept(future)));
/* 22 */     return future;
/*    */   }
/*    */   
/*    */   static TaskScheduler<Runnable> wrapExecutor(final String name, final Executor executor) {
/* 26 */     return new TaskScheduler<Runnable>() {
/*    */         public String name() {
/* 29 */           return name;
/*    */         }
/*    */         
/*    */         public void schedule(Runnable runnable) {
/* 34 */           executor.execute(runnable);
/*    */         }
/*    */         
/*    */         public Runnable wrapRunnable(Runnable runnable) {
/* 39 */           return runnable;
/*    */         }
/*    */         
/*    */         public String toString() {
/* 44 */           return name;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   String name();
/*    */   
/*    */   void schedule(R paramR);
/*    */   
/*    */   R wrapRunnable(Runnable paramRunnable);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\thread\TaskScheduler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */