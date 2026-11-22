/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class ClientboundMoveMinecartPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int entityId;
/*    */   
/*    */   private final List<NewMinecartBehavior.MinecartStep> lerpSteps;
/*    */   
/*    */   public ClientboundMoveMinecartPacket(int entityId, List<NewMinecartBehavior.MinecartStep> lerpSteps) {
/* 15 */     this.entityId = entityId;
/* 15 */     this.lerpSteps = lerpSteps;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundMoveMinecartPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundMoveMinecartPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundMoveMinecartPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundMoveMinecartPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundMoveMinecartPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundMoveMinecartPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int entityId() {
/* 15 */     return this.entityId;
/*    */   }
/*    */   
/*    */   public List<NewMinecartBehavior.MinecartStep> lerpSteps() {
/* 15 */     return this.lerpSteps;
/*    */   }
/*    */   
/* 16 */   public static final StreamCodec<FriendlyByteBuf, ClientboundMoveMinecartPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundMoveMinecartPacket::entityId, NewMinecartBehavior.MinecartStep.STREAM_CODEC
/*    */       
/* 18 */       .apply(ByteBufCodecs.list()), ClientboundMoveMinecartPacket::lerpSteps, ClientboundMoveMinecartPacket::new);
/*    */   
/*    */   public PacketType<ClientboundMoveMinecartPacket> type() {
/* 24 */     return GamePacketTypes.CLIENTBOUND_MOVE_MINECART_ALONG_TRACK;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 29 */     listener.handleMinecartAlongTrack(this);
/*    */   }
/*    */   
/*    */   public Entity getEntity(Level level) {
/* 33 */     return level.getEntity(this.entityId);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundMoveMinecartPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */