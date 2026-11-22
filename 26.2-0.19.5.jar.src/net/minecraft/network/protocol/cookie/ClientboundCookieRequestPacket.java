/*    */ package net.minecraft.network.protocol.cookie;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ClientboundCookieRequestPacket extends Record implements Packet<ClientCookiePacketListener> {
/*    */   private final Identifier key;
/*    */   
/*    */   public ClientboundCookieRequestPacket(Identifier key) {
/*  9 */     this.key = key;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/cookie/ClientboundCookieRequestPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/cookie/ClientboundCookieRequestPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/cookie/ClientboundCookieRequestPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/cookie/ClientboundCookieRequestPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/cookie/ClientboundCookieRequestPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/cookie/ClientboundCookieRequestPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier key() {
/*  9 */     return this.key;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundCookieRequestPacket> STREAM_CODEC = Packet.codec(ClientboundCookieRequestPacket::write, ClientboundCookieRequestPacket::new);
/*    */   
/*    */   private ClientboundCookieRequestPacket(FriendlyByteBuf input) {
/* 15 */     this(input.readIdentifier());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 19 */     output.writeIdentifier(this.key);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundCookieRequestPacket> type() {
/* 24 */     return CookiePacketTypes.CLIENTBOUND_COOKIE_REQUEST;
/*    */   }
/*    */   
/*    */   public void handle(ClientCookiePacketListener listener) {
/* 29 */     listener.handleRequestCookie(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\cookie\ClientboundCookieRequestPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */