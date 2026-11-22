/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import net.minecraft.commands.arguments.ArgumentSignatures;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.LastSeenMessages;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ServerboundChatCommandSignedPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final String command;
/*    */   
/*    */   private final Instant timeStamp;
/*    */   
/*    */   private final long salt;
/*    */   
/*    */   private final ArgumentSignatures argumentSignatures;
/*    */   
/*    */   private final LastSeenMessages.Update lastSeenMessages;
/*    */   
/*    */   public ServerboundChatCommandSignedPacket(String command, Instant timeStamp, long salt, ArgumentSignatures argumentSignatures, LastSeenMessages.Update lastSeenMessages) {
/* 12 */     this.command = command;
/* 12 */     this.timeStamp = timeStamp;
/* 12 */     this.salt = salt;
/* 12 */     this.argumentSignatures = argumentSignatures;
/* 12 */     this.lastSeenMessages = lastSeenMessages;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String command() {
/* 12 */     return this.command;
/*    */   }
/*    */   
/*    */   public Instant timeStamp() {
/* 12 */     return this.timeStamp;
/*    */   }
/*    */   
/*    */   public long salt() {
/* 12 */     return this.salt;
/*    */   }
/*    */   
/*    */   public ArgumentSignatures argumentSignatures() {
/* 12 */     return this.argumentSignatures;
/*    */   }
/*    */   
/*    */   public LastSeenMessages.Update lastSeenMessages() {
/* 12 */     return this.lastSeenMessages;
/*    */   }
/*    */   
/* 13 */   public static final StreamCodec<FriendlyByteBuf, ServerboundChatCommandSignedPacket> STREAM_CODEC = Packet.codec(ServerboundChatCommandSignedPacket::write, ServerboundChatCommandSignedPacket::new);
/*    */   
/*    */   private ServerboundChatCommandSignedPacket(FriendlyByteBuf input) {
/* 16 */     this(input
/* 17 */         .readUtf(), input
/* 18 */         .readInstant(), input
/* 19 */         .readLong(), new ArgumentSignatures(input), new LastSeenMessages.Update(input));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 26 */     output.writeUtf(this.command);
/* 27 */     output.writeInstant(this.timeStamp);
/* 28 */     output.writeLong(this.salt);
/* 29 */     this.argumentSignatures.write(output);
/* 30 */     this.lastSeenMessages.write(output);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundChatCommandSignedPacket> type() {
/* 35 */     return GamePacketTypes.SERVERBOUND_CHAT_COMMAND_SIGNED;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 40 */     listener.handleSignedChatCommand(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundChatCommandSignedPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */