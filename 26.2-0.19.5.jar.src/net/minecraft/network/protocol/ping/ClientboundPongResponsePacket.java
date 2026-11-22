/*    */ package net.minecraft.network.protocol.ping;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundPongResponsePacket extends Record implements Packet<ClientPongPacketListener> {
/*    */   private final long time;
/*    */   
/*    */   public ClientboundPongResponsePacket(long time) {
/*  8 */     this.time = time;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/ping/ClientboundPongResponsePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/ping/ClientboundPongResponsePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/ping/ClientboundPongResponsePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/ping/ClientboundPongResponsePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/ping/ClientboundPongResponsePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/ping/ClientboundPongResponsePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public long time() {
/*  8 */     return this.time;
/*    */   }
/*    */   
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundPongResponsePacket> STREAM_CODEC = Packet.codec(ClientboundPongResponsePacket::write, ClientboundPongResponsePacket::new);
/*    */   
/*    */   private ClientboundPongResponsePacket(FriendlyByteBuf input) {
/* 12 */     this(input.readLong());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 16 */     output.writeLong(this.time);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundPongResponsePacket> type() {
/* 21 */     return PingPacketTypes.CLIENTBOUND_PONG_RESPONSE;
/*    */   }
/*    */   
/*    */   public void handle(ClientPongPacketListener listener) {
/* 26 */     listener.handlePongResponse(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\ping\ClientboundPongResponsePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */