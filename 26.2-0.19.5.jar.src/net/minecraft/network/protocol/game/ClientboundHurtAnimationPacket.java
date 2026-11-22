/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public final class ClientboundHurtAnimationPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int id;
/*    */   
/*    */   private final float yaw;
/*    */   
/*    */   public ClientboundHurtAnimationPacket(int id, float yaw) {
/*  9 */     this.id = id;
/*  9 */     this.yaw = yaw;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int id() {
/*  9 */     return this.id;
/*    */   }
/*    */   
/*    */   public float yaw() {
/*  9 */     return this.yaw;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundHurtAnimationPacket> STREAM_CODEC = Packet.codec(ClientboundHurtAnimationPacket::write, ClientboundHurtAnimationPacket::new);
/*    */   
/*    */   public ClientboundHurtAnimationPacket(LivingEntity entity) {
/* 13 */     this(entity.getId(), entity.getHurtDir());
/*    */   }
/*    */   
/*    */   private ClientboundHurtAnimationPacket(FriendlyByteBuf input) {
/* 17 */     this(input.readVarInt(), input.readFloat());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 21 */     output.writeVarInt(this.id);
/* 22 */     output.writeFloat(this.yaw);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundHurtAnimationPacket> type() {
/* 27 */     return GamePacketTypes.CLIENTBOUND_HURT_ANIMATION;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 32 */     listener.handleHurtAnimation(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundHurtAnimationPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */