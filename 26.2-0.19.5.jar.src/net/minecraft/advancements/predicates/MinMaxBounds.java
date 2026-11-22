/*     */ package net.minecraft.advancements.predicates;
/*     */ 
/*     */ import com.google.common.collect.Range;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public interface MinMaxBounds<T extends Number & Comparable<T>> {
/*     */   public static final class Ints extends Record implements MinMaxBounds<Integer> {
/*     */     private final MinMaxBounds.Bounds<Integer> bounds;
/*     */     
/*     */     private final MinMaxBounds.Bounds<Long> boundsSqr;
/*     */     
/*     */     public Ints(MinMaxBounds.Bounds<Integer> bounds, MinMaxBounds.Bounds<Long> boundsSqr) {
/*  23 */       this.bounds = bounds;
/*  23 */       this.boundsSqr = boundsSqr;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Ints;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #23	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Ints;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Ints;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #23	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Ints;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Ints;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #23	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Ints;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Bounds<Integer> bounds() {
/*  23 */       return this.bounds;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Bounds<Long> boundsSqr() {
/*  23 */       return this.boundsSqr;
/*     */     }
/*     */     
/*  24 */     public static final Ints ANY = new Ints(MinMaxBounds.Bounds.any());
/*     */     
/*  26 */     public static final Codec<Ints> CODEC = MinMaxBounds.Bounds.<Number>createCodec((Codec<Number>)Codec.INT)
/*  27 */       .validate(MinMaxBounds.Bounds::validateSwappedBoundsInCodec)
/*  28 */       .xmap(Ints::new, Ints::bounds);
/*     */     
/*  30 */     public static final StreamCodec<ByteBuf, Ints> STREAM_CODEC = MinMaxBounds.Bounds.<ByteBuf, Number>createStreamCodec(ByteBufCodecs.INT)
/*  31 */       .map(Ints::new, Ints::bounds);
/*     */     
/*     */     private Ints(MinMaxBounds.Bounds<Integer> bounds) {
/*  34 */       this(bounds, bounds.map(i -> Long.valueOf(Mth.square(i.longValue()))));
/*     */     }
/*     */     
/*     */     public static Ints exactly(int value) {
/*  38 */       return new Ints(MinMaxBounds.Bounds.exactly(Integer.valueOf(value)));
/*     */     }
/*     */     
/*     */     public static Ints between(int min, int max) {
/*  42 */       return new Ints(MinMaxBounds.Bounds.between(Integer.valueOf(min), Integer.valueOf(max)));
/*     */     }
/*     */     
/*     */     public static Ints atLeast(int value) {
/*  46 */       return new Ints(MinMaxBounds.Bounds.atLeast(Integer.valueOf(value)));
/*     */     }
/*     */     
/*     */     public static Ints atMost(int value) {
/*  50 */       return new Ints(MinMaxBounds.Bounds.atMost(Integer.valueOf(value)));
/*     */     }
/*     */     
/*     */     public boolean matches(int value) {
/*  54 */       if (this.bounds.min.isPresent() && ((Integer)this.bounds.min.get()).intValue() > value)
/*  55 */         return false; 
/*  57 */       return (this.bounds.max.isEmpty() || ((Integer)this.bounds.max.get()).intValue() >= value);
/*     */     }
/*     */     
/*     */     public boolean matchesSqr(long valueSqr) {
/*  61 */       if (this.boundsSqr.min.isPresent() && ((Long)this.boundsSqr.min.get()).longValue() > valueSqr)
/*  62 */         return false; 
/*  64 */       return (this.boundsSqr.max.isEmpty() || ((Long)this.boundsSqr.max.get()).longValue() >= valueSqr);
/*     */     }
/*     */     
/*     */     public static Ints fromReader(StringReader reader) throws CommandSyntaxException {
/*  68 */       int start = reader.getCursor();
/*  70 */       Objects.requireNonNull(CommandSyntaxException.BUILT_IN_EXCEPTIONS);
/*  70 */       MinMaxBounds.Bounds<Integer> bounds = MinMaxBounds.Bounds.fromReader(reader, Integer::parseInt, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidInt);
/*  72 */       if (bounds.areSwapped()) {
/*  73 */         reader.setCursor(start);
/*  74 */         throw ERROR_SWAPPED.createWithContext(reader);
/*     */       } 
/*  77 */       return new Ints(bounds);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Doubles extends Record implements MinMaxBounds<Double> {
/*     */     private final MinMaxBounds.Bounds<Double> bounds;
/*     */     
/*     */     private final MinMaxBounds.Bounds<Double> boundsSqr;
/*     */     
/*     */     public Doubles(MinMaxBounds.Bounds<Double> bounds, MinMaxBounds.Bounds<Double> boundsSqr) {
/*  81 */       this.bounds = bounds;
/*  81 */       this.boundsSqr = boundsSqr;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Doubles;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #81	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Doubles;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Doubles;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #81	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Doubles;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Doubles;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #81	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Doubles;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Bounds<Double> bounds() {
/*  81 */       return this.bounds;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Bounds<Double> boundsSqr() {
/*  81 */       return this.boundsSqr;
/*     */     }
/*     */     
/*  82 */     public static final Doubles ANY = new Doubles(MinMaxBounds.Bounds.any());
/*     */     
/*  84 */     public static final Codec<Doubles> CODEC = MinMaxBounds.Bounds.<Number>createCodec((Codec<Number>)Codec.DOUBLE)
/*  85 */       .validate(MinMaxBounds.Bounds::validateSwappedBoundsInCodec)
/*  86 */       .xmap(Doubles::new, Doubles::bounds);
/*     */     
/*  88 */     public static final StreamCodec<ByteBuf, Doubles> STREAM_CODEC = MinMaxBounds.Bounds.<ByteBuf, Number>createStreamCodec(ByteBufCodecs.DOUBLE)
/*  89 */       .map(Doubles::new, Doubles::bounds);
/*     */     
/*     */     private Doubles(MinMaxBounds.Bounds<Double> bounds) {
/*  92 */       this(bounds, bounds.map(Mth::square));
/*     */     }
/*     */     
/*     */     public static Doubles exactly(double value) {
/*  96 */       return new Doubles(MinMaxBounds.Bounds.exactly(Double.valueOf(value)));
/*     */     }
/*     */     
/*     */     public static Doubles between(double min, double max) {
/* 100 */       return new Doubles(MinMaxBounds.Bounds.between(Double.valueOf(min), Double.valueOf(max)));
/*     */     }
/*     */     
/*     */     public static Doubles atLeast(double value) {
/* 104 */       return new Doubles(MinMaxBounds.Bounds.atLeast(Double.valueOf(value)));
/*     */     }
/*     */     
/*     */     public static Doubles atMost(double value) {
/* 108 */       return new Doubles(MinMaxBounds.Bounds.atMost(Double.valueOf(value)));
/*     */     }
/*     */     
/*     */     public boolean matches(double value) {
/* 112 */       if (this.bounds.min.isPresent() && ((Double)this.bounds.min.get()).doubleValue() > value)
/* 113 */         return false; 
/* 115 */       return (this.bounds.max.isEmpty() || ((Double)this.bounds.max.get()).doubleValue() >= value);
/*     */     }
/*     */     
/*     */     public boolean matchesSqr(double valueSqr) {
/* 119 */       if (this.boundsSqr.min.isPresent() && ((Double)this.boundsSqr.min.get()).doubleValue() > valueSqr)
/* 120 */         return false; 
/* 122 */       return (this.boundsSqr.max.isEmpty() || ((Double)this.boundsSqr.max.get()).doubleValue() >= valueSqr);
/*     */     }
/*     */     
/*     */     public static Doubles fromReader(StringReader reader) throws CommandSyntaxException {
/* 126 */       int start = reader.getCursor();
/* 128 */       Objects.requireNonNull(CommandSyntaxException.BUILT_IN_EXCEPTIONS);
/* 128 */       MinMaxBounds.Bounds<Double> bounds = MinMaxBounds.Bounds.fromReader(reader, Double::parseDouble, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidDouble);
/* 130 */       if (bounds.areSwapped()) {
/* 131 */         reader.setCursor(start);
/* 132 */         throw ERROR_SWAPPED.createWithContext(reader);
/*     */       } 
/* 135 */       return new Doubles(bounds);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class FloatDegrees extends Record implements MinMaxBounds<Float> {
/*     */     private final MinMaxBounds.Bounds<Float> bounds;
/*     */     
/*     */     public FloatDegrees(MinMaxBounds.Bounds<Float> bounds) {
/* 139 */       this.bounds = bounds;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/MinMaxBounds$FloatDegrees;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$FloatDegrees;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/MinMaxBounds$FloatDegrees;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$FloatDegrees;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/MinMaxBounds$FloatDegrees;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$FloatDegrees;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Bounds<Float> bounds() {
/* 139 */       return this.bounds;
/*     */     }
/*     */     
/* 140 */     public static final FloatDegrees ANY = new FloatDegrees(MinMaxBounds.Bounds.any());
/*     */     
/* 142 */     public static final Codec<FloatDegrees> CODEC = MinMaxBounds.Bounds.<Number>createCodec((Codec<Number>)Codec.FLOAT)
/* 143 */       .xmap(FloatDegrees::new, FloatDegrees::bounds);
/*     */     
/* 145 */     public static final StreamCodec<ByteBuf, FloatDegrees> STREAM_CODEC = MinMaxBounds.Bounds.<ByteBuf, Number>createStreamCodec(ByteBufCodecs.FLOAT)
/* 146 */       .map(FloatDegrees::new, FloatDegrees::bounds);
/*     */     
/*     */     public static FloatDegrees fromReader(StringReader reader) throws CommandSyntaxException {
/* 149 */       Objects.requireNonNull(CommandSyntaxException.BUILT_IN_EXCEPTIONS);
/* 149 */       MinMaxBounds.Bounds<Float> bounds = MinMaxBounds.Bounds.fromReader(reader, Float::parseFloat, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidFloat);
/* 150 */       return new FloatDegrees(bounds);
/*     */     }
/*     */   }
/*     */   
/* 156 */   public static final SimpleCommandExceptionType ERROR_EMPTY = new SimpleCommandExceptionType((Message)Component.translatable("argument.range.empty"));
/*     */   
/* 157 */   public static final SimpleCommandExceptionType ERROR_SWAPPED = new SimpleCommandExceptionType((Message)Component.translatable("argument.range.swapped"));
/*     */   
/*     */   Bounds<T> bounds();
/*     */   
/*     */   default Optional<T> min() {
/* 162 */     return (bounds()).min;
/*     */   }
/*     */   
/*     */   default Optional<T> max() {
/* 166 */     return (bounds()).max;
/*     */   }
/*     */   
/*     */   default boolean isAny() {
/* 170 */     return bounds().isAny();
/*     */   }
/*     */   
/*     */   static <V extends Number & Comparable<V>, B extends MinMaxBounds<V>> Function<B, DataResult<B>> validateContainedInRange(MinMaxBounds<V> allowed) {
/* 174 */     Range<V> allowedRange = allowed.bounds().asRange();
/* 175 */     return target -> {
/*     */         Range<V> selfAsRange = target.bounds().asRange();
/*     */         return !allowedRange.encloses(selfAsRange) ? DataResult.error(()) : DataResult.success(target);
/*     */       };
/*     */   }
/*     */   
/*     */   public static final class Bounds<T extends Number & Comparable<T>> extends Record {
/*     */     private final Optional<T> min;
/*     */     
/*     */     private final Optional<T> max;
/*     */     
/*     */     public Bounds(Optional<T> min, Optional<T> max) {
/* 184 */       this.min = min;
/* 184 */       this.max = max;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #184	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #184	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #184	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds<TT;>;
/*     */     }
/*     */     
/*     */     public Optional<T> min() {
/* 184 */       return this.min;
/*     */     }
/*     */     
/*     */     public Optional<T> max() {
/* 184 */       return this.max;
/*     */     }
/*     */     
/*     */     public boolean isAny() {
/* 189 */       return (min().isEmpty() && max().isEmpty());
/*     */     }
/*     */     
/*     */     public DataResult<Bounds<T>> validateSwappedBoundsInCodec() {
/* 193 */       if (areSwapped())
/* 194 */         return DataResult.error(() -> "Swapped bounds in range: " + String.valueOf(min()) + " is higher than " + String.valueOf(max())); 
/* 196 */       return DataResult.success(this);
/*     */     }
/*     */     
/*     */     public boolean areSwapped() {
/* 200 */       return (this.min.isPresent() && this.max.isPresent() && ((Comparable<Number>)this.min.get()).compareTo((Number)this.max.get()) > 0);
/*     */     }
/*     */     
/*     */     public Range<T> asRange() {
/* 204 */       if (this.min.isPresent()) {
/* 205 */         if (this.max.isPresent())
/* 206 */           return Range.closed((Comparable)this.min.get(), (Comparable)this.max.get()); 
/* 208 */         return Range.atLeast((Comparable)this.min.get());
/*     */       } 
/* 210 */       if (this.max.isPresent())
/* 211 */         return Range.atMost((Comparable)this.max.get()); 
/* 213 */       return Range.all();
/*     */     }
/*     */     
/*     */     public Optional<T> asPoint() {
/* 218 */       Optional<T> min = min();
/* 219 */       Optional<T> max = max();
/* 220 */       return min.equals(max) ? min : Optional.<T>empty();
/*     */     }
/*     */     
/*     */     public static <T extends Number & Comparable<T>> Bounds<T> any() {
/* 226 */       return new Bounds<>(Optional.empty(), Optional.empty());
/*     */     }
/*     */     
/*     */     public static <T extends Number & Comparable<T>> Bounds<T> exactly(T value) {
/* 230 */       Optional<T> wrapped = Optional.of(value);
/* 231 */       return new Bounds<>(wrapped, wrapped);
/*     */     }
/*     */     
/*     */     public static <T extends Number & Comparable<T>> Bounds<T> between(T min, T max) {
/* 235 */       return new Bounds<>(Optional.of(min), Optional.of(max));
/*     */     }
/*     */     
/*     */     public static <T extends Number & Comparable<T>> Bounds<T> atLeast(T value) {
/* 239 */       return new Bounds<>(Optional.of(value), Optional.empty());
/*     */     }
/*     */     
/*     */     public static <T extends Number & Comparable<T>> Bounds<T> atMost(T value) {
/* 243 */       return new Bounds<>(Optional.empty(), Optional.of(value));
/*     */     }
/*     */     
/*     */     public <U extends Number & Comparable<U>> Bounds<U> map(Function<T, U> mapper) {
/* 247 */       return new Bounds(this.min.map(mapper), this.max.map(mapper));
/*     */     }
/*     */     
/*     */     public static <T extends Number & Comparable<T>> Codec<Bounds<T>> createCodec(Codec<T> numberCodec) {
/* 251 */       Codec<Bounds<T>> rangeCodec = RecordCodecBuilder.create(i -> i.group((App)numberCodec.optionalFieldOf("min").forGetter(Bounds::min), (App)numberCodec.optionalFieldOf("max").forGetter(Bounds::max)).apply((Applicative)i, Bounds::new));
/* 256 */       return Codec.either(rangeCodec, numberCodec).xmap(either -> (Bounds)either.map((), ()), bounds -> {
/*     */             Optional<T> point = bounds.asPoint();
/*     */             return point.isPresent() ? Either.right((Number)point.get()) : Either.left(bounds);
/*     */           });
/*     */     }
/*     */     
/*     */     public static <B extends ByteBuf, T extends Number & Comparable<T>> StreamCodec<B, Bounds<T>> createStreamCodec(final StreamCodec<B, T> numberCodec) {
/* 266 */       return new StreamCodec<B, Bounds<T>>() {
/*     */           private static final int MIN_FLAG = 1;
/*     */           
/*     */           private static final int MAX_FLAG = 2;
/*     */           
/*     */           public MinMaxBounds.Bounds<T> decode(B input) {
/* 272 */             byte flags = input.readByte();
/* 273 */             Optional<T> min = ((flags & 0x1) != 0) ? Optional.<T>of((T)numberCodec.decode(input)) : Optional.<T>empty();
/* 274 */             Optional<T> max = ((flags & 0x2) != 0) ? Optional.<T>of((T)numberCodec.decode(input)) : Optional.<T>empty();
/* 275 */             return new MinMaxBounds.Bounds<>(min, max);
/*     */           }
/*     */           
/*     */           public void encode(B output, MinMaxBounds.Bounds<T> value) {
/* 280 */             Optional<T> min = value.min();
/* 281 */             Optional<T> max = value.max();
/* 282 */             output.writeByte((min.isPresent() ? 1 : 0) | (max.isPresent() ? 2 : 0));
/* 283 */             min.ifPresent(v -> numberCodec.encode(output, v));
/* 284 */             max.ifPresent(v -> numberCodec.encode(output, v));
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public static <T extends Number & Comparable<T>> Bounds<T> fromReader(StringReader reader, Function<String, T> converter, Supplier<DynamicCommandExceptionType> parseExc) throws CommandSyntaxException {
/* 290 */       if (!reader.canRead())
/* 291 */         throw MinMaxBounds.ERROR_EMPTY.createWithContext(reader); 
/* 294 */       int start = reader.getCursor();
/*     */       try {
/* 297 */         Optional<T> max, min = (Optional)readNumber(reader, converter, parseExc);
/* 299 */         if (reader.canRead(2) && reader.peek() == '.' && reader.peek(1) == '.') {
/* 300 */           reader.skip();
/* 301 */           reader.skip();
/* 302 */           max = readNumber(reader, converter, parseExc);
/*     */         } else {
/* 304 */           max = min;
/*     */         } 
/* 307 */         if (min.isEmpty() && max.isEmpty())
/* 308 */           throw MinMaxBounds.ERROR_EMPTY.createWithContext(reader); 
/* 310 */         return new Bounds<>(min, max);
/* 311 */       } catch (CommandSyntaxException e) {
/* 312 */         reader.setCursor(start);
/* 313 */         throw new CommandSyntaxException(e.getType(), e.getRawMessage(), e.getInput(), start);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static <T extends Number> Optional<T> readNumber(StringReader reader, Function<String, T> converter, Supplier<DynamicCommandExceptionType> parseExc) throws CommandSyntaxException {
/* 318 */       int start = reader.getCursor();
/* 319 */       while (reader.canRead() && isAllowedInputChar(reader))
/* 320 */         reader.skip(); 
/* 322 */       String number = reader.getString().substring(start, reader.getCursor());
/* 323 */       if (number.isEmpty())
/* 324 */         return Optional.empty(); 
/*     */       try {
/* 327 */         return Optional.of(converter.apply(number));
/* 328 */       } catch (NumberFormatException ex) {
/* 329 */         throw ((DynamicCommandExceptionType)parseExc.get()).createWithContext(reader, number);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static boolean isAllowedInputChar(StringReader reader) {
/* 334 */       char c = reader.peek();
/* 335 */       if ((c >= '0' && c <= '9') || c == '-')
/* 336 */         return true; 
/* 339 */       if (c == '.')
/* 340 */         return (!reader.canRead(2) || reader.peek(1) != '.'); 
/* 343 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements StreamCodec<B, Bounds<T>> {
/*     */     private static final int MIN_FLAG = 1;
/*     */     
/*     */     private static final int MAX_FLAG = 2;
/*     */     
/*     */     public MinMaxBounds.Bounds<T> decode(B input) {
/*     */       byte flags = input.readByte();
/*     */       Optional<T> min = ((flags & 0x1) != 0) ? Optional.<T>of((T)numberCodec.decode(input)) : Optional.<T>empty();
/*     */       Optional<T> max = ((flags & 0x2) != 0) ? Optional.<T>of((T)numberCodec.decode(input)) : Optional.<T>empty();
/*     */       return new MinMaxBounds.Bounds<>(min, max);
/*     */     }
/*     */     
/*     */     public void encode(B output, MinMaxBounds.Bounds<T> value) {
/*     */       Optional<T> min = value.min();
/*     */       Optional<T> max = value.max();
/*     */       output.writeByte((min.isPresent() ? 1 : 0) | (max.isPresent() ? 2 : 0));
/*     */       min.ifPresent(v -> numberCodec.encode(output, v));
/*     */       max.ifPresent(v -> numberCodec.encode(output, v));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\MinMaxBounds.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */