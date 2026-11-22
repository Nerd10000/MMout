/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundTransferPacket extends Record implements Packet<ClientCommonPacketListener> {
/*    */   private final String host;
/*    */   
/*    */   private final int port;
/*    */   
/*    */   public ClientboundTransferPacket(String host, int port) {
/*  8 */     this.host = host;
/*  8 */     this.port = port;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundTransferPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundTransferPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundTransferPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundTransferPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundTransferPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundTransferPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String host() {
/*  8 */     return this.host;
/*    */   }
/*    */   
/*    */   public int port() {
/*  8 */     return this.port;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundTransferPacket> STREAM_CODEC = Packet.codec(ClientboundTransferPacket::write, ClientboundTransferPacket::new);
/*    */   
/*    */   private ClientboundTransferPacket(FriendlyByteBuf input) {
/* 15 */     this(input.readUtf(), input.readVarInt());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 19 */     output.writeUtf(this.host);
/* 20 */     output.writeVarInt(this.port);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundTransferPacket> type() {
/* 25 */     return CommonPacketTypes.CLIENTBOUND_TRANSFER;
/*    */   }
/*    */   
/*    */   public void handle(ClientCommonPacketListener listener) {
/* 30 */     listener.handleTransfer(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\ClientboundTransferPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */