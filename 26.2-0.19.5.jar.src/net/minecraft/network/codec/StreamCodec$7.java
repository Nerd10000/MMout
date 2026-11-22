/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements StreamCodec<B, C> {
/*     */   public C decode(B input) {
/* 134 */     T1 v1 = codec1.decode(input);
/* 135 */     return constructor.apply(v1);
/*     */   }
/*     */   
/*     */   public void encode(B output, C value) {
/* 140 */     codec1.encode(output, getter1.apply(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$7.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */