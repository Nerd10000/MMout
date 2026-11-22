/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements StreamCodec<B, C> {
/*     */   public C decode(B input) {
/* 293 */     T1 v1 = codec1.decode(input);
/* 294 */     T2 v2 = codec2.decode(input);
/* 295 */     T3 v3 = codec3.decode(input);
/* 296 */     T4 v4 = codec4.decode(input);
/* 297 */     T5 v5 = codec5.decode(input);
/* 298 */     T6 v6 = codec6.decode(input);
/* 299 */     T7 v7 = codec7.decode(input);
/* 300 */     return (C)constructor.apply(v1, v2, v3, v4, v5, v6, v7);
/*     */   }
/*     */   
/*     */   public void encode(B output, C value) {
/* 305 */     codec1.encode(output, getter1.apply(value));
/* 306 */     codec2.encode(output, getter2.apply(value));
/* 307 */     codec3.encode(output, getter3.apply(value));
/* 308 */     codec4.encode(output, getter4.apply(value));
/* 309 */     codec5.encode(output, getter5.apply(value));
/* 310 */     codec6.encode(output, getter6.apply(value));
/* 311 */     codec7.encode(output, getter7.apply(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$13.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */