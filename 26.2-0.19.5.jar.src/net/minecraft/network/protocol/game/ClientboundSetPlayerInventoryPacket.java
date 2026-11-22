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
/*    */ public final class ClientboundSetPlayerInventoryPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int slot;
/*    */   
/*    */   private final ItemStack contents;
/*    */   
/*    */   public ClientboundSetPlayerInventoryPacket(int slot, ItemStack contents) {
/* 10 */     this.slot = slot;
/* 10 */     this.contents = contents;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetPlayerInventoryPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetPlayerInventoryPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetPlayerInventoryPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetPlayerInventoryPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetPlayerInventoryPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetPlayerInventoryPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int slot() {
/* 10 */     return this.slot;
/*    */   }
/*    */   
/*    */   public ItemStack contents() {
/* 10 */     return this.contents;
/*    */   }
/*    */   
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetPlayerInventoryPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundSetPlayerInventoryPacket::slot, ItemStack.OPTIONAL_STREAM_CODEC, ClientboundSetPlayerInventoryPacket::contents, ClientboundSetPlayerInventoryPacket::new);
/*    */   
/*    */   public PacketType<ClientboundSetPlayerInventoryPacket> type() {
/* 22 */     return GamePacketTypes.CLIENTBOUND_SET_PLAYER_INVENTORY;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 27 */     listener.handleSetPlayerInventory(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundSetPlayerInventoryPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */