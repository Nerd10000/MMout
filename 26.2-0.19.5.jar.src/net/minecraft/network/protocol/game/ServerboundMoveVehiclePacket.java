/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ServerboundMoveVehiclePacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final Vec3 position;
/*    */   
/*    */   private final float yRot;
/*    */   
/*    */   private final float xRot;
/*    */   
/*    */   private final boolean onGround;
/*    */   
/*    */   public ServerboundMoveVehiclePacket(Vec3 position, float yRot, float xRot, boolean onGround) {
/* 11 */     this.position = position;
/* 11 */     this.yRot = yRot;
/* 11 */     this.xRot = xRot;
/* 11 */     this.onGround = onGround;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 position() {
/* 11 */     return this.position;
/*    */   }
/*    */   
/*    */   public float yRot() {
/* 11 */     return this.yRot;
/*    */   }
/*    */   
/*    */   public float xRot() {
/* 11 */     return this.xRot;
/*    */   }
/*    */   
/*    */   public boolean onGround() {
/* 11 */     return this.onGround;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ServerboundMoveVehiclePacket> STREAM_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, ServerboundMoveVehiclePacket::position, ByteBufCodecs.FLOAT, ServerboundMoveVehiclePacket::yRot, ByteBufCodecs.FLOAT, ServerboundMoveVehiclePacket::xRot, ByteBufCodecs.BOOL, ServerboundMoveVehiclePacket::onGround, ServerboundMoveVehiclePacket::new);
/*    */   
/*    */   public static ServerboundMoveVehiclePacket fromEntity(Entity entity) {
/* 21 */     if (entity.isInterpolating())
/* 22 */       return new ServerboundMoveVehiclePacket(entity.getInterpolation().position(), entity.getInterpolation().yRot(), entity.getInterpolation().xRot(), entity.onGround()); 
/* 24 */     return new ServerboundMoveVehiclePacket(entity.position(), entity.getYRot(), entity.getXRot(), entity.onGround());
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundMoveVehiclePacket> type() {
/* 30 */     return GamePacketTypes.SERVERBOUND_MOVE_VEHICLE;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 35 */     listener.handleMoveVehicle(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundMoveVehiclePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */