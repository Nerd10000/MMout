/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundDisguisedChatPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Component message;
/*    */   
/*    */   private final ChatType.Bound chatType;
/*    */   
/*    */   public ClientboundDisguisedChatPacket(Component message, ChatType.Bound chatType) {
/* 11 */     this.message = message;
/* 11 */     this.chatType = chatType;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundDisguisedChatPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDisguisedChatPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundDisguisedChatPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDisguisedChatPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundDisguisedChatPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundDisguisedChatPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component message() {
/* 11 */     return this.message;
/*    */   }
/*    */   
/*    */   public ChatType.Bound chatType() {
/* 11 */     return this.chatType;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDisguisedChatPacket> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_STREAM_CODEC, ClientboundDisguisedChatPacket::message, ChatType.Bound.STREAM_CODEC, ClientboundDisguisedChatPacket::chatType, ClientboundDisguisedChatPacket::new);
/*    */   
/*    */   public PacketType<ClientboundDisguisedChatPacket> type() {
/* 20 */     return GamePacketTypes.CLIENTBOUND_DISGUISED_CHAT;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 25 */     listener.handleDisguisedChat(this);
/*    */   }
/*    */   
/*    */   public boolean isSkippable() {
/* 30 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundDisguisedChatPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */