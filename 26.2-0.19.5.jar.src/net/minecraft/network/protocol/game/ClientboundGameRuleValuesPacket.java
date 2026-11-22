/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.gamerules.GameRule;
/*    */ 
/*    */ public final class ClientboundGameRuleValuesPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Map<ResourceKey<GameRule<?>>, String> values;
/*    */   
/*    */   public ClientboundGameRuleValuesPacket(Map<ResourceKey<GameRule<?>>, String> values) {
/* 15 */     this.values = values;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundGameRuleValuesPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundGameRuleValuesPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundGameRuleValuesPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundGameRuleValuesPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundGameRuleValuesPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundGameRuleValuesPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<ResourceKey<GameRule<?>>, String> values() {
/* 15 */     return this.values;
/*    */   }
/*    */   
/* 17 */   public static final StreamCodec<FriendlyByteBuf, ClientboundGameRuleValuesPacket> STREAM_CODEC = ByteBufCodecs.map(java.util.HashMap::new, 
/*    */       
/* 19 */       ResourceKey.streamCodec(Registries.GAME_RULE), ByteBufCodecs.STRING_UTF8)
/*    */     
/* 21 */     .map(ClientboundGameRuleValuesPacket::new, ClientboundGameRuleValuesPacket::values);
/*    */   
/*    */   public PacketType<ClientboundGameRuleValuesPacket> type() {
/* 25 */     return GamePacketTypes.CLIENTBOUND_GAME_RULE_VALUES;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 30 */     listener.handleGameRuleValues(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundGameRuleValuesPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */