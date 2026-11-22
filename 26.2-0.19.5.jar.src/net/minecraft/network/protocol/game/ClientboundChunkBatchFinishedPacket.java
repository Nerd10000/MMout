/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundChunkBatchFinishedPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int batchSize;
/*    */   
/*    */   public ClientboundChunkBatchFinishedPacket(int batchSize) {
/*  8 */     this.batchSize = batchSize;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundChunkBatchFinishedPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundChunkBatchFinishedPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundChunkBatchFinishedPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundChunkBatchFinishedPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundChunkBatchFinishedPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundChunkBatchFinishedPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int batchSize() {
/*  8 */     return this.batchSize;
/*    */   }
/*    */   
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundChunkBatchFinishedPacket> STREAM_CODEC = Packet.codec(ClientboundChunkBatchFinishedPacket::write, ClientboundChunkBatchFinishedPacket::new);
/*    */   
/*    */   private ClientboundChunkBatchFinishedPacket(FriendlyByteBuf input) {
/* 12 */     this(input.readVarInt());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 16 */     output.writeVarInt(this.batchSize);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundChunkBatchFinishedPacket> type() {
/* 21 */     return GamePacketTypes.CLIENTBOUND_CHUNK_BATCH_FINISHED;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 26 */     listener.handleChunkBatchFinished(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundChunkBatchFinishedPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */