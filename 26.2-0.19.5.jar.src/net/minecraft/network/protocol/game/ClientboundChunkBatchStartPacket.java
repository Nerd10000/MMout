/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundChunkBatchStartPacket implements Packet<ClientGamePacketListener> {
/*  9 */   public static final ClientboundChunkBatchStartPacket INSTANCE = new ClientboundChunkBatchStartPacket();
/*    */   
/* 10 */   public static final StreamCodec<ByteBuf, ClientboundChunkBatchStartPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */   
/*    */   public PacketType<ClientboundChunkBatchStartPacket> type() {
/* 17 */     return GamePacketTypes.CLIENTBOUND_CHUNK_BATCH_START;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 22 */     listener.handleChunkBatchStart(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundChunkBatchStartPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */