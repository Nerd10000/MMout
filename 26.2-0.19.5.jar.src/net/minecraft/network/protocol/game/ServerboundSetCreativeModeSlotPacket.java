/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ServerboundSetCreativeModeSlotPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final short slotNum;
/*    */   
/*    */   private final ItemStack itemStack;
/*    */   
/*    */   public ServerboundSetCreativeModeSlotPacket(short slotNum, ItemStack itemStack) {
/* 10 */     this.slotNum = slotNum;
/* 10 */     this.itemStack = itemStack;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public short slotNum() {
/* 10 */     return this.slotNum;
/*    */   }
/*    */   
/*    */   public ItemStack itemStack() {
/* 10 */     return this.itemStack;
/*    */   }
/*    */   
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSetCreativeModeSlotPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.SHORT, ServerboundSetCreativeModeSlotPacket::slotNum, 
/*    */       
/* 16 */       ItemStack.validatedStreamCodec(ItemStack.OPTIONAL_UNTRUSTED_STREAM_CODEC), ServerboundSetCreativeModeSlotPacket::itemStack, ServerboundSetCreativeModeSlotPacket::new);
/*    */   
/*    */   public ServerboundSetCreativeModeSlotPacket(int slotNum, ItemStack itemStack) {
/* 21 */     this((short)slotNum, itemStack);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundSetCreativeModeSlotPacket> type() {
/* 26 */     return GamePacketTypes.SERVERBOUND_SET_CREATIVE_MODE_SLOT;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 31 */     listener.handleSetCreativeModeSlot(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundSetCreativeModeSlotPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */