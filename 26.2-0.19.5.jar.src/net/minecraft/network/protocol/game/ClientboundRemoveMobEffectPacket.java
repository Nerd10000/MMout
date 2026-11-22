/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class ClientboundRemoveMobEffectPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int entityId;
/*    */   
/*    */   private final Holder<MobEffect> effect;
/*    */   
/*    */   public ClientboundRemoveMobEffectPacket(int entityId, Holder<MobEffect> effect) {
/* 14 */     this.entityId = entityId;
/* 14 */     this.effect = effect;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundRemoveMobEffectPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRemoveMobEffectPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundRemoveMobEffectPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRemoveMobEffectPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundRemoveMobEffectPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundRemoveMobEffectPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int entityId() {
/* 14 */     return this.entityId;
/*    */   }
/*    */   
/*    */   public Holder<MobEffect> effect() {
/* 14 */     return this.effect;
/*    */   }
/*    */   
/* 15 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRemoveMobEffectPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundRemoveMobEffectPacket::entityId, MobEffect.STREAM_CODEC, ClientboundRemoveMobEffectPacket::effect, ClientboundRemoveMobEffectPacket::new);
/*    */   
/*    */   public PacketType<ClientboundRemoveMobEffectPacket> type() {
/* 23 */     return GamePacketTypes.CLIENTBOUND_REMOVE_MOB_EFFECT;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 28 */     listener.handleRemoveMobEffect(this);
/*    */   }
/*    */   
/*    */   public Entity getEntity(Level level) {
/* 32 */     return level.getEntity(this.entityId);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundRemoveMobEffectPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */