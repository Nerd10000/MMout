/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.concurrent.CancellationException;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionException;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Consumer;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class FutureChain implements TaskChainer, AutoCloseable {
/* 13 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 15 */   private CompletableFuture<?> head = CompletableFuture.completedFuture(null);
/*    */   
/*    */   private final Executor executor;
/*    */   
/*    */   private volatile boolean closed;
/*    */   
/*    */   public FutureChain(Executor executor) {
/* 21 */     this.executor = executor;
/*    */   }
/*    */   
/*    */   public <T> void append(CompletableFuture<T> preparation, Consumer<T> chainedTask) {
/* 26 */     this
/*    */       
/* 33 */       .head = this.head.thenCombine(preparation, (ignored, value) -> value).thenAcceptAsync(value -> {
/*    */           if (!this.closed)
/*    */             chainedTask.accept(value); 
/* 33 */         }this.executor).exceptionally(t -> {
/*    */           if (t instanceof CompletionException) {
/*    */             CompletionException c = (CompletionException)t;
/*    */             t = c.getCause();
/*    */           } 
/*    */           if (t instanceof CancellationException) {
/*    */             CancellationException c = (CancellationException)t;
/*    */             throw c;
/*    */           } 
/*    */           LOGGER.error("Chain link failed, continuing to next one", t);
/*    */           return null;
/*    */         });
/*    */   }
/*    */   
/*    */   public void close() {
/* 49 */     this.closed = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\FutureChain.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */