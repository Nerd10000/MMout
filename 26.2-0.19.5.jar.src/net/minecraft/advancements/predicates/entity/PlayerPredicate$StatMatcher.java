/*     */ package net.minecraft.advancements.predicates.entity;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.stats.Stat;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.stats.StatsCounter;
/*     */ 
/*     */ final class StatMatcher<T> extends Record {
/*     */   private final StatType<T> type;
/*     */   
/*     */   private final Holder<T> value;
/*     */   
/*     */   private final MinMaxBounds.Ints range;
/*     */   
/*     */   private final Supplier<Stat<T>> stat;
/*     */   
/*     */   private StatMatcher(StatType<T> type, Holder<T> value, MinMaxBounds.Ints range, Supplier<Stat<T>> stat) {
/* 175 */     this.type = type;
/* 175 */     this.value = value;
/* 175 */     this.range = range;
/* 175 */     this.stat = stat;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #175	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #175	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #175	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/PlayerPredicate$StatMatcher<TT;>;
/*     */   }
/*     */   
/*     */   public StatType<T> type() {
/* 175 */     return this.type;
/*     */   }
/*     */   
/*     */   public Holder<T> value() {
/* 175 */     return this.value;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.Ints range() {
/* 175 */     return this.range;
/*     */   }
/*     */   
/*     */   public Supplier<Stat<T>> stat() {
/* 175 */     return this.stat;
/*     */   }
/*     */   
/* 176 */   public static final Codec<StatMatcher<?>> CODEC = BuiltInRegistries.STAT_TYPE.byNameCodec().dispatch(StatMatcher::type, StatMatcher::createTypedCodec);
/*     */   
/*     */   private static <T> MapCodec<StatMatcher<T>> createTypedCodec(StatType<T> type) {
/* 179 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)type.getRegistry().holderByNameCodec().fieldOf("stat").forGetter(StatMatcher::value), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("value", MinMaxBounds.Ints.ANY).forGetter(StatMatcher::range)).apply((Applicative)i, ()));
/*     */   }
/*     */   
/*     */   public StatMatcher(StatType<T> type, Holder<T> value, MinMaxBounds.Ints range) {
/* 186 */     this(type, value, range, (Supplier<Stat<T>>)Suppliers.memoize(() -> type.get(value.value())));
/*     */   }
/*     */   
/*     */   public boolean matches(StatsCounter counter) {
/* 190 */     return this.range.matches(counter.getValue(this.stat.get()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\PlayerPredicate$StatMatcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */