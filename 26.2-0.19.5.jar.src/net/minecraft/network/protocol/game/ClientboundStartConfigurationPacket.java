/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundStartConfigurationPacket implements Packet<ClientGamePacketListener> {
/* 11 */   public static final ClientboundStartConfigurationPacket INSTANCE = new ClientboundStartConfigurationPacket();
/*    */   
/* 12 */   public static final StreamCodec<ByteBuf, ClientboundStartConfigurationPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */   
/*    */   public PacketType<ClientboundStartConfigurationPacket> type() {
/* 19 */     return GamePacketTypes.CLIENTBOUND_START_CONFIGURATION;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 24 */     listener.handleConfigurationStart(this);
/*    */   }
/*    */   
/*    */   public boolean isTerminal() {
/* 29 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundStartConfigurationPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */