/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.server.level.ClientInformation;
/*    */ 
/*    */ public final class ServerboundClientInformationPacket extends Record implements Packet<ServerCommonPacketListener> {
/*    */   private final ClientInformation information;
/*    */   
/*    */   public ServerboundClientInformationPacket(ClientInformation information) {
/*  9 */     this.information = information;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ServerboundClientInformationPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundClientInformationPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ServerboundClientInformationPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundClientInformationPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ServerboundClientInformationPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ServerboundClientInformationPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ClientInformation information() {
/*  9 */     return this.information;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundClientInformationPacket> STREAM_CODEC = Packet.codec(ServerboundClientInformationPacket::write, ServerboundClientInformationPacket::new);
/*    */   
/*    */   private ServerboundClientInformationPacket(FriendlyByteBuf input) {
/* 13 */     this(new ClientInformation(input));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 17 */     this.information.write(output);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundClientInformationPacket> type() {
/* 22 */     return CommonPacketTypes.SERVERBOUND_CLIENT_INFORMATION;
/*    */   }
/*    */   
/*    */   public void handle(ServerCommonPacketListener listener) {
/* 27 */     listener.handleClientInformation(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\ServerboundClientInformationPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */