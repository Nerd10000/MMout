/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import java.util.Set;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.PositionMoveRotation;
/*    */ import net.minecraft.world.entity.Relative;
/*    */ 
/*    */ public final class ClientboundTeleportEntityPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int id;
/*    */   
/*    */   private final PositionMoveRotation change;
/*    */   
/*    */   private final Set<Relative> relatives;
/*    */   
/*    */   private final boolean onGround;
/*    */   
/*    */   public ClientboundTeleportEntityPacket(int id, PositionMoveRotation change, Set<Relative> relatives, boolean onGround) {
/* 13 */     this.id = id;
/* 13 */     this.change = change;
/* 13 */     this.relatives = relatives;
/* 13 */     this.onGround = onGround;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundTeleportEntityPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTeleportEntityPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundTeleportEntityPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTeleportEntityPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundTeleportEntityPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundTeleportEntityPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int id() {
/* 13 */     return this.id;
/*    */   }
/*    */   
/*    */   public PositionMoveRotation change() {
/* 13 */     return this.change;
/*    */   }
/*    */   
/*    */   public Set<Relative> relatives() {
/* 13 */     return this.relatives;
/*    */   }
/*    */   
/*    */   public boolean onGround() {
/* 13 */     return this.onGround;
/*    */   }
/*    */   
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ClientboundTeleportEntityPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundTeleportEntityPacket::id, PositionMoveRotation.STREAM_CODEC, ClientboundTeleportEntityPacket::change, Relative.SET_STREAM_CODEC, ClientboundTeleportEntityPacket::relatives, ByteBufCodecs.BOOL, ClientboundTeleportEntityPacket::onGround, ClientboundTeleportEntityPacket::new);
/*    */   
/*    */   public static ClientboundTeleportEntityPacket teleport(int id, PositionMoveRotation values, Set<Relative> relatives, boolean onGround) {
/* 23 */     return new ClientboundTeleportEntityPacket(id, values, relatives, onGround);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundTeleportEntityPacket> type() {
/* 28 */     return GamePacketTypes.CLIENTBOUND_TELEPORT_ENTITY;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 33 */     listener.handleTeleportEntity(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundTeleportEntityPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */