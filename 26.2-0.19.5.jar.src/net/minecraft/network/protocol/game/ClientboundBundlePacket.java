/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.BundlePacket;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundBundlePacket extends BundlePacket<ClientGamePacketListener> {
/*    */   public ClientboundBundlePacket(Iterable<Packet<? super ClientGamePacketListener>> packets) {
/*  9 */     super(packets);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundBundlePacket> type() {
/* 14 */     return GamePacketTypes.CLIENTBOUND_BUNDLE;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 19 */     listener.handleBundlePacket(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundBundlePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */