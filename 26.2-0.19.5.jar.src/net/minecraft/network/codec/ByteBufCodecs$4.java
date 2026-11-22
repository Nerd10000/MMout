/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, Integer> {
/*     */   public Integer decode(ByteBuf input) {
/* 103 */     return Integer.valueOf(input.readUnsignedShort());
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, Integer value) {
/* 108 */     output.writeShort(value.intValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */