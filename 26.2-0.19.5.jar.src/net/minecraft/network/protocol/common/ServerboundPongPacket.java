/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundPongPacket implements Packet<ServerCommonPacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundPongPacket> STREAM_CODEC = Packet.codec(ServerboundPongPacket::write, ServerboundPongPacket::new);
/*    */   
/*    */   private final int id;
/*    */   
/*    */   public ServerboundPongPacket(int id) {
/* 15 */     this.id = id;
/*    */   }
/*    */   
/*    */   private ServerboundPongPacket(FriendlyByteBuf input) {
/* 19 */     this.id = input.readInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 23 */     output.writeInt(this.id);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundPongPacket> type() {
/* 28 */     return CommonPacketTypes.SERVERBOUND_PONG;
/*    */   }
/*    */   
/*    */   public void handle(ServerCommonPacketListener listener) {
/* 33 */     listener.handlePong(this);
/*    */   }
/*    */   
/*    */   public int getId() {
/* 37 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\ServerboundPongPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */