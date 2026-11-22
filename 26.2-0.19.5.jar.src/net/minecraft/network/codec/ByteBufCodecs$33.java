/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, Integer> {
/*     */   public Integer decode(ByteBuf input) {
/* 732 */     return Integer.valueOf(ARGB.color(input
/* 733 */           .readByte() & 0xFF, input
/* 734 */           .readByte() & 0xFF, input
/* 735 */           .readByte() & 0xFF));
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, Integer value) {
/* 741 */     output.writeByte(ARGB.red(value.intValue()));
/* 742 */     output.writeByte(ARGB.green(value.intValue()));
/* 743 */     output.writeByte(ARGB.blue(value.intValue()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$33.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */