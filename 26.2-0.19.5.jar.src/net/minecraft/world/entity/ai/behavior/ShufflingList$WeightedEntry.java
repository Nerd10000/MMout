/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Objects;
/*     */ 
/*     */ public class WeightedEntry<T> {
/*     */   private final T data;
/*     */   
/*     */   private final int weight;
/*     */   
/*     */   private double randWeight;
/*     */   
/*     */   private WeightedEntry(T data, int weight) {
/*  63 */     this.weight = weight;
/*  64 */     this.data = data;
/*     */   }
/*     */   
/*     */   private double getRandWeight() {
/*  68 */     return this.randWeight;
/*     */   }
/*     */   
/*     */   private void setRandom(float random) {
/*  72 */     this.randWeight = -Math.pow(random, (1.0F / this.weight));
/*     */   }
/*     */   
/*     */   public T getData() {
/*  76 */     return this.data;
/*     */   }
/*     */   
/*     */   public int getWeight() {
/*  80 */     return this.weight;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  85 */     return "" + this.weight + ":" + this.weight;
/*     */   }
/*     */   
/*     */   public static <E> Codec<WeightedEntry<E>> codec(final Codec<E> elementCodec) {
/*  89 */     return new Codec<WeightedEntry<E>>() {
/*     */         public <T> DataResult<Pair<ShufflingList.WeightedEntry<E>, T>> decode(DynamicOps<T> ops, T input) {
/*  92 */           Dynamic<T> map = new Dynamic(ops, input);
/*  94 */           Objects.requireNonNull(elementCodec);
/*  94 */           return map.get("data").flatMap(elementCodec::parse)
/*  95 */             .map(data -> new ShufflingList.WeightedEntry(data, map.get("weight").asInt(1)))
/*  96 */             .map(r -> Pair.of(r, ops.empty()));
/*     */         }
/*     */         
/*     */         public <T> DataResult<T> encode(ShufflingList.WeightedEntry<E> input, DynamicOps<T> ops, T prefix) {
/* 101 */           return ops.mapBuilder()
/* 102 */             .add("weight", ops.createInt(input.weight))
/* 103 */             .add("data", elementCodec.encodeStart(ops, input.data))
/* 104 */             .build(prefix);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\ShufflingList$WeightedEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */