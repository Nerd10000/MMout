/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ServerboundChunkBatchReceivedPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final float desiredChunksPerTick;
/*    */   
/*    */   public ServerboundChunkBatchReceivedPacket(float desiredChunksPerTick) {
/*  8 */     this.desiredChunksPerTick = desiredChunksPerTick;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundChunkBatchReceivedPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChunkBatchReceivedPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundChunkBatchReceivedPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChunkBatchReceivedPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundChunkBatchReceivedPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundChunkBatchReceivedPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float desiredChunksPerTick() {
/*  8 */     return this.desiredChunksPerTick;
/*    */   }
/*    */   
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundChunkBatchReceivedPacket> STREAM_CODEC = Packet.codec(ServerboundChunkBatchReceivedPacket::write, ServerboundChunkBatchReceivedPacket::new);
/*    */   
/*    */   private ServerboundChunkBatchReceivedPacket(FriendlyByteBuf input) {
/* 12 */     this(input.readFloat());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 16 */     output.writeFloat(this.desiredChunksPerTick);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundChunkBatchReceivedPacket> type() {
/* 21 */     return GamePacketTypes.SERVERBOUND_CHUNK_BATCH_RECEIVED;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 26 */     listener.handleChunkBatchReceived(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundChunkBatchReceivedPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */