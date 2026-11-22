/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Objects;
/*     */ 
/*     */ class null implements Codec<ShufflingList.WeightedEntry<E>> {
/*     */   public <T> DataResult<Pair<ShufflingList.WeightedEntry<E>, T>> decode(DynamicOps<T> ops, T input) {
/*  92 */     Dynamic<T> map = new Dynamic(ops, input);
/*  94 */     Objects.requireNonNull(elementCodec);
/*  94 */     return map.get("data").flatMap(elementCodec::parse)
/*  95 */       .map(data -> new ShufflingList.WeightedEntry(data, map.get("weight").asInt(1)))
/*  96 */       .map(r -> Pair.of(r, ops.empty()));
/*     */   }
/*     */   
/*     */   public <T> DataResult<T> encode(ShufflingList.WeightedEntry<E> input, DynamicOps<T> ops, T prefix) {
/* 101 */     return ops.mapBuilder()
/* 102 */       .add("weight", ops.createInt(input.weight))
/* 103 */       .add("data", elementCodec.encodeStart(ops, input.data))
/* 104 */       .build(prefix);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\ShufflingList$WeightedEntry$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */