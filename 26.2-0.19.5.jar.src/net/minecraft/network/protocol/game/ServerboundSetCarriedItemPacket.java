/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundSetCarriedItemPacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSetCarriedItemPacket> STREAM_CODEC = Packet.codec(ServerboundSetCarriedItemPacket::write, ServerboundSetCarriedItemPacket::new);
/*    */   
/*    */   private final int slot;
/*    */   
/*    */   public ServerboundSetCarriedItemPacket(int slot) {
/* 14 */     this.slot = slot;
/*    */   }
/*    */   
/*    */   private ServerboundSetCarriedItemPacket(FriendlyByteBuf input) {
/* 18 */     this.slot = input.readShort();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeShort(this.slot);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundSetCarriedItemPacket> type() {
/* 27 */     return GamePacketTypes.SERVERBOUND_SET_CARRIED_ITEM;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 32 */     listener.handleSetCarriedItem(this);
/*    */   }
/*    */   
/*    */   public int getSlot() {
/* 36 */     return this.slot;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundSetCarriedItemPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */