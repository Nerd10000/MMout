/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ServerboundChatAckPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int offset;
/*    */   
/*    */   public ServerboundChatAckPacket(int offset) {
/*  8 */     this.offset = offset;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundChatAckPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatAckPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundChatAckPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatAckPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundChatAckPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatAckPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int offset() {
/*  8 */     return this.offset;
/*    */   }
/*    */   
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundChatAckPacket> STREAM_CODEC = Packet.codec(ServerboundChatAckPacket::write, ServerboundChatAckPacket::new);
/*    */   
/*    */   private ServerboundChatAckPacket(FriendlyByteBuf input) {
/* 12 */     this(input.readVarInt());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 16 */     output.writeVarInt(this.offset);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundChatAckPacket> type() {
/* 21 */     return GamePacketTypes.SERVERBOUND_CHAT_ACK;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 26 */     listener.handleChatAck(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundChatAckPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */