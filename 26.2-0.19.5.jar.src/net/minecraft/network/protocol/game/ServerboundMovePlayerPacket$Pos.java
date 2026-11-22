/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Pos extends ServerboundMovePlayerPacket {
/*  81 */   public static final StreamCodec<FriendlyByteBuf, Pos> STREAM_CODEC = Packet.codec(Pos::write, Pos::read);
/*     */   
/*     */   public Pos(Vec3 pos, boolean onGround, boolean horizontalCollision) {
/*  84 */     super(pos.x, pos.y, pos.z, 0.0F, 0.0F, onGround, horizontalCollision, true, false);
/*     */   }
/*     */   
/*     */   public Pos(double x, double y, double z, boolean onGround, boolean horizontalCollision) {
/*  88 */     super(x, y, z, 0.0F, 0.0F, onGround, horizontalCollision, true, false);
/*     */   }
/*     */   
/*     */   private static Pos read(FriendlyByteBuf input) {
/*  92 */     double x = input.readDouble();
/*  93 */     double y = input.readDouble();
/*  94 */     double z = input.readDouble();
/*  95 */     short flags = input.readUnsignedByte();
/*  96 */     boolean onGround = ServerboundMovePlayerPacket.unpackOnGround(flags);
/*  97 */     boolean horizontalCollision = ServerboundMovePlayerPacket.unpackHorizontalCollision(flags);
/*  98 */     return new Pos(x, y, z, onGround, horizontalCollision);
/*     */   }
/*     */   
/*     */   private void write(FriendlyByteBuf output) {
/* 102 */     output.writeDouble(this.x);
/* 103 */     output.writeDouble(this.y);
/* 104 */     output.writeDouble(this.z);
/* 105 */     output.writeByte(ServerboundMovePlayerPacket.packFlags(this.onGround, this.horizontalCollision));
/*     */   }
/*     */   
/*     */   public PacketType<Pos> type() {
/* 110 */     return GamePacketTypes.SERVERBOUND_MOVE_PLAYER_POS;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundMovePlayerPacket$Pos.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */