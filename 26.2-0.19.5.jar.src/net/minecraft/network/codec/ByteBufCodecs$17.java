/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ 
/*     */ class null implements StreamCodec<B, V> {
/*     */   public V decode(B input) {
/* 311 */     T payload = original.decode(input);
/* 312 */     return (V)codec.parse(ops, payload).getOrThrow(msg -> new DecoderException("Failed to decode: " + msg + " " + String.valueOf(payload)));
/*     */   }
/*     */   
/*     */   public void encode(B output, V value) {
/* 317 */     T payload = (T)codec.encodeStart(ops, value).getOrThrow(msg -> new EncoderException("Failed to encode: " + msg + " " + String.valueOf(value)));
/* 318 */     original.encode(output, payload);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$17.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */