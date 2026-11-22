/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.DisconnectionDetails;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.PacketSendListener;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketUtils;
/*     */ import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
/*     */ import net.minecraft.network.protocol.common.ServerCommonPacketListener;
/*     */ import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundPongPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
/*     */ import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ClientInformation;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class ServerCommonPacketListenerImpl implements ServerCommonPacketListener {
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int LATENCY_CHECK_INTERVAL = 15000;
/*     */   
/*     */   private static final int CLOSED_LISTENER_TIMEOUT = 15000;
/*     */   
/*  38 */   private static final Component TIMEOUT_DISCONNECTION_MESSAGE = (Component)Component.translatable("disconnect.timeout");
/*     */   
/*  40 */   static final Component DISCONNECT_UNEXPECTED_QUERY = (Component)Component.translatable("multiplayer.disconnect.unexpected_query_response");
/*     */   
/*     */   protected final MinecraftServer server;
/*     */   
/*     */   protected final Connection connection;
/*     */   
/*     */   private final boolean transferred;
/*     */   
/*     */   private long keepAliveTime;
/*     */   
/*     */   private boolean keepAlivePending;
/*     */   
/*     */   private long keepAliveChallenge;
/*     */   
/*     */   private long closedListenerTime;
/*     */   
/*     */   private boolean closed = false;
/*     */   
/*     */   private int latency;
/*     */   
/*     */   private volatile boolean suspendFlushingOnServerThread = false;
/*     */   
/*     */   public ServerCommonPacketListenerImpl(MinecraftServer server, Connection connection, CommonListenerCookie cookie) {
/*  55 */     this.server = server;
/*  56 */     this.connection = connection;
/*  57 */     this.keepAliveTime = Util.getMillis();
/*  58 */     this.latency = cookie.latency();
/*  59 */     this.transferred = cookie.transferred();
/*     */   }
/*     */   
/*     */   private void close() {
/*  63 */     if (!this.closed) {
/*  64 */       this.closedListenerTime = Util.getMillis();
/*  65 */       this.closed = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onDisconnect(DisconnectionDetails details) {
/*  71 */     if (isSingleplayerOwner()) {
/*  72 */       LOGGER.info("Stopping singleplayer server as player logged out");
/*  73 */       this.server.halt(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onPacketError(Packet packet, Exception e) throws ReportedException {
/*  79 */     super.onPacketError(packet, e);
/*  80 */     this.server.reportPacketHandlingException(e, packet.type());
/*     */   }
/*     */   
/*     */   public void handleKeepAlive(ServerboundKeepAlivePacket packet) {
/*  85 */     if (this.keepAlivePending && packet.getId() == this.keepAliveChallenge) {
/*  86 */       int time = (int)(Util.getMillis() - this.keepAliveTime);
/*  87 */       this.latency = (this.latency * 3 + time) / 4;
/*  88 */       this.keepAlivePending = false;
/*  90 */     } else if (!isSingleplayerOwner()) {
/*  91 */       disconnect(TIMEOUT_DISCONNECTION_MESSAGE);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handlePong(ServerboundPongPacket serverboundPongPacket) {}
/*     */   
/*     */   public void handleCustomPayload(ServerboundCustomPayloadPacket packet) {}
/*     */   
/*     */   public void handleCustomClickAction(ServerboundCustomClickActionPacket packet) {
/* 106 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.server.packetProcessor());
/* 107 */     this.server.handleCustomClickAction(packet.id(), packet.payload());
/*     */   }
/*     */   
/*     */   public void handleResourcePackResponse(ServerboundResourcePackPacket packet) {
/* 112 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.server.packetProcessor());
/* 113 */     if (packet.action() == ServerboundResourcePackPacket.Action.DECLINED && this.server.isResourcePackRequired()) {
/* 114 */       LOGGER.info("Disconnecting {} due to resource pack {} rejection", playerProfile().name(), packet.id());
/* 115 */       disconnect((Component)Component.translatable("multiplayer.requiredTexturePrompt.disconnect"));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleCookieResponse(ServerboundCookieResponsePacket packet) {
/* 121 */     disconnect(DISCONNECT_UNEXPECTED_QUERY);
/*     */   }
/*     */   
/*     */   protected void keepConnectionAlive() {
/* 125 */     Profiler.get().push("keepAlive");
/* 126 */     long now = Util.getMillis();
/* 127 */     if (!isSingleplayerOwner() && now - this.keepAliveTime >= 15000L)
/* 128 */       if (this.keepAlivePending) {
/* 129 */         disconnect(TIMEOUT_DISCONNECTION_MESSAGE);
/* 130 */       } else if (checkIfClosed(now)) {
/* 131 */         this.keepAlivePending = true;
/* 132 */         this.keepAliveTime = now;
/* 133 */         this.keepAliveChallenge = now;
/* 134 */         send((Packet<?>)new ClientboundKeepAlivePacket(this.keepAliveChallenge));
/*     */       }  
/* 137 */     Profiler.get().pop();
/*     */   }
/*     */   
/*     */   private boolean checkIfClosed(long now) {
/* 141 */     if (this.closed) {
/* 142 */       if (now - this.closedListenerTime >= 15000L)
/* 143 */         disconnect(TIMEOUT_DISCONNECTION_MESSAGE); 
/* 145 */       return false;
/*     */     } 
/* 147 */     return true;
/*     */   }
/*     */   
/*     */   public void suspendFlushing() {
/* 151 */     this.suspendFlushingOnServerThread = true;
/*     */   }
/*     */   
/*     */   public void resumeFlushing() {
/* 155 */     this.suspendFlushingOnServerThread = false;
/* 156 */     this.connection.flushChannel();
/*     */   }
/*     */   
/*     */   public void send(Packet<?> packet) {
/* 160 */     send(packet, null);
/*     */   }
/*     */   
/*     */   public void send(Packet<?> packet, ChannelFutureListener listener) {
/* 164 */     if (packet.isTerminal())
/* 165 */       close(); 
/* 167 */     boolean flush = (!this.suspendFlushingOnServerThread || !this.server.isSameThread());
/*     */     try {
/* 169 */       this.connection.send(packet, listener, flush);
/* 170 */     } catch (Throwable t) {
/* 171 */       CrashReport report = CrashReport.forThrowable(t, "Sending packet");
/* 172 */       CrashReportCategory category = report.addCategory("Packet being sent");
/* 174 */       category.setDetail("Packet class", () -> packet.getClass().getCanonicalName());
/* 176 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void disconnect(Component reason) {
/* 181 */     disconnect(new DisconnectionDetails(reason));
/*     */   }
/*     */   
/*     */   public void disconnect(DisconnectionDetails details) {
/* 185 */     this.connection.send((Packet)new ClientboundDisconnectPacket(details.reason()), PacketSendListener.thenRun(() -> this.connection.disconnect(details)));
/* 186 */     this.connection.setReadOnly();
/* 187 */     Objects.requireNonNull(this.connection);
/* 187 */     this.server.executeBlocking(this.connection::handleDisconnection);
/*     */   }
/*     */   
/*     */   protected boolean isSingleplayerOwner() {
/* 191 */     return this.server.isSingleplayerOwner(new NameAndId(playerProfile()));
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public GameProfile getOwner() {
/* 198 */     return playerProfile();
/*     */   }
/*     */   
/*     */   public int latency() {
/* 203 */     return this.latency;
/*     */   }
/*     */   
/*     */   protected CommonListenerCookie createCookie(ClientInformation clientInformation) {
/* 207 */     return new CommonListenerCookie(
/* 208 */         playerProfile(), this.latency, clientInformation, this.transferred);
/*     */   }
/*     */   
/*     */   protected abstract GameProfile playerProfile();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\ServerCommonPacketListenerImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */