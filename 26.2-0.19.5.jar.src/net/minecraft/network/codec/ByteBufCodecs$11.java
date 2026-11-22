/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, byte[]> {
/*     */   public byte[] decode(ByteBuf input) {
/* 193 */     return FriendlyByteBuf.readByteArray(input, maxSize);
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, byte[] value) {
/* 198 */     if (value.length > maxSize)
/* 199 */       throw new EncoderException("ByteArray with size " + value.length + " is bigger than allowed " + maxSize); 
/* 201 */     FriendlyByteBuf.writeByteArray(output, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$11.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */