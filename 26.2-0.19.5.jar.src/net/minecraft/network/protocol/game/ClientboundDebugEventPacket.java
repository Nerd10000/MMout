/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.util.debug.DebugSubscription;
/*    */ 
/*    */ public final class ClientboundDebugEventPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final DebugSubscription.Event<?> event;
/*    */   
/*    */   public ClientboundDebugEventPacket(DebugSubscription.Event<?> event) {
/*  9 */     this.event = event;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundDebugEventPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugEventPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundDebugEventPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugEventPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundDebugEventPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugEventPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public DebugSubscription.Event<?> event() {
/*  9 */     return this.event;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDebugEventPacket> STREAM_CODEC = StreamCodec.composite(DebugSubscription.Event.STREAM_CODEC, ClientboundDebugEventPacket::event, ClientboundDebugEventPacket::new);
/*    */   
/*    */   public PacketType<ClientboundDebugEventPacket> type() {
/* 19 */     return GamePacketTypes.CLIENTBOUND_DEBUG_EVENT;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 24 */     listener.handleDebugEvent(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundDebugEventPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */