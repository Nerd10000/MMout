/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ServerboundHelloPacket extends Record implements Packet<ServerLoginPacketListener> {
/*    */   private final String name;
/*    */   
/*    */   private final UUID profileId;
/*    */   
/*    */   public ServerboundHelloPacket(String name, UUID profileId) {
/* 11 */     this.name = name;
/* 11 */     this.profileId = profileId;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String name() {
/* 11 */     return this.name;
/*    */   }
/*    */   
/*    */   public UUID profileId() {
/* 11 */     return this.profileId;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ServerboundHelloPacket> STREAM_CODEC = Packet.codec(ServerboundHelloPacket::write, ServerboundHelloPacket::new);
/*    */   
/*    */   private ServerboundHelloPacket(FriendlyByteBuf input) {
/* 15 */     this(input.readUtf(16), input.readUUID());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 19 */     output.writeUtf(this.name, 16);
/* 20 */     output.writeUUID(this.profileId);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundHelloPacket> type() {
/* 25 */     return LoginPacketTypes.SERVERBOUND_HELLO;
/*    */   }
/*    */   
/*    */   public void handle(ServerLoginPacketListener listener) {
/* 30 */     listener.handleHello(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\login\ServerboundHelloPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */