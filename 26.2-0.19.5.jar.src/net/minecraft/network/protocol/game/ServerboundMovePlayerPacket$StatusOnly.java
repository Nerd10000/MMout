/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ 
/*     */ public class StatusOnly extends ServerboundMovePlayerPacket {
/* 143 */   public static final StreamCodec<FriendlyByteBuf, StatusOnly> STREAM_CODEC = Packet.codec(StatusOnly::write, StatusOnly::read);
/*     */   
/*     */   public StatusOnly(boolean onGround, boolean horizontalCollision) {
/* 146 */     super(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, onGround, horizontalCollision, false, false);
/*     */   }
/*     */   
/*     */   private static StatusOnly read(FriendlyByteBuf input) {
/* 150 */     short flags = input.readUnsignedByte();
/* 151 */     boolean onGround = ServerboundMovePlayerPacket.unpackOnGround(flags);
/* 152 */     boolean horizontalCollision = ServerboundMovePlayerPacket.unpackHorizontalCollision(flags);
/* 153 */     return new StatusOnly(onGround, horizontalCollision);
/*     */   }
/*     */   
/*     */   private void write(FriendlyByteBuf output) {
/* 157 */     output.writeByte(ServerboundMovePlayerPacket.packFlags(this.onGround, this.horizontalCollision));
/*     */   }
/*     */   
/*     */   public PacketType<StatusOnly> type() {
/* 162 */     return GamePacketTypes.SERVERBOUND_MOVE_PLAYER_STATUS_ONLY;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundMovePlayerPacket$StatusOnly.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */