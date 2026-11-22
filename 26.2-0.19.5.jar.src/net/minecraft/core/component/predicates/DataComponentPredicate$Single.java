/*     */ package net.minecraft.core.component.predicates;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class Single<T extends DataComponentPredicate> extends Record {
/*     */   private final DataComponentPredicate.Type<T> type;
/*     */   
/*     */   private final T predicate;
/*     */   
/*     */   public Single(DataComponentPredicate.Type<T> type, T predicate) {
/* 125 */     this.type = type;
/* 125 */     this.predicate = predicate;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #125	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #125	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #125	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/DataComponentPredicate$Single<TT;>;
/*     */   }
/*     */   
/*     */   public DataComponentPredicate.Type<T> type() {
/* 125 */     return this.type;
/*     */   }
/*     */   
/*     */   public T predicate() {
/* 125 */     return this.predicate;
/*     */   }
/*     */   
/*     */   private static <T extends DataComponentPredicate> MapCodec<Single<T>> wrapCodec(DataComponentPredicate.Type<T> type, Codec<T> codec) {
/* 130 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)codec.fieldOf("value").forGetter(Single::predicate)).apply((Applicative)i, ()));
/*     */   }
/*     */   
/*     */   private static <T extends DataComponentPredicate> Single<T> fromEntry(Map.Entry<DataComponentPredicate.Type<?>, T> e) {
/* 137 */     return new Single<>((DataComponentPredicate.Type<T>)e.getKey(), e.getValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\DataComponentPredicate$Single.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */