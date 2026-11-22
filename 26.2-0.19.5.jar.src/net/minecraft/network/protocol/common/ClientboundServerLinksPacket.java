/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.server.ServerLinks;
/*    */ 
/*    */ public final class ClientboundServerLinksPacket extends Record implements Packet<ClientCommonPacketListener> {
/*    */   private final List<ServerLinks.UntrustedEntry> links;
/*    */   
/*    */   public ClientboundServerLinksPacket(List<ServerLinks.UntrustedEntry> links) {
/* 11 */     this.links = links;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundServerLinksPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundServerLinksPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundServerLinksPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundServerLinksPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundServerLinksPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundServerLinksPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<ServerLinks.UntrustedEntry> links() {
/* 11 */     return this.links;
/*    */   }
/*    */   
/* 15 */   public static final StreamCodec<ByteBuf, ClientboundServerLinksPacket> STREAM_CODEC = StreamCodec.composite(ServerLinks.UNTRUSTED_LINKS_STREAM_CODEC, ClientboundServerLinksPacket::links, ClientboundServerLinksPacket::new);
/*    */   
/*    */   public PacketType<ClientboundServerLinksPacket> type() {
/* 22 */     return CommonPacketTypes.CLIENTBOUND_SERVER_LINKS;
/*    */   }
/*    */   
/*    */   public void handle(ClientCommonPacketListener listener) {
/* 27 */     listener.handleServerLinks(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\ClientboundServerLinksPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */