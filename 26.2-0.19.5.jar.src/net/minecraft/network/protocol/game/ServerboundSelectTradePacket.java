/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundSelectTradePacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSelectTradePacket> STREAM_CODEC = Packet.codec(ServerboundSelectTradePacket::write, ServerboundSelectTradePacket::new);
/*    */   
/*    */   private final int item;
/*    */   
/*    */   public ServerboundSelectTradePacket(int item) {
/* 14 */     this.item = item;
/*    */   }
/*    */   
/*    */   private ServerboundSelectTradePacket(FriendlyByteBuf input) {
/* 18 */     this.item = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeVarInt(this.item);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundSelectTradePacket> type() {
/* 27 */     return GamePacketTypes.SERVERBOUND_SELECT_TRADE;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 32 */     listener.handleSelectTrade(this);
/*    */   }
/*    */   
/*    */   public int getItem() {
/* 36 */     return this.item;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundSelectTradePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */