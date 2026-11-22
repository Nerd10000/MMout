/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, Double> {
/*     */   public Double decode(ByteBuf input) {
/* 180 */     return Double.valueOf(input.readDouble());
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, Double value) {
/* 185 */     output.writeDouble(value.doubleValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$10.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */