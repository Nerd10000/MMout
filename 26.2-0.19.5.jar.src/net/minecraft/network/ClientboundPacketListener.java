/*   */ package net.minecraft.network;
/*   */ 
/*   */ import net.minecraft.network.protocol.PacketFlow;
/*   */ 
/*   */ public interface ClientboundPacketListener extends PacketListener {
/*   */   default PacketFlow flow() {
/* 8 */     return PacketFlow.CLIENTBOUND;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\ClientboundPacketListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */