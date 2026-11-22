/*    */ package net.minecraft.network.protocol.configuration;
/*    */ 
/*    */ import java.util.Set;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ClientboundUpdateEnabledFeaturesPacket extends Record implements Packet<ClientConfigurationPacketListener> {
/*    */   private final Set<Identifier> features;
/*    */   
/*    */   public ClientboundUpdateEnabledFeaturesPacket(Set<Identifier> features) {
/* 12 */     this.features = features;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/configuration/ClientboundUpdateEnabledFeaturesPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundUpdateEnabledFeaturesPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/configuration/ClientboundUpdateEnabledFeaturesPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundUpdateEnabledFeaturesPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/configuration/ClientboundUpdateEnabledFeaturesPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundUpdateEnabledFeaturesPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Set<Identifier> features() {
/* 12 */     return this.features;
/*    */   }
/*    */   
/* 13 */   public static final StreamCodec<FriendlyByteBuf, ClientboundUpdateEnabledFeaturesPacket> STREAM_CODEC = Packet.codec(ClientboundUpdateEnabledFeaturesPacket::write, ClientboundUpdateEnabledFeaturesPacket::new);
/*    */   
/*    */   private ClientboundUpdateEnabledFeaturesPacket(FriendlyByteBuf input) {
/* 16 */     this((Set<Identifier>)input.readCollection(java.util.HashSet::new, FriendlyByteBuf::readIdentifier));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 20 */     output.writeCollection(this.features, FriendlyByteBuf::writeIdentifier);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundUpdateEnabledFeaturesPacket> type() {
/* 25 */     return ConfigurationPacketTypes.CLIENTBOUND_UPDATE_ENABLED_FEATURES;
/*    */   }
/*    */   
/*    */   public void handle(ClientConfigurationPacketListener listener) {
/* 30 */     listener.handleEnabledFeatures(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\configuration\ClientboundUpdateEnabledFeaturesPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */