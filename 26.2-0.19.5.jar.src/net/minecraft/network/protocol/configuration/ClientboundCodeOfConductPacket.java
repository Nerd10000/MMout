/*    */ package net.minecraft.network.protocol.configuration;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundCodeOfConductPacket extends Record implements Packet<ClientConfigurationPacketListener> {
/*    */   private final String codeOfConduct;
/*    */   
/*    */   public ClientboundCodeOfConductPacket(String codeOfConduct) {
/*  9 */     this.codeOfConduct = codeOfConduct;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String codeOfConduct() {
/*  9 */     return this.codeOfConduct;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<ByteBuf, ClientboundCodeOfConductPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ClientboundCodeOfConductPacket::codeOfConduct, ClientboundCodeOfConductPacket::new);
/*    */   
/*    */   public PacketType<ClientboundCodeOfConductPacket> type() {
/* 17 */     return ConfigurationPacketTypes.CLIENTBOUND_CODE_OF_CONDUCT;
/*    */   }
/*    */   
/*    */   public void handle(ClientConfigurationPacketListener listener) {
/* 22 */     listener.handleCodeOfConduct(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\configuration\ClientboundCodeOfConductPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */