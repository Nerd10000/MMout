/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ 
/*    */ class null implements ProtocolInfo.Details {
/*    */   public ConnectionProtocol id() {
/* 78 */     return protocol;
/*    */   }
/*    */   
/*    */   public PacketFlow flow() {
/* 83 */     return flow;
/*    */   }
/*    */   
/*    */   public void listPackets(ProtocolInfo.Details.PacketVisitor output) {
/* 88 */     for (int i = 0; i < codecs.size(); i++) {
/* 89 */       ProtocolInfoBuilder.CodecEntry<?, ?, ?, ?> entry = codecs.get(i);
/* 90 */       output.accept(entry.type, i);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\ProtocolInfoBuilder$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */