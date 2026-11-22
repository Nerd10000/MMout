/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ClientboundContainerSetContentPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int containerId;
/*    */   
/*    */   private final int stateId;
/*    */   
/*    */   private final List<ItemStack> items;
/*    */   
/*    */   private final ItemStack carriedItem;
/*    */   
/*    */   public ClientboundContainerSetContentPacket(int containerId, int stateId, List<ItemStack> items, ItemStack carriedItem) {
/* 12 */     this.containerId = containerId;
/* 12 */     this.stateId = stateId;
/* 12 */     this.items = items;
/* 12 */     this.carriedItem = carriedItem;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int containerId() {
/* 12 */     return this.containerId;
/*    */   }
/*    */   
/*    */   public int stateId() {
/* 12 */     return this.stateId;
/*    */   }
/*    */   
/*    */   public List<ItemStack> items() {
/* 12 */     return this.items;
/*    */   }
/*    */   
/*    */   public ItemStack carriedItem() {
/* 12 */     return this.carriedItem;
/*    */   }
/*    */   
/* 19 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundContainerSetContentPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ClientboundContainerSetContentPacket::containerId, ByteBufCodecs.VAR_INT, ClientboundContainerSetContentPacket::stateId, ItemStack.OPTIONAL_LIST_STREAM_CODEC, ClientboundContainerSetContentPacket::items, ItemStack.OPTIONAL_STREAM_CODEC, ClientboundContainerSetContentPacket::carriedItem, ClientboundContainerSetContentPacket::new);
/*    */   
/*    */   public PacketType<ClientboundContainerSetContentPacket> type() {
/* 29 */     return GamePacketTypes.CLIENTBOUND_CONTAINER_SET_CONTENT;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 34 */     listener.handleContainerContent(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundContainerSetContentPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */