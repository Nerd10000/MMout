/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundRenameItemPacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundRenameItemPacket> STREAM_CODEC = Packet.codec(ServerboundRenameItemPacket::write, ServerboundRenameItemPacket::new);
/*    */   
/*    */   private final String name;
/*    */   
/*    */   public ServerboundRenameItemPacket(String name) {
/* 14 */     this.name = name;
/*    */   }
/*    */   
/*    */   private ServerboundRenameItemPacket(FriendlyByteBuf input) {
/* 18 */     this.name = input.readUtf();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeUtf(this.name);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundRenameItemPacket> type() {
/* 27 */     return GamePacketTypes.SERVERBOUND_RENAME_ITEM;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 32 */     listener.handleRenameItem(this);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 36 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundRenameItemPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */