/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements StreamCodec<B, C> {
/*     */   public C decode(B input) {
/* 370 */     T1 v1 = codec1.decode(input);
/* 371 */     T2 v2 = codec2.decode(input);
/* 372 */     T3 v3 = codec3.decode(input);
/* 373 */     T4 v4 = codec4.decode(input);
/* 374 */     T5 v5 = codec5.decode(input);
/* 375 */     T6 v6 = codec6.decode(input);
/* 376 */     T7 v7 = codec7.decode(input);
/* 377 */     T8 v8 = codec8.decode(input);
/* 378 */     T9 v9 = codec9.decode(input);
/* 379 */     return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9);
/*     */   }
/*     */   
/*     */   public void encode(B output, C value) {
/* 384 */     codec1.encode(output, getter1.apply(value));
/* 385 */     codec2.encode(output, getter2.apply(value));
/* 386 */     codec3.encode(output, getter3.apply(value));
/* 387 */     codec4.encode(output, getter4.apply(value));
/* 388 */     codec5.encode(output, getter5.apply(value));
/* 389 */     codec6.encode(output, getter6.apply(value));
/* 390 */     codec7.encode(output, getter7.apply(value));
/* 391 */     codec8.encode(output, getter8.apply(value));
/* 392 */     codec9.encode(output, getter9.apply(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$15.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */