/*   */ package net.minecraft.network;
/*   */ 
/*   */ import net.minecraft.network.protocol.PacketFlow;
/*   */ 
/*   */ public interface ServerboundPacketListener extends PacketListener {
/*   */   default PacketFlow flow() {
/* 8 */     return PacketFlow.SERVERBOUND;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\ServerboundPacketListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */