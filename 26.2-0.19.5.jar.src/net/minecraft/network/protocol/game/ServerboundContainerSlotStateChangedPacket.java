/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ServerboundContainerSlotStateChangedPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int slotId;
/*    */   
/*    */   private final int containerId;
/*    */   
/*    */   private final boolean newState;
/*    */   
/*    */   public ServerboundContainerSlotStateChangedPacket(int slotId, int containerId, boolean newState) {
/*  8 */     this.slotId = slotId;
/*  8 */     this.containerId = containerId;
/*  8 */     this.newState = newState;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundContainerSlotStateChangedPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerSlotStateChangedPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundContainerSlotStateChangedPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerSlotStateChangedPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundContainerSlotStateChangedPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerSlotStateChangedPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int slotId() {
/*  8 */     return this.slotId;
/*    */   }
/*    */   
/*    */   public int containerId() {
/*  8 */     return this.containerId;
/*    */   }
/*    */   
/*    */   public boolean newState() {
/*  8 */     return this.newState;
/*    */   }
/*    */   
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundContainerSlotStateChangedPacket> STREAM_CODEC = Packet.codec(ServerboundContainerSlotStateChangedPacket::write, ServerboundContainerSlotStateChangedPacket::new);
/*    */   
/*    */   private ServerboundContainerSlotStateChangedPacket(FriendlyByteBuf input) {
/* 12 */     this(input.readVarInt(), input.readContainerId(), input.readBoolean());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 16 */     output.writeVarInt(this.slotId);
/* 17 */     output.writeContainerId(this.containerId);
/* 18 */     output.writeBoolean(this.newState);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundContainerSlotStateChangedPacket> type() {
/* 23 */     return GamePacketTypes.SERVERBOUND_CONTAINER_SLOT_STATE_CHANGED;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 28 */     listener.handleContainerSlotStateChanged(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundContainerSlotStateChangedPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */