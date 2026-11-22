/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundTestInstanceBlockStatus extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Component status;
/*    */   
/*    */   private final Optional<Vec3i> size;
/*    */   
/*    */   public ClientboundTestInstanceBlockStatus(Component status, Optional<Vec3i> size) {
/* 14 */     this.status = status;
/* 14 */     this.size = size;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundTestInstanceBlockStatus;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTestInstanceBlockStatus;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundTestInstanceBlockStatus;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTestInstanceBlockStatus;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundTestInstanceBlockStatus;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundTestInstanceBlockStatus;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component status() {
/* 14 */     return this.status;
/*    */   }
/*    */   
/*    */   public Optional<Vec3i> size() {
/* 14 */     return this.size;
/*    */   }
/*    */   
/* 15 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundTestInstanceBlockStatus> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.STREAM_CODEC, ClientboundTestInstanceBlockStatus::status, 
/*    */       
/* 17 */       ByteBufCodecs.optional(Vec3i.STREAM_CODEC), ClientboundTestInstanceBlockStatus::size, ClientboundTestInstanceBlockStatus::new);
/*    */   
/*    */   public PacketType<ClientboundTestInstanceBlockStatus> type() {
/* 23 */     return GamePacketTypes.CLIENTBOUND_TEST_INSTANCE_BLOCK_STATUS;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 28 */     listener.handleTestInstanceBlockStatus(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundTestInstanceBlockStatus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */