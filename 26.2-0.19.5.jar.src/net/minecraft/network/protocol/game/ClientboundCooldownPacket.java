/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ClientboundCooldownPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Identifier cooldownGroup;
/*    */   
/*    */   private final int duration;
/*    */   
/*    */   public ClientboundCooldownPacket(Identifier cooldownGroup, int duration) {
/* 10 */     this.cooldownGroup = cooldownGroup;
/* 10 */     this.duration = duration;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundCooldownPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCooldownPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundCooldownPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCooldownPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundCooldownPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundCooldownPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier cooldownGroup() {
/* 10 */     return this.cooldownGroup;
/*    */   }
/*    */   
/*    */   public int duration() {
/* 10 */     return this.duration;
/*    */   }
/*    */   
/* 11 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCooldownPacket> STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, ClientboundCooldownPacket::cooldownGroup, ByteBufCodecs.VAR_INT, ClientboundCooldownPacket::duration, ClientboundCooldownPacket::new);
/*    */   
/*    */   public PacketType<ClientboundCooldownPacket> type() {
/* 19 */     return GamePacketTypes.CLIENTBOUND_COOLDOWN;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 24 */     listener.handleItemCooldown(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundCooldownPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */