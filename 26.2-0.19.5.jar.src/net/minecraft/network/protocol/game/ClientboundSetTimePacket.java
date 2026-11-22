/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.clock.ClockNetworkState;
/*    */ import net.minecraft.world.clock.WorldClock;
/*    */ 
/*    */ public final class ClientboundSetTimePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final long gameTime;
/*    */   
/*    */   private final Map<Holder<WorldClock>, ClockNetworkState> clockUpdates;
/*    */   
/*    */   public ClientboundSetTimePacket(long gameTime, Map<Holder<WorldClock>, ClockNetworkState> clockUpdates) {
/* 15 */     this.gameTime = gameTime;
/* 15 */     this.clockUpdates = clockUpdates;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public long gameTime() {
/* 15 */     return this.gameTime;
/*    */   }
/*    */   
/*    */   public Map<Holder<WorldClock>, ClockNetworkState> clockUpdates() {
/* 15 */     return this.clockUpdates;
/*    */   }
/*    */   
/* 20 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetTimePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.LONG, ClientboundSetTimePacket::gameTime, 
/*    */       
/* 22 */       ByteBufCodecs.map(java.util.HashMap::new, WorldClock.STREAM_CODEC, ClockNetworkState.STREAM_CODEC), ClientboundSetTimePacket::clockUpdates, ClientboundSetTimePacket::new);
/*    */   
/*    */   public PacketType<ClientboundSetTimePacket> type() {
/* 28 */     return GamePacketTypes.CLIENTBOUND_SET_TIME;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 33 */     listener.handleSetTime(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundSetTimePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */