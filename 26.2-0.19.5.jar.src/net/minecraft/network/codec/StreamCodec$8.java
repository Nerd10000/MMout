/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements StreamCodec<B, C> {
/*     */   public C decode(B input) {
/* 153 */     T1 v1 = codec1.decode(input);
/* 154 */     T2 v2 = codec2.decode(input);
/* 155 */     return constructor.apply(v1, v2);
/*     */   }
/*     */   
/*     */   public void encode(B output, C value) {
/* 160 */     codec1.encode(output, getter1.apply(value));
/* 161 */     codec2.encode(output, getter2.apply(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$8.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */