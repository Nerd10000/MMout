/*    */ package net.minecraft.core;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ class null implements StreamCodec<ByteBuf, UUID> {
/*    */   public UUID decode(ByteBuf input) {
/* 64 */     return FriendlyByteBuf.readUUID(input);
/*    */   }
/*    */   
/*    */   public void encode(ByteBuf output, UUID value) {
/* 69 */     FriendlyByteBuf.writeUUID(output, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\UUIDUtil$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */