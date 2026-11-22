/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.time.Duration;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public final class FileIOStat extends Record {
/*    */   private final Duration duration;
/*    */   
/*    */   private final String path;
/*    */   
/*    */   private final long bytes;
/*    */   
/*    */   public FileIOStat(Duration duration, String path, long bytes) {
/* 11 */     this.duration = duration;
/* 11 */     this.path = path;
/* 11 */     this.bytes = bytes;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Duration duration() {
/* 11 */     return this.duration;
/*    */   }
/*    */   
/*    */   public String path() {
/* 11 */     return this.path;
/*    */   }
/*    */   
/*    */   public long bytes() {
/* 11 */     return this.bytes;
/*    */   }
/*    */   
/*    */   public static Summary summary(Duration recordingDuration, List<FileIOStat> ioStats) {
/* 14 */     long totalBytes = ioStats.stream().mapToLong(it -> it.bytes).sum();
/* 15 */     return new Summary(totalBytes, totalBytes / recordingDuration
/*    */         
/* 17 */         .getSeconds(), ioStats
/* 18 */         .size(), ioStats
/* 19 */         .size() / recordingDuration.getSeconds(), ioStats
/* 20 */         .stream().map(FileIOStat::duration).reduce(Duration.ZERO, Duration::plus), ((Map)ioStats
/* 21 */         .stream().filter(it -> (it.path != null)).collect(Collectors.groupingBy(stat -> stat.path, Collectors.summingLong(it -> it.bytes))))
/* 22 */         .entrySet().stream()
/* 23 */         .sorted(Map.Entry.comparingByValue().reversed())
/* 24 */         .map(e -> Pair.of(e.getKey(), e.getValue()))
/* 25 */         .limit(10L)
/* 26 */         .toList());
/*    */   }
/*    */   
/*    */   public static final class Summary extends Record {
/*    */     private final long totalBytes;
/*    */     
/*    */     private final double bytesPerSecond;
/*    */     
/*    */     private final long counts;
/*    */     
/*    */     private final double countsPerSecond;
/*    */     
/*    */     private final Duration timeSpentInIO;
/*    */     
/*    */     private final List<Pair<String, Long>> topTenContributorsByTotalBytes;
/*    */     
/*    */     public Summary(long totalBytes, double bytesPerSecond, long counts, double countsPerSecond, Duration timeSpentInIO, List<Pair<String, Long>> topTenContributorsByTotalBytes) {
/* 30 */       this.totalBytes = totalBytes;
/* 30 */       this.bytesPerSecond = bytesPerSecond;
/* 30 */       this.counts = counts;
/* 30 */       this.countsPerSecond = countsPerSecond;
/* 30 */       this.timeSpentInIO = timeSpentInIO;
/* 30 */       this.topTenContributorsByTotalBytes = topTenContributorsByTotalBytes;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/FileIOStat$Summary;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public long totalBytes() {
/* 30 */       return this.totalBytes;
/*    */     }
/*    */     
/*    */     public double bytesPerSecond() {
/* 30 */       return this.bytesPerSecond;
/*    */     }
/*    */     
/*    */     public long counts() {
/* 30 */       return this.counts;
/*    */     }
/*    */     
/*    */     public double countsPerSecond() {
/* 30 */       return this.countsPerSecond;
/*    */     }
/*    */     
/*    */     public Duration timeSpentInIO() {
/* 30 */       return this.timeSpentInIO;
/*    */     }
/*    */     
/*    */     public List<Pair<String, Long>> topTenContributorsByTotalBytes() {
/* 30 */       return this.topTenContributorsByTotalBytes;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\stats\FileIOStat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */