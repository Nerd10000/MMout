/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import java.util.Optional;
/*     */ 
/*     */ class null implements Codec<Optional<A>> {
/*     */   public <T> DataResult<Pair<Optional<A>, T>> decode(DynamicOps<T> ops, T input) {
/* 776 */     if (isEmptyMap(ops, input))
/* 777 */       return DataResult.success(Pair.of(Optional.empty(), input)); 
/* 779 */     return codec.decode(ops, input).map(pair -> pair.mapFirst(Optional::of));
/*     */   }
/*     */   
/*     */   private static <T> boolean isEmptyMap(DynamicOps<T> ops, T input) {
/* 783 */     Optional<MapLike<T>> map = ops.getMap(input).result();
/* 784 */     return (map.isPresent() && ((MapLike)map.get()).entries().findAny().isEmpty());
/*     */   }
/*     */   
/*     */   public <T> DataResult<T> encode(Optional<A> input, DynamicOps<T> ops, T prefix) {
/* 789 */     if (input.isEmpty())
/* 790 */       return DataResult.success(ops.emptyMap()); 
/* 792 */     return codec.encode(input.get(), ops, prefix);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ExtraCodecs$7.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */