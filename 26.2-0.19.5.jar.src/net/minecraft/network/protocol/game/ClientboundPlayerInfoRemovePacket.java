/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.codec.StreamEncoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundPlayerInfoRemovePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final List<UUID> profileIds;
/*    */   
/*    */   public ClientboundPlayerInfoRemovePacket(List<UUID> profileIds) {
/* 12 */     this.profileIds = profileIds;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoRemovePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoRemovePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoRemovePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoRemovePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoRemovePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoRemovePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<UUID> profileIds() {
/* 12 */     return this.profileIds;
/*    */   }
/*    */   
/* 13 */   public static final StreamCodec<FriendlyByteBuf, ClientboundPlayerInfoRemovePacket> STREAM_CODEC = Packet.codec(ClientboundPlayerInfoRemovePacket::write, ClientboundPlayerInfoRemovePacket::new);
/*    */   
/*    */   private ClientboundPlayerInfoRemovePacket(FriendlyByteBuf input) {
/* 16 */     this(input.readList((StreamDecoder)UUIDUtil.STREAM_CODEC));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 20 */     output.writeCollection(this.profileIds, (StreamEncoder)UUIDUtil.STREAM_CODEC);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundPlayerInfoRemovePacket> type() {
/* 25 */     return GamePacketTypes.CLIENTBOUND_PLAYER_INFO_REMOVE;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 30 */     listener.handlePlayerInfoRemove(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundPlayerInfoRemovePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */