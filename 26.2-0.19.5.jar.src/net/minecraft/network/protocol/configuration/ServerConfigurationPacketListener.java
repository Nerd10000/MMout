/*   */ package net.minecraft.network.protocol.configuration;
/*   */ 
/*   */ import net.minecraft.network.ConnectionProtocol;
/*   */ import net.minecraft.network.protocol.common.ServerCommonPacketListener;
/*   */ 
/*   */ public interface ServerConfigurationPacketListener extends ServerCommonPacketListener {
/*   */   default ConnectionProtocol protocol() {
/* 9 */     return ConnectionProtocol.CONFIGURATION;
/*   */   }
/*   */   
/*   */   void handleConfigurationFinished(ServerboundFinishConfigurationPacket paramServerboundFinishConfigurationPacket);
/*   */   
/*   */   void handleSelectKnownPacks(ServerboundSelectKnownPacks paramServerboundSelectKnownPacks);
/*   */   
/*   */   void handleAcceptCodeOfConduct(ServerboundAcceptCodeOfConductPacket paramServerboundAcceptCodeOfConductPacket);
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\configuration\ServerConfigurationPacketListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */