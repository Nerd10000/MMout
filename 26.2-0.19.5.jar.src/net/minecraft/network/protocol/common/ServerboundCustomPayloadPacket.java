/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.network.protocol.common.custom.BrandPayload;
/*    */ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
/*    */ import net.minecraft.network.protocol.common.custom.DiscardedPayload;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public final class ServerboundCustomPayloadPacket extends Record implements Packet<ServerCommonPacketListener> {
/*    */   private final CustomPacketPayload payload;
/*    */   
/*    */   private static final int MAX_PAYLOAD_SIZE = 32767;
/*    */   
/*    */   public static final StreamCodec<FriendlyByteBuf, ServerboundCustomPayloadPacket> STREAM_CODEC;
/*    */   
/*    */   public ServerboundCustomPayloadPacket(CustomPacketPayload payload) {
/* 15 */     this.payload = payload;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public CustomPacketPayload payload() {
/* 15 */     return this.payload;
/*    */   }
/*    */   
/*    */   static {
/* 27 */     STREAM_CODEC = CustomPacketPayload.codec(id -> DiscardedPayload.codec(id, 32767), (List)Util.make(Lists.newArrayList((Object[])new CustomPacketPayload.TypeAndCodec[] { new CustomPacketPayload.TypeAndCodec(BrandPayload.TYPE, BrandPayload.STREAM_CODEC) }), types -> {
/*    */           
/* 27 */           })).map(ServerboundCustomPayloadPacket::new, ServerboundCustomPayloadPacket::payload);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundCustomPayloadPacket> type() {
/* 31 */     return CommonPacketTypes.SERVERBOUND_CUSTOM_PAYLOAD;
/*    */   }
/*    */   
/*    */   public void handle(ServerCommonPacketListener listener) {
/* 36 */     listener.handleCustomPayload(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\ServerboundCustomPayloadPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */