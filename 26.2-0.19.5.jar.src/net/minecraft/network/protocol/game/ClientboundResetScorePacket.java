/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundResetScorePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final String owner;
/*    */   
/*    */   private final String objectiveName;
/*    */   
/*    */   public ClientboundResetScorePacket(String owner, String objectiveName) {
/*  9 */     this.owner = owner;
/*  9 */     this.objectiveName = objectiveName;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundResetScorePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundResetScorePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundResetScorePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundResetScorePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundResetScorePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundResetScorePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String owner() {
/*  9 */     return this.owner;
/*    */   }
/*    */   
/*    */   public String objectiveName() {
/*  9 */     return this.objectiveName;
/*    */   }
/*    */   
/* 13 */   public static final StreamCodec<FriendlyByteBuf, ClientboundResetScorePacket> STREAM_CODEC = Packet.codec(ClientboundResetScorePacket::write, ClientboundResetScorePacket::new);
/*    */   
/*    */   private ClientboundResetScorePacket(FriendlyByteBuf input) {
/* 16 */     this(input
/* 17 */         .readUtf(), (String)input
/* 18 */         .readNullable(FriendlyByteBuf::readUtf));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 23 */     output.writeUtf(this.owner);
/* 24 */     output.writeNullable(this.objectiveName, FriendlyByteBuf::writeUtf);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundResetScorePacket> type() {
/* 29 */     return GamePacketTypes.CLIENTBOUND_RESET_SCORE;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 34 */     listener.handleResetScore(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundResetScorePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */