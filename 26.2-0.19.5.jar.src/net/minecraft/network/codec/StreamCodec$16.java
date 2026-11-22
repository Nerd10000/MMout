/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements StreamCodec<B, C> {
/*     */   public C decode(B input) {
/* 413 */     T1 v1 = codec1.decode(input);
/* 414 */     T2 v2 = codec2.decode(input);
/* 415 */     T3 v3 = codec3.decode(input);
/* 416 */     T4 v4 = codec4.decode(input);
/* 417 */     T5 v5 = codec5.decode(input);
/* 418 */     T6 v6 = codec6.decode(input);
/* 419 */     T7 v7 = codec7.decode(input);
/* 420 */     T8 v8 = codec8.decode(input);
/* 421 */     T9 v9 = codec9.decode(input);
/* 422 */     T10 v10 = codec10.decode(input);
/* 423 */     return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);
/*     */   }
/*     */   
/*     */   public void encode(B output, C value) {
/* 428 */     codec1.encode(output, getter1.apply(value));
/* 429 */     codec2.encode(output, getter2.apply(value));
/* 430 */     codec3.encode(output, getter3.apply(value));
/* 431 */     codec4.encode(output, getter4.apply(value));
/* 432 */     codec5.encode(output, getter5.apply(value));
/* 433 */     codec6.encode(output, getter6.apply(value));
/* 434 */     codec7.encode(output, getter7.apply(value));
/* 435 */     codec8.encode(output, getter8.apply(value));
/* 436 */     codec9.encode(output, getter9.apply(value));
/* 437 */     codec10.encode(output, getter10.apply(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$16.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */