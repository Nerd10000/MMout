/*    */ package net.minecraft.network.codec;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ 
/*    */ class null implements StreamCodec<ByteBuf, Short> {
/*    */   public Short decode(ByteBuf input) {
/* 91 */     return Short.valueOf(input.readShort());
/*    */   }
/*    */   
/*    */   public void encode(ByteBuf output, Short value) {
/* 96 */     output.writeShort(value.shortValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */