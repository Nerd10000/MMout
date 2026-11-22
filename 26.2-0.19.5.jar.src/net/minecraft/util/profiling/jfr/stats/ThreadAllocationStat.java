/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ import com.google.common.base.MoreObjects;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.time.Duration;
/*    */ import java.time.Instant;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.TreeMap;
/*    */ import java.util.stream.Collectors;
/*    */ import jdk.jfr.consumer.RecordedEvent;
/*    */ import jdk.jfr.consumer.RecordedThread;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class ThreadAllocationStat extends Record {
/*    */   private final Instant timestamp;
/*    */   
/*    */   private final String threadName;
/*    */   
/*    */   private final long totalBytes;
/*    */   
/*    */   public ThreadAllocationStat(Instant timestamp, String threadName, long totalBytes) {
/* 16 */     this.timestamp = timestamp;
/* 16 */     this.threadName = threadName;
/* 16 */     this.totalBytes = totalBytes;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Instant timestamp() {
/* 16 */     return this.timestamp;
/*    */   }
/*    */   
/*    */   public String threadName() {
/* 16 */     return this.threadName;
/*    */   }
/*    */   
/*    */   public long totalBytes() {
/* 16 */     return this.totalBytes;
/*    */   }
/*    */   
/* 17 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private static final String UNKNOWN_THREAD = "unknown";
/*    */   
/*    */   public static ThreadAllocationStat from(RecordedEvent event) {
/* 21 */     RecordedThread recoredThread = event.getThread("thread");
/* 25 */     String threadName = (recoredThread == null) ? "unknown" : (String)MoreObjects.firstNonNull(recoredThread.getJavaName(), "unknown");
/* 27 */     return new ThreadAllocationStat(event.getStartTime(), threadName, event.getLong("allocated"));
/*    */   }
/*    */   
/*    */   public static Summary summary(List<ThreadAllocationStat> stats) {
/* 31 */     Map<String, Double> allocationsPerSecondByThread = new TreeMap<>();
/* 32 */     Map<String, List<ThreadAllocationStat>> byThread = (Map<String, List<ThreadAllocationStat>>)stats.stream().collect(Collectors.groupingBy(it -> it.threadName));
/* 34 */     byThread.forEach((thread, threadStats) -> {
/*    */           if (threadStats.size() < 2)
/*    */             return; 
/*    */           ThreadAllocationStat first = threadStats.getFirst();
/*    */           ThreadAllocationStat last = threadStats.getLast();
/*    */           long duration = Duration.between(first.timestamp, last.timestamp).getSeconds();
/*    */           if (duration <= 0L) {
/*    */             LOGGER.warn("Thread allocation stat timestamps are not in chronological order for thread {}, skipping it", thread);
/*    */             return;
/*    */           } 
/*    */           long diff = last.totalBytes - first.totalBytes;
/*    */           allocationsPerSecondByThread.put(thread, Double.valueOf(diff / duration));
/*    */         });
/* 51 */     return new Summary(allocationsPerSecondByThread);
/*    */   }
/*    */   
/*    */   public static final class Summary extends Record {
/*    */     private final Map<String, Double> allocationsPerSecondByThread;
/*    */     
/*    */     public Summary(Map<String, Double> allocationsPerSecondByThread) {
/* 54 */       this.allocationsPerSecondByThread = allocationsPerSecondByThread;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat$Summary;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #54	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat$Summary;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat$Summary;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #54	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat$Summary;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat$Summary;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #54	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/ThreadAllocationStat$Summary;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Map<String, Double> allocationsPerSecondByThread() {
/* 54 */       return this.allocationsPerSecondByThread;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\stats\ThreadAllocationStat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */