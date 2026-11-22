/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.bootstrap.ServerBootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.local.LocalAddress;
/*     */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*     */ import io.netty.util.HashedWheelTimer;
/*     */ import io.netty.util.Timeout;
/*     */ import io.netty.util.Timer;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.PacketSendListener;
/*     */ import net.minecraft.network.RateKickingConnection;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.ServerInfo;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerConnectionListener {
/*  46 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final MinecraftServer server;
/*     */   
/*     */   public volatile boolean running;
/*     */   
/*     */   private volatile UUID sessionId;
/*     */   
/*  51 */   private final List<ChannelFuture> channels = Collections.synchronizedList(Lists.newArrayList());
/*     */   
/*  52 */   private final List<Connection> connections = Collections.synchronizedList(Lists.newArrayList());
/*     */   
/*     */   public ServerConnectionListener(MinecraftServer server) {
/*  55 */     this.server = server;
/*  56 */     this.running = true;
/*     */   }
/*     */   
/*     */   public void startTcpServerListener(InetAddress address, int port) throws IOException {
/*  60 */     synchronized (this.channels) {
/*  61 */       EventLoopGroupHolder eventLoopGroupHolder = EventLoopGroupHolder.remote(this.server.useNativeTransport());
/*  63 */       this.channels.add(((ServerBootstrap)((ServerBootstrap)(new ServerBootstrap())
/*  64 */           .channel(eventLoopGroupHolder.serverChannelCls()))
/*  65 */           .childHandler((ChannelHandler)new ChannelInitializer<Channel>() {
/*     */               protected void initChannel(Channel channel) {
/*     */                 try {
/*  69 */                   channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/*  70 */                 } catch (ChannelException channelException) {}
/*  74 */                 ChannelPipeline pipeline = channel.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30));
/*  76 */                 if (ServerConnectionListener.this.server.repliesToStatus())
/*  77 */                   pipeline.addLast("legacy_query", (ChannelHandler)new LegacyQueryHandler((ServerInfo)ServerConnectionListener.this.getServer())); 
/*  80 */                 Connection.configureSerialization(pipeline, PacketFlow.SERVERBOUND, false, null);
/*  82 */                 int rateLimitPacketsPerSecond = ServerConnectionListener.this.server.getRateLimitPacketsPerSecond();
/*  83 */                 Connection connection = (rateLimitPacketsPerSecond > 0) ? (Connection)new RateKickingConnection(rateLimitPacketsPerSecond) : new Connection(PacketFlow.SERVERBOUND);
/*  84 */                 ServerConnectionListener.this.connections.add(connection);
/*  85 */                 connection.configurePacketHandler(pipeline);
/*  86 */                 connection.setListenerForServerboundHandshake((PacketListener)new ServerHandshakePacketListenerImpl(ServerConnectionListener.this.server, connection));
/*     */               }
/*  89 */             }).group(eventLoopGroupHolder.eventLoopGroup())
/*  90 */           .localAddress(address, port))
/*  91 */           .bind()
/*  92 */           .syncUninterruptibly());
/*     */     } 
/*     */   }
/*     */   
/*     */   public SocketAddress startMemoryChannel() {
/*     */     ChannelFuture newChannel;
/*  99 */     synchronized (this.channels) {
/* 122 */       newChannel = ((ServerBootstrap)((ServerBootstrap)(new ServerBootstrap()).channel(EventLoopGroupHolder.local().serverChannelCls())).childHandler((ChannelHandler)new ChannelInitializer<Channel>() {
/*     */             protected void initChannel(Channel channel) {
/*     */               Connection connection = new Connection(PacketFlow.SERVERBOUND);
/*     */               connection.setListenerForServerboundHandshake((PacketListener)new MemoryServerHandshakePacketListenerImpl(ServerConnectionListener.this.server, connection));
/*     */               ServerConnectionListener.this.connections.add(connection);
/*     */               ChannelPipeline pipeline = channel.pipeline();
/*     */               Connection.configureInMemoryPipeline(pipeline, PacketFlow.SERVERBOUND);
/*     */               if (SharedConstants.DEBUG_FAKE_LATENCY_MS > 0)
/*     */                 pipeline.addLast("latency", (ChannelHandler)new ServerConnectionListener.LatencySimulator(SharedConstants.DEBUG_FAKE_LATENCY_MS, SharedConstants.DEBUG_FAKE_JITTER_MS)); 
/*     */               connection.configurePacketHandler(pipeline);
/*     */             }
/* 122 */           }).group(EventLoopGroupHolder.local().eventLoopGroup()).localAddress((SocketAddress)LocalAddress.ANY)).bind().syncUninterruptibly();
/* 124 */       this.channels.add(newChannel);
/*     */     } 
/* 127 */     return newChannel.channel().localAddress();
/*     */   }
/*     */   
/*     */   public void acceptChannel(Channel channel, final UUID profileId) {
/* 131 */     channel.pipeline().addLast(new ChannelHandler[] { (ChannelHandler)new ChannelInitializer<Channel>() {
/*     */             protected void initChannel(Channel ch) {
/* 134 */               int rateLimitPacketsPerSecond = ServerConnectionListener.this.server.getRateLimitPacketsPerSecond();
/* 137 */               Connection connection = (rateLimitPacketsPerSecond > 0) ? (Connection)new RateKickingConnection(rateLimitPacketsPerSecond) : new Connection(PacketFlow.SERVERBOUND);
/* 140 */               ChannelPipeline pipeline = ch.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30));
/* 142 */               Connection.configureSerialization(pipeline, PacketFlow.SERVERBOUND, false, null);
/* 143 */               connection.configurePacketHandler(pipeline);
/* 144 */               connection.setListenerForServerboundHandshake((PacketListener)new ServerHandshakePacketListenerImpl(ServerConnectionListener.this.server, connection));
/* 145 */               connection.setIntendedProfileId(profileId);
/* 146 */               ServerConnectionListener.this.connections.add(connection);
/*     */             }
/*     */           } });
/* 150 */     EventLoopGroupHolder.local().eventLoopGroup().register(channel).syncUninterruptibly();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 154 */     this.running = false;
/* 156 */     for (ChannelFuture channel : this.channels) {
/*     */       try {
/* 158 */         channel.channel().close().sync();
/* 159 */       } catch (InterruptedException ignored) {
/* 160 */         LOGGER.error("Interrupted whilst closing channel");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stopTcpServerListener() {
/* 166 */     synchronized (this.channels) {
/* 167 */       Iterator<ChannelFuture> iterator = this.channels.iterator();
/* 168 */       while (iterator.hasNext()) {
/* 169 */         ChannelFuture future = iterator.next();
/* 170 */         if (future.channel() instanceof io.netty.channel.local.LocalServerChannel)
/*     */           continue; 
/*     */         try {
/* 174 */           future.channel().close().sync();
/* 175 */         } catch (InterruptedException ignored) {
/* 176 */           LOGGER.error("Interrupted whilst closing TCP listener");
/*     */         } 
/* 178 */         iterator.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 184 */     synchronized (this.connections) {
/* 185 */       Iterator<Connection> iterator = this.connections.iterator();
/* 187 */       while (iterator.hasNext()) {
/* 188 */         Connection connection = iterator.next();
/* 190 */         if (connection.isConnecting())
/*     */           continue; 
/* 193 */         if (connection.isConnected()) {
/*     */           try {
/* 195 */             connection.tick();
/* 196 */           } catch (Exception e) {
/* 197 */             if (connection.isMemoryConnection())
/* 198 */               throw new ReportedException(CrashReport.forThrowable(e, "Ticking memory connection")); 
/* 200 */             LOGGER.warn("Failed to handle packet for {}", connection.getLoggableAddress(this.server.logIPs()), e);
/* 201 */             MutableComponent mutableComponent = Component.literal("Internal server error");
/* 202 */             connection.send((Packet)new ClientboundDisconnectPacket((Component)mutableComponent), PacketSendListener.thenRun(() -> connection.disconnect(component)));
/* 203 */             connection.setReadOnly();
/*     */           } 
/*     */           continue;
/*     */         } 
/* 207 */         iterator.remove();
/* 208 */         connection.handleDisconnection();
/*     */       } 
/* 213 */       if (this.connections.isEmpty())
/* 214 */         synchronized (this) {
/* 215 */           this.sessionId = null;
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   public MinecraftServer getServer() {
/* 222 */     return this.server;
/*     */   }
/*     */   
/*     */   private static class LatencySimulator extends ChannelInboundHandlerAdapter {
/* 226 */     private static final Timer TIMER = (Timer)new HashedWheelTimer((new ThreadFactoryBuilder()).setNameFormat("Latency Simulator #%d").setDaemon(true).build());
/*     */     
/*     */     private final int delay;
/*     */     
/*     */     private final int jitter;
/*     */     
/* 230 */     private final List<DelayedMessage> queuedMessages = Lists.newArrayList();
/*     */     
/*     */     public LatencySimulator(int delay, int jitter) {
/* 233 */       this.delay = delay;
/* 234 */       this.jitter = jitter;
/*     */     }
/*     */     
/*     */     public void channelRead(ChannelHandlerContext ctx, Object msg) {
/* 239 */       delayDownstream(ctx, msg);
/*     */     }
/*     */     
/*     */     private void delayDownstream(ChannelHandlerContext ctx, Object msg) {
/* 243 */       int sendDelay = this.delay + (int)(Math.random() * this.jitter);
/* 244 */       this.queuedMessages.add(new DelayedMessage(ctx, msg));
/* 245 */       TIMER.newTimeout(this::onTimeout, sendDelay, TimeUnit.MILLISECONDS);
/*     */     }
/*     */     
/*     */     private void onTimeout(Timeout timeout) {
/* 249 */       DelayedMessage next = this.queuedMessages.remove(0);
/* 250 */       next.ctx.fireChannelRead(next.msg);
/*     */     }
/*     */     
/*     */     private static final class DelayedMessage extends Record {
/*     */       private final ChannelHandlerContext ctx;
/*     */       
/*     */       private final Object msg;
/*     */       
/*     */       private DelayedMessage(ChannelHandlerContext ctx, Object msg) {
/* 253 */         this.ctx = ctx;
/* 253 */         this.msg = msg;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #253	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #253	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #253	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public ChannelHandlerContext ctx() {
/* 253 */         return this.ctx;
/*     */       }
/*     */       
/*     */       public Object msg() {
/* 253 */         return this.msg;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class DelayedMessage extends Record {
/*     */     private final ChannelHandlerContext ctx;
/*     */     
/*     */     private final Object msg;
/*     */     
/*     */     private DelayedMessage(ChannelHandlerContext ctx, Object msg) {
/* 253 */       this.ctx = ctx;
/* 253 */       this.msg = msg;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #253	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #253	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #253	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ChannelHandlerContext ctx() {
/* 253 */       return this.ctx;
/*     */     }
/*     */     
/*     */     public Object msg() {
/* 253 */       return this.msg;
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Connection> getConnections() {
/* 258 */     return this.connections;
/*     */   }
/*     */   
/*     */   public UUID getSessionId() {
/* 262 */     UUID uuid = this.sessionId;
/* 263 */     if (uuid != null)
/* 264 */       return uuid; 
/* 266 */     synchronized (this) {
/* 267 */       uuid = this.sessionId;
/* 268 */       if (uuid == null) {
/* 269 */         uuid = UUID.randomUUID();
/* 270 */         this.sessionId = uuid;
/*     */       } 
/* 272 */       return uuid;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\ServerConnectionListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */