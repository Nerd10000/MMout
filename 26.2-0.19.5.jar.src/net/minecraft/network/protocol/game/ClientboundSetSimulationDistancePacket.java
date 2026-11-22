/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundSetSimulationDistancePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int simulationDistance;
/*    */   
/*    */   public ClientboundSetSimulationDistancePacket(int simulationDistance) {
/*  8 */     this.simulationDistance = simulationDistance;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetSimulationDistancePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetSimulationDistancePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetSimulationDistancePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetSimulationDistancePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetSimulationDistancePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetSimulationDistancePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int simulationDistance() {
/*  8 */     return this.simulationDistance;
/*    */   }
/*    */   
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetSimulationDistancePacket> STREAM_CODEC = Packet.codec(ClientboundSetSimulationDistancePacket::write, ClientboundSetSimulationDistancePacket::new);
/*    */   
/*    */   private ClientboundSetSimulationDistancePacket(FriendlyByteBuf input) {
/* 12 */     this(input.readVarInt());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 16 */     output.writeVarInt(this.simulationDistance);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundSetSimulationDistancePacket> type() {
/* 21 */     return GamePacketTypes.CLIENTBOUND_SET_SIMULATION_DISTANCE;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 26 */     listener.handleSetSimulationDistance(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundSetSimulationDistancePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */