/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.HashBiMap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.JavaOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import com.mojang.serialization.codecs.BaseMapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.floats.FloatArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Instant;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.temporal.TemporalAccessor;
/*     */ import java.util.Arrays;
/*     */ import java.util.Base64;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HexFormat;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalLong;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ import org.apache.commons.lang3.StringEscapeUtils;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ import org.joml.AxisAngle4f;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector2f;
/*     */ import org.joml.Vector2fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector3i;
/*     */ import org.joml.Vector3ic;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ 
/*     */ public class ExtraCodecs {
/*     */   public static <T> Codec<T> converter(DynamicOps<T> ops) {
/*  88 */     return Codec.PASSTHROUGH.xmap(t -> t.convert(ops).getValue(), t -> new Dynamic(ops, t));
/*     */   }
/*     */   
/*  94 */   public static final Codec<JsonElement> JSON = converter((DynamicOps<JsonElement>)JsonOps.INSTANCE);
/*     */   
/*  95 */   public static final Codec<Object> JAVA = converter((DynamicOps)JavaOps.INSTANCE);
/*     */   
/*  96 */   public static final Codec<Tag> NBT = converter((DynamicOps<Tag>)NbtOps.INSTANCE);
/*     */   
/*     */   public static final Codec<Vector2fc> VECTOR2F;
/*     */   
/*     */   public static final Codec<Vector3fc> VECTOR3F;
/*     */   
/*     */   public static final Codec<Vector3ic> VECTOR3I;
/*     */   
/*     */   public static final Codec<Vector4fc> VECTOR4F;
/*     */   
/*     */   public static final Codec<Quaternionfc> QUATERNIONF_COMPONENTS;
/*     */   
/*     */   public static final Codec<AxisAngle4f> AXISANGLE4F;
/*     */   
/*     */   static {
/*  98 */     VECTOR2F = Codec.FLOAT.listOf().comapFlatMap(input -> Util.<T>fixedSize(input, 2).map(()), vec -> List.of(Float.valueOf(vec.x()), Float.valueOf(vec.y())));
/* 103 */     VECTOR3F = Codec.FLOAT.listOf().comapFlatMap(input -> Util.<T>fixedSize(input, 3).map(()), vec -> List.of(Float.valueOf(vec.x()), Float.valueOf(vec.y()), Float.valueOf(vec.z())));
/* 107 */     VECTOR3I = Codec.INT.listOf().comapFlatMap(input -> Util.<T>fixedSize(input, 3).map(()), vec -> List.of(Integer.valueOf(vec.x()), Integer.valueOf(vec.y()), Integer.valueOf(vec.z())));
/* 112 */     VECTOR4F = Codec.FLOAT.listOf().comapFlatMap(input -> Util.<T>fixedSize(input, 4).map(()), vec -> List.of(Float.valueOf(vec.x()), Float.valueOf(vec.y()), Float.valueOf(vec.z()), Float.valueOf(vec.w())));
/* 117 */     QUATERNIONF_COMPONENTS = Codec.FLOAT.listOf().comapFlatMap(input -> Util.<T>fixedSize(input, 4).map(()), q -> List.of(Float.valueOf(q.x()), Float.valueOf(q.y()), Float.valueOf(q.z()), Float.valueOf(q.w())));
/* 122 */     AXISANGLE4F = RecordCodecBuilder.create(i -> i.group((App)Codec.FLOAT.fieldOf("angle").forGetter(()), (App)VECTOR3F.fieldOf("axis").forGetter(())).apply((Applicative)i, AxisAngle4f::new));
/*     */   }
/*     */   
/* 127 */   public static final Codec<Quaternionfc> QUATERNIONF = Codec.withAlternative(QUATERNIONF_COMPONENTS, AXISANGLE4F
/*     */       
/* 129 */       .xmap(Quaternionf::new, AxisAngle4f::new));
/*     */   
/*     */   public static final Codec<Matrix4fc> MATRIX4F;
/*     */   
/*     */   private static final String HEX_COLOR_PREFIX = "#";
/*     */   
/*     */   public static final Codec<Integer> RGB_COLOR_CODEC;
/*     */   
/*     */   public static final Codec<Integer> ARGB_COLOR_CODEC;
/*     */   
/*     */   static {
/* 132 */     MATRIX4F = Codec.FLOAT.listOf().comapFlatMap(input -> Util.<T>fixedSize(input, 16).map(()), m -> {
/*     */           FloatArrayList floatArrayList = new FloatArrayList(16);
/*     */           for (int i = 0; i < 16; i++)
/*     */             floatArrayList.add(m.getRowColumn(i >> 2, i & 0x3)); 
/*     */           return (List)floatArrayList;
/*     */         });
/* 177 */     RGB_COLOR_CODEC = Codec.withAlternative((Codec)Codec.INT, VECTOR3F, v -> Integer.valueOf(ARGB.colorFromFloat(1.0F, v.x(), v.y(), v.z())));
/* 181 */     ARGB_COLOR_CODEC = Codec.withAlternative((Codec)Codec.INT, VECTOR4F, v -> Integer.valueOf(ARGB.colorFromFloat(v.w(), v.x(), v.y(), v.z())));
/*     */   }
/*     */   
/*     */   private static Codec<Integer> hexColor(int expectedDigits) {
/*     */     long maxValue = (1L << expectedDigits * 4) - 1L;
/*     */     return Codec.STRING.comapFlatMap(string -> {
/*     */           if (!string.startsWith("#"))
/*     */             return DataResult.error(()); 
/*     */           int digits = string.length() - "#".length();
/*     */           if (digits != expectedDigits)
/*     */             return DataResult.error(()); 
/*     */           try {
/*     */             long value = HexFormat.fromHexDigitsToLong(string, "#".length(), string.length());
/*     */             return (value < 0L || value > maxValue) ? DataResult.error(()) : DataResult.success(Integer.valueOf((int)value));
/*     */           } catch (NumberFormatException e) {
/*     */             return DataResult.error(());
/*     */           } 
/*     */         }value -> "#" + HexFormat.of().toHexDigits(value.intValue(), expectedDigits));
/*     */   }
/*     */   
/* 187 */   public static final Codec<Integer> STRING_RGB_COLOR = Codec.withAlternative(
/* 188 */       hexColor(6).xmap(ARGB::opaque, ARGB::transparent), RGB_COLOR_CODEC);
/*     */   
/* 191 */   public static final Codec<Integer> STRING_ARGB_COLOR = Codec.withAlternative(
/* 192 */       hexColor(8), ARGB_COLOR_CODEC);
/*     */   
/*     */   public static final Codec<Integer> UNSIGNED_BYTE;
/*     */   
/*     */   public static final Codec<Integer> NON_NEGATIVE_INT;
/*     */   
/*     */   public static final Codec<Integer> POSITIVE_INT;
/*     */   
/*     */   public static final Codec<Long> NON_NEGATIVE_LONG;
/*     */   
/*     */   public static final Codec<Long> POSITIVE_LONG;
/*     */   
/*     */   public static final Codec<Float> NON_NEGATIVE_FLOAT;
/*     */   
/*     */   public static final Codec<Float> POSITIVE_FLOAT;
/*     */   
/*     */   public static final Codec<Pattern> PATTERN;
/*     */   
/*     */   static {
/* 196 */     UNSIGNED_BYTE = Codec.BYTE.flatComapMap(UnsignedBytes::toInt, integer -> (integer.intValue() > 255) ? DataResult.error(()) : DataResult.success(Byte.valueOf(integer.byteValue())));
/* 443 */     NON_NEGATIVE_INT = intRangeWithMessage(0, 2147483647, n -> "Value must be non-negative: " + n);
/* 444 */     POSITIVE_INT = intRangeWithMessage(1, 2147483647, n -> "Value must be positive: " + n);
/* 459 */     NON_NEGATIVE_LONG = longRangeWithMessage(0L, Long.MAX_VALUE, n -> "Value must be non-negative: " + n);
/* 460 */     POSITIVE_LONG = longRangeWithMessage(1L, Long.MAX_VALUE, n -> "Value must be positive: " + n);
/* 484 */     NON_NEGATIVE_FLOAT = floatRangeMinInclusiveWithMessage(0.0F, Float.MAX_VALUE, n -> "Value must be non-negative: " + n);
/* 485 */     POSITIVE_FLOAT = floatRangeMinExclusiveWithMessage(0.0F, Float.MAX_VALUE, n -> "Value must be positive: " + n);
/* 552 */     PATTERN = Codec.STRING.comapFlatMap(pattern -> {
/*     */           try {
/*     */             return DataResult.success(Pattern.compile(pattern));
/* 555 */           } catch (PatternSyntaxException e) {
/*     */             return DataResult.error(());
/*     */           } 
/*     */         }Pattern::pattern);
/*     */   }
/*     */   
/*     */   public static <P, I> Codec<I> intervalCodec(Codec<P> pointCodec, String lowerBoundName, String upperBoundName, BiFunction<P, P, DataResult<I>> makeInterval, Function<I, P> getMin, Function<I, P> getMax) {
/*     */     Codec<I> arrayCodec = Codec.list(pointCodec).comapFlatMap(list -> Util.<T>fixedSize(list, 2).flatMap(()), p -> ImmutableList.of(getMin.apply(p), getMax.apply(p)));
/*     */     Codec<I> objectCodec = RecordCodecBuilder.create(i -> i.group((App)pointCodec.fieldOf(lowerBoundName).forGetter(Pair::getFirst), (App)pointCodec.fieldOf(upperBoundName).forGetter(Pair::getSecond)).apply((Applicative)i, Pair::of)).comapFlatMap(p -> (DataResult)makeInterval.apply(p.getFirst(), p.getSecond()), i -> Pair.of(getMin.apply(i), getMax.apply(i)));
/*     */     Codec<I> arrayOrObjectCodec = Codec.withAlternative(arrayCodec, objectCodec);
/*     */     return Codec.either(pointCodec, arrayOrObjectCodec).comapFlatMap(either -> (DataResult)either.map((), DataResult::success), p -> {
/*     */           P min = getMin.apply(p);
/*     */           P max = getMax.apply(p);
/*     */           return Objects.equals(min, max) ? Either.left(min) : Either.right(p);
/*     */         });
/*     */   }
/*     */   
/*     */   public static <A> Codec.ResultFunction<A> orElsePartial(final A value) {
/*     */     return new Codec.ResultFunction<A>() {
/*     */         public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<A, T>> a) {
/*     */           MutableObject<String> message = new MutableObject();
/*     */           Objects.requireNonNull(message);
/*     */           Optional<Pair<A, T>> result = a.resultOrPartial(message::setValue);
/*     */           if (result.isPresent())
/*     */             return a; 
/*     */           return DataResult.error(() -> "(" + (String)message.get() + " -> using default)", Pair.of(value, input));
/*     */         }
/*     */         
/*     */         public <T> DataResult<T> coApply(DynamicOps<T> ops, A input, DataResult<T> t) {
/*     */           return t;
/*     */         }
/*     */         
/*     */         public String toString() {
/*     */           return "OrElsePartial[" + String.valueOf(value) + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <E> Codec<E> idResolverCodec(ToIntFunction<E> toInt, IntFunction<E> fromInt, int unknownId) {
/*     */     return Codec.INT.flatXmap(id -> (DataResult)Optional.ofNullable(fromInt.apply(id.intValue())).map(DataResult::success).orElseGet(()), e -> {
/*     */           int id = toInt.applyAsInt(e);
/*     */           return (id == unknownId) ? DataResult.error(()) : DataResult.success(Integer.valueOf(id));
/*     */         });
/*     */   }
/*     */   
/*     */   public static <I, E> Codec<E> idResolverCodec(Codec<I> value, Function<I, E> fromId, Function<E, I> toId) {
/*     */     return value.flatXmap(id -> {
/*     */           E element = fromId.apply(id);
/*     */           return (element == null) ? DataResult.error(()) : DataResult.success(element);
/*     */         }e -> {
/*     */           I id = toId.apply(e);
/*     */           return (id == null) ? DataResult.error(()) : DataResult.success(id);
/*     */         });
/*     */   }
/*     */   
/*     */   public static <E> Codec<E> orCompressed(final Codec<E> normal, final Codec<E> compressed) {
/*     */     return new Codec<E>() {
/*     */         public <T> DataResult<T> encode(E input, DynamicOps<T> ops, T prefix) {
/*     */           if (ops.compressMaps())
/*     */             return compressed.encode(input, ops, prefix); 
/*     */           return normal.encode(input, ops, prefix);
/*     */         }
/*     */         
/*     */         public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
/*     */           if (ops.compressMaps())
/*     */             return compressed.decode(ops, input); 
/*     */           return normal.decode(ops, input);
/*     */         }
/*     */         
/*     */         public String toString() {
/*     */           return String.valueOf(normal) + " orCompressed " + String.valueOf(normal);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <E> MapCodec<E> orCompressed(final MapCodec<E> normal, final MapCodec<E> compressed) {
/*     */     return new MapCodec<E>() {
/*     */         public <T> RecordBuilder<T> encode(E input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/*     */           if (ops.compressMaps())
/*     */             return compressed.encode(input, ops, prefix); 
/*     */           return normal.encode(input, ops, prefix);
/*     */         }
/*     */         
/*     */         public <T> DataResult<E> decode(DynamicOps<T> ops, MapLike<T> input) {
/*     */           if (ops.compressMaps())
/*     */             return compressed.decode(ops, input); 
/*     */           return normal.decode(ops, input);
/*     */         }
/*     */         
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/*     */           return compressed.keys(ops);
/*     */         }
/*     */         
/*     */         public String toString() {
/*     */           return String.valueOf(normal) + " orCompressed " + String.valueOf(normal);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <E> Codec<E> overrideLifecycle(Codec<E> codec, final Function<E, Lifecycle> decodeLifecycle, final Function<E, Lifecycle> encodeLifecycle) {
/*     */     return codec.mapResult(new Codec.ResultFunction<E>() {
/*     */           public <T> DataResult<Pair<E, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<E, T>> a) {
/*     */             return a.result().map(r -> a.setLifecycle(decodeLifecycle.apply(r.getFirst()))).orElse(a);
/*     */           }
/*     */           
/*     */           public <T> DataResult<T> coApply(DynamicOps<T> ops, E input, DataResult<T> t) {
/*     */             return t.setLifecycle(encodeLifecycle.apply(input));
/*     */           }
/*     */           
/*     */           public String toString() {
/*     */             return "WithLifecycle[" + String.valueOf(decodeLifecycle) + " " + String.valueOf(encodeLifecycle) + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static <E> Codec<E> overrideLifecycle(Codec<E> codec, Function<E, Lifecycle> lifecycleGetter) {
/*     */     return overrideLifecycle(codec, lifecycleGetter, lifecycleGetter);
/*     */   }
/*     */   
/*     */   public static <K, V> StrictUnboundedMapCodec<K, V> strictUnboundedMap(Codec<K> keyCodec, Codec<V> elementCodec) {
/*     */     return new StrictUnboundedMapCodec<>(keyCodec, elementCodec);
/*     */   }
/*     */   
/*     */   public static <E> Codec<List<E>> compactListCodec(Codec<E> elementCodec) {
/*     */     return compactListCodec(elementCodec, elementCodec.listOf());
/*     */   }
/*     */   
/*     */   public static <E> Codec<List<E>> compactListCodec(Codec<E> elementCodec, Codec<List<E>> listCodec) {
/*     */     return Codec.either(listCodec, elementCodec).xmap(e -> (List)e.map((), List::of), v -> (v.size() == 1) ? Either.right(v.getFirst()) : Either.left(v));
/*     */   }
/*     */   
/*     */   public static final class StrictUnboundedMapCodec<K, V> extends Record implements BaseMapCodec<K, V>, Codec<Map<K, V>> {
/*     */     private final Codec<K> keyCodec;
/*     */     
/*     */     private final Codec<V> elementCodec;
/*     */     
/*     */     public StrictUnboundedMapCodec(Codec<K> keyCodec, Codec<V> elementCodec) {
/*     */       this.keyCodec = keyCodec;
/*     */       this.elementCodec = elementCodec;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #387	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec<TK;TV;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #387	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec<TK;TV;>;
/*     */     }
/*     */     
/*     */     public Codec<K> keyCodec() {
/*     */       return this.keyCodec;
/*     */     }
/*     */     
/*     */     public Codec<V> elementCodec() {
/*     */       return this.elementCodec;
/*     */     }
/*     */     
/*     */     public <T> DataResult<Map<K, V>> decode(DynamicOps<T> ops, MapLike<T> input) {
/*     */       ImmutableMap.Builder<K, V> read = ImmutableMap.builder();
/*     */       for (Pair<T, T> pair : (Iterable<Pair<T, T>>)input.entries().toList()) {
/*     */         DataResult<K> k = keyCodec().parse(ops, pair.getFirst());
/*     */         DataResult<V> v = elementCodec().parse(ops, pair.getSecond());
/*     */         DataResult<Pair<K, V>> entry = k.apply2stable(Pair::of, v);
/*     */         Optional<DataResult.Error<Pair<K, V>>> error = entry.error();
/*     */         if (error.isPresent()) {
/*     */           String errorMessage = ((DataResult.Error)error.get()).message();
/*     */           return DataResult.error(() -> k.result().isPresent() ? ("Map entry '" + String.valueOf(k.result().get()) + "' : " + errorMessage) : errorMessage);
/*     */         } 
/*     */         if (entry.result().isPresent()) {
/*     */           Pair<K, V> kvPair = entry.result().get();
/*     */           read.put(kvPair.getFirst(), kvPair.getSecond());
/*     */           continue;
/*     */         } 
/*     */         return DataResult.error(() -> "Empty or invalid map contents are not allowed");
/*     */       } 
/*     */       ImmutableMap immutableMap = read.build();
/*     */       return DataResult.success(immutableMap);
/*     */     }
/*     */     
/*     */     public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> ops, T input) {
/*     */       return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> decode(ops, map)).map(r -> Pair.of(r, input));
/*     */     }
/*     */     
/*     */     public <T> DataResult<T> encode(Map<K, V> input, DynamicOps<T> ops, T prefix) {
/*     */       return encode(input, ops, ops.mapBuilder()).build(prefix);
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return "StrictUnboundedMapCodec[" + String.valueOf(this.keyCodec) + " -> " + String.valueOf(this.elementCodec) + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   private static Codec<Integer> intRangeWithMessage(int minInclusive, int maxInclusive, Function<Integer, String> error) {
/*     */     return Codec.INT.validate(value -> (value.compareTo(Integer.valueOf(minInclusive)) >= 0 && value.compareTo(Integer.valueOf(maxInclusive)) <= 0) ? DataResult.success(value) : DataResult.error(()));
/*     */   }
/*     */   
/*     */   public static Codec<Integer> intRange(int minInclusive, int maxInclusive) {
/*     */     return intRangeWithMessage(minInclusive, maxInclusive, n -> "Value must be within range [" + minInclusive + ";" + maxInclusive + "]: " + n);
/*     */   }
/*     */   
/*     */   private static Codec<Long> longRangeWithMessage(long minInclusive, long maxInclusive, Function<Long, String> error) {
/*     */     return Codec.LONG.validate(value -> (value.compareTo(Long.valueOf(minInclusive)) >= 0L && value.compareTo(Long.valueOf(maxInclusive)) <= 0L) ? DataResult.success(value) : DataResult.error(()));
/*     */   }
/*     */   
/*     */   public static Codec<Long> longRange(int minInclusive, int maxInclusive) {
/*     */     return longRangeWithMessage(minInclusive, maxInclusive, n -> "Value must be within range [" + minInclusive + ";" + maxInclusive + "]: " + n);
/*     */   }
/*     */   
/*     */   private static Codec<Float> floatRangeMinInclusiveWithMessage(float minInclusive, float maxInclusive, Function<Float, String> error) {
/*     */     return Codec.FLOAT.validate(value -> (value.compareTo(Float.valueOf(minInclusive)) >= 0 && value.compareTo(Float.valueOf(maxInclusive)) <= 0) ? DataResult.success(value) : DataResult.error(()));
/*     */   }
/*     */   
/*     */   private static Codec<Float> floatRangeMinExclusiveWithMessage(float minExclusive, float maxInclusive, Function<Float, String> error) {
/*     */     return Codec.FLOAT.validate(value -> (value.compareTo(Float.valueOf(minExclusive)) > 0 && value.compareTo(Float.valueOf(maxInclusive)) <= 0) ? DataResult.success(value) : DataResult.error(()));
/*     */   }
/*     */   
/*     */   public static Codec<Float> floatRange(float minInclusive, float maxInclusive) {
/*     */     return floatRangeMinInclusiveWithMessage(minInclusive, maxInclusive, n -> "Value must be within range [" + minInclusive + ";" + maxInclusive + "]: " + n);
/*     */   }
/*     */   
/*     */   public static <T> Codec<List<T>> nonEmptyList(Codec<List<T>> listCodec) {
/*     */     return listCodec.validate(list -> list.isEmpty() ? DataResult.error(()) : DataResult.success(list));
/*     */   }
/*     */   
/*     */   public static <T> Codec<HolderSet<T>> nonEmptyHolderSet(Codec<HolderSet<T>> listCodec) {
/*     */     return listCodec.validate(list -> list.unwrap().right().filter(List::isEmpty).isPresent() ? DataResult.error(()) : DataResult.success(list));
/*     */   }
/*     */   
/*     */   public static <M extends Map<?, ?>> Codec<M> nonEmptyMap(Codec<M> mapCodec) {
/*     */     return mapCodec.validate(map -> map.isEmpty() ? DataResult.error(()) : DataResult.success(map));
/*     */   }
/*     */   
/*     */   public static <E> MapCodec<E> retrieveContext(final Function<DynamicOps<?>, DataResult<E>> getter) {
/*     */     class ContextRetrievalCodec extends MapCodec<E> {
/*     */       public <T> RecordBuilder<T> encode(E input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/*     */         return prefix;
/*     */       }
/*     */       
/*     */       public <T> DataResult<E> decode(DynamicOps<T> ops, MapLike<T> input) {
/*     */         return getter.apply(ops);
/*     */       }
/*     */       
/*     */       public String toString() {
/*     */         return "ContextRetrievalCodec[" + String.valueOf(getter) + "]";
/*     */       }
/*     */       
/*     */       public <T> Stream<T> keys(DynamicOps<T> ops) {
/*     */         return Stream.empty();
/*     */       }
/*     */     };
/*     */     return new ContextRetrievalCodec();
/*     */   }
/*     */   
/*     */   public static <E, L extends Collection<E>, T> Function<L, DataResult<L>> ensureHomogenous(Function<E, T> typeGetter) {
/*     */     return container -> {
/*     */         Iterator<E> it = container.iterator();
/*     */         if (it.hasNext()) {
/*     */           T firstType = (T)typeGetter.apply(it.next());
/*     */           while (it.hasNext()) {
/*     */             E next = it.next();
/*     */             T nextType = typeGetter.apply(next);
/*     */             if (nextType != firstType)
/*     */               return DataResult.error(()); 
/*     */           } 
/*     */         } 
/*     */         return DataResult.success(container, Lifecycle.stable());
/*     */       };
/*     */   }
/*     */   
/*     */   public static <A> Codec<A> catchDecoderException(final Codec<A> codec) {
/* 561 */     return Codec.of((Encoder)codec, new Decoder<A>() {
/*     */           public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/*     */             try {
/* 565 */               return codec.decode(ops, input);
/* 566 */             } catch (Exception e) {
/* 567 */               return DataResult.error(() -> "Caught exception decoding " + String.valueOf(input) + ": " + e.getMessage());
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static Codec<TemporalAccessor> temporalCodec(DateTimeFormatter formatter) {
/* 580 */     Objects.requireNonNull(formatter);
/*     */     return Codec.STRING.comapFlatMap(s -> {
/*     */           try {
/*     */             return DataResult.success(formatter.parse(s));
/*     */           } catch (Exception e) {
/*     */             Objects.requireNonNull(e);
/*     */             return DataResult.error(e::getMessage);
/*     */           } 
/* 580 */         }formatter::format);
/*     */   }
/*     */   
/* 583 */   public static final Codec<Instant> INSTANT_ISO8601 = temporalCodec(DateTimeFormatter.ISO_INSTANT).xmap(Instant::from, Function.identity());
/*     */   
/*     */   public static final Codec<byte[]> BASE64_STRING;
/*     */   
/*     */   public static final Codec<String> ESCAPED_STRING;
/*     */   
/*     */   public static final Codec<TagOrElementLocation> TAG_OR_ELEMENT_ID;
/*     */   
/*     */   public static final Function<Optional<Long>, OptionalLong> toOptionalLong;
/*     */   
/*     */   public static final Function<OptionalLong, Optional<Long>> fromOptionalLong;
/*     */   
/*     */   public static final Codec<BitSet> BIT_SET;
/*     */   
/*     */   public static final int MAX_PROPERTY_NAME_LENGTH = 64;
/*     */   
/*     */   public static final int MAX_PROPERTY_VALUE_LENGTH = 32767;
/*     */   
/*     */   public static final int MAX_PROPERTY_SIGNATURE_LENGTH = 1024;
/*     */   
/*     */   public static final int MAX_PROPERTIES = 16;
/*     */   
/*     */   private static final Codec<Property> PROPERTY;
/*     */   
/*     */   public static final Codec<PropertyMap> PROPERTY_MAP;
/*     */   
/*     */   public static final Codec<String> PLAYER_NAME;
/*     */   
/*     */   static {
/* 585 */     BASE64_STRING = Codec.STRING.comapFlatMap(string -> {
/*     */           try {
/*     */             return DataResult.success(Base64.getDecoder().decode(string));
/* 589 */           } catch (IllegalArgumentException e) {
/*     */             return DataResult.error(());
/*     */           } 
/*     */         }bytes -> Base64.getEncoder().encodeToString(bytes));
/* 596 */     ESCAPED_STRING = Codec.STRING.comapFlatMap(str -> DataResult.success(StringEscapeUtils.unescapeJava(str)), StringEscapeUtils::escapeJava);
/* 609 */     TAG_OR_ELEMENT_ID = Codec.STRING.comapFlatMap(name -> name.startsWith("#") ? Identifier.read(name.substring(1)).map(()) : Identifier.read(name).map(()), TagOrElementLocation::decoratedId);
/* 616 */     toOptionalLong = (o -> (OptionalLong)o.map(OptionalLong::of).orElseGet(OptionalLong::empty));
/* 617 */     fromOptionalLong = (l -> l.isPresent() ? Optional.<Long>of(Long.valueOf(l.getAsLong())) : Optional.empty());
/* 623 */     BIT_SET = Codec.LONG_STREAM.xmap(longStream -> BitSet.valueOf(longStream.toArray()), bitSet -> Arrays.stream(bitSet.toLongArray()));
/* 633 */     PROPERTY = RecordCodecBuilder.create(i -> i.group((App)Codec.sizeLimitedString(64).fieldOf("name").forGetter(Property::name), (App)Codec.sizeLimitedString(32767).fieldOf("value").forGetter(Property::value), (App)Codec.sizeLimitedString(1024).optionalFieldOf("signature").forGetter(())).apply((Applicative)i, ()));
/* 646 */     PROPERTY_MAP = Codec.either(Codec.unboundedMap((Codec)Codec.STRING, Codec.STRING.listOf()).validate(map -> (map.size() > 16) ? DataResult.error(()) : DataResult.success(map)), PROPERTY.sizeLimitedListOf(16)).xmap(mapListEither -> {
/*     */           ImmutableMultimap.Builder<String, Property> result = ImmutableMultimap.builder();
/*     */           mapListEither.ifLeft(()).ifRight(());
/*     */           return new PropertyMap((Multimap)result.build());
/*     */         }propertyMap -> Either.right(propertyMap.values().stream().toList()));
/* 662 */     PLAYER_NAME = Codec.string(0, 16).validate(name -> StringUtil.isValidPlayerName(name) ? DataResult.success(name) : DataResult.error(()));
/*     */   }
/*     */   
/*     */   public static final class TagOrElementLocation extends Record {
/*     */     private final Identifier id;
/*     */     
/*     */     private final boolean tag;
/*     */     
/*     */     public TagOrElementLocation(Identifier id, boolean tag) {
/*     */       this.id = id;
/*     */       this.tag = tag;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ExtraCodecs$TagOrElementLocation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #598	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ExtraCodecs$TagOrElementLocation;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ExtraCodecs$TagOrElementLocation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #598	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ExtraCodecs$TagOrElementLocation;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Identifier id() {
/*     */       return this.id;
/*     */     }
/*     */     
/*     */     public boolean tag() {
/*     */       return this.tag;
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return decoratedId();
/*     */     }
/*     */     
/*     */     private String decoratedId() {
/*     */       return this.tag ? ("#" + String.valueOf(this.id)) : this.id.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   public static MapCodec<OptionalLong> asOptionalLong(MapCodec<Optional<Long>> fieldCodec) {
/*     */     return fieldCodec.xmap(toOptionalLong, fromOptionalLong);
/*     */   }
/*     */   
/*     */   private static MapCodec<GameProfile> gameProfileCodec(Codec<UUID> uuidCodec) {
/* 670 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)uuidCodec.fieldOf("id").forGetter(GameProfile::id), (App)PLAYER_NAME.fieldOf("name").forGetter(GameProfile::name), (App)PROPERTY_MAP.optionalFieldOf("properties", PropertyMap.EMPTY).forGetter(GameProfile::properties)).apply((Applicative)i, GameProfile::new));
/*     */   }
/*     */   
/* 677 */   public static final Codec<GameProfile> AUTHLIB_GAME_PROFILE = gameProfileCodec(UUIDUtil.AUTHLIB_CODEC).codec();
/*     */   
/* 679 */   public static final MapCodec<GameProfile> STORED_GAME_PROFILE = gameProfileCodec(UUIDUtil.CODEC);
/*     */   
/*     */   public static final Codec<String> NON_EMPTY_STRING;
/*     */   
/*     */   public static final Codec<Integer> CODEPOINT;
/*     */   
/*     */   public static final Codec<String> RESOURCE_PATH_CODEC;
/*     */   
/*     */   public static final Codec<URI> UNTRUSTED_URI;
/*     */   
/*     */   public static final Codec<String> CHAT_STRING;
/*     */   
/*     */   static {
/* 681 */     NON_EMPTY_STRING = Codec.STRING.validate(value -> value.isEmpty() ? DataResult.error(()) : DataResult.success(value));
/* 683 */     CODEPOINT = Codec.STRING.comapFlatMap(s -> {
/*     */           int[] codepoint = s.codePoints().toArray();
/*     */           return (codepoint.length != 1) ? DataResult.error(()) : DataResult.success(Integer.valueOf(codepoint[0]));
/* 683 */         }Character::toString);
/* 701 */     RESOURCE_PATH_CODEC = Codec.STRING.validate(s -> !Identifier.isValidPath(s) ? DataResult.error(()) : DataResult.success(s));
/* 708 */     UNTRUSTED_URI = Codec.STRING.comapFlatMap(string -> {
/*     */           try {
/*     */             return DataResult.success(Util.parseAndValidateUntrustedUri(string));
/* 712 */           } catch (URISyntaxException e) {
/*     */             Objects.requireNonNull(e);
/*     */             return DataResult.error(e::getMessage);
/*     */           } 
/*     */         }URI::toString);
/* 719 */     CHAT_STRING = Codec.STRING.validate(string -> {
/*     */           for (int i = 0; i < string.length(); i++) {
/*     */             char c = string.charAt(i);
/*     */             if (!StringUtil.isAllowedChatCharacter(c))
/*     */               return DataResult.error(()); 
/*     */           } 
/*     */           return DataResult.success(string);
/*     */         });
/*     */   }
/*     */   
/*     */   public static <K, V> Codec<Map<K, V>> sizeLimitedMap(Codec<Map<K, V>> codec, int maxSizeInclusive) {
/*     */     return codec.validate(map -> (map.size() > maxSizeInclusive) ? DataResult.error(()) : DataResult.success(map));
/*     */   }
/*     */   
/*     */   public static <T> Codec<Object2BooleanMap<T>> object2BooleanMap(Codec<T> keyCodec) {
/* 730 */     return Codec.unboundedMap(keyCodec, (Codec)Codec.BOOL).xmap(it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap::new, it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap::new);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static <K, V> MapCodec<V> dispatchOptionalValue(final String typeKey, final String valueKey, final Codec<K> typeCodec, final Function<? super V, ? extends K> typeGetter, final Function<? super K, ? extends Codec<? extends V>> valueCodec) {
/* 736 */     return new MapCodec<V>() {
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 739 */           return Stream.of((T[])new Object[] { ops.createString(this.val$typeKey), ops.createString(this.val$valueKey) });
/*     */         }
/*     */         
/*     */         public <T> DataResult<V> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 744 */           T typeName = (T)input.get(typeKey);
/* 745 */           if (typeName == null)
/* 746 */             return DataResult.error(() -> "Missing \"" + typeKey + "\" in: " + String.valueOf(input)); 
/* 748 */           return typeCodec.decode(ops, typeName).flatMap(type -> {
/*     */                 Objects.requireNonNull(ops);
/*     */                 T value = Objects.requireNonNullElseGet((T)input.get(valueKey), ops::emptyMap);
/*     */                 return ((Codec)valueCodec.apply(type.getFirst())).decode(ops, value).map(Pair::getFirst);
/*     */               });
/*     */         }
/*     */         
/*     */         public <T> RecordBuilder<T> encode(V input, DynamicOps<T> ops, RecordBuilder<T> builder) {
/* 756 */           K type = typeGetter.apply(input);
/* 757 */           builder.add(typeKey, typeCodec.encodeStart(ops, type));
/* 758 */           DataResult<T> parameters = encode(valueCodec.apply(type), input, ops);
/* 759 */           if (parameters.result().isEmpty() || !Objects.equals(parameters.result().get(), ops.emptyMap()))
/* 760 */             builder.add(valueKey, parameters); 
/* 762 */           return builder;
/*     */         }
/*     */         
/*     */         private <T, V2 extends V> DataResult<T> encode(Codec<V2> codec, V input, DynamicOps<T> ops) {
/* 767 */           return codec.encodeStart(ops, input);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <A> Codec<Optional<A>> optionalEmptyMap(final Codec<A> codec) {
/* 773 */     return new Codec<Optional<A>>() {
/*     */         public <T> DataResult<Pair<Optional<A>, T>> decode(DynamicOps<T> ops, T input) {
/* 776 */           if (isEmptyMap(ops, input))
/* 777 */             return DataResult.success(Pair.of(Optional.empty(), input)); 
/* 779 */           return codec.decode(ops, input).map(pair -> pair.mapFirst(Optional::of));
/*     */         }
/*     */         
/*     */         private static <T> boolean isEmptyMap(DynamicOps<T> ops, T input) {
/* 783 */           Optional<MapLike<T>> map = ops.getMap(input).result();
/* 784 */           return (map.isPresent() && ((MapLike)map.get()).entries().findAny().isEmpty());
/*     */         }
/*     */         
/*     */         public <T> DataResult<T> encode(Optional<A> input, DynamicOps<T> ops, T prefix) {
/* 789 */           if (input.isEmpty())
/* 790 */             return DataResult.success(ops.emptyMap()); 
/* 792 */           return codec.encode(input.get(), ops, prefix);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static <E extends Enum<E>> Codec<E> legacyEnum(Function<String, E> valueOf) {
/* 800 */     return Codec.STRING.comapFlatMap(key -> {
/*     */           try {
/*     */             return DataResult.success(valueOf.apply(key));
/* 803 */           } catch (IllegalArgumentException ignored) {
/*     */             return DataResult.error(());
/*     */           } 
/*     */         }Enum::toString);
/*     */   }
/*     */   
/*     */   public static class LateBoundIdMapper<I, V> {
/*     */     private final BiMap<I, V> idToValue;
/*     */     
/*     */     public LateBoundIdMapper() {
/* 811 */       this.idToValue = (BiMap<I, V>)HashBiMap.create();
/*     */     }
/*     */     
/*     */     public Codec<V> codec(Codec<I> idCodec) {
/* 814 */       BiMap<V, I> valueToId = this.idToValue.inverse();
/* 815 */       Objects.requireNonNull(this.idToValue);
/* 815 */       Objects.requireNonNull(valueToId);
/* 815 */       return ExtraCodecs.idResolverCodec(idCodec, this.idToValue::get, valueToId::get);
/*     */     }
/*     */     
/*     */     public LateBoundIdMapper<I, V> put(I id, V value) {
/* 820 */       Objects.requireNonNull(value, () -> "Value for " + String.valueOf(id) + " is null");
/* 821 */       this.idToValue.put(id, value);
/* 822 */       return this;
/*     */     }
/*     */     
/*     */     public Set<V> values() {
/* 826 */       return Collections.unmodifiableSet(this.idToValue.values());
/*     */     }
/*     */   }
/*     */   
/*     */   public static Codec<Path> pathCodec(Function<String, Path> pathFactory) {
/* 831 */     return Codec.STRING.xmap(pathFactory, path -> FilenameUtils.separatorsToUnix(path.toString()));
/*     */   }
/*     */   
/*     */   public static Codec<Path> relaiveNormalizedSubPathCodec(Function<String, Path> pathFactory) {
/* 839 */     return pathCodec(pathFactory).xmap(Path::normalize, Path::normalize).validate(path -> path.isAbsolute() ? DataResult.error(()) : (
/*     */         
/* 843 */         (path.startsWith("..") || path.startsWith(".") || FileUtil.isEmptyPath(path)) ? DataResult.error(()) : DataResult.success(path)));
/*     */   }
/*     */   
/*     */   public static Codec<Path> guardedPathCodec(Path baseFolder) {
/* 851 */     Objects.requireNonNull(baseFolder.getFileSystem());
/* 851 */     FileSystem fileSystem = baseFolder.getFileSystem();
/* 851 */     Objects.requireNonNull(baseFolder);
/* 851 */     Objects.requireNonNull(baseFolder);
/* 851 */     return relaiveNormalizedSubPathCodec(x$0 -> rec$.getPath(x$0, new String[0])).xmap(baseFolder::resolve, baseFolder::relativize);
/*     */   }
/*     */   
/*     */   public static <A> MapCodec<A> optionalAlwaysPresentFieldOf(Codec<A> elementCodec, String name, A defaultValue, boolean lenient) {
/* 859 */     return Codec.optionalField(name, elementCodec, lenient).xmap(o -> o.orElse(defaultValue), Optional::of);
/*     */   }
/*     */   
/*     */   public static <A> MapCodec<A> optionalAlwaysPresentFieldOf(Codec<A> elementCodec, String name, A defaultValue) {
/* 870 */     return optionalAlwaysPresentFieldOf(elementCodec, name, defaultValue, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ExtraCodecs.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */