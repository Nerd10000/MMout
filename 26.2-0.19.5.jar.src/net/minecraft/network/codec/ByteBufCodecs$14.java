/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.network.Utf8String;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, String> {
/*     */   public String decode(ByteBuf input) {
/* 234 */     return Utf8String.read(input, maxStringLength);
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, String value) {
/* 239 */     Utf8String.write(output, value, maxStringLength);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$14.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */