/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.MessageSignature;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundDeleteChatPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final MessageSignature.Packed messageSignature;
/*    */   
/*    */   public ClientboundDeleteChatPacket(MessageSignature.Packed messageSignature) {
/*  9 */     this.messageSignature = messageSignature;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundDeleteChatPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDeleteChatPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundDeleteChatPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDeleteChatPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundDeleteChatPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundDeleteChatPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MessageSignature.Packed messageSignature() {
/*  9 */     return this.messageSignature;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundDeleteChatPacket> STREAM_CODEC = Packet.codec(ClientboundDeleteChatPacket::write, ClientboundDeleteChatPacket::new);
/*    */   
/*    */   private ClientboundDeleteChatPacket(FriendlyByteBuf input) {
/* 13 */     this(MessageSignature.Packed.read(input));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 17 */     MessageSignature.Packed.write(output, this.messageSignature);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundDeleteChatPacket> type() {
/* 22 */     return GamePacketTypes.CLIENTBOUND_DELETE_CHAT;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 27 */     listener.handleDeleteChat(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundDeleteChatPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */