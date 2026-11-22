/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public final class ClientboundForgetLevelChunkPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final ChunkPos pos;
/*    */   
/*    */   public ClientboundForgetLevelChunkPacket(ChunkPos pos) {
/*  9 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ChunkPos pos() {
/*  9 */     return this.pos;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundForgetLevelChunkPacket> STREAM_CODEC = Packet.codec(ClientboundForgetLevelChunkPacket::write, ClientboundForgetLevelChunkPacket::new);
/*    */   
/*    */   private ClientboundForgetLevelChunkPacket(FriendlyByteBuf input) {
/* 13 */     this(input.readChunkPos());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 17 */     output.writeChunkPos(this.pos);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundForgetLevelChunkPacket> type() {
/* 22 */     return GamePacketTypes.CLIENTBOUND_FORGET_LEVEL_CHUNK;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 27 */     listener.handleForgetLevelChunk(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundForgetLevelChunkPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */