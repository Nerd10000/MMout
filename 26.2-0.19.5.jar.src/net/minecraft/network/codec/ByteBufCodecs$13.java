/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, long[]> {
/*     */   public long[] decode(ByteBuf input) {
/* 221 */     return FriendlyByteBuf.readLongArray(input);
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, long[] value) {
/* 226 */     FriendlyByteBuf.writeLongArray(output, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$13.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */