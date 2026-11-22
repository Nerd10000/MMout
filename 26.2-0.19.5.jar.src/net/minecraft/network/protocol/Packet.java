/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.codec.StreamMemberEncoder;
/*    */ 
/*    */ public interface Packet<T extends net.minecraft.network.PacketListener> {
/*    */   PacketType<? extends Packet<T>> type();
/*    */   
/*    */   void handle(T paramT);
/*    */   
/*    */   default boolean isSkippable() {
/* 19 */     return false;
/*    */   }
/*    */   
/*    */   default boolean isTerminal() {
/* 26 */     return false;
/*    */   }
/*    */   
/*    */   static <B extends io.netty.buffer.ByteBuf, T extends Packet<?>> StreamCodec<B, T> codec(StreamMemberEncoder<B, T> writer, StreamDecoder<B, T> reader) {
/* 30 */     return StreamCodec.ofMember(writer, reader);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\Packet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */