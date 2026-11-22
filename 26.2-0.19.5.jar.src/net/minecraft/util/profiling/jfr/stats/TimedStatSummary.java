/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.profiling.jfr.Percentiles;
/*    */ 
/*    */ public final class TimedStatSummary<T extends TimedStat> extends Record {
/*    */   private final T fastest;
/*    */   
/*    */   private final T slowest;
/*    */   
/*    */   private final T secondSlowest;
/*    */   
/*    */   private final int count;
/*    */   
/*    */   private final Map<Integer, Double> percentilesNanos;
/*    */   
/*    */   private final Duration totalDuration;
/*    */   
/*    */   public TimedStatSummary(T fastest, T slowest, T secondSlowest, int count, Map<Integer, Double> percentilesNanos, Duration totalDuration) {
/* 12 */     this.fastest = fastest;
/* 12 */     this.slowest = slowest;
/* 12 */     this.secondSlowest = secondSlowest;
/* 12 */     this.count = count;
/* 12 */     this.percentilesNanos = percentilesNanos;
/* 12 */     this.totalDuration = totalDuration;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary<TT;>;
/*    */   }
/*    */   
/*    */   public T fastest() {
/* 12 */     return this.fastest;
/*    */   }
/*    */   
/*    */   public T slowest() {
/* 12 */     return this.slowest;
/*    */   }
/*    */   
/*    */   public T secondSlowest() {
/* 12 */     return this.secondSlowest;
/*    */   }
/*    */   
/*    */   public int count() {
/* 12 */     return this.count;
/*    */   }
/*    */   
/*    */   public Map<Integer, Double> percentilesNanos() {
/* 12 */     return this.percentilesNanos;
/*    */   }
/*    */   
/*    */   public Duration totalDuration() {
/* 12 */     return this.totalDuration;
/*    */   }
/*    */   
/*    */   public static <T extends TimedStat> Optional<TimedStatSummary<T>> summary(List<T> values) {
/* 21 */     if (values.isEmpty())
/* 22 */       return Optional.empty(); 
/* 24 */     List<T> sorted = values.stream().sorted(Comparator.comparing(TimedStat::duration)).toList();
/* 25 */     Duration totalDuration = sorted.stream().map(TimedStat::duration).reduce(Duration::plus).orElse(Duration.ZERO);
/* 26 */     TimedStat timedStat1 = (TimedStat)sorted.getFirst();
/* 27 */     TimedStat timedStat2 = (TimedStat)sorted.getLast();
/* 28 */     TimedStat timedStat3 = (sorted.size() > 1) ? (TimedStat)sorted.get(sorted.size() - 2) : null;
/* 29 */     int count = sorted.size();
/* 30 */     Map<Integer, Double> percentilesNanos = Percentiles.evaluate(sorted.stream().mapToLong(it -> it.duration().toNanos()).toArray());
/* 31 */     return Optional.of(new TimedStatSummary<>((T)timedStat1, (T)timedStat2, (T)timedStat3, count, percentilesNanos, totalDuration));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\stats\TimedStatSummary.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */