/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, Integer> {
/*     */   public Integer decode(ByteBuf input) {
/* 115 */     return Integer.valueOf(input.readInt());
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, Integer value) {
/* 120 */     output.writeInt(value.intValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$5.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */