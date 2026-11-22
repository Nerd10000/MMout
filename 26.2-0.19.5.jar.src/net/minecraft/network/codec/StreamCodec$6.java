/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements StreamCodec<B, U> {
/*     */   public U decode(B input) {
/* 110 */     V key = StreamCodec.this.decode(input);
/* 111 */     StreamCodec<? super B, ? extends U> valueCodec = codec.apply(key);
/* 112 */     return valueCodec.decode(input);
/*     */   }
/*     */   
/*     */   public void encode(B output, U value) {
/* 117 */     V key = type.apply(value);
/* 119 */     StreamCodec<B, U> valueCodec = codec.apply(key);
/* 120 */     StreamCodec.this.encode(output, key);
/* 121 */     valueCodec.encode(output, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$6.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */