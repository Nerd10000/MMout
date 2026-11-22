/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements StreamCodec<B, C> {
/*     */   public C decode(B input) {
/* 228 */     T1 v1 = codec1.decode(input);
/* 229 */     T2 v2 = codec2.decode(input);
/* 230 */     T3 v3 = codec3.decode(input);
/* 231 */     T4 v4 = codec4.decode(input);
/* 232 */     T5 v5 = codec5.decode(input);
/* 233 */     return (C)constructor.apply(v1, v2, v3, v4, v5);
/*     */   }
/*     */   
/*     */   public void encode(B output, C value) {
/* 238 */     codec1.encode(output, getter1.apply(value));
/* 239 */     codec2.encode(output, getter2.apply(value));
/* 240 */     codec3.encode(output, getter3.apply(value));
/* 241 */     codec4.encode(output, getter4.apply(value));
/* 242 */     codec5.encode(output, getter5.apply(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$11.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */