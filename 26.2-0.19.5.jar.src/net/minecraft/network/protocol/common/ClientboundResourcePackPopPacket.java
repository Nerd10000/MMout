/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.codec.StreamEncoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundResourcePackPopPacket extends Record implements Packet<ClientCommonPacketListener> {
/*    */   private final Optional<UUID> id;
/*    */   
/*    */   public ClientboundResourcePackPopPacket(Optional<UUID> id) {
/* 12 */     this.id = id;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<UUID> id() {
/* 12 */     return this.id;
/*    */   }
/*    */   
/* 15 */   public static final StreamCodec<FriendlyByteBuf, ClientboundResourcePackPopPacket> STREAM_CODEC = Packet.codec(ClientboundResourcePackPopPacket::write, ClientboundResourcePackPopPacket::new);
/*    */   
/*    */   private ClientboundResourcePackPopPacket(FriendlyByteBuf input) {
/* 18 */     this(input
/* 19 */         .readOptional((StreamDecoder)UUIDUtil.STREAM_CODEC));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 24 */     output.writeOptional(this.id, (StreamEncoder)UUIDUtil.STREAM_CODEC);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundResourcePackPopPacket> type() {
/* 29 */     return CommonPacketTypes.CLIENTBOUND_RESOURCE_PACK_POP;
/*    */   }
/*    */   
/*    */   public void handle(ClientCommonPacketListener listener) {
/* 34 */     listener.handleResourcePackPop(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\ClientboundResourcePackPopPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */