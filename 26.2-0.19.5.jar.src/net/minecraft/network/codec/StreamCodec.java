/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.datafixers.util.Function11;
/*     */ import com.mojang.datafixers.util.Function12;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ public interface StreamCodec<B, V> extends StreamEncoder<B, V>, StreamDecoder<B, V> {
/*     */   static <B, V> StreamCodec<B, V> of(final StreamEncoder<B, V> encoder, final StreamDecoder<B, V> decoder) {
/*  23 */     return new StreamCodec<B, V>() {
/*     */         public V decode(B input) {
/*  26 */           return decoder.decode(input);
/*     */         }
/*     */         
/*     */         public void encode(B output, V value) {
/*  31 */           encoder.encode(output, value);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, V> StreamCodec<B, V> ofMember(final StreamMemberEncoder<B, V> encoder, final StreamDecoder<B, V> decoder) {
/*  38 */     return new StreamCodec<B, V>() {
/*     */         public V decode(B input) {
/*  41 */           return decoder.decode(input);
/*     */         }
/*     */         
/*     */         public void encode(B output, V value) {
/*  46 */           encoder.encode(value, output);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, V> StreamCodec<B, V> unit(final V instance) {
/*  52 */     return new StreamCodec<B, V>() {
/*     */         public V decode(B input) {
/*  55 */           return (V)instance;
/*     */         }
/*     */         
/*     */         public void encode(B output, V value) {
/*  60 */           if (!value.equals(instance))
/*  61 */             throw new IllegalStateException("Can't encode '" + String.valueOf(value) + "', expected '" + String.valueOf(instance) + "'"); 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default <O> StreamCodec<B, O> apply(CodecOperation<B, V, O> operation) {
/*  73 */     return operation.apply(this);
/*     */   }
/*     */   
/*     */   default <O> StreamCodec<B, O> map(final Function<? super V, ? extends O> to, final Function<? super O, ? extends V> from) {
/*  77 */     return new StreamCodec<B, O>() {
/*     */         public O decode(B input) {
/*  80 */           return (O)to.apply(StreamCodec.this.decode(input));
/*     */         }
/*     */         
/*     */         public void encode(B output, O value) {
/*  85 */           StreamCodec.this.encode(output, from.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface CodecOperation<B, S, T> {
/*     */     StreamCodec<B, T> apply(StreamCodec<B, S> param1StreamCodec);
/*     */   }
/*     */   
/*     */   default <O extends io.netty.buffer.ByteBuf> StreamCodec<O, V> mapStream(final Function<O, ? extends B> operation) {
/*  91 */     return new StreamCodec<O, V>() {
/*     */         public V decode(O input) {
/*  94 */           B wrappedStream = operation.apply(input);
/*  95 */           return StreamCodec.this.decode(wrappedStream);
/*     */         }
/*     */         
/*     */         public void encode(O output, V value) {
/* 100 */           B wrappedStream = operation.apply(output);
/* 101 */           StreamCodec.this.encode(wrappedStream, value);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default <U> StreamCodec<B, U> dispatch(final Function<? super U, ? extends V> type, final Function<? super V, ? extends StreamCodec<? super B, ? extends U>> codec) {
/* 107 */     return new StreamCodec<B, U>() {
/*     */         public U decode(B input) {
/* 110 */           V key = StreamCodec.this.decode(input);
/* 111 */           StreamCodec<? super B, ? extends U> valueCodec = codec.apply(key);
/* 112 */           return valueCodec.decode(input);
/*     */         }
/*     */         
/*     */         public void encode(B output, U value) {
/* 117 */           V key = type.apply(value);
/* 119 */           StreamCodec<B, U> valueCodec = codec.apply(key);
/* 120 */           StreamCodec.this.encode(output, key);
/* 121 */           valueCodec.encode(output, value);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, C, T1> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final Function<T1, C> constructor) {
/* 131 */     return new StreamCodec<B, C>() {
/*     */         public C decode(B input) {
/* 134 */           T1 v1 = codec1.decode(input);
/* 135 */           return constructor.apply(v1);
/*     */         }
/*     */         
/*     */         public void encode(B output, C value) {
/* 140 */           codec1.encode(output, getter1.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, C, T1, T2> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final BiFunction<T1, T2, C> constructor) {
/* 150 */     return new StreamCodec<B, C>() {
/*     */         public C decode(B input) {
/* 153 */           T1 v1 = codec1.decode(input);
/* 154 */           T2 v2 = codec2.decode(input);
/* 155 */           return constructor.apply(v1, v2);
/*     */         }
/*     */         
/*     */         public void encode(B output, C value) {
/* 160 */           codec1.encode(output, getter1.apply(value));
/* 161 */           codec2.encode(output, getter2.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, C, T1, T2, T3> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final Function3<T1, T2, T3, C> constructor) {
/* 172 */     return new StreamCodec<B, C>() {
/*     */         public C decode(B input) {
/* 175 */           T1 v1 = codec1.decode(input);
/* 176 */           T2 v2 = codec2.decode(input);
/* 177 */           T3 v3 = codec3.decode(input);
/* 178 */           return (C)constructor.apply(v1, v2, v3);
/*     */         }
/*     */         
/*     */         public void encode(B output, C value) {
/* 183 */           codec1.encode(output, getter1.apply(value));
/* 184 */           codec2.encode(output, getter2.apply(value));
/* 185 */           codec3.encode(output, getter3.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, C, T1, T2, T3, T4> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final Function4<T1, T2, T3, T4, C> constructor) {
/* 197 */     return new StreamCodec<B, C>() {
/*     */         public C decode(B input) {
/* 200 */           T1 v1 = codec1.decode(input);
/* 201 */           T2 v2 = codec2.decode(input);
/* 202 */           T3 v3 = codec3.decode(input);
/* 203 */           T4 v4 = codec4.decode(input);
/* 204 */           return (C)constructor.apply(v1, v2, v3, v4);
/*     */         }
/*     */         
/*     */         public void encode(B output, C value) {
/* 209 */           codec1.encode(output, getter1.apply(value));
/* 210 */           codec2.encode(output, getter2.apply(value));
/* 211 */           codec3.encode(output, getter3.apply(value));
/* 212 */           codec4.encode(output, getter4.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final Function5<T1, T2, T3, T4, T5, C> constructor) {
/* 225 */     return new StreamCodec<B, C>() {
/*     */         public C decode(B input) {
/* 228 */           T1 v1 = codec1.decode(input);
/* 229 */           T2 v2 = codec2.decode(input);
/* 230 */           T3 v3 = codec3.decode(input);
/* 231 */           T4 v4 = codec4.decode(input);
/* 232 */           T5 v5 = codec5.decode(input);
/* 233 */           return (C)constructor.apply(v1, v2, v3, v4, v5);
/*     */         }
/*     */         
/*     */         public void encode(B output, C value) {
/* 238 */           codec1.encode(output, getter1.apply(value));
/* 239 */           codec2.encode(output, getter2.apply(value));
/* 240 */           codec3.encode(output, getter3.apply(value));
/* 241 */           codec4.encode(output, getter4.apply(value));
/* 242 */           codec5.encode(output, getter5.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5, T6> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final Function6<T1, T2, T3, T4, T5, T6, C> constructor) {
/* 256 */     return new StreamCodec<B, C>() {
/*     */         public C decode(B input) {
/* 259 */           T1 v1 = codec1.decode(input);
/* 260 */           T2 v2 = codec2.decode(input);
/* 261 */           T3 v3 = codec3.decode(input);
/* 262 */           T4 v4 = codec4.decode(input);
/* 263 */           T5 v5 = codec5.decode(input);
/* 264 */           T6 v6 = codec6.decode(input);
/* 265 */           return (C)constructor.apply(v1, v2, v3, v4, v5, v6);
/*     */         }
/*     */         
/*     */         public void encode(B output, C value) {
/* 270 */           codec1.encode(output, getter1.apply(value));
/* 271 */           codec2.encode(output, getter2.apply(value));
/* 272 */           codec3.encode(output, getter3.apply(value));
/* 273 */           codec4.encode(output, getter4.apply(value));
/* 274 */           codec5.encode(output, getter5.apply(value));
/* 275 */           codec6.encode(output, getter6.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final Function7<T1, T2, T3, T4, T5, T6, T7, C> constructor) {
/* 290 */     return new StreamCodec<B, C>() {
/*     */         public C decode(B input) {
/* 293 */           T1 v1 = codec1.decode(input);
/* 294 */           T2 v2 = codec2.decode(input);
/* 295 */           T3 v3 = codec3.decode(input);
/* 296 */           T4 v4 = codec4.decode(input);
/* 297 */           T5 v5 = codec5.decode(input);
/* 298 */           T6 v6 = codec6.decode(input);
/* 299 */           T7 v7 = codec7.decode(input);
/* 300 */           return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7);
/*     */         }
/*     */         
/*     */         public void encode(B output, C value) {
/* 305 */           codec1.encode(output, getter1.apply(value));
/* 306 */           codec2.encode(output, getter2.apply(value));
/* 307 */           codec3.encode(output, getter3.apply(value));
/* 308 */           codec4.encode(output, getter4.apply(value));
/* 309 */           codec5.encode(output, getter5.apply(value));
/* 310 */           codec6.encode(output, getter6.apply(value));
/* 311 */           codec7.encode(output, getter7.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8, final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> constructor) {
/* 327 */     return new StreamCodec<B, C>() {
/*     */         public C decode(B input) {
/* 330 */           T1 v1 = codec1.decode(input);
/* 331 */           T2 v2 = codec2.decode(input);
/* 332 */           T3 v3 = codec3.decode(input);
/* 333 */           T4 v4 = codec4.decode(input);
/* 334 */           T5 v5 = codec5.decode(input);
/* 335 */           T6 v6 = codec6.decode(input);
/* 336 */           T7 v7 = codec7.decode(input);
/* 337 */           T8 v8 = codec8.decode(input);
/* 338 */           return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8);
/*     */         }
/*     */         
/*     */         public void encode(B output, C value) {
/* 343 */           codec1.encode(output, getter1.apply(value));
/* 344 */           codec2.encode(output, getter2.apply(value));
/* 345 */           codec3.encode(output, getter3.apply(value));
/* 346 */           codec4.encode(output, getter4.apply(value));
/* 347 */           codec5.encode(output, getter5.apply(value));
/* 348 */           codec6.encode(output, getter6.apply(value));
/* 349 */           codec7.encode(output, getter7.apply(value));
/* 350 */           codec8.encode(output, getter8.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8, final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9, final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C> constructor) {
/* 367 */     return new StreamCodec<B, C>() {
/*     */         public C decode(B input) {
/* 370 */           T1 v1 = codec1.decode(input);
/* 371 */           T2 v2 = codec2.decode(input);
/* 372 */           T3 v3 = codec3.decode(input);
/* 373 */           T4 v4 = codec4.decode(input);
/* 374 */           T5 v5 = codec5.decode(input);
/* 375 */           T6 v6 = codec6.decode(input);
/* 376 */           T7 v7 = codec7.decode(input);
/* 377 */           T8 v8 = codec8.decode(input);
/* 378 */           T9 v9 = codec9.decode(input);
/* 379 */           return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9);
/*     */         }
/*     */         
/*     */         public void encode(B output, C value) {
/* 384 */           codec1.encode(output, getter1.apply(value));
/* 385 */           codec2.encode(output, getter2.apply(value));
/* 386 */           codec3.encode(output, getter3.apply(value));
/* 387 */           codec4.encode(output, getter4.apply(value));
/* 388 */           codec5.encode(output, getter5.apply(value));
/* 389 */           codec6.encode(output, getter6.apply(value));
/* 390 */           codec7.encode(output, getter7.apply(value));
/* 391 */           codec8.encode(output, getter8.apply(value));
/* 392 */           codec9.encode(output, getter9.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8, final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9, final StreamCodec<? super B, T10> codec10, final Function<C, T10> getter10, final Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, C> constructor) {
/* 410 */     return new StreamCodec<B, C>() {
/*     */         public C decode(B input) {
/* 413 */           T1 v1 = codec1.decode(input);
/* 414 */           T2 v2 = codec2.decode(input);
/* 415 */           T3 v3 = codec3.decode(input);
/* 416 */           T4 v4 = codec4.decode(input);
/* 417 */           T5 v5 = codec5.decode(input);
/* 418 */           T6 v6 = codec6.decode(input);
/* 419 */           T7 v7 = codec7.decode(input);
/* 420 */           T8 v8 = codec8.decode(input);
/* 421 */           T9 v9 = codec9.decode(input);
/* 422 */           T10 v10 = codec10.decode(input);
/* 423 */           return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);
/*     */         }
/*     */         
/*     */         public void encode(B output, C value) {
/* 428 */           codec1.encode(output, getter1.apply(value));
/* 429 */           codec2.encode(output, getter2.apply(value));
/* 430 */           codec3.encode(output, getter3.apply(value));
/* 431 */           codec4.encode(output, getter4.apply(value));
/* 432 */           codec5.encode(output, getter5.apply(value));
/* 433 */           codec6.encode(output, getter6.apply(value));
/* 434 */           codec7.encode(output, getter7.apply(value));
/* 435 */           codec8.encode(output, getter8.apply(value));
/* 436 */           codec9.encode(output, getter9.apply(value));
/* 437 */           codec10.encode(output, getter10.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8, final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9, final StreamCodec<? super B, T10> codec10, final Function<C, T10> getter10, final StreamCodec<? super B, T11> codec11, final Function<C, T11> getter11, final Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, C> constructor) {
/* 456 */     return new StreamCodec<B, C>() {
/*     */         public C decode(B input) {
/* 459 */           T1 v1 = codec1.decode(input);
/* 460 */           T2 v2 = codec2.decode(input);
/* 461 */           T3 v3 = codec3.decode(input);
/* 462 */           T4 v4 = codec4.decode(input);
/* 463 */           T5 v5 = codec5.decode(input);
/* 464 */           T6 v6 = codec6.decode(input);
/* 465 */           T7 v7 = codec7.decode(input);
/* 466 */           T8 v8 = codec8.decode(input);
/* 467 */           T9 v9 = codec9.decode(input);
/* 468 */           T10 v10 = codec10.decode(input);
/* 469 */           T11 v11 = codec11.decode(input);
/* 470 */           return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11);
/*     */         }
/*     */         
/*     */         public void encode(B output, C value) {
/* 475 */           codec1.encode(output, getter1.apply(value));
/* 476 */           codec2.encode(output, getter2.apply(value));
/* 477 */           codec3.encode(output, getter3.apply(value));
/* 478 */           codec4.encode(output, getter4.apply(value));
/* 479 */           codec5.encode(output, getter5.apply(value));
/* 480 */           codec6.encode(output, getter6.apply(value));
/* 481 */           codec7.encode(output, getter7.apply(value));
/* 482 */           codec8.encode(output, getter8.apply(value));
/* 483 */           codec9.encode(output, getter9.apply(value));
/* 484 */           codec10.encode(output, getter10.apply(value));
/* 485 */           codec11.encode(output, getter11.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> getter2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> getter3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> getter4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> getter5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> getter6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> getter7, final StreamCodec<? super B, T8> codec8, final Function<C, T8> getter8, final StreamCodec<? super B, T9> codec9, final Function<C, T9> getter9, final StreamCodec<? super B, T10> codec10, final Function<C, T10> getter10, final StreamCodec<? super B, T11> codec11, final Function<C, T11> getter11, final StreamCodec<? super B, T12> codec12, final Function<C, T12> getter12, final Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, C> constructor) {
/* 505 */     return new StreamCodec<B, C>() {
/*     */         public C decode(B input) {
/* 508 */           T1 v1 = codec1.decode(input);
/* 509 */           T2 v2 = codec2.decode(input);
/* 510 */           T3 v3 = codec3.decode(input);
/* 511 */           T4 v4 = codec4.decode(input);
/* 512 */           T5 v5 = codec5.decode(input);
/* 513 */           T6 v6 = codec6.decode(input);
/* 514 */           T7 v7 = codec7.decode(input);
/* 515 */           T8 v8 = codec8.decode(input);
/* 516 */           T9 v9 = codec9.decode(input);
/* 517 */           T10 v10 = codec10.decode(input);
/* 518 */           T11 v11 = codec11.decode(input);
/* 519 */           T12 v12 = codec12.decode(input);
/* 520 */           return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12);
/*     */         }
/*     */         
/*     */         public void encode(B output, C value) {
/* 525 */           codec1.encode(output, getter1.apply(value));
/* 526 */           codec2.encode(output, getter2.apply(value));
/* 527 */           codec3.encode(output, getter3.apply(value));
/* 528 */           codec4.encode(output, getter4.apply(value));
/* 529 */           codec5.encode(output, getter5.apply(value));
/* 530 */           codec6.encode(output, getter6.apply(value));
/* 531 */           codec7.encode(output, getter7.apply(value));
/* 532 */           codec8.encode(output, getter8.apply(value));
/* 533 */           codec9.encode(output, getter9.apply(value));
/* 534 */           codec10.encode(output, getter10.apply(value));
/* 535 */           codec11.encode(output, getter11.apply(value));
/* 536 */           codec12.encode(output, getter12.apply(value));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B, T> StreamCodec<B, T> recursive(final UnaryOperator<StreamCodec<B, T>> factory) {
/* 542 */     return new StreamCodec<B, T>() {
/*     */         private final Supplier<StreamCodec<B, T>> inner;
/*     */         
/*     */         public T decode(B input) {
/* 547 */           return ((StreamCodec<B, T>)this.inner.get()).decode(input);
/*     */         }
/*     */         
/*     */         public void encode(B output, T value) {
/* 552 */           ((StreamCodec<B, T>)this.inner.get()).encode(output, value);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default <S extends B> StreamCodec<S, V> cast() {
/* 561 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */