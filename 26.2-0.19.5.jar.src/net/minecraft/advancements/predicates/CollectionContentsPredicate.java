/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ public interface CollectionContentsPredicate<T, P extends Predicate<T>> extends Predicate<Iterable<? extends T>> {
/*    */   List<P> unpack();
/*    */   
/*    */   static <T, P extends Predicate<T>> Codec<CollectionContentsPredicate<T, P>> codec(Codec<P> elementCodec) {
/* 13 */     return elementCodec.listOf().xmap(CollectionContentsPredicate::of, CollectionContentsPredicate::unpack);
/*    */   }
/*    */   
/*    */   @SafeVarargs
/*    */   static <T, P extends Predicate<T>> CollectionContentsPredicate<T, P> of(P... predicates) {
/* 21 */     return of(List.of(predicates));
/*    */   }
/*    */   
/*    */   static <T, P extends Predicate<T>> CollectionContentsPredicate<T, P> of(List<P> predicates) {
/* 25 */     switch (predicates.size()) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */     } 
/* 25 */     return 
/*    */       
/* 28 */       new Multiple<>(predicates);
/*    */   }
/*    */   
/*    */   public static class Zero<T, P extends Predicate<T>> implements CollectionContentsPredicate<T, P> {
/*    */     public boolean test(Iterable<? extends T> values) {
/* 35 */       return true;
/*    */     }
/*    */     
/*    */     public List<P> unpack() {
/* 40 */       return List.of();
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Single<T, P extends Predicate<T>> extends Record implements CollectionContentsPredicate<T, P> {
/*    */     private final P test;
/*    */     
/*    */     public Single(P test) {
/* 44 */       this.test = test;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Single;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Single;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Single<TT;TP;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Single;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Single;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Single<TT;TP;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Single;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #44	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Single;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Single<TT;TP;>;
/*    */     }
/*    */     
/*    */     public P test() {
/* 44 */       return this.test;
/*    */     }
/*    */     
/*    */     public boolean test(Iterable<? extends T> values) {
/* 47 */       for (T value : values) {
/* 48 */         if (this.test.test(value))
/* 49 */           return true; 
/*    */       } 
/* 52 */       return false;
/*    */     }
/*    */     
/*    */     public List<P> unpack() {
/* 57 */       return List.of(this.test);
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Multiple<T, P extends Predicate<T>> extends Record implements CollectionContentsPredicate<T, P> {
/*    */     private final List<P> tests;
/*    */     
/*    */     public Multiple(List<P> tests) {
/* 61 */       this.tests = tests;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple<TT;TP;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple<TT;TP;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/CollectionContentsPredicate$Multiple<TT;TP;>;
/*    */     }
/*    */     
/*    */     public List<P> tests() {
/* 61 */       return this.tests;
/*    */     }
/*    */     
/*    */     public boolean test(Iterable<? extends T> values) {
/* 64 */       List<Predicate<T>> testsToMatch = new ArrayList<>(this.tests);
/* 65 */       for (T value : values) {
/* 66 */         testsToMatch.removeIf(p -> p.test(value));
/* 67 */         if (testsToMatch.isEmpty())
/* 68 */           return true; 
/*    */       } 
/* 72 */       return false;
/*    */     }
/*    */     
/*    */     public List<P> unpack() {
/* 77 */       return this.tests;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\CollectionContentsPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */