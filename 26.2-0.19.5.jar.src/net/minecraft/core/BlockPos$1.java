/*    */ package net.minecraft.core;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ class null implements StreamCodec<ByteBuf, BlockPos> {
/*    */   public BlockPos decode(ByteBuf input) {
/* 43 */     return FriendlyByteBuf.readBlockPos(input);
/*    */   }
/*    */   
/*    */   public void encode(ByteBuf output, BlockPos value) {
/* 48 */     FriendlyByteBuf.writeBlockPos(output, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\BlockPos$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */