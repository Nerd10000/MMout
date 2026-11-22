/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.stats.Stat;
/*    */ 
/*    */ public final class ClientboundAwardStatsPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Object2IntMap<Stat<?>> stats;
/*    */   
/*    */   public ClientboundAwardStatsPacket(Object2IntMap<Stat<?>> stats) {
/* 12 */     this.stats = stats;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundAwardStatsPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundAwardStatsPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundAwardStatsPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundAwardStatsPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundAwardStatsPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundAwardStatsPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Object2IntMap<Stat<?>> stats() {
/* 12 */     return this.stats;
/*    */   }
/*    */   
/* 13 */   private static final StreamCodec<RegistryFriendlyByteBuf, Object2IntMap<Stat<?>>> STAT_VALUES_STREAM_CODEC = ByteBufCodecs.map(it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap::new, Stat.STREAM_CODEC, ByteBufCodecs.VAR_INT);
/*    */   
/* 19 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundAwardStatsPacket> STREAM_CODEC = STAT_VALUES_STREAM_CODEC
/* 20 */     .map(ClientboundAwardStatsPacket::new, ClientboundAwardStatsPacket::stats);
/*    */   
/*    */   public PacketType<ClientboundAwardStatsPacket> type() {
/* 24 */     return GamePacketTypes.CLIENTBOUND_AWARD_STATS;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 29 */     listener.handleAwardStats(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundAwardStatsPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */