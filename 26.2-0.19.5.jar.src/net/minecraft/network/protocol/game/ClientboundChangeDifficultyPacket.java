/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.Difficulty;
/*    */ 
/*    */ public final class ClientboundChangeDifficultyPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Difficulty difficulty;
/*    */   
/*    */   private final boolean locked;
/*    */   
/*    */   public ClientboundChangeDifficultyPacket(Difficulty difficulty, boolean locked) {
/* 10 */     this.difficulty = difficulty;
/* 10 */     this.locked = locked;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundChangeDifficultyPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundChangeDifficultyPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundChangeDifficultyPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundChangeDifficultyPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundChangeDifficultyPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundChangeDifficultyPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Difficulty difficulty() {
/* 10 */     return this.difficulty;
/*    */   }
/*    */   
/*    */   public boolean locked() {
/* 10 */     return this.locked;
/*    */   }
/*    */   
/* 14 */   public static final StreamCodec<ByteBuf, ClientboundChangeDifficultyPacket> STREAM_CODEC = StreamCodec.composite(Difficulty.STREAM_CODEC, ClientboundChangeDifficultyPacket::difficulty, ByteBufCodecs.BOOL, ClientboundChangeDifficultyPacket::locked, ClientboundChangeDifficultyPacket::new);
/*    */   
/*    */   public PacketType<ClientboundChangeDifficultyPacket> type() {
/* 22 */     return GamePacketTypes.CLIENTBOUND_CHANGE_DIFFICULTY;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 27 */     listener.handleChangeDifficulty(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundChangeDifficultyPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */