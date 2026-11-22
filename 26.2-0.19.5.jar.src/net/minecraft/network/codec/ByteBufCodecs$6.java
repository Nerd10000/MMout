/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, Integer> {
/*     */   public Integer decode(ByteBuf input) {
/* 127 */     return Integer.valueOf(VarInt.read(input));
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, Integer value) {
/* 132 */     VarInt.write(output, value.intValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$6.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */