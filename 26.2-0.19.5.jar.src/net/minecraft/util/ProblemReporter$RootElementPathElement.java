/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class RootElementPathElement extends Record implements ProblemReporter.PathElement {
/*    */   private final ResourceKey<?> id;
/*    */   
/*    */   public RootElementPathElement(ResourceKey<?> id) {
/* 50 */     this.id = id;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$RootElementPathElement;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$RootElementPathElement;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$RootElementPathElement;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$RootElementPathElement;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$RootElementPathElement;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$RootElementPathElement;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResourceKey<?> id() {
/* 50 */     return this.id;
/*    */   }
/*    */   
/*    */   public String get() {
/* 53 */     return "{" + String.valueOf(this.id.identifier()) + "@" + String.valueOf(this.id.registry()) + "}";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ProblemReporter$RootElementPathElement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */