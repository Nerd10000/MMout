/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundGameTestHighlightPosPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final BlockPos absolutePos;
/*    */   
/*    */   private final BlockPos relativePos;
/*    */   
/*    */   public ClientboundGameTestHighlightPosPacket(BlockPos absolutePos, BlockPos relativePos) {
/* 10 */     this.absolutePos = absolutePos;
/* 10 */     this.relativePos = relativePos;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundGameTestHighlightPosPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundGameTestHighlightPosPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundGameTestHighlightPosPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundGameTestHighlightPosPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundGameTestHighlightPosPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundGameTestHighlightPosPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockPos absolutePos() {
/* 10 */     return this.absolutePos;
/*    */   }
/*    */   
/*    */   public BlockPos relativePos() {
/* 10 */     return this.relativePos;
/*    */   }
/*    */   
/* 14 */   public static final StreamCodec<ByteBuf, ClientboundGameTestHighlightPosPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ClientboundGameTestHighlightPosPacket::absolutePos, BlockPos.STREAM_CODEC, ClientboundGameTestHighlightPosPacket::relativePos, ClientboundGameTestHighlightPosPacket::new);
/*    */   
/*    */   public PacketType<ClientboundGameTestHighlightPosPacket> type() {
/* 22 */     return GamePacketTypes.CLIENTBOUND_GAME_TEST_HIGHLIGHT_POS;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 27 */     listener.handleGameTestHighlightPos(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundGameTestHighlightPosPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */