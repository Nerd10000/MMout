/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundServerDataPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Component motd;
/*    */   
/*    */   private final Optional<byte[]> iconBytes;
/*    */   
/*    */   public ClientboundServerDataPacket(Component motd, Optional<byte[]> iconBytes) {
/* 13 */     this.motd = motd;
/* 13 */     this.iconBytes = iconBytes;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component motd() {
/* 13 */     return this.motd;
/*    */   }
/*    */   
/*    */   public Optional<byte[]> iconBytes() {
/* 13 */     return this.iconBytes;
/*    */   }
/*    */   
/* 17 */   public static final StreamCodec<ByteBuf, ClientboundServerDataPacket> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC, ClientboundServerDataPacket::motd, ByteBufCodecs.BYTE_ARRAY
/*    */       
/* 19 */       .apply(ByteBufCodecs::optional), ClientboundServerDataPacket::iconBytes, ClientboundServerDataPacket::new);
/*    */   
/*    */   public PacketType<ClientboundServerDataPacket> type() {
/* 25 */     return GamePacketTypes.CLIENTBOUND_SERVER_DATA;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 30 */     listener.handleServerData(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundServerDataPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */