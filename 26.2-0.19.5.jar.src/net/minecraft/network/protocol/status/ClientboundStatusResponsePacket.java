/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ 
/*    */ public final class ClientboundStatusResponsePacket extends Record implements Packet<ClientStatusPacketListener> {
/*    */   private final ServerStatus status;
/*    */   
/*    */   public ClientboundStatusResponsePacket(ServerStatus status) {
/* 14 */     this.status = status;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ServerStatus status() {
/* 14 */     return this.status;
/*    */   }
/*    */   
/* 15 */   private static final RegistryOps<JsonElement> OPS = RegistryAccess.EMPTY.createSerializationContext((DynamicOps)JsonOps.INSTANCE);
/*    */   
/* 17 */   public static final StreamCodec<ByteBuf, ClientboundStatusResponsePacket> STREAM_CODEC = StreamCodec.composite(
/* 18 */       ByteBufCodecs.lenientJson(32767).apply(ByteBufCodecs.fromCodec((DynamicOps)OPS, ServerStatus.CODEC)), ClientboundStatusResponsePacket::status, ClientboundStatusResponsePacket::new);
/*    */   
/*    */   public PacketType<ClientboundStatusResponsePacket> type() {
/* 24 */     return StatusPacketTypes.CLIENTBOUND_STATUS_RESPONSE;
/*    */   }
/*    */   
/*    */   public void handle(ClientStatusPacketListener listener) {
/* 29 */     listener.handleStatusResponse(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\status\ClientboundStatusResponsePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */