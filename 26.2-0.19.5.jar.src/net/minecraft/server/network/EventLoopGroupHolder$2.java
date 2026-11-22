/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.IoHandlerFactory;
/*    */ import io.netty.channel.ServerChannel;
/*    */ import io.netty.channel.epoll.EpollIoHandler;
/*    */ 
/*    */ class null extends EventLoopGroupHolder {
/*    */   null(String type, Class<? extends Channel> channelCls, Class<? extends ServerChannel> serverChannelCls) {
/* 36 */     super(type, channelCls, serverChannelCls);
/*    */   }
/*    */   
/*    */   protected IoHandlerFactory ioHandlerFactory() {
/* 39 */     return EpollIoHandler.newFactory();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\EventLoopGroupHolder$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */