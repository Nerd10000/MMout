/*     */ package net.minecraft.advancements.predicates;
/*     */ 
/*     */ import com.google.common.collect.Range;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ public final class Bounds<T extends Number & Comparable<T>> extends Record {
/*     */   private final Optional<T> min;
/*     */   
/*     */   private final Optional<T> max;
/*     */   
/*     */   public Bounds(Optional<T> min, Optional<T> max) {
/* 184 */     this.min = min;
/* 184 */     this.max = max;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #184	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #184	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #184	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$Bounds<TT;>;
/*     */   }
/*     */   
/*     */   public Optional<T> min() {
/* 184 */     return this.min;
/*     */   }
/*     */   
/*     */   public Optional<T> max() {
/* 184 */     return this.max;
/*     */   }
/*     */   
/*     */   public boolean isAny() {
/* 189 */     return (min().isEmpty() && max().isEmpty());
/*     */   }
/*     */   
/*     */   public DataResult<Bounds<T>> validateSwappedBoundsInCodec() {
/* 193 */     if (areSwapped())
/* 194 */       return DataResult.error(() -> "Swapped bounds in range: " + String.valueOf(min()) + " is higher than " + String.valueOf(max())); 
/* 196 */     return DataResult.success(this);
/*     */   }
/*     */   
/*     */   public boolean areSwapped() {
/* 200 */     return (this.min.isPresent() && this.max.isPresent() && ((Comparable<Number>)this.min.get()).compareTo((Number)this.max.get()) > 0);
/*     */   }
/*     */   
/*     */   public Range<T> asRange() {
/* 204 */     if (this.min.isPresent()) {
/* 205 */       if (this.max.isPresent())
/* 206 */         return Range.closed((Comparable)this.min.get(), (Comparable)this.max.get()); 
/* 208 */       return Range.atLeast((Comparable)this.min.get());
/*     */     } 
/* 210 */     if (this.max.isPresent())
/* 211 */       return Range.atMost((Comparable)this.max.get()); 
/* 213 */     return Range.all();
/*     */   }
/*     */   
/*     */   public Optional<T> asPoint() {
/* 218 */     Optional<T> min = min();
/* 219 */     Optional<T> max = max();
/* 220 */     return min.equals(max) ? min : Optional.<T>empty();
/*     */   }
/*     */   
/*     */   public static <T extends Number & Comparable<T>> Bounds<T> any() {
/* 226 */     return new Bounds<>(Optional.empty(), Optional.empty());
/*     */   }
/*     */   
/*     */   public static <T extends Number & Comparable<T>> Bounds<T> exactly(T value) {
/* 230 */     Optional<T> wrapped = Optional.of(value);
/* 231 */     return new Bounds<>(wrapped, wrapped);
/*     */   }
/*     */   
/*     */   public static <T extends Number & Comparable<T>> Bounds<T> between(T min, T max) {
/* 235 */     return new Bounds<>(Optional.of(min), Optional.of(max));
/*     */   }
/*     */   
/*     */   public static <T extends Number & Comparable<T>> Bounds<T> atLeast(T value) {
/* 239 */     return new Bounds<>(Optional.of(value), Optional.empty());
/*     */   }
/*     */   
/*     */   public static <T extends Number & Comparable<T>> Bounds<T> atMost(T value) {
/* 243 */     return new Bounds<>(Optional.empty(), Optional.of(value));
/*     */   }
/*     */   
/*     */   public <U extends Number & Comparable<U>> Bounds<U> map(Function<T, U> mapper) {
/* 247 */     return new Bounds(this.min.map(mapper), this.max.map(mapper));
/*     */   }
/*     */   
/*     */   public static <T extends Number & Comparable<T>> Codec<Bounds<T>> createCodec(Codec<T> numberCodec) {
/* 251 */     Codec<Bounds<T>> rangeCodec = RecordCodecBuilder.create(i -> i.group((App)numberCodec.optionalFieldOf("min").forGetter(Bounds::min), (App)numberCodec.optionalFieldOf("max").forGetter(Bounds::max)).apply((Applicative)i, Bounds::new));
/* 256 */     return Codec.either(rangeCodec, numberCodec).xmap(either -> (Bounds)either.map((), ()), bounds -> {
/*     */           Optional<T> point = bounds.asPoint();
/*     */           return point.isPresent() ? Either.right((Number)point.get()) : Either.left(bounds);
/*     */         });
/*     */   }
/*     */   
/*     */   public static <B extends ByteBuf, T extends Number & Comparable<T>> StreamCodec<B, Bounds<T>> createStreamCodec(final StreamCodec<B, T> numberCodec) {
/* 266 */     return new StreamCodec<B, Bounds<T>>() {
/*     */         private static final int MIN_FLAG = 1;
/*     */         
/*     */         private static final int MAX_FLAG = 2;
/*     */         
/*     */         public MinMaxBounds.Bounds<T> decode(B input) {
/* 272 */           byte flags = input.readByte();
/* 273 */           Optional<T> min = ((flags & 0x1) != 0) ? Optional.<T>of((T)numberCodec.decode(input)) : Optional.<T>empty();
/* 274 */           Optional<T> max = ((flags & 0x2) != 0) ? Optional.<T>of((T)numberCodec.decode(input)) : Optional.<T>empty();
/* 275 */           return new MinMaxBounds.Bounds<>(min, max);
/*     */         }
/*     */         
/*     */         public void encode(B output, MinMaxBounds.Bounds<T> value) {
/* 280 */           Optional<T> min = value.min();
/* 281 */           Optional<T> max = value.max();
/* 282 */           output.writeByte((min.isPresent() ? 1 : 0) | (max.isPresent() ? 2 : 0));
/* 283 */           min.ifPresent(v -> numberCodec.encode(output, v));
/* 284 */           max.ifPresent(v -> numberCodec.encode(output, v));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <T extends Number & Comparable<T>> Bounds<T> fromReader(StringReader reader, Function<String, T> converter, Supplier<DynamicCommandExceptionType> parseExc) throws CommandSyntaxException {
/* 290 */     if (!reader.canRead())
/* 291 */       throw MinMaxBounds.ERROR_EMPTY.createWithContext(reader); 
/* 294 */     int start = reader.getCursor();
/*     */     try {
/* 297 */       Optional<T> max, min = (Optional)readNumber(reader, converter, parseExc);
/* 299 */       if (reader.canRead(2) && reader.peek() == '.' && reader.peek(1) == '.') {
/* 300 */         reader.skip();
/* 301 */         reader.skip();
/* 302 */         max = readNumber(reader, converter, parseExc);
/*     */       } else {
/* 304 */         max = min;
/*     */       } 
/* 307 */       if (min.isEmpty() && max.isEmpty())
/* 308 */         throw MinMaxBounds.ERROR_EMPTY.createWithContext(reader); 
/* 310 */       return new Bounds<>(min, max);
/* 311 */     } catch (CommandSyntaxException e) {
/* 312 */       reader.setCursor(start);
/* 313 */       throw new CommandSyntaxException(e.getType(), e.getRawMessage(), e.getInput(), start);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T extends Number> Optional<T> readNumber(StringReader reader, Function<String, T> converter, Supplier<DynamicCommandExceptionType> parseExc) throws CommandSyntaxException {
/* 318 */     int start = reader.getCursor();
/* 319 */     while (reader.canRead() && isAllowedInputChar(reader))
/* 320 */       reader.skip(); 
/* 322 */     String number = reader.getString().substring(start, reader.getCursor());
/* 323 */     if (number.isEmpty())
/* 324 */       return Optional.empty(); 
/*     */     try {
/* 327 */       return Optional.of(converter.apply(number));
/* 328 */     } catch (NumberFormatException ex) {
/* 329 */       throw ((DynamicCommandExceptionType)parseExc.get()).createWithContext(reader, number);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isAllowedInputChar(StringReader reader) {
/* 334 */     char c = reader.peek();
/* 335 */     if ((c >= '0' && c <= '9') || c == '-')
/* 336 */       return true; 
/* 339 */     if (c == '.')
/* 340 */       return (!reader.canRead(2) || reader.peek(1) != '.'); 
/* 343 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\MinMaxBounds$Bounds.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */