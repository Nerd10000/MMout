/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundProjectilePowerPacket implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundProjectilePowerPacket> STREAM_CODEC = Packet.codec(ClientboundProjectilePowerPacket::write, ClientboundProjectilePowerPacket::new);
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final double accelerationPower;
/*    */   
/*    */   public ClientboundProjectilePowerPacket(int id, double accelerationPower) {
/* 15 */     this.id = id;
/* 16 */     this.accelerationPower = accelerationPower;
/*    */   }
/*    */   
/*    */   private ClientboundProjectilePowerPacket(FriendlyByteBuf input) {
/* 20 */     this.id = input.readVarInt();
/* 21 */     this.accelerationPower = input.readDouble();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 25 */     output.writeVarInt(this.id);
/* 26 */     output.writeDouble(this.accelerationPower);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundProjectilePowerPacket> type() {
/* 31 */     return GamePacketTypes.CLIENTBOUND_PROJECTILE_POWER;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 36 */     listener.handleProjectilePowerPacket(this);
/*    */   }
/*    */   
/*    */   public int getId() {
/* 40 */     return this.id;
/*    */   }
/*    */   
/*    */   public double getAccelerationPower() {
/* 44 */     return this.accelerationPower;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundProjectilePowerPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */