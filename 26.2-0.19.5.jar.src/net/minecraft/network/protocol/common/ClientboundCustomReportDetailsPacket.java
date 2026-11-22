/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundCustomReportDetailsPacket extends Record implements Packet<ClientCommonPacketListener> {
/*    */   private final Map<String, String> details;
/*    */   
/*    */   private static final int MAX_DETAIL_KEY_LENGTH = 128;
/*    */   
/*    */   private static final int MAX_DETAIL_VALUE_LENGTH = 4096;
/*    */   
/*    */   private static final int MAX_DETAIL_COUNT = 32;
/*    */   
/*    */   public ClientboundCustomReportDetailsPacket(Map<String, String> details) {
/* 12 */     this.details = details;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundCustomReportDetailsPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundCustomReportDetailsPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundCustomReportDetailsPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundCustomReportDetailsPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundCustomReportDetailsPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundCustomReportDetailsPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<String, String> details() {
/* 12 */     return this.details;
/*    */   }
/*    */   
/* 20 */   private static final StreamCodec<ByteBuf, Map<String, String>> DETAILS_STREAM_CODEC = ByteBufCodecs.map(java.util.HashMap::new, 
/*    */       
/* 22 */       ByteBufCodecs.stringUtf8(128), 
/* 23 */       ByteBufCodecs.stringUtf8(4096), 32);
/*    */   
/* 27 */   public static final StreamCodec<ByteBuf, ClientboundCustomReportDetailsPacket> STREAM_CODEC = StreamCodec.composite(DETAILS_STREAM_CODEC, ClientboundCustomReportDetailsPacket::details, ClientboundCustomReportDetailsPacket::new);
/*    */   
/*    */   public PacketType<ClientboundCustomReportDetailsPacket> type() {
/* 34 */     return CommonPacketTypes.CLIENTBOUND_CUSTOM_REPORT_DETAILS;
/*    */   }
/*    */   
/*    */   public void handle(ClientCommonPacketListener listener) {
/* 39 */     listener.handleCustomReportDetails(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\ClientboundCustomReportDetailsPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */