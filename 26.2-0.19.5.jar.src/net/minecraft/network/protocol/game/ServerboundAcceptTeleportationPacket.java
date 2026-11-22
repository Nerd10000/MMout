/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundAcceptTeleportationPacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundAcceptTeleportationPacket> STREAM_CODEC = Packet.codec(ServerboundAcceptTeleportationPacket::write, ServerboundAcceptTeleportationPacket::new);
/*    */   
/*    */   private final int id;
/*    */   
/*    */   public ServerboundAcceptTeleportationPacket(int id) {
/* 14 */     this.id = id;
/*    */   }
/*    */   
/*    */   private ServerboundAcceptTeleportationPacket(FriendlyByteBuf input) {
/* 18 */     this.id = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeVarInt(this.id);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundAcceptTeleportationPacket> type() {
/* 27 */     return GamePacketTypes.SERVERBOUND_ACCEPT_TELEPORTATION;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 32 */     listener.handleAcceptTeleportPacket(this);
/*    */   }
/*    */   
/*    */   public int getId() {
/* 36 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundAcceptTeleportationPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */