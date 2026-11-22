/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ class null extends MapCodec<V> {
/*     */   public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 739 */     return Stream.of((T[])new Object[] { ops.createString(this.val$typeKey), ops.createString(this.val$valueKey) });
/*     */   }
/*     */   
/*     */   public <T> DataResult<V> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 744 */     T typeName = (T)input.get(typeKey);
/* 745 */     if (typeName == null)
/* 746 */       return DataResult.error(() -> "Missing \"" + typeKey + "\" in: " + String.valueOf(input)); 
/* 748 */     return typeCodec.decode(ops, typeName).flatMap(type -> {
/*     */           Objects.requireNonNull(ops);
/*     */           T value = Objects.requireNonNullElseGet((T)input.get(valueKey), ops::emptyMap);
/*     */           return ((Codec)valueCodec.apply(type.getFirst())).decode(ops, value).map(Pair::getFirst);
/*     */         });
/*     */   }
/*     */   
/*     */   public <T> RecordBuilder<T> encode(V input, DynamicOps<T> ops, RecordBuilder<T> builder) {
/* 756 */     K type = typeGetter.apply(input);
/* 757 */     builder.add(typeKey, typeCodec.encodeStart(ops, type));
/* 758 */     DataResult<T> parameters = encode(valueCodec.apply(type), input, ops);
/* 759 */     if (parameters.result().isEmpty() || !Objects.equals(parameters.result().get(), ops.emptyMap()))
/* 760 */       builder.add(valueKey, parameters); 
/* 762 */     return builder;
/*     */   }
/*     */   
/*     */   private <T, V2 extends V> DataResult<T> encode(Codec<V2> codec, V input, DynamicOps<T> ops) {
/* 767 */     return codec.encodeStart(ops, input);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ExtraCodecs$6.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */