/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, Long> {
/*     */   public Long decode(ByteBuf input) {
/* 144 */     return Long.valueOf(input.readLong());
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, Long value) {
/* 149 */     output.writeLong(value.longValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$7.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */