/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundContainerClosePacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundContainerClosePacket> STREAM_CODEC = Packet.codec(ServerboundContainerClosePacket::write, ServerboundContainerClosePacket::new);
/*    */   
/*    */   private final int containerId;
/*    */   
/*    */   public ServerboundContainerClosePacket(int containerId) {
/* 14 */     this.containerId = containerId;
/*    */   }
/*    */   
/*    */   private ServerboundContainerClosePacket(FriendlyByteBuf input) {
/* 18 */     this.containerId = input.readContainerId();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeContainerId(this.containerId);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundContainerClosePacket> type() {
/* 27 */     return GamePacketTypes.SERVERBOUND_CONTAINER_CLOSE;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 32 */     listener.handleContainerClose(this);
/*    */   }
/*    */   
/*    */   public int getContainerId() {
/* 36 */     return this.containerId;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundContainerClosePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */