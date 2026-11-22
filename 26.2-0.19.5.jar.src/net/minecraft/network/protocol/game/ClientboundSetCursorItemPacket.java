/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ClientboundSetCursorItemPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final ItemStack contents;
/*    */   
/*    */   public ClientboundSetCursorItemPacket(ItemStack contents) {
/*  9 */     this.contents = contents;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetCursorItemPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetCursorItemPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetCursorItemPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetCursorItemPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetCursorItemPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetCursorItemPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ItemStack contents() {
/*  9 */     return this.contents;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetCursorItemPacket> STREAM_CODEC = StreamCodec.composite(ItemStack.OPTIONAL_STREAM_CODEC, ClientboundSetCursorItemPacket::contents, ClientboundSetCursorItemPacket::new);
/*    */   
/*    */   public PacketType<ClientboundSetCursorItemPacket> type() {
/* 19 */     return GamePacketTypes.CLIENTBOUND_SET_CURSOR_ITEM;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 24 */     listener.handleSetCursorItem(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundSetCursorItemPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */