/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundStatusRequestPacket implements Packet<ServerStatusPacketListener> {
/*  9 */   public static final ServerboundStatusRequestPacket INSTANCE = new ServerboundStatusRequestPacket();
/*    */   
/* 10 */   public static final StreamCodec<ByteBuf, ServerboundStatusRequestPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */   
/*    */   public PacketType<ServerboundStatusRequestPacket> type() {
/* 17 */     return StatusPacketTypes.SERVERBOUND_STATUS_REQUEST;
/*    */   }
/*    */   
/*    */   public void handle(ServerStatusPacketListener listener) {
/* 22 */     listener.handleStatusRequest(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\status\ServerboundStatusRequestPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */