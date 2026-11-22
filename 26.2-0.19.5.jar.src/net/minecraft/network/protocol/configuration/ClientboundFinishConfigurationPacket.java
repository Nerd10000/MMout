/*    */ package net.minecraft.network.protocol.configuration;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundFinishConfigurationPacket implements Packet<ClientConfigurationPacketListener> {
/*  9 */   public static final ClientboundFinishConfigurationPacket INSTANCE = new ClientboundFinishConfigurationPacket();
/*    */   
/* 10 */   public static final StreamCodec<ByteBuf, ClientboundFinishConfigurationPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */   
/*    */   public PacketType<ClientboundFinishConfigurationPacket> type() {
/* 17 */     return ConfigurationPacketTypes.CLIENTBOUND_FINISH_CONFIGURATION;
/*    */   }
/*    */   
/*    */   public void handle(ClientConfigurationPacketListener listener) {
/* 22 */     listener.handleConfigurationFinished(this);
/*    */   }
/*    */   
/*    */   public boolean isTerminal() {
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\configuration\ClientboundFinishConfigurationPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */