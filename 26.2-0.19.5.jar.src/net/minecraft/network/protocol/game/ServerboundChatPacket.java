/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.LastSeenMessages;
/*    */ import net.minecraft.network.chat.MessageSignature;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ServerboundChatPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final String message;
/*    */   
/*    */   private final Instant timeStamp;
/*    */   
/*    */   private final long salt;
/*    */   
/*    */   private final MessageSignature signature;
/*    */   
/*    */   private final LastSeenMessages.Update lastSeenMessages;
/*    */   
/*    */   public ServerboundChatPacket(String message, Instant timeStamp, long salt, MessageSignature signature, LastSeenMessages.Update lastSeenMessages) {
/* 14 */     this.message = message;
/* 14 */     this.timeStamp = timeStamp;
/* 14 */     this.salt = salt;
/* 14 */     this.signature = signature;
/* 14 */     this.lastSeenMessages = lastSeenMessages;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundChatPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundChatPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundChatPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String message() {
/* 14 */     return this.message;
/*    */   }
/*    */   
/*    */   public Instant timeStamp() {
/* 14 */     return this.timeStamp;
/*    */   }
/*    */   
/*    */   public long salt() {
/* 14 */     return this.salt;
/*    */   }
/*    */   
/*    */   public MessageSignature signature() {
/* 14 */     return this.signature;
/*    */   }
/*    */   
/*    */   public LastSeenMessages.Update lastSeenMessages() {
/* 14 */     return this.lastSeenMessages;
/*    */   }
/*    */   
/* 15 */   public static final StreamCodec<FriendlyByteBuf, ServerboundChatPacket> STREAM_CODEC = Packet.codec(ServerboundChatPacket::write, ServerboundChatPacket::new);
/*    */   
/*    */   private ServerboundChatPacket(FriendlyByteBuf input) {
/* 18 */     this(input
/* 19 */         .readUtf(256), input
/* 20 */         .readInstant(), input
/* 21 */         .readLong(), (MessageSignature)input
/* 22 */         .readNullable(MessageSignature::read), new LastSeenMessages.Update(input));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeUtf(this.message, 256);
/* 29 */     output.writeInstant(this.timeStamp);
/* 30 */     output.writeLong(this.salt);
/* 31 */     output.writeNullable(this.signature, MessageSignature::write);
/* 32 */     this.lastSeenMessages.write(output);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundChatPacket> type() {
/* 37 */     return GamePacketTypes.SERVERBOUND_CHAT;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 42 */     listener.handleChat(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundChatPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */