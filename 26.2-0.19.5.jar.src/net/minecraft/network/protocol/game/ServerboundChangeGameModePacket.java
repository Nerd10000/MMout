/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ public final class ServerboundChangeGameModePacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final GameType mode;
/*    */   
/*    */   public ServerboundChangeGameModePacket(GameType mode) {
/*  9 */     this.mode = mode;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundChangeGameModePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChangeGameModePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundChangeGameModePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChangeGameModePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundChangeGameModePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundChangeGameModePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public GameType mode() {
/*  9 */     return this.mode;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<ByteBuf, ServerboundChangeGameModePacket> STREAM_CODEC = StreamCodec.composite(GameType.STREAM_CODEC, ServerboundChangeGameModePacket::mode, ServerboundChangeGameModePacket::new);
/*    */   
/*    */   public PacketType<ServerboundChangeGameModePacket> type() {
/* 19 */     return GamePacketTypes.SERVERBOUND_CHANGE_GAME_MODE;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 24 */     listener.handleChangeGameMode(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundChangeGameModePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */