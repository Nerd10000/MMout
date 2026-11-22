/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function12;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements StreamCodec<B, C> {
/*     */   public C decode(B input) {
/* 508 */     T1 v1 = codec1.decode(input);
/* 509 */     T2 v2 = codec2.decode(input);
/* 510 */     T3 v3 = codec3.decode(input);
/* 511 */     T4 v4 = codec4.decode(input);
/* 512 */     T5 v5 = codec5.decode(input);
/* 513 */     T6 v6 = codec6.decode(input);
/* 514 */     T7 v7 = codec7.decode(input);
/* 515 */     T8 v8 = codec8.decode(input);
/* 516 */     T9 v9 = codec9.decode(input);
/* 517 */     T10 v10 = codec10.decode(input);
/* 518 */     T11 v11 = codec11.decode(input);
/* 519 */     T12 v12 = codec12.decode(input);
/* 520 */     return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12);
/*     */   }
/*     */   
/*     */   public void encode(B output, C value) {
/* 525 */     codec1.encode(output, getter1.apply(value));
/* 526 */     codec2.encode(output, getter2.apply(value));
/* 527 */     codec3.encode(output, getter3.apply(value));
/* 528 */     codec4.encode(output, getter4.apply(value));
/* 529 */     codec5.encode(output, getter5.apply(value));
/* 530 */     codec6.encode(output, getter6.apply(value));
/* 531 */     codec7.encode(output, getter7.apply(value));
/* 532 */     codec8.encode(output, getter8.apply(value));
/* 533 */     codec9.encode(output, getter9.apply(value));
/* 534 */     codec10.encode(output, getter10.apply(value));
/* 535 */     codec11.encode(output, getter11.apply(value));
/* 536 */     codec12.encode(output, getter12.apply(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$18.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */