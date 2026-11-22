/*    */ package net.minecraft.network.protocol.configuration;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.server.packs.repository.KnownPack;
/*    */ 
/*    */ public final class ClientboundSelectKnownPacks extends Record implements Packet<ClientConfigurationPacketListener> {
/*    */   private final List<KnownPack> knownPacks;
/*    */   
/*    */   public ClientboundSelectKnownPacks(List<KnownPack> knownPacks) {
/* 12 */     this.knownPacks = knownPacks;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/configuration/ClientboundSelectKnownPacks;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundSelectKnownPacks;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/configuration/ClientboundSelectKnownPacks;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundSelectKnownPacks;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/configuration/ClientboundSelectKnownPacks;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundSelectKnownPacks;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<KnownPack> knownPacks() {
/* 12 */     return this.knownPacks;
/*    */   }
/*    */   
/* 15 */   public static final StreamCodec<ByteBuf, ClientboundSelectKnownPacks> STREAM_CODEC = StreamCodec.composite(KnownPack.STREAM_CODEC
/* 16 */       .apply(ByteBufCodecs.list()), ClientboundSelectKnownPacks::knownPacks, ClientboundSelectKnownPacks::new);
/*    */   
/*    */   public PacketType<ClientboundSelectKnownPacks> type() {
/* 22 */     return ConfigurationPacketTypes.CLIENTBOUND_SELECT_KNOWN_PACKS;
/*    */   }
/*    */   
/*    */   public void handle(ClientConfigurationPacketListener listener) {
/* 27 */     listener.handleSelectKnownPacks(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\configuration\ClientboundSelectKnownPacks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */