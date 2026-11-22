/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ public final class Entry<T, P extends Predicate<T>> extends Record {
/*    */   private final P test;
/*    */   
/*    */   private final MinMaxBounds.Ints count;
/*    */   
/*    */   public Entry(P test, MinMaxBounds.Ints count) {
/* 73 */     this.test = test;
/* 73 */     this.count = count;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #73	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Entry;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Entry<TT;TP;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #73	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Entry;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Entry<TT;TP;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #73	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/CollectionCountsPredicate$Entry<TT;TP;>;
/*    */   }
/*    */   
/*    */   public P test() {
/* 73 */     return this.test;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints count() {
/* 73 */     return this.count;
/*    */   }
/*    */   
/*    */   public static <T, P extends Predicate<T>> Codec<Entry<T, P>> codec(Codec<P> elementCodec) {
/* 75 */     return RecordCodecBuilder.create(i -> i.group((App)elementCodec.fieldOf("test").forGetter(Entry::test), (App)MinMaxBounds.Ints.CODEC.fieldOf("count").forGetter(Entry::count)).apply((Applicative)i, Entry::new));
/*    */   }
/*    */   
/*    */   public boolean test(Iterable<? extends T> values) {
/* 82 */     int count = 0;
/* 83 */     for (T value : values) {
/* 84 */       if (this.test.test(value))
/* 85 */         count++; 
/*    */     } 
/* 88 */     return this.count.matches(count);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\CollectionCountsPredicate$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */