/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ class null implements StreamCodec<ByteBuf, ChunkPos> {
/*    */   public ChunkPos decode(ByteBuf input) {
/* 32 */     return FriendlyByteBuf.readChunkPos(input);
/*    */   }
/*    */   
/*    */   public void encode(ByteBuf output, ChunkPos value) {
/* 37 */     FriendlyByteBuf.writeChunkPos(output, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\ChunkPos$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */