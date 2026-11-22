/*    */ package net.minecraft.network.protocol.cookie;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.codec.StreamEncoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.network.protocol.common.ClientboundStoreCookiePacket;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ServerboundCookieResponsePacket extends Record implements Packet<ServerCookiePacketListener> {
/*    */   private final Identifier key;
/*    */   
/*    */   private final byte[] payload;
/*    */   
/*    */   public ServerboundCookieResponsePacket(Identifier key, byte[] payload) {
/* 11 */     this.key = key;
/* 11 */     this.payload = payload;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier key() {
/* 11 */     return this.key;
/*    */   }
/*    */   
/*    */   public byte[] payload() {
/* 11 */     return this.payload;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ServerboundCookieResponsePacket> STREAM_CODEC = Packet.codec(ServerboundCookieResponsePacket::write, ServerboundCookieResponsePacket::new);
/*    */   
/*    */   private ServerboundCookieResponsePacket(FriendlyByteBuf input) {
/* 15 */     this(input.readIdentifier(), (byte[])input.readNullable((StreamDecoder)ClientboundStoreCookiePacket.PAYLOAD_STREAM_CODEC));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 19 */     output.writeIdentifier(this.key);
/* 20 */     output.writeNullable(this.payload, (StreamEncoder)ClientboundStoreCookiePacket.PAYLOAD_STREAM_CODEC);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundCookieResponsePacket> type() {
/* 25 */     return CookiePacketTypes.SERVERBOUND_COOKIE_RESPONSE;
/*    */   }
/*    */   
/*    */   public void handle(ServerCookiePacketListener listener) {
/* 30 */     listener.handleCookieResponse(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\cookie\ServerboundCookieResponsePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */