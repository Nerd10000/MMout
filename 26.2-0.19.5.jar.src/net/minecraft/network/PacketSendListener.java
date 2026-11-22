/*    */ package net.minecraft.network;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import io.netty.channel.ChannelFuture;
/*    */ import io.netty.channel.ChannelFutureListener;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class PacketSendListener {
/* 15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static ChannelFutureListener thenRun(Runnable runnable) {
/* 18 */     return future -> {
/*    */         runnable.run();
/*    */         if (!future.isSuccess())
/*    */           future.channel().pipeline().fireExceptionCaught(future.cause()); 
/*    */       };
/*    */   }
/*    */   
/*    */   public static ChannelFutureListener exceptionallySend(Supplier<Packet<?>> handler) {
/* 27 */     return future -> {
/*    */         if (!future.isSuccess()) {
/*    */           Packet<?> newPacket = handler.get();
/*    */           if (newPacket != null) {
/*    */             LOGGER.warn("Failed to deliver packet, sending fallback {}", newPacket.type(), future.cause());
/*    */             future.channel().writeAndFlush(newPacket, future.channel().voidPromise());
/*    */           } else {
/*    */             future.channel().pipeline().fireExceptionCaught(future.cause());
/*    */           } 
/*    */         } 
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\PacketSendListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */