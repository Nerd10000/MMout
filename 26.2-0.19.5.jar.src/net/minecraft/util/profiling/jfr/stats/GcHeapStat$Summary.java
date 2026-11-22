/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ import java.time.Duration;
/*    */ 
/*    */ public final class Summary extends Record {
/*    */   private final Duration duration;
/*    */   
/*    */   private final Duration gcTotalDuration;
/*    */   
/*    */   private final int totalGCs;
/*    */   
/*    */   private final double allocationRateBytesPerSecond;
/*    */   
/*    */   public Summary(Duration duration, Duration gcTotalDuration, int totalGCs, double allocationRateBytesPerSecond) {
/* 48 */     this.duration = duration;
/* 48 */     this.gcTotalDuration = gcTotalDuration;
/* 48 */     this.totalGCs = totalGCs;
/* 48 */     this.allocationRateBytesPerSecond = allocationRateBytesPerSecond;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/GcHeapStat$Summary;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Duration duration() {
/* 48 */     return this.duration;
/*    */   }
/*    */   
/*    */   public Duration gcTotalDuration() {
/* 48 */     return this.gcTotalDuration;
/*    */   }
/*    */   
/*    */   public int totalGCs() {
/* 48 */     return this.totalGCs;
/*    */   }
/*    */   
/*    */   public double allocationRateBytesPerSecond() {
/* 48 */     return this.allocationRateBytesPerSecond;
/*    */   }
/*    */   
/*    */   public float gcOverHead() {
/* 50 */     return (float)this.gcTotalDuration.toMillis() / (float)this.duration.toMillis();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\stats\GcHeapStat$Summary.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */