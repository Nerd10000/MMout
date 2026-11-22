/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import java.util.concurrent.ForkJoinPool;
/*     */ import java.util.concurrent.ForkJoinWorkerThread;
/*     */ 
/*     */ class null extends ForkJoinWorkerThread {
/*     */   null(ForkJoinPool pool) {
/* 216 */     super(pool);
/*     */   }
/*     */   
/*     */   protected void onStart() {
/* 219 */     TracyClient.setThreadName(threadName, name.hashCode());
/* 220 */     super.onStart();
/*     */   }
/*     */   
/*     */   protected void onTermination(Throwable exception) {
/* 225 */     if (exception != null) {
/* 226 */       Util.LOGGER.warn("{} died", getName(), exception);
/*     */     } else {
/* 228 */       Util.LOGGER.debug("{} shutdown", getName());
/*     */     } 
/* 230 */     super.onTermination(exception);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Util$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */