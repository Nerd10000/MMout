/*    */ package net.minecraft.core;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ class null implements StreamCodec<ByteBuf, BlockBox> {
/*    */   public BlockBox decode(ByteBuf input) {
/* 14 */     return new BlockBox(
/* 15 */         FriendlyByteBuf.readBlockPos(input), 
/* 16 */         FriendlyByteBuf.readBlockPos(input));
/*    */   }
/*    */   
/*    */   public void encode(ByteBuf output, BlockBox value) {
/* 22 */     FriendlyByteBuf.writeBlockPos(output, value.min());
/* 23 */     FriendlyByteBuf.writeBlockPos(output, value.max());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\BlockBox$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */