/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.TickRateManager;
/*    */ 
/*    */ public final class ClientboundTickingStepPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int tickSteps;
/*    */   
/*    */   public ClientboundTickingStepPacket(int tickSteps) {
/*  9 */     this.tickSteps = tickSteps;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int tickSteps() {
/*  9 */     return this.tickSteps;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundTickingStepPacket> STREAM_CODEC = Packet.codec(ClientboundTickingStepPacket::write, ClientboundTickingStepPacket::new);
/*    */   
/*    */   private ClientboundTickingStepPacket(FriendlyByteBuf input) {
/* 13 */     this(input.readVarInt());
/*    */   }
/*    */   
/*    */   public static ClientboundTickingStepPacket from(TickRateManager manager) {
/* 17 */     return new ClientboundTickingStepPacket(manager.frozenTicksToRun());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 21 */     output.writeVarInt(this.tickSteps);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundTickingStepPacket> type() {
/* 26 */     return GamePacketTypes.CLIENTBOUND_TICKING_STEP;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 31 */     listener.handleTickingStep(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundTickingStepPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */