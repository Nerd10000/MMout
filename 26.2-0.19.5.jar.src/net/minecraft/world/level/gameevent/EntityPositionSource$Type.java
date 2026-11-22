/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class Type implements PositionSourceType<EntityPositionSource> {
/*    */   public MapCodec<EntityPositionSource> codec() {
/* 94 */     return EntityPositionSource.CODEC;
/*    */   }
/*    */   
/*    */   public StreamCodec<ByteBuf, EntityPositionSource> streamCodec() {
/* 99 */     return EntityPositionSource.STREAM_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\EntityPositionSource$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */