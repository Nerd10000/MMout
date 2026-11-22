/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.PositionMoveRotation;
/*    */ 
/*    */ public final class ClientboundEntityPositionSyncPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int id;
/*    */   
/*    */   private final PositionMoveRotation values;
/*    */   
/*    */   private final boolean onGround;
/*    */   
/*    */   public ClientboundEntityPositionSyncPacket(int id, PositionMoveRotation values, boolean onGround) {
/* 11 */     this.id = id;
/* 11 */     this.values = values;
/* 11 */     this.onGround = onGround;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundEntityPositionSyncPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundEntityPositionSyncPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundEntityPositionSyncPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundEntityPositionSyncPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundEntityPositionSyncPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundEntityPositionSyncPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int id() {
/* 11 */     return this.id;
/*    */   }
/*    */   
/*    */   public PositionMoveRotation values() {
/* 11 */     return this.values;
/*    */   }
/*    */   
/*    */   public boolean onGround() {
/* 11 */     return this.onGround;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundEntityPositionSyncPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundEntityPositionSyncPacket::id, PositionMoveRotation.STREAM_CODEC, ClientboundEntityPositionSyncPacket::values, ByteBufCodecs.BOOL, ClientboundEntityPositionSyncPacket::onGround, ClientboundEntityPositionSyncPacket::new);
/*    */   
/*    */   public static ClientboundEntityPositionSyncPacket of(Entity entity) {
/* 20 */     return new ClientboundEntityPositionSyncPacket(entity.getId(), new PositionMoveRotation(entity.trackingPosition(), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot()), entity.onGround());
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundEntityPositionSyncPacket> type() {
/* 25 */     return GamePacketTypes.CLIENTBOUND_ENTITY_POSITION_SYNC;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 30 */     listener.handleEntityPositionSync(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundEntityPositionSyncPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */