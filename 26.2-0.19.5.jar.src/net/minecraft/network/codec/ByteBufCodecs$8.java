/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.network.VarLong;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, Long> {
/*     */   public Long decode(ByteBuf input) {
/* 156 */     return Long.valueOf(VarLong.read(input));
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, Long value) {
/* 161 */     VarLong.write(output, value.longValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$8.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */