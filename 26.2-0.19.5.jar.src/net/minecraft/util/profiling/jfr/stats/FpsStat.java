/*   */ package net.minecraft.util.profiling.jfr.stats;
/*   */ 
/*   */ import jdk.jfr.consumer.RecordedEvent;
/*   */ 
/*   */ public final class FpsStat extends Record {
/*   */   private final int fps;
/*   */   
/*   */   public FpsStat(int fps) {
/* 5 */     this.fps = fps;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/FpsStat;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FpsStat;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/FpsStat;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/FpsStat;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/FpsStat;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/FpsStat;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public int fps() {
/* 5 */     return this.fps;
/*   */   }
/*   */   
/*   */   public static FpsStat from(RecordedEvent event, String field) {
/* 7 */     return new FpsStat(event.getInt(field));
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\stats\FpsStat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */