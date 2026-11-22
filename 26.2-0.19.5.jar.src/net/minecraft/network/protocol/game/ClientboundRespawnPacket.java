/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundRespawnPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final CommonPlayerSpawnInfo commonPlayerSpawnInfo;
/*    */   
/*    */   private final byte dataToKeep;
/*    */   
/*    */   public ClientboundRespawnPacket(CommonPlayerSpawnInfo commonPlayerSpawnInfo, byte dataToKeep) {
/*  9 */     this.commonPlayerSpawnInfo = commonPlayerSpawnInfo;
/*  9 */     this.dataToKeep = dataToKeep;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public CommonPlayerSpawnInfo commonPlayerSpawnInfo() {
/*  9 */     return this.commonPlayerSpawnInfo;
/*    */   }
/*    */   
/*    */   public byte dataToKeep() {
/*  9 */     return this.dataToKeep;
/*    */   }
/*    */   
/* 13 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRespawnPacket> STREAM_CODEC = Packet.codec(ClientboundRespawnPacket::write, ClientboundRespawnPacket::new);
/*    */   
/*    */   public static final byte KEEP_ATTRIBUTE_MODIFIERS = 1;
/*    */   
/*    */   public static final byte KEEP_ENTITY_DATA = 2;
/*    */   
/*    */   public static final byte KEEP_ALL_DATA = 3;
/*    */   
/*    */   private ClientboundRespawnPacket(RegistryFriendlyByteBuf input) {
/* 24 */     this(new CommonPlayerSpawnInfo(input), input
/*    */         
/* 26 */         .readByte());
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 31 */     this.commonPlayerSpawnInfo.write(output);
/* 32 */     output.writeByte(this.dataToKeep);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundRespawnPacket> type() {
/* 37 */     return GamePacketTypes.CLIENTBOUND_RESPAWN;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 42 */     listener.handleRespawn(this);
/*    */   }
/*    */   
/*    */   public boolean shouldKeep(byte mask) {
/* 46 */     return ((this.dataToKeep & mask) != 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundRespawnPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */