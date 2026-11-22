/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.level.block.state.properties.TestBlockMode;
/*    */ 
/*    */ public final class ServerboundSetTestBlockPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final BlockPos position;
/*    */   
/*    */   private final TestBlockMode mode;
/*    */   
/*    */   private final String message;
/*    */   
/*    */   public ServerboundSetTestBlockPacket(BlockPos position, TestBlockMode mode, String message) {
/* 11 */     this.position = position;
/* 11 */     this.mode = mode;
/* 11 */     this.message = message;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundSetTestBlockPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetTestBlockPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundSetTestBlockPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetTestBlockPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundSetTestBlockPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetTestBlockPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockPos position() {
/* 11 */     return this.position;
/*    */   }
/*    */   
/*    */   public TestBlockMode mode() {
/* 11 */     return this.mode;
/*    */   }
/*    */   
/*    */   public String message() {
/* 11 */     return this.message;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSetTestBlockPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ServerboundSetTestBlockPacket::position, TestBlockMode.STREAM_CODEC, ServerboundSetTestBlockPacket::mode, ByteBufCodecs.STRING_UTF8, ServerboundSetTestBlockPacket::message, ServerboundSetTestBlockPacket::new);
/*    */   
/*    */   public PacketType<ServerboundSetTestBlockPacket> type() {
/* 21 */     return GamePacketTypes.SERVERBOUND_SET_TEST_BLOCK;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 26 */     listener.handleSetTestBlock(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundSetTestBlockPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */