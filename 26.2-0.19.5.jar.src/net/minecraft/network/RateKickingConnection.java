/*    */ package net.minecraft.network;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class RateKickingConnection extends Connection {
/* 10 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 11 */   private static final Component EXCEED_REASON = (Component)Component.translatable("disconnect.exceeded_packet_rate");
/*    */   
/*    */   private final int rateLimitPacketsPerSecond;
/*    */   
/*    */   public RateKickingConnection(int rateLimitPacketsPerSecond) {
/* 16 */     super(PacketFlow.SERVERBOUND);
/* 17 */     this.rateLimitPacketsPerSecond = rateLimitPacketsPerSecond;
/*    */   }
/*    */   
/*    */   protected void tickSecond() {
/* 22 */     super.tickSecond();
/* 24 */     float averageReceivedPackets = getAverageReceivedPackets();
/* 25 */     if (averageReceivedPackets > this.rateLimitPacketsPerSecond) {
/* 26 */       LOGGER.warn("Player exceeded rate-limit (sent {} packets per second)", Float.valueOf(averageReceivedPackets));
/* 28 */       send((Packet<?>)new ClientboundDisconnectPacket(EXCEED_REASON), PacketSendListener.thenRun(() -> disconnect(EXCEED_REASON)));
/* 29 */       setReadOnly();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\RateKickingConnection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */