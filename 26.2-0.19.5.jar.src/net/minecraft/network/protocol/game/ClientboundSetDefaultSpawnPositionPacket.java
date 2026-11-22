/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.level.storage.LevelData;
/*    */ 
/*    */ public final class ClientboundSetDefaultSpawnPositionPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final LevelData.RespawnData respawnData;
/*    */   
/*    */   public ClientboundSetDefaultSpawnPositionPacket(LevelData.RespawnData respawnData) {
/*  9 */     this.respawnData = respawnData;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetDefaultSpawnPositionPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetDefaultSpawnPositionPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetDefaultSpawnPositionPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetDefaultSpawnPositionPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetDefaultSpawnPositionPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetDefaultSpawnPositionPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public LevelData.RespawnData respawnData() {
/*  9 */     return this.respawnData;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetDefaultSpawnPositionPacket> STREAM_CODEC = StreamCodec.composite(LevelData.RespawnData.STREAM_CODEC, ClientboundSetDefaultSpawnPositionPacket::respawnData, ClientboundSetDefaultSpawnPositionPacket::new);
/*    */   
/*    */   public PacketType<ClientboundSetDefaultSpawnPositionPacket> type() {
/* 17 */     return GamePacketTypes.CLIENTBOUND_SET_DEFAULT_SPAWN_POSITION;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 22 */     listener.handleSetSpawn(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundSetDefaultSpawnPositionPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */