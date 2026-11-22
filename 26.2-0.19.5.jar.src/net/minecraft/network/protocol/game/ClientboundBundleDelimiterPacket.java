/*   */ package net.minecraft.network.protocol.game;
/*   */ 
/*   */ import net.minecraft.network.protocol.BundleDelimiterPacket;
/*   */ import net.minecraft.network.protocol.PacketType;
/*   */ 
/*   */ public class ClientboundBundleDelimiterPacket extends BundleDelimiterPacket<ClientGamePacketListener> {
/*   */   public PacketType<ClientboundBundleDelimiterPacket> type() {
/* 9 */     return GamePacketTypes.CLIENTBOUND_BUNDLE_DELIMITER;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundBundleDelimiterPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */