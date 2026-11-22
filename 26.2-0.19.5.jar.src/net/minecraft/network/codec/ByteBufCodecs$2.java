/*    */ package net.minecraft.network.codec;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ 
/*    */ class null implements StreamCodec<ByteBuf, Byte> {
/*    */   public Byte decode(ByteBuf input) {
/* 77 */     return Byte.valueOf(input.readByte());
/*    */   }
/*    */   
/*    */   public void encode(ByteBuf output, Byte value) {
/* 82 */     output.writeByte(value.byteValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */