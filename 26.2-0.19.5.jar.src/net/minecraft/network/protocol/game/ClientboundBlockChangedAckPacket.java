/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundBlockChangedAckPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int sequence;
/*    */   
/*    */   public ClientboundBlockChangedAckPacket(int sequence) {
/*  8 */     this.sequence = sequence;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundBlockChangedAckPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundBlockChangedAckPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundBlockChangedAckPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundBlockChangedAckPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundBlockChangedAckPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundBlockChangedAckPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int sequence() {
/*  8 */     return this.sequence;
/*    */   }
/*    */   
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundBlockChangedAckPacket> STREAM_CODEC = Packet.codec(ClientboundBlockChangedAckPacket::write, ClientboundBlockChangedAckPacket::new);
/*    */   
/*    */   private ClientboundBlockChangedAckPacket(FriendlyByteBuf input) {
/* 12 */     this(input.readVarInt());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 16 */     output.writeVarInt(this.sequence);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundBlockChangedAckPacket> type() {
/* 21 */     return GamePacketTypes.CLIENTBOUND_BLOCK_CHANGED_ACK;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 26 */     listener.handleBlockChangedAck(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundBlockChangedAckPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */