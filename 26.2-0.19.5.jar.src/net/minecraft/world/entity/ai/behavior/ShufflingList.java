/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class ShufflingList<U> implements Iterable<U> {
/*     */   protected final List<WeightedEntry<U>> entries;
/*     */   
/*  23 */   private final RandomSource random = RandomSource.create();
/*     */   
/*     */   public ShufflingList() {
/*  26 */     this.entries = Lists.newArrayList();
/*     */   }
/*     */   
/*     */   private ShufflingList(List<WeightedEntry<U>> entries) {
/*  30 */     this.entries = Lists.newArrayList(entries);
/*     */   }
/*     */   
/*     */   public static <U> Codec<ShufflingList<U>> codec(Codec<U> elementCodec) {
/*  34 */     return WeightedEntry.<E>codec(elementCodec).listOf().xmap(ShufflingList::new, l -> l.entries);
/*     */   }
/*     */   
/*     */   public ShufflingList<U> add(U data, int weight) {
/*  38 */     this.entries.add(new WeightedEntry<>(data, weight));
/*  39 */     return this;
/*     */   }
/*     */   
/*     */   public ShufflingList<U> shuffle() {
/*  43 */     this.entries.forEach(k -> k.setRandom(this.random.nextFloat()));
/*  44 */     this.entries.sort(Comparator.comparingDouble(WeightedEntry::getRandWeight));
/*  45 */     return this;
/*     */   }
/*     */   
/*     */   public Stream<U> stream() {
/*  49 */     return this.entries.stream().map(WeightedEntry::getData);
/*     */   }
/*     */   
/*     */   public Iterator<U> iterator() {
/*  54 */     return Iterators.transform(this.entries.iterator(), WeightedEntry::getData);
/*     */   }
/*     */   
/*     */   public static class WeightedEntry<T> {
/*     */     private final T data;
/*     */     
/*     */     private final int weight;
/*     */     
/*     */     private double randWeight;
/*     */     
/*     */     private WeightedEntry(T data, int weight) {
/*  63 */       this.weight = weight;
/*  64 */       this.data = data;
/*     */     }
/*     */     
/*     */     private double getRandWeight() {
/*  68 */       return this.randWeight;
/*     */     }
/*     */     
/*     */     private void setRandom(float random) {
/*  72 */       this.randWeight = -Math.pow(random, (1.0F / this.weight));
/*     */     }
/*     */     
/*     */     public T getData() {
/*  76 */       return this.data;
/*     */     }
/*     */     
/*     */     public int getWeight() {
/*  80 */       return this.weight;
/*     */     }
/*     */     
/*     */     public String toString() {
/*  85 */       return "" + this.weight + ":" + this.weight;
/*     */     }
/*     */     
/*     */     public static <E> Codec<WeightedEntry<E>> codec(final Codec<E> elementCodec) {
/*  89 */       return new Codec<WeightedEntry<E>>() {
/*     */           public <T> DataResult<Pair<ShufflingList.WeightedEntry<E>, T>> decode(DynamicOps<T> ops, T input) {
/*  92 */             Dynamic<T> map = new Dynamic(ops, input);
/*  94 */             Objects.requireNonNull(elementCodec);
/*  94 */             return map.get("data").flatMap(elementCodec::parse)
/*  95 */               .map(data -> new ShufflingList.WeightedEntry(data, map.get("weight").asInt(1)))
/*  96 */               .map(r -> Pair.of(r, ops.empty()));
/*     */           }
/*     */           
/*     */           public <T> DataResult<T> encode(ShufflingList.WeightedEntry<E> input, DynamicOps<T> ops, T prefix) {
/* 101 */             return ops.mapBuilder()
/* 102 */               .add("weight", ops.createInt(input.weight))
/* 103 */               .add("data", elementCodec.encodeStart(ops, input.data))
/* 104 */               .build(prefix);
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements Codec<WeightedEntry<E>> {
/*     */     public <T> DataResult<Pair<ShufflingList.WeightedEntry<E>, T>> decode(DynamicOps<T> ops, T input) {
/*     */       Dynamic<T> map = new Dynamic(ops, input);
/*     */       Objects.requireNonNull(elementCodec);
/*     */       return map.get("data").flatMap(elementCodec::parse).map(data -> new ShufflingList.WeightedEntry(data, map.get("weight").asInt(1))).map(r -> Pair.of(r, ops.empty()));
/*     */     }
/*     */     
/*     */     public <T> DataResult<T> encode(ShufflingList.WeightedEntry<E> input, DynamicOps<T> ops, T prefix) {
/* 104 */       return ops.mapBuilder().add("weight", ops.createInt(input.weight)).add("data", elementCodec.encodeStart(ops, input.data)).build(prefix);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 112 */     return "ShufflingList[" + String.valueOf(this.entries) + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\ShufflingList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */