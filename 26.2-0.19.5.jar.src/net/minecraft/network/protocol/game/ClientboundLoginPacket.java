/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class ClientboundLoginPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int playerId;
/*    */   
/*    */   private final boolean hardcore;
/*    */   
/*    */   private final Set<ResourceKey<Level>> levels;
/*    */   
/*    */   private final int maxPlayers;
/*    */   
/*    */   private final int chunkRadius;
/*    */   
/*    */   private final int simulationDistance;
/*    */   
/*    */   private final boolean reducedDebugInfo;
/*    */   
/*    */   private final boolean showDeathScreen;
/*    */   
/*    */   private final boolean doLimitedCrafting;
/*    */   
/*    */   private final CommonPlayerSpawnInfo commonPlayerSpawnInfo;
/*    */   
/*    */   private final boolean onlineMode;
/*    */   
/*    */   private final boolean enforcesSecureChat;
/*    */   
/*    */   public ClientboundLoginPacket(int playerId, boolean hardcore, Set<ResourceKey<Level>> levels, int maxPlayers, int chunkRadius, int simulationDistance, boolean reducedDebugInfo, boolean showDeathScreen, boolean doLimitedCrafting, CommonPlayerSpawnInfo commonPlayerSpawnInfo, boolean onlineMode, boolean enforcesSecureChat) {
/* 17 */     this.playerId = playerId;
/* 17 */     this.hardcore = hardcore;
/* 17 */     this.levels = levels;
/* 17 */     this.maxPlayers = maxPlayers;
/* 17 */     this.chunkRadius = chunkRadius;
/* 17 */     this.simulationDistance = simulationDistance;
/* 17 */     this.reducedDebugInfo = reducedDebugInfo;
/* 17 */     this.showDeathScreen = showDeathScreen;
/* 17 */     this.doLimitedCrafting = doLimitedCrafting;
/* 17 */     this.commonPlayerSpawnInfo = commonPlayerSpawnInfo;
/* 17 */     this.onlineMode = onlineMode;
/* 17 */     this.enforcesSecureChat = enforcesSecureChat;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int playerId() {
/* 17 */     return this.playerId;
/*    */   }
/*    */   
/*    */   public boolean hardcore() {
/* 17 */     return this.hardcore;
/*    */   }
/*    */   
/*    */   public Set<ResourceKey<Level>> levels() {
/* 17 */     return this.levels;
/*    */   }
/*    */   
/*    */   public int maxPlayers() {
/* 17 */     return this.maxPlayers;
/*    */   }
/*    */   
/*    */   public int chunkRadius() {
/* 17 */     return this.chunkRadius;
/*    */   }
/*    */   
/*    */   public int simulationDistance() {
/* 17 */     return this.simulationDistance;
/*    */   }
/*    */   
/*    */   public boolean reducedDebugInfo() {
/* 17 */     return this.reducedDebugInfo;
/*    */   }
/*    */   
/*    */   public boolean showDeathScreen() {
/* 17 */     return this.showDeathScreen;
/*    */   }
/*    */   
/*    */   public boolean doLimitedCrafting() {
/* 17 */     return this.doLimitedCrafting;
/*    */   }
/*    */   
/*    */   public CommonPlayerSpawnInfo commonPlayerSpawnInfo() {
/* 17 */     return this.commonPlayerSpawnInfo;
/*    */   }
/*    */   
/*    */   public boolean onlineMode() {
/* 17 */     return this.onlineMode;
/*    */   }
/*    */   
/*    */   public boolean enforcesSecureChat() {
/* 17 */     return this.enforcesSecureChat;
/*    */   }
/*    */   
/* 31 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLoginPacket> STREAM_CODEC = Packet.codec(ClientboundLoginPacket::write, ClientboundLoginPacket::new);
/*    */   
/*    */   private ClientboundLoginPacket(RegistryFriendlyByteBuf input) {
/* 34 */     this(input
/* 35 */         .readInt(), input
/* 36 */         .readBoolean(), (Set<ResourceKey<Level>>)input
/* 37 */         .readCollection(Sets::newHashSetWithExpectedSize, buf -> buf.readResourceKey(Registries.DIMENSION)), input
/* 38 */         .readVarInt(), input
/* 39 */         .readVarInt(), input
/* 40 */         .readVarInt(), input
/* 41 */         .readBoolean(), input
/* 42 */         .readBoolean(), input
/* 43 */         .readBoolean(), new CommonPlayerSpawnInfo(input), input
/*    */         
/* 45 */         .readBoolean(), input
/* 46 */         .readBoolean());
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 51 */     output.writeInt(this.playerId);
/* 52 */     output.writeBoolean(this.hardcore);
/* 53 */     output.writeCollection(this.levels, FriendlyByteBuf::writeResourceKey);
/* 54 */     output.writeVarInt(this.maxPlayers);
/* 55 */     output.writeVarInt(this.chunkRadius);
/* 56 */     output.writeVarInt(this.simulationDistance);
/* 57 */     output.writeBoolean(this.reducedDebugInfo);
/* 58 */     output.writeBoolean(this.showDeathScreen);
/* 59 */     output.writeBoolean(this.doLimitedCrafting);
/* 60 */     this.commonPlayerSpawnInfo.write(output);
/* 61 */     output.writeBoolean(this.onlineMode);
/* 62 */     output.writeBoolean(this.enforcesSecureChat);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundLoginPacket> type() {
/* 67 */     return GamePacketTypes.CLIENTBOUND_LOGIN;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 72 */     listener.handleLogin(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundLoginPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */