/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundCustomChatCompletionsPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Action action;
/*    */   
/*    */   private final List<String> entries;
/*    */   
/*    */   public ClientboundCustomChatCompletionsPacket(Action action, List<String> entries) {
/* 12 */     this.action = action;
/* 12 */     this.entries = entries;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Action action() {
/* 12 */     return this.action;
/*    */   }
/*    */   
/*    */   public List<String> entries() {
/* 12 */     return this.entries;
/*    */   }
/*    */   
/* 13 */   public static final StreamCodec<FriendlyByteBuf, ClientboundCustomChatCompletionsPacket> STREAM_CODEC = Packet.codec(ClientboundCustomChatCompletionsPacket::write, ClientboundCustomChatCompletionsPacket::new);
/*    */   
/*    */   public enum Action {
/* 16 */     ADD, REMOVE, SET;
/*    */   }
/*    */   
/*    */   private ClientboundCustomChatCompletionsPacket(FriendlyByteBuf input) {
/* 22 */     this((Action)input
/* 23 */         .readEnum(Action.class), input
/* 24 */         .readList(FriendlyByteBuf::readUtf));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 29 */     output.writeEnum(this.action);
/* 30 */     output.writeCollection(this.entries, FriendlyByteBuf::writeUtf);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundCustomChatCompletionsPacket> type() {
/* 35 */     return GamePacketTypes.CLIENTBOUND_CUSTOM_CHAT_COMPLETIONS;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 40 */     listener.handleCustomChatCompletions(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundCustomChatCompletionsPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */