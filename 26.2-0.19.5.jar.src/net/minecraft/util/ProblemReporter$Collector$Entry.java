/*    */ package net.minecraft.util;
/*    */ 
/*    */ final class Entry extends Record {
/*    */   private final ProblemReporter.Collector source;
/*    */   
/*    */   private final ProblemReporter.Problem problem;
/*    */   
/*    */   private Entry(ProblemReporter.Collector source, ProblemReporter.Problem problem) {
/* 93 */     this.source = source;
/* 93 */     this.problem = problem;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #93	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #93	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #93	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ProblemReporter.Collector source() {
/* 93 */     return this.source;
/*    */   }
/*    */   
/*    */   public ProblemReporter.Problem problem() {
/* 93 */     return this.problem;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ProblemReporter$Collector$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */