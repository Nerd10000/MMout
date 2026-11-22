/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ public final class CollectionPredicate<T, P extends Predicate<T>> extends Record implements Predicate<Iterable<? extends T>> {
/*    */   private final Optional<CollectionContentsPredicate<T, P>> contains;
/*    */   
/*    */   private final Optional<CollectionCountsPredicate<T, P>> counts;
/*    */   
/*    */   private final Optional<MinMaxBounds.Ints> size;
/*    */   
/*    */   public CollectionPredicate(Optional<CollectionContentsPredicate<T, P>> contains, Optional<CollectionCountsPredicate<T, P>> counts, Optional<MinMaxBounds.Ints> size) {
/* 10 */     this.contains = contains;
/* 10 */     this.counts = counts;
/* 10 */     this.size = size;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/CollectionPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionPredicate;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionPredicate<TT;TP;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/CollectionPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionPredicate;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/CollectionPredicate<TT;TP;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/CollectionPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/CollectionPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/CollectionPredicate<TT;TP;>;
/*    */   }
/*    */   
/*    */   public Optional<CollectionContentsPredicate<T, P>> contains() {
/* 10 */     return this.contains;
/*    */   }
/*    */   
/*    */   public Optional<CollectionCountsPredicate<T, P>> counts() {
/* 10 */     return this.counts;
/*    */   }
/*    */   
/*    */   public Optional<MinMaxBounds.Ints> size() {
/* 10 */     return this.size;
/*    */   }
/*    */   
/*    */   public static <T, P extends Predicate<T>> Codec<CollectionPredicate<T, P>> codec(Codec<P> elementCodec) {
/* 16 */     return RecordCodecBuilder.create(i -> i.group((App)CollectionContentsPredicate.<T, Predicate<T>>codec(elementCodec).optionalFieldOf("contains").forGetter(CollectionPredicate::contains), (App)CollectionCountsPredicate.<T, Predicate<T>>codec(elementCodec).optionalFieldOf("count").forGetter(CollectionPredicate::counts), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("size").forGetter(CollectionPredicate::size)).apply((Applicative)i, CollectionPredicate::new));
/*    */   }
/*    */   
/*    */   public boolean test(Iterable<? extends T> value) {
/* 25 */     if (this.contains.isPresent() && !((CollectionContentsPredicate)this.contains.get()).test(value))
/* 26 */       return false; 
/* 29 */     if (this.counts.isPresent() && !((CollectionCountsPredicate)this.counts.get()).test(value))
/* 30 */       return false; 
/* 33 */     if (this.size.isPresent() && !((MinMaxBounds.Ints)this.size.get()).matches(Iterables.size(value)))
/* 34 */       return false; 
/* 37 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\CollectionPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */