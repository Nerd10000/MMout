/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, Integer> {
/*     */   public Integer decode(ByteBuf input) {
/* 393 */     return Integer.valueOf(FriendlyByteBuf.readContainerId(input));
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, Integer value) {
/* 398 */     FriendlyByteBuf.writeContainerId(output, value.intValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$22.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */