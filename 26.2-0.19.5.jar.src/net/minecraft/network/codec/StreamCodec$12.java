/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements StreamCodec<B, C> {
/*     */   public C decode(B input) {
/* 259 */     T1 v1 = codec1.decode(input);
/* 260 */     T2 v2 = codec2.decode(input);
/* 261 */     T3 v3 = codec3.decode(input);
/* 262 */     T4 v4 = codec4.decode(input);
/* 263 */     T5 v5 = codec5.decode(input);
/* 264 */     T6 v6 = codec6.decode(input);
/* 265 */     return (C)constructor.apply(v1, v2, v3, v4, v5, v6);
/*     */   }
/*     */   
/*     */   public void encode(B output, C value) {
/* 270 */     codec1.encode(output, getter1.apply(value));
/* 271 */     codec2.encode(output, getter2.apply(value));
/* 272 */     codec3.encode(output, getter3.apply(value));
/* 273 */     codec4.encode(output, getter4.apply(value));
/* 274 */     codec5.encode(output, getter5.apply(value));
/* 275 */     codec6.encode(output, getter6.apply(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$12.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */