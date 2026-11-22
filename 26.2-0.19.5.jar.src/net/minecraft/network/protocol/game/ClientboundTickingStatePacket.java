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
/*    */ public final class ClientboundTickingStatePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final float tickRate;
/*    */   
/*    */   private final boolean isFrozen;
/*    */   
/*    */   public ClientboundTickingStatePacket(float tickRate, boolean isFrozen) {
/*  9 */     this.tickRate = tickRate;
/*  9 */     this.isFrozen = isFrozen;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float tickRate() {
/*  9 */     return this.tickRate;
/*    */   }
/*    */   
/*    */   public boolean isFrozen() {
/*  9 */     return this.isFrozen;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundTickingStatePacket> STREAM_CODEC = Packet.codec(ClientboundTickingStatePacket::write, ClientboundTickingStatePacket::new);
/*    */   
/*    */   private ClientboundTickingStatePacket(FriendlyByteBuf input) {
/* 13 */     this(input
/* 14 */         .readFloat(), input
/* 15 */         .readBoolean());
/*    */   }
/*    */   
/*    */   public static ClientboundTickingStatePacket from(TickRateManager manager) {
/* 20 */     return new ClientboundTickingStatePacket(manager.tickrate(), manager.isFrozen());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 24 */     output.writeFloat(this.tickRate);
/* 25 */     output.writeBoolean(this.isFrozen);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundTickingStatePacket> type() {
/* 30 */     return GamePacketTypes.CLIENTBOUND_TICKING_STATE;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 35 */     listener.handleTickingState(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundTickingStatePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */