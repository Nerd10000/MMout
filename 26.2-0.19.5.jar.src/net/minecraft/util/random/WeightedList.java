/*     */ package net.minecraft.util.random;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public final class WeightedList<E> {
/*     */   private static final int FLAT_THRESHOLD = 64;
/*     */   
/*     */   private final int totalWeight;
/*     */   
/*     */   private final List<Weighted<E>> items;
/*     */   
/*     */   private final Selector<E> selector;
/*     */   
/*     */   private WeightedList(List<? extends Weighted<E>> items) {
/*  28 */     this.items = List.copyOf(items);
/*  29 */     this.totalWeight = WeightedRandom.getTotalWeight(items, Weighted::weight);
/*  30 */     if (this.totalWeight == 0) {
/*  31 */       this.selector = null;
/*  32 */     } else if (this.totalWeight < 64) {
/*  33 */       this.selector = new Flat<>(this.items, this.totalWeight);
/*     */     } else {
/*  35 */       this.selector = new Compact<>(this.items);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <E> WeightedList<E> of() {
/*  40 */     return new WeightedList<>(List.of());
/*     */   }
/*     */   
/*     */   public static <E> WeightedList<E> of(E value) {
/*  44 */     return new WeightedList<>(List.of(new Weighted<>(value, 1)));
/*     */   }
/*     */   
/*     */   public static <E> WeightedList<E> of(E... items) {
/*  48 */     Builder<E> builder = builder();
/*  49 */     for (E item : items)
/*  50 */       builder.add(item); 
/*  52 */     return builder.build();
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public static <E> WeightedList<E> of(Weighted<E>... items) {
/*  57 */     return new WeightedList<>(List.of(items));
/*     */   }
/*     */   
/*     */   public static <E> WeightedList<E> of(List<Weighted<E>> items) {
/*  61 */     return new WeightedList<>(items);
/*     */   }
/*     */   
/*     */   public static <E> Builder<E> builder() {
/*  65 */     return new Builder<>();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  70 */     return (this.selector == null);
/*     */   }
/*     */   
/*     */   public <T> WeightedList<T> map(Function<E, T> mapper) {
/*  74 */     return new WeightedList(Lists.transform(this.items, e -> e.map(mapper)));
/*     */   }
/*     */   
/*     */   public Optional<E> getRandom(RandomSource random) {
/*  78 */     if (this.selector == null)
/*  79 */       return Optional.empty(); 
/*  81 */     int selection = random.nextInt(this.totalWeight);
/*  82 */     return Optional.of(this.selector.get(selection));
/*     */   }
/*     */   
/*     */   public E getRandomOrThrow(RandomSource random) {
/*  87 */     if (this.selector == null)
/*  88 */       throw new IllegalStateException("Weighted list has no elements"); 
/*  90 */     int selection = random.nextInt(this.totalWeight);
/*  91 */     return this.selector.get(selection);
/*     */   }
/*     */   
/*     */   public List<Weighted<E>> unwrap() {
/*  95 */     return this.items;
/*     */   }
/*     */   
/*     */   private static <E> Codec<WeightedList<E>> entryToListCodec(Codec<Weighted<E>> weightedElementCodec) {
/*  99 */     return weightedElementCodec.listOf().xmap(WeightedList::of, WeightedList::unwrap);
/*     */   }
/*     */   
/*     */   public static <E> Codec<WeightedList<E>> codec(Codec<E> elementCodec) {
/* 103 */     return entryToListCodec(Weighted.codec(elementCodec));
/*     */   }
/*     */   
/*     */   public static <E> Codec<WeightedList<E>> codec(MapCodec<E> elementCodec) {
/* 107 */     return entryToListCodec(Weighted.codec(elementCodec));
/*     */   }
/*     */   
/*     */   private static <E> Codec<WeightedList<E>> entryToNonEmptyListCodec(Codec<Weighted<E>> weightedElementCodec) {
/* 111 */     return entryToListCodec(weightedElementCodec)
/* 112 */       .validate(list -> list.isEmpty() ? DataResult.error(()) : DataResult.success(list));
/*     */   }
/*     */   
/*     */   public static <E> Codec<WeightedList<E>> nonEmptyCodec(Codec<E> elementCodec) {
/* 116 */     return entryToNonEmptyListCodec(Weighted.codec(elementCodec));
/*     */   }
/*     */   
/*     */   public static <E> Codec<WeightedList<E>> nonEmptyCodec(MapCodec<E> elementCodec) {
/* 120 */     return entryToNonEmptyListCodec(Weighted.codec(elementCodec));
/*     */   }
/*     */   
/*     */   public static <E, B extends io.netty.buffer.ByteBuf> StreamCodec<B, WeightedList<E>> streamCodec(StreamCodec<B, E> elementCodec) {
/* 124 */     return Weighted.<B, T>streamCodec(elementCodec).apply(ByteBufCodecs.list()).map(WeightedList::of, WeightedList::unwrap);
/*     */   }
/*     */   
/*     */   public boolean contains(E value) {
/* 128 */     for (Weighted<E> item : this.items) {
/* 129 */       if (item.value().equals(value))
/* 130 */         return true; 
/*     */     } 
/* 133 */     return false;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 138 */     if (this == obj)
/* 139 */       return true; 
/* 141 */     if (obj instanceof WeightedList) {
/* 141 */       WeightedList<?> list = (WeightedList)obj;
/* 142 */       return (this.totalWeight == list.totalWeight && Objects.equals(this.items, list.items));
/*     */     } 
/* 144 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 149 */     int result = this.totalWeight;
/* 150 */     result = 31 * result + this.items.hashCode();
/* 151 */     return result;
/*     */   }
/*     */   
/*     */   public static class Builder<E> {
/* 162 */     private final ImmutableList.Builder<Weighted<E>> result = ImmutableList.builder();
/*     */     
/*     */     public Builder<E> add(E item) {
/* 165 */       return add(item, 1);
/*     */     }
/*     */     
/*     */     public Builder<E> add(E item, int weight) {
/* 169 */       this.result.add(new Weighted<>(item, weight));
/* 170 */       return this;
/*     */     }
/*     */     
/*     */     public WeightedList<E> build() {
/* 174 */       return new WeightedList<>((List<? extends Weighted<E>>)this.result.build());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Flat<E> implements Selector<E> {
/*     */     private final Object[] entries;
/*     */     
/*     */     private Flat(List<Weighted<E>> entries, int totalWeight) {
/* 182 */       this.entries = new Object[totalWeight];
/* 183 */       int i = 0;
/* 184 */       for (Weighted<E> entry : entries) {
/* 185 */         int weight = entry.weight();
/* 186 */         Arrays.fill(this.entries, i, i + weight, entry.value());
/* 187 */         i += weight;
/*     */       } 
/*     */     }
/*     */     
/*     */     public E get(int selection) {
/* 194 */       return (E)this.entries[selection];
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Compact<E> implements Selector<E> {
/*     */     private final Weighted<?>[] entries;
/*     */     
/*     */     private Compact(List<Weighted<E>> entries) {
/* 202 */       this.entries = (Weighted<?>[])entries.toArray(x$0 -> new Weighted[x$0]);
/*     */     }
/*     */     
/*     */     public E get(int selection) {
/* 208 */       for (Weighted<?> entry : this.entries) {
/* 209 */         selection -= entry.weight();
/* 210 */         if (selection < 0)
/* 211 */           return (E)entry.value(); 
/*     */       } 
/* 214 */       throw new IllegalStateException("" + selection + " exceeded total weight");
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface Selector<E> {
/*     */     E get(int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\random\WeightedList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */