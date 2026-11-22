/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.IoHandlerFactory;
/*     */ import io.netty.channel.MultiThreadIoEventLoopGroup;
/*     */ import io.netty.channel.ServerChannel;
/*     */ import io.netty.channel.epoll.Epoll;
/*     */ import io.netty.channel.epoll.EpollIoHandler;
/*     */ import io.netty.channel.epoll.EpollServerSocketChannel;
/*     */ import io.netty.channel.epoll.EpollSocketChannel;
/*     */ import io.netty.channel.kqueue.KQueue;
/*     */ import io.netty.channel.kqueue.KQueueIoHandler;
/*     */ import io.netty.channel.kqueue.KQueueServerSocketChannel;
/*     */ import io.netty.channel.kqueue.KQueueSocketChannel;
/*     */ import io.netty.channel.local.LocalChannel;
/*     */ import io.netty.channel.local.LocalIoHandler;
/*     */ import io.netty.channel.local.LocalServerChannel;
/*     */ import io.netty.channel.nio.NioIoHandler;
/*     */ import io.netty.channel.socket.nio.NioServerSocketChannel;
/*     */ import io.netty.channel.socket.nio.NioSocketChannel;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ 
/*     */ public abstract class EventLoopGroupHolder {
/*  29 */   private static final EventLoopGroupHolder NIO = new EventLoopGroupHolder("NIO", NioSocketChannel.class, NioServerSocketChannel.class) {
/*     */       protected IoHandlerFactory ioHandlerFactory() {
/*  32 */         return NioIoHandler.newFactory();
/*     */       }
/*     */     };
/*     */   
/*  36 */   private static final EventLoopGroupHolder EPOLL = new EventLoopGroupHolder("Epoll", EpollSocketChannel.class, EpollServerSocketChannel.class) {
/*     */       protected IoHandlerFactory ioHandlerFactory() {
/*  39 */         return EpollIoHandler.newFactory();
/*     */       }
/*     */     };
/*     */   
/*  43 */   private static final EventLoopGroupHolder KQUEUE = new EventLoopGroupHolder("Kqueue", KQueueSocketChannel.class, KQueueServerSocketChannel.class) {
/*     */       protected IoHandlerFactory ioHandlerFactory() {
/*  46 */         return KQueueIoHandler.newFactory();
/*     */       }
/*     */     };
/*     */   
/*  50 */   private static final EventLoopGroupHolder LOCAL = new EventLoopGroupHolder("Local", LocalChannel.class, LocalServerChannel.class) {
/*     */       protected IoHandlerFactory ioHandlerFactory() {
/*  53 */         return LocalIoHandler.newFactory();
/*     */       }
/*     */     };
/*     */   
/*     */   private final String type;
/*     */   
/*     */   private final Class<? extends Channel> channelCls;
/*     */   
/*     */   private final Class<? extends ServerChannel> serverChannelCls;
/*     */   
/*     */   private volatile EventLoopGroup group;
/*     */   
/*     */   public static EventLoopGroupHolder remote(boolean allowNativeTransport) {
/*  58 */     if (allowNativeTransport) {
/*  59 */       if (KQueue.isAvailable())
/*  60 */         return KQUEUE; 
/*  63 */       if (Epoll.isAvailable())
/*  64 */         return EPOLL; 
/*     */     } 
/*  67 */     return NIO;
/*     */   }
/*     */   
/*     */   public static EventLoopGroupHolder local() {
/*  71 */     return LOCAL;
/*     */   }
/*     */   
/*     */   private EventLoopGroupHolder(String type, Class<? extends Channel> channelCls, Class<? extends ServerChannel> serverChannelCls) {
/*  81 */     this.type = type;
/*  82 */     this.channelCls = channelCls;
/*  83 */     this.serverChannelCls = serverChannelCls;
/*     */   }
/*     */   
/*     */   private ThreadFactory createThreadFactory() {
/*  87 */     return (new ThreadFactoryBuilder()).setNameFormat("Netty " + this.type + " IO #%d").setDaemon(true).build();
/*     */   }
/*     */   
/*     */   protected abstract IoHandlerFactory ioHandlerFactory();
/*     */   
/*     */   private EventLoopGroup createEventLoopGroup() {
/*  93 */     return (EventLoopGroup)new MultiThreadIoEventLoopGroup(createThreadFactory(), ioHandlerFactory());
/*     */   }
/*     */   
/*     */   public EventLoopGroup eventLoopGroup() {
/*  97 */     EventLoopGroup result = this.group;
/*  98 */     if (result == null)
/*  99 */       synchronized (this) {
/* 100 */         result = this.group;
/* 101 */         if (result == null) {
/* 102 */           result = createEventLoopGroup();
/* 103 */           this.group = result;
/*     */         } 
/*     */       }  
/* 108 */     return result;
/*     */   }
/*     */   
/*     */   public Class<? extends Channel> channelCls() {
/* 112 */     return this.channelCls;
/*     */   }
/*     */   
/*     */   public Class<? extends ServerChannel> serverChannelCls() {
/* 116 */     return this.serverChannelCls;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\EventLoopGroupHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */