/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ServerboundContainerButtonClickPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int containerId;
/*    */   
/*    */   private final int buttonId;
/*    */   
/*    */   public ServerboundContainerButtonClickPacket(int containerId, int buttonId) {
/*  9 */     this.containerId = containerId;
/*  9 */     this.buttonId = buttonId;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundContainerButtonClickPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerButtonClickPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundContainerButtonClickPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerButtonClickPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundContainerButtonClickPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerButtonClickPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int containerId() {
/*  9 */     return this.containerId;
/*    */   }
/*    */   
/*    */   public int buttonId() {
/*  9 */     return this.buttonId;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundContainerButtonClickPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ServerboundContainerButtonClickPacket::containerId, ByteBufCodecs.VAR_INT, ServerboundContainerButtonClickPacket::buttonId, ServerboundContainerButtonClickPacket::new);
/*    */   
/*    */   public PacketType<ServerboundContainerButtonClickPacket> type() {
/* 18 */     return GamePacketTypes.SERVERBOUND_CONTAINER_BUTTON_CLICK;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 23 */     listener.handleContainerButtonClick(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundContainerButtonClickPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */