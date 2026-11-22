/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ public final class Multiple<T, P extends Predicate<T>> extends Record implements CollectionContentsPredicate<T, P> {
/*    */   private final List<P> tests;
/*    */   
/*    */   public Multiple(List<P> tests) {
/* 61 */     this.tests = tests;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple<TT;TP;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple<TT;TP;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple<TT;TP;>;
/*    */   }
/*    */   
/*    */   public List<P> tests() {
/* 61 */     return this.tests;
/*    */   }
/*    */   
/*    */   public boolean test(Iterable<? extends T> values) {
/* 64 */     List<Predicate<T>> testsToMatch = new ArrayList<>(this.tests);
/* 65 */     for (T value : values) {
/* 66 */       testsToMatch.removeIf(p -> p.test(value));
/* 67 */       if (testsToMatch.isEmpty())
/* 68 */         return true; 
/*    */     } 
/* 72 */     return false;
/*    */   }
/*    */   
/*    */   public List<P> unpack() {
/* 77 */     return this.tests;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\CollectionContentsPredicate$Multiple.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */