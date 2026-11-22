/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements StreamCodec<B, C> {
/*     */   public C decode(B input) {
/* 175 */     T1 v1 = codec1.decode(input);
/* 176 */     T2 v2 = codec2.decode(input);
/* 177 */     T3 v3 = codec3.decode(input);
/* 178 */     return (C)constructor.apply(v1, v2, v3);
/*     */   }
/*     */   
/*     */   public void encode(B output, C value) {
/* 183 */     codec1.encode(output, getter1.apply(value));
/* 184 */     codec2.encode(output, getter2.apply(value));
/* 185 */     codec3.encode(output, getter3.apply(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$9.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */