/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.FilterMask;
/*    */ import net.minecraft.network.chat.MessageSignature;
/*    */ import net.minecraft.network.chat.SignedMessageBody;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.codec.StreamEncoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundPlayerChatPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int globalIndex;
/*    */   
/*    */   private final UUID sender;
/*    */   
/*    */   private final int index;
/*    */   
/*    */   private final MessageSignature signature;
/*    */   
/*    */   private final SignedMessageBody.Packed body;
/*    */   
/*    */   private final Component unsignedContent;
/*    */   
/*    */   private final FilterMask filterMask;
/*    */   
/*    */   private final ChatType.Bound chatType;
/*    */   
/*    */   public ClientboundPlayerChatPacket(int globalIndex, UUID sender, int index, MessageSignature signature, SignedMessageBody.Packed body, Component unsignedContent, FilterMask filterMask, ChatType.Bound chatType) {
/* 18 */     this.globalIndex = globalIndex;
/* 18 */     this.sender = sender;
/* 18 */     this.index = index;
/* 18 */     this.signature = signature;
/* 18 */     this.body = body;
/* 18 */     this.unsignedContent = unsignedContent;
/* 18 */     this.filterMask = filterMask;
/* 18 */     this.chatType = chatType;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int globalIndex() {
/* 18 */     return this.globalIndex;
/*    */   }
/*    */   
/*    */   public UUID sender() {
/* 18 */     return this.sender;
/*    */   }
/*    */   
/*    */   public int index() {
/* 18 */     return this.index;
/*    */   }
/*    */   
/*    */   public MessageSignature signature() {
/* 18 */     return this.signature;
/*    */   }
/*    */   
/*    */   public SignedMessageBody.Packed body() {
/* 18 */     return this.body;
/*    */   }
/*    */   
/*    */   public Component unsignedContent() {
/* 18 */     return this.unsignedContent;
/*    */   }
/*    */   
/*    */   public FilterMask filterMask() {
/* 18 */     return this.filterMask;
/*    */   }
/*    */   
/*    */   public ChatType.Bound chatType() {
/* 18 */     return this.chatType;
/*    */   }
/*    */   
/* 29 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlayerChatPacket> STREAM_CODEC = Packet.codec(ClientboundPlayerChatPacket::write, ClientboundPlayerChatPacket::new);
/*    */   
/*    */   private ClientboundPlayerChatPacket(RegistryFriendlyByteBuf input) {
/* 32 */     this(input
/* 33 */         .readVarInt(), input
/* 34 */         .readUUID(), input
/* 35 */         .readVarInt(), (MessageSignature)input
/* 36 */         .readNullable(MessageSignature::read), new SignedMessageBody.Packed((FriendlyByteBuf)input), 
/*    */         
/* 38 */         (Component)FriendlyByteBuf.readNullable((ByteBuf)input, (StreamDecoder)ComponentSerialization.TRUSTED_STREAM_CODEC), 
/* 39 */         FilterMask.read((FriendlyByteBuf)input), (ChatType.Bound)ChatType.Bound.STREAM_CODEC
/* 40 */         .decode(input));
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 45 */     output.writeVarInt(this.globalIndex);
/* 46 */     output.writeUUID(this.sender);
/* 47 */     output.writeVarInt(this.index);
/* 48 */     output.writeNullable(this.signature, MessageSignature::write);
/* 49 */     this.body.write((FriendlyByteBuf)output);
/* 50 */     FriendlyByteBuf.writeNullable((ByteBuf)output, this.unsignedContent, (StreamEncoder)ComponentSerialization.TRUSTED_STREAM_CODEC);
/* 51 */     FilterMask.write((FriendlyByteBuf)output, this.filterMask);
/* 52 */     ChatType.Bound.STREAM_CODEC.encode(output, this.chatType);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundPlayerChatPacket> type() {
/* 57 */     return GamePacketTypes.CLIENTBOUND_PLAYER_CHAT;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 62 */     listener.handlePlayerChat(this);
/*    */   }
/*    */   
/*    */   public boolean isSkippable() {
/* 67 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundPlayerChatPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */