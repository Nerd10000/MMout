/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.UUID;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundLoginFinishedPacket extends Record implements Packet<ClientLoginPacketListener> {
/*    */   private final GameProfile gameProfile;
/*    */   
/*    */   private final UUID sessionId;
/*    */   
/*    */   public ClientboundLoginFinishedPacket(GameProfile gameProfile, UUID sessionId) {
/* 13 */     this.gameProfile = gameProfile;
/* 13 */     this.sessionId = sessionId;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public GameProfile gameProfile() {
/* 13 */     return this.gameProfile;
/*    */   }
/*    */   
/*    */   public UUID sessionId() {
/* 13 */     return this.sessionId;
/*    */   }
/*    */   
/* 17 */   public static final StreamCodec<ByteBuf, ClientboundLoginFinishedPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.GAME_PROFILE, ClientboundLoginFinishedPacket::gameProfile, UUIDUtil.STREAM_CODEC, ClientboundLoginFinishedPacket::sessionId, ClientboundLoginFinishedPacket::new);
/*    */   
/*    */   public PacketType<ClientboundLoginFinishedPacket> type() {
/* 25 */     return LoginPacketTypes.CLIENTBOUND_LOGIN_FINISHED;
/*    */   }
/*    */   
/*    */   public void handle(ClientLoginPacketListener listener) {
/* 30 */     listener.handleLoginFinished(this);
/*    */   }
/*    */   
/*    */   public boolean isTerminal() {
/* 35 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\login\ClientboundLoginFinishedPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */