/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, Float> {
/*     */   public Float decode(ByteBuf input) {
/* 168 */     return Float.valueOf(input.readFloat());
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, Float value) {
/* 173 */     output.writeFloat(value.floatValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$9.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */