/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.RemoteChatSession;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ServerboundChatSessionUpdatePacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final RemoteChatSession.Data chatSession;
/*    */   
/*    */   public ServerboundChatSessionUpdatePacket(RemoteChatSession.Data chatSession) {
/*  9 */     this.chatSession = chatSession;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundChatSessionUpdatePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatSessionUpdatePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundChatSessionUpdatePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatSessionUpdatePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundChatSessionUpdatePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatSessionUpdatePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public RemoteChatSession.Data chatSession() {
/*  9 */     return this.chatSession;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundChatSessionUpdatePacket> STREAM_CODEC = Packet.codec(ServerboundChatSessionUpdatePacket::write, ServerboundChatSessionUpdatePacket::new);
/*    */   
/*    */   private ServerboundChatSessionUpdatePacket(FriendlyByteBuf input) {
/* 13 */     this(RemoteChatSession.Data.read(input));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 17 */     RemoteChatSession.Data.write(output, this.chatSession);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundChatSessionUpdatePacket> type() {
/* 22 */     return GamePacketTypes.SERVERBOUND_CHAT_SESSION_UPDATE;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 27 */     listener.handleChatSessionUpdate(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundChatSessionUpdatePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */