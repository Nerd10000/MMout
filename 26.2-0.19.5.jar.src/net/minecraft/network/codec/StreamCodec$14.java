/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements StreamCodec<B, C> {
/*     */   public C decode(B input) {
/* 330 */     T1 v1 = codec1.decode(input);
/* 331 */     T2 v2 = codec2.decode(input);
/* 332 */     T3 v3 = codec3.decode(input);
/* 333 */     T4 v4 = codec4.decode(input);
/* 334 */     T5 v5 = codec5.decode(input);
/* 335 */     T6 v6 = codec6.decode(input);
/* 336 */     T7 v7 = codec7.decode(input);
/* 337 */     T8 v8 = codec8.decode(input);
/* 338 */     return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8);
/*     */   }
/*     */   
/*     */   public void encode(B output, C value) {
/* 343 */     codec1.encode(output, getter1.apply(value));
/* 344 */     codec2.encode(output, getter2.apply(value));
/* 345 */     codec3.encode(output, getter3.apply(value));
/* 346 */     codec4.encode(output, getter4.apply(value));
/* 347 */     codec5.encode(output, getter5.apply(value));
/* 348 */     codec6.encode(output, getter6.apply(value));
/* 349 */     codec7.encode(output, getter7.apply(value));
/* 350 */     codec8.encode(output, getter8.apply(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$14.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */