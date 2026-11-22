/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements StreamCodec<B, C> {
/*     */   public C decode(B input) {
/* 200 */     T1 v1 = codec1.decode(input);
/* 201 */     T2 v2 = codec2.decode(input);
/* 202 */     T3 v3 = codec3.decode(input);
/* 203 */     T4 v4 = codec4.decode(input);
/* 204 */     return (C)constructor.apply(v1, v2, v3, v4);
/*     */   }
/*     */   
/*     */   public void encode(B output, C value) {
/* 209 */     codec1.encode(output, getter1.apply(value));
/* 210 */     codec2.encode(output, getter2.apply(value));
/* 211 */     codec3.encode(output, getter3.apply(value));
/* 212 */     codec4.encode(output, getter4.apply(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$10.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */