/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.time.Duration;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class Summary extends Record {
/*    */   private final long totalBytes;
/*    */   
/*    */   private final double bytesPerSecond;
/*    */   
/*    */   private final long counts;
/*    */   
/*    */   private final double countsPerSecond;
/*    */   
/*    */   private final Duration timeSpentInIO;
/*    */   
/*    */   private final List<Pair<String, Long>> topTenContributorsByTotalBytes;
/*    */   
/*    */   public Summary(long totalBytes, double bytesPerSecond, long counts, double countsPerSecond, Duration timeSpentInIO, List<Pair<String, Long>> topTenContributorsByTotalBytes) {
/* 30 */     this.totalBytes = totalBytes;
/* 30 */     this.bytesPerSecond = bytesPerSecond;
/* 30 */     this.counts = counts;
/* 30 */     this.countsPerSecond = countsPerSecond;
/* 30 */     this.timeSpentInIO = timeSpentInIO;
/* 30 */     this.topTenContributorsByTotalBytes = topTenContributorsByTotalBytes;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public long totalBytes() {
/* 30 */     return this.totalBytes;
/*    */   }
/*    */   
/*    */   public double bytesPerSecond() {
/* 30 */     return this.bytesPerSecond;
/*    */   }
/*    */   
/*    */   public long counts() {
/* 30 */     return this.counts;
/*    */   }
/*    */   
/*    */   public double countsPerSecond() {
/* 30 */     return this.countsPerSecond;
/*    */   }
/*    */   
/*    */   public Duration timeSpentInIO() {
/* 30 */     return this.timeSpentInIO;
/*    */   }
/*    */   
/*    */   public List<Pair<String, Long>> topTenContributorsByTotalBytes() {
/* 30 */     return this.topTenContributorsByTotalBytes;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\stats\FileIOStat$Summary.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */