/*    */ package net.minecraft.client;
/*    */ 
/*    */ import java.util.concurrent.locks.LockSupport;
/*    */ 
/*    */ public class FramerateLimiter {
/*    */   private static final double OVERSHOOT_SMOOTHING = 0.1D;
/*    */   
/*    */   private static final long MAX_CURRENT_OVERSHOOT_NS = 25000000L;
/*    */   
/*    */   private static final long MAX_AVERAGE_OVERSHOOT_NS = 2000000L;
/*    */   
/*    */   private static final long ONE_SECOND_IN_NANOSECONDS = 1000000000L;
/*    */   
/*    */   private static final long SPIN_SAFETY_BUFFER_NS = 500000L;
/*    */   
/* 12 */   private static long lastFrameTime = System.nanoTime();
/*    */   
/* 13 */   private static long averageOvershootNs = 0L;
/*    */   
/*    */   private static int lastFramerateLimit;
/*    */   
/*    */   public static void limitDisplayFPS(int framerateLimit) {
/* 17 */     long targetTimePerFrame = 1000000000L / framerateLimit;
/* 18 */     long targetTimeNs = lastFrameTime + targetTimePerFrame;
/* 20 */     if (framerateLimit != lastFramerateLimit) {
/* 21 */       averageOvershootNs = 0L;
/* 22 */       lastFramerateLimit = framerateLimit;
/*    */     } 
/*    */     long remainingTimeNs;
/* 26 */     while ((remainingTimeNs = targetTimeNs - System.nanoTime()) > 0L) {
/* 27 */       if (remainingTimeNs > averageOvershootNs + 500000L) {
/* 28 */         long sleepStartTimeNs = System.nanoTime();
/* 30 */         long expectedSleepTimeNs = remainingTimeNs - averageOvershootNs - 500000L;
/* 32 */         if (Thread.interrupted())
/*    */           continue; 
/* 35 */         LockSupport.parkNanos(expectedSleepTimeNs);
/* 37 */         long sleepDurationNs = System.nanoTime() - sleepStartTimeNs;
/* 38 */         long currentOvershootNs = sleepDurationNs - expectedSleepTimeNs;
/* 40 */         if (currentOvershootNs > 0L && currentOvershootNs < 25000000L) {
/* 41 */           averageOvershootNs = (long)(0.1D * currentOvershootNs + 0.9D * averageOvershootNs);
/* 42 */           averageOvershootNs = Math.min(averageOvershootNs, 2000000L);
/*    */         } 
/*    */         continue;
/*    */       } 
/* 45 */       Thread.onSpinWait();
/*    */     } 
/* 49 */     lastFrameTime = System.nanoTime();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\FramerateLimiter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */