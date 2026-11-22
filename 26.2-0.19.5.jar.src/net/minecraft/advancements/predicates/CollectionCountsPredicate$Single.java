/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ public final class Single<T, P extends Predicate<T>> extends Record implements CollectionCountsPredicate<T, P> {
/*    */   private final CollectionCountsPredicate.Entry<T, P> entry;
/*    */   
/*    */   public Single(CollectionCountsPredicate.Entry<T, P> entry) {
/* 44 */     this.entry = entry;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Single;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Single;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Single<TT;TP;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Single;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Single;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Single<TT;TP;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Single;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Single;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Single<TT;TP;>;
/*    */   }
/*    */   
/*    */   public CollectionCountsPredicate.Entry<T, P> entry() {
/* 44 */     return this.entry;
/*    */   }
/*    */   
/*    */   public boolean test(Iterable<? extends T> values) {
/* 47 */     return this.entry.test(values);
/*    */   }
/*    */   
/*    */   public List<CollectionCountsPredicate.Entry<T, P>> unpack() {
/* 52 */     return List.of(this.entry);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\CollectionCountsPredicate$Single.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */