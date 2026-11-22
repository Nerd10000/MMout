/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ class null implements TaskChainer {
/*    */   public <T> void append(CompletableFuture<T> preparation, Consumer<T> chainedTask) {
/* 18 */     preparation.thenAcceptAsync(chainedTask, executor).exceptionally(e -> {
/*    */           LOGGER.error("Task failed", e);
/*    */           return null;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\TaskChainer$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */