/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function11;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements StreamCodec<B, C> {
/*     */   public C decode(B input) {
/* 459 */     T1 v1 = codec1.decode(input);
/* 460 */     T2 v2 = codec2.decode(input);
/* 461 */     T3 v3 = codec3.decode(input);
/* 462 */     T4 v4 = codec4.decode(input);
/* 463 */     T5 v5 = codec5.decode(input);
/* 464 */     T6 v6 = codec6.decode(input);
/* 465 */     T7 v7 = codec7.decode(input);
/* 466 */     T8 v8 = codec8.decode(input);
/* 467 */     T9 v9 = codec9.decode(input);
/* 468 */     T10 v10 = codec10.decode(input);
/* 469 */     T11 v11 = codec11.decode(input);
/* 470 */     return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11);
/*     */   }
/*     */   
/*     */   public void encode(B output, C value) {
/* 475 */     codec1.encode(output, getter1.apply(value));
/* 476 */     codec2.encode(output, getter2.apply(value));
/* 477 */     codec3.encode(output, getter3.apply(value));
/* 478 */     codec4.encode(output, getter4.apply(value));
/* 479 */     codec5.encode(output, getter5.apply(value));
/* 480 */     codec6.encode(output, getter6.apply(value));
/* 481 */     codec7.encode(output, getter7.apply(value));
/* 482 */     codec8.encode(output, getter8.apply(value));
/* 483 */     codec9.encode(output, getter9.apply(value));
/* 484 */     codec10.encode(output, getter10.apply(value));
/* 485 */     codec11.encode(output, getter11.apply(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$17.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */