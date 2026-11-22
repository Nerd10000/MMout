/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function3;
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
/*    */ public final class ClientboundPlayerPositionPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int id;
/*    */   
/*    */   private final PositionMoveRotation change;
/*    */   
/*    */   private final Set<Relative> relatives;
/*    */   
/*    */   public ClientboundPlayerPositionPacket(int id, PositionMoveRotation change, Set<Relative> relatives) {
/* 13 */     this.id = id;
/* 13 */     this.change = change;
/* 13 */     this.relatives = relatives;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundPlayerPositionPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerPositionPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundPlayerPositionPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerPositionPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundPlayerPositionPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerPositionPacket;
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
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ClientboundPlayerPositionPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundPlayerPositionPacket::id, PositionMoveRotation.STREAM_CODEC, ClientboundPlayerPositionPacket::change, Relative.SET_STREAM_CODEC, ClientboundPlayerPositionPacket::relatives, ClientboundPlayerPositionPacket::new);
/*    */   
/*    */   public static ClientboundPlayerPositionPacket of(int id, PositionMoveRotation values, Set<Relative> relatives) {
/* 22 */     return new ClientboundPlayerPositionPacket(id, values, relatives);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundPlayerPositionPacket> type() {
/* 27 */     return GamePacketTypes.CLIENTBOUND_PLAYER_POSITION;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 32 */     listener.handleMovePlayer(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundPlayerPositionPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */