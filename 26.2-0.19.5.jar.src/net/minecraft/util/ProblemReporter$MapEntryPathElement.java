/*    */ package net.minecraft.util;
/*    */ 
/*    */ public final class MapEntryPathElement extends Record implements ProblemReporter.PathElement {
/*    */   private final String name;
/*    */   
/*    */   private final String key;
/*    */   
/*    */   public MapEntryPathElement(String name, String key) {
/* 78 */     this.name = name;
/* 78 */     this.key = key;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$MapEntryPathElement;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$MapEntryPathElement;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$MapEntryPathElement;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$MapEntryPathElement;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$MapEntryPathElement;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$MapEntryPathElement;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String name() {
/* 78 */     return this.name;
/*    */   }
/*    */   
/*    */   public String key() {
/* 78 */     return this.key;
/*    */   }
/*    */   
/*    */   public String get() {
/* 81 */     return "." + this.name + "[" + this.key + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ProblemReporter$MapEntryPathElement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */