/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ServerboundPickItemFromBlockPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final BlockPos pos;
/*    */   
/*    */   private final boolean includeData;
/*    */   
/*    */   public ServerboundPickItemFromBlockPacket(BlockPos pos, boolean includeData) {
/* 10 */     this.pos = pos;
/* 10 */     this.includeData = includeData;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundPickItemFromBlockPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundPickItemFromBlockPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundPickItemFromBlockPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundPickItemFromBlockPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundPickItemFromBlockPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundPickItemFromBlockPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockPos pos() {
/* 10 */     return this.pos;
/*    */   }
/*    */   
/*    */   public boolean includeData() {
/* 10 */     return this.includeData;
/*    */   }
/*    */   
/* 11 */   public static final StreamCodec<ByteBuf, ServerboundPickItemFromBlockPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ServerboundPickItemFromBlockPacket::pos, ByteBufCodecs.BOOL, ServerboundPickItemFromBlockPacket::includeData, ServerboundPickItemFromBlockPacket::new);
/*    */   
/*    */   public PacketType<ServerboundPickItemFromBlockPacket> type() {
/* 19 */     return GamePacketTypes.SERVERBOUND_PICK_ITEM_FROM_BLOCK;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 24 */     listener.handlePickItemFromBlock(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundPickItemFromBlockPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */