/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class PosRot extends ServerboundMovePlayerPacket {
/* 43 */   public static final StreamCodec<FriendlyByteBuf, PosRot> STREAM_CODEC = Packet.codec(PosRot::write, PosRot::read);
/*    */   
/*    */   public PosRot(Vec3 pos, float yRot, float xRot, boolean onGround, boolean horizontalCollision) {
/* 46 */     super(pos.x, pos.y, pos.z, yRot, xRot, onGround, horizontalCollision, true, true);
/*    */   }
/*    */   
/*    */   public PosRot(double x, double y, double z, float yRot, float xRot, boolean onGround, boolean horizontalCollision) {
/* 50 */     super(x, y, z, yRot, xRot, onGround, horizontalCollision, true, true);
/*    */   }
/*    */   
/*    */   private static PosRot read(FriendlyByteBuf input) {
/* 54 */     double x = input.readDouble();
/* 55 */     double y = input.readDouble();
/* 56 */     double z = input.readDouble();
/* 57 */     float yRot = input.readFloat();
/* 58 */     float xRot = input.readFloat();
/* 59 */     short flags = input.readUnsignedByte();
/* 60 */     boolean onGround = ServerboundMovePlayerPacket.unpackOnGround(flags);
/* 61 */     boolean horizontalCollision = ServerboundMovePlayerPacket.unpackHorizontalCollision(flags);
/* 62 */     return new PosRot(x, y, z, yRot, xRot, onGround, horizontalCollision);
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 66 */     output.writeDouble(this.x);
/* 67 */     output.writeDouble(this.y);
/* 68 */     output.writeDouble(this.z);
/* 69 */     output.writeFloat(this.yRot);
/* 70 */     output.writeFloat(this.xRot);
/* 71 */     output.writeByte(ServerboundMovePlayerPacket.packFlags(this.onGround, this.horizontalCollision));
/*    */   }
/*    */   
/*    */   public PacketType<PosRot> type() {
/* 76 */     return GamePacketTypes.SERVERBOUND_MOVE_PLAYER_POS_ROT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundMovePlayerPacket$PosRot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */