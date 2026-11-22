/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ServerboundSelectBundleItemPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int slotId;
/*    */   
/*    */   private final int selectedItemIndex;
/*    */   
/*    */   public ServerboundSelectBundleItemPacket(int slotId, int selectedItemIndex) {
/*  9 */     this.slotId = slotId;
/*  9 */     this.selectedItemIndex = selectedItemIndex;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundSelectBundleItemPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSelectBundleItemPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundSelectBundleItemPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSelectBundleItemPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundSelectBundleItemPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundSelectBundleItemPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int slotId() {
/*  9 */     return this.slotId;
/*    */   }
/*    */   
/*    */   public int selectedItemIndex() {
/*  9 */     return this.selectedItemIndex;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSelectBundleItemPacket> STREAM_CODEC = Packet.codec(ServerboundSelectBundleItemPacket::write, ServerboundSelectBundleItemPacket::new);
/*    */   
/*    */   private ServerboundSelectBundleItemPacket(FriendlyByteBuf input) {
/* 13 */     this(input.readVarInt(), input.readVarInt());
/* 14 */     if (this.selectedItemIndex < 0 && this.selectedItemIndex != -1)
/* 15 */       throw new IllegalArgumentException("Invalid selectedItemIndex: " + this.selectedItemIndex); 
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 20 */     output.writeVarInt(this.slotId);
/* 21 */     output.writeVarInt(this.selectedItemIndex);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundSelectBundleItemPacket> type() {
/* 26 */     return GamePacketTypes.SERVERBOUND_BUNDLE_ITEM_SELECTED;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 31 */     listener.handleBundleItemSelectedPacket(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundSelectBundleItemPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */