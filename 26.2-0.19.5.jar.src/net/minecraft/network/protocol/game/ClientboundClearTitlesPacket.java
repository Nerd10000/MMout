/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundClearTitlesPacket implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundClearTitlesPacket> STREAM_CODEC = Packet.codec(ClientboundClearTitlesPacket::write, ClientboundClearTitlesPacket::new);
/*    */   
/*    */   private final boolean resetTimes;
/*    */   
/*    */   public ClientboundClearTitlesPacket(boolean resetTimes) {
/* 14 */     this.resetTimes = resetTimes;
/*    */   }
/*    */   
/*    */   private ClientboundClearTitlesPacket(FriendlyByteBuf input) {
/* 18 */     this.resetTimes = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeBoolean(this.resetTimes);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundClearTitlesPacket> type() {
/* 27 */     return GamePacketTypes.CLIENTBOUND_CLEAR_TITLES;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 32 */     listener.handleTitlesClear(this);
/*    */   }
/*    */   
/*    */   public boolean shouldResetTimes() {
/* 36 */     return this.resetTimes;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundClearTitlesPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */