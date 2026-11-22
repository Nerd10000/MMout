/*    */ package net.minecraft.util;
/*    */ 
/*    */ public final class IndexedFieldPathElement extends Record implements ProblemReporter.PathElement {
/*    */   private final String name;
/*    */   
/*    */   private final int index;
/*    */   
/*    */   public IndexedFieldPathElement(String name, int index) {
/* 64 */     this.name = name;
/* 64 */     this.index = index;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String name() {
/* 64 */     return this.name;
/*    */   }
/*    */   
/*    */   public int index() {
/* 64 */     return this.index;
/*    */   }
/*    */   
/*    */   public String get() {
/* 67 */     return "." + this.name + "[" + this.index + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ProblemReporter$IndexedFieldPathElement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */