/*     */ package net.minecraft.network;
/*     */ 
/*     */ import com.google.common.collect.Queues;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelOutboundHandler;
/*     */ import io.netty.channel.ChannelOutboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.SimpleChannelInboundHandler;
/*     */ import io.netty.handler.flow.FlowControlHandler;
/*     */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.function.Consumer;
/*     */ import javax.crypto.Cipher;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.protocol.BundlerInfo;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
/*     */ import net.minecraft.network.protocol.handshake.ClientIntent;
/*     */ import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
/*     */ import net.minecraft.network.protocol.handshake.HandshakeProtocols;
/*     */ import net.minecraft.network.protocol.handshake.ServerHandshakePacketListener;
/*     */ import net.minecraft.network.protocol.login.ClientLoginPacketListener;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
/*     */ import net.minecraft.network.protocol.login.LoginProtocols;
/*     */ import net.minecraft.network.protocol.status.ClientStatusPacketListener;
/*     */ import net.minecraft.network.protocol.status.StatusProtocols;
/*     */ import net.minecraft.server.RunningOnDifferentThreadException;
/*     */ import net.minecraft.server.network.EventLoopGroupHolder;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.debugchart.LocalSampleLogger;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.MarkerFactory;
/*     */ 
/*     */ public class Connection extends SimpleChannelInboundHandler<Packet<?>> {
/*     */   private static final float AVERAGE_PACKETS_SMOOTHING = 0.75F;
/*     */   
/*  62 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  64 */   public static final Marker ROOT_MARKER = MarkerFactory.getMarker("NETWORK");
/*     */   
/*     */   public static final Marker PACKET_MARKER;
/*     */   
/*     */   public static final Marker PACKET_RECEIVED_MARKER;
/*     */   
/*     */   public static final Marker PACKET_SENT_MARKER;
/*     */   
/*     */   static {
/*  66 */     PACKET_MARKER = (Marker)Util.make(MarkerFactory.getMarker("NETWORK_PACKETS"), m -> m.add(ROOT_MARKER));
/*  68 */     PACKET_RECEIVED_MARKER = (Marker)Util.make(MarkerFactory.getMarker("PACKET_RECEIVED"), m -> m.add(PACKET_MARKER));
/*  69 */     PACKET_SENT_MARKER = (Marker)Util.make(MarkerFactory.getMarker("PACKET_SENT"), m -> m.add(PACKET_MARKER));
/*     */   }
/*     */   
/*  71 */   private static final ProtocolInfo<ServerHandshakePacketListener> INITIAL_PROTOCOL = HandshakeProtocols.SERVERBOUND;
/*     */   
/*     */   private final PacketFlow receiving;
/*     */   
/*     */   private volatile boolean sendLoginDisconnect = true;
/*     */   
/*  76 */   private final Queue<Consumer<Connection>> pendingActions = Queues.newConcurrentLinkedQueue();
/*     */   
/*     */   private Channel channel;
/*     */   
/*     */   private SocketAddress address;
/*     */   
/*     */   private volatile PacketListener disconnectListener;
/*     */   
/*     */   private volatile PacketListener packetListener;
/*     */   
/*     */   private DisconnectionDetails disconnectionDetails;
/*     */   
/*     */   private boolean disconnectionHandled;
/*     */   
/*     */   private int receivedPackets;
/*     */   
/*     */   private int sentPackets;
/*     */   
/*     */   private float averageReceivedPackets;
/*     */   
/*     */   private float averageSentPackets;
/*     */   
/*     */   private int tickCount;
/*     */   
/*     */   private boolean handlingFault;
/*     */   
/*     */   private volatile DisconnectionDetails delayedDisconnect;
/*     */   
/*     */   private BandwidthDebugMonitor bandwidthDebugMonitor;
/*     */   
/*     */   private UUID intendedProfileId;
/*     */   
/*     */   public Connection(PacketFlow receiving) {
/*  94 */     this.receiving = receiving;
/*     */   }
/*     */   
/*     */   public void channelActive(ChannelHandlerContext ctx) throws Exception {
/*  99 */     super.channelActive(ctx);
/* 100 */     this.channel = ctx.channel();
/* 101 */     this.address = this.channel.remoteAddress();
/* 102 */     if (this.delayedDisconnect != null)
/* 103 */       disconnect(this.delayedDisconnect); 
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) {
/* 109 */     disconnect((Component)Component.translatable("disconnect.endOfStream"));
/*     */   }
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
/* 114 */     if (cause instanceof SkipPacketException) {
/* 115 */       LOGGER.debug("Skipping packet due to errors", cause.getCause());
/*     */       return;
/*     */     } 
/* 119 */     boolean isFirstFault = !this.handlingFault;
/* 120 */     this.handlingFault = true;
/* 122 */     if (!this.channel.isOpen())
/*     */       return; 
/* 126 */     if (cause instanceof io.netty.handler.timeout.TimeoutException) {
/* 127 */       LOGGER.debug("Timeout", cause);
/* 128 */       disconnect((Component)Component.translatable("disconnect.timeout"));
/*     */     } else {
/*     */       DisconnectionDetails details;
/* 130 */       MutableComponent mutableComponent = Component.translatable("disconnect.genericReason", new Object[] { "Internal Exception: " + String.valueOf(cause) });
/* 133 */       PacketListener listener = this.packetListener;
/* 134 */       if (listener != null) {
/* 135 */         details = listener.createDisconnectionInfo((Component)mutableComponent, cause);
/*     */       } else {
/* 137 */         details = new DisconnectionDetails((Component)mutableComponent);
/*     */       } 
/* 140 */       if (isFirstFault) {
/* 141 */         LOGGER.debug("Failed to sent packet", cause);
/* 142 */         if (getSending() == PacketFlow.CLIENTBOUND) {
/* 143 */           Packet<?> packet = this.sendLoginDisconnect ? (Packet<?>)new ClientboundLoginDisconnectPacket((Component)mutableComponent) : (Packet<?>)new ClientboundDisconnectPacket((Component)mutableComponent);
/* 144 */           send(packet, PacketSendListener.thenRun(() -> disconnect(details)));
/*     */         } else {
/* 146 */           disconnect(details);
/*     */         } 
/* 148 */         setReadOnly();
/*     */       } else {
/* 150 */         LOGGER.debug("Double fault", cause);
/* 151 */         disconnect(details);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void channelRead0(ChannelHandlerContext ctx, Packet<?> packet) {
/* 158 */     if (!this.channel.isOpen())
/*     */       return; 
/* 161 */     PacketListener packetListener = this.packetListener;
/* 162 */     if (packetListener == null)
/* 163 */       throw new IllegalStateException("Received a packet before the packet listener was initialized"); 
/* 165 */     if (packetListener.shouldHandleMessage(packet)) {
/*     */       try {
/* 167 */         genericsFtw(packet, packetListener);
/* 168 */       } catch (RunningOnDifferentThreadException runningOnDifferentThreadException) {
/*     */       
/* 169 */       } catch (RejectedExecutionException ignored) {
/* 170 */         disconnect((Component)Component.translatable("multiplayer.disconnect.server_shutdown"));
/* 171 */       } catch (ClassCastException exception) {
/* 173 */         LOGGER.error("Received {} that couldn't be processed", packet.getClass(), exception);
/* 174 */         disconnect((Component)Component.translatable("multiplayer.disconnect.invalid_packet"));
/*     */       } 
/* 176 */       this.receivedPackets++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T extends PacketListener> void genericsFtw(Packet<T> packet, PacketListener listener) {
/* 182 */     packet.handle(listener);
/*     */   }
/*     */   
/*     */   private void validateListener(ProtocolInfo<?> protocol, PacketListener packetListener) {
/* 186 */     Objects.requireNonNull(packetListener, "packetListener");
/* 187 */     PacketFlow listenerFlow = packetListener.flow();
/* 188 */     if (listenerFlow != this.receiving)
/* 189 */       throw new IllegalStateException("Trying to set listener for wrong side: connection is " + String.valueOf(this.receiving) + ", but listener is " + String.valueOf(listenerFlow)); 
/* 191 */     ConnectionProtocol listenerProtocol = packetListener.protocol();
/* 192 */     if (protocol.id() != listenerProtocol)
/* 193 */       throw new IllegalStateException("Listener protocol (" + String.valueOf(listenerProtocol) + ") does not match requested one " + String.valueOf(protocol)); 
/*     */   }
/*     */   
/*     */   private static void syncAfterConfigurationChange(ChannelFuture future) {
/*     */     try {
/* 201 */       future.syncUninterruptibly();
/* 202 */     } catch (Exception e) {
/* 204 */       if (e instanceof java.nio.channels.ClosedChannelException) {
/* 205 */         LOGGER.info("Connection closed during protocol change");
/*     */         return;
/*     */       } 
/* 208 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public <T extends PacketListener> void setupInboundProtocol(ProtocolInfo<T> protocol, T packetListener) {
/* 213 */     validateListener(protocol, (PacketListener)packetListener);
/* 214 */     if (protocol.flow() != getReceiving())
/* 215 */       throw new IllegalStateException("Invalid inbound protocol: " + String.valueOf(protocol.id())); 
/* 218 */     this.packetListener = (PacketListener)packetListener;
/* 219 */     this.disconnectListener = null;
/* 221 */     UnconfiguredPipelineHandler.InboundConfigurationTask configMessage = UnconfiguredPipelineHandler.setupInboundProtocol(protocol);
/* 223 */     BundlerInfo bundlerInfo = protocol.bundlerInfo();
/* 224 */     if (bundlerInfo != null) {
/* 225 */       PacketBundlePacker newBundler = new PacketBundlePacker(bundlerInfo);
/* 226 */       configMessage = configMessage.andThen(ctx -> ctx.pipeline().addAfter("decoder", "bundler", (ChannelHandler)newBundler));
/*     */     } 
/* 229 */     syncAfterConfigurationChange(this.channel.writeAndFlush(configMessage));
/*     */   }
/*     */   
/*     */   public void setupOutboundProtocol(ProtocolInfo<?> protocol) {
/* 233 */     if (protocol.flow() != getSending())
/* 234 */       throw new IllegalStateException("Invalid outbound protocol: " + String.valueOf(protocol.id())); 
/* 237 */     UnconfiguredPipelineHandler.OutboundConfigurationTask configMessage = UnconfiguredPipelineHandler.setupOutboundProtocol(protocol);
/* 239 */     BundlerInfo bundlerInfo = protocol.bundlerInfo();
/* 240 */     if (bundlerInfo != null) {
/* 241 */       PacketBundleUnpacker newUnbundler = new PacketBundleUnpacker(bundlerInfo);
/* 242 */       configMessage = configMessage.andThen(ctx -> ctx.pipeline().addAfter("encoder", "unbundler", (ChannelHandler)newUnbundler));
/*     */     } 
/* 245 */     boolean isLoginProtocol = (protocol.id() == ConnectionProtocol.LOGIN);
/* 246 */     syncAfterConfigurationChange(this.channel.writeAndFlush(configMessage.andThen(ctx -> this.sendLoginDisconnect = isLoginProtocol)));
/*     */   }
/*     */   
/*     */   public void setListenerForServerboundHandshake(PacketListener packetListener) {
/* 250 */     if (this.packetListener != null)
/* 251 */       throw new IllegalStateException("Listener already set"); 
/* 253 */     if (this.receiving != PacketFlow.SERVERBOUND || packetListener
/* 254 */       .flow() != PacketFlow.SERVERBOUND || packetListener
/* 255 */       .protocol() != INITIAL_PROTOCOL.id())
/* 257 */       throw new IllegalStateException("Invalid initial listener"); 
/* 259 */     this.packetListener = packetListener;
/*     */   }
/*     */   
/*     */   public void initiateServerboundStatusConnection(String hostName, int port, ClientStatusPacketListener listener) {
/* 263 */     initiateServerboundConnection(hostName, port, StatusProtocols.SERVERBOUND, StatusProtocols.CLIENTBOUND, listener, ClientIntent.STATUS);
/*     */   }
/*     */   
/*     */   public void initiateServerboundPlayConnection(String hostName, int port, ClientLoginPacketListener listener) {
/* 267 */     initiateServerboundConnection(hostName, port, LoginProtocols.SERVERBOUND, LoginProtocols.CLIENTBOUND, listener, ClientIntent.LOGIN);
/*     */   }
/*     */   
/*     */   public <S extends ServerboundPacketListener, C extends ClientboundPacketListener> void initiateServerboundPlayConnection(String hostName, int port, ProtocolInfo<S> outbound, ProtocolInfo<C> inbound, C listener, boolean transfer) {
/* 271 */     initiateServerboundConnection(hostName, port, outbound, inbound, listener, transfer ? ClientIntent.TRANSFER : ClientIntent.LOGIN);
/*     */   }
/*     */   
/*     */   private <S extends ServerboundPacketListener, C extends ClientboundPacketListener> void initiateServerboundConnection(String hostName, int port, ProtocolInfo<S> outbound, ProtocolInfo<C> inbound, C listener, ClientIntent intent) {
/* 275 */     if (outbound.id() != inbound.id())
/* 276 */       throw new IllegalStateException("Mismatched initial protocols"); 
/* 279 */     this.disconnectListener = (PacketListener)listener;
/* 280 */     runOnceConnected(connection -> {
/*     */           setupInboundProtocol(inbound, listener);
/*     */           connection.sendPacket((Packet<?>)new ClientIntentionPacket(SharedConstants.getCurrentVersion().protocolVersion(), hostName, port, intent), null, true);
/*     */           setupOutboundProtocol(outbound);
/*     */         });
/*     */   }
/*     */   
/*     */   public void send(Packet<?> packet) {
/* 289 */     send(packet, null);
/*     */   }
/*     */   
/*     */   public void send(Packet<?> packet, ChannelFutureListener listener) {
/* 293 */     send(packet, listener, true);
/*     */   }
/*     */   
/*     */   public void send(Packet<?> packet, ChannelFutureListener listener, boolean flush) {
/* 297 */     if (isConnected()) {
/* 298 */       flushQueue();
/* 299 */       sendPacket(packet, listener, flush);
/*     */     } else {
/* 301 */       this.pendingActions.add(connection -> connection.sendPacket(packet, listener, flush));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void runOnceConnected(Consumer<Connection> action) {
/* 306 */     if (isConnected()) {
/* 307 */       flushQueue();
/* 308 */       action.accept(this);
/*     */     } else {
/* 310 */       this.pendingActions.add(action);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sendPacket(Packet<?> packet, ChannelFutureListener listener, boolean flush) {
/* 315 */     this.sentPackets++;
/* 316 */     if (this.channel.eventLoop().inEventLoop()) {
/* 317 */       doSendPacket(packet, listener, flush);
/*     */     } else {
/* 319 */       this.channel.eventLoop().execute(() -> doSendPacket(packet, listener, flush));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doSendPacket(Packet<?> packet, ChannelFutureListener listener, boolean flush) {
/* 324 */     if (listener != null) {
/* 325 */       ChannelFuture future = flush ? this.channel.writeAndFlush(packet) : this.channel.write(packet);
/* 326 */       future.addListener((GenericFutureListener)listener);
/* 328 */     } else if (flush) {
/* 329 */       this.channel.writeAndFlush(packet, this.channel.voidPromise());
/*     */     } else {
/* 331 */       this.channel.write(packet, this.channel.voidPromise());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void flushChannel() {
/* 337 */     if (isConnected()) {
/* 338 */       flush();
/*     */     } else {
/* 340 */       this.pendingActions.add(Connection::flush);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void flush() {
/* 345 */     if (this.channel.eventLoop().inEventLoop()) {
/* 346 */       this.channel.flush();
/*     */     } else {
/* 348 */       this.channel.eventLoop().execute(() -> this.channel.flush());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void flushQueue() {
/* 353 */     if (this.channel == null || !this.channel.isOpen())
/*     */       return; 
/* 358 */     synchronized (this.pendingActions) {
/*     */       Consumer<Connection> pendingAction;
/* 360 */       while ((pendingAction = this.pendingActions.poll()) != null)
/* 361 */         pendingAction.accept(this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 367 */     flushQueue();
/* 369 */     PacketListener packetListener = this.packetListener;
/* 369 */     if (packetListener instanceof TickablePacketListener) {
/* 369 */       TickablePacketListener tickable = (TickablePacketListener)packetListener;
/* 370 */       tickable.tick();
/*     */     } 
/* 373 */     if (!isConnected() && !this.disconnectionHandled)
/* 374 */       handleDisconnection(); 
/* 377 */     if (this.channel != null)
/* 378 */       this.channel.flush(); 
/* 381 */     if (this.tickCount++ % 20 == 0)
/* 382 */       tickSecond(); 
/* 384 */     if (this.bandwidthDebugMonitor != null)
/* 385 */       this.bandwidthDebugMonitor.tick(); 
/*     */   }
/*     */   
/*     */   protected void tickSecond() {
/* 390 */     this.averageSentPackets = Mth.lerp(0.75F, this.sentPackets, this.averageSentPackets);
/* 391 */     this.averageReceivedPackets = Mth.lerp(0.75F, this.receivedPackets, this.averageReceivedPackets);
/* 392 */     this.sentPackets = 0;
/* 393 */     this.receivedPackets = 0;
/*     */   }
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 397 */     return this.address;
/*     */   }
/*     */   
/*     */   public String getLoggableAddress(boolean logIPs) {
/* 401 */     if (this.address == null)
/* 402 */       return "local"; 
/* 404 */     if (logIPs)
/* 405 */       return this.address.toString(); 
/* 407 */     return "IP hidden";
/*     */   }
/*     */   
/*     */   public void disconnect(Component reason) {
/* 411 */     disconnect(new DisconnectionDetails(reason));
/*     */   }
/*     */   
/*     */   public void disconnect(DisconnectionDetails details) {
/* 415 */     if (this.channel == null)
/* 416 */       this.delayedDisconnect = details; 
/* 418 */     if (isConnected()) {
/* 419 */       this.channel.close().awaitUninterruptibly();
/* 421 */       this.disconnectionDetails = details;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isMemoryConnection() {
/* 426 */     return (this.channel instanceof io.netty.channel.local.LocalChannel || this.channel instanceof io.netty.channel.local.LocalServerChannel);
/*     */   }
/*     */   
/*     */   public PacketFlow getReceiving() {
/* 430 */     return this.receiving;
/*     */   }
/*     */   
/*     */   public PacketFlow getSending() {
/* 434 */     return this.receiving.getOpposite();
/*     */   }
/*     */   
/*     */   public static Connection connectToServer(InetSocketAddress address, EventLoopGroupHolder eventLoopGroupHolder, LocalSampleLogger bandwidthLogger) {
/* 438 */     Connection connection = new Connection(PacketFlow.CLIENTBOUND);
/* 440 */     if (bandwidthLogger != null)
/* 441 */       connection.setBandwidthLogger(bandwidthLogger); 
/* 443 */     ChannelFuture connect = connect(address, eventLoopGroupHolder, connection);
/* 444 */     connect.syncUninterruptibly();
/* 446 */     return connection;
/*     */   }
/*     */   
/*     */   public static ChannelFuture connect(InetSocketAddress address, EventLoopGroupHolder eventLoopGroupHolder, final Connection connection) {
/* 450 */     return ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group(eventLoopGroupHolder.eventLoopGroup())).handler((ChannelHandler)new ChannelInitializer<Channel>() {
/*     */           protected void initChannel(Channel channel) {
/*     */             try {
/* 454 */               channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/* 455 */             } catch (ChannelException channelException) {}
/* 459 */             ChannelPipeline pipeline = channel.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30));
/* 461 */             Connection.configureSerialization(pipeline, PacketFlow.CLIENTBOUND, false, connection.bandwidthDebugMonitor);
/* 463 */             connection.configurePacketHandler(pipeline);
/*     */           }
/* 465 */         })).channel(eventLoopGroupHolder.channelCls())).connect(address.getAddress(), address.getPort());
/*     */   }
/*     */   
/*     */   private static String outboundHandlerName(boolean configureOutbound) {
/* 469 */     return configureOutbound ? "encoder" : "outbound_config";
/*     */   }
/*     */   
/*     */   private static String inboundHandlerName(boolean configureInbound) {
/* 473 */     return configureInbound ? "decoder" : "inbound_config";
/*     */   }
/*     */   
/*     */   public void configurePacketHandler(ChannelPipeline pipeline) {
/* 477 */     pipeline
/* 478 */       .addLast("hackfix", (ChannelHandler)new ChannelOutboundHandlerAdapter(this) {
/*     */           public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 489 */             super.write(ctx, msg, promise);
/*     */           }
/* 492 */         }).addLast("packet_handler", (ChannelHandler)this);
/*     */   }
/*     */   
/*     */   public static void configureSerialization(ChannelPipeline pipeline, PacketFlow inboundDirection, boolean local, BandwidthDebugMonitor monitor) {
/* 496 */     PacketFlow outboundDirection = inboundDirection.getOpposite();
/* 499 */     boolean configureInbound = (inboundDirection == PacketFlow.SERVERBOUND);
/* 500 */     boolean configureOutbound = (outboundDirection == PacketFlow.SERVERBOUND);
/* 504 */     pipeline
/*     */       
/* 506 */       .addLast("splitter", (ChannelHandler)createFrameDecoder(monitor, local))
/*     */       
/* 511 */       .addLast(new ChannelHandler[] { (ChannelHandler)new FlowControlHandler() }).addLast(inboundHandlerName(configureInbound), configureInbound ? (ChannelHandler)new PacketDecoder<>(INITIAL_PROTOCOL) : (ChannelHandler)new UnconfiguredPipelineHandler.Inbound())
/*     */       
/* 516 */       .addLast("prepender", (ChannelHandler)createFrameEncoder(local))
/*     */       
/* 519 */       .addLast(outboundHandlerName(configureOutbound), configureOutbound ? (ChannelHandler)new PacketEncoder<>(INITIAL_PROTOCOL) : (ChannelHandler)new UnconfiguredPipelineHandler.Outbound());
/*     */   }
/*     */   
/*     */   private static ChannelOutboundHandler createFrameEncoder(boolean local) {
/* 524 */     return local ? (ChannelOutboundHandler)new LocalFrameEncoder() : (ChannelOutboundHandler)new Varint21LengthFieldPrepender();
/*     */   }
/*     */   
/*     */   private static ChannelInboundHandler createFrameDecoder(BandwidthDebugMonitor monitor, boolean local) {
/* 528 */     if (!local)
/* 529 */       return (ChannelInboundHandler)new Varint21FrameDecoder(monitor); 
/* 532 */     if (monitor != null)
/* 533 */       return (ChannelInboundHandler)new MonitoredLocalFrameDecoder(monitor); 
/* 536 */     return (ChannelInboundHandler)new LocalFrameDecoder();
/*     */   }
/*     */   
/*     */   public static void configureInMemoryPipeline(ChannelPipeline pipeline, PacketFlow packetFlow) {
/* 540 */     configureSerialization(pipeline, packetFlow, true, null);
/*     */   }
/*     */   
/*     */   public static Connection connectToLocalServer(SocketAddress address) {
/* 544 */     final Connection connection = new Connection(PacketFlow.CLIENTBOUND);
/* 546 */     ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group(EventLoopGroupHolder.local().eventLoopGroup())).handler((ChannelHandler)new ChannelInitializer<Channel>() {
/*     */           protected void initChannel(Channel channel) {
/* 549 */             ChannelPipeline pipeline = channel.pipeline();
/* 550 */             Connection.configureInMemoryPipeline(pipeline, PacketFlow.CLIENTBOUND);
/* 551 */             connection.configurePacketHandler(pipeline);
/*     */           }
/* 553 */         })).channel(EventLoopGroupHolder.local().channelCls())).connect(address).syncUninterruptibly();
/* 555 */     return connection;
/*     */   }
/*     */   
/*     */   public static Connection fromChannel(Channel channel, PacketFlow flow, LocalSampleLogger bandwidthLogger) {
/* 559 */     Connection connection = new Connection(flow);
/* 560 */     if (bandwidthLogger != null)
/* 561 */       connection.setBandwidthLogger(bandwidthLogger); 
/* 565 */     ChannelPipeline pipeline = channel.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30));
/* 566 */     configureSerialization(pipeline, flow, false, connection.bandwidthDebugMonitor);
/* 567 */     connection.configurePacketHandler(pipeline);
/* 569 */     EventLoopGroupHolder.local().eventLoopGroup().register(channel).syncUninterruptibly();
/* 571 */     return connection;
/*     */   }
/*     */   
/*     */   public void setEncryptionKey(Cipher decryptCipher, Cipher encryptCipher) {
/* 575 */     this.channel.pipeline().addBefore("splitter", "decrypt", (ChannelHandler)new CipherDecoder(decryptCipher));
/* 576 */     this.channel.pipeline().addBefore("prepender", "encrypt", (ChannelHandler)new CipherEncoder(encryptCipher));
/*     */   }
/*     */   
/*     */   public boolean isConnected() {
/* 580 */     return (this.channel != null && this.channel.isOpen());
/*     */   }
/*     */   
/*     */   public boolean isConnecting() {
/* 584 */     return (this.channel == null);
/*     */   }
/*     */   
/*     */   public PacketListener getPacketListener() {
/* 588 */     return this.packetListener;
/*     */   }
/*     */   
/*     */   public DisconnectionDetails getDisconnectionDetails() {
/* 592 */     return this.disconnectionDetails;
/*     */   }
/*     */   
/*     */   public void setReadOnly() {
/* 596 */     if (this.channel != null)
/* 597 */       this.channel.config().setAutoRead(false); 
/*     */   }
/*     */   
/*     */   public void setupCompression(int threshold, boolean validateDecompressed) {
/* 602 */     if (threshold >= 0) {
/* 603 */       ChannelHandler channelHandler = this.channel.pipeline().get("decompress");
/* 603 */       if (channelHandler instanceof CompressionDecoder) {
/* 603 */         CompressionDecoder compressionDecoder = (CompressionDecoder)channelHandler;
/* 604 */         compressionDecoder.setThreshold(threshold, validateDecompressed);
/*     */       } else {
/* 606 */         this.channel.pipeline().addAfter("splitter", "decompress", (ChannelHandler)new CompressionDecoder(threshold, validateDecompressed));
/*     */       } 
/* 609 */       channelHandler = this.channel.pipeline().get("compress");
/* 609 */       if (channelHandler instanceof CompressionEncoder) {
/* 609 */         CompressionEncoder compressionEncoder = (CompressionEncoder)channelHandler;
/* 610 */         compressionEncoder.setThreshold(threshold);
/*     */       } else {
/* 612 */         this.channel.pipeline().addAfter("prepender", "compress", (ChannelHandler)new CompressionEncoder(threshold));
/*     */       } 
/*     */     } else {
/* 615 */       if (this.channel.pipeline().get("decompress") instanceof CompressionDecoder)
/* 616 */         this.channel.pipeline().remove("decompress"); 
/* 619 */       if (this.channel.pipeline().get("compress") instanceof CompressionEncoder)
/* 620 */         this.channel.pipeline().remove("compress"); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleDisconnection() {
/* 626 */     if (this.channel == null || this.channel.isOpen())
/*     */       return; 
/* 630 */     if (this.disconnectionHandled) {
/* 631 */       LOGGER.warn("handleDisconnection() called twice");
/*     */       return;
/*     */     } 
/* 635 */     this.disconnectionHandled = true;
/* 636 */     PacketListener packetListener = getPacketListener();
/* 637 */     PacketListener disconnectListener = (packetListener != null) ? packetListener : this.disconnectListener;
/* 638 */     if (disconnectListener != null) {
/* 639 */       DisconnectionDetails details = Objects.<DisconnectionDetails>requireNonNullElseGet(getDisconnectionDetails(), () -> new DisconnectionDetails((Component)Component.translatable("multiplayer.disconnect.generic")));
/* 640 */       disconnectListener.onDisconnect(details);
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getAverageReceivedPackets() {
/* 645 */     return this.averageReceivedPackets;
/*     */   }
/*     */   
/*     */   public float getAverageSentPackets() {
/* 649 */     return this.averageSentPackets;
/*     */   }
/*     */   
/*     */   public void setBandwidthLogger(LocalSampleLogger bandwidthLogger) {
/* 653 */     this.bandwidthDebugMonitor = new BandwidthDebugMonitor(bandwidthLogger);
/*     */   }
/*     */   
/*     */   public void setIntendedProfileId(UUID profileId) {
/* 657 */     this.intendedProfileId = profileId;
/*     */   }
/*     */   
/*     */   public UUID getIntendedProfileId() {
/* 661 */     return this.intendedProfileId;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\Connection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */