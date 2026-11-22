/*    */ package net.minecraft.network.codec;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ 
/*    */ class null implements StreamCodec<ByteBuf, Boolean> {
/*    */   public Boolean decode(ByteBuf input) {
/* 65 */     return Boolean.valueOf(input.readBoolean());
/*    */   }
/*    */   
/*    */   public void encode(ByteBuf output, Boolean value) {
/* 70 */     output.writeBoolean(value.booleanValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */