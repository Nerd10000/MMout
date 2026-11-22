/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.net.HostAndPort;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.bootstrap.ServerBootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.MultiThreadIoEventLoopGroup;
/*     */ import io.netty.channel.nio.NioIoHandler;
/*     */ import io.netty.channel.socket.nio.NioServerSocketChannel;
/*     */ import io.netty.handler.codec.http.HttpObjectAggregator;
/*     */ import io.netty.handler.codec.http.HttpServerCodec;
/*     */ import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
/*     */ import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
/*     */ import io.netty.handler.logging.LogLevel;
/*     */ import io.netty.handler.logging.LoggingHandler;
/*     */ import io.netty.handler.ssl.SslContext;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.security.AuthenticationHandler;
/*     */ import net.minecraft.server.jsonrpc.websocket.JsonToWebSocketEncoder;
/*     */ import net.minecraft.server.jsonrpc.websocket.WebSocketToJsonCodec;
/*     */ import net.minecraft.server.notifications.NotificationManager;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ManagementServer {
/*  41 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final HostAndPort hostAndPort;
/*     */   
/*     */   private final AuthenticationHandler authenticationHandler;
/*     */   
/*     */   private Channel serverChannel;
/*     */   
/*     */   private final EventLoopGroup eventLoopGroup;
/*     */   
/*     */   private ScheduledFuture<?> heartbeat;
/*     */   
/*  50 */   private final Set<Connection> connections = Sets.newIdentityHashSet();
/*     */   
/*     */   public ManagementServer(HostAndPort hostAndPort, AuthenticationHandler authenticationHandler) {
/*  53 */     this.hostAndPort = hostAndPort;
/*  54 */     this.authenticationHandler = authenticationHandler;
/*  55 */     this
/*     */       
/*  59 */       .eventLoopGroup = (EventLoopGroup)new MultiThreadIoEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Management server IO #%d").setDaemon(true).build(), NioIoHandler.newFactory());
/*     */   }
/*     */   
/*     */   public ManagementServer(HostAndPort hostAndPort, AuthenticationHandler authenticationHandler, EventLoopGroup eventLoopGroup) {
/*  64 */     this.hostAndPort = hostAndPort;
/*  65 */     this.authenticationHandler = authenticationHandler;
/*  66 */     this.eventLoopGroup = eventLoopGroup;
/*     */   }
/*     */   
/*     */   public boolean scheduleHeartbeat(NotificationManager notificationManager, long period) {
/*  70 */     if (this.heartbeat != null && 
/*  71 */       !this.heartbeat.cancel(true)) {
/*  72 */       LOGGER.warn("The existing heartbeat was not canceled and the new heartbeat of {} seconds has not been applied.", Long.valueOf(period));
/*  73 */       return false;
/*     */     } 
/*  76 */     if (period > 0L) {
/*  77 */       Objects.requireNonNull(notificationManager);
/*  77 */       this.heartbeat = (ScheduledFuture<?>)this.eventLoopGroup.scheduleAtFixedRate(notificationManager::statusHeartbeat, period, period, TimeUnit.SECONDS);
/*     */     } 
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   public void onConnected(Connection connection) {
/*  83 */     synchronized (this.connections) {
/*  84 */       this.connections.add(connection);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onDisconnected(Connection connection) {
/*  89 */     synchronized (this.connections) {
/*  90 */       this.connections.remove(connection);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void startWithoutTls(MinecraftApi minecraftApi) {
/*  95 */     start(minecraftApi, null);
/*     */   }
/*     */   
/*     */   public void startWithTls(MinecraftApi minecraftApi, SslContext sslContext) {
/*  99 */     start(minecraftApi, sslContext);
/*     */   }
/*     */   
/*     */   private void start(final MinecraftApi minecraftApi, final SslContext sslContext) {
/* 103 */     final JsonRpcLogger jsonrpcLogger = new JsonRpcLogger();
/* 134 */     ChannelFuture channel = ((ServerBootstrap)((ServerBootstrap)((ServerBootstrap)(new ServerBootstrap()).handler((ChannelHandler)new LoggingHandler(LogLevel.DEBUG))).channel(NioServerSocketChannel.class)).childHandler((ChannelHandler)new ChannelInitializer<Channel>() {
/*     */           protected void initChannel(Channel channel) {
/*     */             try {
/*     */               channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/*     */             } catch (ChannelException channelException) {}
/*     */             ChannelPipeline pipeline = channel.pipeline();
/*     */             if (sslContext != null)
/*     */               pipeline.addLast(new ChannelHandler[] { (ChannelHandler)this.val$sslContext.newHandler(channel.alloc()) }); 
/*     */             pipeline.addLast(new ChannelHandler[] { (ChannelHandler)new HttpServerCodec() }).addLast(new ChannelHandler[] { (ChannelHandler)new HttpObjectAggregator(65536) }).addLast(new ChannelHandler[] { (ChannelHandler)this.this$0.authenticationHandler }).addLast(new ChannelHandler[] { (ChannelHandler)new WebSocketServerProtocolHandler("/") }).addLast(new ChannelHandler[] { (ChannelHandler)new WebSocketFrameAggregator(65536) }).addLast(new ChannelHandler[] { (ChannelHandler)new WebSocketToJsonCodec() }).addLast(new ChannelHandler[] { (ChannelHandler)new JsonToWebSocketEncoder() }).addLast(new ChannelHandler[] { (ChannelHandler)new Connection(channel, this.this$0, this.val$minecraftApi, this.val$jsonrpcLogger) });
/*     */           }
/* 134 */         }).group(this.eventLoopGroup).localAddress(this.hostAndPort.getHost(), this.hostAndPort.getPort())).bind();
/* 135 */     this.serverChannel = channel.channel();
/* 136 */     channel.syncUninterruptibly();
/* 138 */     LOGGER.info("Json-RPC Management connection listening on {}:{}", this.hostAndPort.getHost(), Integer.valueOf(getPort()));
/*     */   }
/*     */   
/*     */   public void stop(boolean closeNioEventLoopGroup) throws InterruptedException {
/* 142 */     if (this.serverChannel != null) {
/* 143 */       this.serverChannel.close().sync();
/* 144 */       this.serverChannel = null;
/*     */     } 
/* 146 */     this.connections.clear();
/* 148 */     if (closeNioEventLoopGroup)
/* 149 */       this.eventLoopGroup.shutdownGracefully().sync(); 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 154 */     forEachConnection(Connection::tick);
/*     */   }
/*     */   
/*     */   public int getPort() {
/* 158 */     return (this.serverChannel != null) ? ((InetSocketAddress)this.serverChannel.localAddress()).getPort() : this.hostAndPort.getPort();
/*     */   }
/*     */   
/*     */   void forEachConnection(Consumer<Connection> action) {
/* 163 */     synchronized (this.connections) {
/* 164 */       this.connections.forEach(action);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\ManagementServer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */