/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ClientboundSetEntityMotionPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int id;
/*    */   
/*    */   private final Vec3 movement;
/*    */   
/*    */   public ClientboundSetEntityMotionPacket(int id, Vec3 movement) {
/* 11 */     this.id = id;
/* 11 */     this.movement = movement;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int id() {
/* 11 */     return this.id;
/*    */   }
/*    */   
/*    */   public Vec3 movement() {
/* 11 */     return this.movement;
/*    */   }
/*    */   
/* 15 */   public static final StreamCodec<ByteBuf, ClientboundSetEntityMotionPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundSetEntityMotionPacket::id, Vec3.LP_STREAM_CODEC, ClientboundSetEntityMotionPacket::movement, ClientboundSetEntityMotionPacket::new);
/*    */   
/*    */   public ClientboundSetEntityMotionPacket(Entity entity) {
/* 22 */     this(entity.getId(), entity.getDeltaMovement());
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundSetEntityMotionPacket> type() {
/* 27 */     return GamePacketTypes.CLIENTBOUND_SET_ENTITY_MOTION;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 32 */     listener.handleSetEntityMotion(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundSetEntityMotionPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */