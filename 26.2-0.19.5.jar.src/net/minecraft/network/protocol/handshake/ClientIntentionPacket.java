/*    */ package net.minecraft.network.protocol.handshake;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientIntentionPacket extends Record implements Packet<ServerHandshakePacketListener> {
/*    */   private final int protocolVersion;
/*    */   
/*    */   private final String hostName;
/*    */   
/*    */   private final int port;
/*    */   
/*    */   private final ClientIntent intention;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int protocolVersion() {
/* 11 */     return this.protocolVersion;
/*    */   }
/*    */   
/*    */   public String hostName() {
/* 11 */     return this.hostName;
/*    */   }
/*    */   
/*    */   public int port() {
/* 11 */     return this.port;
/*    */   }
/*    */   
/*    */   public ClientIntent intention() {
/* 11 */     return this.intention;
/*    */   }
/*    */   
/* 17 */   public static final StreamCodec<FriendlyByteBuf, ClientIntentionPacket> STREAM_CODEC = Packet.codec(ClientIntentionPacket::write, ClientIntentionPacket::new);
/*    */   
/*    */   private static final int MAX_HOST_LENGTH = 255;
/*    */   
/*    */   @Deprecated
/*    */   public ClientIntentionPacket(int protocolVersion, String hostName, int port, ClientIntent intention) {
/* 23 */     this.protocolVersion = protocolVersion;
/* 23 */     this.hostName = hostName;
/* 23 */     this.port = port;
/* 23 */     this.intention = intention;
/*    */   }
/*    */   
/*    */   private ClientIntentionPacket(FriendlyByteBuf input) {
/* 27 */     this(input
/* 28 */         .readVarInt(), input
/* 29 */         .readUtf(255), input
/* 30 */         .readUnsignedShort(), 
/* 31 */         ClientIntent.byId(input.readVarInt()));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 36 */     output.writeVarInt(this.protocolVersion);
/* 37 */     output.writeUtf(this.hostName);
/* 38 */     output.writeShort(this.port);
/* 39 */     output.writeVarInt(this.intention.id());
/*    */   }
/*    */   
/*    */   public PacketType<ClientIntentionPacket> type() {
/* 44 */     return HandshakePacketTypes.CLIENT_INTENTION;
/*    */   }
/*    */   
/*    */   public void handle(ServerHandshakePacketListener listener) {
/* 49 */     listener.handleIntention(this);
/*    */   }
/*    */   
/*    */   public boolean isTerminal() {
/* 54 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\handshake\ClientIntentionPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */