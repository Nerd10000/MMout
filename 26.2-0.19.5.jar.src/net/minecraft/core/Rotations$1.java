/*    */ package net.minecraft.core;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ class null implements StreamCodec<ByteBuf, Rotations> {
/*    */   public Rotations decode(ByteBuf input) {
/* 24 */     return new Rotations(input
/* 25 */         .readFloat(), input
/* 26 */         .readFloat(), input
/* 27 */         .readFloat());
/*    */   }
/*    */   
/*    */   public void encode(ByteBuf output, Rotations value) {
/* 33 */     output.writeFloat(value.x);
/* 34 */     output.writeFloat(value.y);
/* 35 */     output.writeFloat(value.z);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\Rotations$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */