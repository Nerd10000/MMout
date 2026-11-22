/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.util.debug.DebugSubscription;
/*    */ 
/*    */ public final class ServerboundDebugSubscriptionRequestPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final Set<DebugSubscription<?>> subscriptions;
/*    */   
/*    */   public ServerboundDebugSubscriptionRequestPacket(Set<DebugSubscription<?>> subscriptions) {
/* 14 */     this.subscriptions = subscriptions;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundDebugSubscriptionRequestPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundDebugSubscriptionRequestPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundDebugSubscriptionRequestPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundDebugSubscriptionRequestPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundDebugSubscriptionRequestPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundDebugSubscriptionRequestPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Set<DebugSubscription<?>> subscriptions() {
/* 14 */     return this.subscriptions;
/*    */   }
/*    */   
/* 17 */   private static final StreamCodec<RegistryFriendlyByteBuf, Set<DebugSubscription<?>>> SET_STREAM_CODEC = ByteBufCodecs.registry(Registries.DEBUG_SUBSCRIPTION)
/* 18 */     .apply(ByteBufCodecs.collection(it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet::new, 32));
/*    */   
/* 19 */   public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundDebugSubscriptionRequestPacket> STREAM_CODEC = SET_STREAM_CODEC.map(ServerboundDebugSubscriptionRequestPacket::new, ServerboundDebugSubscriptionRequestPacket::subscriptions);
/*    */   
/*    */   public PacketType<ServerboundDebugSubscriptionRequestPacket> type() {
/* 23 */     return GamePacketTypes.SERVERBOUND_DEBUG_SUBSCRIPTION_REQUEST;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 28 */     listener.handleDebugSubscriptionRequest(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundDebugSubscriptionRequestPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */