/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class PosRot extends ClientboundMoveEntityPacket {
/* 27 */   public static final StreamCodec<FriendlyByteBuf, PosRot> STREAM_CODEC = Packet.codec(PosRot::write, PosRot::read);
/*    */   
/*    */   public PosRot(int id, short xa, short ya, short za, byte yRot, byte xRot, boolean onGround) {
/* 30 */     super(id, xa, ya, za, yRot, xRot, onGround, true, true);
/*    */   }
/*    */   
/*    */   private static PosRot read(FriendlyByteBuf input) {
/* 34 */     int entityId = input.readVarInt();
/* 35 */     short xa = input.readShort();
/* 36 */     short ya = input.readShort();
/* 37 */     short za = input.readShort();
/* 38 */     byte yRot = input.readByte();
/* 39 */     byte xRot = input.readByte();
/* 40 */     boolean onGround = input.readBoolean();
/* 42 */     return new PosRot(entityId, xa, ya, za, yRot, xRot, onGround);
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 46 */     output.writeVarInt(this.entityId);
/* 47 */     output.writeShort(this.xa);
/* 48 */     output.writeShort(this.ya);
/* 49 */     output.writeShort(this.za);
/* 50 */     output.writeByte(this.yRot);
/* 51 */     output.writeByte(this.xRot);
/* 52 */     output.writeBoolean(this.onGround);
/*    */   }
/*    */   
/*    */   public PacketType<PosRot> type() {
/* 57 */     return GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_POS_ROT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundMoveEntityPacket$PosRot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */