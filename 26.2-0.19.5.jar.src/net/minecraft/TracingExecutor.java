/*    */ package net.minecraft;
/*    */ 
/*    */ import com.mojang.jtracy.TracyClient;
/*    */ import com.mojang.jtracy.Zone;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ 
/*    */ public final class TracingExecutor extends Record implements Executor {
/*    */   private final ExecutorService service;
/*    */   
/*    */   public TracingExecutor(ExecutorService service) {
/* 10 */     this.service = service;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/TracingExecutor;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/TracingExecutor;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/TracingExecutor;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/TracingExecutor;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/TracingExecutor;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/TracingExecutor;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ExecutorService service() {
/* 10 */     return this.service;
/*    */   }
/*    */   
/*    */   public Executor forName(String name) {
/* 12 */     if (SharedConstants.IS_RUNNING_IN_IDE)
/* 13 */       return command -> this.service.execute(()); 
/* 24 */     if (TracyClient.isAvailable())
/* 25 */       return command -> this.service.execute(()); 
/* 31 */     return this.service;
/*    */   }
/*    */   
/*    */   public void execute(Runnable command) {
/* 36 */     this.service.execute(wrapUnnamed(command));
/*    */   }
/*    */   
/*    */   public void shutdownAndAwait(long timeout, TimeUnit unit) {
/*    */     boolean terminated;
/* 40 */     this.service.shutdown();
/*    */     try {
/* 43 */       terminated = this.service.awaitTermination(timeout, unit);
/* 44 */     } catch (InterruptedException e) {
/* 45 */       terminated = false;
/*    */     } 
/* 47 */     if (!terminated)
/* 48 */       this.service.shutdownNow(); 
/*    */   }
/*    */   
/*    */   private static Runnable wrapUnnamed(Runnable command) {
/* 53 */     if (!TracyClient.isAvailable())
/* 54 */       return command; 
/* 56 */     return () -> {
/*    */         Zone ignored = TracyClient.beginZone("task", SharedConstants.IS_RUNNING_IN_IDE);
/*    */         try {
/*    */           command.run();
/*    */           if (ignored != null)
/*    */             ignored.close(); 
/* 57 */         } catch (Throwable t$) {
/*    */           if (ignored != null)
/*    */             try {
/*    */               ignored.close();
/* 57 */             } catch (Throwable x2) {
/*    */               t$.addSuppressed(x2);
/*    */             }  
/*    */           throw t$;
/*    */         } 
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\TracingExecutor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */