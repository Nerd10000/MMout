/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.network.protocol.login.custom.CustomQueryAnswerPayload;
/*    */ import net.minecraft.network.protocol.login.custom.DiscardedQueryAnswerPayload;
/*    */ 
/*    */ public final class ServerboundCustomQueryAnswerPacket extends Record implements Packet<ServerLoginPacketListener> {
/*    */   private final int transactionId;
/*    */   
/*    */   private final CustomQueryAnswerPayload payload;
/*    */   
/*    */   public ServerboundCustomQueryAnswerPacket(int transactionId, CustomQueryAnswerPayload payload) {
/* 13 */     this.transactionId = transactionId;
/* 13 */     this.payload = payload;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int transactionId() {
/* 13 */     return this.transactionId;
/*    */   }
/*    */   
/*    */   public CustomQueryAnswerPayload payload() {
/* 13 */     return this.payload;
/*    */   }
/*    */   
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ServerboundCustomQueryAnswerPacket> STREAM_CODEC = Packet.codec(ServerboundCustomQueryAnswerPacket::write, ServerboundCustomQueryAnswerPacket::read);
/*    */   
/*    */   private static final int MAX_PAYLOAD_SIZE = 1048576;
/*    */   
/*    */   private static ServerboundCustomQueryAnswerPacket read(FriendlyByteBuf input) {
/* 19 */     int transactionId = input.readVarInt();
/* 20 */     return new ServerboundCustomQueryAnswerPacket(transactionId, 
/*    */         
/* 22 */         readPayload(transactionId, input));
/*    */   }
/*    */   
/*    */   private static CustomQueryAnswerPayload readPayload(int transactionId, FriendlyByteBuf input) {
/* 32 */     return readUnknownPayload(input);
/*    */   }
/*    */   
/*    */   private static CustomQueryAnswerPayload readUnknownPayload(FriendlyByteBuf input) {
/* 36 */     int length = input.readableBytes();
/* 37 */     if (length < 0 || length > 1048576)
/* 38 */       throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes"); 
/* 40 */     input.skipBytes(length);
/* 41 */     return (CustomQueryAnswerPayload)DiscardedQueryAnswerPayload.INSTANCE;
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 45 */     output.writeVarInt(this.transactionId);
/* 46 */     output.writeNullable(this.payload, (buf, data) -> data.write(buf));
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundCustomQueryAnswerPacket> type() {
/* 51 */     return LoginPacketTypes.SERVERBOUND_CUSTOM_QUERY_ANSWER;
/*    */   }
/*    */   
/*    */   public void handle(ServerLoginPacketListener listener) {
/* 56 */     listener.handleCustomQueryPacket(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\login\ServerboundCustomQueryAnswerPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */