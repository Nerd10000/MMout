/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.util.debug.DebugSubscription;
/*    */ 
/*    */ public final class ClientboundDebugBlockValuePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final BlockPos blockPos;
/*    */   
/*    */   private final DebugSubscription.Update<?> update;
/*    */   
/*    */   public ClientboundDebugBlockValuePacket(BlockPos blockPos, DebugSubscription.Update<?> update) {
/* 10 */     this.blockPos = blockPos;
/* 10 */     this.update = update;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundDebugBlockValuePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugBlockValuePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundDebugBlockValuePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugBlockValuePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundDebugBlockValuePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugBlockValuePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockPos blockPos() {
/* 10 */     return this.blockPos;
/*    */   }
/*    */   
/*    */   public DebugSubscription.Update<?> update() {
/* 10 */     return this.update;
/*    */   }
/*    */   
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDebugBlockValuePacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ClientboundDebugBlockValuePacket::blockPos, DebugSubscription.Update.STREAM_CODEC, ClientboundDebugBlockValuePacket::update, ClientboundDebugBlockValuePacket::new);
/*    */   
/*    */   public PacketType<ClientboundDebugBlockValuePacket> type() {
/* 22 */     return GamePacketTypes.CLIENTBOUND_DEBUG_BLOCK_VALUE;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 27 */     listener.handleDebugBlockValue(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundDebugBlockValuePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */