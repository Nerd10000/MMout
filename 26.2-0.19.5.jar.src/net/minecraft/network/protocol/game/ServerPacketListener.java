/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.network.ServerboundPacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public interface ServerPacketListener extends ServerboundPacketListener {
/* 10 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   default void onPacketError(Packet packet, Exception e) throws ReportedException {
/* 14 */     LOGGER.error("Failed to handle packet {}, suppressing error", packet, e);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerPacketListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */