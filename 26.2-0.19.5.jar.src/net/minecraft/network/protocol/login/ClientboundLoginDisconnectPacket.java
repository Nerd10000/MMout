/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ 
/*    */ public final class ClientboundLoginDisconnectPacket extends Record implements Packet<ClientLoginPacketListener> {
/*    */   private final Component reason;
/*    */   
/*    */   public ClientboundLoginDisconnectPacket(Component reason) {
/* 16 */     this.reason = reason;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/login/ClientboundLoginDisconnectPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginDisconnectPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/login/ClientboundLoginDisconnectPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginDisconnectPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/login/ClientboundLoginDisconnectPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginDisconnectPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component reason() {
/* 16 */     return this.reason;
/*    */   }
/*    */   
/* 19 */   private static final RegistryOps<JsonElement> OPS = RegistryAccess.EMPTY.createSerializationContext((DynamicOps)JsonOps.INSTANCE);
/*    */   
/* 23 */   public static final StreamCodec<ByteBuf, ClientboundLoginDisconnectPacket> STREAM_CODEC = StreamCodec.composite(
/*    */       
/* 25 */       ByteBufCodecs.lenientJson(262144).apply(ByteBufCodecs.fromCodec((DynamicOps)OPS, ComponentSerialization.CODEC)), ClientboundLoginDisconnectPacket::reason, ClientboundLoginDisconnectPacket::new);
/*    */   
/*    */   public PacketType<ClientboundLoginDisconnectPacket> type() {
/* 31 */     return LoginPacketTypes.CLIENTBOUND_LOGIN_DISCONNECT;
/*    */   }
/*    */   
/*    */   public void handle(ClientLoginPacketListener listener) {
/* 36 */     listener.handleDisconnect(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\login\ClientboundLoginDisconnectPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */