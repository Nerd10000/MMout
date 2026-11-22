/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class Type implements PositionSourceType<BlockPositionSource> {
/*    */   public MapCodec<BlockPositionSource> codec() {
/* 36 */     return BlockPositionSource.CODEC;
/*    */   }
/*    */   
/*    */   public StreamCodec<ByteBuf, BlockPositionSource> streamCodec() {
/* 41 */     return BlockPositionSource.STREAM_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\BlockPositionSource$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */