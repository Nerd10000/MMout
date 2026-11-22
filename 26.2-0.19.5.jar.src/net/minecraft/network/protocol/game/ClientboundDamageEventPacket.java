/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ClientboundDamageEventPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int entityId;
/*    */   
/*    */   private final Holder<DamageType> sourceType;
/*    */   
/*    */   private final int sourceCauseId;
/*    */   
/*    */   private final int sourceDirectId;
/*    */   
/*    */   private final Optional<Vec3> sourcePosition;
/*    */   
/*    */   public ClientboundDamageEventPacket(int entityId, Holder<DamageType> sourceType, int sourceCauseId, int sourceDirectId, Optional<Vec3> sourcePosition) {
/* 17 */     this.entityId = entityId;
/* 17 */     this.sourceType = sourceType;
/* 17 */     this.sourceCauseId = sourceCauseId;
/* 17 */     this.sourceDirectId = sourceDirectId;
/* 17 */     this.sourcePosition = sourcePosition;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int entityId() {
/* 17 */     return this.entityId;
/*    */   }
/*    */   
/*    */   public Holder<DamageType> sourceType() {
/* 17 */     return this.sourceType;
/*    */   }
/*    */   
/*    */   public int sourceCauseId() {
/* 17 */     return this.sourceCauseId;
/*    */   }
/*    */   
/*    */   public int sourceDirectId() {
/* 17 */     return this.sourceDirectId;
/*    */   }
/*    */   
/*    */   public Optional<Vec3> sourcePosition() {
/* 17 */     return this.sourcePosition;
/*    */   }
/*    */   
/* 24 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDamageEventPacket> STREAM_CODEC = Packet.codec(ClientboundDamageEventPacket::write, ClientboundDamageEventPacket::new);
/*    */   
/*    */   public ClientboundDamageEventPacket(Entity entity, DamageSource source) {
/* 27 */     this(entity
/* 28 */         .getId(), source
/* 29 */         .typeHolder(), 
/* 30 */         (source.getEntity() != null) ? source.getEntity().getId() : -1, 
/* 31 */         (source.getDirectEntity() != null) ? source.getDirectEntity().getId() : -1, 
/* 32 */         Optional.ofNullable(source.sourcePositionRaw()));
/*    */   }
/*    */   
/*    */   private static void writeOptionalEntityId(FriendlyByteBuf output, int id) {
/* 38 */     output.writeVarInt(id + 1);
/*    */   }
/*    */   
/*    */   private static int readOptionalEntityId(FriendlyByteBuf input) {
/* 42 */     return input.readVarInt() - 1;
/*    */   }
/*    */   
/*    */   private ClientboundDamageEventPacket(RegistryFriendlyByteBuf input) {
/* 46 */     this(input
/* 47 */         .readVarInt(), (Holder<DamageType>)DamageType.STREAM_CODEC
/* 48 */         .decode(input), 
/* 49 */         readOptionalEntityId((FriendlyByteBuf)input), 
/* 50 */         readOptionalEntityId((FriendlyByteBuf)input), input
/* 51 */         .readOptional(i -> new Vec3(i.readDouble(), i.readDouble(), i.readDouble())));
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 56 */     output.writeVarInt(this.entityId);
/* 57 */     DamageType.STREAM_CODEC.encode(output, this.sourceType);
/* 58 */     writeOptionalEntityId((FriendlyByteBuf)output, this.sourceCauseId);
/* 59 */     writeOptionalEntityId((FriendlyByteBuf)output, this.sourceDirectId);
/* 60 */     output.writeOptional(this.sourcePosition, (o, pos) -> {
/*    */           o.writeDouble(pos.x());
/*    */           o.writeDouble(pos.y());
/*    */           o.writeDouble(pos.z());
/*    */         });
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundDamageEventPacket> type() {
/* 69 */     return GamePacketTypes.CLIENTBOUND_DAMAGE_EVENT;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 74 */     listener.handleDamageEvent(this);
/*    */   }
/*    */   
/*    */   public DamageSource getSource(Level level) {
/* 78 */     if (this.sourcePosition.isPresent())
/* 79 */       return new DamageSource(this.sourceType, this.sourcePosition.get()); 
/* 81 */     Entity cause = level.getEntity(this.sourceCauseId);
/* 82 */     Entity direct = level.getEntity(this.sourceDirectId);
/* 83 */     return new DamageSource(this.sourceType, direct, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundDamageEventPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */