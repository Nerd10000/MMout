/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundConfigurationAcknowledgedPacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final ServerboundConfigurationAcknowledgedPacket INSTANCE = new ServerboundConfigurationAcknowledgedPacket();
/*    */   
/* 10 */   public static final StreamCodec<ByteBuf, ServerboundConfigurationAcknowledgedPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */   
/*    */   public PacketType<ServerboundConfigurationAcknowledgedPacket> type() {
/* 17 */     return GamePacketTypes.SERVERBOUND_CONFIGURATION_ACKNOWLEDGED;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 22 */     listener.handleConfigurationAcknowledged(this);
/*    */   }
/*    */   
/*    */   public boolean isTerminal() {
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundConfigurationAcknowledgedPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */