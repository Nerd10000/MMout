/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.Difficulty;
/*    */ 
/*    */ public final class ServerboundChangeDifficultyPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final Difficulty difficulty;
/*    */   
/*    */   public ServerboundChangeDifficultyPacket(Difficulty difficulty) {
/*  9 */     this.difficulty = difficulty;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundChangeDifficultyPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChangeDifficultyPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundChangeDifficultyPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChangeDifficultyPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundChangeDifficultyPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundChangeDifficultyPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Difficulty difficulty() {
/*  9 */     return this.difficulty;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<ByteBuf, ServerboundChangeDifficultyPacket> STREAM_CODEC = StreamCodec.composite(Difficulty.STREAM_CODEC, ServerboundChangeDifficultyPacket::difficulty, ServerboundChangeDifficultyPacket::new);
/*    */   
/*    */   public PacketType<ServerboundChangeDifficultyPacket> type() {
/* 19 */     return GamePacketTypes.SERVERBOUND_CHANGE_DIFFICULTY;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 24 */     listener.handleChangeDifficulty(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundChangeDifficultyPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */