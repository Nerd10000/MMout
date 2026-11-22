/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.util.debugchart.RemoteDebugSampleType;
/*    */ 
/*    */ public final class ClientboundDebugSamplePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final long[] sample;
/*    */   
/*    */   private final RemoteDebugSampleType debugSampleType;
/*    */   
/*    */   public ClientboundDebugSamplePacket(long[] sample, RemoteDebugSampleType debugSampleType) {
/*  9 */     this.sample = sample;
/*  9 */     this.debugSampleType = debugSampleType;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public long[] sample() {
/*  9 */     return this.sample;
/*    */   }
/*    */   
/*    */   public RemoteDebugSampleType debugSampleType() {
/*  9 */     return this.debugSampleType;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundDebugSamplePacket> STREAM_CODEC = Packet.codec(ClientboundDebugSamplePacket::write, ClientboundDebugSamplePacket::new);
/*    */   
/*    */   private ClientboundDebugSamplePacket(FriendlyByteBuf input) {
/* 13 */     this(input.readLongArray(), (RemoteDebugSampleType)input.readEnum(RemoteDebugSampleType.class));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 17 */     output.writeLongArray(this.sample);
/* 18 */     output.writeEnum((Enum)this.debugSampleType);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundDebugSamplePacket> type() {
/* 23 */     return GamePacketTypes.CLIENTBOUND_DEBUG_SAMPLE;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 28 */     listener.handleDebugSample(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundDebugSamplePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */