/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.OptionalInt;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ServerboundSpectatorActionPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final OptionalInt spectateEntityId;
/*    */   
/*    */   public ServerboundSpectatorActionPacket(OptionalInt spectateEntityId) {
/* 11 */     this.spectateEntityId = spectateEntityId;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundSpectatorActionPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSpectatorActionPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundSpectatorActionPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSpectatorActionPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundSpectatorActionPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundSpectatorActionPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public OptionalInt spectateEntityId() {
/* 11 */     return this.spectateEntityId;
/*    */   }
/*    */   
/* 14 */   public static final StreamCodec<ByteBuf, ServerboundSpectatorActionPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.OPTIONAL_VAR_INT, ServerboundSpectatorActionPacket::spectateEntityId, ServerboundSpectatorActionPacket::new);
/*    */   
/*    */   public PacketType<ServerboundSpectatorActionPacket> type() {
/* 21 */     return GamePacketTypes.SERVERBOUND_SPECTATOR_ACTION;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 26 */     listener.handleSpectatorAction(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundSpectatorActionPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */